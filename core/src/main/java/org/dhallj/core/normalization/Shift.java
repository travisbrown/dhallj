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
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.visitor.IdentityVis;

/**
 * Shifts all instances of a variable.
 *
 * <p>Note that this visitor maintains internal state and instances should not be reused.
 */
public final class Shift extends IdentityVis {
  private final int d;
  private final String name;
  private int cutoff = 0;

  public Shift(boolean isIncrement, String name) {
    this.d = isIncrement ? 1 : -1;
    this.name = name;
  }

  public Expr onIdentifier(String value, long index) {
    if (value.equals(this.name) && index >= this.cutoff) {
      return Expr.makeIdentifier(value, index + this.d);
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

  public Expr onPi(String name, Expr type, Expr result) {
    if (name.equals(this.name)) {
      this.cutoff -= 1;
    }

    return Expr.makePi(name, type, result);
  }

  public Expr onLet(String name, Expr type, Expr value, Expr body) {
    if (name.equals(this.name)) {
      this.cutoff -= 1;
    }
    return Expr.makeLet(name, type, value, body);
  }
}
