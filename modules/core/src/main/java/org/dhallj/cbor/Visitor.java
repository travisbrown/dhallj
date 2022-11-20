package org.dhallj.cbor;

import java.math.BigInteger;

/**
 * Represents a function from a CBOR expression to a value.
 *
 * @param R The result type
 */
public interface Visitor<R> {

  public R onUnsignedInteger(BigInteger value);

  public R onNegativeInteger(BigInteger value);

  public R onByteString(byte[] value);

  public R onTextString(String value);

  public R onVariableArray(BigInteger length, String name);

  public R onArray(BigInteger length, BigInteger tagI);

  public R onMap(BigInteger size);

  public R onFalse();

  public R onTrue();

  public R onNull();

  public R onHalfFloat(float value);

  public R onSingleFloat(float value);

  public R onDoubleFloat(double value);

  public R onTag();
}
