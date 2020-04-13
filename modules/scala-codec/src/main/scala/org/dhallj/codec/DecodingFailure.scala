package org.dhallj.codec

import org.dhallj.core.{DhallException, Expr}

class DecodingFailure(val target: String, val value: Expr) extends DhallException(s"Error decoding $target") {
  final override def fillInStackTrace(): Throwable = this
}
