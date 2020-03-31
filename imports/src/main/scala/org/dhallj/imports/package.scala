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
import org.http4s.Headers

//TODO import from urls, files, env vars
//TODO sha256 checking if required
//TODO support caching
//TODO quoted path components?
//TODO cycle detection - should be easy once we have stack of parent imports
//TODO handle duplicate imports - should be easy with caching logic
//TODO handle importing as text, location
//TODO handle optional imports and the ? operator
package object imports {

  implicit class ResolveImports(e: Expr) {
    //TODO need implicit Client[F] for remote imports
    def resolveImports[F[_]](implicit F: Sync[F]): F[Expr] = e.accept(ResolveImportsVisitor[F](Nil))
  }

  private case class ResolveImportsVisitor[F[_]](parents: List[ImportContext])(implicit F: Sync[F])
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
      for {
        l <- lhs.apply
        r <- rhs.apply
      } yield Expr.makeOperatorApplication(operator, l, r)

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

    override def onIdentifier(value: String, index: Long): F[Expr] = F.pure(Expr.makeIdentifier(value, index))

    override def onRecordLiteral(fields: lang.Iterable[Map.Entry[String, Thunk[F[Expr]]]], size: Int): F[Expr] =
      for {
        f <- fields.asScala.toList.traverse(e => e.getValue.apply.map(v => e.getKey -> v))
      } yield Expr.makeRecordLiteral(f.toMap.asJava.entrySet)

    override def onRecordType(fields: lang.Iterable[Map.Entry[String, Thunk[F[Expr]]]], size: Int): F[Expr] =
      for {
        f <- fields.asScala.toList.traverse(e => e.getValue.apply.map(v => e.getKey -> v))
      } yield Expr.makeRecordType(f.toMap.asJava.entrySet)

    override def onUnionType(fields: lang.Iterable[Map.Entry[String, Thunk[F[Expr]]]], size: Int): F[Expr] =
      for {
        f <- fields.asScala.toList.traverse(e => e.getValue.apply.map(v => e.getKey -> v))
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
        t <- `type`.apply
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
        t <- tpe.apply
      } yield Expr.makeToMap(b, t)

    override def onMerge(left: Thunk[F[Expr]], right: Thunk[F[Expr]], tpe: Thunk[F[Expr]]): F[Expr] =
      for {
        l <- left.apply
        r <- right.apply
        t <- tpe.apply
      } yield Expr.makeMerge(l, r, t)

    override def onLocalImport(path: Path, mode: Import.Mode, hash: Array[Byte]): F[Expr] = onImport(Local(path), mode, hash)

    override def onRemoteImport(url: URI, mode: Import.Mode, hash: Array[Byte]): F[Expr] = onImport(Remote(url), mode, hash)

    override def onEnvImport(value: String, mode: Import.Mode, hash: Array[Byte]): F[Expr] =
      onImport(Env(value), mode, hash)

    private def onImport(i: ImportContext, mode: Import.Mode, hash: Array[Byte]): F[Expr] = for {
      imp          <- if (parents.isEmpty) canonicalize(i) else canonicalize(i, parents.head)
      _            <- if (parents.nonEmpty) ReferentialSanityCheck(parents.head, imp) else F.unit
      r            <- resolve(imp)
      (v, headers) = r
      e            <- F.delay(Dhall.parse(v))
      _            <- if (parents.nonEmpty) CORSComplianceCheck(parents.head, imp, headers) else F.unit
      //TODO do we need to do this based on sha256 instead or something instead? Although parents won't be fully resolved
      _            <- if (isCyclic(imp, parents))
                        F.raiseError[Unit](new RuntimeException(s"Cyclic import - $imp is already imported in chain $parents"))
                      else F.unit
      result       <- e.accept(ResolveImportsVisitor[F](imp :: parents))
    } yield result

    //TODO handle missing and unresolved imports properly
    private def resolve(i: ImportContext): F[(String, Headers)] = i match {
      case Env(value) => for {
        vO <- F.delay(sys.env.get(value))
        v <- vO.fold(F.raiseError[String](new RuntimeException(s"Unresolved import - env import $value undefined")))(F.pure)
      } yield v -> Headers.empty
      case Local(path) => for {
        v <- F.delay(scala.io.Source.fromFile(path.toString).mkString)
      } yield v -> Headers.empty
      case _ => F.raiseError(new RuntimeException("TODO"))
    }

    override def onMissingImport(mode: Import.Mode, hash: Array[Byte]): F[Expr] = ???
  }

  //TODO check that equality is sensibly defined for URI and Path
  private def isCyclic(imp: ImportContext, parents: List[ImportContext]): Boolean = parents.contains(imp)

  private def checkSha256[F[_]](e: Expr, expected: Array[Byte]): F[Unit] = ???

  private def toHex(bs: Array[Byte]): String = {
    val sb = new StringBuilder
    for (b <- bs) {
      sb.append(String.format("%02x", Byte.box(b)))
    }
    sb.toString
  }

//  def resolveRemote[F[_]](imp: org.dhallj.core.Constructors.RemoteImport)(implicit Client: Client[F], F: Sync[F]): F[String] = for {
//    v <- Client.expect[String](imp.url)
//  } yield ""

  sealed trait ImportContext
  case class Env(value: String) extends ImportContext
  case class Local(absolutePath: Path) extends ImportContext
  case class Remote(uri: URI) extends ImportContext
  case object Missing extends ImportContext

}
