package org.dhallj.s.visitor

import java.net.URI
import java.nio.file.Path
import org.dhallj.core.{Expr, Operator, Source}
import org.dhallj.s.Visitor

trait ConstantVisitor[A] extends Visitor[A] {
  def constantValue: A
  def onDouble(value: Double): A = constantValue
  def onNatural(value: BigInt): A = constantValue
  def onInteger(value: BigInt): A = constantValue
  def onBuiltIn(namee: String): A = constantValue
  def onIdentifier(name: String, index: Option[Long]): A = constantValue
  def onText(parts: Iterable[String], interpolated: Iterable[Expr]): A = constantValue
  def onApplication(base: Expr, arg: Expr): A = constantValue
  def onOperatorApplication(operator: Operator, lhs: Expr, rhs: Expr): A = constantValue
  def onIf(cond: Expr, thenValue: Expr, elseValue: Expr): A = constantValue
  def onLambda(param: String, input: Expr, result: Expr): A = constantValue
  def onPi(param: Option[String], input: Expr, result: Expr): A = constantValue
  def onAssert(base: Expr): A = constantValue
  def onFieldAccess(base: Expr, fieldName: String): A = constantValue
  def onProjection(base: Expr, fieldNames: Iterable[String]): A = constantValue
  def onProjectionByType(base: Expr, tpe: Expr): A = constantValue
  def onRecord(fields: Iterable[(String, Expr)], size: Int): A = constantValue
  def onRecordType(fields: Iterable[(String, Expr)], size: Int): A = constantValue
  def onUnionType(fields: Iterable[(String, Option[Expr])], size: Int): A = constantValue
  def onNonEmptyList(values: Iterable[Expr], size: Int): A = constantValue
  def onEmptyList(tpe: Expr): A = constantValue
  def onLet(name: String, tpe: Option[Expr], value: Expr, body: Expr): A = constantValue
  def onAnnotated(base: Expr, tpe: Expr): A = constantValue
  def onMerge(handlers: Expr, union: Expr, tpe: Option[Expr]): A = constantValue
  def onToMap(base: Expr, tpe: Option[Expr]): A = constantValue
  def onLocalImport(path: Path, mode: Expr.ImportMode, hash: Option[Array[Byte]]): A = constantValue
  def onEnvImport(value: String, mode: Expr.ImportMode, hash: Option[Array[Byte]]): A = constantValue
  def onRemoteImport(url: URI, using: Option[Expr], mode: Expr.ImportMode, hash: Option[Array[Byte]]): A = constantValue
  def onMissingImport(mode: Expr.ImportMode, hash: Option[Array[Byte]]): A = constantValue
}

object ConstantVisitor {
  class Optional[A] extends ConstantVisitor[Option[A]] {
    def constantValue: Option[A] = None
    def onNote(base: Expr, source: Source): Option[A] = base.accept(this)
  }
}
