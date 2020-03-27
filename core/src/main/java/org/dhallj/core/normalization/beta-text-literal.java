package org.dhallj.core.normalization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dhallj.core.Expr;
import org.dhallj.core.Thunk;
import org.dhallj.core.visitor.ConstantVisitor;

final class BetaNormalizeTextLiteral {
  static final Expr apply(String[] parts, Iterable<Thunk<Expr>> interpolated) {
    if (parts.length == 1) {
      return Expr.makeTextLiteralThunked(parts, interpolated);
    } else {
      int partsSize = 0;
      for (String part : parts) {
        partsSize += part.length();
      }
      int c = 0;
      if (partsSize == 0) {
        Expr notEmptyString = null;
        boolean tooMany = false;
        Iterator<Thunk<Expr>> it = interpolated.iterator();

        while (it.hasNext() && !tooMany) {
          Expr next = it.next().apply();
          String nextAsSimpleTextLiteral = next.asSimpleTextLiteral();

          if (nextAsSimpleTextLiteral == null || nextAsSimpleTextLiteral.length() != 0) {
            if (notEmptyString == null) {
              notEmptyString = next;
            } else {
              tooMany = true;
            }
          }
        }

        if (!tooMany) {
          return notEmptyString;
        }
      }

      final List<String> newParts = new ArrayList(parts.length);
      final List<Expr> newInterpolated = new ArrayList(parts.length - 1);
      boolean lastAdded = false;

      int partIndex = 0;
      newParts.add(parts[partIndex++]);

      for (Thunk<Expr> thunk : interpolated) {
        Expr expr = thunk.apply();

        lastAdded =
            expr.acceptExternal(
                new ConstantVisitor.External<Boolean>(false) {
                  @Override
                  public Boolean onTextLiteral(String[] parts, Iterable<Expr> interpolated) {
                    newParts.set(newParts.size() - 1, newParts.get(newParts.size() - 1) + parts[0]);

                    Iterator<Expr> it = interpolated.iterator();

                    for (int i = 1; i < parts.length; i++) {
                      newInterpolated.add(it.next());
                      newParts.add(parts[i]);
                    }
                    return true;
                  }
                });

        if (!lastAdded) {

          newParts.add(parts[partIndex++]);
          newInterpolated.add(expr);

        } else {
          newParts.set(newParts.size() - 1, newParts.get(newParts.size() - 1) + parts[partIndex++]);
        }
      }

      return Expr.makeTextLiteral(newParts.toArray(new String[newParts.size()]), newInterpolated);
    }
  }
}
