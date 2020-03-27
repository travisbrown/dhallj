package org.dhallj.core.ast;

import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsSimpleIdentifier extends ConstantVisitor.External<String> {
  public static final Visitor<Expr, String> instance = new AsSimpleIdentifier();

  AsSimpleIdentifier() {
    super(null);
  }

  @Override
  public String onIdentifier(String value, long index) {
    if (index == 0) {
      return value;
    } else {
      return null;
    }
  }
}
