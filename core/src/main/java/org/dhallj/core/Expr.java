package org.dhallj.core;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.dhallj.cbor.ByteArrayWriter;
import org.dhallj.cbor.Writer;
import org.dhallj.core.ast.AsBoolLiteral;
import org.dhallj.core.ast.AsDoubleLiteral;
import org.dhallj.core.ast.AsIntegerLiteral;
import org.dhallj.core.ast.AsListLiteral;
import org.dhallj.core.ast.AsNaturalLiteral;
import org.dhallj.core.ast.AsRecordLiteral;
import org.dhallj.core.ast.AsRecordType;
import org.dhallj.core.ast.AsSimpleIdentifier;
import org.dhallj.core.ast.AsSimpleTextLiteral;
import org.dhallj.core.ast.AsUnionType;
import org.dhallj.core.ast.IsIdentifier;
import org.dhallj.core.binary.Encode;
import org.dhallj.core.normalization.AlphaNormalize;
import org.dhallj.core.normalization.BetaNormalize;
import org.dhallj.core.normalization.Shift;
import org.dhallj.core.normalization.Substitute;
import org.dhallj.core.properties.IsResolved;
import org.dhallj.core.typechecking.TypeCheck;
import org.dhallj.core.util.ThunkUtilities;

/**
 * Represents a Dhall expression.
 *
 * <p>Note that there are two tools for manipulating expressions: internal visitors, which are a
 * fold over the structure, and external visitors, which correspond to pattern matching.
 */
public abstract class Expr {
  public abstract <A> A accept(Visitor<Thunk<A>, A> visitor);

  public abstract <A> A acceptExternal(Visitor<Expr, A> visitor);

  public final Expr increment(String name) {
    return this.accept(new Shift(true, name));
  }

  public final Expr decrement(String name) {
    return this.accept(new Shift(false, name));
  }

  public final Expr substitute(String name, int index, Expr replacement) {
    return this.accept(new Substitute(name, index, replacement));
  }

  public final Expr substitute(String name, Expr replacement) {
    return this.substitute(name, 0, replacement);
  }

  public final Expr alphaNormalize() {
    return this.acceptExternal(AlphaNormalize.instance);
  }

  public final Expr normalize() {
    return this.accept(BetaNormalize.instance);
  }

  public final Expr typeCheck() {
    return this.acceptExternal(new TypeCheck());
  }

  public final void encode(Writer writer) {
    this.acceptExternal(new Encode(writer));
  }

  public final byte[] encodeToByteArray() {
    ByteArrayWriter writer = new ByteArrayWriter();
    this.acceptExternal(new Encode(writer));
    return writer.getBytes();
  }

