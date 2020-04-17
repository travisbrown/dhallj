package org.dhallj.imports

import java.net.URI
import java.nio.file.Path
import java.security.MessageDigest

import cats.effect.Sync
import cats.implicits._
import org.dhallj.cats.LiftVisitor
import org.dhallj.core.DhallException.ResolutionFailure
import org.dhallj.core.Expr.ImportMode
import org.dhallj.core.Expr.Util.typeCheck
import org.dhallj.core._
import org.dhallj.core.binary.Decode
import org.dhallj.imports.Caching.ImportsCache
import org.dhallj.imports.Canonicalization.canonicalize
import org.dhallj.imports.ResolveImportsVisitor._
import org.dhallj.parser.DhallParser
import org.http4s.Status.Successful
import org.http4s.Uri.unsafeFromString
import org.http4s.client.Client
import org.http4s.{EntityDecoder, Headers, Request}

import scala.collection.mutable.{Map => MMap}

//TODO proper error handling
private[dhallj] case class ResolveImportsVisitor[F[_] <: AnyRef](cache: ImportsCache[F], parents: List[ImportContext])(
  implicit Client: Client[F],
  F: Sync[F]
) extends LiftVisitor[F](F) {
  private var duplicateImportsCache: MMap[ImportContext, String] = MMap.empty

  override def onOperatorApplication(operator: Operator, lhs: F[Expr], rhs: F[Expr]): F[Expr] =
    if (operator == Operator.IMPORT_ALT)
      lhs.handleErrorWith(_ => rhs)
    else {
      F.map2(lhs, rhs)(Expr.makeOperatorApplication(operator, _, _))
    }

  override def onLocalImport(path: Path, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    onImport(Local(path), mode, hash)

  override def onClasspathImport(path: Path, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    onImport(Classpath(path), mode, hash)

  override def onRemoteImport(url: URI, using: F[Expr], mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    if (using.ne(null)) using >>= (u => onImport(Remote(url, u), mode, hash))
    else onImport(Remote(url, null), mode, hash)

  override def onEnvImport(value: String, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    onImport(Env(value), mode, hash)

  override def onMissingImport(mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    onImport(Missing, mode, hash)

  private def onImport(i: ImportContext, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] = {

    //TODO replicate Dhall Haskell flow
    //Compute chain
    //Referential sanity check
    //Check cyclic imports
    //Resolve import - results in fully resolved, type-checked and normalized expr
    //    Check in memory cache
    //    Load with semantic cache >=> put in in-memory cache
    //Load with semantic  cache
    //    If hash present and in cache check bytes hash and return as fully resolved and normalized expr
    //    Else
    //        load expr from semi-semantic cache
    //        alpha-normalize expr (why?!)
    //        check that hash of expr matches expected
    //        return expr
    //Load with semi-semantic cache (as code)
    //    Actually resolve import as text
    //    Parse expr
    //    Resolve expr
    //    Compute semi-semantic hash of resolved expr
    //    if present in semisemantic cache
    //        return resolved, type checked normalized expr
    //    Else
    //        substitute expr
    //        typecheck expr with empty context
    //        beta-normalize expr
    //        encode and write to semi-semantic cache
    //        return expr
    //Load with semi-semantic cache (as text)
    //    Resolve expr
    //Load with semi-semantic cache (as code)
    //    Create location expr
    //Compute semi-semantic hash
    //    Take expr with all imports type-checked and normalized
    //    Encode and hash it

    def resolve(i: ImportContext, mode: Expr.ImportMode, hash: Array[Byte]): F[(Expr, Headers)] = {

      def resolve(i: ImportContext, hash: Array[Byte]): F[(String, Headers)] = {

        def resolve(i: ImportContext): F[(String, Headers)] =
          for {
            cached <- F.delay(duplicateImportsCache.get(i))
            result <- cached match {
              case Some(v) => F.pure(v -> Headers.empty)
              case None =>
                for {
                  v <- i match {
                    case Env(value) =>
                      for {
                        vO <- F.delay(sys.env.get(value))
                        v <- vO.fold(
                          F.raiseError[String](new ResolutionFailure(s"Missing import - env import $value undefined"))
                        )(F.pure)
                      } yield v -> Headers.empty
                    case Local(path) =>
                      for {
                        v <- F.delay(scala.io.Source.fromFile(path.toString).mkString)
                      } yield v -> Headers.empty
                    case Classpath(path) =>
                      for {
                        v <- F.delay(
                          scala.io.Source.fromInputStream(getClass.getResourceAsStream(path.toString)).mkString
                        )
                      } yield v -> Headers.empty
                    case Remote(uri, using) =>
                      for {
                        headers <- F.pure(ToHeaders(using))
                        req <- F.pure(Request[F](uri = unsafeFromString(uri.toString), headers = headers))
                        resp <- Client.fetch[(String, Headers)](req) {
                          case Successful(resp) =>
                            for {
                              s <- EntityDecoder.decodeString(resp)
                            } yield s -> resp.headers
                          case _ =>
                            F.raiseError[(String, Headers)](
                              new ResolutionFailure(s"Missing import - cannot resolve $uri")
                            )
                        }
                      } yield resp
                    case Missing => F.raiseError(new ResolutionFailure("Missing import - cannot resolve missing"))
                  }
                  _ <- F.delay(duplicateImportsCache.put(i, v._1))
                } yield v
            }
          } yield result

        def checkHashesMatch(encoded: Array[Byte], expected: Array[Byte]): F[Unit] = {
          val hashed = MessageDigest.getInstance("SHA-256").digest(encoded)
          if (hashed.sameElements(expected)) F.unit
          else F.raiseError(new ResolutionFailure("Cached expression does not match its hash"))
        }

        if (hash eq null) resolve(i)
        else
          for {
            bytesO <- cache.get(hash)
            result <- bytesO.fold(resolve(i))(bs =>
              for {
                _ <- checkHashesMatch(bs, hash)
                r <- F.delay(Decode.decode(bs).toString() -> Headers.empty)
              } yield r
            )
          } yield result
      }

      mode match {
        case Expr.ImportMode.CODE =>
          for {
            v <- resolve(i, hash)
            (s, headers) = v
            e <- F.delay(DhallParser.parse(s))
          } yield e -> headers
        //TODO check if this can be interpolated? The spec isn't very clear
        case Expr.ImportMode.RAW_TEXT =>
          for {
            v <- resolve(i, hash)
            (s, headers) = v
            e <- F.pure(Expr.makeTextLiteral(s))
          } yield e -> headers
        case Expr.ImportMode.LOCATION =>
          F.raiseError(new ResolutionFailure("Importing as location should already have been handled"))
      }
    }

    //TODO check that equality is sensibly defined for URI and Path
    def rejectCyclicImports(imp: ImportContext, parents: List[ImportContext]): F[Unit] =
      if (parents.contains(imp))
        F.raiseError[Unit](new ResolutionFailure(s"Cyclic import - $imp is already imported in chain $parents"))
      else F.unit

    def validateHash(imp: ImportContext, e: Expr, expected: Array[Byte]): F[Unit] =
      if (expected eq null) F.unit
      else
        for {
          bytes <- F.pure(e.normalize().getEncodedBytes())
          encoded <- F.delay(MessageDigest.getInstance("SHA-256").digest(bytes))
          _ <- if (encoded.sameElements(expected)) F.unit
          else F.raiseError(new ResolutionFailure(s"SHA256 validation exception for ${imp}"))
          _ <- cache.put(encoded, bytes)
        } yield ()

    def importLocation(imp: ImportContext): F[Expr] = {
      def makeLocation(field: String, value: String): F[Expr] =
        F.pure(
          Expr.makeApplication(Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, field), Expr.makeTextLiteral(value))
        )

      imp match {
        case Local(path) => makeLocation("Local", path.toString)
        // Cannot support this and remain spec-compliant as result type must be <Local Text | Remote Text | Environment Text | Missing>
        case Classpath(path) =>
          F.raiseError(new ResolutionFailure("Importing classpath as location is not supported"))
        case Remote(uri, _) => makeLocation("Remote", uri.toString)
        case Env(value)     => makeLocation("Environment", value)
        case Missing        => F.pure(Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Missing"))
      }
    }

    def importNonLocation(imp: ImportContext, mode: ImportMode, hash: Array[Byte]) =
      for {
        _ <- if (parents.nonEmpty) ReferentialSanityCheck(parents.head, imp) else F.unit
        r <- resolve(imp, mode, hash)
        (e, headers) = r
        _ <- if (parents.nonEmpty) CORSComplianceCheck(parents.head, imp, headers) else F.unit
        //TODO do we need to do this based on sha256 instead or something instead? Although parents won't be fully resolved
        _ <- rejectCyclicImports(imp, parents)
        result <- {
          val v = ResolveImportsVisitor[F](cache, imp :: parents)
          v.duplicateImportsCache = this.duplicateImportsCache
          e.accept(v)
        }
        _ <- validateHash(imp, result, hash)
        _ <- F.delay(typeCheck(result))
      } yield result

    for {
      imp <- if (parents.isEmpty) canonicalize(i)
      else canonicalize(parents.head, i)
      result <- if (mode == ImportMode.LOCATION) importLocation(imp) else importNonLocation(imp, mode, hash)
    } yield result
  }
}

object ResolveImportsVisitor {

  def mkVisitor[F[_] <: AnyRef: Sync: Client]: F[ResolveImportsVisitor[F]] =
    Caching.mkImportsCache[F].map(c => mkVisitor(c))

  def mkVisitor[F[_] <: AnyRef: Sync: Client](cache: ImportsCache[F]): ResolveImportsVisitor[F] =
    ResolveImportsVisitor(cache, Nil)

  sealed trait ImportContext

  case class Env(value: String) extends ImportContext

  case class Local(absolutePath: Path) extends ImportContext

  case class Classpath(absolutePath: Path) extends ImportContext

  case class Remote(uri: URI, using: Expr) extends ImportContext

  case object Missing extends ImportContext

}
