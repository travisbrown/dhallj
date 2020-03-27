package org.dhallj.cbor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.math.BigInteger;

public abstract class Writer {
  private static int BYTES_1 = 24;
  private static int BYTES_2 = 25;
  private static int BYTES_4 = 26;
  private static int BYTES_8 = 27;
  private static int MAJOR_TYPE_UNSIGNED_INTEGER = 0;
  private static int MAJOR_TYPE_NEGATIVE_INTEGER = 1;
  private static int MAJOR_TYPE_BYTE_STRING = 2;
  private static int MAJOR_TYPE_TEXT_STRING = 3;
  private static int MAJOR_TYPE_ARRAY = 4;;
  private static int MAJOR_TYPE_MAP = 5;
  private static int MAJOR_TYPE_TAG = 6;
  private static int MAJOR_TYPE_SPECIAL = 7;
  private static int FALSE = 244;
  private static int TRUE = 245;
  private static int NULL = 246;
  private static BigInteger LONG_MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);
  private static BigInteger BYTES_8_MAX_VALUE = new BigInteger("18446744073709551616");

  protected abstract OutputStream getStream();

  public final void writeNull() {
    this.write((byte) NULL);
  }

  public final void writeBoolean(boolean value) {
    this.write((byte) (value ? TRUE : FALSE));
  }

  public final void writeLong(long value) {
    this.writeTypeAndLength(
        (value >= 0) ? MAJOR_TYPE_UNSIGNED_INTEGER : MAJOR_TYPE_NEGATIVE_INTEGER, value);
  }

  public final void writeBigInteger(BigInteger value) {
    this.writeTypeAndLength(
        (value.compareTo(BigInteger.ZERO) >= 0)
            ? MAJOR_TYPE_UNSIGNED_INTEGER
            : MAJOR_TYPE_NEGATIVE_INTEGER,
        value);
  }

  public final void writeString(String value) {
    byte[] bs = null;
    try {
      bs = value.getBytes("UTF-8");
    } catch (java.io.UnsupportedEncodingException e) {
    }
    this.writeTypeAndLength(MAJOR_TYPE_TEXT_STRING, bs.length);
    this.write(bs);
  }

  public final void writeDouble(double value) {
    int base = MAJOR_TYPE_SPECIAL << 5;

    if (Double.isNaN(value)) {
      this.write((byte) (base | BYTES_2), (byte) 126, (byte) 0);
    } else if (Double.isInfinite(value)) {
      if (Double.compare(value, 0) > 0) {
        this.write((byte) (base | BYTES_2), (byte) 124, (byte) 0);
      } else {
        this.write((byte) (base | BYTES_2), (byte) 252, (byte) 0);
      }
    } else if (Double.compare(value, 0.0) == 0) {
      this.write((byte) (base | BYTES_2), (byte) 0, (byte) 0);

    } else if (Double.compare(value, -0.0) == 0) {
      this.write((byte) (base | BYTES_2), (byte) 8, (byte) 0);
    } else {
      float asFloat = (float) value;
      if (value == (double) asFloat) {
        int bits = Float.floatToRawIntBits(asFloat);
        write(
            (byte) (base | BYTES_4),
            (byte) ((bits >> 24) & 0xff),
            (byte) ((bits >> 16) & 0xff),
            (byte) ((bits >> 8) & 0xff),
            (byte) ((bits >> 0) & 0xff));
      } else {

        long bits = Double.doubleToRawLongBits(value);
        write(
            (byte) (base | BYTES_8),
            (byte) ((bits >> 56) & 0xff),
            (byte) ((bits >> 48) & 0xff),
            (byte) ((bits >> 40) & 0xff),
            (byte) ((bits >> 32) & 0xff),
            (byte) ((bits >> 24) & 0xff),
            (byte) ((bits >> 16) & 0xff),
            (byte) ((bits >> 8) & 0xff),
            (byte) ((bits >> 0) & 0xff));
      }
    }
  }

  public final void writeArrayStart(int length) {
    this.writeTypeAndLength(MAJOR_TYPE_ARRAY, length);
  }

  public final void writeMapStart(int length) {
    this.writeTypeAndLength(MAJOR_TYPE_MAP, length);
  }

  private final void write(byte b) {
    try {
      this.getStream().write(b);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private final void write(byte... bs) {
    try {
      this.getStream().write(bs);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private final void writeTypeAndLength(int majorType, long length) {
    int base = majorType << 5;

    if (length <= 23L) {
      this.write((byte) (base | length));
    } else if (length < (1L << 8)) {
      this.write((byte) (base | BYTES_1), (byte) length);
    } else if (length < (1L << 16)) {
      this.write((byte) (base | BYTES_2), (byte) (length >> 8), (byte) (length & 0xff));
    } else if (length < (1L << 32)) {
      this.write(
          (byte) (base | BYTES_4),
          (byte) ((length >> 24) & 0xff),
          (byte) ((length >> 16) & 0xff),
          (byte) ((length >> 8) & 0xff),
          (byte) (length & 0xff));
    } else {
      this.write(
          (byte) (base | BYTES_8),
          (byte) ((length >> 56) & 0xff),
          (byte) ((length >> 48) & 0xff),
          (byte) ((length >> 40) & 0xff),
          (byte) ((length >> 32) & 0xff),
          (byte) ((length >> 24) & 0xff),
          (byte) ((length >> 16) & 0xff),
          (byte) ((length >> 8) & 0xff),
          (byte) (length & 0xff));
    }
  }

  private final void writeTypeAndLength(int majorType, BigInteger length) {
    if (length.compareTo(LONG_MAX_VALUE) <= 0) {
      this.writeTypeAndLength(majorType, length.longValue());
    } else if (length.compareTo(BYTES_8_MAX_VALUE) < 0) {
      int base = majorType << 5;
      BigInteger mask = BigInteger.valueOf(0xff);
      write(
          (byte) (base | BYTES_8),
          length.shiftRight(56).and(mask).byteValue(),
          length.shiftRight(48).and(mask).byteValue(),
          length.shiftRight(40).and(mask).byteValue(),
          length.shiftRight(32).and(mask).byteValue(),
          length.shiftRight(24).and(mask).byteValue(),
          length.shiftRight(16).and(mask).byteValue(),
          length.shiftRight(8).and(mask).byteValue(),
          length.and(mask).byteValue());
    } else {
      if (majorType == MAJOR_TYPE_NEGATIVE_INTEGER) {
        writeTypeAndLength(MAJOR_TYPE_TAG, 3);
      } else {
        writeTypeAndLength(MAJOR_TYPE_TAG, 2);
      }
      byte[] bs = length.toByteArray();
      writeTypeAndLength(MAJOR_TYPE_BYTE_STRING, bs.length);
      write(bs);
    }
  }
}
