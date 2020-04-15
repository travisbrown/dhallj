package org.dhallj.core;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Map.Entry;

/**
 * Represents a function from a Dhall expression to a value.
 *
 * @param A The result type
 */
public interface ExternalVisitor<A> {
  A onNote(Expr base, Source source);

  A onNatural(BigInteger value);

  A onInteger(BigInteger value);

  A onDouble(double value);

  A onBuiltIn(String name);

  A onIdentifier(String name, long index);

  A onLambda(String name, Expr type, Expr result);

  A onPi(String name, Expr type, Expr result);

  A onLet(String name, Expr type, Expr value, Expr body);

  A onText(String[] parts, Iterable<Expr> interpolated);

  A onNonEmptyList(Iterable<Expr> values, int size);

  A onEmptyList(Expr type);

  A onRecord(Iterable<Entry<String, Expr>> fields, int size);

  A onRecordType(Iterable<Entry<String, Expr>> fields, int size);

  A onUnionType(Iterable<Entry<String, Expr>> fields, int size);

  A onFieldAccess(Expr base, String fieldName);

  A onProjection(Expr base, String[] fieldNames);

  A onProjectionByType(Expr base, Expr type);

  A onApplication(Expr base, Expr arg);

  A onOperatorApplication(Operator operator, Expr lhs, Expr rhs);

  A onIf(Expr predicate, Expr thenValue, Expr elseValue);

  A onAnnotated(Expr base, Expr type);

  A onAssert(Expr base);

  A onMerge(Expr handlers, Expr union, Expr type);

  A onToMap(Expr base, Expr type);

  A onMissingImport(Expr.ImportMode mode, byte[] hash);

  A onEnvImport(String value, Expr.ImportMode mode, byte[] hash);

  A onLocalImport(Path path, Expr.ImportMode mode, byte[] hash);

  A onClasspathImport(Path path, Expr.ImportMode mode, byte[] hash);

  A onRemoteImport(URI url, Expr using, Expr.ImportMode mode, byte[] hash);

  /**
   * Represents a function from a Dhall expression that always returns the same value.
   *
   * <p>This is a convenience class designed to help with implementations that have a default value
   * for most cases.
   *
   * <p>Note that by default the implementation sees through note layers.
   *
   * @param A The result type
   */
  public static class Constant<A> implements ExternalVisitor<A> {
    private final A returnValue;

    protected A getReturnValue() {
      return this.returnValue;
    }

    public Constant(A value) {
      this.returnValue = value;
    }

    @Override
    public A onNote(Expr base, Source source) {
      return base.accept(this);
    }

    @Override
    public A onNatural(BigInteger value) {
      return this.getReturnValue();
    }

    @Override
    public A onInteger(BigInteger value) {
      return this.getReturnValue();
    }

    @Override
    public A onDouble(double value) {
      return this.getReturnValue();
    }

    @Override
    public A onBuiltIn(String name) {
      return this.getReturnValue();
    }

    @Override
    public A onIdentifier(String name, long index) {
      return this.getReturnValue();
    }

    @Override
    public A onLambda(String name, Expr input, Expr result) {
      return this.getReturnValue();
    }

    @Override
    public A onPi(String name, Expr input, Expr result) {
      return this.getReturnValue();
    }

    @Override
    public A onLet(String name, Expr type, Expr value, Expr body) {
      return this.getReturnValue();
    }

    @Override
    public A onText(String[] parts, Iterable<Expr> interpolated) {
      return this.getReturnValue();
    }

    @Override
    public A onNonEmptyList(Iterable<Expr> values, int size) {
      return this.getReturnValue();
    }

    @Override
    public A onEmptyList(Expr tpe) {
      return this.getReturnValue();
    }

    @Override
    public A onRecord(Iterable<Entry<String, Expr>> fields, int size) {
      return this.getReturnValue();
    }

    @Override
    public A onRecordType(Iterable<Entry<String, Expr>> fields, int size) {
      return this.getReturnValue();
    }

    @Override
    public A onUnionType(Iterable<Entry<String, Expr>> fields, int size) {
      return this.getReturnValue();
    }

    @Override
    public A onFieldAccess(Expr base, String fieldName) {
      return this.getReturnValue();
    }

    @Override
    public A onProjection(Expr base, String[] fieldNames) {
      return this.getReturnValue();
    }

    @Override
    public A onProjectionByType(Expr base, Expr tpe) {
      return this.getReturnValue();
    }

    @Override
    public A onApplication(Expr base, Expr arg) {
      return this.getReturnValue();
    }

    @Override
    public A onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
      return this.getReturnValue();
    }

    @Override
    public A onIf(Expr predicate, Expr thenValue, Expr elseValue) {
      return this.getReturnValue();
    }

    @Override
    public A onAnnotated(Expr base, Expr tpe) {
      return this.getReturnValue();
    }

    @Override
    public A onAssert(Expr base) {
      return this.getReturnValue();
    }

    @Override
    public A onMerge(Expr handlers, Expr union, Expr tpe) {
      return this.getReturnValue();
    }

    @Override
    public A onToMap(Expr base, Expr type) {
      return this.getReturnValue();
    }

    @Override
    public A onMissingImport(Expr.ImportMode mode, byte[] hash) {
      return this.getReturnValue();
    }

    @Override
    public A onEnvImport(String value, Expr.ImportMode mode, byte[] hash) {
      return this.getReturnValue();
    }

    @Override
    public A onLocalImport(Path path, Expr.ImportMode mode, byte[] hash) {
      return this.getReturnValue();
    }

    @Override
    public A onClasspathImport(Path path, Expr.ImportMode mode, byte[] hash) {
      return this.getReturnValue();
    }

    @Override
    public A onRemoteImport(URI url, Expr using, Expr.ImportMode mode, byte[] hash) {
      return this.getReturnValue();
    }
  }
}
