package org.dhallj.core.ast;

import java.util.ArrayList;
import java.util.List;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsListLiteral extends ConstantVisitor.External<List<Expr>> {
  public static final Visitor<Expr, List<Expr>> instance = new AsListLiteral();

  AsListLiteral() {
    super(null);
  }

  @Override
  public List<Expr> onNonEmptyListLiteral(Iterable<Expr> values, int size) {
    List<Expr> result = new ArrayList(size);

    for (Expr value : values) {
      result.add(value);
    }
    return result;
  }

  @Override
  public List<Expr> onEmptyListLiteral(Expr tpe) {
    return new ArrayList(0);
  }
}
