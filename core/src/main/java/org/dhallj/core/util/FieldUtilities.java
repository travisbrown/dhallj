package org.dhallj.core.util;

import java.util.Comparator;
import java.util.Map.Entry;
import org.dhallj.core.Expr;

/**
 * Utility methods related to fields that are useful across packages.
 *
 * <p>This shouldn't exist.
 */
public final class FieldUtilities {
  public static <A> A lookup(Iterable<Entry<String, A>> entries, String key) {
    for (Entry<String, A> entry : entries) {
      if (entry.getKey().equals(key)) {
        return entry.getValue();
      }
    }
    return null;
  }

  public static <A> Entry<String, A> lookupEntry(Iterable<Entry<String, A>> entries, String key) {
    for (Entry<String, A> entry : entries) {
      if (entry.getKey().equals(key)) {
        return entry;
      }
    }
    return null;
  }

  public static final Comparator<Entry<String, Expr>> entryComparator =
      new Comparator<Entry<String, Expr>>() {
        public int compare(Entry<String, Expr> a, Entry<String, Expr> b) {
          return a.getKey().compareTo(b.getKey());
        }
      };
}
