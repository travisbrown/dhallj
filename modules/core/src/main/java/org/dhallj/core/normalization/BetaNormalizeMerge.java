package org.dhallj.core.normalization;

import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.ExternalVisitor;
import org.dhallj.core.Operator;

final class BetaNormalizeMerge extends ExternalVisitor.Constant<Expr> {
  private final List<Entry<String, Expr>> handlers;

  private BetaNormalizeMerge(List<Entry<String, Expr>> handlers) {
    super(null);
    this.handlers = handlers;
  }

  static final Expr apply(Expr handlers, Expr union, Expr type) {
    List<Entry<String, Expr>> fields = Expr.Util.asRecordLiteral(handlers);

    Expr result = union.accept(new BetaNormalizeMerge(fields));

    if (result != null) {
      return result.accept(BetaNormalize.instance);
    } else {

      return Expr.makeMerge(handlers, union, type);
    }
  }

  @Override
  public Expr onFieldAccess(Expr base, String fieldName) {
    List<Entry<String, Expr>> baseAsUnion = Expr.Util.asUnionType(base);

    if (baseAsUnion != null) {
      return merge(this.handlers, fieldName);
    } else {
      return null;
    }
  }

  @Override
  public Expr onApplication(Expr base, Expr arg) {
    Entry<Expr, String> baseAsFieldAccess = Expr.Util.asFieldAccess(base);
    if (baseAsFieldAccess != null) {
      List<Entry<String, Expr>> accessedAsUnion = Expr.Util.asUnionType(baseAsFieldAccess.getKey());

      if (accessedAsUnion != null) {
        return merge(this.handlers, baseAsFieldAccess.getValue(), arg);
      } else {
        return null;
      }

    } else {
      String baseAsBuiltIn = Expr.Util.asBuiltIn(base);

      if (baseAsBuiltIn != null) {
        if (baseAsBuiltIn.equals("Some")) {
          return merge(this.handlers, baseAsBuiltIn, arg);
        } else if (baseAsBuiltIn.equals("None")) {
          return merge(this.handlers, baseAsBuiltIn);
        }
      }
      return null;
    }
  }

  private static Expr merge(List<Entry<String, Expr>> handlers, String fieldName) {
    return NormalizationUtilities.lookup(handlers, fieldName);
  }

  private static Expr merge(List<Entry<String, Expr>> handlers, String fieldName, Expr arg) {
    Expr handler = NormalizationUtilities.lookup(handlers, fieldName);
    if (handler != null) {
      return Expr.makeApplication(handler, arg);
    } else {
      return null;
    }
  }
}
