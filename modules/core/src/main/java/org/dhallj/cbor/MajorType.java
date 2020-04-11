package org.dhallj.cbor;

public enum MajorType {
  UNSIGNED_INTEGER(0),
  NEGATIVE_INTEGER(1),
  BYTE_STRING(2),
  TEXT_STRING(3),
  ARRAY(4),
  MAP(5),
  SEMANTIC_TAG(6),
  PRIMITIVE(7);

  final int value;

  private MajorType(int value) {
    this.value = value;
  }

  public static MajorType fromByte(byte b) {
    switch ((b & 0xff) >> 5) {
      case 0:
        return UNSIGNED_INTEGER;
      case 1:
        return NEGATIVE_INTEGER;
      case 2:
        return BYTE_STRING;
      case 3:
        return TEXT_STRING;
      case 4:
        return ARRAY;
      case 5:
        return MAP;
      case 6:
        return SEMANTIC_TAG;
      case 7:
        return PRIMITIVE;
      default:
        throw new IllegalArgumentException(String.format("Invalid CBOR major type %d", b));
    }
  }
}
