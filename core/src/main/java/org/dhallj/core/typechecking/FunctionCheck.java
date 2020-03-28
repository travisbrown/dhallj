package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;

public final class FunctionCheck {

  public static Universe check(Universe input, Universe output) {
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
