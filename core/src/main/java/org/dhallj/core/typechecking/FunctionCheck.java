package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;

public final class FunctionCheck {

  public static Universe check(Universe input, Universe output) {
    if (output.equals(Universe.TYPE)) {
      return Universe.TYPE;
    } else {
      return input.max(output);
    }
  }
}
