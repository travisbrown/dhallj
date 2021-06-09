package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;

final class Context {
  private final String key;
  private final Expr value;
  private final Context tail;

  Context(String key, Expr value, Context tail) {
    this.key = key;
    this.value = value;
    this.tail = tail;
  }

  public Expr lookup(String targetKey, long index) {
    if (this.key != null && this.key.equals(targetKey)) {
      if (index == 0) {
        return this.value;
      } else {
        if (this.tail == null) {
          return null;
        } else {
          return this.tail.lookup(targetKey, index - 1);
        }
      }
    } else {
      if (this.tail == null) {
        return null;
      } else {
        return this.tail.lookup(targetKey, index);
      }
    }
  }

  public Context insert(String key, Expr value) {
    return new Context(key, value, this);
  }

  public Context increment(String name) {
    if (this.key == null) {
      return this;
    } else {
      return new Context(this.key, this.value.increment(name), this.tail.increment(name));
    }
  }

  public Context decrement(String name) {
    if (this.key == null) {
      return this;
    } else {
      return new Context(this.key, this.value.decrement(name), this.tail.decrement(name));
    }
  }

  public static final Context EMPTY = new Context(null, null, null);
}