  public final String hash() {
    ByteArrayWriter writer = new ByteArrayWriter();
    this.acceptExternal(new Encode(writer));
    byte[] bs = writer.getBytes();

    java.security.MessageDigest digest = null;
    try {
      digest = java.security.MessageDigest.getInstance("SHA-256");
    } catch (java.security.NoSuchAlgorithmException e) {

    }
    byte[] encoded = digest.digest(bs);
    return bytesToHex(encoded);
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder();
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if (hex.length() == 1) hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public final boolean isType() {
    return this.typeCheck().equivalent(Constants.TYPE);
  }

  public final boolean isKind() {
    return this.typeCheck().equivalent(Constants.KIND);
  }

  public final boolean isSort() {
    return this.typeCheck().equivalent(Constants.SORT);
  }

  public final Boolean asBoolLiteral() {
    return this.acceptExternal(AsBoolLiteral.instance);
  }

  public final BigInteger asNaturalLiteral() {
    return this.acceptExternal(AsNaturalLiteral.instance);
  }

  public final BigInteger asIntegerLiteral() {
    return this.acceptExternal(AsIntegerLiteral.instance);
  }

  public final Double asDoubleLiteral() {
    return this.acceptExternal(AsDoubleLiteral.instance);
  }

  public final String asSimpleTextLiteral() {
    return this.acceptExternal(AsSimpleTextLiteral.instance);
  }

  public final String asSimpleIdentifier() {
    return this.acceptExternal(AsSimpleIdentifier.instance);
  }

  public final boolean isIdentifier(String name, long index) {
    return this.acceptExternal(new IsIdentifier(name, index));
  }

  public final boolean isIdentifier(String name) {
    return this.isIdentifier(name, 0);
  }

  public final List<Expr> asListLiteral() {
    return this.acceptExternal(AsListLiteral.instance);
  }

  public final Iterable<Entry<String, Expr>> asRecordLiteral() {
    return this.acceptExternal(AsRecordLiteral.instance);
  }

  public final Iterable<Entry<String, Expr>> asRecordType() {
    return this.acceptExternal(AsRecordType.instance);
  }

  public final Iterable<Entry<String, Expr>> asUnionType() {
    return this.acceptExternal(AsUnionType.instance);
  }

  public final boolean isResolved() {
    return this.accept(IsResolved.instance);
  }

  public final boolean same(Expr other) {
    return org.dhallj.core.util.EqualsVisitor.equals(this, other);
  }

  public final boolean equivalent(Expr other) {
    // TODO fix
    return same(other);
  }

  public final String show() {
    return org.dhallj.core.util.ToStringVisitor.show(this);
  }

  public final String toString() {
    return this.show();
  }

  public static final class Sugar {
    public static final Expr desugarComplete(Expr lhs, Expr rhs) {

      return Expr.makeAnnotated(
          Expr.makeOperatorApplication(Operator.PREFER, Expr.makeFieldAccess(lhs, "default"), rhs),
          Expr.makeFieldAccess(lhs, "Type"));
    }
  }

  public static final class Constants {
    private static List<Entry<String, Expr>> emptyFields = new ArrayList(0);

    public static Expr UNDERSCORE = makeIdentifier("_");
    public static Expr SORT = makeIdentifier("Sort");
    public static Expr KIND = makeIdentifier("Kind");
    public static Expr TYPE = makeIdentifier("Type");
    public static Expr BOOL = makeIdentifier("Bool");
    public static Expr TRUE = makeIdentifier("True");
    public static Expr FALSE = makeIdentifier("False");
    public static Expr LIST = makeIdentifier("List");
    public static Expr OPTIONAL = makeIdentifier("Optional");
    public static Expr DOUBLE = makeIdentifier("Double");
    public static Expr NATURAL = makeIdentifier("Natural");
    public static Expr INTEGER = makeIdentifier("Integer");
    public static Expr TEXT = makeIdentifier("Text");
    public static Expr NONE = makeIdentifier("None");
    public static Expr SOME = makeIdentifier("Some");
    public static Expr NATURAL_FOLD = makeIdentifier("Natural/fold");
    public static Expr LIST_FOLD = makeIdentifier("List/fold");
    public static Expr ZERO = makeNaturalLiteral(BigInteger.ZERO);
    public static Expr EMPTY_RECORD_LITERAL = makeRecordLiteral(emptyFields);
    public static Expr EMPTY_RECORD_TYPE = makeRecordType(emptyFields);

    private static Set<String> builtInNames = new HashSet();

    static {
      builtInNames.add("Bool");
      builtInNames.add("Double");
      builtInNames.add("Double/show");
      builtInNames.add("False");
      builtInNames.add("Integer");
      builtInNames.add("Integer/clamp");
      builtInNames.add("Integer/negate");
      builtInNames.add("Integer/show");
      builtInNames.add("Integer/toDouble");
      builtInNames.add("Kind");
      builtInNames.add("List");
      builtInNames.add("List/build");
      builtInNames.add("List/fold");
      builtInNames.add("List/head");
      builtInNames.add("List/indexed");
      builtInNames.add("List/last");
      builtInNames.add("List/length");
      builtInNames.add("List/reverse");
      builtInNames.add("Natural");
      builtInNames.add("Natural/build");
      builtInNames.add("Natural/even");
      builtInNames.add("Natural/isZero");
      builtInNames.add("Natural/odd");
      builtInNames.add("Natural/show");
      builtInNames.add("Natural/subtract");
      builtInNames.add("Natural/toInteger");
      builtInNames.add("None");
      builtInNames.add("Optional");
      builtInNames.add("Optional/build");
      builtInNames.add("Optional/fold");
      builtInNames.add("Some");
      builtInNames.add("Text");
      builtInNames.add("Text/show");
      builtInNames.add("True");
      builtInNames.add("Type");
    }

    public static boolean isBuiltInConstant(String name) {
      return builtInNames.contains(name);
    }
  }

  public static final class Parsed extends Expr {
    private final Expr base;
    private final Source source;

    public Parsed(Expr base, Source source) {
      this.base = base;
      this.source = source;
    }

    public final Source getSource() {
      return this.source;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> baseVisited = new ExprUtilities.ExprThunk(visitor, this.base);
      return visitor.onNote(baseVisited, this.source);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onNote(base, this.source);
    }
  }

  public static final Expr makeDoubleLiteral(double value) {
    return new Constructors.DoubleLiteral(value);
  }

  public static final Expr makeNaturalLiteral(BigInteger value) {
    return new Constructors.NaturalLiteral(value);
  }

  public static final Expr makeIntegerLiteral(BigInteger value) {
    return new Constructors.IntegerLiteral(value);
  }

  public static final Expr makeTextLiteral(String[] parts, Expr[] interpolated) {
    return new Constructors.TextLiteral(parts, interpolated);
  }

  public static final Expr makeTextLiteral(String[] parts, Iterable<Expr> interpolated) {
    return new Constructors.TextLiteral(parts, ExprUtilities.exprsToArray(interpolated));
  }

  public static final Expr makeTextLiteralThunked(
      String[] parts, Iterable<Thunk<Expr>> interpolated) {
    return new Constructors.TextLiteral(parts, ThunkUtilities.exprsToArray(interpolated));
  }

  public static final Expr makeTextLiteral(String value) {
    String[] parts = {value};
    return new Constructors.TextLiteral(parts, ExprUtilities.exprsToArray(new ArrayList(0)));
  }

  public static final Expr makeApplication(Expr base, Expr arg) {
    return new Constructors.Application(base, arg);
  }

  public static final Expr makeOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    return new Constructors.OperatorApplication(operator, lhs, rhs);
  }

