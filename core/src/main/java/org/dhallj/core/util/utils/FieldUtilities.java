package org.dhallj.core.util;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Thunk;

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

  public static List<Entry<String, Expr>> sortFields(
      Iterable<Entry<String, Expr>> fields, int size) {
    List<Entry<String, Expr>> result = new ArrayList(size);

    for (Entry<String, Expr> entry : fields) {
      result.add(entry);
    }

    Collections.sort(result, entryComparator);
    return result;
  }

  public static List<Entry<String, Expr>> sortAndFlattenFields(
      Iterable<Entry<String, Thunk<Expr>>> fields, int size) {
    List<Entry<String, Expr>> result = new ArrayList(size);

    for (Entry<String, Thunk<Expr>> entry : fields) {
      result.add(new SimpleImmutableEntry(entry.getKey(), entry.getValue().apply()));
    }

    Collections.sort(result, entryComparator);
    return result;
  }

  private static final Comparator<Entry<String, Expr>> entryComparator =
      new Comparator<Entry<String, Expr>>() {
        public int compare(Entry<String, Expr> a, Entry<String, Expr> b) {
          return a.getKey().compareTo(b.getKey());
        }
      };
}
