package org.dhallj.core.binary.cbor;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface Visitor<R> {

  public R onUnsignedInteger(BigInteger value);

  public R onNegativeInteger(BigInteger value);

  public R onByteString(byte[] value);

  public R onTextString(String value);

  public R onArray(BigInteger length);

  public R onMap(BigInteger size);

  public R onFalse();

  public R onTrue();

  public R onNull();

  public R onHalfFloat(float value);

  public R onSingleFloat(float value);

  public R onDoubleFloat(double value);

  public R onTag();
}
