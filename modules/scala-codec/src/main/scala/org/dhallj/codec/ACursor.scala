package org.dhallj.codec

import org.dhallj.core.Expr

abstract class ACursor(private val lastCursor: ACursor, private val lastOp: CursorOp) {
  def focus: Option[Expr]

  final def history: List[CursorOp] = {
    var next = this
    val builder = List.newBuilder[CursorOp]

    while (next.ne(null)) {
      if (next.lastOp.ne(null)) {
        builder += next.lastOp
      }
      next = next.lastCursor
    }

    builder.result()
  }

  def downField(f: String): ACursor
  def unionAlternative(f: String): ACursor

  def succeeded: Boolean
  def failed: Boolean = !succeeded
}

class FailedCursor(lastCursor: HCursor, lastOp: CursorOp) extends ACursor(lastCursor, lastOp) {
  val focus = None
  final def succeeded: Boolean = false

  override def downField(f: String): ACursor = this
  override def unionAlternative(f: String): ACursor = this
}
