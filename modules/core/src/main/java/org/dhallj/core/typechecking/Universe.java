package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;

public enum Universe {
  TYPE,
  KIND,
  SORT;

  public final Universe max(Universe other) {
    if (this == SORT || other == SORT) {
      return SORT;
    } else if (this == KIND || other == KIND) {
      return KIND;
    } else {
      return TYPE;
    }
  }

  public final Expr toExpr() {
    if (this == TYPE) {
      return Expr.Constants.TYPE;
    } else if (this == KIND) {
      return Expr.Constants.KIND;
    } else {
      return Expr.Constants.SORT;
    }
  }

  public static final boolean isUniverse(Expr expr) {
    return fromExpr(expr) != null;
  }

  public static final Universe fromExpr(Expr expr) {
    String name = Expr.Util.asBuiltIn(expr);

    if (name != null) {
      if (name.equals("Type")) {
        return TYPE;
      } else if (name.equals("Kind")) {
        return KIND;
      } else if (name.equals("Sort")) {
        return SORT;
      }
    }
    return null;
  }

  public static Universe functionCheck(Universe input, Universe output) {
    if (output == Universe.TYPE) {
      return Universe.TYPE;
    } else {
      return input.max(output);
    }
  }
}
