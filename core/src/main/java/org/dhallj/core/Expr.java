package org.dhallj.core;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.security.MessageDigest;
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
    return this.acceptVis(new AlphaNormalize());
  }

  public final Expr normalize() {
    return this.acceptVis(BetaNormalize.instance);
  }

  public final Expr typeCheck() {
    return this.acceptExternal(new TypeCheck());
  }

  public final void encode(OutputStream stream) throws IOException {
    this.acceptVis(Encode.instance).writeToStream(stream);
  }

  public final byte[] encodeToByteArray() {
    try {
      return this.acceptVis(Encode.instance).getBytes();
    } catch (IOException e) {
      return null;
    }
  }

  public final byte[] hashBytes() {
    byte[] result = this.cachedHashBytes.get();

    if (result == null) {
      byte[] valueEncodedBytes = this.encodeToByteArray();

      MessageDigest digest = null;
      try {
        digest = java.security.MessageDigest.getInstance("SHA-256");
      } catch (java.security.NoSuchAlgorithmException e) {

      }
      result = digest.digest(valueEncodedBytes);
      if (!this.cachedHashBytes.compareAndSet(null, result)) {
        return this.cachedHashBytes.get();
      }
    }
    return result;
  }

  public final String hash() {
    return Util.encodeBytes(this.hashBytes());
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
    String asBuiltIn = this.asBuiltIn();

    if (asBuiltIn != null) {
      if (asBuiltIn.equals("True")) {
        return true;
      } else if (asBuiltIn.equals("False")) {
        return false;
      }
    }
    return null;
  }

  public final BigInteger asNaturalLiteral() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.NATURAL) {
      return ((Constructors.NaturalLiteral) value).value;
    } else {
      return null;
    }
  }

  public final BigInteger asIntegerLiteral() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.INTEGER) {
      return ((Constructors.IntegerLiteral) value).value;
    } else {
      return null;
    }
  }

  public final Double asDoubleLiteral() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.DOUBLE) {
      return ((Constructors.DoubleLiteral) value).value;
    } else {
      return null;
    }
  }

  public final String asSimpleTextLiteral() {
    Expr value = this.getNonNote();

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

  public final String asBuiltIn() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.BUILT_IN) {
      return ((Constructors.BuiltIn) value).name;
    } else {
      return null;
    }
  }

  public final List<Expr> asListLiteral() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.NON_EMPTY_LIST) {
      return Arrays.asList(((Constructors.NonEmptyListLiteral) value).values);
    } else if (value.tag == Tags.EMPTY_LIST) {
      return new ArrayList<Expr>(0);
    } else {
      return null;
    }
  }

  public final Iterable<Entry<String, Expr>> asRecordLiteral() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.RECORD) {
      return Arrays.asList(((Constructors.RecordLiteral) value).fields);
    } else {
      return null;
    }
  }

  public final Iterable<Entry<String, Expr>> asRecordType() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.RECORD_TYPE) {
      return Arrays.asList(((Constructors.RecordType) value).fields);
    } else {
      return null;
    }
  }

  public final List<Entry<String, Expr>> asUnionType() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.UNION_TYPE) {
      return Arrays.asList(((Constructors.UnionType) value).fields);
    } else {
      return null;
    }
  }

  public final Entry<Expr, String> asFieldAccess() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.FIELD_ACCESS) {
      Constructors.FieldAccess fieldAccess = (Constructors.FieldAccess) value;
      return new SimpleImmutableEntry(fieldAccess.base, fieldAccess.fieldName);
    } else {
      return null;
    }
  }

  public final long asUnderscore() {
    Expr value = this.getNonNote();

    if (value.tag == Tags.IDENTIFIER) {
      Constructors.Identifier identifier = (Constructors.Identifier) value;
      if (identifier.name.equals("_")) {
        return identifier.index;
      }
    }
    return -1;
  }

  public final boolean isResolved() {
    return this.acceptVis(IsResolved.instance);
  }

  public final boolean sameStructure(Expr other) {
    return firstDiff(other) == null;
  }

  public final boolean equivalent(Expr other) {
    return Arrays.equals(
        this.normalize().alphaNormalize().hashBytes(),
        other.normalize().alphaNormalize().hashBytes());
  }

  public final boolean equals(Object obj) {
    if (obj instanceof Expr) {
      return this.equivalent((Expr) obj);
    } else {
      return false;
    }
  }

  public final int hashCode() {
    return Arrays.hashCode(this.normalize().alphaNormalize().hashBytes());
  }

  public final String show() {
    return this.acceptVis(ToStringVisitor.instance).toString();
  }

  public final String toString() {
    return this.show();
  }

  private Expr getNonNote() {
    Expr current = this;

    while (current.tag == Tags.NOTE) {
      current = ((Parsed) current).base;
    }

    return current;
  }

  public static final class Sugar {
    public static final Expr desugarComplete(Expr lhs, Expr rhs) {

      return Expr.makeAnnotated(
          Expr.makeOperatorApplication(Operator.PREFER, Expr.makeFieldAccess(lhs, "default"), rhs),
          Expr.makeFieldAccess(lhs, "Type"));
    }
  }

  public static final class Util {
    public static String encodeBytes(byte[] hash) {
      StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    }

    public static Expr getListArg(Expr expr) {
      return getElementType(expr, "List");
    }

    public static Expr getOptionalArg(Expr expr) {
      return getElementType(expr, "Optional");
    }

    public static Expr getSomeArg(Expr expr) {
      return getElementType(expr, "Some");
    }

    public static Expr getNoneArg(Expr expr) {
      return getElementType(expr, "None");
    }

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
  }

  public static final class Constants {
    private static List<Entry<String, Expr>> emptyFields = new ArrayList(0);

    public static Expr UNDERSCORE = makeIdentifier("_");
    public static Expr SORT = new Constructors.BuiltIn("Sort");
    public static Expr KIND = new Constructors.BuiltIn("Kind");
    public static Expr TYPE = new Constructors.BuiltIn("Type");
    public static Expr BOOL = new Constructors.BuiltIn("Bool");
    public static Expr TRUE = new Constructors.BuiltIn("True");
    public static Expr FALSE = new Constructors.BuiltIn("False");
    public static Expr LIST = new Constructors.BuiltIn("List");
    public static Expr OPTIONAL = new Constructors.BuiltIn("Optional");
    public static Expr DOUBLE = new Constructors.BuiltIn("Double");
    public static Expr NATURAL = new Constructors.BuiltIn("Natural");
    public static Expr INTEGER = new Constructors.BuiltIn("Integer");
    public static Expr TEXT = new Constructors.BuiltIn("Text");
    public static Expr NONE = new Constructors.BuiltIn("None");
    public static Expr SOME = new Constructors.BuiltIn("Some");
    public static Expr NATURAL_FOLD = new Constructors.BuiltIn("Natural/fold");
    public static Expr LIST_FOLD = new Constructors.BuiltIn("List/fold");
    public static Expr ZERO = makeNaturalLiteral(BigInteger.ZERO);
    public static Expr EMPTY_RECORD_LITERAL = makeRecordLiteral(emptyFields);
    public static Expr EMPTY_RECORD_TYPE = makeRecordType(emptyFields);
    public static Expr EMPTY_UNION_TYPE = makeUnionType(emptyFields);
    public static Expr LOCATION_TYPE =
        makeUnionType(
            new HashMap<String, Expr>() {
              {
                put("Local", TEXT);
                put("Remote", TEXT);
                put("Environment", TEXT);
                put("Missing", null);
              }
            }.entrySet());
    public static String MAP_KEY_FIELD_NAME = "mapKey";
    public static String MAP_VALUE_FIELD_NAME = "mapValue";

    private static Map<String, Expr> builtIns = new HashMap<String, Expr>(36);
    private static Set<String> keywords = new HashSet<String>(16);

    static {
      builtIns.put("Bool", BOOL);
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
      builtIns.put("Optional/build", new Constructors.BuiltIn("Optional/build"));
      builtIns.put("Optional/fold", new Constructors.BuiltIn("Optional/fold"));
      builtIns.put("Some", SOME);
      builtIns.put("Sort", SORT);
      builtIns.put("Text", TEXT);
      builtIns.put("Text/show", new Constructors.BuiltIn("Text/show"));
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

  public static final Expr makeTextLiteral(String value) {
    String[] parts = {value};
    return new Constructors.TextLiteral(parts, ExprUtilities.exprsToArray(new ArrayList(0)));
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
      throw new RuntimeException("Bad name" + name);
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

  public static final Expr makeRecordLiteral(Iterable<Entry<String, Expr>> fields) {
    return new Constructors.RecordLiteral(ExprUtilities.entriesToArray(fields));
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

  public static final Expr makeUnionType(Entry<String, Expr>[] fields) {
    return new Constructors.UnionType(fields);
  }

  public static final Expr makeUnionType(Iterable<Entry<String, Expr>> fields) {
    return new Constructors.UnionType(ExprUtilities.entriesToArray(fields));
  }

  public static final Expr makeNonEmptyListLiteral(Expr[] values) {
    return new Constructors.NonEmptyListLiteral(values);
  }

  public static final Expr makeNonEmptyListLiteral(Iterable<Expr> values) {
    return new Constructors.NonEmptyListLiteral(ExprUtilities.exprsToArray(values));
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
    int size;

    State(Expr expr, int state, int size) {
      this.expr = expr;
      this.state = state;
      this.size = size;
    }

    State(Expr expr, int state) {
      this(expr, state, 0);
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
    LinkedList<LinkedList<LetBinding<Expr>>> letBindingsStack =
        new LinkedList<LinkedList<LetBinding<Expr>>>();
    LinkedList<List<String>> letBindingNamesStack = new LinkedList<List<String>>();

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
        case Tags.BUILT_IN:
          Constructors.BuiltIn tmpBuiltIn = (Constructors.BuiltIn) current.expr;
          values.push(vis.onBuiltIn(tmpBuiltIn.name));
          break;
        case Tags.IDENTIFIER:
          Constructors.Identifier tmpIdentifier = (Constructors.Identifier) current.expr;
          values.push(vis.onIdentifier(tmpIdentifier.name, tmpIdentifier.index));
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
              stack.push(new State(tmpPi.type, 0));
              break;
            case 1:
              vis.bind(tmpPi.name, tmpPi.type);
              current.state = 2;
              stack.push(current);
              stack.push(new State(tmpPi.result, 0));
              break;
            case 2:
              v1 = values.poll();
              v0 = values.poll();
              values.push(vis.onPi(tmpPi.name, v0, v1));
          }
          break;
        case Tags.LET:
          Constructors.Let tmpLet = (Constructors.Let) current.expr;

          LinkedList<LetBinding<Expr>> letBindings;

          if (current.state == 0) {
            letBindings = new LinkedList<LetBinding<Expr>>();
            letBindings.push(new LetBinding(tmpLet.name, tmpLet.type, tmpLet.value));

            gatherLetBindings(tmpLet.body, letBindings);

            current.state = 1;
            current.size = letBindings.size();

            List<String> letBindingNames = new ArrayList(current.size);

            for (LetBinding<Expr> letBinding : letBindings) {
              letBindingNames.add(letBinding.getName());
            }

            letBindingNamesStack.push(letBindingNames);
          } else {
            letBindings = letBindingsStack.poll();
          }

          if (letBindings.isEmpty()) {
            if (current.state == 1) {
              current.state = 3;
              stack.push(current);
              stack.push(new State(gatherLetBindings(tmpLet.body, null), 0));
              letBindingsStack.push(letBindings);
            } else {
              List<String> letBindingNames = letBindingNamesStack.poll();
              List<LetBinding<A>> valueBindings = new ArrayList<LetBinding<A>>(current.size);

              A body = values.poll();

              for (int i = 0; i < current.size; i++) {
                v1 = values.poll();
                v0 = values.poll();

                valueBindings.add(
                    new LetBinding(letBindingNames.get(current.size - i - 1), v0, v1));
              }

              values.push(vis.onLet(valueBindings, body));
            }
          } else {
            LetBinding<Expr> letBinding = letBindings.poll();

            switch (current.state) {
              case 1:
                current.state = 2;
                if (letBinding.hasType()) {
                  stack.push(current);
                  stack.push(new State(letBinding.getType(), 0));
                  letBindings.push(letBinding);
                  letBindingsStack.push(letBindings);
                  break;
                } else {
                  values.push(null);
                }
              case 2:
                current.state = 1;
                vis.bind(letBinding.getName(), letBinding.getType());
                stack.push(current);
                stack.push(new State(letBinding.getValue(), 0));
                letBindingsStack.push(letBindings);
                break;
            }
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
            values.push(vis.onEmptyList(tmpEmptyList.type, values.poll()));
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

            Expr base = gatherApplicationArgs(tmpApplication.base, application);

            application.push(base);

            vis.preApplication(application.size());
            current.state = 1;
            current.size = application.size();
          } else {
            application = applicationStack.poll();
          }

          if (application.isEmpty()) {
            List<A> args = new ArrayList<A>(current.size);
            for (int i = 0; i < current.size - 1; i++) {
              args.add(values.poll());
            }
            Collections.reverse(args);

            A base = values.poll();

            values.push(
                vis.onApplication(gatherApplicationArgs(tmpApplication.base, null), base, args));
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

              if (tmpToMap.type != null) {
                stack.push(current);
                stack.push(new State(tmpToMap.type, 0));
                break;
              } else {
                values.push(null);
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

          values.push(vis.onEnvImport(tmpEnvImport.name, tmpEnvImport.mode, tmpEnvImport.hash));
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

              if (tmpRemoteImport.using != null) {
                stack.push(current);
                stack.push(new State(tmpRemoteImport.using, 0));
                break;
              } else {
                values.push(null);
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

  private static final Expr gatherApplicationArgs(Expr candidate, LinkedList<Expr> args) {
    Expr current = candidate.getNonNote();

    while (current.tag == Tags.APPLICATION) {
      Constructors.Application currentApplication = (Constructors.Application) current;

      if (args != null) {
        args.push(currentApplication.arg);
      }
      current = currentApplication.base.getNonNote();
    }

    return current;
  }

  private static final Expr gatherLetBindings(Expr candidate, LinkedList<LetBinding<Expr>> args) {
    Expr current = candidate.getNonNote();

    while (current.tag == Tags.LET) {
      Constructors.Let currentLet = (Constructors.Let) current;

      if (args != null) {
        args.push(new LetBinding(currentLet.name, currentLet.type, currentLet.value));
      }
      current = currentLet.body.getNonNote();
    }

    return current;
  }

  public final Entry<Expr, Expr> firstDiff(Expr other) {
    LinkedList<Expr> stackA = new LinkedList<Expr>();
    LinkedList<Expr> stackB = new LinkedList<Expr>();

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
        if (Arrays.equals(currentA.encodeToByteArray(), currentB.encodeToByteArray())) {
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
              return new SimpleImmutableEntry<Expr, Expr>(
                  fieldsA[i].getValue(), fieldsB[i].getValue());
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
              return new SimpleImmutableEntry<Expr, Expr>(
                  fieldsA[i].getValue(), fieldsB[i].getValue());
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
              return new SimpleImmutableEntry<Expr, Expr>(
                  fieldsA[i].getValue(), fieldsB[i].getValue());
            }

            if (fieldsA[i].getValue() == null ^ fieldsB[i].getValue() == null) {
              return new SimpleImmutableEntry<Expr, Expr>(currentA, currentB);
            }
            stackA.add(fieldsA[i].getValue());
            stackB.add(fieldsB[i].getValue());
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
      return new SimpleImmutableEntry<Expr, Expr>(currentA, currentB);
    }
  }
}
