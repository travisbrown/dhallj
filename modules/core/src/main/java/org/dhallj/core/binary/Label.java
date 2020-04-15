package org.dhallj.core.binary;

final class Label {
  public static final int APPLICATION = 0;
  public static final int LAMBDA = 1;
  public static final int PI = 2;
  public static final int OPERATOR_APPLICATION = 3;
  public static final int LIST = 4;
  public static final int SOME = 5;
  public static final int MERGE = 6;
  public static final int RECORD_TYPE = 7;
  public static final int RECORD_LITERAL = 8;
  public static final int FIELD_ACCESS = 9;
  public static final int PROJECTION = 10;
  public static final int UNION_TYPE = 11;
  public static final int IF = 14;
  public static final int NATURAL = 15;
  public static final int INTEGER = 16;
  public static final int TEXT = 18;
  public static final int ASSERT = 19;
  public static final int IMPORT = 24;
  public static final int LET = 25;
  public static final int ANNOTATED = 26;
  public static final int TO_MAP = 27;
  public static final int EMPTY_LIST_WITH_ABSTRACT_TYPE = 28;

  public static final int IMPORT_TYPE_REMOTE_HTTP = 0;
  public static final int IMPORT_TYPE_REMOTE_HTTPS = 1;
  public static final int IMPORT_TYPE_LOCAL_ABSOLUTE = 2;
  public static final int IMPORT_TYPE_LOCAL_HERE = 3;
  public static final int IMPORT_TYPE_LOCAL_PARENT = 4;
  public static final int IMPORT_TYPE_LOCAL_HOME = 5;
  public static final int IMPORT_TYPE_ENV = 6;
  public static final int IMPORT_TYPE_MISSING = 7;
  public static final int IMPORT_TYPE_CLASSPATH =
      23; // Allows CBOR tiny field encoding but leaves room for Dhall spec to expand import types
}
