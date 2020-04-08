package org.dhallj.core.normalization;

import java.util.List;
import org.dhallj.core.Expr;
import org.dhallj.core.LetBinding;
import org.dhallj.core.visitor.IdentityVis;

/**
 * Substitutes an expression for all instances of a variable in another expression.
 *
 * <p>Note that this visitor maintains internal state and instances should not be reused.
 */
public final class Substitute extends IdentityVis {
  private final String name;
  private int index;
  private Expr replacement;

  public Substitute(String name, int index, Expr replacement) {
    this.name = name;
    this.index = index;
    this.replacement = replacement;
  }

  @Override
  public void bind(String name, Expr type) {
    this.replacement = this.replacement.increment(name);

    if (name.equals(this.name)) {
      this.index += 1;
    }
  }

  @Override
  public Expr onIdentifier(Expr self, String value, long index) {
    if (value.equals(this.name) && index == this.index) {
      return this.replacement;
    } else {
      return self;
    }
  }

  @Override
  public Expr onLambda(String name, Expr type, Expr result) {
    this.replacement = this.replacement.decrement(name);

    if (name.equals(this.name)) {
      this.index -= 1;
    }

    return Expr.makeLambda(name, type, result);
  }

  @Override
  public Expr onPi(String name, Expr type, Expr result) {
    this.replacement = this.replacement.decrement(name);

    if (name.equals(this.name)) {
      this.index -= 1;
    }

    return Expr.makePi(name, type, result);
  }

  @Override
  public Expr onLet(List<LetBinding<Expr>> bindings, Expr body) {
    for (LetBinding<Expr> binding : bindings) {
      String name = binding.getName();
      this.replacement = this.replacement.decrement(name);
      if (name.equals(this.name)) {
        this.index -= 1;
      }
    }
    return Expr.makeLet(bindings, body);
  }
}
