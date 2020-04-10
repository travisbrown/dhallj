package org.dhallj.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class ArrayIterable<A> implements Iterable<A> {
  private final A[] values;

  public ArrayIterable(A[] values) {
    this.values = values;
  }

  public final Iterator<A> iterator() {
    return new ArrayIterator<A>(this.values);
  }

  private static final class ArrayIterator<A> implements Iterator<A> {
    private final A[] values;
    private int i = 0;

    ArrayIterator(A[] values) {
      this.values = values;
    }

    public final boolean hasNext() {
      return this.i < this.values.length;
    }

    public final A next() {
      try {
        return this.values[this.i++];
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new NoSuchElementException();
      }
    }

    public final void remove() {
      throw new UnsupportedOperationException("remove");
    }
  }
}
