package org.dhallj.yaml;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import org.dhallj.core.converters.JsonHandler;

public class YamlHandler implements JsonHandler {
  private final Deque<YamlContext> stack = new ArrayDeque<>();
  private final boolean skipNulls;

  public YamlHandler(boolean skipNulls) {
    this.skipNulls = skipNulls;
  }

  public YamlHandler() {
    this(true);
  }

  private final void addValue(Object value) {
    if (stack.isEmpty()) {
      stack.push(new RootContext(value));
    } else {
      stack.peek().add(value);
    }
  }

  public Object getResult() {
    return this.stack.pop().getResult();
  }

  public void onNull() {
    this.addValue(null);
  }

  public void onBoolean(boolean value) {
    this.addValue(value);
  }

  public void onNumber(BigInteger value) {
    this.addValue(value);
  }

  public void onDouble(double value) {
    this.addValue(value);
  }

  public void onString(String value) {
    this.addValue(value.replaceAll("\\\\n", "\n"));
  }

  public void onArrayStart() {
    this.stack.push(new ArrayContext());
  }

  public void onArrayEnd() {
    YamlContext current = this.stack.pop();

    if (this.stack.isEmpty()) {
      this.stack.push(current);
    } else {
      this.stack.peek().add(current.getResult());
    }
  }

  public void onArrayElementGap() {}

  public void onObjectStart() {
    this.stack.push(new ObjectContext(this.skipNulls));
  }

  public void onObjectEnd() {
    YamlContext current = this.stack.pop();

    if (this.stack.isEmpty()) {
      this.stack.push(current);
    } else {
      this.stack.peek().add(current.getResult());
    }
  }

  public void onObjectField(String name) {
    this.stack.peek().add(name);
  }

  public void onObjectFieldGap() {}
}
