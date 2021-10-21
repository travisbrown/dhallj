package org.dhallj.codec

import org.dhallj.ast.{Application, FieldAccess, RecordLiteral, UnionType}
import org.dhallj.core.Expr

abstract class HCursor(private val lastCursor: HCursor, private val lastOp: CursorOp)
    extends ACursor(lastCursor, lastOp) {
  def expr: Expr
  final def focus: Option[Expr] = Some(expr)
  final def succeeded: Boolean = true

  final def downField(f: String): ACursor = expr match {
    case RecordLiteral(m) =>
      m.get(f) match {
        case Some(expr) => HCursor(expr, this, DownField(f))
        case None       => new FailedCursor(this, DownField(f))
      }
    case _ =>
      new FailedCursor(this, DownField(f))
  }

  def unionAlternative(f: String): ACursor = expr match {
    case Application(FieldAccess(UnionType(_), `f`), arg) =>
      HCursor(arg, this, UnionAlternative(f))
    case FieldAccess(UnionType(_), `f`) =>
      HCursor(expr, this, UnionAlternative(f))
    case _ =>
      new FailedCursor(this, UnionAlternative(f))
  }
}

object HCursor {
  def fromExpr(e: Expr): HCursor = new HCursor(null, null) {
    override def expr: Expr = e
  }

  def apply(e: Expr, lastCursor: HCursor, lastOp: CursorOp): HCursor = new HCursor(lastCursor, lastOp) {
    override def expr: Expr = e
  }
}
