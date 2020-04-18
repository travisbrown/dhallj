package org.dhallj.core.typechecking;

import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;

final class NonNegativeIndices extends Visitor.Property {
  public static final Visitor<Boolean> instance = new NonNegativeIndices();

  @Override
  public Boolean onIdentifier(Expr self, String name, long index) {
    return index >= 0;
  }
}
