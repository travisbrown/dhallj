package org.dhallj.core;

final class Tags {
  static final int NOTE = 0;

  // Non-recursive constructors.
  static final int NATURAL = 1;
  static final int INTEGER = 2;
  static final int DOUBLE = 3;
  static final int BUILT_IN = 4;
  static final int IDENTIFIER = 5;

  // Binding constructors.
  static final int LAMBDA = 6;
  static final int PI = 7;
  static final int LET = 8;

  // Other.
  static final int TEXT = 9;
  static final int NON_EMPTY_LIST = 10;
  static final int EMPTY_LIST = 11;

  static final int RECORD = 12;
  static final int RECORD_TYPE = 13;
  static final int UNION_TYPE = 14;

  static final int FIELD_ACCESS = 15;
  static final int PROJECTION = 16;
  static final int PROJECTION_BY_TYPE = 17;

  static final int APPLICATION = 18;
  static final int OPERATOR_APPLICATION = 19;
  static final int IF = 20;
  static final int ANNOTATED = 21;
  static final int ASSERT = 22;

  // Syntactic sugar.
  static final int MERGE = 23;
  static final int TO_MAP = 24;

  // Imports.
  static final int MISSING_IMPORT = 25;
  static final int ENV_IMPORT = 26;
  static final int LOCAL_IMPORT = 27;
  static final int REMOTE_IMPORT = 28;
  static final int CLASSPATH_IMPORT = 29;
}
