package org.dhallj.core.ast;

import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsUnionType extends ConstantVisitor.External<Iterable<Entry<String, Expr>>> {
  public static final Visitor<Expr, Iterable<Entry<String, Expr>>> instance = new AsUnionType();

  AsUnionType() {
    super(null);
  }

  @Override
  public Iterable<Entry<String, Expr>> onUnionType(Iterable<Entry<String, Expr>> fields, int size) {
    return fields;
  }
}
