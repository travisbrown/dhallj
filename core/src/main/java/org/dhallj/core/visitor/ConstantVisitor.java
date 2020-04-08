package org.dhallj.core.visitor;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Visitor;

/**
 * Represents a function from a Dhall expression to a value that always returns the same value.
 *
 * <p>This is a convenience class designed to help with implementations that have a default value
 * for most cases.
 *
 * <p>Note that both the internal and external implementations recurse through note layers.
 *
 * @param I The internal result type used while recursing
 * @param A The final result type
 */
public class ConstantVisitor<I, A> implements Visitor<I, A> {
  private final A returnValue;

  protected A getReturnValue() {
    return this.returnValue;
  }

  ConstantVisitor(A value) {
    this.returnValue = value;
  }

  public static class External<A> extends ConstantVisitor<Expr, A> implements ExternalVisitor<A> {

    public External(A value) {
      super(value);
    }

    @Override
    public A onNote(Expr base, Source source) {
      return base.acceptExternal(this);
    }
  }

  public A onDoubleLiteral(double value) {
    return this.getReturnValue();
  }

  public A onNaturalLiteral(BigInteger value) {
    return this.getReturnValue();
  }

  public A onIntegerLiteral(BigInteger value) {
    return this.getReturnValue();
  }

  public A onTextLiteral(String[] parts, Iterable<I> interpolated) {
    return this.getReturnValue();
  }

  public A onApplication(I base, I arg) {
    return this.getReturnValue();
  }

  public A onOperatorApplication(Operator operator, I lhs, I rhs) {
    return this.getReturnValue();
  }

  public A onIf(I cond, I thenValue, I elseValue) {
    return this.getReturnValue();
  }

  public A onLambda(String param, I input, I result) {
    return this.getReturnValue();
  }

  public A onPi(String param, I input, I result) {
    return this.getReturnValue();
  }

  public A onAssert(I base) {
    return this.getReturnValue();
  }

  public A onFieldAccess(I base, String fieldName) {
    return this.getReturnValue();
  }

  public A onProjection(I base, String[] fieldNames) {
    return this.getReturnValue();
  }

  public A onProjectionByType(I base, I tpe) {
    return this.getReturnValue();
  }

  public A onBuiltIn(String name) {
    return this.getReturnValue();
  }

  public A onIdentifier(String value, long index) {
    return this.getReturnValue();
  }

  public A onRecordLiteral(Iterable<Entry<String, I>> fields, int size) {
    return this.getReturnValue();
  }

  public A onRecordType(Iterable<Entry<String, I>> fields, int size) {
    return this.getReturnValue();
  }

  public A onUnionType(Iterable<Entry<String, I>> fields, int size) {
    return this.getReturnValue();
  }

  public A onNonEmptyListLiteral(Iterable<I> values, int size) {
    return this.getReturnValue();
  }

  public A onEmptyListLiteral(I tpe) {
    return this.getReturnValue();
  }

  public A onLet(String name, I type, I value, I body) {
    return this.getReturnValue();
  }

  public A onAnnotated(I base, I tpe) {
    return this.getReturnValue();
  }

  public A onToMap(I base, I tpe) {
    return this.getReturnValue();
  }

  public A onMerge(I left, I right, I tpe) {

    return this.getReturnValue();
  }

  public A onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    return this.getReturnValue();
  }

  public A onRemoteImport(URI url, I using, Import.Mode mode, byte[] hash) {
    return this.getReturnValue();
  }

  public A onEnvImport(String value, Import.Mode mode, byte[] hash) {
    return this.getReturnValue();
  }

  public A onMissingImport(Import.Mode mode, byte[] hash) {
    return this.getReturnValue();
  }

  public A onNote(I base, Source source) {
    return this.getReturnValue();
  }
}
