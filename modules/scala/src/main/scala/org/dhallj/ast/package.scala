package org.dhallj.ast

import java.net.URI
import java.nio.file.Path
import java.lang.{Iterable => JIterable}
import java.math.BigInteger
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.{Map => JMap}
import org.dhallj.core.{Expr, ExternalVisitor, Operator, Source}
import scala.collection.JavaConverters._

abstract private class OptionVisitor[A] extends ExternalVisitor.Constant[Option[A]](None)

abstract private[ast] class Constructor[A] {
  type Result = A

  protected[this] def extractor: ExternalVisitor[Option[A]]

  final def unapply(expr: Expr): Option[A] = expr.accept(extractor)

  protected[this] def tupleToEntry[K, V](tuple: (K, V)): JMap.Entry[K, V] = new SimpleImmutableEntry(tuple._1, tuple._2)
  protected[this] def optionTupleToEntry[K, V >: Null](tuple: (K, Option[V])): JMap.Entry[K, V] =
    new SimpleImmutableEntry(tuple._1, tuple._2.orNull)

  protected[this] def entryToTuple[K, V](entry: JMap.Entry[K, V]): (K, V) = (entry.getKey, entry.getValue)
  protected[this] def entryToOptionTuple[K, V](entry: JMap.Entry[K, V]): (K, Option[V]) =
    (entry.getKey, Option(entry.getValue))
}

object NaturalLiteral extends Constructor[BigInt] {
  def apply(value: BigInt): Option[Expr] =
    if (value >= 0) Some(Expr.makeNaturalLiteral(value.underlying)) else None
  def apply(value: Long): Option[Expr] =
    if (value >= 0) Some(Expr.makeNaturalLiteral(BigInteger.valueOf(value))) else None

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onNatural(value: BigInteger): Option[BigInt] = Some(new BigInt(value))
    }
}

object IntegerLiteral extends Constructor[BigInt] {
  def apply(value: BigInt): Expr = Expr.makeIntegerLiteral(value.underlying)
  def apply(value: Long): Expr = Expr.makeNaturalLiteral(BigInteger.valueOf(value))

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onInteger(value: BigInteger): Option[BigInt] = Some(new BigInt(value))
    }
}

object DoubleLiteral extends Constructor[Double] {
  def apply(value: Double): Expr = Expr.makeDoubleLiteral(value)

  protected[this] val extractor: ExternalVisitor[Option[Result]] = new OptionVisitor[Result] {
    override def onDouble(value: Double): Option[Double] = Some(value)
  }
}

object BoolLiteral extends Constructor[Boolean] {
  def apply(value: Boolean): Expr = if (value) Expr.Constants.TRUE else Expr.Constants.FALSE

  protected[this] val extractor: ExternalVisitor[Option[Result]] = new OptionVisitor[Result] {
    override def onBuiltIn(name: String): Option[Boolean] = name match {
      case "True"  => Some(true)
      case "False" => Some(false)
      case _       => None
    }
  }
}

object Identifier extends Constructor[(String, Option[Long])] {
  def apply(name: String, index: Long): Expr = Expr.makeIdentifier(name, index)
  def apply(name: String): Expr = apply(name, 0)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onIdentifier(name: String, index: Long): Option[Result] =
        Some((name, if (index == 0) None else Some(index)))
    }
}

object Lambda extends Constructor[(String, Expr, Expr)] {
  def apply(name: String, tpe: Expr, result: Expr): Expr = Expr.makeLambda(name, tpe, result)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onLambda(name: String, tpe: Expr, result: Expr): Option[Result] =
        Some((name, tpe, result))
    }
}

object Pi extends Constructor[(Option[String], Expr, Expr)] {
  def apply(name: String, tpe: Expr, result: Expr): Expr = Expr.makePi(name, tpe, result)
  def apply(tpe: Expr, result: Expr): Expr = Expr.makePi("_", tpe, result)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onPi(name: String, tpe: Expr, result: Expr): Option[Result] =
        Some((if (name == "_") None else Some(name), tpe, result))
    }
}

object Let extends Constructor[(String, Option[Expr], Expr, Expr)] {
  def apply(name: String, tpe: Expr, value: Expr, body: Expr): Expr = Expr.makeLet(name, tpe, value, body)
  def apply(name: String, value: Expr, body: Expr): Expr = Expr.makeLet(name, null, value, body)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onLet(name: String, tpe: Expr, value: Expr, body: Expr): Option[Result] =
        Some(name, Option(tpe), value, body)
    }
}

