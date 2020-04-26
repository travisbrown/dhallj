package org.dhallj.cbor;

/**
 * Conversions between 16 and 32-bit floating point numbers.
 *
 * @see <a href="https://stackoverflow.com/a/6162687/334519">this Stack Overflow answer</a>
 */
public final class HalfFloat {
  public static final int fromFloat(float asFloat) {
    int bits = Float.floatToIntBits(asFloat);
    int sign = bits >>> 16 & 0x8000; // sign only
    int value = (bits & 0x7fffffff) + 0x1000; // rounded value

    if (value >= 0x47800000) // might be or become NaN/Inf
    { // avoid Inf due to rounding

      if (value < 0x7f800000) // was value but too large
      return sign | 0x7c00; // make it +/-Inf
      return sign
          | 0x7c00
          | // remains +/-Inf or NaN
          (bits & 0x007fffff) >>> 13; // keep NaN (and Inf) bits
    }
    if (value >= 0x38800000) { // remains normalized value
      return sign | value - 0x38000000 >>> 13; // exp - 127 + 15
    }
    if (value < 0x33000000) { // too small for subnormal
      return sign; // becomes +/-0
    }
    value = (bits & 0x7fffffff) >>> 23; // tmp exp for subnormal calc

    return sign
        | ((bits & 0x7fffff | 0x800000) // add subnormal bit
                + (0x800000 >>> value - 102) // round depending on cut off
            >>> 126 - value); // div by 2^(1-(exp-127+15)) and >> 13 | exp=0
  }

  public static final float toFloat(int bits) {
    int mant = bits & 0x03ff; // 10 bits mantissa
    int exp = bits & 0x7c00; // 5 bits exponent
    if (exp == 0x7c00) { // NaN/Inf
      exp = 0x3fc00; // -> NaN/Inf
    } else if (exp != 0) { // normalized value
      exp += 0x1c000; // exp - 15 + 127
    } else if (mant != 0) { // && exp==0 -> subnormal
      exp = 0x1c400; // make it normal
      do {
        mant <<= 1; // mantissa * 2
        exp -= 0x400; // decrease exp by 1
      } while ((mant & 0x400) == 0); // while not normal
      mant &= 0x3ff; // discard subnormal bit
    } // else +/-0 -> +/-0
    return Float.intBitsToFloat( // combine all parts
        (bits & 0x8000) << 16 // sign  << ( 31 - 15 )
            | (exp | mant) << 13); // value << ( 23 - 10 )
  }
}
