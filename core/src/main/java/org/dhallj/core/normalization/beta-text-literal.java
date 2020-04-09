package org.dhallj.core.normalization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dhallj.core.Expr;
import org.dhallj.core.visitor.ConstantVisitor;

final class BetaNormalizeTextLiteral {
  static final Expr apply(String[] parts, List<Expr> interpolated) {
    if (parts.length == 1) {
      return Expr.makeTextLiteral(parts[0]);
    } else {
      int partsSize = 0;
      for (String part : parts) {
        partsSize += part.length();
      }
      int c = 0;
      if (partsSize == 0) {
        Expr notEmptyString = null;
        boolean tooMany = false;
        Iterator<Expr> it = interpolated.iterator();

        while (it.hasNext() && !tooMany) {
          Expr next = it.next();
          String nextAsSimpleTextLiteral = Expr.Util.asSimpleTextLiteral(next);

          if (nextAsSimpleTextLiteral == null || nextAsSimpleTextLiteral.length() != 0) {
            if (notEmptyString == null) {
              notEmptyString = next;
            } else {
              tooMany = true;
            }
          }
        }

        if (!tooMany && notEmptyString != null) {
          return notEmptyString;
        }
      }

      final List<String> newParts = new ArrayList(parts.length);
      final List<Expr> newInterpolated = new ArrayList(parts.length - 1);
      newParts.add(parts[0]);

      boolean wasInlined = false;
      int partIndex = 1;

      for (Expr expr : interpolated) {
        wasInlined =
            expr.acceptExternal(new InlineInterpolatedTextLiteral(newParts, newInterpolated));

        if (!wasInlined) {
          newInterpolated.add(expr);
          newParts.add(parts[partIndex++]);
        } else {
          int lastIndex = newParts.size() - 1;
          String lastPart = newParts.get(lastIndex);
          newParts.set(lastIndex, lastPart + parts[partIndex++]);
        }
      }

      return Expr.makeTextLiteral(newParts.toArray(new String[newParts.size()]), newInterpolated);
    }
  }

  private static final class InlineInterpolatedTextLiteral
      extends ConstantVisitor.External<Boolean> {
    private final List<String> newParts;
    private final List<Expr> newInterpolated;

    InlineInterpolatedTextLiteral(List<String> newParts, List<Expr> newInterpolated) {
      super(false);
      this.newParts = newParts;
      this.newInterpolated = newInterpolated;
    }

    @Override
    public Boolean onTextLiteral(String[] parts, Iterable<Expr> interpolated) {
      int lastIndex = newParts.size() - 1;
      String lastPart = newParts.get(lastIndex);
      newParts.set(lastIndex, lastPart + parts[0]);

      Iterator<Expr> it = interpolated.iterator();

      for (int i = 1; i < parts.length; i++) {
        newInterpolated.add(it.next());
        newParts.add(parts[i]);
      }
      return true;
    }
  }
}
