package org.dhallj.core.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
        try {
          return ArrayIterator.this.values[this.i++];
        } catch (ArrayIndexOutOfBoundsException e) {
          throw new NoSuchElementException();
        }
      }
    };
  }
}
