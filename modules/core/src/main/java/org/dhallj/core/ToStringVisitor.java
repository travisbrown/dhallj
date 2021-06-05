package org.dhallj.core;

import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

final class ToStringState {
  static final int NONE = 0;
  static final int OPERATOR = 1;
  static final int APPLICATION = Operator.NOT_EQUALS.getPrecedence() + 1;
  static final int IMPORT = APPLICATION + 1;
  static final int COMPLETE = IMPORT + 1;
  static final int SELECTOR = COMPLETE + 1;
  static final int PRIMITIVE = SELECTOR + 1;

  private final String text;
  private final int level;

  ToStringState(String text, int level) {
    this.text = text;
    this.level = level;
  }

  ToStringState(String text) {
    this(text, PRIMITIVE);
  }

  ToStringState withText(String text) {
    return new ToStringState(text, this.level);
  }

  String toString(int contextLevel) {
    if (this.level < contextLevel) {
      return "(" + this.text + ")";
    } else {
      return this.text;
    }
  }

  public String toString() {
    return this.toString(NONE);
  }

  static final int getOperatorLevel(Operator operator) {
    if (operator == Operator.COMPLETE) {
      return COMPLETE;
    } else {
      return operator.getPrecedence();
    }
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
        (value.compareTo(BigInteger.ZERO) >= 0) ? ("+" + value.toString()) : value.toString();
    return new ToStringState(withSign);
  }

  public ToStringState onDouble(Expr self, double value) {
    return new ToStringState(Double.toString(value));
  }

  public ToStringState onBuiltIn(Expr self, String name) {
    return new ToStringState(name);
  }

