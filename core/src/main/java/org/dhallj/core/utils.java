package org.dhallj.core;

import java.lang.reflect.Array;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/** Static utility classes and methods for internal use. */
class ExprUtilities {
  static final class ExprThunk<A> implements Thunk<A> {
    private final Visitor<Thunk<A>, A> visitor;
    private final Expr expr;

    ExprThunk(Visitor<Thunk<A>, A> visitor, Expr expr) {
      this.visitor = visitor;
      this.expr = expr;
    }

    public final A apply() {
      return (this.expr == null) ? null : this.expr.accept(this.visitor);
    }
  }

  static final class MappedExprArray<A> extends MappedArray<Expr, Thunk<A>> {
    private final Visitor<Thunk<A>, A> visitor;

    MappedExprArray(Visitor<Thunk<A>, A> visitor, Expr[] values) {
      super(values);
      this.visitor = visitor;
    }

    protected final Thunk<A> map(Expr in) {
      return new ExprThunk(this.visitor, in);
    }
  }

  static final class MappedEntryArray<A>
      extends MappedArray<Entry<String, Expr>, Entry<String, Thunk<A>>> {
    private final Visitor<Thunk<A>, A> visitor;

    MappedEntryArray(Visitor<Thunk<A>, A> visitor, Entry<String, Expr>[] values) {
      super(values);
      this.visitor = visitor;
    }

    protected final Entry<String, Thunk<A>> map(Entry<String, Expr> in) {
      Expr value = in.getValue();
      Thunk<A> newValue = new ExprThunk(this.visitor, value);
      return new SimpleImmutableEntry(in.getKey(), newValue);
    }
  }

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

  static Entry<String, Expr>[] entriesToArrayThunked(Iterable<Entry<String, Thunk<Expr>>> entries) {
    List<Entry<String, Expr>> result = new ArrayList();

    for (Entry<String, Thunk<Expr>> entry : entries) {
      Entry<String, Expr> unthunked =
          new SimpleImmutableEntry(entry.getKey(), entry.getValue().apply());
      result.add(unthunked);
    }

    return result.toArray((Entry<String, Expr>[]) Array.newInstance(Entry.class, result.size()));
  }
}
