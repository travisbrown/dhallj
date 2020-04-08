package org.dhallj.core.util;

import java.util.Iterator;

public final class ArrayIterator<A> implements Iterable<A> {
  private final A[] values;

  public ArrayIterator(A[] values) {
    this.values = values;
  }

  public final Iterator<A> iterator() {
    return new Iterator<A>() {
      private int i = 0;

      public final boolean hasNext() {
        return this.i < ArrayIterator.this.values.length;
      }

      public final A next() {
        return ArrayIterator.this.values[this.i++];
      }
    };
  }
}
