package org.dhallj.core;

import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

final class ToStringState {
  static final int BASE = 0;
  static final int APPLICATION = 1;
  static final int FIELD_ACCESS = 99;
  static final int ASSERT = 100;
  static final int MERGE = 102;
  static final int TO_MAP = 102;
  static final int APPLICATION_ARGUMENT = 102;
  static final int ANNOTATED = 104;
  static final int LAMBDA = 105;
  static final int PI = 105;
  static final int LET = 105;
  static final int IF = 106;
  static final int NONE = Integer.MAX_VALUE;

  private final String text;
  private final int looseness;

  ToStringState(String text, int looseness) {
    this.text = text;
    this.looseness = looseness;
  }

  ToStringState(String text) {
    this(text, BASE);
  }

  ToStringState withText(String text) {
    return new ToStringState(text, this.looseness);
  }

  String toString(int contextLooseness, boolean parenthesizeIfSame) {
    if (contextLooseness > this.looseness
        || (!parenthesizeIfSame && contextLooseness == this.looseness)) {
      return this.text;
    } else {
      return String.format("(%s)", this.text);
    }
  }

  String toString(int contextLooseness) {
    return this.toString(contextLooseness, false);
  }

  public String toString() {
    return this.toString(NONE);
  }

  private static final int baseOperatorLooseness = 4;

  static final int getOperatorLooseness(Operator operator) {
    int level = baseOperatorLooseness;

    switch (operator) {
      case COMPLETE:
        level += 1;
        ;
      case IMPORT_ALT:
        level += 1;
      case OR:
        level += 1;
      case PLUS:
        level += 1;
      case TEXT_APPEND:
        level += 1;
      case LIST_APPEND:
        level += 1;
      case AND:
        level += 1;
      case COMBINE:
        level += 1;
      case PREFER:
        level += 1;
      case COMBINE_TYPES:
        level += 1;
      case TIMES:
        level += 1;
      case EQUALS:
        level += 1;
      case NOT_EQUALS:
        level += 1;
      case EQUIVALENT:
        level += 1;
      default:
    }

    return level;
  }
}

final class ToStringVisitor extends Visitor.NoPrepareEvents<ToStringState> {
  public static Visitor<ToStringState> instance = new ToStringVisitor();

  public void bind(String name, Expr type) {}

  public ToStringState onNote(ToStringState base, Source source) {
    return base;
  }

  public ToStringState onNatural(Expr self, BigInteger value) {
    return new ToStringState(value.toString());
  }

  public ToStringState onInteger(Expr self, BigInteger value) {
    String withSign =
        (value.compareTo(BigInteger.ZERO) >= 0)
            ? String.format("+%s", value.toString())
            : value.toString();
    return new ToStringState(withSign);
  }

  public ToStringState onDouble(Expr self, double value) {
    return new ToStringState(Double.toString(value));
  }

  public ToStringState onBuiltIn(Expr self, String name) {
    return new ToStringState(name);
  }

  private static boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  private static boolean isDigit(char c) {
    return (c >= '0' && c <= '9');
  }

  private static boolean isSimpleLabel(String name) {
    char c = name.charAt(0);
    if (!isAlpha(c) && c != '_') {
      return false;
    }

    for (int i = 1; i < name.length(); i++) {
      c = name.charAt(i);
      if (!isAlpha(c) && !isDigit(c) && c != '-' && c != '/' && c != '_') {
        return false;
      }
    }

    return true;
  }

  private static String escapeName(String name) {
    if (!isSimpleLabel(name) || Expr.Constants.isBuiltIn(name) || Expr.Constants.isKeyword(name)) {
      return String.format("`%s`", name);
    } else {
      return name;
    }
  }

  public ToStringState onIdentifier(Expr self, String name, long index) {
    String maybeEscaped = escapeName(name);
    return new ToStringState(
        (index == 0) ? maybeEscaped : String.format("%s@%d", maybeEscaped, index));
  }

  public ToStringState onLambda(String name, ToStringState type, ToStringState result) {
    return new ToStringState(
        String.format(
            "λ(%s : %s) → %s",
            escapeName(name),
            type.toString(ToStringState.LAMBDA, true),
            result.toString(ToStringState.LAMBDA)),
        ToStringState.LAMBDA);
  }

  public ToStringState onPi(String name, ToStringState type, ToStringState result) {
    String typeString = type.toString(ToStringState.PI, true);
    String resultString = result.toString(ToStringState.PI);

    return new ToStringState(
        name.equals("_")
            ? String.format("%s → %s", typeString, resultString)
            : String.format("∀(%s : %s) → %s", escapeName(name), typeString, resultString),
        ToStringState.PI);
  }

