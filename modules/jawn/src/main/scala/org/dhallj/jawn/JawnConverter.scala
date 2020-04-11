package org.dhallj.jawn

import org.dhallj.core.Expr
import org.dhallj.core.converters.JsonConverter
import org.typelevel.jawn.Facade

class JawnConverter[J](facade: Facade[J]) {
  def apply(expr: Expr): Option[J] = {
    val handler = new FacadeHandler(facade)
    val wasConverted = expr.accept(new JsonConverter(handler))

    if (wasConverted) Some(handler.result) else None
  }
}
