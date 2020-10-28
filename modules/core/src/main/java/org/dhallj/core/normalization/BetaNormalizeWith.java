package org.dhallj.core.normalization;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;

final class BetaNormalizeWith {
  static final Expr apply(Expr base, String[] path, Expr value) {
    List<Entry<String, Expr>> baseAsRecordLiteral = Expr.Util.asRecordLiteral(base);

    if (baseAsRecordLiteral != null) {
      List<Entry<String, Expr>> result = new ArrayList<>();
      boolean found = false;

      for (Entry<String, Expr> field : baseAsRecordLiteral) {
        String key = field.getKey();

        if (key.equals(path[0])) {
          found = true;

          Expr newValue;

          if (path.length == 1) {
            newValue = value;
          } else {
            String[] remainingPath = new String[path.length - 1];
            System.arraycopy(path, 1, remainingPath, 0, path.length - 1);

            newValue =
                Expr.makeWith(field.getValue(), remainingPath, value)
                    .accept(BetaNormalize.instance);
          }

          result.add(new SimpleImmutableEntry<>(key, newValue));
        } else {
          result.add(field);
        }
      }

      if (!found) {
        Expr newValue;

        if (path.length == 1) {
          newValue = value;
        } else {
          String[] remainingPath = new String[path.length - 1];
          System.arraycopy(path, 1, remainingPath, 0, path.length - 1);

          newValue =
              Expr.makeWith(Expr.Constants.EMPTY_RECORD_LITERAL, remainingPath, value)
                  .accept(BetaNormalize.instance);
        }

        result.add(new SimpleImmutableEntry<>(path[0], newValue));
      }

      Entry<String, Expr>[] resultArray = result.toArray(new Entry[result.size()]);

      Arrays.sort(resultArray, entryComparator);

      return Expr.makeRecordLiteral(resultArray);
    } else {
      return Expr.makeWith(base, path, value);
    }
  }

  /** Java 8 introduce {@code comparingByKey}, but we can roll our own pretty easily. */
  private static final Comparator<Entry<String, Expr>> entryComparator =
      new Comparator<Entry<String, Expr>>() {
        public int compare(Entry<String, Expr> a, Entry<String, Expr> b) {
          return a.getKey().compareTo(b.getKey());
        }
      };
}
