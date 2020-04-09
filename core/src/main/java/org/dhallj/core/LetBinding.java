package org.dhallj.core;

/** Represents the first part of a {@code let}-expression. */
public final class LetBinding<A> {
  private final String name;
  private final A type;
  private final A value;

  public LetBinding(String name, A type, A value) {
    this.name = name;
    this.type = type;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public boolean hasType() {
    return this.type != null;
  }

  public A getType() {
    return this.type;
  }

  public A getValue() {
    return this.value;
  }
}
