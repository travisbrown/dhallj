package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;

public enum Universe {
  TYPE,
  KIND,
  SORT;

  public final Universe max(Universe other) {
    if (this.equals(SORT) || other.equals(SORT)) {
      return SORT;
    } else if (this.equals(KIND) || other.equals(KIND)) {
      return KIND;
    } else {
      return TYPE;
    }
  }

  public final Expr toExpr() {
    switch (this) {
      case TYPE:
        return Expr.Constants.TYPE;
      case KIND:
        return Expr.Constants.KIND;
      case SORT:
        return Expr.Constants.SORT;
      default:
        return null;
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
    if (output.equals(Universe.TYPE)) {
      return Universe.TYPE;
    } else {
      return input.max(output);
    }
  }
}
