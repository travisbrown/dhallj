package org.dhallj.core.converters;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.cbor.Writer;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Visitor;

public final class JsonConverter extends Visitor.Constant<Boolean> {
  private final JsonHandler handler;

  public JsonConverter(JsonHandler handler) {
    super(false);
    this.handler = handler;
  }

  public static final String toCompactString(Expr expr) {
    JsonHandler.CompactStringPrinter handler = new JsonHandler.CompactStringPrinter();
    boolean isConverted = expr.accept(new JsonConverter(handler));
    if (isConverted) {
      return handler.toString();
    } else {
      return null;
    }
  }

  private static final String escape(String input) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);

      if (c == '\\') {
        char next = input.charAt(++i);

        if (next == '"') {
          builder.append("\\\"");
        } else if (next == '$') {
          builder.append("$");
        } else {
          builder.append(c);
          builder.append(next);
        }
      } else if (c == '"') {
        builder.append("\\\"");
      } else {
        builder.append(c);
      }
    }

    return builder.toString();
  }

  @Override
  public boolean sortFields() {
    return false;
  }

  @Override
  public boolean flattenToMapLists() {
    return true;
  }

  @Override
  public Boolean onNatural(Expr self, BigInteger value) {
    this.handler.onNumber(value);
    return true;
  }

  @Override
  public Boolean onInteger(Expr self, BigInteger value) {
    this.handler.onNumber(value);
    return true;
  }

  @Override
  public Boolean onDouble(Expr self, double value) {
    this.handler.onDouble(value);
    return true;
  }

  @Override
  public Boolean onBuiltIn(Expr self, String name) {
    if (name.equals("True")) {
      this.handler.onBoolean(true);
      return true;
    } else if (name.equals("False")) {
      this.handler.onBoolean(false);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Boolean onText(String[] parts, List<Boolean> interpolated) {
    if (parts.length == 1) {
      this.handler.onString(escape(parts[0]));
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean prepareNonEmptyList(int size) {
    this.handler.onArrayStart();
    return true;
  }

  @Override
  public boolean prepareNonEmptyListElement(int index) {
    if (index > 0) {
      this.handler.onArrayElementGap();
    }
    return true;
  }

  @Override
  public Boolean onNonEmptyList(List<Boolean> values) {
    for (boolean value : values) {
      if (!value) {
        return false;
      }
    }
    this.handler.onArrayEnd();
    return true;
  }

  @Override
  public Boolean onEmptyList(Boolean type) {
    this.handler.onArrayStart();
    this.handler.onArrayEnd();
    return true;
  }

  @Override
  public boolean prepareRecord(int size) {
    this.handler.onObjectStart();
    return true;
  }

  @Override
  public boolean prepareRecordField(String name, Expr type, int index) {
    if (index > 0) {
      this.handler.onObjectFieldGap();
    }
    this.handler.onObjectField(escape(name));
    return true;
  }

  @Override
  public Boolean onRecord(final List<Entry<String, Boolean>> fields) {
    for (Entry<String, Boolean> field : fields) {
      if (!field.getValue()) {
        return false;
      }
    }
    this.handler.onObjectEnd();
    return true;
  }

  @Override
  public boolean prepareFieldAccess(Expr base, String fieldName) {
    List<Entry<String, Expr>> asUnion = Expr.Util.asUnionType(base);

    if (asUnion != null) {
      for (Entry<String, Expr> field : asUnion) {
        if (field.getKey().equals(fieldName) && field.getValue() == null) {
          this.handler.onString(escape(fieldName));
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public Boolean onFieldAccess(Boolean base, String fieldName) {
    return base == null || base;
  }

  @Override
  public boolean prepareApplication(Expr base, int size) {
    String asBuiltIn = Expr.Util.asBuiltIn(base);

    if (asBuiltIn != null && size == 1) {
      if (asBuiltIn.equals("Some")) {
        return false;
      } else if (asBuiltIn.equals("None")) {
        this.handler.onNull();
        return false;
      }
    } else {
      Entry<Expr, String> asFieldAccess = Expr.Util.asFieldAccess(base);

      if (asFieldAccess != null) {
        List<Entry<String, Expr>> asUnion = Expr.Util.asUnionType(asFieldAccess.getKey());

        if (asUnion != null) {
          for (Entry<String, Expr> field : asUnion) {
            if (field.getKey().equals(asFieldAccess.getValue()) && field.getValue() != null) {
              return false;
            }
          }
        }
      }
    }

    return true;
  }

  @Override
  public Boolean onApplication(Boolean base, List<Boolean> args) {
    return base == null || base;
  }
}
