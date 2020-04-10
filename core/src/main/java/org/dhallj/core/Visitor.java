package org.dhallj.core;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;

/**
 * Represents a function from a Dhall expression to a value that recurses through the structure of
 * the expression.
 *
 * @param A The final result type
 */
public interface Visitor<A> {
  void bind(String name, Expr type);

  A onNote(A base, Source source);

  A onNatural(Expr self, BigInteger value);

  A onInteger(Expr self, BigInteger value);

  A onDouble(Expr self, double value);

  A onBuiltIn(Expr self, String value);

  A onIdentifier(Expr self, String value, long index);

  A onLambda(String name, A type, A result);

  A onPi(String name, A type, A result);

  A onLet(List<LetBinding<A>> bindings, A body);

  A onText(String[] parts, List<A> interpolated);

  A onNonEmptyList(List<A> values);

  A onEmptyList(Expr typeExpr, A type);

  A onRecord(List<Entry<String, A>> fields);

  A onRecordType(List<Entry<String, A>> fields);

  A onUnionType(List<Entry<String, A>> fields);

  A onFieldAccess(A base, String fieldName);

  A onProjection(A base, String[] fieldNames);

  A onProjectionByType(A base, A type);

  A onApplication(Expr baseExpr, A base, List<A> args);

  A onOperatorApplication(Operator operator, A lhs, A rhs);

  A onIf(A predicate, A thenValue, A elseValue);

  A onAnnotated(A base, A type);

  A onAssert(A base);

  A onMerge(A handlers, A union, A type);

  A onToMap(A base, A type);

  A onMissingImport(Import.Mode mode, byte[] hash);

  A onEnvImport(String value, Import.Mode mode, byte[] hash);

  A onLocalImport(Path path, Import.Mode mode, byte[] hash);

  A onRemoteImport(URI url, A using, Import.Mode mode, byte[] hash);

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
  public static class Constant<A> implements Visitor<A> {
    private final A returnValue;

    protected A getReturnValue() {
      return this.returnValue;
    }

    public Constant(A value) {
      this.returnValue = value;
    }

    public void bind(String name, Expr type) {}

    public A onNote(A base, Source source) {
      return base;
    }

    public A onNatural(Expr self, BigInteger value) {
      return this.getReturnValue();
    }

    public A onInteger(Expr self, BigInteger value) {
      return this.getReturnValue();
    }

    public A onDouble(Expr self, double value) {
      return this.getReturnValue();
    }

    public A onBuiltIn(Expr self, String value) {
      return this.getReturnValue();
    }

    public A onIdentifier(Expr self, String value, long index) {
      return this.getReturnValue();
    }

    public A onLambda(String name, A type, A result) {
      return this.getReturnValue();
    }

    public A onPi(String name, A type, A result) {
      return this.getReturnValue();
    }

    public A onLet(List<LetBinding<A>> bindings, A body) {
      return this.getReturnValue();
    }

    public A onText(String[] parts, List<A> interpolated) {
      return this.getReturnValue();
    }

    public A onNonEmptyList(List<A> values) {
      return this.getReturnValue();
    }

    public A onEmptyList(Expr typeExpr, A type) {
      return this.getReturnValue();
    }

