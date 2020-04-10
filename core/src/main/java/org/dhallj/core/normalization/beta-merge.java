package org.dhallj.core.normalization;

import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.ExternalVisitor;
import org.dhallj.core.Operator;

final class BetaNormalizeMerge {
  static final Expr apply(Expr left, Expr right, Expr type) {
    final List<Entry<String, Expr>> leftAsRecord = Expr.Util.asRecordLiteral(left);

    if (leftAsRecord != null) {
      Expr result =
          right.accept(
              new ExternalVisitor.Constant<Expr>(null) {
                @Override
                public Expr onFieldAccess(Expr base, String fieldName) {
                  List<Entry<String, Expr>> baseAsUnion = Expr.Util.asUnionType(base);

                  if (baseAsUnion != null) {
                    return merge(leftAsRecord, fieldName);
                  } else {
                    return null;
                  }
                }

                @Override
                public Expr onApplication(Expr base, Expr arg) {
                  Entry<Expr, String> baseAsFieldAccess = Expr.Util.asFieldAccess(base);
                  if (baseAsFieldAccess != null) {
                    List<Entry<String, Expr>> accessedAsUnion =
                        Expr.Util.asUnionType(baseAsFieldAccess.getKey());

                    if (accessedAsUnion != null) {
                      return merge(leftAsRecord, baseAsFieldAccess.getValue(), arg);
                    } else {
                      return null;
                    }

                  } else {
                    String baseAsBuiltIn = Expr.Util.asBuiltIn(base);

                    if (baseAsBuiltIn != null) {
                      if (baseAsBuiltIn.equals("Some")) {
                        return merge(leftAsRecord, baseAsBuiltIn, arg);
                      } else if (baseAsBuiltIn.equals("None")) {
                        return merge(leftAsRecord, baseAsBuiltIn);
                      }
                    }
                    return null;
                  }
                }
              });

      if (result != null) {
        return result.accept(BetaNormalize.instance);
      }
    }
    return Expr.makeMerge(left, right, type);
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
