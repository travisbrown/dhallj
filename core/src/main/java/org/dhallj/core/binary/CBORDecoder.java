package org.dhallj.core.binary;

import org.dhallj.core.binary.CBORConstructors.*;
import org.dhallj.core.binary.CBORExpression.Constants.AdditionalInfo;
import org.dhallj.core.binary.CBORExpression.Constants.MajorType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of enough of the CBOR spec to cope with decoding the CBOR values we need for
 * Dhall
 */
public abstract class CBORDecoder {

  public CBORExpression decode() {
    byte b = this.read();
    switch (MajorType.fromByte(b)) {
      case UNSIGNED_INTEGER:
        return readUnsignedInteger(b);
      case NEGATIVE_INTEGER:
        return readNegativeInteger(b);
      case BYTE_STRING:
        return readByteString(b);
      case TEXT_STRING:
        return readTextString(b);
      case ARRAY:
        return readArray(b);
      case MAP:
        return readMap(b);
      case SEMANTIC_TAG:
        return readSemanticTag(b);
      case PRIMITIVE:
        return readPrimitive(b);
      default:
        throw new RuntimeException(String.format("Invalid CBOR major type %d", b));
    }
  }

  protected abstract byte read();

  protected abstract byte[] read(int count);

  private CBORExpression readUnsignedInteger(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    return new CBORUnsignedInteger(readBigInteger(info, b));
  }

  private CBORExpression readNegativeInteger(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    return new CBORNegativeInteger(readBigInteger(info, b).multiply(BigInteger.valueOf(-1)));
  }

  private CBORExpression readByteString(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info, b);
    if (length.compareTo(BigInteger.ZERO) < 0) {
      throw new RuntimeException("Indefinite byte string not needed for Dhall");
    } else {
      // We don't handle the case where the length is > Integer.MaxValue
      return new CBORByteString(this.read(length.intValue()));
    }
  }

  private CBORExpression readTextString(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info, b);
    if (length.compareTo(BigInteger.ZERO) < 0) {
      // Indefinite length - do we need this for Dhall?
      throw new RuntimeException("Indefinite text string not needed for Dhall");
    } else {
      // We don't handle the case where the length is > Integer.MaxValue
      return new CBORTextString(this.read(length.intValue()));
    }
  }

  private CBORExpression readArray(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info, b);
    if (length.compareTo(BigInteger.ZERO) < 0) {
      throw new RuntimeException("Indefinite array not needed for Dhall");
    } else {
      // We don't handle the case where the length is > Integer.MaxValue
      List<CBORExpression> exprs = new ArrayList<>(length.intValue());
      for (int i = 0; i < length.intValue(); i++) {
        exprs.add(decode());
      }
      return new CBORHeterogeneousArray(exprs);
    }
  }

  private CBORExpression readMap(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info, b);
    if (length.compareTo(BigInteger.ZERO) < 0) {
      throw new RuntimeException("Indefinite array not needed for Dhall");
    } else {
      // We don't handle the case where the length is > Integer.MaxValue
      Map<CBORExpression, CBORExpression> entries = new HashMap<>(length.intValue());
      for (int i = 0; i < length.intValue(); i++) {
        CBORExpression key = decode();
        CBORExpression value = decode();
        entries.put(key, value);
      }
      return new CBORHeterogeneousMap(entries);
    }
  }

  private CBORExpression readSemanticTag(byte b) {
    return null;
  }

  private CBORExpression readPrimitive(byte b) {
    int value = b & 31;
    if (0 <= value && value <= 19) {
      throw new RuntimeException(String.format("Primitive %d is unassigned", value));
    } else if (value == 20) {
      return new CBORFalse();
    } else if (value == 21) {
      return new CBORTrue();
    } else if (value == 22) {
      return new CBORNull();
    } else if (value == 23) {
      throw new RuntimeException(String.format("Primitive %d is unassigned", value));
    } else if (value == 24) {
      throw new RuntimeException("Simple value not needed for Dhall");
    } else if (value == 25) {
      // https://github.com/c-rack/cbor-java/blob/master/src/main/java/co/nstant/in/cbor/decoder/HalfPrecisionFloatDecoder.java
      int bits = 0;
      for (int i = 0; i < 2; i++) {
        int next = this.read() & 0xff;
        bits <<= 8;
        bits |= next;
      }
      int s = (bits & 0x8000) >> 15;
      int e = (bits & 0x7C00) >> 10;
      int f = bits & 0x03FF;

      float result = 0;
      if (e == 0) {
        result = (float) ((s != 0 ? -1 : 1) * Math.pow(2, -14) * (f / Math.pow(2, 10)));
      } else if (e == 0x1F) {
        result = f != 0 ? Float.NaN : (s != 0 ? -1 : 1) * Float.POSITIVE_INFINITY;
      } else {
        result = (float) ((s != 0 ? -1 : 1) * Math.pow(2, e - 15) * (1 + f / Math.pow(2, 10)));
      }
      return new CBORHalfFloat(result);
    } else if (value == 26) {
      int result = 0;
      for (int i = 0; i < 4; i++) {
        int next = this.read() & 0xff;
        result <<= 8;
        result |= next;
      }
      return new CBORSingleFloat(Float.intBitsToFloat(result));
    } else if (value == 27) {
      long result = 0;
      for (int i = 0; i < 4; i++) {
        int next = this.read() & 0xff;
        result <<= 8;
        result |= next;
      }
      return new CBORDoubleFloat(Double.longBitsToDouble(result));
    } else if (28 <= value && value <= 30) {
      throw new RuntimeException(String.format("Primitive %d is unassigned", value));
    } else if (value == 31) {
      throw new RuntimeException("Break stop code not needed for Dhall");
    } else {
      throw new RuntimeException(String.format("Primitive %d is not valid", value));
    }
  }

  private BigInteger readBigInteger(AdditionalInfo info, byte first) {
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
        throw new RuntimeException("Additional info RESERVED should not require reading a uintXX");
      case INDEFINITE:
        return BigInteger.valueOf(-1);
    }
    throw new RuntimeException("Why does Java not have exhaustivity checking?");
  }

  private BigInteger readBigInteger(int numBytes) {
    BigInteger result = BigInteger.ZERO;
    for (int i = 0; i < numBytes; i++) {
      int next = this.read() & 0xff;
      result = result.shiftLeft(8).and(BigInteger.valueOf(next));
    }
    return result;
  }

  private Long readLength() {
    return 0L;
  }

  public static final class ByteArrayCBORDecoder extends CBORDecoder {
    private final byte[] bytes;
    private int cursor = 0;

    public ByteArrayCBORDecoder(byte[] bytes) {
      this.bytes = bytes;
    }

    @Override
    protected byte read() {
      return this.bytes[this.cursor++];
    }

    @Override
    protected byte[] read(int count) {
      byte[] bs = new byte[count];

      System.arraycopy(bytes, this.cursor, bs, 0, count);
      this.cursor += count;

      return bytes;
    }
  }

  public static CBORExpression decode(byte[] bytes) {
    return new ByteArrayCBORDecoder(bytes).decode();
  }
}
