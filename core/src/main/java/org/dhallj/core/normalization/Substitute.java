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
import org.dhallj.core.Thunk;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.IdentityVisitor;

/**
 * Substitutes an expression for all instances of a variable in another expression.
 *
 * <p>Note that this visitor maintains internal state and instances should not be reused.
 */
public final class Substitute extends IdentityVisitor.Internal {
  private final String name;
  private int index;
  private Expr replacement;

  public static Expr substitute(String name, int index, Expr replacement, Expr value) {
    return value.accept(new Substitute(name, index, replacement));
  }

  public Substitute(String name, int index, Expr replacement) {
    this.name = name;
    this.index = index;
    this.replacement = replacement;
  }

  public Expr onIdentifier(String value, long index) {
    if (value.equals(this.name) && index == this.index) {
      return this.replacement;
    } else {
      return Expr.makeIdentifier(value, index);
    }
  }

  public Expr onLambda(String param, Thunk<Expr> input, Thunk<Expr> result) {
    Expr inputEval = input.apply();
    Expr resultEval;
    Expr unshifted = this.replacement;
    this.replacement = this.replacement.increment(param);
    if (param.equals(this.name)) {
      this.index += 1;
      resultEval = result.apply();
      this.index -= 1;
    } else {
      resultEval = result.apply();
    }
    this.replacement = unshifted;

    return Expr.makeLambda(param, inputEval, resultEval);
  }

  public Expr onPi(String param, Thunk<Expr> input, Thunk<Expr> result) {
    Expr inputEval = input.apply();
    Expr resultEval;
    Expr unshifted = this.replacement;
    this.replacement = this.replacement.increment(param);
    if (param.equals(this.name)) {
      this.index += 1;
      resultEval = result.apply();
      this.index -= 1;
    } else {
      resultEval = result.apply();
    }
    this.replacement = unshifted;

    return Expr.makePi(param, inputEval, resultEval);
  }

  public Expr onLet(String name, Thunk<Expr> type, Thunk<Expr> value, Thunk<Expr> body) {
    Expr typeEval = type.apply();
    Expr valueEval = value.apply();
    Expr bodyEval;
    Expr unshifted = this.replacement;
    this.replacement = this.replacement.increment(name);

    if (name.equals(this.name)) {
      this.index += 1;
      bodyEval = body.apply();
      this.index -= 1;

    } else {
      bodyEval = body.apply();
    }
    this.replacement = unshifted;

    return Expr.makeLet(name, typeEval, valueEval, bodyEval);
  }
}
