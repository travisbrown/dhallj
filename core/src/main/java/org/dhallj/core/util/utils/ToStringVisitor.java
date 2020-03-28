package org.dhallj.core.util;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.Import;
import org.dhallj.core.Source;
import org.dhallj.core.Thunk;
import org.dhallj.core.Visitor;

/**
 * A simple printing visitor intended for debugging only.
 *
 * <p>Note that this visitor does not necessarily return valid Dhall code.
 */
public class ToStringVisitor implements Visitor.Internal<String> {
  private static ToStringVisitor instance = new ToStringVisitor();

  public static String show(Expr expr) {
    return expr.accept(instance);
  }

  public String onDoubleLiteral(double value) {
    return Double.toString(value);
  }

  public String onNaturalLiteral(BigInteger value) {
    return value.toString();
  }

  public String onIntegerLiteral(BigInteger value) {
    return value.toString();
  }

  public String onTextLiteral(String[] parts, Iterable<Thunk<String>> interpolated) {

    StringBuilder builder = new StringBuilder("\"");
    builder.append(parts[0]);
    int i = 1;
    Iterator<Thunk<String>> it = interpolated.iterator();

    while (it.hasNext()) {
      builder.append("${");
      builder.append(it.next().apply());
      builder.append("}");
      builder.append(parts[i++]);
    }
    if (i < parts.length) {
      builder.append(parts[i]);
    }
    builder.append("\"");
    return builder.toString();
  }

  public String onApplication(Thunk<String> base, Thunk<String> arg) {
    return String.format("(%s %s)", base.apply(), arg.apply());
  }

  public String onOperatorApplication(Operator operator, Thunk<String> lhs, Thunk<String> rhs) {
    return String.format("(%s %s %s)", lhs.apply(), operator, rhs.apply());
  }

  public String onIf(Thunk<String> cond, Thunk<String> thenValue, Thunk<String> elseValue) {
    return String.format(
        "if %s then %s else %s", cond.apply(), thenValue.apply(), elseValue.apply());
  }

  public String onLambda(String param, Thunk<String> input, Thunk<String> result) {
    return String.format("λ(%s: %s) -> %s", param, input.apply(), result.apply());
  }

  public String onPi(String param, Thunk<String> input, Thunk<String> result) {
    if (param.equals("_")) {
      return String.format("(%s -> %s)", input.apply(), result.apply());
    } else {
      return String.format("(forall (%s: %s) -> %s)", param, input.apply(), result.apply());
    }
  }

  public String onAssert(Thunk<String> base) {
    return String.format("(assert: %s)", base.apply());
  }

  public String onFieldAccess(Thunk<String> base, String fieldName) {
    return String.format("(%s.%s)", base.apply(), fieldName);
  }

  public String onProjection(Thunk<String> base, String[] fieldNames) {
    StringBuilder builder = new StringBuilder(base.apply());
    builder.append(".{");
    for (int i = 0; i < fieldNames.length; i += 1) {
      builder.append(fieldNames[i]);
      if (i < fieldNames.length - 1) {
        builder.append(", ");
      }
    }
    builder.append("}");

    return builder.toString();
  }

  public String onProjectionByType(Thunk<String> base, Thunk<String> type) {
    return "";
  }

  public String onIdentifier(String value, long index) {
    if (index <= 0) {
      return value;
    } else {
      return String.format("%s@%d", value, index);
    }
  }

  public String onRecordLiteral(Iterable<Entry<String, Thunk<String>>> fields, int size) {
    StringBuilder builder = new StringBuilder("{");
    Iterator<Entry<String, Thunk<String>>> it = fields.iterator();
    while (it.hasNext()) {
      Entry<String, Thunk<String>> entry = it.next();
      builder.append(entry.getKey());
      builder.append(" = ");
      builder.append(entry.getValue().apply());
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append("}");

    return builder.toString();
  }

  public String onRecordType(Iterable<Entry<String, Thunk<String>>> fields, int size) {
    StringBuilder builder = new StringBuilder("{");
    Iterator<Entry<String, Thunk<String>>> it = fields.iterator();
    while (it.hasNext()) {
      Entry<String, Thunk<String>> entry = it.next();
      builder.append(entry.getKey());
      builder.append(": ");
      builder.append(entry.getValue().apply());
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append("}");

    return builder.toString();
  }

  public String onUnionType(Iterable<Entry<String, Thunk<String>>> fields, int size) {
    StringBuilder builder = new StringBuilder("<");
    Iterator<Entry<String, Thunk<String>>> it = fields.iterator();
    while (it.hasNext()) {
      Entry<String, Thunk<String>> entry = it.next();
      builder.append(entry.getKey());
      builder.append(": ");
      String type = entry.getValue().apply();
      if (type != null) {
        builder.append(type);
      }
      if (it.hasNext()) {
        builder.append("| ");
      }
    }
    builder.append(">");

    return builder.toString();
  }

  public String onNonEmptyListLiteral(Iterable<Thunk<String>> values, int size) {
    StringBuilder builder = new StringBuilder("[");
    Iterator<Thunk<String>> it = values.iterator();
    while (it.hasNext()) {
      builder.append(it.next().apply());
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append("]");

    return builder.toString();
  }

  public String onEmptyListLiteral(Thunk<String> type) {
    return String.format("[]: %s", type.apply());
  }

  public String onLocalImport(Path path, Import.Mode mode) {
    return null;
  }

  public String onRemoteImport(URI url, Import.Mode mode) {
    return null;
  }

  public String onEnvImport(String value, Import.Mode mode) {
    return null;
  }

  public String onMissingImport(Import.Mode mode) {
    return null;
  }

  public final String onLet(
      String name, Thunk<String> type, Thunk<String> value, Thunk<String> body) {
    StringBuilder builder = new StringBuilder("let ");
    builder.append(name);
    String typeEval = type.apply();
    if (typeEval != null) {
      builder.append(": ");
      builder.append(typeEval);
    }
    builder.append(" = ");
    builder.append(value.apply());
    builder.append(" ");
    builder.append(" in ");
    builder.append(body.apply());

    return builder.toString();
  }

  public final String onAnnotated(Thunk<String> base, Thunk<String> type) {
    return String.format("%s: %s", base.apply(), type.apply());
  }

  public final String onToMap(Thunk<String> base, Thunk<String> type) {
    if (type == null) {
      return String.format("toMap %s", base.apply());
    } else {

      return String.format("toMap %s: %s", base.apply(), type.apply());
    }
  }

  public final String onMerge(Thunk<String> left, Thunk<String> right, Thunk<String> type) {
    if (type == null) {
      return String.format("merge %s %s", left.apply(), right.apply());
    } else {

      return String.format("merge %s %s: %s", left.apply(), right.apply(), type.apply());
    }
  }

  public final String onNote(Thunk<String> base, Source source) {
    return base.apply();
  }
}
