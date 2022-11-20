package org.dhallj.core;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.dhallj.cbor.Writer;
import org.dhallj.core.binary.Encode;
import org.dhallj.core.normalization.AlphaNormalize;
import org.dhallj.core.normalization.BetaNormalize;
import org.dhallj.core.normalization.Shift;
import org.dhallj.core.normalization.Substitute;
import org.dhallj.core.typechecking.TypeCheck;

/**
 * Represents a Dhall expression.
 *
 * <p>Note that there are two tools for manipulating expressions: internal visitors, which are a
 * fold over the structure, and external visitors, which correspond to pattern matching.
 */
public abstract class Expr {
  final int tag;
  private final AtomicReference<byte[]> cachedHashBytes = new AtomicReference<byte[]>();

  Expr(int tag) {
    this.tag = tag;
  }

  /** Run the given external visitor on this expression. */
  public abstract <A> A accept(ExternalVisitor<A> visitor);

  abstract <A> void advance(VisitState<A> state);

  /** Run the given internal visitor on this expression. */
  public final <A> A accept(Visitor<A> visitor) {
    VisitState<A> state = new VisitState<A>(visitor, this);
    while (state.current != null) {
      state.current.expr.advance(state);
      state.current = state.stack.poll();
    }
    return state.valueStack.poll();
  }

  /**
   * Beta-normalize this expression.
   *
   * <p>This operation "evaluates" the expression.
   */
  public final Expr normalize() {
    return this.accept(BetaNormalize.instance);
  }

  /**
   * Alpha-normalize this expression.
   *
   * <p>This operation replaces all variable names with De-Bruijn-indexed underscores.
   */
  public final Expr alphaNormalize() {
    return this.accept(new AlphaNormalize());
  }

  /** Increment a variable name in this expression. */
  public final Expr increment(String name) {
    return this.accept(new Shift(true, name));
  }

  /** Increment a variable name in this expression. */
  public final Expr decrement(String name) {
    return this.accept(new Shift(false, name));
  }

  /** Substitute the given expression for the given variable name in this expression. */
  public final Expr substitute(String name, Expr replacement) {
    return this.accept(new Substitute(name, replacement));
  }

  /**
   * Encode this expression as a CBOR byte array.
   *
   * <p>Note that this method does not normalize the expression.
   */
  public final byte[] getEncodedBytes() {
    Writer.ByteArrayWriter writer = new Writer.ByteArrayWriter();
    this.accept(new Encode(writer));
    return writer.getBytes();
  }

  /**
   * Encode this expression as a CBOR byte array and return the SHA-256 hash of the result.
   *
   * <p>Note that this method does not normalize the expression.
   */
  public final byte[] getHashBytes() {
    byte[] result = this.cachedHashBytes.get();

    if (result == null) {
      Writer.SHA256Writer writer = new Writer.SHA256Writer();
      this.accept(new Encode(writer));
      result = writer.getHashBytes();

      if (!this.cachedHashBytes.compareAndSet(null, result)) {
        return this.cachedHashBytes.get();
      }
    }
    return result;
  }

  /**
   * Encode this expression as a CBOR byte array and return the SHA-256 hash of the result as a
   * string.
   *
   * <p>Note that this method does not normalize the expression.
   */
  public final String hash() {
    return Util.encodeHashBytes(this.getHashBytes());
  }

  /** Check whether all imports in this expression have been resolved. */
  public final boolean isResolved() {
    return this.accept(IsResolved.instance);
  }

  /**
   * Check whether this expression has the same structure as another.
   *
   * <p>This method is a stricter than {@code equivalent} in that it doesn't normalize its
   * arguments.
   */
  public final boolean sameStructure(Expr other) {
    return this.getFirstDiff(other) == null;
  }

  /**
   * Check whether this expression is equivalent to another.
   *
   * <p>Note that this method normalizes both expressions before comparing.
   */
  public final boolean equivalent(Expr other) {
    return Arrays.equals(
        this.normalize().alphaNormalize().getEncodedBytes(),
        other.normalize().alphaNormalize().getEncodedBytes());
  }

  /**
   * Check whether this expression is equivalent to another value.
   *
   * <p>Note that this method normalizes both expressions before comparing. Also note that in future
   * releases this method may compare the hashes of the encoded bytes, but currently it is identical
   * to equivalent (but with an {@code Object} argument because Java).
   */
  public final boolean equals(Object obj) {
    if (obj instanceof Expr) {
      return this.equivalent((Expr) obj);
    } else {
      return false;
    }
  }

  /** Hashes the CBOR encoding of this expression. */
  public final int hashCode() {
    return Arrays.hashCode(this.normalize().alphaNormalize().getEncodedBytes());
  }

  public final String toString() {
    return this.accept(ToStringVisitor.instance).toString();
  }

