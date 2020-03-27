package org.dhallj.core.ast;

import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsBoolLiteral extends ConstantVisitor.External<Boolean> {
  public static final Visitor<Expr, Boolean> instance = new AsBoolLiteral();

  AsBoolLiteral() {
    super(null);
  }

  @Override
  public Boolean onIdentifier(String value, long index) {
    if (index == 0) {
      if (value.equals("True")) {
        return true;
      } else if (value.equals("False")) {
        return false;
      }
    }
    return null;
  }
}