object TextLiteral extends Constructor[(String, Vector[(Expr, String)])] {
  def apply(value: String): Expr = Expr.makeTextLiteral(value)
  def apply(first: String, rest: Vector[(Expr, String)]) = {
    val parts = first +: rest.map(_._2).toArray
    val interpolated = rest.map(_._1).toArray

    Expr.makeTextLiteral(parts, interpolated)
  }

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onText(parts: Array[String], interpolated: JIterable[Expr]): Option[Result] =
        Some((parts(0), interpolated.asScala.zip(parts.tail).toVector))
    }
}

object NonEmptyListLiteral extends Constructor[Vector[Expr]] {
  def apply(head: Expr, tail: Vector[Expr]): Expr = Expr.makeNonEmptyListLiteral((head +: tail).asJava)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onNonEmptyList(values: JIterable[Expr], size: Int): Option[Vector[Expr]] =
        Some(values.asScala.toVector)
    }
}

object EmptyListLiteral extends Constructor[Expr] {
  def apply(tpe: Expr): Expr = Expr.makeEmptyListLiteral(tpe)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onEmptyList(tpe: Expr): Option[Expr] = Some(tpe)
    }
}

object ListLiteral extends Constructor[Vector[Expr]] {
  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onNonEmptyList(values: JIterable[Expr], size: Int): Option[Vector[Expr]] =
        Some(values.asScala.toVector)
      override def onEmptyList(tpe: Expr): Option[Vector[Expr]] = Some(Vector.empty)
    }
}

object RecordLiteral extends Constructor[Map[String, Expr]] {
  def apply(fields: Map[String, Expr]): Expr = Expr.makeRecordLiteral(fields.toSeq.map(tupleToEntry).asJava)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onRecord(fields: JIterable[JMap.Entry[String, Expr]], size: Int): Option[Map[String, Expr]] =
        Some(fields.asScala.map(entryToTuple).toMap)
    }
}

object RecordType extends Constructor[Map[String, Expr]] {
  def apply(fields: Map[String, Expr]): Expr = Expr.makeRecordType(fields.toSeq.map(tupleToEntry).asJava)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onRecordType(fields: JIterable[JMap.Entry[String, Expr]], size: Int): Option[Map[String, Expr]] =
        Some(fields.asScala.map(entryToTuple).toMap)
    }
}

object UnionType extends Constructor[Map[String, Option[Expr]]] {
  def apply(fields: Map[String, Option[Expr]]): Expr = Expr.makeUnionType(fields.toSeq.map(optionTupleToEntry).asJava)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onUnionType(fields: JIterable[JMap.Entry[String, Expr]],
                               size: Int
      ): Option[Map[String, Option[Expr]]] =
        Some(fields.asScala.map(entryToOptionTuple).toMap)
    }
}

object FieldAccess extends Constructor[(Expr, String)] {
  def apply(base: Expr, fieldName: String): Expr = Expr.makeFieldAccess(base, fieldName)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onFieldAccess(base: Expr, fieldName: String): Option[Result] = Some((base, fieldName))
    }
}

object Projection extends Constructor[(Expr, Vector[String])] {
  def apply(base: Expr, fieldNames: Vector[String]): Expr = Expr.makeProjection(base, fieldNames.toArray)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onProjection(base: Expr, fieldNames: Array[String]): Option[Result] =
        Some(base, fieldNames.toVector)
    }
}

object ProjectionByType extends Constructor[(Expr, Expr)] {
  def apply(base: Expr, tpe: Expr): Expr = Expr.makeProjectionByType(base, tpe)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onProjectionByType(base: Expr, tpe: Expr): Option[Result] =
        Some(base, tpe)
    }
}

object Application extends Constructor[(Expr, Expr)] {
  def apply(base: Expr, arg: Expr): Expr = Expr.makeApplication(base, arg)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onApplication(base: Expr, arg: Expr): Option[Result] =
        Some((base, arg))
    }
}

object OperatorApplication extends Constructor[(Operator, Expr, Expr)] {
  def apply(operator: Operator, lhs: Expr, rhs: Expr): Expr = Expr.makeOperatorApplication(operator, lhs, rhs)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onOperatorApplication(operator: Operator, lhs: Expr, rhs: Expr): Option[Result] =
        Some((operator, lhs, rhs))
    }
}

object If extends Constructor[(Expr, Expr, Expr)] {
  def apply(cond: Expr, thenValue: Expr, elseValue: Expr): Expr = Expr.makeIf(cond, thenValue, elseValue)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onIf(cond: Expr, thenValue: Expr, elseValue: Expr): Option[Result] =
        Some((cond, thenValue, elseValue))
    }
}

object Annotated extends Constructor[(Expr, Expr)] {
  def apply(base: Expr, tpe: Expr): Expr = Expr.makeAnnotated(base, tpe)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onAnnotated(base: Expr, tpe: Expr): Option[Result] =
        Some((base, tpe))
    }
}

