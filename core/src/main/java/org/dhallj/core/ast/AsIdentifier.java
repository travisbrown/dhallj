package org.dhallj.core.ast;

import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsIdentifier extends ConstantVisitor.External<String> {
  public static final Visitor<Expr, String> instance = new AsIdentifier();

  AsIdentifier() {
    super(null);
  }

  @Override
  public String onIdentifier(String value, long index) {
    return value;
  }
}
