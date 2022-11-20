package org.dhallj.core.normalization;

import java.util.List;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;

/**
 * Shifts all instances of a variable.
 *
 * <p>Note that this visitor maintains internal state and instances should not be reused.
 */
public final class Shift extends Visitor.Identity {
  private final int change;
  private final String name;
  private int cutoff = 0;

  public Shift(boolean isIncrement, String name) {
    this.change = isIncrement ? 1 : -1;
    this.name = name;
  }

  @Override
  public Expr onIdentifier(Expr self, String name, long index) {
    if (name.equals(this.name) && index >= this.cutoff) {
      return Expr.makeIdentifier(name, index + this.change);
    } else {
      return self;
    }
  }

  @Override
  public void bind(String name, Expr type) {
    if (name.equals(this.name)) {
      this.cutoff += 1;
    }
  }

  @Override
  public Expr onLambda(String name, Expr type, Expr result) {
    if (name.equals(this.name)) {
      this.cutoff -= 1;
    }

    return Expr.makeLambda(name, type, result);
  }

  @Override
  public Expr onPi(String name, Expr type, Expr result) {
    if (name.equals(this.name)) {
      this.cutoff -= 1;
    }

    return Expr.makePi(name, type, result);
  }

  @Override
  public Expr onLet(List<Expr.LetBinding<Expr>> bindings, Expr body) {
    for (Expr.LetBinding<Expr> binding : bindings) {
      if (binding.getName().equals(this.name)) {
        this.cutoff -= 1;
      }
    }
    return Expr.makeLet(bindings, body);
  }
}