object Assert extends Constructor[Expr] {
  def apply(base: Expr): Expr = Expr.makeAssert(base)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onAssert(base: Expr): Option[Expr] =
        Some(base)
    }
}

object Merge extends Constructor[(Expr, Expr, Option[Expr])] {
  def apply(handlers: Expr, union: Expr, tpe: Expr): Expr = Expr.makeMerge(handlers, union, tpe)
  def apply(handlers: Expr, union: Expr): Expr = Expr.makeMerge(handlers, union, null)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onMerge(handlers: Expr, union: Expr, tpe: Expr): Option[Result] =
        Some(handlers, union, Option(tpe))
    }
}

object ToMap extends Constructor[(Expr, Option[Expr])] {
  def apply(base: Expr, tpe: Expr): Expr = Expr.makeToMap(base, tpe)
  def apply(base: Expr): Expr = Expr.makeToMap(base, null)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onToMap(base: Expr, tpe: Expr): Option[Result] = Some(base, Option(tpe))
    }
}

object MissingImport extends Constructor[(Expr.ImportMode, Option[String])] {

  /**
   * Note that this constructor does not verify that the input is a hex-encoded SHA-256 hash.
   */
  def apply(mode: Expr.ImportMode, hash: String): Expr = Expr.makeMissingImport(mode, Expr.Util.decodeHashBytes(hash))
  def apply(mode: Expr.ImportMode): Expr = Expr.makeMissingImport(mode, null)
  def apply(): Expr = Expr.makeMissingImport(Expr.ImportMode.CODE, null)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onMissingImport(mode: Expr.ImportMode, hash: Array[Byte]): Option[Result] =
        Some((mode, Option(hash).map(Expr.Util.encodeHashBytes)))
    }
}

object EnvImport extends Constructor[(String, Expr.ImportMode, Option[String])] {

  /**
   * Note that this constructor does not verify that the input is a hex-encoded SHA-256 hash.
   */
  def apply(name: String, mode: Expr.ImportMode, hash: String): Expr =
    Expr.makeEnvImport(name, mode, Expr.Util.decodeHashBytes(hash))
  def apply(name: String, mode: Expr.ImportMode): Expr = Expr.makeEnvImport(name, mode, null)
  def apply(name: String): Expr = Expr.makeEnvImport(name, Expr.ImportMode.CODE, null)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onEnvImport(name: String, mode: Expr.ImportMode, hash: Array[Byte]): Option[Result] =
        Some((name, mode, Option(hash).map(Expr.Util.encodeHashBytes)))
    }
}

object LocalImport extends Constructor[(Path, Expr.ImportMode, Option[String])] {

  /**
   * Note that this constructor does not verify that the input is a hex-encoded SHA-256 hash.
   */
  def apply(path: Path, mode: Expr.ImportMode, hash: String): Expr =
    Expr.makeLocalImport(path, mode, Expr.Util.decodeHashBytes(hash))
  def apply(path: Path, mode: Expr.ImportMode): Expr = Expr.makeLocalImport(path, mode, null)
  def apply(path: Path): Expr = Expr.makeLocalImport(path, Expr.ImportMode.CODE, null)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onLocalImport(path: Path, mode: Expr.ImportMode, hash: Array[Byte]): Option[Result] =
        Some((path, mode, Option(hash).map(Expr.Util.encodeHashBytes)))
    }
}

object RemoteImport extends Constructor[(URI, Option[Expr], Expr.ImportMode, Option[String])] {

  /**
   * Note that this constructor does not verify that the input is a hex-encoded SHA-256 hash.
   */
  def apply(url: URI, usingExpr: Expr, mode: Expr.ImportMode, hash: String): Expr =
    Expr.makeRemoteImport(url, usingExpr, mode, Expr.Util.decodeHashBytes(hash))
  def apply(url: URI, usingExpr: Expr, mode: Expr.ImportMode): Expr = Expr.makeRemoteImport(url, usingExpr, mode, null)
  def apply(url: URI, usingExpr: Expr): Expr = Expr.makeRemoteImport(url, usingExpr, Expr.ImportMode.CODE, null)
  def apply(url: URI): Expr = Expr.makeRemoteImport(url, null, Expr.ImportMode.CODE, null)

  protected[this] val extractor: ExternalVisitor[Option[Result]] =
    new OptionVisitor[Result] {
      override def onRemoteImport(url: URI, usingExpr: Expr, mode: Expr.ImportMode, hash: Array[Byte]): Option[Result] =
        Some((url, Option(usingExpr), mode, Option(hash).map(Expr.Util.encodeHashBytes)))
    }
}
