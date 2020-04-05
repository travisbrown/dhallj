package org.dhallj.core.normalization;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.LetBinding;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.visitor.IdentityVis;

/**
 * Shifts all instances of a variable.
 *
 * <p>Note that this visitor maintains internal state and instances should not be reused.
 */
public final class Shift extends IdentityVis {
  private final int change;
  private final String name;
  private int cutoff = 0;

  public Shift(boolean isIncrement, String name) {
    this.change = isIncrement ? 1 : -1;
    this.name = name;
  }

  @Override
  public Expr onIdentifier(String value, long index) {
    if (value.equals(this.name) && index >= this.cutoff) {
      return Expr.makeIdentifier(value, index + this.change);
    } else {
      return Expr.makeIdentifier(value, index);
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
  public Expr onLet(List<LetBinding<Expr>> bindings, Expr body) {
    for (LetBinding<Expr> binding : bindings) {
      if (binding.getName().equals(this.name)) {
        this.cutoff -= 1;
      }
    }
    return Expr.makeLet(bindings, body);
  }
}
