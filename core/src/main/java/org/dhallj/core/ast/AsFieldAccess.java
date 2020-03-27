package org.dhallj.core.ast;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsFieldAccess extends ConstantVisitor.External<Entry<Expr, String>> {
  public static final Visitor<Expr, Entry<Expr, String>> instance = new AsFieldAccess();

  AsFieldAccess() {
    super(null);
  }

  @Override
  public Entry<Expr, String> onFieldAccess(Expr base, String fieldName) {
    return new SimpleImmutableEntry(base, fieldName);
  }
}
