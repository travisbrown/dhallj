package org.dhallj.core;

/** Represents a Dhall operator. */
public enum Operator {
  OR("||", 2, true),
  AND("&&", 6, true),
  EQUALS("==", 11, true),
  NOT_EQUALS("!=", 12, true),
  PLUS("+", 3, false),
  TIMES("*", 10, false),
  TEXT_APPEND("++", 4, false),
  LIST_APPEND("#", 5, false),
  COMBINE("\u2227", 7, false),
  PREFER("\u2afd", 8, false),
  COMBINE_TYPES("\u2a53", 9, false),
  IMPORT_ALT("?", 1, false),
  EQUIVALENT("\u2261", 13, false),
  COMPLETE("::", 0, false);

  private static final Operator[] values = values();

  private final String value;
  private final int precedence;
  private final boolean isBoolOperator;

  Operator(String value, int precedence, boolean isBoolOperator) {
    this.value = value;
    this.precedence = precedence;
    this.isBoolOperator = isBoolOperator;
  }

  public final boolean isBoolOperator() {
    return this.isBoolOperator;
  }

  public final int getLabel() {
    return this.ordinal();
  }

  public final int getPrecedence() {
    return this.precedence;
  }

  public final String toString() {
    return this.value;
  }

  public static final Operator fromLabel(int ordinal) {
    if (ordinal >= 0 && ordinal < values.length) {
      return values[ordinal];
    } else {
      return null;
    }
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
    } else if (input.equals(COMBINE.value) || input.equals("/\\")) {
      return COMBINE;
    } else if (input.equals(PREFER.value) || input.equals("//")) {
      return PREFER;
    } else if (input.equals(COMBINE_TYPES.value) || input.equals("//\\\\")) {
      return COMBINE_TYPES;
    } else if (input.equals(IMPORT_ALT.value)) {
      return IMPORT_ALT;
    } else if (input.equals(EQUIVALENT.value) || input.equals("===")) {
      return EQUIVALENT;
    } else if (input.equals(COMPLETE.value)) {
      return COMPLETE;
    } else {
      throw new IllegalArgumentException("No org.dhallj.core.Operator represented by " + input);
    }
  }
}
