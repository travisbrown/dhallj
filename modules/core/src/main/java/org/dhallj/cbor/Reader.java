package org.dhallj.cbor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of enough of the CBOR spec to cope with decoding the CBOR values we need for
 * Dhall.
 */
public abstract class Reader {
  private static final BigInteger TWO = new BigInteger("2");

  /** Only allow symbols that correspond to entire encoded Dhall expressions. */
  public final <R> R nextSymbol(Visitor<R> visitor) {
    skip55799();
    byte b = this.read();
    switch (MajorType.fromByte(b)) {
      case UNSIGNED_INTEGER:
        return visitor.onUnsignedInteger(readUnsignedInteger(b));
      case NEGATIVE_INTEGER:
        return visitor.onNegativeInteger(readNegativeInteger(b));
      case BYTE_STRING:
        return visitor.onByteString(readByteString(b));
      case TEXT_STRING:
        return visitor.onTextString(readTextString(b));
      case ARRAY:
        return readArrayStart(b, visitor);
      case MAP:
        return visitor.onMap(readMapStart(b));
      case SEMANTIC_TAG:
        throw new CborException("We should have skipped semantic tags");
      case PRIMITIVE:
        return readPrimitive(b, visitor);
      default:
        throw new CborException("Invalid CBOR major type " + Byte.toString(b));
    }
  }

  protected abstract byte read();

  protected abstract byte peek();

  protected abstract byte[] read(int count);

  public final BigInteger readUnsignedInteger() {
    skip55799();
    return readUnsignedInteger(read());
  }

  public final BigInteger readPositiveBigNum() {
    skip55799();
    BigInteger result = readBigNum();
    if (result.compareTo(BigInteger.ZERO) < 0) {
      throw new CborException(result.toString() + " is not a positive big num");
    } else {
      return result;
    }
  }

  public final BigInteger readBigNum() {
    skip55799();
    byte next = read();
    switch (MajorType.fromByte(next)) {
      case UNSIGNED_INTEGER:
        return readUnsignedInteger(next);
      case NEGATIVE_INTEGER:
        return readNegativeInteger(next);
      case SEMANTIC_TAG:
        AdditionalInfo info = AdditionalInfo.fromByte(next);
        BigInteger t = readBigInteger(info, next);
        long tag = t.longValue();
        BigInteger length = readUnsignedInteger();
        long len = length.longValue(); // Don't handle Bignums larger than this
        BigInteger result = readBigInteger(len);
        if (tag == 2) {
          return result;
        } else if (tag == 3) {
          return BigInteger.valueOf(-1).subtract(result);
        } else {
          throw new CborException(Long.toString(tag) + " is not a valid tag for a bignum");
        }
      default:
        throw new CborException(
            "Not a valid major type for an Unsigned Integer: "
                + MajorType.fromByte(next).toString());
    }
  }

  public final BigDecimal readBigDecimal() {
    skip55799();
    byte next = read();
    switch (MajorType.fromByte(next)) {
      case SEMANTIC_TAG:
        AdditionalInfo info = AdditionalInfo.fromByte(next);
        BigInteger t = readBigInteger(info, next);
        long tag = t.longValue();
        if (tag == 4) {
          BigInteger length = readArrayStart();
          if (length.equals(TWO)) {
            BigInteger rawScale = readBigNum();
            int scale = -rawScale.intValue();
            BigInteger unscaledValue = readBigNum();

            return new BigDecimal(unscaledValue, scale);
          } else {
            throw new CborException("Invalid decimal fraction");
          }
        } else {
          throw new CborException(
              Long.toString(tag) + " is not a valid tag for a decimal fraction");
        }
      default:
        throw new CborException("Next symbol is not a decimal fraction");
    }
  }

  public final String readNullableTextString() {
    skip55799();
    byte next = read();
    switch (MajorType.fromByte(next)) {
      case TEXT_STRING:
        return readTextString(next);
      case PRIMITIVE:
        return readPrimitive(next, NullVisitor.instanceForString);
      default:
        throw new CborException("Next symbol is neither a text string or null");
    }
  }

