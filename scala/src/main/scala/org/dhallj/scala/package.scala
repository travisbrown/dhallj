package org.dhallj

import org.dhallj.core.Expr
import org.dhallj.parser.Dhall

package object scala {
implicit class ExprExtensions(input: String) {
  def asFunction[I: Codec, O: Codec]: Either[Codec.Error, I => Either[Codec.Error, O]] =
    try {
      val parsed = Dhall.parse(input)

      Right(
      (i: I) => {
        Codec[I].encode(i).flatMap { encoded =>
          Codec[O].decode(Expr.makeApplication(parsed, encoded).normalize)
        }
      }
    )
    } catch {
      case t: Throwable => Left(t)
    }
}
}