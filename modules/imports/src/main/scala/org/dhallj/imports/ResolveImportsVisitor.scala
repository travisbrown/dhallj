package org.dhallj.imports

import java.net.URI
import java.nio.file.{Files, Path, Paths}
import java.security.MessageDigest

import cats.Apply
import cats.data.NonEmptyList
import cats.effect.{Async, Concurrent}
import cats.implicits._
import org.dhallj.cats.LiftVisitor
import org.dhallj.core.DhallException.ResolutionFailure
import org.dhallj.core.Expr.ImportMode
import org.dhallj.core.Expr.Util.typeCheck
import org.dhallj.core._
import org.dhallj.core.binary.Decode
import org.dhallj.imports.Canonicalization.canonicalize
import org.dhallj.imports.ImportContext.Local
import org.dhallj.parser.DhallParser
import org.http4s.Status.Successful
import org.http4s.Uri.unsafeFromString
import org.http4s.client.Client
import org.http4s.{EntityDecoder, Request}

import scala.collection.mutable.{Map => MMap}

//TODO proper error handling
final private class ResolveImportsVisitor[F[_] <: AnyRef](
  semanticCache: ImportCache[F],
  semiSemanticCache: ImportCache[F],
  parents: NonEmptyList[ImportContext]
)(implicit
  Client: Client[F],
  F: Async[F]
) extends LiftVisitor[F](F, true) {
  def this(
    semanticCache: ImportCache[F],
    semiSemanticCache: ImportCache[F],
    parents: List[ImportContext]
  )(implicit Client: Client[F], F: Async[F]) = {
    this(semanticCache, semiSemanticCache, NonEmptyList.fromListUnsafe(parents))
  }

  private var duplicateImportCache: MMap[(ImportContext, ImportMode), Expr] = MMap.empty

  override def onOperatorApplication(operator: Operator, lhs: F[Expr], rhs: F[Expr]): F[Expr] =
    if (operator == Operator.IMPORT_ALT)
      lhs.handleErrorWith {
        case e: ResolutionFailure if e.isAbsentImport => rhs
        case other                                    => F.raiseError(other)
      }
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

  private[this] def checkHash(encoded: Array[Byte], expected: Array[Byte]): Boolean = {
    val hashed = MessageDigest.getInstance("SHA-256").digest(encoded)
    hashed.sameElements(expected)
  }

  private def onImport(i: ImportContext, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] = {
    // TODO check that equality is sensibly defined for URI and Path
    def rejectCyclicImports(imp: ImportContext, parents: NonEmptyList[ImportContext]): F[Unit] =
      if (parents.exists(_ == imp))
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
      if (checkHash(encoded, expected)) F.unit
      else F.raiseError(new ResolutionFailure("Cached expression does not match its hash"))
    }

    def loadWithSemanticCache(imp: ImportContext, mode: ImportMode, hash: Array[Byte]): F[Expr] =
      if (hash == null) loadWithSemiSemanticCache(imp, mode, hash)
      else
        for {
          cached <- semanticCache.get(hash)
          e <- cached match {
            case Some(bs) if checkHash(bs, hash) =>
              F.delay(Decode.decode(bs))
            case _ =>
              for {
                e <- loadWithSemiSemanticCache(imp, mode, hash)
                n = e.normalize.alphaNormalize
                bs = n.getEncodedBytes
                _ <- checkHashesMatch(bs, hash)
                _ <- semanticCache.put(hash, bs)
              } yield n
          }
        } yield e

    def fetch(imp: ImportContext): F[String] = imp match {
      case ImportContext.Env(value) =>
        for {
          vO <- F.delay(sys.env.get(value))
          v <- vO.fold(
            F.raiseError[String](new ResolutionFailure(s"Missing import - env import $value undefined", true))
          )(F.pure)
        } yield v
      case ImportContext.Local(path) =>
        for {
          v <- {
            if (Files.exists(path)) {
              F.delay(new String(Files.readAllBytes(path)))
            } else {
              F.raiseError(new ResolutionFailure(s"Missing import - file $path does not exist", true))
            }
          }
        } yield v
      case ImportContext.Classpath(path) =>
        for {
          v <-
            F.delay(getClass.getResourceAsStream(path.toString)).flatMap { stream =>
              if (stream.ne(null)) {
                F.delay(scala.io.Source.fromInputStream(stream).mkString)
              } else {
                F.raiseError[String](new ResolutionFailure(s"Missing import - resource $path does not exist", true))
              }
            }
        } yield v
      case ImportContext.Remote(uri, using) =>
        for {
          headers <- F.fromOption(ToHeaders(`using`), new ResolutionFailure("Invalid using clause"))
          req <- F.pure(Request[F](uri = unsafeFromString(uri.toString), headers = headers))
          resp <- Client.fetch[String](req) {
            case Successful(resp) =>
              for {
                s <- EntityDecoder.decodeText[F](resp)
                _ <- if (parents.nonEmpty) CorsComplianceCheck(parents.head, imp, resp.headers) else F.unit
              } yield s
            case _ =>
              F.raiseError[String](
                new ResolutionFailure(s"Missing import - cannot resolve $uri", true)
              )
          }
        } yield resp
      case ImportContext.Missing => F.raiseError(new ResolutionFailure("Missing import - cannot resolve missing", true))
    }

    def loadWithSemiSemanticCache(imp: ImportContext, mode: ImportMode, hash: Array[Byte]): F[Expr] = mode match {
      case ImportMode.LOCATION => F.raiseError(new ResolutionFailure("Unreachable - location imports already handled"))
      case ImportMode.RAW_TEXT =>
        for {
          text <- fetch(imp)
        } yield Expr.makeTextLiteral(text)
      case ImportMode.CODE =>
        for {
          text <- fetch(imp)
          parsed <- F.delay(DhallParser.parse(text))
          resolved <- {
            val v = new ResolveImportsVisitor[F](semanticCache, semiSemanticCache, imp :: parents)
            v.duplicateImportCache = this.duplicateImportCache
            parsed.accept(v)
          }
          semiHash = MessageDigest.getInstance("SHA-256").digest(resolved.getEncodedBytes)
          cached <- semiSemanticCache.get(semiHash)
          expr <- cached match {
            case Some(bs) => F.delay(Decode.decode(bs))
            case None =>
              for {
                _ <- F.delay(typeCheck(resolved))
                // TODO substitutions here?
                normalized <- F.delay(resolved.normalize)
                _ <- semiSemanticCache.put(semiHash, normalized.getEncodedBytes)
              } yield normalized
          }
        } yield expr
    }

    def resolve(imp: ImportContext, mode: ImportMode, hash: Array[Byte]): F[Expr] = {
      val p = (imp, mode)
      if (duplicateImportCache.contains(p)) {
        val cached = duplicateImportCache.get(p).get
        if (hash == null) {
          F.delay(cached)
        } else {
          checkHashesMatch(cached.getEncodedBytes, hash).as(cached)
        }
      } else {
        for {
          e <- loadWithSemanticCache(imp, mode, hash)
          _ <- F.delay(duplicateImportCache.put(p, e))
        } yield e
      }
    }

    def importNonLocation(imp: ImportContext, mode: ImportMode, hash: Array[Byte]) =
      for {
        _ <- ReferentialSanityCheck(parents.head, imp)
        _ <- rejectCyclicImports(imp, parents)
        r <- resolve(imp, mode, hash)
      } yield r

    for {
      imp <- canonicalize[F](parents.head, i)(F)
      result <- if (mode == ImportMode.LOCATION) importLocation(imp) else importNonLocation(imp, mode, hash)
    } yield result
  }
}

