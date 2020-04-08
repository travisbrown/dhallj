package org.dhallj.cbor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class Writer {
  private static final int FALSE = 244;
  private static final int TRUE = 245;
  private static final int NULL = 246;
  private static final BigInteger LONG_MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);
  private static final BigInteger EIGHT_BYTES_MAX_VALUE = new BigInteger("18446744073709551616");
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
        stream,
        (value >= 0) ? MajorType.UNSIGNED_INTEGER.value : MajorType.NEGATIVE_INTEGER.value,
        value);
  }

  protected final void writeBigInteger(OutputStream stream, BigInteger value) throws IOException {
    if (value.compareTo(BigInteger.ZERO) >= 0) {
      this.writeTypeAndLength(stream, MajorType.UNSIGNED_INTEGER.value, value);
    } else {
      this.writeTypeAndLength(
          stream, MajorType.NEGATIVE_INTEGER.value, value.add(BigInteger.ONE).negate());
    }
  }

  protected final void writeString(OutputStream stream, String value) throws IOException {
    byte[] bytes = value.getBytes(UTF_8);
    this.writeTypeAndLength(stream, MajorType.TEXT_STRING.value, bytes.length);
    this.write(stream, bytes);
  }

  protected final void writeByteString(OutputStream stream, byte[] bytes) throws IOException {
    this.writeTypeAndLength(stream, MajorType.BYTE_STRING.value, bytes.length);
    this.write(stream, bytes);
  }

  protected final void writeDouble(OutputStream stream, double value) throws IOException {
    int base = MajorType.PRIMITIVE.value << 5;

    if (Double.isNaN(value)) {
      this.write(stream, (byte) (base | AdditionalInfo.TWO_BYTES.value), (byte) 126, (byte) 0);
    } else if (Double.isInfinite(value)) {
      if (Double.compare(value, 0) > 0) {
        this.write(stream, (byte) (base | AdditionalInfo.TWO_BYTES.value), (byte) 124, (byte) 0);
      } else {
        this.write(stream, (byte) (base | AdditionalInfo.TWO_BYTES.value), (byte) 252, (byte) 0);
      }
    } else if (Double.compare(value, 0.0) == 0) {
      this.write(stream, (byte) (base | AdditionalInfo.TWO_BYTES.value), (byte) 0, (byte) 0);

    } else if (Double.compare(value, -0.0) == 0) {
      this.write(stream, (byte) (base | AdditionalInfo.TWO_BYTES.value), (byte) 128, (byte) 0);
    } else {
      float asFloat = (float) value;
      if (value == (double) asFloat) {
        int bits = Float.floatToRawIntBits(asFloat);
        this.write(
            stream,
            (byte) (base | AdditionalInfo.FOUR_BYTES.value),
            (byte) ((bits >> 24) & 0xff),
            (byte) ((bits >> 16) & 0xff),
            (byte) ((bits >> 8) & 0xff),
            (byte) ((bits >> 0) & 0xff));
      } else {

        long bits = Double.doubleToRawLongBits(value);
        this.write(
            stream,
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

  protected final void writeArrayStart(OutputStream stream, int length) throws IOException {
    this.writeTypeAndLength(stream, MajorType.ARRAY.value, length);
  }

  protected final void writeMapStart(OutputStream stream, int length) throws IOException {
    this.writeTypeAndLength(stream, MajorType.MAP.value, length);
  }

  private final void writeTypeAndLength(OutputStream stream, int majorType, long length)
      throws IOException {
    int base = majorType << 5;

    if (length <= 23L) {
      stream.write((byte) (base | length));
    } else if (length < (1L << 8)) {
      this.write(stream, (byte) (base | AdditionalInfo.ONE_BYTE.value), (byte) length);
    } else if (length < (1L << 16)) {
      this.write(
          stream,
          (byte) (base | AdditionalInfo.TWO_BYTES.value),
          (byte) (length >> 8),
          (byte) (length & 0xff));
    } else if (length < (1L << 32)) {
      this.write(
          stream,
          (byte) (base | AdditionalInfo.FOUR_BYTES.value),
          (byte) ((length >> 24) & 0xff),
          (byte) ((length >> 16) & 0xff),
          (byte) ((length >> 8) & 0xff),
          (byte) (length & 0xff));
    } else {
      this.write(
          stream,
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

  private final void writeTypeAndLength(OutputStream stream, int majorType, BigInteger length)
      throws IOException {
    if (length.compareTo(LONG_MAX_VALUE) <= 0) {
      this.writeTypeAndLength(stream, majorType, length.longValue());
    } else if (length.compareTo(EIGHT_BYTES_MAX_VALUE) < 0) {
      int base = majorType << 5;
      BigInteger mask = BigInteger.valueOf(0xff);
      this.write(
          stream,
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
        this.writeTypeAndLength(stream, MajorType.SEMANTIC_TAG.value, 3);
      } else {
        this.writeTypeAndLength(stream, MajorType.SEMANTIC_TAG.value, 2);
      }
      byte[] bs = length.toByteArray();
      writeTypeAndLength(stream, MajorType.BYTE_STRING.value, bs.length);
      write(stream, bs);
    }
  }

  public static final class Nested extends Writer {
    private final Iterable<Writer> writers;

    public Nested(Iterable<Writer> writers) {
      this.writers = writers;
    }

    public void writeToStream(OutputStream stream) throws IOException {
      Deque<Iterator<Writer>> stack = new LinkedList<Iterator<Writer>>();
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
