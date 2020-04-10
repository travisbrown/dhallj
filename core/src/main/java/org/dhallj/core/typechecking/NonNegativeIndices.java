package org.dhallj.core.typechecking;

import java.net.URI;
import java.nio.file.Path;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;

final class NonNegativeIndices extends Visitor.Property {
  public static final Visitor<Boolean> instance = new NonNegativeIndices();

  @Override
  public Boolean onIdentifier(Expr self, String value, long index) {
    return index >= 0;
  }
}
