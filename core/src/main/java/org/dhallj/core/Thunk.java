package org.dhallj.core;

/**
 * Represents a computation.
 *
 * <p>Like the standard library's callable type, but without the checked exception.
 *
 * @see java.util.concurrent.Callable
 */
public interface Thunk<A> {
  public A apply();

  static final class Constant<A> implements Thunk<A> {
    private final A value;

    Constant(A value) {
      this.value = value;
    }

    public A apply() {
      return this.value;
    }
  }
}