  public static final Expr makeIf(Expr cond, Expr thenValue, Expr elseValue) {
    return new Constructors.If(cond, thenValue, elseValue);
  }

  public static final Expr makeLambda(String param, Expr input, Expr result) {
    return new Constructors.Lambda(param, input, result);
  }

  public static final Expr makePi(String param, Expr input, Expr result) {
    return new Constructors.Pi(param, input, result);
  }

  public static final Expr makePi(Expr input, Expr result) {
    return makePi("_", input, result);
  }

  public static final Expr makeAssert(Expr base) {
    return new Constructors.Assert(base);
  }

  public static final Expr makeFieldAccess(Expr base, String fieldName) {
    return new Constructors.FieldAccess(base, fieldName);
  }

  public static final Expr makeProjection(Expr base, String[] fieldNames) {
    return new Constructors.Projection(base, fieldNames);
  }

  public static final Expr makeProjectionByType(Expr base, Expr tpe) {
    return new Constructors.ProjectionByType(base, tpe);
  }

  public static final Expr makeIdentifier(String value, long index) {
    return new Constructors.Identifier(value, index);
  }

  public static final Expr makeIdentifier(String value) {
    return makeIdentifier(value, 0);
  }

  public static final Expr makeRecordLiteral(Entry<String, Expr>[] fields) {
    return new Constructors.RecordLiteral(fields);
  }

