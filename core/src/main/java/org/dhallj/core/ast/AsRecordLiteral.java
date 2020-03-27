package org.dhallj.core.ast;

import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsRecordLiteral extends ConstantVisitor.External<Iterable<Entry<String, Expr>>> {
  public static final Visitor<Expr, Iterable<Entry<String, Expr>>> instance = new AsRecordLiteral();

  AsRecordLiteral() {
    super(null);
  }

  @Override
  public Iterable<Entry<String, Expr>> onRecordLiteral(
      Iterable<Entry<String, Expr>> fields, int size) {
    return fields;
  }
}
