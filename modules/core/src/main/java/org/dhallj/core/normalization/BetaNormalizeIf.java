package org.dhallj.core.normalization;

import org.dhallj.core.Expr;

final class BetaNormalizeIf {
  static final Expr apply(Expr predicate, Expr thenValue, Expr elseValue) {
    Boolean predicateAsBool = Expr.Util.asBoolLiteral(predicate);

    if (predicateAsBool != null) {
      return (predicateAsBool) ? thenValue : elseValue;
    } else {
      Boolean thenAsBool = Expr.Util.asBoolLiteral(thenValue);
      Boolean elseAsBool = Expr.Util.asBoolLiteral(elseValue);

      if (thenAsBool != null && elseAsBool != null && thenAsBool && !elseAsBool) {
        return predicate;
      } else if (thenValue.equivalent(elseValue)) {
        return thenValue;
      } else {
        return Expr.makeIf(predicate, thenValue, elseValue);
      }
    }
  }
}
