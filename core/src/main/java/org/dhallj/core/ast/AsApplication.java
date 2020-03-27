package org.dhallj.core.ast;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class AsApplication extends ConstantVisitor.External<Entry<Expr, Expr>> {
  public static final Visitor<Expr, Entry<Expr, Expr>> instance = new AsApplication();

  AsApplication() {
    super(null);
  }

  @Override
  public Entry<Expr, Expr> onApplication(Expr base, Expr arg) {
    return new SimpleImmutableEntry(base, arg);
  }
}
