package org.dhallj.core.normalization;

import java.util.Comparator;
import java.util.Map.Entry;
import org.dhallj.core.Expr;

/** Static utility classes and methods for internal use. */
final class NormalizationUtilities {
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
