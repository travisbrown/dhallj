package org.dhallj.core.binary;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

final class CBORConstructors {
  static final class CBORUnsignedInteger extends CBORExpression {

    private final BigInteger value;

    CBORUnsignedInteger(BigInteger value) {
      this.value = value;
    }
  }

  static final class CBORNegativeInteger extends CBORExpression {

    private final BigInteger value;

    CBORNegativeInteger(BigInteger value) {
      this.value = value;
    }
  }

  static final class CBORByteString extends CBORExpression {

    private final byte[] value;

    CBORByteString(byte[] value) {
      this.value = value;
    }
  }

  static final class CBORTextString extends CBORExpression {

    private final byte[] value;

    CBORTextString(byte[] value) {
      this.value = value;
    }
  }

  static final class CBORHeterogeneousArray extends CBORExpression {

    private final List<CBORExpression> value;

    CBORHeterogeneousArray(List<CBORExpression> value) {
      this.value = value;
    }
  }

  static final class CBORHeterogeneousMap extends CBORExpression {

    private final Map<CBORExpression, CBORExpression> value;

    CBORHeterogeneousMap(Map<CBORExpression, CBORExpression> value) {
      this.value = value;
    }
  }

  static final class CBORFalse extends CBORExpression {}

  static final class CBORTrue extends CBORExpression {}

  static final class CBORNull extends CBORExpression {}

  static final class CBORHalfFloat extends CBORExpression {
    private final float value;

    CBORHalfFloat(float value) {
      this.value = value;
    }
  }

  static final class CBORSingleFloat extends CBORExpression {

    private final float value;

    CBORSingleFloat(float value) {
      this.value = value;
    }
  }

  static final class CBORDoubleFloat extends CBORExpression {

    private final double value;

    CBORDoubleFloat(double value) {
      this.value = value;
    }
  }

  static final class CBORUnsignedBignum extends CBORExpression {}

  static final class CBORNegativeBignum extends CBORExpression {}

  static final class CBORTag extends CBORExpression {}
}
