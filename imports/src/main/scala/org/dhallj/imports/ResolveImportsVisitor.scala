package org.dhallj.imports

import java.math.BigInteger
import java.net.URI
import java.nio.file.Path
import java.security.MessageDigest
import java.util.{List => JList, Map => JMap}

import cats.effect.Sync
import cats.effect.implicits._
import cats.implicits._
import org.dhallj.core.visitor.PureVis
import org.dhallj.core._
import org.dhallj.imports.Canonicalization.canonicalize
import org.dhallj.imports.ResolveImportsVisitor._
import org.dhallj.parser.Dhall
import org.http4s.Status.Successful
import org.http4s.client.Client
import org.http4s.{EntityDecoder, Headers}

import scala.jdk.CollectionConverters._

//TODO support caching
//TODO quoted path components?
//TODO handle duplicate imports - should be easy with caching logic
//TODO proper error handling
//TODO handle using
private[imports] case class ResolveImportsVisitor[F[_]](resolutionConfig: ResolutionConfig,
                                                        parents: List[ImportContext])(
  implicit Client: Client[F],
  F: Sync[F]
) extends PureVis[F[Expr]] {
  override def onDouble(value: Double): F[Expr] = F.pure(Expr.makeDoubleLiteral(value))

  override def onNatural(value: BigInteger): F[Expr] = F.pure(Expr.makeNaturalLiteral(value))

  override def onInteger(value: BigInteger): F[Expr] = F.pure(Expr.makeIntegerLiteral(value))

  override def onText(parts: Array[String], interpolated: JList[F[Expr]]): F[Expr] =
    for {
      i <- interpolated.asScala.toList.sequence
    } yield Expr.makeTextLiteral(parts, i.asJava)

  override def onApplication(baseExpr: Expr, base: F[Expr], args: JList[F[Expr]]): F[Expr] =
    for {
      b <- base
      a <- args.asScala.toList.sequence
    } yield Expr.makeApplication(b, a.asJava)

  override def onOperatorApplication(operator: Operator, lhs: F[Expr], rhs: F[Expr]): F[Expr] =
    if (operator == Operator.IMPORT_ALT)
      lhs.handleErrorWith(_ => rhs)
    else {
      for {
        l <- lhs
        r <- rhs
      } yield Expr.makeOperatorApplication(operator, l, r)
    }

  override def onIf(cond: F[Expr], thenValue: F[Expr], elseValue: F[Expr]): F[Expr] =
    for {
      c <- cond
      t <- thenValue
      e <- elseValue
    } yield Expr.makeIf(c, t, e)

  override def onLambda(param: String, input: F[Expr], result: F[Expr]): F[Expr] =
    for {
      i <- input
      r <- result
    } yield Expr.makeLambda(param, i, r)

  override def onPi(param: String, input: F[Expr], result: F[Expr]): F[Expr] =
    for {
      i <- input
      r <- result
    } yield Expr.makePi(param, i, r)

  override def onAssert(base: F[Expr]): F[Expr] =
    for {
      b <- base
    } yield Expr.makeAssert(b)

  override def onFieldAccess(base: F[Expr], fieldName: String): F[Expr] =
    for {
      b <- base
    } yield Expr.makeFieldAccess(b, fieldName)

  override def onProjection(base: F[Expr], fieldNames: Array[String]): F[Expr] =
    for {
      b <- base
    } yield Expr.makeProjection(b, fieldNames)

  override def onProjectionByType(base: F[Expr], tpe: F[Expr]): F[Expr] =
    for {
      b <- base
      t <- tpe
    } yield Expr.makeProjectionByType(b, t)

  override def onBuiltIn(name: String): F[Expr] = F.pure(Expr.makeBuiltIn(name))
  override def onIdentifier(value: String, index: Long): F[Expr] = F.pure(Expr.makeIdentifier(value, index))

  override def onRecord(fields: JList[JMap.Entry[String, F[Expr]]]): F[Expr] =
    for {
      f <- fields.asScala.toList.traverse(e => liftNull(e.getValue).map(v => e.getKey -> v))
    } yield Expr.makeRecordLiteral(f.toMap.asJava.entrySet)

  override def onRecordType(fields: JList[JMap.Entry[String, F[Expr]]]): F[Expr] =
    for {
      f <- fields.asScala.toList.traverse(e => liftNull(e.getValue).map(v => e.getKey -> v))
    } yield Expr.makeRecordType(f.toMap.asJava.entrySet)

  override def onUnionType(fields: JList[JMap.Entry[String, F[Expr]]]): F[Expr] =
    for {
      f <- fields.asScala.toList.traverse(e => liftNull(e.getValue).map(v => e.getKey -> v))
    } yield Expr.makeUnionType(f.toMap.asJava.entrySet)

  override def onNonEmptyList(values: JList[F[Expr]]): F[Expr] =
    for {
      v <- values.asScala.toList.sequence
    } yield Expr.makeNonEmptyListLiteral(v.asJava)

  override def onEmptyList(tpeExpr: Expr, tpe: F[Expr]): F[Expr] =
    for {
      t <- tpe
    } yield Expr.makeEmptyListLiteral(t)

  override def onNote(base: F[Expr], source: Source): F[Expr] =
    for {
      b <- base
    } yield Expr.makeNote(b, source)

  override def onLet(bindings: JList[LetBinding[F[Expr]]], body: F[Expr]): F[Expr] =
    for {
      bindings <- bindings.asScala.toList.traverse { binding =>
        for {
          t <- liftNull(binding.getType)
          v <- binding.getValue
        } yield new LetBinding(binding.getName, t, v)
      }
      b <- body
    } yield Expr.makeLet(bindings.asJava, b)

  override def onAnnotated(base: F[Expr], tpe: F[Expr]): F[Expr] =
    for {
      b <- base
      t <- tpe
    } yield Expr.makeAnnotated(b, t)

  override def onToMap(base: F[Expr], tpe: F[Expr]): F[Expr] =
    for {
      b <- base
      t <- liftNull(tpe)
    } yield Expr.makeToMap(b, t)

  override def onMerge(left: F[Expr], right: F[Expr], tpe: F[Expr]): F[Expr] =
    for {
      l <- left
      r <- right
      t <- liftNull(tpe)
    } yield Expr.makeMerge(l, r, t)

  override def onLocalImport(path: Path, mode: Import.Mode, hash: Array[Byte]): F[Expr] =
    onImport(Local(path), mode, hash)

  //TODO handle using
  override def onRemoteImport(url: URI, using: F[Expr], mode: Import.Mode, hash: Array[Byte]): F[Expr] =
    onImport(Remote(url), mode, hash)

  override def onEnvImport(value: String, mode: Import.Mode, hash: Array[Byte]): F[Expr] =
    onImport(Env(value), mode, hash)

  override def onMissingImport(mode: Import.Mode, hash: Array[Byte]): F[Expr] =
    onImport(Missing, mode, hash)

  private def onImport(i: ImportContext, mode: Import.Mode, hash: Array[Byte]): F[Expr] = {
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
    def rejectCyclicImports(imp: ImportContext, parents: List[ImportContext]): F[Unit] =
      if (parents.contains(imp))
        F.raiseError[Unit](new RuntimeException(s"Cyclic import - $imp is already imported in chain $parents"))
      else F.unit

    for {
      imp <- if (parents.isEmpty) canonicalize(resolutionConfig, i)
      else canonicalize(resolutionConfig, parents.head, i)
      _ <- if (parents.nonEmpty) ReferentialSanityCheck(parents.head, imp) else F.unit
      r <- resolve(imp, mode)
      (e, headers) = r
      _ <- if (parents.nonEmpty) CORSComplianceCheck(parents.head, imp, headers) else F.unit
      //TODO do we need to do this based on sha256 instead or something instead? Although parents won't be fully resolved
      _ <- rejectCyclicImports(imp, parents)
      result <- e.acceptVis(ResolveImportsVisitor[F](resolutionConfig, imp :: parents))
      _ <- validateHash(imp, result, hash)
    } yield result
  }

  private def liftNull(t: F[Expr]): F[Expr] = Option(t).getOrElse(F.pure(null))

  private def validateHash(imp: ImportContext, e: Expr, expected: Array[Byte]): F[Unit] =
    if (expected == null) F.unit
    else
      for {
        bytes <- F.pure(e.normalize().encodeToByteArray())
        encoded <- F.delay(sha256.digest(bytes)).guarantee(F.delay(sha256.reset()))
        _ <- if (encoded.sameElements(expected)) F.unit
        else F.raiseError(new RuntimeException(s"SHA256 validation exception for ${imp}"))
      } yield ()

  private def toHex(bs: Array[Byte]): String = {
    val sb = new StringBuilder
    for (b <- bs) {
      sb.append(String.format("%02x", Byte.box(b)))
    }
    sb.toString
  }

  private val sha256: MessageDigest = MessageDigest.getInstance("SHA-256")

}

object ResolveImportsVisitor {

  case class ResolutionConfig(
    localMode: LocalMode
  )

  sealed trait LocalMode
  case object FromFileSystem extends LocalMode
  case object FromResources extends LocalMode

  sealed trait ImportContext
  case class Env(value: String) extends ImportContext
  case class Local(absolutePath: Path) extends ImportContext
  case class Remote(uri: URI) extends ImportContext
  case object Missing extends ImportContext

}
