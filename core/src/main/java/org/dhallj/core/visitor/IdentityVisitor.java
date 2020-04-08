package org.dhallj.core.visitor;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Iterator;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Visitor;

/**
 * Represents an identity function.
 *
 * <p>This is a convenience class designed to help with implementations that only need to change a
 * small number of cases.
 */
public abstract class IdentityVisitor<I> implements Visitor<I, Expr> {
  public Expr onDoubleLiteral(double value) {
    return Expr.makeDoubleLiteral(value);
  }

  public Expr onNaturalLiteral(BigInteger value) {
    return Expr.makeNaturalLiteral(value);
  }

  public Expr onIntegerLiteral(BigInteger value) {
    return Expr.makeIntegerLiteral(value);
  }

  public Expr onBuiltIn(String name) {
    return Expr.makeBuiltIn(name);
  }

  public Expr onIdentifier(String value, long index) {
    return Expr.makeIdentifier(value, index);
  }

  public Expr onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    return Expr.makeLocalImport(path, mode, hash);
  }

  public Expr onEnvImport(String value, Import.Mode mode, byte[] hash) {
    return Expr.makeEnvImport(value, mode, hash);
  }

  public Expr onMissingImport(Import.Mode mode, byte[] hash) {
    return Expr.makeMissingImport(mode, hash);
  }

  public static class External extends IdentityVisitor<Expr> implements ExternalVisitor<Expr> {
    public Expr onTextLiteral(String[] parts, Iterable<Expr> interpolated) {
      return Expr.makeTextLiteral(parts, interpolated);
    }

    public Expr onApplication(Expr base, Expr arg) {
      return Expr.makeApplication(base, arg);
    }

    public Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
      return Expr.makeOperatorApplication(operator, lhs, rhs);
    }

    public Expr onIf(Expr cond, Expr thenValue, Expr elseValue) {
      return Expr.makeIf(cond, thenValue, elseValue);
    }

    public Expr onLambda(String param, Expr input, Expr result) {
      return Expr.makeLambda(param, input, result);
    }

    public Expr onPi(String param, Expr input, Expr result) {
      return Expr.makePi(param, input, result);
    }

    public Expr onAssert(Expr base) {
      return Expr.makeAssert(base);
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

    public Expr onRecordLiteral(Iterable<Entry<String, Expr>> fields, int size) {
      return Expr.makeRecordLiteral(fields);
    }

    public Expr onRecordType(Iterable<Entry<String, Expr>> fields, int size) {
      return Expr.makeRecordType(fields);
    }

    public Expr onUnionType(Iterable<Entry<String, Expr>> fields, int size) {
      return Expr.makeUnionType(fields);
    }

    public Expr onNonEmptyListLiteral(Iterable<Expr> values, int size) {
      return Expr.makeNonEmptyListLiteral(values);
    }

    public Expr onEmptyListLiteral(Expr type) {
      return Expr.makeEmptyListLiteral(type);
    }

    public Expr onLet(String name, Expr type, Expr value, Expr body) {
      return Expr.makeLet(name, type, value, body);
    }

    public Expr onToMap(Expr base, Expr type) {
      return Expr.makeToMap(base, type);
    }

    public Expr onMerge(Expr left, Expr right, Expr type) {
      return Expr.makeMerge(left, right, type);
    }

    public Expr onAnnotated(Expr base, Expr type) {
      return Expr.makeAnnotated(base, type);
    }

    public Expr onNote(Expr base, Source source) {
      return Expr.makeNote(base, source);
    }

    public Expr onRemoteImport(URI url, Expr using, Import.Mode mode, byte[] hash) {
      return Expr.makeRemoteImport(url, using, mode, hash);
    }
  }
}