  public static final Expr makeRecordLiteral(Iterable<Entry<String, Expr>> fields) {
    return new Constructors.RecordLiteral(ExprUtilities.entriesToArray(fields));
  }

  public static final Expr makeRecordLiteralThunked(Iterable<Entry<String, Thunk<Expr>>> fields) {
    return new Constructors.RecordLiteral(ExprUtilities.entriesToArrayThunked(fields));
  }

  public static final Expr makeRecordLiteral(String key, Expr value) {
    return new Constructors.RecordLiteral(ExprUtilities.entryToArray(key, value));
  }

  public static final Expr makeRecordType(Entry<String, Expr>[] fields) {
    return new Constructors.RecordType(fields);
  }

  public static final Expr makeRecordType(Iterable<Entry<String, Expr>> fields) {
    return new Constructors.RecordType(ExprUtilities.entriesToArray(fields));
  }

  public static final Expr makeRecordTypeThunked(Iterable<Entry<String, Thunk<Expr>>> fields) {
    return new Constructors.RecordType(ExprUtilities.entriesToArrayThunked(fields));
  }

  public static final Expr makeUnionType(Entry<String, Expr>[] fields) {
    return new Constructors.UnionType(fields);
  }

  public static final Expr makeUnionType(Iterable<Entry<String, Expr>> fields) {
    return new Constructors.UnionType(ExprUtilities.entriesToArray(fields));
  }

  public static final Expr makeUnionTypeThunked(Iterable<Entry<String, Thunk<Expr>>> fields) {
    return new Constructors.UnionType(ExprUtilities.entriesToArrayThunked(fields));
  }

  public static final Expr makeNonEmptyListLiteral(Expr[] values) {
    return new Constructors.NonEmptyListLiteral(values);
  }

  public static final Expr makeNonEmptyListLiteral(Iterable<Expr> values) {
    return new Constructors.NonEmptyListLiteral(ExprUtilities.exprsToArray(values));
  }

  public static final Expr makeNonEmptyListLiteralThunked(Iterable<Thunk<Expr>> values) {
    return new Constructors.NonEmptyListLiteral(ThunkUtilities.exprsToArray(values));
  }

  public static final Expr makeEmptyListLiteral(Expr tpe) {
    return new Constructors.EmptyListLiteral(tpe);
  }

  public static final Expr makeNote(Expr base, Source source) {
    return new Parsed(base, source);
  }

  public static final Expr makeLet(String name, Expr type, Expr value, Expr body) {
    return new Constructors.Let(name, type, value, body);
  }

  public static final Expr makeLet(String name, Expr value, Expr body) {
    return makeLet(name, null, value, body);
  }

  public static final Expr makeAnnotated(Expr base, Expr type) {
    return new Constructors.Annotated(base, type);
  }

  public static final Expr makeToMap(Expr base, Expr type) {
    return new Constructors.ToMap(base, type);
  }

  public static final Expr makeToMap(Expr base) {
    return makeToMap(base, null);
  }

  public static final Expr makeMerge(Expr left, Expr right, Expr type) {
    return new Constructors.Merge(left, right, type);
  }

  public static final Expr makeMerge(Expr left, Expr right) {
    return makeMerge(left, right, null);
  }

  public static final Expr makeLocalImport(Path path, Import.Mode mode) {
    return new Constructors.LocalImport(path, mode);
  }

  public static final Expr makeRemoteImport(URI url, Import.Mode mode) {
    return new Constructors.RemoteImport(url, mode);
  }

  public static final Expr makeEnvImport(String value, Import.Mode mode) {
    return new Constructors.EnvImport(value, mode);
  }

  public static final Expr makeMissingImport(Import.Mode mode) {
    return new Constructors.MissingImport(mode);
  }
}
