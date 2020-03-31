package org.dhallj.core.util;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Vis;
import org.dhallj.core.visitor.PureVis;

public class ToStringVisitor extends PureVis<String> {
  public static Vis<String> instance = new ToStringVisitor();

  public String onNote(String base, Source source) {
    return base;
  }

  public String onNatural(BigInteger value) {
    return value.toString();
  }

  public String onInteger(BigInteger value) {
    return value.toString();
  }

  public String onDouble(double value) {
    return Double.toString(value);
  }

  public String onIdentifier(String value, long index) {
    return (index == 0) ? value : String.format("%s@%d", value, index);
  }

  public String onLambda(String name, String type, String result) {
    return String.format("λ(%s : %s) → %s", name, type, result);
  }

  public String onPi(String name, String type, String result) {
    return String.format("∀(%s : %s) → %s", name, type, result);
  }

  public String onLet(String name, String type, String value, String body) {
    String typeString = (type == null) ? "" : String.format(": %s", type);
    return String.format("let %s%s = %s in %s", name, typeString, value, body);
  }

  public String onText(String[] parts, List<String> interpolated) {

    StringBuilder builder = new StringBuilder("\"");
    builder.append(parts[0]);
    int i = 1;
    Iterator<String> it = interpolated.iterator();

    while (it.hasNext()) {
      builder.append("${");
      builder.append(it.next());
      builder.append("}");
      builder.append(parts[i++]);
    }
    if (i < parts.length) {
      builder.append(parts[i]);
    }
    builder.append("\"");
    return builder.toString();
  }

  public String onNonEmptyList(List<String> values) {
    StringBuilder builder = new StringBuilder("[");
    Iterator<String> it = values.iterator();
    while (it.hasNext()) {
      builder.append(it.next());
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append("]");

    return builder.toString();
  }

  public String onEmptyList(String type) {
    return String.format("[]: %s", type);
  }

  public String onRecord(List<Entry<String, String>> fields) {
    if (fields.isEmpty()) {
      return "{=}";
    } else {
      StringBuilder builder = new StringBuilder("{");
      Iterator<Entry<String, String>> it = fields.iterator();
      while (it.hasNext()) {
        Entry<String, String> entry = it.next();
        builder.append(entry.getKey());
        builder.append(" = ");
        builder.append(entry.getValue());
        if (it.hasNext()) {
          builder.append(", ");
        }
      }
      builder.append("}");

      return builder.toString();
    }
  }

  public String onRecordType(List<Entry<String, String>> fields) {
    StringBuilder builder = new StringBuilder("{");
    Iterator<Entry<String, String>> it = fields.iterator();
    while (it.hasNext()) {
      Entry<String, String> entry = it.next();
      builder.append(entry.getKey());
      builder.append(": ");
      builder.append(entry.getValue());
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append("}");

    return builder.toString();
  }

  public String onUnionType(List<Entry<String, String>> fields) {
    StringBuilder builder = new StringBuilder("<");
    Iterator<Entry<String, String>> it = fields.iterator();
    while (it.hasNext()) {
      Entry<String, String> entry = it.next();
      builder.append(entry.getKey());
      String type = entry.getValue();
      if (type != null) {
        builder.append(": ");
        builder.append(type);
      }
      if (it.hasNext()) {
        builder.append(" | ");
      }
    }
    builder.append(">");

    return builder.toString();
  }

  public String onFieldAccess(String base, String fieldName) {
    return String.format("%s.%s", base, fieldName);
  }

  public String onProjection(String base, String[] fieldNames) {
    StringBuilder builder = new StringBuilder(base);
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

  public String onProjectionByType(String base, String type) {
    return String.format("%s.(%s)", base, type);
  }

  public String onApplication(String base, List<String> args) {
    StringBuilder builder = new StringBuilder("(");
    builder.append(base);
    builder.append(" ");

    for (int i = 0; i < args.size(); i += 1) {
      builder.append(args.get(i));
      if (i < args.size() - 1) {
        builder.append(" ");
      }
    }
    builder.append(")");

    return builder.toString();
  }

  public String onOperatorApplication(Operator operator, String lhs, String rhs) {
    return String.format("(%s %s %s)", lhs, operator, rhs);
  }

  public String onIf(String predicate, String thenValue, String elseValue) {
    return String.format("if %s then %s else %s", predicate, thenValue, elseValue);
  }

  public String onAnnotated(String base, String type) {
    return String.format("%s: %s", base, type);
  }

  public String onAssert(String base) {
    return String.format("assert: %s", base);
  }

  public String onMerge(String handlers, String union, String type) {
    StringBuilder builder = new StringBuilder("merge");

    builder.append(handlers);
    builder.append(" ");
    builder.append(union);
    if (type != null) {
      builder.append(": ");
      builder.append(type);
    }

    return builder.toString();
  }

  public String onToMap(String base, String type) {
    StringBuilder builder = new StringBuilder("toMap");

    builder.append(base);
    if (type != null) {
      builder.append(": ");
      builder.append(type);
    }

    return builder.toString();
  }

  public String onMissingImport(Import.Mode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder("missing");

    if (mode != Import.Mode.CODE) {
      builder.append(" ");
      builder.append(mode);
    }

    return builder.toString();
  }

  public String onEnvImport(String value, Import.Mode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder("env:");
    builder.append(value);

    if (mode != Import.Mode.CODE) {
      builder.append(" ");
      builder.append(mode);
    }

    return builder.toString();
  }

  public String onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder(path.toString());

    if (mode != Import.Mode.CODE) {
      builder.append(" ");
      builder.append(mode);
    }

    return builder.toString();
  }

  public String onRemoteImport(URI url, String using, Import.Mode mode, byte[] hash) {
    StringBuilder builder = new StringBuilder(url.toString());

    if (mode != Import.Mode.CODE) {
      builder.append(" ");
      builder.append(mode);
    }

    return builder.toString();
  }
}
