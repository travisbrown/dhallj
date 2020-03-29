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
 * Shifts all instances of a variable.
 *
 * <p>Note that this visitor maintains internal state and instances should not be reused.
 */
public final class Shift extends IdentityVisitor.Internal {
  private final int d;
  private final String name;
  private int m = 0;

  public Shift(boolean isIncrement, String name) {
    this.d = isIncrement ? 1 : -1;
    this.name = name;
  }

  public Expr onIdentifier(String value, long index) {
    if (value.equals(this.name) && index >= this.m) {
      return Expr.makeIdentifier(value, index + this.d);
    } else {
      return Expr.makeIdentifier(value, index);
    }
  }

  public Expr onLambda(String param, Thunk<Expr> input, Thunk<Expr> result) {
    Expr inputEval = input.apply();
    Expr resultEval;
    if (param.equals(this.name)) {
      this.m += 1;
      resultEval = result.apply();
      this.m -= 1;
    } else {
      resultEval = result.apply();
    }

    return Expr.makeLambda(param, inputEval, resultEval);
  }

  public Expr onPi(String param, Thunk<Expr> input, Thunk<Expr> result) {
    Expr inputEval = input.apply();
    Expr resultEval;
    if (param.equals(this.name)) {
      this.m += 1;
      resultEval = result.apply();
      this.m -= 1;
    } else {
      resultEval = result.apply();
    }

    return Expr.makePi(param, inputEval, resultEval);
  }

  public Expr onLet(String param, Thunk<Expr> type, Thunk<Expr> value, Thunk<Expr> body) {
    Expr typeEval = type.apply();
    Expr valueEval = value.apply();
    Expr bodyEval;
    if (name.equals(this.name)) {
      this.m += 1;
      bodyEval = body.apply();
      this.m -= 1;
    } else {
      bodyEval = body.apply();
    }

    return Expr.makeLet(param, typeEval, valueEval, bodyEval);
  }
}
