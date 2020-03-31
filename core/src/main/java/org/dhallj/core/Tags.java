package org.dhallj.core;

final class Tags {
  static final int NOTE = 0;

  // Non-recursive constructors.
  static final int NATURAL = 1;
  static final int INTEGER = 2;
  static final int DOUBLE = 3;
  static final int IDENTIFIER = 4;

  // Binding constructors.
  static final int LAMBDA = 5;
  static final int PI = 6;
  static final int LET = 7;

  // Other.
  static final int TEXT = 8;
  static final int NON_EMPTY_LIST = 9;
  static final int EMPTY_LIST = 10;

  static final int RECORD = 11;
  static final int RECORD_TYPE = 12;
  static final int UNION_TYPE = 13;

  static final int FIELD_ACCESS = 14;
  static final int PROJECTION = 15;
  static final int PROJECTION_BY_TYPE = 16;

  static final int APPLICATION = 17;
  static final int OPERATOR_APPLICATION = 18;
  static final int IF = 19;
  static final int ANNOTATED = 20;
  static final int ASSERT = 21;

  // Syntactic sugar.
  static final int MERGE = 22;
  static final int TO_MAP = 23;

  // Imports.
  static final int MISSING_IMPORT = 24;
  static final int ENV_IMPORT = 25;
  static final int LOCAL_IMPORT = 26;
  static final int REMOTE_IMPORT = 27;
}