  Expr getNonNote() {
    Expr current = this;

    while (current.tag == Tags.NOTE) {
      current = ((Parsed) current).base;
    }

    return current;
  }

  /**
   * Convenience methods for working with expressions.
   *
   * <p>Note that many of these operations represent "down-casts", and return {@code null} in cases
   * where the expression doesn't have the requested constructor.
   */
  public static final class Util {
    private Util() {}

    /** Type-check the given expression and return the inferred type. */
    public static final Expr typeCheck(Expr expr) {
      return expr.accept(new TypeCheck());
    }

    /** Return the first difference between the structure of two expressions as a pair. */
    public static final Entry<Expr, Expr> getFirstDiff(Expr first, Expr second) {
      return first.getFirstDiff(second);
    }

    /** Write an encoded expression to a stream. */
    public static final void encodeToStream(Expr expr, OutputStream stream) {
      Writer.OutputStreamWriter writer = new Writer.OutputStreamWriter(stream);
      expr.accept(new Encode(writer));
    }

    /** Encode an array of bytes as a hex string. */
    public static String encodeHashBytes(byte[] hash) {
      StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    }

    public static final byte[] decodeHashBytes(String input) {
      byte[] bytes = new byte[input.length() / 2];
      for (int i = 0; i < input.length(); i += 2) {

        int d1 = Character.digit(input.charAt(i), 16);
        int d2 = Character.digit(input.charAt(i + 1), 16);
        bytes[i / 2] = (byte) ((d1 << 4) + d2);
      }
      return bytes;
    }

    /** If the expression is an application of {@code List}, return the element type. */
    public static Expr getListArg(Expr expr) {
      return getElementType(expr, "List");
    }

    /** If the expression is an application of {@code Optional}, return the element type. */
    public static Expr getOptionalArg(Expr expr) {
      return getElementType(expr, "Optional");
    }

    /** If the expression is an application of {@code Some}, return the element type. */
    public static Expr getSomeArg(Expr expr) {
      return getElementType(expr, "Some");
    }

    /** If the expression is an application of {@code None}, return the element type. */
    public static Expr getNoneArg(Expr expr) {
      return getElementType(expr, "None");
    }

    /** If the expression is a {@code Bool} literal, return its value. */
    public static final Boolean asBoolLiteral(Expr expr) {
      String asBuiltIn = Util.asBuiltIn(expr);

      if (asBuiltIn != null) {
        if (asBuiltIn.equals("True")) {
          return true;
        } else if (asBuiltIn.equals("False")) {
          return false;
        }
      }
      return null;
    }

    /** If the expression is a {@code Natural} literal, return its value. */
    public static final BigInteger asNaturalLiteral(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.NATURAL) {
        return ((Constructors.NaturalLiteral) value).value;
      } else {
        return null;
      }
    }