  public final byte[] readNullableByteString() {
    skip55799();
    byte next = read();
    switch (MajorType.fromByte(next)) {
      case BYTE_STRING:
        return readByteString(next);
      case PRIMITIVE:
        return readPrimitive(next, NullVisitor.instanceForByteArray);
      default:
        throw new CborException("Next symbol is neither a byte string or null");
    }
  }

  /**
   * This is unfortunate and horrible.
   *
   * <p>A hack to support decoding record projections, which are the only expressions which have a
   * CBOR representation where we don't know simply from the length of the array and the first
   * element what type of expression we're decoding - could be projection or projection by type
   */
  public final String tryReadTextString() {
    skip55799();
    byte next = peek();
    switch (MajorType.fromByte(next)) {
      case TEXT_STRING:
        return readTextString(read());
      default:
        return null;
    }
  }

  public final BigInteger readArrayStart() {
    skip55799();
    byte next = read();
    switch (MajorType.fromByte(next)) {
      case ARRAY:
        AdditionalInfo info = AdditionalInfo.fromByte(next);
        BigInteger length = readBigInteger(info, next);
        if (length.compareTo(BigInteger.ZERO) < 0) {
          throw new CborException("Indefinite array not needed for Dhall");
        } else {
          return length;
        }
      default:
        throw new CborException("Next symbol is not an array");
    }
  }

