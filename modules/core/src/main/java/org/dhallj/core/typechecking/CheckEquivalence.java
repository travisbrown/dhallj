package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.ExternalVisitor;

final class CheckEquivalence extends ExternalVisitor.Constant<Boolean> {
  public static final ExternalVisitor<Boolean> instance = new CheckEquivalence();

  CheckEquivalence() {
    super(null);
  }

  @Override
  public Boolean onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    return operator.equals(Operator.EQUIVALENT) && lhs.equivalent(rhs);
  }
}