private object ResolveImportsVisitor {
  def apply[F[_] <: AnyRef: Async: Client](relativeTo: Path): F[ResolveImportsVisitor[F]] =
    Apply[F].map2(ImportCache[F]("dhall"), ImportCache[F]("dhallj"))(apply[F](_, _, relativeTo))

  def apply[F[_] <: AnyRef: Async: Client](semanticCache: ImportCache[F],
                                           semiSemanticCache: ImportCache[F],
                                           relativeTo: Path
  ): ResolveImportsVisitor[F] =
    // We add a placeholder filename "package.dhall" for the base directory as a Local import must have a filename
    new ResolveImportsVisitor[F](semanticCache,
                                 semiSemanticCache,
                                 NonEmptyList.one(Local(relativeTo.resolve("package.dhall")))
    )

  def apply[F[_] <: AnyRef: Async: Client](semanticCache: ImportCache[F], relativeTo: Path): ResolveImportsVisitor[F] =
    apply[F](semanticCache, new ImportCache.NoopImportCache[F], relativeTo)

  private def cwd: Path = Paths.get(".")

  def apply[F[_] <: AnyRef: Async: Client]: F[ResolveImportsVisitor[F]] = apply[F](cwd)

  def apply[F[_] <: AnyRef: Async: Client](semanticCache: ImportCache[F],
                                           semiSemanticCache: ImportCache[F]
  ): ResolveImportsVisitor[F] =
    apply[F](semanticCache, semiSemanticCache, cwd)

  def apply[F[_] <: AnyRef: Async: Client](semanticCache: ImportCache[F]): ResolveImportsVisitor[F] =
    apply[F](semanticCache, cwd)
}
