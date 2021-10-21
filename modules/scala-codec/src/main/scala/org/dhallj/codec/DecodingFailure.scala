package org.dhallj.codec

import org.dhallj.core.{DhallException, Expr}

class DecodingFailure(val message: String, val value: Option[Expr], val history: List[CursorOp])
    extends DhallException(s"$message: ${history.mkString(",")}") {
  final override def fillInStackTrace(): Throwable = this
}

object DecodingFailure {
  def failedTarget(target: String, value: Expr, history: List[CursorOp]): DecodingFailure =
    new DecodingFailure(s"Error decoding $target", Some(value), history)
}
