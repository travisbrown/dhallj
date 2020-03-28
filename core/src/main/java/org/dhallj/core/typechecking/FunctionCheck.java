package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;

public final class FunctionCheck {
  public enum Universe {
    TYPE,
    KIND,
    SORT;

    public static Universe fromExpr(Expr expr) {
      if (expr.equivalent(Expr.Constants.TYPE)) {
        return TYPE;
      } else if (expr.equivalent(Expr.Constants.KIND)) {
        return KIND;
      } else if (expr.equivalent(Expr.Constants.SORT)) {
        return SORT;
      } else {
        return null;
      }
    }
  }

  public Universe check(Universe input, Universe output) {
    if (output.equals(Universe.TYPE)) {
      return Universe.TYPE;
    } else if (output.equals(Universe.SORT)) {
      return Universe.SORT;
    } else {
      if (input.equals(Universe.KIND)) {
        return Universe.KIND;
      } else if (input.equals(Universe.TYPE)) {
        return Universe.KIND;
      } else {
        return Universe.SORT;
      }
    }
  }
}
