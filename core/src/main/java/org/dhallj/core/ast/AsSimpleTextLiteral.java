package org.dhallj.core.ast;

import java.util.Iterator;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsSimpleTextLiteral extends ConstantVisitor.External<String> {
  public static final Visitor<Expr, String> instance = new AsSimpleTextLiteral();

  AsSimpleTextLiteral() {
    super(null);
  }

  @Override
  public String onTextLiteral(String[] parts, Iterable<Expr> interpolated) {
    Iterator<Expr> it = interpolated.iterator();

    if (parts.length == 1 && !it.hasNext()) {
      return parts[0];
    } else {
      return null;
    }
  }
}
