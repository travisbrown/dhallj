package org.dhallj.cbor;

public enum AdditionalInfo {
  DIRECT(0), // 0-23
  ONE_BYTE(24), // 24
  TWO_BYTES(25), // 25
  FOUR_BYTES(26), // 26
  EIGHT_BYTES(27), // 27
  RESERVED(28), // 28-30
  INDEFINITE(31); // 31

  final int value;

  private AdditionalInfo(int value) {
    this.value = value;
  }

  public static AdditionalInfo fromByte(byte b) {
    switch (b & 31) {
      case 24:
        return ONE_BYTE;
      case 25:
        return TWO_BYTES;
      case 26:
        return FOUR_BYTES;
      case 27:
        return EIGHT_BYTES;
      case 28:
      case 29:
      case 30:
        return RESERVED;
      case 31:
        return INDEFINITE;
      default:
        return DIRECT;
    }
  }
}
