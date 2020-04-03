package org.dhallj.cbor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class Writer {
  private static final int BYTES_1 = 24;
  private static final int BYTES_2 = 25;
  private static final int BYTES_4 = 26;
  private static final int BYTES_8 = 27;
  private static final int MAJOR_TYPE_UNSIGNED_INTEGER = 0;
  private static final int MAJOR_TYPE_NEGATIVE_INTEGER = 1;
  private static final int MAJOR_TYPE_BYTE_STRING = 2;
  private static final int MAJOR_TYPE_TEXT_STRING = 3;
  private static final int MAJOR_TYPE_ARRAY = 4;;
  private static final int MAJOR_TYPE_MAP = 5;
  private static final int MAJOR_TYPE_TAG = 6;
  private static final int MAJOR_TYPE_SPECIAL = 7;
  private static final int FALSE = 244;
  private static final int TRUE = 245;
  private static final int NULL = 246;
  private static final BigInteger LONG_MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);
  private static final BigInteger BYTES_8_MAX_VALUE = new BigInteger("18446744073709551616");
  private static final Charset UTF_8 = Charset.forName("UTF-8");

  public final byte[] getBytes() throws IOException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    this.writeToStream(stream);
    return stream.toByteArray();
  }

  public abstract void writeToStream(OutputStream stream) throws IOException;

  public final boolean tryWriteToStream(OutputStream stream) {
    try {
      this.writeToStream(stream);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  private final void write(OutputStream stream, byte... bs) throws IOException {
    stream.write(bs);
  }

  protected final void writeNull(OutputStream stream) throws IOException {
    stream.write((byte) NULL);
  }

  protected final void writeBoolean(OutputStream stream, boolean value) throws IOException {
    stream.write((byte) (value ? TRUE : FALSE));
  }

  protected final void writeLong(OutputStream stream, long value) throws IOException {
    this.writeTypeAndLength(
        stream, (value >= 0) ? MAJOR_TYPE_UNSIGNED_INTEGER : MAJOR_TYPE_NEGATIVE_INTEGER, value);
  }

  protected final void writeBigInteger(OutputStream stream, BigInteger value) throws IOException {
    if (value.compareTo(BigInteger.ZERO) >= 0) {
      this.writeTypeAndLength(stream, MAJOR_TYPE_UNSIGNED_INTEGER, value);
    } else {
      this.writeTypeAndLength(
          stream, MAJOR_TYPE_NEGATIVE_INTEGER, value.add(BigInteger.ONE).negate());
    }
  }

  protected final void writeString(OutputStream stream, String value) throws IOException {
    byte[] bytes = value.getBytes(UTF_8);
    this.writeTypeAndLength(stream, MAJOR_TYPE_TEXT_STRING, bytes.length);
    this.write(stream, bytes);
  }

  protected final void writeByteString(OutputStream stream, byte[] bytes) throws IOException {
    this.writeTypeAndLength(stream, MAJOR_TYPE_BYTE_STRING, bytes.length);
    this.write(stream, bytes);
  }

  protected final void writeDouble(OutputStream stream, double value) throws IOException {
    int base = MAJOR_TYPE_SPECIAL << 5;

    if (Double.isNaN(value)) {
      this.write(stream, (byte) (base | BYTES_2), (byte) 126, (byte) 0);
    } else if (Double.isInfinite(value)) {
      if (Double.compare(value, 0) > 0) {
        this.write(stream, (byte) (base | BYTES_2), (byte) 124, (byte) 0);
      } else {
        this.write(stream, (byte) (base | BYTES_2), (byte) 252, (byte) 0);
      }
    } else if (Double.compare(value, 0.0) == 0) {
      this.write(stream, (byte) (base | BYTES_2), (byte) 0, (byte) 0);

    } else if (Double.compare(value, -0.0) == 0) {
      this.write(stream, (byte) (base | BYTES_2), (byte) 128, (byte) 0);
    } else {
      float asFloat = (float) value;
      if (value == (double) asFloat) {
        int bits = Float.floatToRawIntBits(asFloat);
        this.write(
            stream,
            (byte) (base | BYTES_4),
            (byte) ((bits >> 24) & 0xff),
            (byte) ((bits >> 16) & 0xff),
            (byte) ((bits >> 8) & 0xff),
            (byte) ((bits >> 0) & 0xff));
      } else {

        long bits = Double.doubleToRawLongBits(value);
        this.write(
            stream,
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

  protected final void writeArrayStart(OutputStream stream, int length) throws IOException {
    this.writeTypeAndLength(stream, MAJOR_TYPE_ARRAY, length);
  }

  protected final void writeMapStart(OutputStream stream, int length) throws IOException {
    this.writeTypeAndLength(stream, MAJOR_TYPE_MAP, length);
  }

  private final void writeTypeAndLength(OutputStream stream, int majorType, long length)
      throws IOException {
    int base = majorType << 5;

    if (length <= 23L) {
      stream.write((byte) (base | length));
    } else if (length < (1L << 8)) {
      this.write(stream, (byte) (base | BYTES_1), (byte) length);
    } else if (length < (1L << 16)) {
      this.write(stream, (byte) (base | BYTES_2), (byte) (length >> 8), (byte) (length & 0xff));
    } else if (length < (1L << 32)) {
      this.write(
          stream,
          (byte) (base | BYTES_4),
          (byte) ((length >> 24) & 0xff),
          (byte) ((length >> 16) & 0xff),
          (byte) ((length >> 8) & 0xff),
          (byte) (length & 0xff));
    } else {
      this.write(
          stream,
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

  private final void writeTypeAndLength(OutputStream stream, int majorType, BigInteger length)
      throws IOException {
    if (length.compareTo(LONG_MAX_VALUE) <= 0) {
      this.writeTypeAndLength(stream, majorType, length.longValue());
    } else if (length.compareTo(BYTES_8_MAX_VALUE) < 0) {
      int base = majorType << 5;
      BigInteger mask = BigInteger.valueOf(0xff);
      this.write(
          stream,
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
        this.writeTypeAndLength(stream, MAJOR_TYPE_TAG, 3);
      } else {
        this.writeTypeAndLength(stream, MAJOR_TYPE_TAG, 2);
      }
      byte[] bs = length.toByteArray();
      writeTypeAndLength(stream, MAJOR_TYPE_BYTE_STRING, bs.length);
      write(stream, bs);
    }
  }

  public static final class Nested extends Writer {
    private final Iterable<Writer> writers;

    public Nested(Iterable<Writer> writers) {
      this.writers = writers;
    }

    public void writeToStream(OutputStream stream) throws IOException {
      LinkedList<Iterator<Writer>> stack = new LinkedList<Iterator<Writer>>();
      Iterator<Writer> current = this.writers.iterator();

      do {
        if (current.hasNext()) {
          Writer next = current.next();

          if (next instanceof Nested) {
            stack.push(current);
            current = ((Nested) next).writers.iterator();
          } else {
            next.writeToStream(stream);
          }
        } else {
          current = stack.poll();
        }
      } while (current != null);
    }
  }
}
