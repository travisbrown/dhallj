package org.dhallj.core.normalization;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Thunk;
import org.dhallj.core.Visitor;
import org.dhallj.core.util.FieldUtilities;
import org.dhallj.core.util.ThunkUtilities;

/**
 * Performs beta normalization.
 *
 * <p>This is a stateless visitor intended for use as a singleton.
 */
public final class BetaNormalize implements Visitor.Internal<Expr> {
  public static Visitor<Thunk<Expr>, Expr> instance = new BetaNormalize();

  public Expr onTextLiteral(String[] parts, Iterable<Thunk<Expr>> interpolated) {
    return BetaNormalizeTextLiteral.apply(parts, interpolated);
  }

  public Expr onIf(Thunk<Expr> cond, Thunk<Expr> thenValue, Thunk<Expr> elseValue) {
    return BetaNormalizeIf.apply(cond.apply(), thenValue.apply(), elseValue.apply());
  }

  public Expr onApplication(Thunk<Expr> base, Thunk<Expr> arg) {
    return BetaNormalizeApplication.apply(base.apply(), arg.apply());
  }

  public Expr onOperatorApplication(Operator operator, Thunk<Expr> lhs, Thunk<Expr> rhs) {
    return BetaNormalizeOperatorApplication.apply(operator, lhs.apply(), rhs.apply());
  }

  public Expr onRecordLiteral(Iterable<Entry<String, Thunk<Expr>>> fields, int size) {
    return Expr.makeRecordLiteral(FieldUtilities.sortAndFlattenFields(fields, size));
  }

  public Expr onRecordType(Iterable<Entry<String, Thunk<Expr>>> fields, int size) {
    return Expr.makeRecordType(FieldUtilities.sortAndFlattenFields(fields, size));
  }

  public Expr onUnionType(Iterable<Entry<String, Thunk<Expr>>> fields, int size) {
    return Expr.makeUnionType(FieldUtilities.sortAndFlattenFields(fields, size));
  }

  public Expr onFieldAccess(Thunk<Expr> base, String fieldName) {
    return BetaNormalizeFieldAccess.apply(base.apply(), fieldName);
  }

  public Expr onProjectionByType(Thunk<Expr> base, Thunk<Expr> arg) {
    Iterable<Entry<String, Expr>> argAsRecordType = arg.apply().asRecordType();
    Set<String> keys = new TreeSet();
    for (Entry<String, Expr> entry : argAsRecordType) {
      keys.add(entry.getKey());
    }

    return Expr.makeProjection(base.apply(), keys.toArray(new String[keys.size()])).accept(this);
  }

  public Expr onProjection(Thunk<Expr> base, String[] fieldNames) {
    return BetaNormalizeProjection.apply(base.apply(), fieldNames);
  }

  public Expr onLet(String name, Thunk<Expr> type, Thunk<Expr> value, Thunk<Expr> body) {
    return body.apply()
        .substitute(name, value.apply().increment(name))
        .decrement(name)
        .accept(this);
  }

  public Expr onAnnotated(Thunk<Expr> base, Thunk<Expr> type) {
    return base.apply();
  }

  public Expr onToMap(Thunk<Expr> base, Thunk<Expr> type) {
    return BetaNormalizeToMap.apply(base.apply(), type.apply());
  }

  public Expr onMerge(Thunk<Expr> left, Thunk<Expr> right, Thunk<Expr> type) {
    return BetaNormalizeMerge.apply(left.apply(), right.apply(), type.apply());
  }

  public Expr onNaturalLiteral(BigInteger value) {
    return Expr.makeNaturalLiteral(value);
  }

  public Expr onIntegerLiteral(BigInteger value) {
    return Expr.makeIntegerLiteral(value);
  }

  public Expr onDoubleLiteral(double value) {
    return Expr.makeDoubleLiteral(value);
  }

  public Expr onIdentifier(String name, long index) {
    return Expr.makeIdentifier(name, index);
  }

  public Expr onLambda(String name, Thunk<Expr> type, Thunk<Expr> value) {
    return Expr.makeLambda(name, type.apply(), value.apply());
  }

  public Expr onPi(String name, Thunk<Expr> type, Thunk<Expr> value) {
    return Expr.makePi(name, type.apply(), value.apply());
  }

  public Expr onNonEmptyListLiteral(Iterable<Thunk<Expr>> values, int size) {
    return Expr.makeNonEmptyListLiteral(ThunkUtilities.exprsToArray(values));
  }

  public Expr onEmptyListLiteral(Thunk<Expr> type) {
    return Expr.makeEmptyListLiteral(type.apply());
  }

  public Expr onAssert(Thunk<Expr> type) {
    return Expr.makeAssert(type.apply());
  }

  public Expr onNote(Thunk<Expr> base, Source source) {
    return base.apply();
  }

  public Expr onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    return Expr.makeLocalImport(path, mode, hash);
  }

  public Expr onRemoteImport(URI url, Import.Mode mode, byte[] hash) {
    return Expr.makeRemoteImport(url, mode, hash);
  }

  public Expr onEnvImport(String value, Import.Mode mode, byte[] hash) {
    return Expr.makeEnvImport(value, mode, hash);
  }

  public Expr onMissingImport(Import.Mode mode, byte[] hash) {
    return Expr.makeMissingImport(mode, hash);
  }
}
