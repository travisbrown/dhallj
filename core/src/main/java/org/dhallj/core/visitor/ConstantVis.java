package org.dhallj.core.visitor;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Thunk;
import org.dhallj.core.Source;

/**
 * Represents a function from a Dhall expression to a value that always returns the same value.
 *
 * <p>This is a convenience class designed to help with implementations that have a default value
 * for most cases.
 *
 * <p>Note that both the internal and external implementations recurse through note layers.
 *
 * @param A The result type
 */
public abstract class ConstantVis<A> extends PureVis<A> {
  private final A returnValue;

  protected A getReturnValue() {
    return this.returnValue;
  }

  public ConstantVis(A value) {
    this.returnValue = value;
  }

  public A onNote(A base, Source source) {
    return base;
  }

  public A onNatural(BigInteger value) {
    return this.getReturnValue();
  }

  public A onInteger(BigInteger value) {
    return this.getReturnValue();
  }

  public A onDouble(double value) {
    return this.getReturnValue();
  }

  public A onIdentifier(String value, long index) {
    return this.getReturnValue();
  }

  public A onLambda(String name, A type, A result) {
    return this.getReturnValue();
  }

  public A onPi(String name, A type, A result) {
    return this.getReturnValue();
  }

  public A onLet(String name, A type, A value, A body) {
    return this.getReturnValue();
  }

  public A onText(String[] parts, List<A> interpolated) {
    return this.getReturnValue();
  }

  public A onNonEmptyList(List<A> values) {
    return this.getReturnValue();
  }

  public A onEmptyList(A tpe) {
    return this.getReturnValue();
  }

  public A onRecordLiteral(List<Entry<String, A>> fields) {
    return this.getReturnValue();
  }

  public A onRecordType(List<Entry<String, A>> fields) {
    return this.getReturnValue();
  }

  public A onUnionType(List<Entry<String, A>> fields) {
    return this.getReturnValue();
  }

  public A onFieldAccess(A base, String fieldName) {
    return this.getReturnValue();
  }

  public A onProjection(A base, String[] fieldNames) {
    return this.getReturnValue();
  }

  public A onProjectionByType(A base, A type) {
    return this.getReturnValue();
  }

  public A onApplication(A base, List<A> args) {
    return this.getReturnValue();
  }

  public A onOperatorApplication(Operator operator, A lhs, A rhs) {
    return this.getReturnValue();
  }

  public A onIf(A predicate, A thenValue, A elseValue) {
    return this.getReturnValue();
  }

  public A onAnnotated(A base, A tpe) {
    return this.getReturnValue();
  }

  public A onAssert(A base) {
    return this.getReturnValue();
  }

  public A onMerge(A handlers, A union, A type) {
    return this.getReturnValue();
  }

  public A onToMap(A base, A type) {
    return this.getReturnValue();
  }

  public A onMissingImport(Import.Mode mode, byte[] hash) {
    return this.getReturnValue();
  }

  public A onEnvImport(String value, Import.Mode mode, byte[] hash) {
    return this.getReturnValue();
  }

  public A onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    return this.getReturnValue();
  }

  public A onRemoteImport(URI url, A using, Import.Mode mode, byte[] hash) {
    return this.getReturnValue();
  }
}
