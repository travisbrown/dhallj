package org.dhallj.core.normalization;

import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.ast.AsFieldAccess;
import org.dhallj.core.util.FieldUtilities;
import org.dhallj.core.visitor.ConstantVisitor;

final class BetaNormalizeMerge {
  static final Expr apply(Expr left, Expr right, Expr type) {
    final Iterable<Entry<String, Expr>> leftAsRecord = left.asRecordLiteral();

    if (leftAsRecord != null) {
      Expr result =
          right.acceptExternal(
              new ConstantVisitor.External<Expr>(null) {
                @Override
                public Expr onFieldAccess(Expr base, String fieldName) {
                  Iterable<Entry<String, Expr>> baseAsUnion = base.asUnionType();

                  if (baseAsUnion != null) {
                    return merge(leftAsRecord, fieldName);
                  } else {
                    return null;
                  }
                }

                @Override
                public Expr onApplication(Expr base, Expr arg) {
                  Entry<Expr, String> baseAsFieldAccess =
                      base.acceptExternal(AsFieldAccess.instance);
                  if (baseAsFieldAccess != null) {
                    Iterable<Entry<String, Expr>> accessedAsUnion =
                        baseAsFieldAccess.getKey().asUnionType();

                    if (accessedAsUnion != null) {
                      return merge(leftAsRecord, baseAsFieldAccess.getValue(), arg);
                    } else {
                      return null;
                    }

                  } else {
                    String baseAsIdentifier = base.asSimpleIdentifier();

                    if (baseAsIdentifier != null) {
                      if (baseAsIdentifier.equals("Some")) {
                        return merge(leftAsRecord, baseAsIdentifier, arg);
                      } else if (baseAsIdentifier.equals("None")) {
                        return merge(leftAsRecord, baseAsIdentifier);
                      }
                    }
                    return null;
                  }
                }
              });

      if (result != null) {
        return result.acceptVis(BetaNormalize.instance);
      }
    }
    return Expr.makeMerge(left, right, type);
  }

  private static Expr merge(Iterable<Entry<String, Expr>> handlers, String fieldName) {
    return FieldUtilities.lookup(handlers, fieldName);
  }

  private static Expr merge(Iterable<Entry<String, Expr>> handlers, String fieldName, Expr arg) {
    Expr handler = FieldUtilities.lookup(handlers, fieldName);
    if (handler != null) {
      return Expr.makeApplication(handler, arg);
    } else {
      return null;
    }
  }
}
