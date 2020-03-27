package org.dhallj.core.normalization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dhallj.core.Expr;
import org.dhallj.core.Thunk;

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

      List<String> newParts = new ArrayList(parts.length);
      List<Expr> newInterpolated = new ArrayList(parts.length - 1);

      int partIndex = 0;
      newParts.add(parts[partIndex++]);

      for (Thunk<Expr> thunk : interpolated) {
        Expr expr = thunk.apply();
        String nextAsSimpleTextLiteral = expr.asSimpleTextLiteral();

        if (nextAsSimpleTextLiteral != null) {
          String current = newParts.get(newParts.size() - 1);
          newParts.set(newParts.size() - 1, current + nextAsSimpleTextLiteral + parts[partIndex++]);
        } else {
          newParts.add(parts[partIndex++]);
          newInterpolated.add(expr);
        }
      }

      return Expr.makeTextLiteral(newParts.toArray(new String[newParts.size()]), newInterpolated);
    }
  }
}
