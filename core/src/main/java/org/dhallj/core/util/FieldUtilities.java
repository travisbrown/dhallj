package org.dhallj.core.util;

import java.lang.reflect.Array;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.dhallj.core.Expr;
import org.dhallj.core.Thunk;

/**
 * Utility methods related to fields that are useful across packages.
 *
 * <p>This shouldn't exist.
 */
public final class FieldUtilities {

  public static final <A> Entry<String, A>[] prefer(
      Iterable<Entry<String, A>> base, Iterable<Entry<String, A>> updates) {
    Map<String, A> updateMap = new LinkedHashMap();

    for (Entry<String, A> field : updates) {
      updateMap.put(field.getKey(), field.getValue());
    }

    List<Entry<String, A>> result = new ArrayList();

    for (Entry<String, A> field : base) {
      String key = field.getKey();

      A inUpdates = updateMap.remove(key);

      if (inUpdates == null) {
        result.add(field);
      } else {
        result.add(new SimpleImmutableEntry(key, inUpdates));
      }
    }

    for (Entry<String, A> field : updateMap.entrySet()) {
      result.add(field);
    }

    return result.toArray((Entry<String, A>[]) Array.newInstance(Entry.class, result.size()));
  }

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
