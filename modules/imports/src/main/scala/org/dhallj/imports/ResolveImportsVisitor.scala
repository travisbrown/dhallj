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
import org.http4s.{EntityDecoder, Request}

import scala.collection.mutable.{Map => MMap}

//TODO proper error handling
private[dhallj] case class ResolveImportsVisitor[F[_] <: AnyRef](semanticCache: ImportsCache[F],
                                                                 semiSemanticCache: ImportsCache[F],
                                                                 parents: List[ImportContext])(
  implicit Client: Client[F],
  F: Sync[F]
) extends LiftVisitor[F](F) {
  private var duplicateImportsCache: MMap[ImportContext, Expr] = MMap.empty

  override def onOperatorApplication(operator: Operator, lhs: F[Expr], rhs: F[Expr]): F[Expr] =
    if (operator == Operator.IMPORT_ALT)
      lhs.handleErrorWith(_ => rhs)
    else {
      F.map2(lhs, rhs)(Expr.makeOperatorApplication(operator, _, _))
    }

  override def onLocalImport(path: Path, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    onImport(ImportContext.Local(path), mode, hash)

  override def onClasspathImport(path: Path, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    onImport(ImportContext.Classpath(path), mode, hash)

  override def onRemoteImport(url: URI, using: F[Expr], mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    if (using.ne(null)) using >>= (u => onImport(ImportContext.Remote(url, u), mode, hash))
    else onImport(ImportContext.Remote(url, null), mode, hash)

  override def onEnvImport(value: String, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    onImport(ImportContext.Env(value), mode, hash)

  override def onMissingImport(mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    onImport(ImportContext.Missing, mode, hash)

  private def onImport(i: ImportContext, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] = {
    //TODO check that equality is sensibly defined for URI and Path
    def rejectCyclicImports(imp: ImportContext, parents: List[ImportContext]): F[Unit] =
      if (parents.contains(imp))
        F.raiseError[Unit](new ResolutionFailure(s"Cyclic import - $imp is already imported in chain $parents"))
      else F.unit

    def importLocation(imp: ImportContext): F[Expr] =
      imp match {
        case ImportContext.Local(path) => makeLocation("Local", path.toString)
        // Cannot support this and remain spec-compliant as result type must be <Local Text | Remote Text | Environment Text | Missing>
        case ImportContext.Classpath(path) =>
          F.raiseError(new ResolutionFailure("Importing classpath as location is not supported"))
        case ImportContext.Remote(uri, _) => makeLocation("Remote", uri.toString)
        case ImportContext.Env(value)     => makeLocation("Environment", value)
        case ImportContext.Missing        => F.pure(Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Missing"))
      }

    def makeLocation(field: String, value: String): F[Expr] =
      F.pure(
        Expr.makeApplication(Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, field), Expr.makeTextLiteral(value))
      )

    def checkHashesMatch(encoded: Array[Byte], expected: Array[Byte]): F[Unit] = {
      val hashed = MessageDigest.getInstance("SHA-256").digest(encoded)
      if (hashed.sameElements(expected)) F.unit
      else F.raiseError(new ResolutionFailure("Cached expression does not match its hash"))
    }

    def loadWithSemanticCache(imp: ImportContext, mode: ImportMode, hash: Array[Byte]): F[Expr] =
      if (hash == null) loadWithSemiSemanticCache(imp, mode, hash)
      else
        for {
          cached <- semanticCache.get(hash)
          e <- cached match {
            case Some(bs) =>
              for {
                _ <- checkHashesMatch(bs, hash)
                e <- F.delay(Decode.decode(bs))
              } yield e
            case None =>
              for {
                e <- loadWithSemiSemanticCache(imp, mode, hash)
                bs = e.alphaNormalize.getEncodedBytes
                _ <- checkHashesMatch(bs, hash)
                _ <- semanticCache.put(hash, bs)
              } yield e
          }
        } yield e

    def fetch(imp: ImportContext): F[String] = imp match {
      case ImportContext.Env(value) =>
        for {
          vO <- F.delay(sys.env.get(value))
          v <- vO.fold(
            F.raiseError[String](new ResolutionFailure(s"Missing import - env import $value undefined"))
          )(F.pure)
        } yield v
      case ImportContext.Local(path) =>
        for {
          v <- F.delay(scala.io.Source.fromFile(path.toString).mkString)
        } yield v
      case ImportContext.Classpath(path) =>
        for {
          v <- F.delay(
            scala.io.Source.fromInputStream(getClass.getResourceAsStream(path.toString)).mkString
          )
        } yield v
      case ImportContext.Remote(uri, using) =>
        for {
          headers <- F.pure(ToHeaders(using))
          req <- F.pure(Request[F](uri = unsafeFromString(uri.toString), headers = headers))
          resp <- Client.fetch[String](req) {
            case Successful(resp) =>
              for {
                s <- EntityDecoder.decodeString(resp)
                _ <- if (parents.nonEmpty) CORSComplianceCheck(parents.head, imp, resp.headers) else F.unit
              } yield s
            case _ =>
              F.raiseError[String](
                new ResolutionFailure(s"Missing import - cannot resolve $uri")
              )
          }
        } yield resp
      case ImportContext.Missing => F.raiseError(new ResolutionFailure("Missing import - cannot resolve missing"))
    }

    def loadWithSemiSemanticCache(imp: ImportContext, mode: ImportMode, hash: Array[Byte]): F[Expr] = mode match {
      case ImportMode.LOCATION =>
        imp match {
          case ImportContext.Local(path) => makeLocation("Local", path.toString)
          // Cannot support this and remain spec-compliant as result type must be <Local Text | Remote Text | Environment Text | Missing>
          case ImportContext.Classpath(path) =>
            F.raiseError(new ResolutionFailure("Importing classpath as location is not supported"))
          case ImportContext.Remote(uri, _) => makeLocation("Remote", uri.toString)
          case ImportContext.Env(value)     => makeLocation("Environment", value)
          case ImportContext.Missing        => F.pure(Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Missing"))
        }
      case ImportMode.RAW_TEXT =>
        for {
          text <- fetch(imp)
        } yield Expr.makeTextLiteral(text)
      case ImportMode.CODE =>
        for {
          text <- fetch(imp)
          parsed <- F.delay(DhallParser.parse(text))
          resolved <- {
            val v = ResolveImportsVisitor[F](semanticCache, semiSemanticCache, imp :: parents)
            v.duplicateImportsCache = this.duplicateImportsCache
            parsed.accept(v)
          }
          semiHash = MessageDigest.getInstance("SHA-256").digest(resolved.getEncodedBytes)
          cached <- semiSemanticCache.get(semiHash)
          expr <- cached match {
            case Some(bs) => F.delay(Decode.decode(bs))
            case None =>
              for {
                _ <- F.delay(typeCheck(resolved))
                //TODO substitutions here?
                normalized <- F.delay(resolved.normalize)
                _ <- semiSemanticCache.put(semiHash, normalized.getEncodedBytes)
              } yield normalized
          }
        } yield expr
    }

    def resolve(imp: ImportContext, mode: ImportMode, hash: Array[Byte]): F[Expr] =
      if (duplicateImportsCache.contains(imp))
        F.delay(duplicateImportsCache.get(imp).get)
      else
        for {
          e <- loadWithSemanticCache(imp, mode, hash)
          _ <- F.delay(duplicateImportsCache.put(imp, e))
        } yield e

    def importNonLocation(imp: ImportContext, mode: ImportMode, hash: Array[Byte]) =
      for {
        _ <- if (parents.nonEmpty) ReferentialSanityCheck(parents.head, imp) else F.unit
        _ <- rejectCyclicImports(imp, parents)
        r <- resolve(imp, mode, hash)
      } yield r

    for {
      imp <- if (parents.isEmpty) canonicalize(i) else canonicalize(parents.head, i)
      result <- if (mode == ImportMode.LOCATION) importLocation(imp) else importNonLocation(imp, mode, hash)
    } yield result
  }
}

object ResolveImportsVisitor {

  def mkVisitor[F[_] <: AnyRef: Sync: Client]: F[ResolveImportsVisitor[F]] =
    (Caching.mkImportsCache[F]("dhall"), Caching.mkImportsCache[F]("dhallj")).mapN((c, c2) => mkVisitor(c, c2))

  def mkVisitor[F[_] <: AnyRef: Sync: Client](semanticCache: ImportsCache[F],
                                              semiSemanticCache: ImportsCache[F]): ResolveImportsVisitor[F] =
    ResolveImportsVisitor(semanticCache, semiSemanticCache, Nil)

}