  public final <R> Map<String, R> readMap(Visitor<R> visitor) {
    skip55799();
    byte b = this.read();
    switch (MajorType.fromByte(b)) {
      case MAP:
        int length = readMapStart(b).intValue();
        Map<String, R> entries = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
          String key = readNullableTextString();
          R value = nextSymbol(visitor);
          entries.put(key, value);
        }
        return entries;
      default:
        throw new CborException(
            "Cannot read map - major type is " + MajorType.fromByte(b).toString());
    }
  }

  private final BigInteger readUnsignedInteger(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    return readBigInteger(info, b);
  }

  private final BigInteger readNegativeInteger(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    return BigInteger.valueOf(-1).subtract(readBigInteger(info, b));
  }

  private final byte[] readByteString(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info, b);
    if (length.compareTo(BigInteger.ZERO) < 0) {
      throw new CborException("Indefinite byte string not needed for Dhall");
    } else {
      // We don't handle the case where the length is > Integer.MaxValue
      return this.read(length.intValue());
    }
  }

  private final String readTextString(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info, b);
    if (length.compareTo(BigInteger.ZERO) < 0) {
      // Indefinite length - do we need this for Dhall?
      throw new CborException("Indefinite text string not needed for Dhall");
    } else {
      // We don't handle the case where the length is > Integer.MaxValue
      return new String(this.read(length.intValue()), Charset.forName("UTF-8"));
    }
  }

  private final <R> R readArrayStart(byte b, Visitor<R> visitor) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info, b);
    if (length.compareTo(BigInteger.ZERO) < 0) {
      throw new CborException("Indefinite array not needed for Dhall");
    } else {
      skip55799();
      byte next = read();
      switch (MajorType.fromByte(next)) {
        case UNSIGNED_INTEGER:
          return visitor.onArray(length, readUnsignedInteger(next));
        case TEXT_STRING:
          return visitor.onVariableArray(length, readTextString(next));
        default:
          throw new CborException(
              "Invalid start to CBOR-encoded Dhall expression " + MajorType.fromByte(b).toString());
      }
    }
  }

  private final BigInteger readMapStart(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info, b);
    if (length.compareTo(BigInteger.ZERO) < 0) {
      throw new CborException("Indefinite array not needed for Dhall");
    } else {
      return length;
    }
  }

  private static final String unassignedMessage(int v) {
    StringBuilder builder = new StringBuilder("Primitive ");
    builder.append(v);
    builder.append(" is unassigned");
    return builder.toString();
  }

  private static final String notValidMessage(int v) {
    StringBuilder builder = new StringBuilder("Primitive ");
    builder.append(v);
    builder.append(" is not valid");
    return builder.toString();
  }

  private final <R> R readPrimitive(byte b, Visitor<R> visitor) {
    int value = b & 31;
    if (0 <= value && value <= 19) {
      throw new CborException(unassignedMessage(value));
    } else if (value == 20) {
      return visitor.onFalse();
    } else if (value == 21) {
      return visitor.onTrue();
    } else if (value == 22) {
      return visitor.onNull();
    } else if (value == 23) {
      throw new CborException(unassignedMessage(value));
    } else if (value == 24) {
      throw new CborException("Simple value not needed for Dhall");
    } else if (value == 25) {
      // https://github.com/c-rack/cbor-java/blob/master/src/main/java/co/nstant/in/cbor/decoder/HalfPrecisionFloatDecoder.java
      int bits = 0;
      for (int i = 0; i < 2; i++) {
        int next = this.read() & 0xff;
        bits <<= 8;
        bits |= next;
      }

      return visitor.onHalfFloat(HalfFloat.toFloat(bits));
    } else if (value == 26) {
      int result = 0;
      for (int i = 0; i < 4; i++) {
        int next = this.read() & 0xff;
        result <<= 8;
        result |= next;
      }
      return visitor.onSingleFloat(Float.intBitsToFloat(result));
    } else if (value == 27) {
      long result = 0;
      for (int i = 0; i < 8; i++) {
        int next = this.read() & 0xff;
        result <<= 8;
        result |= next;
      }
      return visitor.onDoubleFloat(Double.longBitsToDouble(result));
    } else if (28 <= value && value <= 30) {
      throw new CborException(unassignedMessage(value));
    } else if (value == 31) {
      throw new CborException("Break stop code not needed for Dhall");
    } else {
      throw new CborException(notValidMessage(value));
    }
  }

  private final void skip55799() {
    byte next = peek();
    switch (MajorType.fromByte(next)) {
      case SEMANTIC_TAG:
        AdditionalInfo info = AdditionalInfo.fromByte(next);
        switch (info) {
          case DIRECT:
            return; // Don't advance pointer if it's a Bignum
          default:
            BigInteger tag = readBigInteger(info, read()); // Now advance pointer
            int t = tag.intValue();
            if (t != 55799) {
              throw new CborException("Unrecognized CBOR semantic tag " + Integer.toString(t));
            } else {
              skip55799(); // Please tell me no encoders do this
            }
        }
      default:
    }
  }

  private final BigInteger readBigInteger(AdditionalInfo info, byte first) {
    switch (info) {
      case DIRECT:
        return BigInteger.valueOf(first & 31);
      case ONE_BYTE:
        return readBigInteger(1);
      case TWO_BYTES:
        return readBigInteger(2);
      case FOUR_BYTES:
        return readBigInteger(4);
      case EIGHT_BYTES:
        return readBigInteger(8);
      case RESERVED:
        throw new CborException("Additional info RESERVED should not require reading a uintXX");
      case INDEFINITE:
        return BigInteger.valueOf(-1);
      default:
        throw new IllegalArgumentException("Invalid AdditionalInfo");
    }
  }

  private final BigInteger readBigInteger(long numBytes) {
    BigInteger result = BigInteger.ZERO;
    for (long i = 0; i < numBytes; i++) {
      int next = this.read() & 0xff;
      result = result.shiftLeft(8).or(BigInteger.valueOf(next));
    }
    return result;
  }

  public static final class ByteArrayReader extends Reader {
    private final byte[] bytes;
    private int cursor = 0;

    public ByteArrayReader(byte[] bytes) {
      this.bytes = bytes;
    }

    @Override
    protected byte read() {
      return this.bytes[this.cursor++];
    }

    @Override
    protected byte peek() {
      return this.bytes[this.cursor];
    }

    @Override
    protected byte[] read(int count) {
      byte[] bs = new byte[count];

      System.arraycopy(bytes, this.cursor, bs, 0, count);
      this.cursor += count;

      return bs;
    }
  }
}