    /** If the expression is an {@code Integer} literal, return its value. */
    public static final BigInteger asIntegerLiteral(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.INTEGER) {
        return ((Constructors.IntegerLiteral) value).value;
      } else {
        return null;
      }
    }

    /** If the expression is a {@code Double} literal, return its value. */
    public static final Double asDoubleLiteral(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.DOUBLE) {
        return ((Constructors.DoubleLiteral) value).value;
      } else {
        return null;
      }
    }

    /** If the expression is a {@code Text} literal with no interpolations, return its value. */
    public static final String asSimpleTextLiteral(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.TEXT) {
        Constructors.TextLiteral text = (Constructors.TextLiteral) value;

        if (text.parts.length == 1) {
          return text.parts[0];
        } else {
          return null;
        }
      } else {
        return null;
      }
    }

    /** If the expression is a Dhall built-in, return its name. */
    public static final String asBuiltIn(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.BUILT_IN) {
        return ((Constructors.BuiltIn) value).name;
      } else {
        return null;
      }
    }

    /** If the expression is a {@code List} literal, return its contents. */
    public static final List<Expr> asListLiteral(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.NON_EMPTY_LIST) {
        return Arrays.asList(((Constructors.NonEmptyListLiteral) value).values);
      } else if (value.tag == Tags.EMPTY_LIST) {
        return new ArrayList<Expr>(0);
      } else {
        return null;
      }
    }

    /** If the expression is a record literal, return its fields. */
    public static final List<Entry<String, Expr>> asRecordLiteral(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.RECORD) {
        return Arrays.asList(((Constructors.RecordLiteral) value).fields);
      } else {
        return null;
      }
    }

    /** If the expression is a record type, return its fields. */
    public static final List<Entry<String, Expr>> asRecordType(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.RECORD_TYPE) {
        return Arrays.asList(((Constructors.RecordType) value).fields);
      } else {
        return null;
      }
    }

    /** If the expression is a union type, return its fields. */
    public static final List<Entry<String, Expr>> asUnionType(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.UNION_TYPE) {
        return Arrays.asList(((Constructors.UnionType) value).fields);
      } else {
        return null;
      }
    }

    /** If the expression is a field access, return the base and field name. */
    public static final Entry<Expr, String> asFieldAccess(Expr expr) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.FIELD_ACCESS) {
        Constructors.FieldAccess fieldAccess = (Constructors.FieldAccess) value;
        return new SimpleImmutableEntry<>(fieldAccess.base, fieldAccess.fieldName);
      } else {
        return null;
      }
    }

    /**
     * If the expression is an application of the specified type constructor, return the element
     * type.
     */
    private static Expr getElementType(Expr expr, String typeConstructor) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.APPLICATION) {
        Constructors.Application application = (Constructors.Application) value;

        Expr applied = application.base.getNonNote();

        if (applied.tag == Tags.BUILT_IN
            && ((Constructors.BuiltIn) applied).name.equals(typeConstructor)) {
          return application.arg;
        }
      }

      return null;
    }

    public static final String escapeText(String input, boolean quoted) {
      StringBuilder builder = new StringBuilder();

      if (quoted) {
        builder.append("\\\"");
      }

      for (int i = 0; i < input.length(); i++) {
        char c = input.charAt(i);
        if (c == '"') {
          if (quoted) {
            builder.append("\\\\\"");
          } else {
            builder.append("\\\"");
          }
        } else if (c == '$') {
          if (quoted) {
            builder.append("\\\\u0024");
          } else {
            builder.append("$");
          }
        } else if (c == '\\') {
          if (quoted) {
            builder.append("\\\\");
          } else {
            builder.append("\\");
          }
        } else if (c >= '\u0000' && c <= '\u001f') {
          if (quoted) {
            builder.append('\\');
          }
          String asHex = Long.toHexString((long) c);

          builder.append("\\u");

          if (asHex.length() < 2) {
            builder.append('0');
          }
          if (asHex.length() < 3) {
            builder.append('0');
          }
          if (asHex.length() < 4) {
            builder.append('0');
          }
          builder.append(asHex);
        } else {
          builder.append(c);
        }
      }
      if (quoted) {
        builder.append("\\\"");
      }

      return builder.toString();
    }

    /** Desugar the complete operator ({@code ::}). */
    public static final Expr desugarComplete(Expr lhs, Expr rhs) {

      return Expr.makeAnnotated(
          Expr.makeOperatorApplication(Operator.PREFER, Expr.makeFieldAccess(lhs, "default"), rhs),
          Expr.makeFieldAccess(lhs, "Type"));
    }

    /**
     * If the expression is a lambda, apply it to the given argument.
     *
     * <p>Returns null if the expression is not a lambda.
     */
    public static final Expr applyAsLambda(Expr expr, Expr arg) {
      Expr value = expr.getNonNote();

      if (value.tag == Tags.LAMBDA) {
        Constructors.Lambda lambda = ((Constructors.Lambda) value);
        return lambda.result.substitute(lambda.name, arg);
      } else {
        return null;
      }
    }

    static final Entry<Expr, Expr> flattenToMapRecord(List<Entry<String, Expr>> fields) {
      if (fields == null || fields.size() != 2) {
        return null;
      }

      Expr key = null;
      Expr value = null;

      for (Entry<String, Expr> entry : fields) {
        if (entry.getKey().equals(Constants.MAP_KEY_FIELD_NAME)) {
          key = entry.getValue();
        } else if (entry.getKey().equals(Constants.MAP_VALUE_FIELD_NAME)) {
          value = entry.getValue();
        }
      }

      if (key == null || value == null) {
        return null;
      }

      return new SimpleImmutableEntry<>(key, value);
    }
  }

  /** Represents the first part of a {@code let}-expression. */
  public static final class LetBinding<A> {
    private final String name;
    private final A type;
    private final A value;

    public LetBinding(String name, A type, A value) {
      this.name = name;
      this.type = type;
      this.value = value;
    }

    public String getName() {
      return this.name;
    }

    public boolean hasType() {
      return this.type != null;
    }

    public A getType() {
      return this.type;
    }

    public A getValue() {
      return this.value;
    }
  }

  /** Modifier specifying how an import should be parsed into a Dhall expression. */
  public static enum ImportMode {
    CODE,
    RAW_TEXT,
    LOCATION;

    public String toString() {
      if (this == RAW_TEXT) {
        return "Text";
      } else if (this == LOCATION) {
        return "Location";
      } else {
        return "Code";
      }
    }
  }

  /** Definitions of Dhall built-ins and other frequently-used expressions. */
  public static final class Constants {
    private static final Entry[] emptyFields = {};

    public static final Expr UNDERSCORE = makeIdentifier("_");
    public static final Expr SORT = new Constructors.BuiltIn("Sort");
    public static final Expr KIND = new Constructors.BuiltIn("Kind");
    public static final Expr TYPE = new Constructors.BuiltIn("Type");
    public static final Expr BOOL = new Constructors.BuiltIn("Bool");
    public static final Expr TRUE = new Constructors.BuiltIn("True");
    public static final Expr FALSE = new Constructors.BuiltIn("False");
    public static final Expr LIST = new Constructors.BuiltIn("List");
    public static final Expr OPTIONAL = new Constructors.BuiltIn("Optional");
    public static final Expr DOUBLE = new Constructors.BuiltIn("Double");
    public static final Expr NATURAL = new Constructors.BuiltIn("Natural");
    public static final Expr INTEGER = new Constructors.BuiltIn("Integer");
    public static final Expr TEXT = new Constructors.BuiltIn("Text");
    public static final Expr NONE = new Constructors.BuiltIn("None");
    public static final Expr SOME = new Constructors.BuiltIn("Some");

    public static final Expr DATE = new Constructors.BuiltIn("Date");
    public static final Expr TIME = new Constructors.BuiltIn("Time");
    public static final Expr TIME_ZONE = new Constructors.BuiltIn("TimeZone");

    public static final Expr NATURAL_FOLD = new Constructors.BuiltIn("Natural/fold");
    public static final Expr LIST_FOLD = new Constructors.BuiltIn("List/fold");
    public static final Expr ZERO = makeNaturalLiteral(BigInteger.ZERO);
    public static final Expr EMPTY_RECORD_LITERAL = makeRecordLiteral(emptyFields);
    public static final Expr EMPTY_RECORD_TYPE = makeRecordType(emptyFields);
    public static final Expr EMPTY_UNION_TYPE = makeUnionType(emptyFields);
    public static final Expr LOCATION_TYPE =
        makeUnionType(
            new Entry[] {
              new SimpleImmutableEntry<>("Environment", TEXT),
              new SimpleImmutableEntry<>("Local", TEXT),
              new SimpleImmutableEntry<>("Missing", null),
              new SimpleImmutableEntry<>("Remote", TEXT)
            });
    public static final String MAP_KEY_FIELD_NAME = "mapKey";
    public static final String MAP_VALUE_FIELD_NAME = "mapValue";

    private static final Map<String, Expr> builtIns = new HashMap<String, Expr>(34);
    private static final Set<String> keywords = new HashSet<String>(16);

    static {
      builtIns.put("Bool", BOOL);
      builtIns.put("Date", DATE);
      builtIns.put("Double", DOUBLE);
      builtIns.put("Double/show", new Constructors.BuiltIn("Double/show"));
      builtIns.put("False", FALSE);
      builtIns.put("Integer", INTEGER);
      builtIns.put("Integer/clamp", new Constructors.BuiltIn("Integer/clamp"));
      builtIns.put("Integer/negate", new Constructors.BuiltIn("Integer/negate"));
      builtIns.put("Integer/show", new Constructors.BuiltIn("Integer/show"));
      builtIns.put("Integer/toDouble", new Constructors.BuiltIn("Integer/toDouble"));
      builtIns.put("Kind", KIND);
      builtIns.put("List", LIST);
      builtIns.put("List/build", new Constructors.BuiltIn("List/build"));
      builtIns.put("List/fold", new Constructors.BuiltIn("List/fold"));
      builtIns.put("List/head", new Constructors.BuiltIn("List/head"));
      builtIns.put("List/indexed", new Constructors.BuiltIn("List/indexed"));
      builtIns.put("List/last", new Constructors.BuiltIn("List/last"));
      builtIns.put("List/length", new Constructors.BuiltIn("List/length"));
      builtIns.put("List/reverse", new Constructors.BuiltIn("List/reverse"));
      builtIns.put("Natural", NATURAL);
      builtIns.put("Natural/build", new Constructors.BuiltIn("Natural/build"));
      builtIns.put("Natural/even", new Constructors.BuiltIn("Natural/even"));
      builtIns.put("Natural/fold", new Constructors.BuiltIn("Natural/fold"));
      builtIns.put("Natural/isZero", new Constructors.BuiltIn("Natural/isZero"));
      builtIns.put("Natural/odd", new Constructors.BuiltIn("Natural/odd"));
      builtIns.put("Natural/show", new Constructors.BuiltIn("Natural/show"));
      builtIns.put("Natural/subtract", new Constructors.BuiltIn("Natural/subtract"));
      builtIns.put("Natural/toInteger", new Constructors.BuiltIn("Natural/toInteger"));
      builtIns.put("None", NONE);
      builtIns.put("Optional", OPTIONAL);
      builtIns.put("Some", SOME);
      builtIns.put("Sort", SORT);
      builtIns.put("Text", TEXT);
      builtIns.put("Text/replace", new Constructors.BuiltIn("Text/replace"));
      builtIns.put("Text/show", new Constructors.BuiltIn("Text/show"));
      builtIns.put("Time", TIME);
      builtIns.put("TimeZone", TIME_ZONE);
      builtIns.put("True", TRUE);
      builtIns.put("Type", TYPE);

      keywords.add("if");
      keywords.add("then");
      keywords.add("else");
      keywords.add("let");
      keywords.add("in");
      keywords.add("using");
      keywords.add("missing");
      keywords.add("assert");
      keywords.add("as");
      keywords.add("Infinity");
      keywords.add("NaN");
      keywords.add("merge");
      keywords.add("Some");
      keywords.add("toMap");
      keywords.add("forall");
      keywords.add("with");
    }

    static Expr getBuiltIn(String name) {
      return builtIns.get(name);
    }

    public static boolean isBuiltIn(String name) {
      return builtIns.containsKey(name);
    }

    public static boolean isKeyword(String name) {
      return keywords.contains(name);
    }
  }

  /** Represents a Dhall expression that's been parsed and has associated source information. */
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

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onNote(base, this.source);
    }

    final <A> void advance(VisitState<A> state) {
      this.base.advance(state);
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

  public static final Expr makeDateLiteral(int year, int month, int day) {
    return new Constructors.DateLiteral(year, month, day);
  }

  public static final Expr makeTimeLiteral(
      int hour, int minute, int second, BigDecimal fractional) {
    return new Constructors.TimeLiteral(hour, minute, second, fractional);
  }

  public static final Expr makeTimeZoneLiteral(int seconds) {
    return new Constructors.TimeZoneLiteral(seconds);
  }

  public static final Expr makeTextLiteral(String[] parts, Expr[] interpolated) {
    return new Constructors.TextLiteral(parts, interpolated);
  }

  public static final Expr makeTextLiteral(String[] parts, Collection<Expr> interpolated) {
    return new Constructors.TextLiteral(parts, interpolated.toArray(new Expr[interpolated.size()]));
  }

  private static final Expr[] emptyExprArray = {};

  public static final Expr makeTextLiteral(String value) {
    String[] parts = {value};
    return new Constructors.TextLiteral(parts, emptyExprArray);
  }

  public static final Expr makeApplication(Expr base, Expr arg) {
    return new Constructors.Application(base, arg);
  }

  public static final Expr makeApplication(Expr base, Expr[] args) {
    Expr acc = base;
    for (int i = 0; i < args.length; i++) {
      acc = Expr.makeApplication(acc, args[i]);
    }
    return acc;
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

  public static final Expr makeBuiltIn(String name) {
    if (Constants.getBuiltIn(name) == null) {
      throw new IllegalArgumentException(name + " is not a built-in");
    }
    return Constants.getBuiltIn(name);
  }

  public static final Expr makeIdentifier(String name, long index) {
    return new Constructors.Identifier(name, index);
  }

  public static final Expr makeIdentifier(String name) {
    return makeIdentifier(name, 0);
  }

  public static final Expr makeRecordLiteral(Entry<String, Expr>[] fields) {
    return new Constructors.RecordLiteral(fields);
  }

  public static final Expr makeRecordLiteral(Collection<Entry<String, Expr>> fields) {
    return new Constructors.RecordLiteral(fields.toArray(new Entry[fields.size()]));
  }

  public static final Expr makeRecordLiteral(String key, Expr value) {
    return new Constructors.RecordLiteral(new Entry[] {new SimpleImmutableEntry<>(key, value)});
  }

  public static final Expr makeRecordType(Entry<String, Expr>[] fields) {
    return new Constructors.RecordType(fields);
  }

  public static final Expr makeRecordType(Collection<Entry<String, Expr>> fields) {
    return new Constructors.RecordType(fields.toArray(new Entry[fields.size()]));
  }

  public static final Expr makeUnionType(Entry<String, Expr>[] fields) {
    return new Constructors.UnionType(fields);
  }

  public static final Expr makeUnionType(Collection<Entry<String, Expr>> fields) {
    return new Constructors.UnionType(fields.toArray(new Entry[fields.size()]));
  }

  public static final Expr makeNonEmptyListLiteral(Expr[] values) {
    return new Constructors.NonEmptyListLiteral(values);
  }

  public static final Expr makeNonEmptyListLiteral(Collection<Expr> values) {
    return new Constructors.NonEmptyListLiteral(values.toArray(new Expr[values.size()]));
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

  public static final Expr makeLet(List<LetBinding<Expr>> bindings, Expr body) {
    Expr result = body;

    for (int i = bindings.size() - 1; i >= 0; i--) {
      LetBinding<Expr> binding = bindings.get(i);
      result =
          new Constructors.Let(binding.getName(), binding.getType(), binding.getValue(), result);
    }

    return result;
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

  public static final Expr makeWith(Expr base, String[] path, Expr value) {
    return new Constructors.With(base, path, value);
  }

  public static final Expr makeMerge(Expr left, Expr right, Expr type) {
    return new Constructors.Merge(left, right, type);
  }

  public static final Expr makeMerge(Expr left, Expr right) {
    return makeMerge(left, right, null);
  }

  public static final Expr makeLocalImport(Path path, ImportMode mode, byte[] hash) {
    return new Constructors.LocalImport(path, mode, hash);
  }

  public static final Expr makeClasspathImport(Path path, ImportMode mode, byte[] hash) {
    return new Constructors.ClasspathImport(path, mode, hash);
  }

  public static final Expr makeRemoteImport(URI url, Expr using, ImportMode mode, byte[] hash) {
    return new Constructors.RemoteImport(url, using, mode, hash);
  }

  public static final Expr makeEnvImport(String value, ImportMode mode, byte[] hash) {
    return new Constructors.EnvImport(value, mode, hash);
  }

  public static final Expr makeMissingImport(ImportMode mode, byte[] hash) {
    return new Constructors.MissingImport(mode, hash);
  }

  private final Entry<Expr, Expr> getFirstDiff(Expr other) {
    Deque<Expr> stackA = new ArrayDeque<Expr>();
    Deque<Expr> stackB = new ArrayDeque<Expr>();

    Expr currentA = this;
    Expr currentB = other;

    stackA.add(currentA);
    stackB.add(currentB);

    while (true) {
      currentA = stackA.poll();
      currentB = stackB.poll();

      if (currentA == null || currentB == null) {
        break;
      }

      currentA = currentA.getNonNote();
      currentB = currentB.getNonNote();

      if (currentA.tag != currentB.tag) {
        break;
      }

      if (currentA.tag == Tags.NATURAL) {
        if (((Constructors.NaturalLiteral) currentA)
            .value.equals(((Constructors.NaturalLiteral) currentB).value)) {
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.INTEGER) {
        if (((Constructors.IntegerLiteral) currentA)
            .value.equals(((Constructors.IntegerLiteral) currentB).value)) {
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.DOUBLE) {
        // We must compare double literals using the binary encoding.
        if (Arrays.equals(currentA.getEncodedBytes(), currentB.getEncodedBytes())) {
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.BUILT_IN) {
        Constructors.BuiltIn builtInA = (Constructors.BuiltIn) currentA;
        Constructors.BuiltIn builtInB = (Constructors.BuiltIn) currentB;
        if (builtInA.name.equals(builtInB.name)) {
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.IDENTIFIER) {
        Constructors.Identifier identifierA = (Constructors.Identifier) currentA;
        Constructors.Identifier identifierB = (Constructors.Identifier) currentB;
        if (identifierA.name.equals(identifierB.name) && identifierA.index == identifierB.index) {
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.LAMBDA) {
        Constructors.Lambda lambdaA = (Constructors.Lambda) currentA;
        Constructors.Lambda lambdaB = (Constructors.Lambda) currentB;
        if (lambdaA.name.equals(lambdaB.name)) {
          stackA.add(lambdaA.type);
          stackB.add(lambdaB.type);
          stackA.add(lambdaA.result);
          stackB.add(lambdaB.result);
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.PI) {
        Constructors.Pi piA = (Constructors.Pi) currentA;
        Constructors.Pi piB = (Constructors.Pi) currentB;
        if (piA.name.equals(piB.name) && !(piA.type == null ^ piB.type == null)) {
          if (piA.type != null) {
            stackA.add(piA.type);
            stackB.add(piB.type);
          }
          stackA.add(piA.result);
          stackB.add(piB.result);
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.LET) {
        Constructors.Let letA = (Constructors.Let) currentA;
        Constructors.Let letB = (Constructors.Let) currentB;
        if (letA.name.equals(letB.name) && !(letA.type == null ^ letB.type == null)) {
          if (letA.type != null) {
            stackA.add(letA.type);
            stackB.add(letB.type);
          }
          stackA.add(letA.value);
          stackB.add(letB.value);
          stackA.add(letA.body);
          stackB.add(letB.body);
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.TEXT) {
        Constructors.TextLiteral textA = (Constructors.TextLiteral) currentA;
        Constructors.TextLiteral textB = (Constructors.TextLiteral) currentB;
        if (Arrays.equals(textA.interpolated, textB.interpolated)) {
          for (Expr interpolation : textA.interpolated) {
            stackA.add(interpolation);
          }

          for (Expr interpolation : textB.interpolated) {
            stackB.add(interpolation);
          }
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.NON_EMPTY_LIST) {
        Constructors.NonEmptyListLiteral nonEmptyListA =
            (Constructors.NonEmptyListLiteral) currentA;
        Constructors.NonEmptyListLiteral nonEmptyListB =
            (Constructors.NonEmptyListLiteral) currentB;
        for (Expr value : nonEmptyListA.values) {
          stackA.add(value);
        }

        for (Expr value : nonEmptyListB.values) {
          stackB.add(value);
        }
        continue;
      } else if (currentA.tag == Tags.EMPTY_LIST) {
        Constructors.EmptyListLiteral emptyListA = (Constructors.EmptyListLiteral) currentA;
        Constructors.EmptyListLiteral emptyListB = (Constructors.EmptyListLiteral) currentB;
        stackA.add(emptyListA.type);
        stackB.add(emptyListB.type);
        continue;
      } else if (currentA.tag == Tags.RECORD) {
        Constructors.RecordLiteral recordA = (Constructors.RecordLiteral) currentA;
        Constructors.RecordLiteral recordB = (Constructors.RecordLiteral) currentB;
        Entry<String, Expr>[] fieldsA = recordA.fields;
        Entry<String, Expr>[] fieldsB = recordB.fields;

        if (fieldsA.length == fieldsB.length) {
          for (int i = 0; i < fieldsA.length; i++) {
            if (!fieldsA[i].getKey().equals(fieldsB[i].getKey())) {
              return new SimpleImmutableEntry<>(fieldsA[i].getValue(), fieldsB[i].getValue());
            }

            stackA.add(fieldsA[i].getValue());
            stackB.add(fieldsB[i].getValue());
          }
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.RECORD_TYPE) {
        Constructors.RecordType recordTypeA = (Constructors.RecordType) currentA;
        Constructors.RecordType recordTypeB = (Constructors.RecordType) currentB;
        Entry<String, Expr>[] fieldsA = recordTypeA.fields;
        Entry<String, Expr>[] fieldsB = recordTypeB.fields;

        if (fieldsA.length == fieldsB.length) {
          for (int i = 0; i < fieldsA.length; i++) {
            if (!fieldsA[i].getKey().equals(fieldsB[i].getKey())) {
              return new SimpleImmutableEntry<>(fieldsA[i].getValue(), fieldsB[i].getValue());
            }

            stackA.add(fieldsA[i].getValue());
            stackB.add(fieldsB[i].getValue());
          }
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.UNION_TYPE) {
        Constructors.UnionType unionTypeA = (Constructors.UnionType) currentA;
        Constructors.UnionType unionTypeB = (Constructors.UnionType) currentB;
        Entry<String, Expr>[] fieldsA = unionTypeA.fields;
        Entry<String, Expr>[] fieldsB = unionTypeB.fields;

        if (fieldsA.length == fieldsB.length) {
          for (int i = 0; i < fieldsA.length; i++) {
            if (!fieldsA[i].getKey().equals(fieldsB[i].getKey())) {
              return new SimpleImmutableEntry<>(fieldsA[i].getValue(), fieldsB[i].getValue());
            }

            if (fieldsA[i].getValue() != null && fieldsB[i].getValue() != null) {
              stackA.add(fieldsA[i].getValue());
              stackB.add(fieldsB[i].getValue());
            } else if (fieldsA[i].getValue() == null ^ fieldsB[i].getValue() == null) {
              return new SimpleImmutableEntry<>(currentA, currentB);
            }
          }
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.FIELD_ACCESS) {
        Constructors.FieldAccess fieldAccessA = (Constructors.FieldAccess) currentA;
        Constructors.FieldAccess fieldAccessB = (Constructors.FieldAccess) currentB;

        if (fieldAccessA.fieldName.equals(fieldAccessB.fieldName)) {
          stackA.add(fieldAccessA.base);
          stackB.add(fieldAccessB.base);
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.PROJECTION) {
        Constructors.Projection projectionA = (Constructors.Projection) currentA;
        Constructors.Projection projectionB = (Constructors.Projection) currentB;

        if (Arrays.equals(projectionA.fieldNames, projectionB.fieldNames)) {
          stackA.add(projectionA.base);
          stackB.add(projectionB.base);
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.PROJECTION_BY_TYPE) {
        Constructors.ProjectionByType projectionByTypeA = (Constructors.ProjectionByType) currentA;
        Constructors.ProjectionByType projectionByTypeB = (Constructors.ProjectionByType) currentB;

        stackA.add(projectionByTypeA.base);
        stackB.add(projectionByTypeB.base);
        stackA.add(projectionByTypeA.type);
        stackB.add(projectionByTypeB.type);
        continue;
      } else if (currentA.tag == Tags.APPLICATION) {
        Constructors.Application applicationA = (Constructors.Application) currentA;
        Constructors.Application applicationB = (Constructors.Application) currentB;

        stackA.add(applicationA.base);
        stackB.add(applicationB.base);
        stackA.add(applicationA.arg);
        stackB.add(applicationB.arg);
        continue;
      } else if (currentA.tag == Tags.OPERATOR_APPLICATION) {
        Constructors.OperatorApplication operatorApplicationA =
            (Constructors.OperatorApplication) currentA;
        Constructors.OperatorApplication operatorApplicationB =
            (Constructors.OperatorApplication) currentB;

        if (operatorApplicationA.operator.equals(operatorApplicationB.operator)) {
          stackA.add(operatorApplicationA.lhs);
          stackB.add(operatorApplicationB.lhs);
          stackA.add(operatorApplicationA.rhs);
          stackB.add(operatorApplicationB.rhs);
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.IF) {
        Constructors.If ifA = (Constructors.If) currentA;
        Constructors.If ifB = (Constructors.If) currentB;

        stackA.add(ifA.predicate);
        stackB.add(ifB.predicate);
        stackA.add(ifA.thenValue);
        stackB.add(ifB.thenValue);
        stackA.add(ifA.elseValue);
        stackB.add(ifB.elseValue);
        continue;
      } else if (currentA.tag == Tags.ANNOTATED) {
        Constructors.Annotated annotatedA = (Constructors.Annotated) currentA;
        Constructors.Annotated annotatedB = (Constructors.Annotated) currentB;

        stackA.add(annotatedA.base);
        stackB.add(annotatedB.base);
        stackA.add(annotatedA.type);
        stackB.add(annotatedB.type);
        continue;
      } else if (currentA.tag == Tags.ASSERT) {
        Constructors.Assert assertA = (Constructors.Assert) currentA;
        Constructors.Assert assertB = (Constructors.Assert) currentB;

        stackA.add(assertA.base);
        stackB.add(assertB.base);
        continue;
      } else if (currentA.tag == Tags.MERGE) {
        Constructors.Merge mergeA = (Constructors.Merge) currentA;
        Constructors.Merge mergeB = (Constructors.Merge) currentB;
        if (!(mergeA.type == null ^ mergeB.type == null)) {
          stackA.add(mergeA.handlers);
          stackB.add(mergeB.handlers);
          stackA.add(mergeA.union);
          stackB.add(mergeB.union);
          if (mergeA.type != null) {
            stackA.add(mergeA.type);
            stackB.add(mergeB.type);
          }
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.TO_MAP) {
        Constructors.ToMap toMapA = (Constructors.ToMap) currentA;
        Constructors.ToMap toMapB = (Constructors.ToMap) currentB;
        if (!(toMapA.type == null ^ toMapB.type == null)) {
          stackA.add(toMapA.base);
          stackB.add(toMapB.base);
          if (toMapA.type != null) {
            stackA.add(toMapA.type);
            stackB.add(toMapB.type);
          }
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.MISSING_IMPORT) {
        Constructors.MissingImport missingImportA = (Constructors.MissingImport) currentA;
        Constructors.MissingImport missingImportB = (Constructors.MissingImport) currentB;
        if (missingImportA.mode.equals(missingImportB.mode)
            && Arrays.equals(missingImportA.hash, missingImportB.hash)) {
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.ENV_IMPORT) {
        Constructors.EnvImport envImportA = (Constructors.EnvImport) currentA;
        Constructors.EnvImport envImportB = (Constructors.EnvImport) currentB;
        if (envImportA.name.equals(envImportB.name)
            && envImportA.mode.equals(envImportB.mode)
            && Arrays.equals(envImportA.hash, envImportB.hash)) {
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.LOCAL_IMPORT) {
        Constructors.LocalImport localImportA = (Constructors.LocalImport) currentA;
        Constructors.LocalImport localImportB = (Constructors.LocalImport) currentB;
        if (localImportA.path.equals(localImportB.path)
            && localImportA.mode.equals(localImportB.mode)
            && Arrays.equals(localImportA.hash, localImportB.hash)) {
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.CLASSPATH_IMPORT) {
        Constructors.ClasspathImport classpathImportA = (Constructors.ClasspathImport) currentA;
        Constructors.ClasspathImport classpathImportB = (Constructors.ClasspathImport) currentB;
        if (classpathImportA.path.equals(classpathImportB.path)
            && classpathImportA.mode.equals(classpathImportB.mode)
            && Arrays.equals(classpathImportA.hash, classpathImportB.hash)) {
          continue;
        } else {
          break;
        }
      } else if (currentA.tag == Tags.REMOTE_IMPORT) {
        Constructors.RemoteImport remoteImportA = (Constructors.RemoteImport) currentA;
        Constructors.RemoteImport remoteImportB = (Constructors.RemoteImport) currentB;
        if (remoteImportA.url.equals(remoteImportB.url)
            && remoteImportA.mode.equals(remoteImportB.mode)
            && Arrays.equals(remoteImportA.hash, remoteImportB.hash)) {
          continue;
        } else {
          break;
        }
      }
    }

    if (currentA == null && currentB == null) {
      return null;
    } else {
      return new SimpleImmutableEntry<>(currentA, currentB);
    }
  }
}