  public ToStringState onLet(
      String name, ToStringState type, ToStringState value, ToStringState body) {
    String typeString =
        (type == null) ? "" : String.format(" : %s", type.toString(ToStringState.LET));
    return new ToStringState(
        String.format(
            "let %s%s = %s in %s",
            escapeName(name),
            typeString,
            value.toString(ToStringState.LET),
            body.toString(ToStringState.LET)),
        ToStringState.LET);
  }

  public ToStringState onLet(List<Expr.LetBinding<ToStringState>> bindings, ToStringState body) {
    String result = body.toString(ToStringState.LET);

    for (int i = bindings.size() - 1; i >= 0; i--) {
      Expr.LetBinding<ToStringState> binding = bindings.get(i);

      String typeString =
          binding.hasType()
              ? String.format(" : %s", binding.getType().toString(ToStringState.LET))
              : "";

      result =
          String.format(
              "let %s%s = %s in %s",
              escapeName(binding.getName()),
              typeString,
              binding.getValue().toString(ToStringState.LET),
              result);
    }

    return new ToStringState(result, ToStringState.LET);
  }

  public ToStringState onText(String[] parts, List<ToStringState> interpolated) {

    StringBuilder builder = new StringBuilder("\"");
    builder.append(Expr.Util.escapeText(parts[0], false));
    int i = 1;
    Iterator<ToStringState> it = interpolated.iterator();

    while (it.hasNext()) {
      builder.append("${");
      builder.append(it.next().toString(ToStringState.NONE));
      builder.append("}");
      builder.append(Expr.Util.escapeText(parts[i++], false));
    }
    if (i < parts.length) {
      builder.append(Expr.Util.escapeText(parts[i], false));
    }
    builder.append("\"");
    return new ToStringState(builder.toString());
  }

