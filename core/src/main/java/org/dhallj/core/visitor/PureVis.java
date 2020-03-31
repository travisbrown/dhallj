package org.dhallj.core.visitor;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Thunk;
import org.dhallj.core.Source;
import org.dhallj.core.Vis;

/** @param A The result type */
public abstract class PureVis<A> implements Vis<A> {
  public void bind(String param, Expr type) {}

  public void preText(int size) {}

  public void preNonEmptyList(int size) {}

  public void preRecord(int size) {}

  public void preRecordType(int size) {}

  public void preUnionType(int size) {}

  public void preApplication(int size) {}
}
