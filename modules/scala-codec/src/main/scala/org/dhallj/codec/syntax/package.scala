package org.dhallj.codec

import org.dhallj.core.Expr

package object syntax {
  implicit final class DhallCodecAnyOps[A](val value: A) extends AnyVal {
    def asExpr(implicit A: Encoder[A]): Expr = A.encode(value)
  }

  implicit final class DhallCodecExprOps(val expr: Expr) extends AnyVal {
    def as[A: Decoder]: Decoder.Result[A] = Decoder[A].decode(HCursor.fromExpr(expr))
  }
}
