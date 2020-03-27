package org.dhallj.core.ast;

import java.math.BigInteger;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsNaturalLiteral extends ConstantVisitor.External<BigInteger> {
  public static final Visitor<Expr, BigInteger> instance = new AsNaturalLiteral();

  AsNaturalLiteral() {
    super(null);
  }

  @Override
  public BigInteger onNaturalLiteral(BigInteger value) {
    return value;
  }
}
