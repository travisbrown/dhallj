package org.dhallj.core.visitor;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Vis;

/** @param A The result type */
public abstract class PureVis<A> implements Vis<A> {
  public void bind(String param, Expr type) {}
}