    public A onRecord(List<Entry<String, A>> fields) {
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

    public A onApplication(Expr baseExpr, A base, List<A> args) {
      return this.getReturnValue();
    }

    public A onOperatorApplication(Operator operator, A lhs, A rhs) {
      return this.getReturnValue();
    }

    public A onIf(A predicate, A thenValue, A elseValue) {
      return this.getReturnValue();
    }

    public A onAnnotated(A base, A type) {
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

  /**
   * Represents a property of Dhall expressions.
   *
   * <p>This is a convenience class designed to help with implementations that have a default value
   * for most cases.
   */
  public class Property extends Constant<Boolean> {
    public Property() {
      super(true);
    }

    public Boolean onLambda(String name, Boolean type, Boolean result) {
      return type && result;
    }

    public Boolean onPi(String name, Boolean type, Boolean result) {
      return type && result;
    }

    public Boolean onLet(List<LetBinding<Boolean>> bindings, Boolean body) {
      for (LetBinding<Boolean> binding : bindings) {
        if (!binding.getValue() || (binding.hasType() && !binding.getType())) {
          return false;
        }
      }
      return true;
    }

    public Boolean onText(String[] parts, List<Boolean> interpolated) {
      for (Boolean value : interpolated) {
        if (!value) {
          return false;
        }
      }
      return true;
    }

    public Boolean onNonEmptyList(List<Boolean> values) {
      for (Boolean value : values) {
        if (!value) {
          return false;
        }
      }
      return true;
    }

    public Boolean onEmptyList(Expr typeExpr, Boolean type) {
      return type;
    }

    public Boolean onRecord(List<Entry<String, Boolean>> fields) {
      for (Entry<String, Boolean> entry : fields) {
        if (!entry.getValue()) {
          return false;
        }
      }
      return true;
    }

    public Boolean onRecordType(List<Entry<String, Boolean>> fields) {
      for (Entry<String, Boolean> entry : fields) {
        if (!entry.getValue()) {
          return false;
        }
      }
      return true;
    }

    public Boolean onUnionType(List<Entry<String, Boolean>> fields) {
      for (Entry<String, Boolean> entry : fields) {
        if (entry.getValue() != null && !entry.getValue()) {
          return false;
        }
      }
      return true;
    }

    public Boolean onFieldAccess(Boolean base, String fieldName) {
      return base;
    }

    public Boolean onProjection(Boolean base, String[] fieldNames) {
      return base;
    }

    public Boolean onProjectionByType(Boolean base, Boolean type) {
      return base && type;
    }

    public Boolean onApplication(Expr baseExpr, Boolean base, List<Boolean> args) {
      if (!base) {
        return false;
      }
      for (Boolean value : args) {
        if (!value) {
          return false;
        }
      }
      return true;
    }

    public Boolean onOperatorApplication(Operator operator, Boolean lhs, Boolean rhs) {
      return lhs && rhs;
    }

    public Boolean onIf(Boolean predicate, Boolean thenValue, Boolean elseValue) {
      return predicate && thenValue && elseValue;
    }

    public Boolean onAnnotated(Boolean base, Boolean type) {
      return base && type;
    }

    public Boolean onAssert(Boolean base) {
      return base;
    }

    public Boolean onMerge(Boolean handlers, Boolean union, Boolean type) {
      return handlers && union && (type == null || type);
    }

    public Boolean onToMap(Boolean base, Boolean type) {
      return base && (type == null || type);
    }

    public Boolean onLocalImport(Path path, Import.Mode mode, byte[] hash) {
      return true;
    }

    public Boolean onRemoteImport(URI url, Boolean using, Import.Mode mode, byte[] hash) {
      return true;
    }

    public Boolean onEnvImport(String value, Import.Mode mode, byte[] hash) {
      return true;
    }

    public Boolean onMissingImport(Import.Mode mode, byte[] hash) {
      return true;
    }
  }

  /**
   * Represents an identity function.
   *
   * <p>This is a convenience class designed to help with implementations that only need to change a
   * small number of cases.
   */
  public abstract class Identity implements Visitor<Expr> {
    public Expr onNote(Expr base, Source source) {
      return Expr.makeNote(base, source);
    }

    public Expr onNatural(Expr self, BigInteger value) {
      return self;
    }

    public Expr onInteger(Expr self, BigInteger value) {
      return self;
    }

    public Expr onDouble(Expr self, double value) {
      return self;
    }

    public Expr onBuiltIn(Expr self, String name) {
      return self;
    }

    public Expr onIdentifier(Expr self, String name, long index) {
      return self;
    }

    public Expr onLambda(String name, Expr type, Expr result) {
      return Expr.makeLambda(name, type, result);
    }

    public Expr onPi(String name, Expr type, Expr result) {
      return Expr.makePi(name, type, result);
    }

    public Expr onLet(List<LetBinding<Expr>> bindings, Expr body) {
      return Expr.makeLet(bindings, body);
    }

    public Expr onText(String[] parts, List<Expr> interpolated) {
      return Expr.makeTextLiteral(parts, interpolated);
    }

    public Expr onNonEmptyList(List<Expr> values) {
      return Expr.makeNonEmptyListLiteral(values);
    }

    public Expr onEmptyList(Expr typeExpr, Expr type) {
      return Expr.makeEmptyListLiteral(type);
    }

    public Expr onRecord(List<Entry<String, Expr>> fields) {
      return Expr.makeRecordLiteral(fields);
    }

    public Expr onRecordType(List<Entry<String, Expr>> fields) {
      return Expr.makeRecordType(fields);
    }

    public Expr onUnionType(List<Entry<String, Expr>> fields) {
      return Expr.makeUnionType(fields);
    }

    public Expr onFieldAccess(Expr base, String fieldName) {
      return Expr.makeFieldAccess(base, fieldName);
    }

    public Expr onProjection(Expr base, String[] fieldNames) {
      return Expr.makeProjection(base, fieldNames);
    }

    public Expr onProjectionByType(Expr base, Expr type) {
      return Expr.makeProjectionByType(base, type);
    }

    public Expr onApplication(Expr baseExpr, Expr base, List<Expr> args) {
      return Expr.makeApplication(base, args);
    }

    public Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
      return Expr.makeOperatorApplication(operator, lhs, rhs);
    }

    public Expr onIf(Expr predicate, Expr thenValue, Expr elseValue) {
      return Expr.makeIf(predicate, thenValue, elseValue);
    }

    public Expr onAnnotated(Expr base, Expr type) {
      return Expr.makeAnnotated(base, type);
    }

    public Expr onAssert(Expr base) {
      return Expr.makeAssert(base);
    }

    public Expr onMerge(Expr handlers, Expr union, Expr type) {
      return Expr.makeMerge(handlers, union, type);
    }

    public Expr onToMap(Expr base, Expr type) {
      return Expr.makeToMap(base, type);
    }

    public Expr onMissingImport(Import.Mode mode, byte[] hash) {
      return Expr.makeMissingImport(mode, hash);
    }

    public Expr onEnvImport(String value, Import.Mode mode, byte[] hash) {
      return Expr.makeEnvImport(value, mode, hash);
    }

    public Expr onLocalImport(Path path, Import.Mode mode, byte[] hash) {
      return Expr.makeLocalImport(path, mode, hash);
    }

    public Expr onRemoteImport(URI url, Expr using, Import.Mode mode, byte[] hash) {
      return Expr.makeRemoteImport(url, using, mode, hash);
    }
  }
}
