package org.dhallj.core;

public enum Operator {
  OR("||", true),
  AND("&&", true),
  EQUALS("==", true),
  NOT_EQUALS("!=", true),
  PLUS("+", false),
  TIMES("*", false),
  TEXT_APPEND("++", false),
  LIST_APPEND("#", false),
  COMBINE("/\\", false),
  PREFER("//", false),
  COMBINE_TYPES("//\\\\", false),
  IMPORT_ALT("?", false),
  EQUIVALENT("===", false),
  COMPLETE("::", false);

  private final String value;
  private final boolean isBoolOperator;

  Operator(String value, boolean isBoolOperator) {
    this.value = value;
    this.isBoolOperator = isBoolOperator;
  }

  public final boolean isBoolOperator() {
    return this.isBoolOperator;
  }

  public final int getLabel() {
    return this.ordinal();
  }

  public final String toString() {
    return this.value;
  }

  public static final Operator parse(String input) {
    if (input.equals(OR.value)) {
      return OR;
    } else if (input.equals(AND.value)) {
      return AND;
    } else if (input.equals(EQUALS.value)) {
      return EQUALS;
    } else if (input.equals(NOT_EQUALS.value)) {
      return NOT_EQUALS;
    } else if (input.equals(PLUS.value)) {
      return PLUS;
    } else if (input.equals(TIMES.value)) {
      return TIMES;
    } else if (input.equals(TEXT_APPEND.value)) {
      return TEXT_APPEND;
    } else if (input.equals(LIST_APPEND.value)) {
      return LIST_APPEND;
    } else if (input.equals(COMBINE.value) || input.equals("\u2227")) {
      return COMBINE;
    } else if (input.equals(PREFER.value) || input.equals("\u2afd")) {
      return PREFER;
    } else if (input.equals(COMBINE_TYPES.value) || input.equals("\u2a53")) {
      return COMBINE_TYPES;
    } else if (input.equals(IMPORT_ALT.value)) {
      return IMPORT_ALT;
    } else if (input.equals(EQUIVALENT.value) || input.equals("\u2261")) {
      return EQUIVALENT;
    } else if (input.equals(COMPLETE.value)) {
      return COMPLETE;
    } else {
      throw new IllegalArgumentException(
          String.format("No org.dhallj.core.Operator represented by %s", input));
    }
  }
}
