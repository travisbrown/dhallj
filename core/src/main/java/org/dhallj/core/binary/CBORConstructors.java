package org.dhallj.core.binary;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public final class CBORConstructors {
  public static final class CBORUnsignedInteger extends CBORExpression {

    private final BigInteger value;

    CBORUnsignedInteger(BigInteger value) {
      this.value = value;
    }

    public BigInteger getValue() {
      return value;
    }
  }

  public static final class CBORNegativeInteger extends CBORExpression {

    private final BigInteger value;

    CBORNegativeInteger(BigInteger value) {
      this.value = value;
    }

    public BigInteger getValue() {
      return value;
    }
  }

  public static final class CBORByteString extends CBORExpression {

    private final byte[] value;

    CBORByteString(byte[] value) {
      System.out.println("In constructor, length is " + value.length);
      this.value = value;
      for (int i = 0; i < value.length; i++) {
        System.out.print(value[i] & 0xff);
        System.out.print(" ");
      }
      System.out.println("");
    }

    public byte[] getValue() {
      return value;
    }
  }

  public static final class CBORTextString extends CBORExpression {

    private final String value;

    CBORTextString(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  public static final class CBORHeterogeneousArray extends CBORExpression {

    private final List<CBORExpression> value;

    CBORHeterogeneousArray(List<CBORExpression> value) {
      this.value = value;
    }

    public List<CBORExpression> getValue() {
      return value;
    }
  }

  public static final class CBORHeterogeneousMap extends CBORExpression {

    private final Map<CBORExpression, CBORExpression> value;

    CBORHeterogeneousMap(Map<CBORExpression, CBORExpression> value) {
      this.value = value;
    }

    public Map<CBORExpression, CBORExpression> getValue() {
      return value;
    }
  }

  public static final class CBORFalse extends CBORExpression {}

  public static final class CBORTrue extends CBORExpression {}

  public static final class CBORNull extends CBORExpression {}

  public static final class CBORHalfFloat extends CBORExpression {
    private final float value;

    CBORHalfFloat(float value) {
      this.value = value;
    }

    public float getValue() {
      return value;
    }
  }

  public static final class CBORSingleFloat extends CBORExpression {

    private final float value;

    CBORSingleFloat(float value) {
      this.value = value;
    }

    public float getValue() {
      return value;
    }
  }

  public static final class CBORDoubleFloat extends CBORExpression {

    private final double value;

    CBORDoubleFloat(double value) {
      this.value = value;
    }

    public double getValue() {
      return value;
    }
  }

  public static final class CBORUnsignedBignum extends CBORExpression {}

  public static final class CBORNegativeBignum extends CBORExpression {}

  public static final class CBORTag extends CBORExpression {}
}
