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
import java.util.LinkedList;
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
import org.dhallj.core.util.ToStringVisitor;

/**
 * Represents a Dhall expression.
 *
 * <p>Note that there are two tools for manipulating expressions: internal visitors, which are a
 * fold over the structure, and external visitors, which correspond to pattern matching.
 */
public abstract class Expr {
  final int tag;

  Expr(int tag) {
    this.tag = tag;
  }

  public abstract <A> A accept(Visitor<Thunk<A>, A> visitor);

  public abstract <A> A acceptExternal(Visitor<Expr, A> visitor);

  public final Expr increment(String name) {
    return this.acceptVis(new Shift(true, name));
  }

  public final Expr decrement(String name) {
    return this.acceptVis(new Shift(false, name));
  }

  public final Expr substitute(String name, int index, Expr replacement) {
    return this.acceptVis(new Substitute(name, index, replacement));
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
    return this.normalize().alphaNormalize().same(other.normalize().alphaNormalize());
  }

  public final String show() {
    return this.acceptVis(ToStringVisitor.instance);
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
    public static String MAP_KEY_FIELD_NAME = "mapKey";
    public static String MAP_VALUE_FIELD_NAME = "mapValue";

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
    final Expr base;
    final Source source;

    public Parsed(Expr base, Source source) {
      super(Tags.NOTE);
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

  public static final Expr makeApplication(Expr base, List<Expr> args) {
    Expr acc = base;
    for (int i = 0; i < args.size(); i++) {
      acc = Expr.makeApplication(acc, args.get(i));
    }
    return acc;
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

  public static final Expr makeLocalImport(Path path, Import.Mode mode, byte[] hash) {
    return new Constructors.LocalImport(path, mode, hash);
  }

  public static final Expr makeRemoteImport(URI url, Expr using, Import.Mode mode, byte[] hash) {
    return new Constructors.RemoteImport(url, using, mode, hash);
  }

  public static final Expr makeEnvImport(String value, Import.Mode mode, byte[] hash) {
    return new Constructors.EnvImport(value, mode, hash);
  }

  public static final Expr makeMissingImport(Import.Mode mode, byte[] hash) {
    return new Constructors.MissingImport(mode, hash);
  }

  private final class State {
    final Expr expr;
    int state;

    State(Expr expr, int state) {
      this.expr = expr;
      this.state = state;
    }

    public String toString() {
      return String.format("%s %d %d", this.expr, this.expr.tag, this.state);
    }
  }

  public final <A> A acceptVis(Vis<A> vis) {
    State current = new State(this, 0);
    LinkedList<State> stack = new LinkedList<State>();
    LinkedList<A> values = new LinkedList<A>();

    A v0;
    A v1;
    A v2;

    LinkedList<LinkedList<Expr>> applicationStack = new LinkedList<LinkedList<Expr>>();

    while (current != null) {
      switch (current.expr.tag) {
        case Tags.NOTE:
          Parsed tmpNote = (Parsed) current.expr;
          switch (current.state) {
            case 0:
              current.state = 1;
              stack.push(current);
              stack.push(new State(tmpNote.base, 0));
              break;
            case 1:
              v0 = values.poll();
              values.push(vis.onNote(v0, tmpNote.source));
              break;
          }
          break;
        case Tags.NATURAL:
          values.push(vis.onNatural(((Constructors.NaturalLiteral) current.expr).value));
          break;
        case Tags.INTEGER:
          values.push(vis.onInteger(((Constructors.IntegerLiteral) current.expr).value));
          break;
        case Tags.DOUBLE:
          values.push(vis.onDouble(((Constructors.DoubleLiteral) current.expr).value));
          break;
        case Tags.IDENTIFIER:
          Constructors.Identifier tmpIdentifier = (Constructors.Identifier) current.expr;
          values.push(vis.onIdentifier(tmpIdentifier.value, tmpIdentifier.index));
          break;
        case Tags.LAMBDA:
          Constructors.Lambda tmpLambda = (Constructors.Lambda) current.expr;
          switch (current.state) {
            case 0:
              current.state = 1;
              stack.push(current);
              stack.push(new State(tmpLambda.type, 0));
              break;
            case 1:
              vis.bind(tmpLambda.name, tmpLambda.type);
              current.state = 2;
              stack.push(current);
              stack.push(new State(tmpLambda.result, 0));
              break;
            case 2:
              v1 = values.poll();
              v0 = values.poll();
              values.push(vis.onLambda(tmpLambda.name, v0, v1));
          }
          break;
        case Tags.PI:
          Constructors.Pi tmpPi = (Constructors.Pi) current.expr;
          switch (current.state) {
            case 0:
              current.state = 1;
              stack.push(current);
              stack.push(new State(tmpPi.input, 0));
              break;
            case 1:
              vis.bind(tmpPi.param, tmpPi.input);
              current.state = 2;
              stack.push(current);
              stack.push(new State(tmpPi.result, 0));
              break;
            case 2:
              v1 = values.poll();
              v0 = values.poll();
              values.push(vis.onPi(tmpPi.param, v0, v1));
          }
          break;
        case Tags.LET:
          Constructors.Let tmpLet = (Constructors.Let) current.expr;
          switch (current.state) {
            case 0:
              current.state = 1;
              if (tmpLet.type != null) {
                stack.push(current);
                stack.push(new State(tmpLet.type, 0));
                break;
              } else {
                values.push(null);
                continue;
              }
            case 1:
              current.state = 2;
              stack.push(current);
              stack.push(new State(tmpLet.value, 0));
              break;
            case 2:
              vis.bind(tmpLet.name, tmpLet.type);
              current.state = 3;
              stack.push(current);
              stack.push(new State(tmpLet.body, 0));
              break;
            case 3:
              v2 = values.poll();
              v1 = values.poll();
              v0 = values.poll();
              values.push(vis.onLet(tmpLet.name, v0, v1, v2));
          }
          break;
        case Tags.TEXT:
          Constructors.TextLiteral tmpText = (Constructors.TextLiteral) current.expr;
          if (current.state == 0) {
            vis.preText(tmpText.interpolated.length);

            if (tmpText.interpolated.length == 0) {
              values.push(vis.onText(tmpText.parts, new ArrayList<A>()));

            } else {
              current.state = 1;
              stack.push(current);
              stack.push(new State(tmpText.interpolated[current.state - 1], 0));
            }
          } else if (current.state == tmpText.interpolated.length) {
            List<A> results = new ArrayList<A>();
            for (int i = 0; i < tmpText.interpolated.length; i += 1) {
              results.add(values.poll());
            }
            Collections.reverse(results);
            values.push(vis.onText(tmpText.parts, results));
          } else {
            current.state += 1;
            stack.push(current);
            stack.push(new State(tmpText.interpolated[current.state - 1], 0));
          }
          break;
        case Tags.NON_EMPTY_LIST:
          Constructors.NonEmptyListLiteral tmpNonEmptyList =
              (Constructors.NonEmptyListLiteral) current.expr;
          if (current.state == 0) {
            vis.preNonEmptyList(tmpNonEmptyList.values.length);
            current.state = 1;
            stack.push(current);
            stack.push(new State(tmpNonEmptyList.values[current.state - 1], 0));
          } else if (current.state == tmpNonEmptyList.values.length) {
            List<A> results = new ArrayList<A>();
            for (int i = 0; i < tmpNonEmptyList.values.length; i += 1) {
              results.add(values.poll());
            }
            Collections.reverse(results);
            values.push(vis.onNonEmptyList(results));
          } else {
            current.state += 1;
            stack.push(current);
            stack.push(new State(tmpNonEmptyList.values[current.state - 1], 0));
          }
          break;
        case Tags.EMPTY_LIST:
          Constructors.EmptyListLiteral tmpEmptyList = (Constructors.EmptyListLiteral) current.expr;
          if (current.state == 0) {
            current.state = 1;
            stack.push(current);
            stack.push(new State(tmpEmptyList.type, 0));
          } else {
            values.push(vis.onEmptyList(values.poll()));
          }
          break;

        case Tags.RECORD:
          Constructors.RecordLiteral tmpRecord = (Constructors.RecordLiteral) current.expr;
          if (current.state == 0) {
            vis.preRecord(tmpRecord.fields.length);

            if (tmpRecord.fields.length == 0) {
              values.push(vis.onRecord(new ArrayList<Entry<String, A>>()));
            } else {
              current.state = 1;
              stack.push(current);
              stack.push(new State(tmpRecord.fields[current.state - 1].getValue(), 0));
            }
          } else if (current.state == tmpRecord.fields.length) {
            List<Entry<String, A>> results = new ArrayList<Entry<String, A>>();
            for (int i = tmpRecord.fields.length - 1; i >= 0; i -= 1) {
              results.add(new SimpleImmutableEntry(tmpRecord.fields[i].getKey(), values.poll()));
            }
            Collections.reverse(results);
            values.push(vis.onRecord(results));
          } else {
            current.state += 1;
            stack.push(current);
            stack.push(new State(tmpRecord.fields[current.state - 1].getValue(), 0));
          }
          break;

        case Tags.RECORD_TYPE:
          Constructors.RecordType tmpRecordType = (Constructors.RecordType) current.expr;
          if (current.state == 0) {
            vis.preRecord(tmpRecordType.fields.length);

            if (tmpRecordType.fields.length == 0) {
              values.push(vis.onRecordType(new ArrayList<Entry<String, A>>()));
            } else {
              current.state = 1;
              stack.push(current);
              stack.push(new State(tmpRecordType.fields[current.state - 1].getValue(), 0));
            }
          } else if (current.state == tmpRecordType.fields.length) {
            List<Entry<String, A>> results = new ArrayList<Entry<String, A>>();
            for (int i = tmpRecordType.fields.length - 1; i >= 0; i -= 1) {
              results.add(
                  new SimpleImmutableEntry(tmpRecordType.fields[i].getKey(), values.poll()));
            }
            Collections.reverse(results);
            values.push(vis.onRecordType(results));
          } else {
            current.state += 1;
            stack.push(current);
            stack.push(new State(tmpRecordType.fields[current.state - 1].getValue(), 0));
          }
          break;

        case Tags.UNION_TYPE:
          Constructors.UnionType tmpUnionType = (Constructors.UnionType) current.expr;
          if (current.state == 0) {
            vis.preRecord(tmpUnionType.fields.length);

            if (tmpUnionType.fields.length == 0) {
              values.push(vis.onUnionType(new ArrayList<Entry<String, A>>()));
            } else {
              current.state = 1;
              stack.push(current);
              Expr type = tmpUnionType.fields[current.state - 1].getValue();
              if (type == null) {
                values.push(null);
              } else {
                stack.push(new State(type, 0));
              }
            }
          } else if (current.state == tmpUnionType.fields.length) {
            List<Entry<String, A>> results = new ArrayList<Entry<String, A>>();
            for (int i = tmpUnionType.fields.length - 1; i >= 0; i -= 1) {
              results.add(new SimpleImmutableEntry(tmpUnionType.fields[i].getKey(), values.poll()));
            }
            Collections.reverse(results);
            values.push(vis.onUnionType(results));
          } else {
            current.state += 1;
            stack.push(current);
            Expr type = tmpUnionType.fields[current.state - 1].getValue();
            if (type == null) {
              values.push(null);
            } else {
              stack.push(new State(type, 0));
            }
          }
          break;

        case Tags.FIELD_ACCESS:
          Constructors.FieldAccess tmpFieldAccess = (Constructors.FieldAccess) current.expr;
          if (current.state == 0) {
            current.state = 1;
            stack.push(current);
            stack.push(new State(tmpFieldAccess.base, 0));
          } else {
            values.push(vis.onFieldAccess(values.poll(), tmpFieldAccess.fieldName));
          }
          break;

        case Tags.PROJECTION:
          Constructors.Projection tmpProjection = (Constructors.Projection) current.expr;
          if (current.state == 0) {
            current.state = 1;
            stack.push(current);
            stack.push(new State(tmpProjection.base, 0));
          } else {
            values.push(vis.onProjection(values.poll(), tmpProjection.fieldNames));
          }
          break;

        case Tags.PROJECTION_BY_TYPE:
          Constructors.ProjectionByType tmpProjectionByType =
              (Constructors.ProjectionByType) current.expr;
          if (current.state == 0) {
            current.state = 1;
            stack.push(current);
            stack.push(new State(tmpProjectionByType.base, 0));
          } else if (current.state == 1) {
            current.state = 2;
            stack.push(current);
            stack.push(new State(tmpProjectionByType.type, 0));
          } else {
            v1 = values.poll();
            v0 = values.poll();
            values.push(vis.onProjectionByType(v0, v1));
          }
          break;

        case Tags.APPLICATION:
          Constructors.Application tmpApplication = (Constructors.Application) current.expr;

          LinkedList<Expr> application;
          if (current.state == 0) {
            application = new LinkedList<Expr>();
            application.push(tmpApplication.arg);

            Expr candidate = tmpApplication.base;

            while (candidate.tag == Tags.APPLICATION
                || (candidate.tag == Tags.NOTE
                    && ((Parsed) candidate).base.tag == Tags.APPLICATION)) {
              if (candidate.tag == Tags.APPLICATION) {
                Constructors.Application candidateApplication =
                    (Constructors.Application) candidate;
                application.push(candidateApplication.arg);
                candidate = candidateApplication.base;
              } else {
                Constructors.Application candidateApplication =
                    (Constructors.Application) ((Parsed) candidate).base;
                application.push(candidateApplication.arg);
                candidate = candidateApplication.base;
              }
            }

            application.push(candidate);

            vis.preApplication(application.size());
            current.state = application.size();
          } else {
            application = applicationStack.poll();
          }

          if (application.isEmpty()) {
            List<A> args = new ArrayList<A>(current.state);

            for (int i = 0; i < current.state - 1; i++) {
              args.add(values.poll());
            }
            Collections.reverse(args);

            A base = values.poll();

            values.push(vis.onApplication(base, args));
          } else {
            stack.push(current);
            stack.push(new State(application.poll(), 0));
            applicationStack.push(application);
          }
          break;

        case Tags.OPERATOR_APPLICATION:
          Constructors.OperatorApplication tmpOperatorApplication =
              (Constructors.OperatorApplication) current.expr;
          if (current.state == 0) {
            current.state = 1;
            stack.push(current);
            stack.push(new State(tmpOperatorApplication.lhs, 0));
          } else if (current.state == 1) {
            current.state = 2;
            stack.push(current);
            stack.push(new State(tmpOperatorApplication.rhs, 0));
          } else {
            v1 = values.poll();
            v0 = values.poll();
            values.push(vis.onOperatorApplication(tmpOperatorApplication.operator, v0, v1));
          }
          break;
        case Tags.IF:
          Constructors.If tmpIf = (Constructors.If) current.expr;
          if (current.state == 0) {
            current.state = 1;
            stack.push(current);
            stack.push(new State(tmpIf.predicate, 0));
          } else if (current.state == 1) {
            current.state = 2;
            stack.push(current);
            stack.push(new State(tmpIf.thenValue, 0));
          } else if (current.state == 2) {
            current.state = 3;
            stack.push(current);
            stack.push(new State(tmpIf.elseValue, 0));
          } else {
            v2 = values.poll();
            v1 = values.poll();
            v0 = values.poll();
            values.push(vis.onIf(v0, v1, v2));
          }
          break;
        case Tags.ANNOTATED:
          Constructors.Annotated tmpAnnotated = (Constructors.Annotated) current.expr;
          if (current.state == 0) {
            current.state = 1;
            stack.push(current);
            stack.push(new State(tmpAnnotated.base, 0));
          } else if (current.state == 1) {
            current.state = 2;
            stack.push(current);
            stack.push(new State(tmpAnnotated.type, 0));
          } else {
            v1 = values.poll();
            v0 = values.poll();
            values.push(vis.onAnnotated(v0, v1));
          }
          break;
        case Tags.ASSERT:
          Constructors.Assert tmpAssert = (Constructors.Assert) current.expr;
          if (current.state == 0) {
            current.state = 1;
            stack.push(current);
            stack.push(new State(tmpAssert.base, 0));
          } else {
            values.push(vis.onAssert(values.poll()));
          }
          break;
        case Tags.MERGE:
          Constructors.Merge tmpMerge = (Constructors.Merge) current.expr;
          switch (current.state) {
            case 0:
              current.state = 1;
              stack.push(current);
              stack.push(new State(tmpMerge.handlers, 0));
              break;
            case 1:
              current.state = 2;
              stack.push(current);
              stack.push(new State(tmpMerge.union, 0));
              break;
            case 2:
              current.state = 3;

              if (tmpMerge.type != null) {
                stack.push(current);
                stack.push(new State(tmpMerge.type, 0));
                break;
              } else {
                values.push(null);
                continue;
              }
            case 3:
              v2 = values.poll();
              v1 = values.poll();
              v0 = values.poll();
              values.push(vis.onMerge(v0, v1, v2));

              break;
          }
          break;
        case Tags.TO_MAP:
          Constructors.ToMap tmpToMap = (Constructors.ToMap) current.expr;
          switch (current.state) {
            case 0:
              current.state = 1;
              stack.push(current);
              stack.push(new State(tmpToMap.base, 0));
              break;
            case 1:
              current.state = 2;
              stack.push(current);

              if (tmpToMap.type != null) {
                stack.push(new State(tmpToMap.type, 0));
                break;
              } else {
                values.push(null);
                continue;
              }
            case 2:
              v1 = values.poll();
              v0 = values.poll();
              values.push(vis.onToMap(v0, v1));

              break;
          }
          break;

        case Tags.MISSING_IMPORT:
          Constructors.MissingImport tmpMissingImport = (Constructors.MissingImport) current.expr;

          values.push(vis.onMissingImport(tmpMissingImport.mode, tmpMissingImport.hash));
          break;

        case Tags.ENV_IMPORT:
          Constructors.EnvImport tmpEnvImport = (Constructors.EnvImport) current.expr;

          values.push(vis.onEnvImport(tmpEnvImport.value, tmpEnvImport.mode, tmpEnvImport.hash));
          break;
        case Tags.LOCAL_IMPORT:
          Constructors.LocalImport tmpLocalImport = (Constructors.LocalImport) current.expr;

          values.push(
              vis.onLocalImport(tmpLocalImport.path, tmpLocalImport.mode, tmpLocalImport.hash));
          break;
        case Tags.REMOTE_IMPORT:
          Constructors.RemoteImport tmpRemoteImport = (Constructors.RemoteImport) current.expr;

          switch (current.state) {
            case 0:
              current.state = 1;
              stack.push(current);

              if (tmpRemoteImport.using != null) {
                stack.push(new State(tmpRemoteImport.using, 0));
                break;
              } else {
                values.push(null);
                continue;
              }
            case 1:
              values.push(
                  vis.onRemoteImport(
                      tmpRemoteImport.url,
                      values.poll(),
                      tmpRemoteImport.mode,
                      tmpRemoteImport.hash));

              break;
          }
          break;
      }
      current = stack.poll();
    }

    return values.poll();
  }
}
