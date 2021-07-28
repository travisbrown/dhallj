package org.dhallj.cbor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public abstract class Writer {
  private static final int FALSE = 244;
  private static final int TRUE = 245;
  private static final int NULL = 246;
  private static final BigInteger LONG_MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);
  private static final BigInteger EIGHT_BYTES_MAX_VALUE = new BigInteger("18446744073709551616");
  private static final Charset UTF_8 = Charset.forName("UTF-8");

  private static final class WrappedIOException extends RuntimeException {
    final IOException underlying;

    WrappedIOException(IOException underlying) {
      this.underlying = underlying;
    }
  }

  public static class OutputStreamWriter extends Writer {
    protected final OutputStream stream;

    public OutputStreamWriter(OutputStream stream) {
      this.stream = stream;
    }

    protected final void write(byte b) {
      try {
        this.stream.write(b);
      } catch (IOException e) {
        throw new WrappedIOException(e);
      }
    }

    protected final void write(byte... bs) {
      try {
        this.stream.write(bs);
      } catch (IOException e) {
        throw new WrappedIOException(e);
      }
    }
  }

  public static final class ByteArrayWriter extends OutputStreamWriter {
    public ByteArrayWriter() {
      super(new ByteArrayOutputStream());
    }

    public final byte[] getBytes() {
      return ((ByteArrayOutputStream) this.stream).toByteArray();
    }
  }

  public static final class SHA256Writer extends Writer {
    private final MessageDigest messageDigest;

    public SHA256Writer() {
      MessageDigest tmp = null;
      try {
        tmp = MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException e) {
        // TODO: Something reasonable here.
      }

      this.messageDigest = tmp;
    }

    public final byte[] getHashBytes() {
      return this.messageDigest.digest();
    }

    protected final void write(byte b) {
      this.messageDigest.update(b);
    }

    protected final void write(byte... bs) {
      this.messageDigest.update(bs);
    }
  }

  protected abstract void write(byte b);

  protected abstract void write(byte... bs);

  public final void writeNull() {
    this.write((byte) NULL);
  }

  public final void writeBoolean(boolean value) {
    this.write((byte) (value ? TRUE : FALSE));
  }

  public final void writeLong(long value) {
    if (value >= 0) {
      this.writeTypeAndLength(MajorType.UNSIGNED_INTEGER.value, value);
    } else {
      this.writeTypeAndLength(MajorType.NEGATIVE_INTEGER.value, -(value + 1));
    }
  }

  public final void writeBigInteger(BigInteger value) {
    if (value.compareTo(BigInteger.ZERO) >= 0) {
      this.writeTypeAndLength(MajorType.UNSIGNED_INTEGER.value, value);
    } else {
      this.writeTypeAndLength(MajorType.NEGATIVE_INTEGER.value, value.add(BigInteger.ONE).negate());
    }
  }

  public final void writeBigDecimal(BigDecimal value) {
    this.writeTypeAndLength(MajorType.SEMANTIC_TAG.value, 4);
    this.writeTypeAndLength(MajorType.ARRAY.value, 2);
    this.writeLong(-((long) value.scale()));
    this.writeBigInteger(value.unscaledValue());
  }

  public final void writeString(String value) {
    byte[] bytes = value.getBytes(UTF_8);
    this.writeTypeAndLength(MajorType.TEXT_STRING.value, bytes.length);
    this.write(bytes);
  }

  public final void writeByteString(byte[] bytes) {
    this.writeTypeAndLength(MajorType.BYTE_STRING.value, bytes.length);
    this.write(bytes);
  }

  public final void writeDouble(double value) {
    int base = MajorType.PRIMITIVE.value << 5;

    if (Double.isNaN(value)) {
      this.write((byte) (base | AdditionalInfo.TWO_BYTES.value), (byte) 126, (byte) 0);
    } else {
      float asFloat = (float) value;
      if (value == (double) asFloat) {
        int asHalfFloatBits = HalfFloat.fromFloat(asFloat);

        if (HalfFloat.toFloat(asHalfFloatBits) == asFloat) {
          this.write(
              (byte) (base | AdditionalInfo.TWO_BYTES.value),
              (byte) ((asHalfFloatBits >> 8) & 0xff),
              (byte) ((asHalfFloatBits >> 0) & 0xff));
        } else {

          int bits = Float.floatToIntBits(asFloat);
          this.write(
              (byte) (base | AdditionalInfo.FOUR_BYTES.value),
              (byte) ((bits >> 24) & 0xff),
              (byte) ((bits >> 16) & 0xff),
              (byte) ((bits >> 8) & 0xff),
              (byte) ((bits >> 0) & 0xff));
        }
      } else {

        long bits = Double.doubleToLongBits(value);
        this.write(
            (byte) (base | AdditionalInfo.EIGHT_BYTES.value),
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
    this.writeTypeAndLength(MajorType.ARRAY.value, length);
  }

  public final void writeMapStart(int length) {
    this.writeTypeAndLength(MajorType.MAP.value, length);
  }

  private final void writeTypeAndLength(int majorType, long length) {
    int base = majorType << 5;

    if (length <= 23L) {
      this.write((byte) (base | length));
    } else if (length < (1L << 8)) {
      this.write((byte) (base | AdditionalInfo.ONE_BYTE.value), (byte) length);
    } else if (length < (1L << 16)) {
      this.write(
          (byte) (base | AdditionalInfo.TWO_BYTES.value),
          (byte) (length >> 8),
          (byte) (length & 0xff));
    } else if (length < (1L << 32)) {
      this.write(
          (byte) (base | AdditionalInfo.FOUR_BYTES.value),
          (byte) ((length >> 24) & 0xff),
          (byte) ((length >> 16) & 0xff),
          (byte) ((length >> 8) & 0xff),
          (byte) (length & 0xff));
    } else {
      this.write(
          (byte) (base | AdditionalInfo.EIGHT_BYTES.value),
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
    } else if (length.compareTo(EIGHT_BYTES_MAX_VALUE) < 0) {
      int base = majorType << 5;
      BigInteger mask = BigInteger.valueOf(0xff);
      this.write(
          (byte) (base | AdditionalInfo.EIGHT_BYTES.value),
          length.shiftRight(56).and(mask).byteValue(),
          length.shiftRight(48).and(mask).byteValue(),
          length.shiftRight(40).and(mask).byteValue(),
          length.shiftRight(32).and(mask).byteValue(),
          length.shiftRight(24).and(mask).byteValue(),
          length.shiftRight(16).and(mask).byteValue(),
          length.shiftRight(8).and(mask).byteValue(),
          length.and(mask).byteValue());
    } else {
      if (majorType == MajorType.NEGATIVE_INTEGER.value) {
        this.writeTypeAndLength(MajorType.SEMANTIC_TAG.value, 3);
      } else {
        this.writeTypeAndLength(MajorType.SEMANTIC_TAG.value, 2);
      }
      byte[] bs = length.toByteArray();
      writeTypeAndLength(MajorType.BYTE_STRING.value, bs.length);
      write(bs);
    }
  }
}
