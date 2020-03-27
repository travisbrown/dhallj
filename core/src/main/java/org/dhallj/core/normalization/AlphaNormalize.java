package org.dhallj.core.normalization;

import org.dhallj.core.Expr;
import org.dhallj.core.Thunk;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.IdentityVisitor;

/**
 * Performs alpha normalization.
 *
 * <p>This is a stateless visitor intended for use as a singleton.
 */
public final class AlphaNormalize extends IdentityVisitor.External.Recursing {
  public static Visitor<Expr, Expr> instance = new AlphaNormalize();

  private static final Expr dename(String name, Expr input) {
    return input.increment("_").substitute(name, 0, Expr.Constants.UNDERSCORE).decrement(name);
  }

  @Override
  public Expr onLambda(String param, Expr input, Expr result) {
    Expr newInput = input.acceptExternal(this);

    if (param.equals("_")) {
      return Expr.makeLambda(param, newInput, result.acceptExternal(this));
    } else {

      return Expr.makeLambda("_", newInput, dename(param, result).acceptExternal(this));
    }
  }

  @Override
  public Expr onPi(String param, Expr input, Expr result) {
    Expr newInput = input.acceptExternal(this);

    if (param.equals("_")) {
      return Expr.makePi(param, newInput, result.acceptExternal(this));
    } else {

      return Expr.makePi("_", newInput, dename(param, result).acceptExternal(this));
    }
  }

  @Override
  public Expr onLet(String name, Expr type, Expr value, Expr body) {
    Expr newType = (type == null) ? null : type.acceptExternal(this);
    if (name.equals("_")) {

      return Expr.makeLet(name, newType, value.acceptExternal(this), body.acceptExternal(this));
    } else {
      return Expr.makeLet(
          "_", newType, value.acceptExternal(this), dename(name, body).acceptExternal(this));
    }
  }
}