  public ToStringState onIdentifier(Expr self, String name, long index) {
    String maybeEscaped = escapeName(name);
    return new ToStringState(
        (index == 0) ? maybeEscaped : (maybeEscaped + "@" + Long.toString(index)));
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
        builder.append(entry.getValue().toString());
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
      builder.append(entry.getValue().toString());
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

  public ToStringState onNonEmptyList(List<ToStringState> values) {
    StringBuilder builder = new StringBuilder("[");
    Iterator<ToStringState> it = values.iterator();
    while (it.hasNext()) {
      builder.append(it.next().toString());
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append("]");

    return new ToStringState(builder.toString());
  }

  public ToStringState onFieldAccess(ToStringState base, String fieldName) {
    return new ToStringState(
        base.toString(ToStringState.PRIMITIVE) + "." + fieldName, ToStringState.SELECTOR);
  }

  public ToStringState onProjection(ToStringState base, String[] fieldNames) {
    StringBuilder builder = new StringBuilder(base.toString(ToStringState.PRIMITIVE));
    builder.append(".{");
    for (int i = 0; i < fieldNames.length; i += 1) {
      builder.append(fieldNames[i]);
      if (i < fieldNames.length - 1) {
        builder.append(", ");
      }
    }
    builder.append("}");

    return new ToStringState(builder.toString(), ToStringState.SELECTOR);
  }

  public ToStringState onProjectionByType(ToStringState base, ToStringState type) {
    return new ToStringState(
        base.toString(ToStringState.PRIMITIVE) + ".(" + type.toString() + ")",
        ToStringState.SELECTOR);
  }

  public ToStringState onOperatorApplication(
      Operator operator, ToStringState lhs, ToStringState rhs) {
    int operatorLevel = ToStringState.getOperatorLevel(operator);

    return new ToStringState(
        lhs.toString(operatorLevel)
            + " "
            + operator.toString()
            + " "
            + rhs.toString(operatorLevel + 1),
        operatorLevel);
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

    return new ToStringState(builder.toString(), ToStringState.IMPORT);
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

    return new ToStringState(builder.toString(), ToStringState.IMPORT);
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

    return new ToStringState(builder.toString(), ToStringState.IMPORT);
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

    return new ToStringState(builder.toString(), ToStringState.IMPORT);
  }

  public ToStringState onRemoteImport(
      URI url, ToStringState using, Expr.ImportMode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder(url.toString());

    if (using != null) {
      builder.append(" using ");
      builder.append(using.toString(ToStringState.IMPORT));
    }

    if (hash != null) {
      builder.append(" ");
      builder.append(Expr.Util.encodeHashBytes(hash));
    }

    if (mode != Expr.ImportMode.CODE) {
      builder.append(" as ");
      builder.append(mode);
    }

    return new ToStringState(builder.toString(), ToStringState.IMPORT);
  }

  public ToStringState onMerge(ToStringState handlers, ToStringState union, ToStringState type) {
    StringBuilder builder = new StringBuilder("merge ");

    builder.append(handlers.toString(ToStringState.IMPORT));
    builder.append(" ");
    builder.append(union.toString(ToStringState.IMPORT));
    if (type != null) {
      builder.append(" : ");
      builder.append(type.toString(ToStringState.APPLICATION));
      return new ToStringState(builder.toString(), ToStringState.NONE);
    } else {
      return new ToStringState(builder.toString(), ToStringState.APPLICATION);
    }
  }

  public ToStringState onLambda(String name, ToStringState type, ToStringState result) {
    return new ToStringState(
        "λ(" + escapeName(name) + " : " + type.toString() + ") → " + result.toString(),
        ToStringState.NONE);
  }

  public ToStringState onPi(String name, ToStringState type, ToStringState result) {
    String resultString = result.toString();

    return new ToStringState(
        name.equals("_")
            ? (type.toString(ToStringState.OPERATOR) + " → " + resultString)
            : ("∀(" + escapeName(name) + " : " + type.toString() + ") → " + resultString),
        ToStringState.NONE);
  }

  public ToStringState onLet(List<Expr.LetBinding<ToStringState>> bindings, ToStringState body) {
    String result = body.toString();

    for (int i = bindings.size() - 1; i >= 0; i--) {
      Expr.LetBinding<ToStringState> binding = bindings.get(i);

      String typeString = binding.hasType() ? (" : " + binding.getType().toString()) : "";

      result =
          "let "
              + escapeName(binding.getName())
              + typeString
              + " = "
              + binding.getValue().toString()
              + " in "
              + result;
    }

    return new ToStringState(result, ToStringState.NONE);
  }

  public ToStringState onText(String[] parts, List<ToStringState> interpolated) {

    StringBuilder builder = new StringBuilder("\"");
    builder.append(Expr.Util.escapeText(parts[0], false));
    int i = 1;
    Iterator<ToStringState> it = interpolated.iterator();

    while (it.hasNext()) {
      builder.append("${");
      builder.append(it.next().toString());
      builder.append("}");
      builder.append(Expr.Util.escapeText(parts[i++], false));
    }
    if (i < parts.length) {
      builder.append(Expr.Util.escapeText(parts[i], false));
    }
    builder.append("\"");
    return new ToStringState(builder.toString());
  }

  public ToStringState onEmptyList(ToStringState type) {
    return new ToStringState(
        "[] : " + type.toString(ToStringState.APPLICATION), ToStringState.NONE);
  }

  public ToStringState onApplication(ToStringState base, List<ToStringState> args) {
    StringBuilder builder = new StringBuilder(base.toString(ToStringState.IMPORT));
    builder.append(" ");

    for (int i = 0; i < args.size(); i += 1) {
      builder.append(args.get(i).toString(ToStringState.IMPORT));
      if (i < args.size() - 1) {
        builder.append(" ");
      }
    }

    return new ToStringState(builder.toString(), ToStringState.APPLICATION);
  }

  public ToStringState onIf(
      ToStringState predicate, ToStringState thenValue, ToStringState elseValue) {
    return new ToStringState(
        "if "
            + predicate.toString()
            + " then "
            + thenValue.toString()
            + " else "
            + elseValue.toString(),
        ToStringState.NONE);
  }

  public ToStringState onAnnotated(ToStringState base, ToStringState type) {
    return new ToStringState(
        base.toString(ToStringState.OPERATOR) + " : " + type.toString(), ToStringState.NONE);
  }

  public ToStringState onAssert(ToStringState base) {
    return new ToStringState("assert : " + base.toString(), ToStringState.NONE);
  }

  public ToStringState onToMap(ToStringState base, ToStringState type) {
    StringBuilder builder = new StringBuilder("toMap ");

    builder.append(base.toString(ToStringState.IMPORT));
    if (type != null) {
      builder.append(" : ");
      builder.append(type.toString(ToStringState.APPLICATION));
      return new ToStringState(builder.toString(), ToStringState.NONE);
    } else {
      return new ToStringState(builder.toString(), ToStringState.APPLICATION);
    }
  }

  public ToStringState onWith(ToStringState base, String[] path, ToStringState value) {
    StringBuilder builder = new StringBuilder();

    builder.append(base.toString(ToStringState.IMPORT));
    builder.append(" with ");
    for (int i = 0; i < path.length - 1; i += 1) {
      builder.append(path[i]);
      builder.append(".");
    }
    builder.append(path[path.length - 1]);
    builder.append(" = ");
    builder.append(value.toString(ToStringState.OPERATOR));

    return new ToStringState(builder.toString(), ToStringState.NONE);
  }

  private static boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  private static boolean isDigit(char c) {
    return (c >= '0' && c <= '9');
  }

  private static boolean isSimpleLabel(String name) {
    if (name.length() == 0) {
      return false;
    }

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
      return "`" + name + "`";
    } else {
      return name;
    }
  }
}
