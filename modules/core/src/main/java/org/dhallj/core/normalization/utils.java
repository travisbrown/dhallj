package org.dhallj.core.normalization;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;

/** Static utility classes and methods for internal use. */
final class NormalizationUtilities {
  static final Expr booleanToExpr(boolean value) {
    return value ? Expr.Constants.TRUE : Expr.Constants.FALSE;
  }

  static final Expr[] prependValue = new Expr[] {Expr.makeIdentifier("a")};
  static final Expr prependExpr =
      Expr.makeOperatorApplication(
          Operator.LIST_APPEND,
          Expr.makeNonEmptyListLiteral(prependValue),
          Expr.makeIdentifier("as"));

  static final Expr indexedRecordType(Expr type) {
    Entry[] fields = {
      new SimpleImmutableEntry("index", Expr.Constants.NATURAL),
      new SimpleImmutableEntry("value", type)
    };
    return Expr.makeRecordType(fields);
  }

  static final <A> A lookup(Iterable<Entry<String, A>> entries, String key) {
    for (Entry<String, A> entry : entries) {
      if (entry.getKey().equals(key)) {
        return entry.getValue();
      }
    }
    return null;
  }

  static final <A> Entry<String, A> lookupEntry(Iterable<Entry<String, A>> entries, String key) {
    for (Entry<String, A> entry : entries) {
      if (entry.getKey().equals(key)) {
        return entry;
      }
    }
    return null;
  }

  static final Comparator<Entry<String, Expr>> entryComparator =
      new Comparator<Entry<String, Expr>>() {
        public int compare(Entry<String, Expr> a, Entry<String, Expr> b) {
          return a.getKey().compareTo(b.getKey());
        }
      };
}
