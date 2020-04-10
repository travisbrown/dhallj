package org.dhallj.s.ast

import org.dhallj.core.{Expr, Operator, Source}
import org.dhallj.s.Visitor
import org.dhallj.s.visitor.ConstantVisitor
import scala.jdk.CollectionConverters._

abstract class Constructor[A] {
  type Result = A
  protected[this] def extractor: Visitor[Option[A]]

  final def unapply(expr: Expr): Option[A] = expr.accept(extractor)
}

object DoubleLiteral extends Constructor[Double] {
  def apply(value: Double): Expr = Expr.makeDoubleLiteral(value)

  protected[this] val extractor: Visitor[Option[Result]] = new ConstantVisitor.Optional[Result] {
    override def onDouble(value: Double): Option[Double] = Some(value)
  }

}

object NaturalLiteral extends Constructor[BigInt] {
  def apply(value: BigInt): Expr = Expr.makeNaturalLiteral(value.underlying)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onNatural(value: BigInt): Option[BigInt] = Some(value)
    }
}

object IntegerLiteral extends Constructor[BigInt] {
  def apply(value: BigInt): Expr = Expr.makeIntegerLiteral(value.underlying)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onInteger(value: BigInt): Option[BigInt] = Some(value)
    }
}

object TextLiteral extends Constructor[(Iterable[String], Iterable[Expr])] {
  def apply(value: String): Expr = Expr.makeTextLiteral(value)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onText(parts: Iterable[String],
                          interpolated: Iterable[Expr]): Option[(Iterable[String], Iterable[Expr])] =
        Some((parts, interpolated))
    }
}

object BuiltIn extends Constructor[String] {
  def apply(name: String): Option[Expr] = Option(Expr.makeBuiltIn(name))

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onBuiltIn(name: String): Option[String] = Some(name)
    }
}

object Identifier extends Constructor[(String, Option[Long])] {
  def apply(name: String, index: Option[Long] = None): Expr = Expr.makeIdentifier(name, index.getOrElse(0))

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onIdentifier(name: String, index: Option[Long]): Option[(String, Option[Long])] =
        Some((name, index))
    }
}

object Application extends Constructor[(Expr, Expr)] {
  def apply(base: Expr, arg: Expr): Expr = Expr.makeApplication(base, arg)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onApplication(base: Expr, arg: Expr): Option[(Expr, Expr)] =
        Some((base, arg))
    }
}

object OperatorApplication extends Constructor[(Operator, Expr, Expr)] {
  def apply(operator: Operator, lhs: Expr, rhs: Expr): Expr = Expr.makeOperatorApplication(operator, lhs, rhs)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onOperatorApplication(operator: Operator, lhs: Expr, rhs: Expr): Option[(Operator, Expr, Expr)] =
        Some((operator, lhs, rhs))
    }
}

object If extends Constructor[(Expr, Expr, Expr)] {
  def apply(cond: Expr, thenValue: Expr, elseValue: Expr): Expr = Expr.makeIf(cond, thenValue, elseValue)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onIf(cond: Expr, thenValue: Expr, elseValue: Expr): Option[(Expr, Expr, Expr)] =
        Some((cond, thenValue, elseValue))
    }
}

object Lambda extends Constructor[(String, Expr, Expr)] {
  def apply(param: String, input: Expr, result: Expr): Expr = Expr.makeLambda(param, input, result)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onLambda(param: String, input: Expr, result: Expr): Option[(String, Expr, Expr)] =
        Some((param, input, result))
    }
}

object Pi extends Constructor[(Option[String], Expr, Expr)] {
  def apply(param: Option[String], input: Expr, result: Expr): Expr = Expr.makePi(param.orNull, input, result)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onPi(param: Option[String], input: Expr, result: Expr): Option[(Option[String], Expr, Expr)] =
        Some((param, input, result))
    }
}

object Assert extends Constructor[Expr] {
  def apply(base: Expr): Expr = Expr.makeAssert(base)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onAssert(base: Expr): Option[Expr] =
        Some(base)
    }
}

