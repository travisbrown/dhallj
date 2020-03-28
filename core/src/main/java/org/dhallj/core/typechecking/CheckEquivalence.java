package org.dhallj.core.typechecking;

import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

final class CheckEquivalence extends ConstantVisitor.External<Boolean> {
  public static final Visitor<Expr, Boolean> instance = new CheckEquivalence();

  CheckEquivalence() {
    super(null);
  }

  @Override
  public Boolean onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    return operator.equals(Operator.EQUIVALENT) && lhs.equivalent(rhs);
  }
}
