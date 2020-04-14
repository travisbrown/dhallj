package org.dhallj.core.normalization;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;

final class BetaNormalizeToMap {
  static final Expr apply(Expr base, Expr type) {
    Iterable<Entry<String, Expr>> baseAsRecordLiteral = Expr.Util.asRecordLiteral(base);

    if (baseAsRecordLiteral != null) {

      List<Expr> result = new ArrayList();

      for (Entry<String, Expr> entry : baseAsRecordLiteral) {
        result.add(makeRecord(entry.getKey(), entry.getValue()));
      }

      if (result.isEmpty()) {
        return Expr.makeEmptyListLiteral(type);
      } else {
        return Expr.makeNonEmptyListLiteral(result);
      }
    } else {
      return Expr.makeToMap(base, type);
    }
  }

  private static final String escape(String input) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);

      if (c == '\\') {
        char next = input.charAt(++i);

        if (next == '\\') {
          builder.append("\\\\");
        } else if (next == 'n') {
          builder.append("\\\\n");
        } else if (next == '$') {
          builder.append("\\\\\\$");
        } else {
          builder.append("\\\\");
          builder.append(next);
        }
      } else {
        builder.append(c);
      }
    }

    return builder.toString();
  }

  private static final Expr makeRecord(String key, Expr value) {
    List<Entry<String, Expr>> fields = new ArrayList(2);

    fields.add(
        new SimpleImmutableEntry(
            Expr.Constants.MAP_KEY_FIELD_NAME, Expr.makeTextLiteral(escape(key))));
    fields.add(new SimpleImmutableEntry(Expr.Constants.MAP_VALUE_FIELD_NAME, value));
    return Expr.makeRecordLiteral(fields);
  }
}