object FieldAccess extends Constructor[(Expr, String)] {
  def apply(base: Expr, fieldName: String): Expr = Expr.makeFieldAccess(base, fieldName)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onFieldAccess(base: Expr, fieldName: String): Option[(Expr, String)] =
        Some(base, fieldName)
    }
}

object Projection extends Constructor[(Expr, Iterable[String])] {
  def apply(base: Expr, fieldNames: Iterable[String]): Expr = Expr.makeProjection(base, fieldNames.toArray)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onProjection(base: Expr, fieldNames: Iterable[String]): Option[(Expr, Iterable[String])] =
        Some(base, fieldNames)
    }
}

object ProjectionByType extends Constructor[(Expr, Expr)] {
  def apply(base: Expr, tpe: Expr): Expr = Expr.makeProjectionByType(base, tpe)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onProjectionByType(base: Expr, tpe: Expr): Option[(Expr, Expr)] =
        Some(base, tpe)
    }
}

object RecordLiteral extends Constructor[Iterable[(String, Expr)]] {
  def apply(fields: Iterable[(String, Expr)]): Expr = Expr.makeRecordLiteral(fields.map(Visitor.tupleToEntry).asJava)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onRecord(fields: Iterable[(String, Expr)], size: Int): Option[Iterable[(String, Expr)]] =
        Some(fields)
    }
}

object RecordType extends Constructor[Iterable[(String, Expr)]] {
  def apply(fields: Iterable[(String, Expr)]): Expr = Expr.makeRecordType(fields.map(Visitor.tupleToEntry).asJava)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onRecordType(fields: Iterable[(String, Expr)], size: Int): Option[Iterable[(String, Expr)]] =
        Some(fields)
    }
}

object UnionType extends Constructor[Iterable[(String, Option[Expr])]] {
  def apply(fields: Iterable[(String, Option[Expr])]): Expr =
    Expr.makeUnionType(fields.map(Visitor.optionTupleToEntry).asJava)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onUnionType(fields: Iterable[(String, Option[Expr])],
                               size: Int): Option[Iterable[(String, Option[Expr])]] =
        Some(fields)
    }
}

object NonEmptyListLiteral extends Constructor[Iterable[Expr]] {
  def apply(values: Iterable[Expr]): Expr = Expr.makeNonEmptyListLiteral(values.asJava)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onNonEmptyList(values: Iterable[Expr], size: Int): Option[Iterable[Expr]] =
        Some(values)
    }
}

object EmptyListLiteral extends Constructor[Expr] {
  def apply(tpe: Expr): Expr = Expr.makeEmptyListLiteral(tpe)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onEmptyList(tpe: Expr): Option[Expr] = Some(tpe)
    }
}

object Note extends Constructor[(Expr, Source)] {
  def apply(base: Expr, source: Source): Expr = Expr.makeNote(base, source)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onNote(base: Expr, source: Source): Option[(Expr, Source)] = Some(base, source)
    }
}

object Let extends Constructor[(String, Option[Expr], Expr, Expr)] {
  def apply(name: String, tpe: Option[Expr], value: Expr, body: Expr): Expr =
    Expr.makeLet(name, tpe.orNull, value, body)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onLet(name: String, tpe: Option[Expr], value: Expr, body: Expr): Option[Result] =
        Some(name, tpe, value, body)
    }
}

object ToMap extends Constructor[(Expr, Option[Expr])] {
  def apply(base: Expr, tpe: Option[Expr]): Expr = Expr.makeToMap(base, tpe.orNull)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onToMap(base: Expr, tpe: Option[Expr]): Option[(Expr, Option[Expr])] =
        Some(base, tpe)
    }
}

object Merge extends Constructor[(Expr, Expr, Option[Expr])] {
  def apply(left: Expr, right: Expr, tpe: Option[Expr]): Expr = Expr.makeMerge(left, right, tpe.orNull)

  protected[this] val extractor: Visitor[Option[Result]] =
    new ConstantVisitor.Optional[Result] {
      override def onMerge(left: Expr, right: Expr, tpe: Option[Expr]): Option[(Expr, Expr, Option[Expr])] =
        Some(left, right, tpe)
    }
}
