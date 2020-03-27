package org.dhallj.core.ast;

import java.math.BigInteger;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsIntegerLiteral extends ConstantVisitor.External<BigInteger> {
  public static final Visitor<Expr, BigInteger> instance = new AsIntegerLiteral();

  AsIntegerLiteral() {
    super(null);
  }

  @Override
  public BigInteger onIntegerLiteral(BigInteger value) {
    return value;
  }
}