  public ToStringState onNonEmptyList(List<ToStringState> values) {
    StringBuilder builder = new StringBuilder("[");
    Iterator<ToStringState> it = values.iterator();
    while (it.hasNext()) {
      builder.append(it.next().toString(ToStringState.NONE));
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append("]");

    return new ToStringState(builder.toString());
  }

  public ToStringState onEmptyList(ToStringState type) {
    return new ToStringState(String.format("[] : %s", type), ToStringState.ANNOTATED);
  }

  public ToStringState onRecord(List<Entry<String, ToStringState>> fields) {
    if (fields.isEmpty()) {
      return new ToStringState("{=}");
    } else {
      StringBuilder builder = new StringBuilder("{");
      Iterator<Entry<String, ToStringState>> it = fields.iterator();
      while (it.hasNext()) {
        Entry<String, ToStringState> entry = it.next();
        builder.append(escapeName(entry.getKey()));
        builder.append(" = ");
        builder.append(entry.getValue().toString(ToStringState.NONE));
        if (it.hasNext()) {
          builder.append(", ");
        }
      }
      builder.append("}");

      return new ToStringState(builder.toString());
    }
  }

  public ToStringState onRecordType(List<Entry<String, ToStringState>> fields) {
    StringBuilder builder = new StringBuilder("{");
    Iterator<Entry<String, ToStringState>> it = fields.iterator();
    while (it.hasNext()) {
      Entry<String, ToStringState> entry = it.next();
      builder.append(escapeName(entry.getKey()));
      builder.append(" : ");
      builder.append(entry.getValue().toString(ToStringState.NONE));
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append("}");

    return new ToStringState(builder.toString());
  }

  public ToStringState onUnionType(List<Entry<String, ToStringState>> fields) {
    StringBuilder builder = new StringBuilder("<");
    Iterator<Entry<String, ToStringState>> it = fields.iterator();
    while (it.hasNext()) {
      Entry<String, ToStringState> entry = it.next();
      builder.append(escapeName(entry.getKey()));
      ToStringState type = entry.getValue();
      if (type != null) {
        builder.append(" : ");
        builder.append(type.toString());
      }
      if (it.hasNext()) {
        builder.append(" | ");
      }
    }
    builder.append(">");

    return new ToStringState(builder.toString());
  }

  public ToStringState onFieldAccess(ToStringState base, String fieldName) {
    return new ToStringState(
        String.format("%s.%s", base.toString(ToStringState.FIELD_ACCESS), fieldName),
        ToStringState.FIELD_ACCESS);
  }

  public ToStringState onProjection(ToStringState base, String[] fieldNames) {
    StringBuilder builder = new StringBuilder(base.toString(ToStringState.FIELD_ACCESS));
    builder.append(".{");
    for (int i = 0; i < fieldNames.length; i += 1) {
      builder.append(fieldNames[i]);
      if (i < fieldNames.length - 1) {
        builder.append(", ");
      }
    }
    builder.append("}");

    return new ToStringState(builder.toString(), ToStringState.FIELD_ACCESS);
  }

  public ToStringState onProjectionByType(ToStringState base, ToStringState type) {
    return new ToStringState(
        String.format(
            "%s.(%s)",
            base.toString(ToStringState.FIELD_ACCESS), type.toString(ToStringState.NONE)),
        ToStringState.FIELD_ACCESS);
  }

  public ToStringState onApplication(ToStringState base, List<ToStringState> args) {
    StringBuilder builder = new StringBuilder(base.toString(ToStringState.APPLICATION));
    builder.append(" ");

    for (int i = 0; i < args.size(); i += 1) {
      builder.append(args.get(i).toString(ToStringState.APPLICATION, true));
      if (i < args.size() - 1) {
        builder.append(" ");
      }
    }

    return new ToStringState(builder.toString(), ToStringState.APPLICATION);
  }

  public ToStringState onOperatorApplication(
      Operator operator, ToStringState lhs, ToStringState rhs) {
    int operatorLooseness = ToStringState.getOperatorLooseness(operator);

    return new ToStringState(
        String.format(
            "%s %s %s",
            lhs.toString(operatorLooseness), operator, rhs.toString(operatorLooseness, true)),
        operatorLooseness);
  }

  public ToStringState onIf(
      ToStringState predicate, ToStringState thenValue, ToStringState elseValue) {
    return new ToStringState(
        String.format(
            "if %s then %s else %s",
            predicate.toString(ToStringState.IF),
            thenValue.toString(ToStringState.IF),
            elseValue.toString(ToStringState.IF)),
        ToStringState.IF);
  }

  public ToStringState onAnnotated(ToStringState base, ToStringState type) {
    return new ToStringState(
        String.format(
            "%s : %s",
            base.toString(ToStringState.ANNOTATED), type.toString(ToStringState.ANNOTATED)),
        ToStringState.ANNOTATED);
  }

  public ToStringState onAssert(ToStringState base) {
    return new ToStringState(
        String.format("assert : %s", base.toString(ToStringState.ASSERT)), ToStringState.ASSERT);
  }

  public ToStringState onMerge(ToStringState handlers, ToStringState union, ToStringState type) {
    StringBuilder builder = new StringBuilder("merge ");

    builder.append(handlers.toString(ToStringState.MERGE));
    builder.append(" ");
    builder.append(union.toString(ToStringState.BASE));
    if (type != null) {
      builder.append(" : ");
      builder.append(type.toString(ToStringState.MERGE));
    }

    return new ToStringState(builder.toString(), ToStringState.MERGE);
  }

  public ToStringState onToMap(ToStringState base, ToStringState type) {
    StringBuilder builder = new StringBuilder("toMap ");

    builder.append(base.toString(ToStringState.TO_MAP, true));
    if (type != null) {
      builder.append(" : ");
      builder.append(type.toString(ToStringState.BASE));
    }

    return new ToStringState(builder.toString(), ToStringState.TO_MAP);
  }

  public ToStringState onMissingImport(Expr.ImportMode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder("missing");

    if (hash != null) {
      builder.append(" ");
      builder.append(Expr.Util.encodeHashBytes(hash));
    }

    if (mode != Expr.ImportMode.CODE) {
      builder.append(" as ");
      builder.append(mode);
    }

    return new ToStringState(builder.toString(), ToStringState.BASE);
  }

  public ToStringState onEnvImport(String value, Expr.ImportMode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder("env:");
    builder.append(value);

    if (hash != null) {
      builder.append(" ");
      builder.append(Expr.Util.encodeHashBytes(hash));
    }

    if (mode != Expr.ImportMode.CODE) {
      builder.append(" as ");
      builder.append(mode);
    }

    return new ToStringState(builder.toString(), ToStringState.BASE);
  }

  public ToStringState onLocalImport(Path path, Expr.ImportMode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder(path.toString());

    if (hash != null) {
      builder.append(" ");
      builder.append(Expr.Util.encodeHashBytes(hash));
    }

    if (mode != Expr.ImportMode.CODE) {
      builder.append(" as ");
      builder.append(mode);
    }

    return new ToStringState(builder.toString(), ToStringState.BASE);
  }

  @Override
  public ToStringState onClasspathImport(Path path, Expr.ImportMode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder("classpath:");

    builder.append(path.toString());

    if (hash != null) {
      builder.append(" ");
      builder.append(Expr.Util.encodeHashBytes(hash));
    }

    if (mode != Expr.ImportMode.CODE) {
      builder.append(" as ");
      builder.append(mode);
    }

    return new ToStringState(builder.toString(), ToStringState.BASE);
  }

  public ToStringState onRemoteImport(
      URI url, ToStringState using, Expr.ImportMode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder(url.toString());

    if (using != null) {
      builder.append(" using ");
      builder.append(using.toString(ToStringState.BASE));
    }

    if (hash != null) {
      builder.append(" ");
      builder.append(Expr.Util.encodeHashBytes(hash));
    }

    if (mode != Expr.ImportMode.CODE) {
      builder.append(" as ");
      builder.append(mode);
    }

    return new ToStringState(builder.toString(), ToStringState.BASE);
  }
}
