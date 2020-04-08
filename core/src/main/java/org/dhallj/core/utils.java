package org.dhallj.core;

import java.lang.reflect.Array;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/** Static utility classes and methods for internal use. */
class ExprUtilities {
  static final class IdentityArray<A> extends MappedArray<A, A> {
    IdentityArray(A[] values) {
      super(values);
    }

    protected final A map(A in) {
      return in;
    }
  }

  private abstract static class MappedArray<I, O> implements Iterable<O> {
    private final I[] values;

    MappedArray(I[] values) {
      this.values = values;
    }

    protected abstract O map(I in);

    public final Iterator<O> iterator() {
      return new Iterator<O>() {
        private int i = 0;

        public final boolean hasNext() {
          return i < values.length;
        }

        public final O next() {
          return MappedArray.this.map(values[i++]);
        }
      };
    }
  }

  static Expr[] exprsToArray(Iterable<Expr> values) {
    List<Expr> result = new ArrayList();

    for (Expr value : values) {
      result.add(value);
    }

    return result.toArray(new Expr[result.size()]);
  }

  static Entry<String, Expr>[] entryToArray(String key, Expr value) {
    Entry<String, Expr>[] arr = (Entry<String, Expr>[]) Array.newInstance(Entry.class, 1);
    arr[0] = new SimpleImmutableEntry(key, value);

    return arr;
  }

  static Entry<String, Expr>[] entriesToArray(Iterable<Entry<String, Expr>> entries) {
    List<Entry<String, Expr>> result = new ArrayList();

    for (Entry<String, Expr> entry : entries) {
      result.add(entry);
    }

    return result.toArray((Entry<String, Expr>[]) Array.newInstance(Entry.class, result.size()));
  }
}
