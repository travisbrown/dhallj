package org.dhallj.s.visitor

import org.dhallj.core.{Operator, Source, Thunk, Visitor => CoreVisitor}
import org.dhallj.s.Visitor

trait StrictInternalVisitor[A] extends CoreVisitor.Internal[A] with Visitor[Thunk[A], A] {
  def onDoubleLiteral(value: Double): A
  def onNaturalLiteral(value: BigInt): A
  def onIntegerLiteral(value: BigInt): A
  def onIdentifier(value: String, index: Option[Long]): A
  def onTextLiteralStrict(parts: Iterable[String], interpolated: Iterable[A]): A
  def onApplicationStrict(base: A, arg: A): A
  def onOperatorApplicationStrict(operator: Operator, lhs: A, rhs: A): A
  def onIfStrict(cond: A, thenValue: A, elseValue: A): A
  def onLambdaStrict(param: String, input: A, result: A): A
  def onPiStrict(param: Option[String], input: A, result: A): A
  def onAssertStrict(base: A): A
  def onFieldAccessStrict(base: A, fieldName: String): A
  def onProjectionStrict(base: A, fieldNames: Iterable[String]): A
  def onProjectionByTypeStrict(base: A, tpe: A): A
  def onRecordLiteralStrict(fields: Iterable[(String, A)], size: Int): A
  def onRecordTypeStrict(fields: Iterable[(String, A)], size: Int): A
  def onUnionTypeStrict(fields: Iterable[(String, Option[A])], size: Int): A
  def onNonEmptyListLiteralStrict(values: Iterable[A], size: Int): A
  def onEmptyListLiteralStrict(tpe: A): A
  def onNoteStrict(base: A, source: Source): A
  def onLetStrict(name: String, tpe: Option[A], value: A, body: A): A
  def onAnnotatedStrict(base: A, tpe: A): A
  def onToMapStrict(base: A, tpe: Option[A]): A
  def onMergeStrict(left: A, right: A, tpe: Option[A]): A

  final def onTextLiteral(parts: Iterable[String], interpolated: Iterable[Thunk[A]]): A =
    onTextLiteralStrict(parts, interpolated.map(_.apply))
  final def onOperatorApplicationStrict(operator: Operator, lhs: Thunk[A], rhs: Thunk[A]): A =
    onOperatorApplicationStrict(operator, lhs.apply, rhs.apply)
  final def onIf(cond: Thunk[A], thenValue: Thunk[A], elseValue: Thunk[A]): A =
    onIfStrict(cond.apply, thenValue.apply, elseValue.apply)
  final def onLambda(param: String, input: Thunk[A], result: Thunk[A]): A =
    onLambdaStrict(param, input.apply, result.apply)
  final def onPi(param: Option[String], input: Thunk[A], result: Thunk[A]): A =
    onPiStrict(param, input.apply, result.apply)
  final def onAssert(base: Thunk[A]): A = onAssertStrict(base.apply)
  final def onFieldAccess(base: Thunk[A], fieldName: String): A = onFieldAccessStrict(base.apply, fieldName)
  final def onProjection(base: Thunk[A], fieldNames: Iterable[String]): A = onProjectionStrict(base.apply, fieldNames)
  final def onRecordLiteral(fields: Iterable[(String, Thunk[A])], size: Int): A =
    onRecordLiteralStrict(fields.map(Visitor.evalTupleValue), size)
  final def onRecordType(fields: Iterable[(String, Thunk[A])], size: Int): A =
    onRecordTypeStrict(fields.map(Visitor.evalTupleValue), size)
  final def onUnionType(fields: Iterable[(String, Option[Thunk[A]])], size: Int): A =
    onUnionTypeStrict(fields.map(Visitor.evalOptionTupleValue), size)
  final def onNonEmptyListLiteral(values: Iterable[Thunk[A]], size: Int): A =
    onNonEmptyListLiteralStrict(values.map(_.apply), size)
  final def onEmptyListLiteral(tpe: Thunk[A]): A = onEmptyListLiteralStrict(tpe.apply)
  final def onNote(base: Thunk[A], source: Source): A = onNoteStrict(base.apply, source)
  final def onLet(name: String, tpe: Option[Thunk[A]], value: Thunk[A], body: Thunk[A]): A =
    onLetStrict(name, tpe.map(_.apply), value.apply, body.apply)
  final def onAnnotated(base: Thunk[A], tpe: Thunk[A]): A = onAnnotatedStrict(base.apply, tpe.apply)
  final def onToMap(base: Thunk[A], tpe: Option[Thunk[A]]): A = onToMapStrict(base.apply, tpe.map(_.apply))
  final def onMerge(left: Thunk[A], right: Thunk[A], tpe: Option[Thunk[A]]): A =
    onMergeStrict(left.apply, right.apply, tpe.map(_.apply))
}
