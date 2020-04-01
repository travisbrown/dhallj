package org.dhallj.core.binary;

public class Label {
  public static int APPLICATION = 0;
  public static int LAMBDA = 1;
  public static int PI = 2;
  public static int OPERATOR_APPLICATION = 3;
  public static int LIST = 4;
  public static int SOME = 5;
  public static int MERGE = 6;
  public static int RECORD_TYPE = 7;
  public static int RECORD_LITERAL = 8;
  public static int FIELD_ACCESS = 9;
  public static int PROJECTION = 10;
  public static int UNION_TYPE = 11;
  public static int IF = 14;
  public static int NATURAL = 15;
  public static int INTEGER = 16;
  public static int TEXT = 18;
  public static int ASSERT = 19;
  public static int IMPORT = 24;
  public static int LET = 25;
  public static int ANNOTATION = 26;
  public static int TO_MAP = 27;
  public static int EMPTY_LIST_WITH_ABSTRACT_TYPE = 28;

  public static int IMPORT_TYPE_REMOTE_HTTP = 0;
  public static int IMPORT_TYPE_REMOTE_HTTPS = 1;
  public static int IMPORT_TYPE_LOCAL_ABSOLUTE = 2;
  public static int IMPORT_TYPE_LOCAL_HERE = 3;
  public static int IMPORT_TYPE_LOCAL_PARENT = 4;
  public static int IMPORT_TYPE_LOCAL_HOME = 5;
  public static int IMPORT_TYPE_MISSING = 7;
  public static int IMPORT_TYPE_ENV = 6;
}
