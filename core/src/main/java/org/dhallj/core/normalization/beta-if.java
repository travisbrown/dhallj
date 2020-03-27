package org.dhallj.core.normalization;

import org.dhallj.core.Expr;

final class BetaNormalizeIf {
  static final Expr apply(Expr cond, Expr thenValue, Expr elseValue) {
    Boolean condAsBool = cond.asBoolLiteral();

    if (condAsBool != null) {
      return (condAsBool) ? thenValue : elseValue;
    } else {
      Boolean thenAsBool = thenValue.asBoolLiteral();
      Boolean elseAsBool = elseValue.asBoolLiteral();

      if (thenAsBool != null && elseAsBool != null && thenAsBool && !elseAsBool) {
        return cond;
      } else if (thenValue.equivalent(elseValue)) {
        return thenValue;
      } else {
        return Expr.makeIf(cond, thenValue, elseValue);
      }
    }
  }
}
