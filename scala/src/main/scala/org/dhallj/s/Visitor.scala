package org.dhallj.s

import java.lang.{Iterable => JIterable}
import java.math.BigInteger
import java.net.URI
import java.nio.file.Path
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.{Map => JMap}
import org.dhallj.core.{Expr, Import, Operator, Source, ExternalVisitor => CoreExternalVisitor}
import scala.jdk.CollectionConverters._

trait Visitor[A] extends CoreExternalVisitor[A] {
  //def onDouble(value: Double): A
  def onNatural(value: BigInt): A
  def onInteger(value: BigInt): A
  def onText(parts: Iterable[String], interpolated: Iterable[Expr]): A
  //def onApplication(base: Expr, arg: Expr): A
  //def onOperatorApplication(operator: Operator, lhs: Expr, rhs: Expr): A
  //def onIf(cond: Expr, thenValue: Expr, elseValue: Expr): A
  //def onLambda(param: String, input: Expr, result: Expr): A
  def onPi(param: Option[String], input: Expr, result: Expr): A
  //def onAssert(base: Expr): A
  //ef onFieldAccess(base: Expr, fieldName: String): A
  def onProjection(base: Expr, fieldNames: Iterable[String]): A
  //def onProjectionByType(base: Expr, tpe: Expr): A
  def onIdentifier(value: String, index: Option[Long]): A
  def onRecord(fields: Iterable[(String, Expr)], size: Int): A
  def onRecordType(fields: Iterable[(String, Expr)], size: Int): A
  def onUnionType(fields: Iterable[(String, Option[Expr])], size: Int): A
  def onNonEmptyList(values: Iterable[Expr], size: Int): A
  //def onEmptyList(tpe: Expr): A
  //def onNote(base: Expr, source: Source): A
  def onLet(name: String, tpe: Option[Expr], value: Expr, body: Expr): A
  //def onAnnotated(base: Expr, tpe: Expr): A
  def onToMap(base: Expr, tpe: Option[Expr]): A
  def onMerge(left: Expr, right: Expr, tpe: Option[Expr]): A
  def onMissingImport(mode: Import.Mode, hash: Option[Array[Byte]]): A
  def onEnvImport(value: String, mode: Import.Mode, hash: Option[Array[Byte]]): A
  def onLocalImport(path: Path, mode: Import.Mode, hash: Option[Array[Byte]]): A
  def onRemoteImport(url: URI, using: Option[Expr], mode: Import.Mode, hash: Option[Array[Byte]]): A

  final def onNatural(value: BigInteger): A = onNatural(new BigInt(value))
  final def onInteger(value: BigInteger): A = onInteger(new BigInt(value))
  final def onText(parts: Array[String], interpolated: JIterable[Expr]): A =
    onText(parts.toIterable, interpolated.asScala)
  final def onPi(param: String, input: Expr, result: Expr): A = onPi(Option(param), input, result)
  final def onProjection(base: Expr, fieldNames: Array[String]): A = onProjection(base, fieldNames.toIterable)
  final def onIdentifier(value: String, index: Long): A = onIdentifier(value, if (index == 0) None else Some(index))
  final def onRecord(fields: JIterable[JMap.Entry[String, Expr]], size: Int): A =
    onRecord(fields.asScala.map(Visitor.entryToTuple), size)
  final def onRecordType(fields: JIterable[JMap.Entry[String, Expr]], size: Int): A =
    onRecordType(fields.asScala.map(Visitor.entryToTuple), size)
  final def onUnionType(fields: JIterable[JMap.Entry[String, Expr]], size: Int): A =
    onUnionType(fields.asScala.map(Visitor.entryToOptionTuple), size)
  final def onNonEmptyList(values: JIterable[Expr], size: Int): A = onNonEmptyList(values.asScala, size)
  final def onLet(name: String, tpe: Expr, value: Expr, body: Expr): A = onLet(name, Option(tpe), value, body)
  final def onToMap(base: Expr, tpe: Expr): A = onToMap(base, Option(tpe))
  final def onMerge(left: Expr, right: Expr, tpe: Expr): A = onMerge(left, right, Option(tpe))

  final def onMissingImport(mode: Import.Mode, hash: Array[Byte]) = onMissingImport(mode, Option(hash))
  final def onEnvImport(value: String, mode: Import.Mode, hash: Array[Byte]) = onEnvImport(value, mode, Option(hash))
  final def onLocalImport(path: Path, mode: Import.Mode, hash: Array[Byte]) = onLocalImport(path, mode, Option(hash))
  final def onRemoteImport(url: URI, using: Expr, mode: Import.Mode, hash: Array[Byte]) =
    onRemoteImport(url, Option(using), mode, Option(hash))
}

object Visitor {
  private[s] def tupleToEntry[K, V](tuple: (K, V)): JMap.Entry[K, V] = new SimpleImmutableEntry(tuple._1, tuple._2)
  private[s] def optionTupleToEntry[K, V >: Null](tuple: (K, Option[V])): JMap.Entry[K, V] =
    new SimpleImmutableEntry(tuple._1, tuple._2.orNull)
  private[s] def entryToTuple[K, V](entry: JMap.Entry[K, V]): (K, V) = (entry.getKey, entry.getValue)
  private[s] def entryToOptionTuple[K, V](entry: JMap.Entry[K, V]): (K, Option[V]) =
    (entry.getKey, Option(entry.getValue))
}
