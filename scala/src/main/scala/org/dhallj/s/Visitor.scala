package org.dhallj.s

import java.lang.{Iterable => JIterable}
import java.math.BigInteger
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.{Map => JMap}
import org.dhallj.core.{Operator, Source, Thunk, Visitor => CoreVisitor}
import org.dhallj.core.visitor.{ConstantVisitor => CoreConstantVisitor}
import scala.jdk.CollectionConverters._

trait Visitor[I, A] extends CoreVisitor[I, A] {
  //def onDoubleLiteral(value: Double): A
  def onNaturalLiteral(value: BigInt): A
  def onIntegerLiteral(value: BigInt): A
  def onTextLiteral(parts: Iterable[String], interpolated: Iterable[I]): A
  //def onApplication(base: I, arg: I): A
  //def onOperatorApplication(operator: Operator, lhs: I, rhs: I): A
  //def onIf(cond: I, thenValue: I, elseValue: I): A
  //def onLambda(param: String, input: I, result: I): A
  def onPi(param: Option[String], input: I, result: I): A
  //def onAssert(base: I): A
  //ef onFieldAccess(base: I, fieldName: String): A
  def onProjection(base: I, fieldNames: Iterable[String]): A
  //def onProjectionByType(base: I, tpe: I): A
  def onIdentifier(value: String, index: Option[Long]): A
  def onRecordLiteral(fields: Iterable[(String, I)], size: Int): A
  def onRecordType(fields: Iterable[(String, I)], size: Int): A
  def onUnionType(fields: Iterable[(String, Option[I])], size: Int): A
  def onNonEmptyListLiteral(values: Iterable[I], size: Int): A
  //def onEmptyListLiteral(tpe: I): A
  //def onNote(base: I, source: Source): A
  def onLet(name: String, tpe: Option[I], value: I, body: I): A
  //def onAnnotated(base: I, tpe: I): A
  def onToMap(base: I, tpe: Option[I]): A
  def onMerge(left: I, right: I, tpe: Option[I]): A

  final def onNaturalLiteral(value: BigInteger): A = onNaturalLiteral(new BigInt(value))
  final def onIntegerLiteral(value: BigInteger): A = onIntegerLiteral(new BigInt(value))
  final def onTextLiteral(parts: Array[String], interpolated: JIterable[I]): A =
    onTextLiteral(parts.toIterable, interpolated.asScala)
  final def onPi(param: String, input: I, result: I): A = onPi(Option(param), input, result)
  final def onProjection(base: I, fieldNames: Array[String]): A = onProjection(base, fieldNames.toIterable)
  final def onIdentifier(value: String, index: Long): A = onIdentifier(value, if (index == 0) None else Some(index))
  final def onRecordLiteral(fields: JIterable[JMap.Entry[String, I]], size: Int): A =
    onRecordLiteral(fields.asScala.map(Visitor.entryToTuple), size)
  final def onRecordType(fields: JIterable[JMap.Entry[String, I]], size: Int): A =
    onRecordType(fields.asScala.map(Visitor.entryToTuple), size)
  final def onUnionType(fields: JIterable[JMap.Entry[String, I]], size: Int): A =
    onUnionType(fields.asScala.map(Visitor.entryToOptionTuple), size)
  final def onNonEmptyListLiteral(values: JIterable[I], size: Int): A = onNonEmptyListLiteral(values.asScala, size)
  final def onLet(name: String, tpe: I, value: I, body: I): A = onLet(name, Option(tpe), value, body)
  final def onToMap(base: I, tpe: I): A = onToMap(base, Option(tpe))
  final def onMerge(left: I, right: I, tpe: I): A = onMerge(left, right, Option(tpe))
}

object Visitor {
  private[s] def tupleToEntry[K, V](tuple: (K, V)): JMap.Entry[K, V] = new SimpleImmutableEntry(tuple._1, tuple._2)
  private[s] def optionTupleToEntry[K, V >: Null](tuple: (K, Option[V])): JMap.Entry[K, V] =
    new SimpleImmutableEntry(tuple._1, tuple._2.orNull)
  private[s] def entryToTuple[K, V](entry: JMap.Entry[K, V]): (K, V) = (entry.getKey, entry.getValue)
  private[s] def entryToOptionTuple[K, V](entry: JMap.Entry[K, V]): (K, Option[V]) =
    (entry.getKey, Option(entry.getValue))
  private[s] def evalOptionTupleValue[K, V](tuple: (K, Option[Thunk[V]])): (K, Option[V]) =
    (tuple._1, tuple._2.map(_.apply))
  private[s] def evalTupleValue[K, V](tuple: (K, Thunk[V])): (K, V) = (tuple._1, tuple._2.apply)
}
