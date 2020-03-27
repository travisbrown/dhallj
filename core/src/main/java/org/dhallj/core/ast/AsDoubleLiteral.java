package org.dhallj.core.ast;

import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsDoubleLiteral extends ConstantVisitor.External<Double> {
  public static final Visitor<Expr, Double> instance = new AsDoubleLiteral();

  AsDoubleLiteral() {
    super(null);
  }

  @Override
  public Double onDoubleLiteral(double value) {
    return value;
  }
}
