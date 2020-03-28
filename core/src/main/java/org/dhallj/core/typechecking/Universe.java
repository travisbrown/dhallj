package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public enum Universe {
  TYPE,
  KIND,
  SORT;

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
    return expr.acceptExternal(FromExpr.instance) != null;
  }

  public static final Universe fromExpr(Expr expr) {
    return expr.acceptExternal(FromExpr.instance);
  }

  private static class FromExpr extends ConstantVisitor.External {
    private static final Visitor<Expr, Universe> instance = new FromExpr();

    FromExpr() {
      super(null);
    }

    @Override
    public final Universe onIdentifier(String name, long index) {
      if (index == 0) {
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
  }
}
