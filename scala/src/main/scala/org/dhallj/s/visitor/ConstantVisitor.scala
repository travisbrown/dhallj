package org.dhallj.s.visitor

import java.net.URI
import java.nio.file.Path
import org.dhallj.core.{Expr, Import, Operator, Source}
import org.dhallj.s.Visitor

trait ConstantVisitor[I, A] extends Visitor[I, A] {
  def constantValue: A
  def onDoubleLiteral(value: Double): A = constantValue
  def onNaturalLiteral(value: BigInt): A = constantValue
  def onIntegerLiteral(value: BigInt): A = constantValue
  def onBuiltIn(namee: String): A = constantValue
  def onIdentifier(name: String, index: Option[Long]): A = constantValue
  def onTextLiteral(parts: Iterable[String], interpolated: Iterable[I]): A = constantValue
  def onApplication(base: I, arg: I): A = constantValue
  def onOperatorApplication(operator: Operator, lhs: I, rhs: I): A = constantValue
  def onIf(cond: I, thenValue: I, elseValue: I): A = constantValue
  def onLambda(param: String, input: I, result: I): A = constantValue
  def onPi(param: Option[String], input: I, result: I): A = constantValue
  def onAssert(base: I): A = constantValue
  def onFieldAccess(base: I, fieldName: String): A = constantValue
  def onProjection(base: I, fieldNames: Iterable[String]): A = constantValue
  def onProjectionByType(base: I, tpe: I): A = constantValue
  def onRecordLiteral(fields: Iterable[(String, I)], size: Int): A = constantValue
  def onRecordType(fields: Iterable[(String, I)], size: Int): A = constantValue
  def onUnionType(fields: Iterable[(String, Option[I])], size: Int): A = constantValue
  def onNonEmptyListLiteral(values: Iterable[I], size: Int): A = constantValue
  def onEmptyListLiteral(tpe: I): A = constantValue
  def onLet(name: String, tpe: Option[I], value: I, body: I): A = constantValue
  def onAnnotated(base: I, tpe: I): A = constantValue
  def onToMap(base: I, tpe: Option[I]): A = constantValue
  def onMerge(left: I, right: I, tpe: Option[I]): A = constantValue
  def onLocalImport(path: Path, mode: Import.Mode, hash: Option[Array[Byte]]): A = constantValue
  def onEnvImport(value: String, mode: Import.Mode, hash: Option[Array[Byte]]): A = constantValue
  def onRemoteImport(url: URI, using: Option[I], mode: Import.Mode, hash: Option[Array[Byte]]): A = constantValue
  def onMissingImport(mode: Import.Mode, hash: Option[Array[Byte]]): A = constantValue
}

object ConstantVisitor {
  class Optional[A] extends ConstantVisitor[Expr, Option[A]] {
    def constantValue: Option[A] = None
    def onNote(base: Expr, source: Source): Option[A] = base.acceptExternal(this)
  }
}
