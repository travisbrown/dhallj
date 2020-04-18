package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;
import org.dhallj.core.ExternalVisitor;

final class TypeCheckApplication extends ExternalVisitor.Constant<Expr> {
  private final Expr arg;
  private final Expr argType;
  private final TypeCheck typeCheck;

  TypeCheckApplication(Expr arg, Expr argType, TypeCheck typeCheck) {
    super(null);
    this.arg = arg;
    this.argType = argType;
    this.typeCheck = typeCheck;
  }

  @Override
  public Expr onBuiltIn(String name) {
    if (name.equals("Some")) {
      if (TypeCheck.isType(this.argType.accept(this.typeCheck))) {
        return Expr.makeApplication(Expr.Constants.OPTIONAL, this.argType);
      } else {
        throw TypeCheckFailure.makeSomeApplicationError(this.arg, this.argType);
      }
    }
    throw TypeCheckFailure.makeBuiltInApplicationError(name, this.arg, this.argType);
  }

  @Override
  public Expr onPi(String name, Expr input, Expr result) {
    if (input.equivalent(this.argType)) {
      return result.substitute(name, arg).normalize();
    } else {
      throw TypeCheckFailure.makeApplicationTypeError(input, this.argType);
    }
  }
}
