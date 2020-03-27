package org.dhallj.core.ast;

import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class IsIdentifier extends ConstantVisitor.External<Boolean> {
  private final String targetValue;
  private final long targetIndex;

  public IsIdentifier(String targetValue, long targetIndex) {
    super(false);
    this.targetValue = targetValue;
    this.targetIndex = targetIndex;
  }

  public IsIdentifier(String targetValue) {
    this(targetValue, 0);
  }

  @Override
  public Boolean onIdentifier(String value, long index) {
    return value.equals(this.targetValue) && index == this.targetIndex;
  }
}
