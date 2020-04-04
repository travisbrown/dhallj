package org.dhallj

import java.lang
import java.math.BigInteger
import java.net.URI
import java.nio.file.Path
import java.util.Map

import cats.effect.Sync
import org.dhallj.core.{Expr, Import, Operator, Source, Thunk, Visitor}
import org.http4s.client._
import cats.implicits._

import scala.jdk.CollectionConverters._
import java.nio.file.{Files, Paths}
import java.security.MessageDigest

import org.dhallj.parser.Dhall
import org.dhallj.imports.Canonicalization._
import org.dhallj.imports.{CORSComplianceCheck, ReferentialSanityCheck}
import org.http4s.{EntityDecoder, Headers, Uri}
import org.http4s.Status.Successful

//TODO sha256 checking if required
//TODO support caching
//TODO quoted path components?
//TODO handle duplicate imports - should be easy with caching logic
//TODO handle optional imports and the ? operator? (I don't think we need to do this here)
//TODO proper error handling
package object imports {

  implicit class ResolveImports(e: Expr) {
    def resolveImports[F[_]](
      resolutionConfig: ResolutionConfig = ResolutionConfig(FromFileSystem)
    )(implicit Client: Client[F], F: Sync[F]): F[Expr] = e.accept(ResolveImportsVisitor[F](resolutionConfig, Nil))
  }

  case class ResolutionConfig(
    localMode: LocalMode
  )

  sealed trait LocalMode
  case object FromFileSystem extends LocalMode
  case object FromResources extends LocalMode

  private case class ResolveImportsVisitor[F[_]](resolutionConfig: ResolutionConfig, parents: List[ImportContext])(implicit Client: Client[F], F: Sync[F])
      extends Visitor.Internal[F[Expr]] {
    override def onDoubleLiteral(value: Double): F[Expr] = F.pure(Expr.makeDoubleLiteral(value))

    override def onNaturalLiteral(value: BigInteger): F[Expr] = F.pure(Expr.makeNaturalLiteral(value))

    override def onIntegerLiteral(value: BigInteger): F[Expr] = F.pure(Expr.makeIntegerLiteral(value))

    override def onTextLiteral(parts: Array[String], interpolated: lang.Iterable[Thunk[F[Expr]]]): F[Expr] =
      for {
        i <- interpolated.asScala.toList.traverse(_.apply)
      } yield Expr.makeTextLiteral(parts, i.asJava)

    override def onApplication(base: Thunk[F[Expr]], arg: Thunk[F[Expr]]): F[Expr] =
      for {
        b <- base.apply
        a <- arg.apply
      } yield Expr.makeApplication(b, a)

    override def onOperatorApplication(operator: Operator, lhs: Thunk[F[Expr]], rhs: Thunk[F[Expr]]): F[Expr] =
      if (operator == Operator.IMPORT_ALT) lhs.apply else {
        for {
          l <- lhs.apply
          r <- rhs.apply
        } yield Expr.makeOperatorApplication(operator, l, r)
      }

    override def onIf(cond: Thunk[F[Expr]], thenValue: Thunk[F[Expr]], elseValue: Thunk[F[Expr]]): F[Expr] =
      for {
        c <- cond.apply
        t <- thenValue.apply
        e <- elseValue.apply
      } yield Expr.makeIf(c, t, e)

    override def onLambda(param: String, input: Thunk[F[Expr]], result: Thunk[F[Expr]]): F[Expr] =
      for {
        i <- input.apply
        r <- result.apply
      } yield Expr.makeLambda(param, i, r)

    override def onPi(param: String, input: Thunk[F[Expr]], result: Thunk[F[Expr]]): F[Expr] =
      for {
        i <- input.apply
        r <- result.apply
      } yield Expr.makePi(param, i, r)

    override def onAssert(base: Thunk[F[Expr]]): F[Expr] =
      for {
        b <- base.apply
      } yield Expr.makeAssert(b)

    override def onFieldAccess(base: Thunk[F[Expr]], fieldName: String): F[Expr] =
      for {
        b <- base.apply
      } yield Expr.makeFieldAccess(b, fieldName)

    override def onProjection(base: Thunk[F[Expr]], fieldNames: Array[String]): F[Expr] =
      for {
        b <- base.apply
      } yield Expr.makeProjection(b, fieldNames)

    override def onProjectionByType(base: Thunk[F[Expr]], tpe: Thunk[F[Expr]]): F[Expr] =
      for {
        b <- base.apply
        t <- tpe.apply
      } yield Expr.makeProjectionByType(b, t)

    override def onBuiltIn(name: String): F[Expr] = F.pure(Expr.makeBuiltIn(name))
    override def onIdentifier(value: String, index: Long): F[Expr] = F.pure(Expr.makeIdentifier(value, index))

    override def onRecordLiteral(fields: lang.Iterable[Map.Entry[String, Thunk[F[Expr]]]], size: Int): F[Expr] =
      for {
        f <- fields.asScala.toList.traverse(e => liftNull(e.getValue).map(v => e.getKey -> v))
      } yield Expr.makeRecordLiteral(f.toMap.asJava.entrySet)

    override def onRecordType(fields: lang.Iterable[Map.Entry[String, Thunk[F[Expr]]]], size: Int): F[Expr] =
      for {
        f <- fields.asScala.toList.traverse(e => liftNull(e.getValue).map(v => e.getKey -> v))
      } yield Expr.makeRecordType(f.toMap.asJava.entrySet)

    override def onUnionType(fields: lang.Iterable[Map.Entry[String, Thunk[F[Expr]]]], size: Int): F[Expr] =
      for {
        f <- fields.asScala.toList.traverse(e => liftNull(e.getValue).map(v => e.getKey -> v))
      } yield Expr.makeUnionType(f.toMap.asJava.entrySet)

    override def onNonEmptyListLiteral(values: lang.Iterable[Thunk[F[Expr]]], size: Int): F[Expr] =
      for {
        v <- values.asScala.toList.traverse(_.apply)
      } yield Expr.makeNonEmptyListLiteral(v.asJava)

    override def onEmptyListLiteral(tpe: Thunk[F[Expr]]): F[Expr] =
      for {
        t <- tpe.apply
      } yield Expr.makeEmptyListLiteral(t)

    override def onNote(base: Thunk[F[Expr]], source: Source): F[Expr] =
      for {
        b <- base.apply
      } yield Expr.makeNote(b, source)

    override def onLet(name: String, `type`: Thunk[F[Expr]], value: Thunk[F[Expr]], body: Thunk[F[Expr]]): F[Expr] =
      for {
        t <- liftNull(`type`)
        v <- value.apply
        b <- body.apply
      } yield Expr.makeLet(name, t, v, b)

    override def onAnnotated(base: Thunk[F[Expr]], tpe: Thunk[F[Expr]]): F[Expr] =
      for {
        b <- base.apply
        t <- tpe.apply
      } yield Expr.makeAnnotated(b, t)

    override def onToMap(base: Thunk[F[Expr]], tpe: Thunk[F[Expr]]): F[Expr] =
      for {
        b <- base.apply
        t <- liftNull(tpe)
      } yield Expr.makeToMap(b, t)

    override def onMerge(left: Thunk[F[Expr]], right: Thunk[F[Expr]], tpe: Thunk[F[Expr]]): F[Expr] =
      for {
        l <- left.apply
        r <- right.apply
        t <- liftNull(tpe)
      } yield Expr.makeMerge(l, r, t)

    override def onLocalImport(path: Path, mode: Import.Mode, hash: Array[Byte]): F[Expr] =
      onImport(Local(path), mode, hash)

    //TODO handle using
    override def onRemoteImport(url: URI, using: Thunk[F[Expr]], mode: Import.Mode, hash: Array[Byte]): F[Expr] =
      onImport(Remote(url), mode, hash)

    override def onEnvImport(value: String, mode: Import.Mode, hash: Array[Byte]): F[Expr] =
      onImport(Env(value), mode, hash)

    override def onMissingImport(mode: Import.Mode, hash: Array[Byte]): F[Expr] =
      onImport(Missing, mode, hash)

    private def onImport(i: ImportContext, mode: Import.Mode, hash: Array[Byte]): F[Expr] = {
      //TODO handle missing and unresolved imports properly
      def resolve(i: ImportContext, mode: Import.Mode): F[(Expr, Headers)] = {
        def makeLocation(field: String, value: String): F[Expr] =
          F.pure(
            Expr.makeApplication(Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, field), Expr.makeTextLiteral(value))
          )

        def resolve(i: ImportContext): F[(String, Headers)] = i match {
          case Env(value) =>
            for {
              vO <- F.delay(sys.env.get(value))
              v <- vO.fold(
                F.raiseError[String](new RuntimeException(s"Missing import - env import $value undefined"))
              )(F.pure)
            } yield v -> Headers.empty
          case Local(path) =>
            for {
              v <- resolutionConfig.localMode match {
                case FromFileSystem => F.delay(scala.io.Source.fromFile(path.toString).mkString)
                case FromResources =>
                  F.delay(scala.io.Source.fromInputStream(getClass.getResourceAsStream(path.toString)).mkString)
              }
            } yield v -> Headers.empty
          case Remote(uri) =>
            Client.get(uri.toString) {
              case Successful(resp) =>
                for {
                  s <- EntityDecoder.decodeString(resp)
                } yield s -> resp.headers
              case _ => F.raiseError[(String, Headers)](new RuntimeException(s"Missing import - cannot resolve $uri"))
            }
          case Missing => F.raiseError(new RuntimeException(s"Missing import - cannot resolve missing"))
        }

        mode match {
          case Import.Mode.CODE =>
            for {
              v <- resolve(i)
              (s, headers) = v
              e <- F.delay(Dhall.parse(s))
            } yield e -> headers
          //TODO check if this can be interpolated? The spec isn't very clear
          case Import.Mode.RAW_TEXT =>
            for {
              v <- resolve(i)
              (s, headers) = v
              e <- F.pure(Expr.makeTextLiteral(s))
            } yield e -> headers
          case Import.Mode.LOCATION =>
            for {
              expr <- i match {
                case Local(path) => makeLocation("Local", path.toString)
                case Remote(uri) => makeLocation("Remote", uri.toString)
                case Env(value)  => makeLocation("Environment", value)
                case Missing     => F.pure(Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Missing"))
              }
            } yield expr -> Headers.empty
        }
      }

      //TODO check that equality is sensibly defined for URI and Path
      def isCyclic(imp: ImportContext, parents: List[ImportContext]): Boolean = parents.contains(imp)

      for {
        imp <- if (parents.isEmpty) canonicalize(resolutionConfig, i) else canonicalize(resolutionConfig, parents.head, i)
        _ <- if (parents.nonEmpty) ReferentialSanityCheck(parents.head, imp) else F.unit
        r <- resolve(imp, mode)
        (e, headers) = r
        _ <- if (parents.nonEmpty) CORSComplianceCheck(parents.head, imp, headers) else F.unit
        //TODO do we need to do this based on sha256 instead or something instead? Although parents won't be fully resolved
        _ <- if (isCyclic(imp, parents))
          F.raiseError[Unit](new RuntimeException(s"Cyclic import - $imp is already imported in chain $parents"))
        else F.unit
        result <- e.accept(ResolveImportsVisitor[F](resolutionConfig, imp :: parents))
      } yield result
    }

    private def liftNull(t: Thunk[F[Expr]]): F[Expr] = Option(t.apply).getOrElse(F.pure(null))

    private def checkSha256[F[_]](e: Expr, expected: Array[Byte]): F[Unit] = ???

    private def toHex(bs: Array[Byte]): String = {
      val sb = new StringBuilder
      for (b <- bs) {
        sb.append(String.format("%02x", Byte.box(b)))
      }
      sb.toString
    }

  }

  sealed trait ImportContext
  case class Env(value: String) extends ImportContext
  case class Local(absolutePath: Path) extends ImportContext
  case class Remote(uri: URI) extends ImportContext
  case object Missing extends ImportContext

}
