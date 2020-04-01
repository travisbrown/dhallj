package org.dhallj.core.binary;

import org.dhallj.core.Expr;
import org.dhallj.core.binary.CBORConstructors.*;
import org.dhallj.core.binary.CBORExpression.Constants.AdditionalInfo;
import org.dhallj.core.binary.CBORExpression.Constants.MajorType;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO need the following CBOR types: array, string,
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
    return new CBORUnsignedInteger(readBigInteger(info));
  }

  private CBORExpression readNegativeInteger(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    return new CBORNegativeInteger(readBigInteger(info).multiply(BigInteger.valueOf(-1)));
  }

  private CBORExpression readByteString(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info);
    if (length.compareTo(BigInteger.ZERO) < 0) {
      throw new RuntimeException("Indefinite byte string not needed for Dhall");
    } else {
      // We don't handle the case where the length is > Integer.MaxValue
      return new CBORByteString(this.read(length.intValue()));
    }
  }

  private CBORExpression readTextString(byte b) {
    AdditionalInfo info = AdditionalInfo.fromByte(b);
    BigInteger length = readBigInteger(info);
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
    BigInteger length = readBigInteger(info);
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
    BigInteger length = readBigInteger(info);
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
    return null;
  }

  private BigInteger readBigInteger(AdditionalInfo info) {
    switch (info) {
      case DIRECT:
        return BigInteger.valueOf(this.read() & 31);
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

  public static final class ByteArrayDecoder extends CBORDecoder {
    private final byte[] bytes;
    private int cursor = 0;

    public ByteArrayDecoder(byte[] bytes) {
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
    return new ByteArrayDecoder(bytes).decode();
  }
}
