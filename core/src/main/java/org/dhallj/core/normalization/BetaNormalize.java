package org.dhallj.core.normalization;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.LetBinding;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Vis;
import org.dhallj.core.util.FieldUtilities;
import org.dhallj.core.visitor.PureVis;

/**
 * Performs beta normalization.
 *
 * <p>This is a stateless visitor intended for use as a singleton.
 */
public final class BetaNormalize extends PureVis<Expr> {
  public static Vis<Expr> instance = new BetaNormalize();

  public Expr onNote(Expr base, Source source) {
    return base;
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
    Expr result = body;

    for (int i = bindings.size() - 1; i >= 0; i--) {
      LetBinding<Expr> binding = bindings.get(i);
      String name = binding.getName();

      result = result.substitute(name, binding.getValue().increment(name)).decrement(name);
    }

    return result.acceptVis(this);
  }

  public Expr onText(String[] parts, List<Expr> interpolated) {
    return BetaNormalizeTextLiteral.apply(parts, interpolated);
  }

  public Expr onNonEmptyList(List<Expr> values) {
    return Expr.makeNonEmptyListLiteral(values);
  }

  public Expr onEmptyList(Expr typeExpr, Expr type) {
    return Expr.makeEmptyListLiteral(type);
  }

  public Expr onRecord(List<Entry<String, Expr>> fields) {
    Collections.sort(fields, FieldUtilities.entryComparator);
    return Expr.makeRecordLiteral(fields);
  }

  public Expr onRecordType(List<Entry<String, Expr>> fields) {
    Collections.sort(fields, FieldUtilities.entryComparator);
    return Expr.makeRecordType(fields);
  }

  public Expr onUnionType(List<Entry<String, Expr>> fields) {
    Collections.sort(fields, FieldUtilities.entryComparator);
    return Expr.makeUnionType(fields);
  }

  public Expr onFieldAccess(Expr base, String fieldName) {
    return BetaNormalizeFieldAccess.apply(base, fieldName);
  }

  public Expr onProjection(Expr base, String[] fieldNames) {
    return BetaNormalizeProjection.apply(base, fieldNames);
  }

  public Expr onProjectionByType(Expr base, Expr arg) {
    Iterable<Entry<String, Expr>> argAsRecordType = arg.asRecordType();
    Set<String> keys = new TreeSet();
    for (Entry<String, Expr> entry : argAsRecordType) {
      keys.add(entry.getKey());
    }

    return Expr.makeProjection(base, keys.toArray(new String[keys.size()])).acceptVis(this);
  }

  public Expr onApplication(Expr baseExpr, Expr base, List<Expr> args) {
    return BetaNormalizeApplication.apply(base, args);
  }

  public Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    return BetaNormalizeOperatorApplication.apply(operator, lhs, rhs);
  }

  public Expr onIf(Expr predicate, Expr thenValue, Expr elseValue) {
    return BetaNormalizeIf.apply(predicate, thenValue, elseValue);
  }

  public Expr onAnnotated(Expr base, Expr type) {
    return base;
  }

  public Expr onAssert(Expr type) {
    return Expr.makeAssert(type);
  }

  public Expr onMerge(Expr handlers, Expr union, Expr type) {
    return BetaNormalizeMerge.apply(handlers, union, type);
  }

  public Expr onToMap(Expr base, Expr type) {
    return BetaNormalizeToMap.apply(base, type);
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
