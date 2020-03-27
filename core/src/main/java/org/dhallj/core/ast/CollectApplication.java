package org.dhallj.core.ast;

import java.util.ArrayList;
import java.util.List;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class CollectApplication extends ConstantVisitor.External<List<Expr>> {
  private Expr base;
  private final List<Expr> args;

  protected List<Expr> getReturnValue() {
    return this.args;
  }

  public CollectApplication(Expr base, Expr arg) {
    super(null);
    this.base = base;
    this.args = new ArrayList();
    this.args.add(base);
    this.args.add(arg);
  }

  @Override
  public List<Expr> onApplication(Expr base, Expr arg) {
    this.base = base;
    this.args.set(0, arg);
    this.args.add(0, base);
    this.base.acceptExternal(this);
    return this.args;
  }
}
