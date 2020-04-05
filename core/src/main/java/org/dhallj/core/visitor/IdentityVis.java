package org.dhallj.core.visitor;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.LetBinding;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;

/**
 * Represents an identity function.
 *
 * <p>This is a convenience class designed to help with implementations that only need to change a
 * small number of cases.
 */
public abstract class IdentityVis extends PureVis<Expr> {

  public Expr onNote(Expr base, Source source) {
    return Expr.makeNote(base, source);
  }

  public Expr onNatural(BigInteger value) {
    return Expr.makeNaturalLiteral(value);
  }

  public Expr onInteger(BigInteger value) {
    return Expr.makeIntegerLiteral(value);
  }

  public Expr onDouble(double value) {
    return Expr.makeDoubleLiteral(value);
  }

  public Expr onBuiltIn(String name) {
    return Expr.makeBuiltIn(name);
  }

  public Expr onIdentifier(String name, long index) {
    return Expr.makeIdentifier(name, index);
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
