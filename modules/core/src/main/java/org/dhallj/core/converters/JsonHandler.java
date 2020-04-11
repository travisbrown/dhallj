package org.dhallj.core.converters;

import java.io.PrintWriter;
import java.math.BigInteger;

public interface JsonHandler {
  void onNull();

  void onBoolean(boolean value);

  void onNumber(BigInteger value);

  void onDouble(double value);

  void onString(String value);

  void onArrayStart();

  void onArrayEnd();

  void onArrayElementGap();

  void onObjectStart();

  void onObjectEnd();

  void onObjectField(String name);

  void onObjectFieldGap();

  public static final class CompactPrinter implements JsonHandler {
    private final PrintWriter writer;

    public CompactPrinter(PrintWriter writer) {
      this.writer = writer;
    }

    public void onNull() {
      this.writer.print("null");
    }

    public void onBoolean(boolean value) {
      this.writer.print(value);
    }

    public void onNumber(BigInteger value) {
      this.writer.print(value.toString());
    }

    public void onDouble(double value) {
      this.writer.print(value);
    }

    public void onString(String value) {
      this.writer.printf("\"%s\"", value);
    }

    public void onArrayStart() {
      this.writer.print("[");
    }

    public void onArrayEnd() {
      this.writer.print("]");
    }

    public void onArrayElementGap() {
      this.writer.print(",");
    }

    public void onObjectStart() {
      this.writer.print("{");
    }

    public void onObjectEnd() {
      this.writer.print("}");
    }

    public void onObjectField(String name) {
      this.writer.printf("\"%s\":", name);
    }

    public void onObjectFieldGap() {
      this.writer.print(",");
    }
  }

  public static final class CompactStringPrinter implements JsonHandler {
    private final StringBuilder builder;

    public CompactStringPrinter() {
      this.builder = new StringBuilder();
    }

    public String toString() {
      return this.builder.toString();
    }

    public void onNull() {
      this.builder.append("null");
    }

    public void onBoolean(boolean value) {
      this.builder.append(value);
    }

    public void onNumber(BigInteger value) {
      this.builder.append(value.toString());
    }

    public void onDouble(double value) {
      this.builder.append(value);
    }

    public void onString(String value) {
      this.builder.append(String.format("\"%s\"", value));
    }

    public void onArrayStart() {
      this.builder.append("[");
    }

    public void onArrayEnd() {
      this.builder.append("]");
    }

    public void onArrayElementGap() {
      this.builder.append(",");
    }

    public void onObjectStart() {
      this.builder.append("{");
    }

    public void onObjectEnd() {
      this.builder.append("}");
    }

    public void onObjectField(String name) {
      this.builder.append(String.format("\"%s\":", name));
    }

    public void onObjectFieldGap() {
      this.builder.append(",");
    }
  }
}
