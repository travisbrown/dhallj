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

  public boolean sortFields() {
    return false;
  }

  public Boolean onNatural(Expr self, BigInteger value) {
    this.handler.onNumber(value);
    return true;
  }

  public Boolean onInteger(Expr self, BigInteger value) {
    this.handler.onNumber(value);
    return true;
  }

  public Boolean onDouble(Expr self, double value) {
    this.handler.onDouble(value);
    return true;
  }

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

  public Boolean onText(String[] parts, List<Boolean> interpolated) {
    if (parts.length == 1) {
      this.handler.onString(parts[0]);
      return true;
    } else {
      return false;
    }
  }

  public boolean prepareNonEmptyList(int size) {
    this.handler.onArrayStart();
    return true;
  }

  public boolean prepareNonEmptyListElement(int index) {
    if (index > 0) {
      this.handler.onArrayElementGap();
    }
    return true;
  }

  public Boolean onNonEmptyList(List<Boolean> values) {
    for (boolean value : values) {
      if (!value) {
        return false;
      }
    }
    this.handler.onArrayEnd();
    return true;
  }

  public Boolean onEmptyList(Boolean type) {
    this.handler.onArrayStart();
    this.handler.onArrayEnd();
    return true;
  }

  public boolean prepareRecord(int size) {
    this.handler.onObjectStart();
    return true;
  }

  public boolean prepareRecordField(String name, Expr type, int index) {
    if (index > 0) {
      this.handler.onObjectFieldGap();
    }
    this.handler.onObjectField(name);
    return true;
  }

  public Boolean onRecord(final List<Entry<String, Boolean>> fields) {
    for (Entry<String, Boolean> field : fields) {
      if (!field.getValue()) {
        return false;
      }
    }
    this.handler.onObjectEnd();
    return true;
  }
}
