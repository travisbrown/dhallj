package org.dhallj.cbor;

import java.math.BigInteger;

/** To read a CBOR primitive and ensure it is null. */
final class NullVisitor<R> implements Visitor<R> {
  static final Visitor<String> instanceForString = new NullVisitor<String>();
  static final Visitor<byte[]> instanceForByteArray = new NullVisitor<byte[]>();

  @Override
  public R onUnsignedInteger(BigInteger value) {
    return notExpected("Unsigned integer");
  }

  @Override
  public R onNegativeInteger(BigInteger value) {
    return notExpected("Negative integer");
  }

  @Override
  public R onByteString(byte[] value) {
    return notExpected("Byte string");
  }

  @Override
  public R onTextString(String value) {
    return notExpected("Text string");
  }

  @Override
  public R onVariableArray(BigInteger length, String name) {
    return notExpected("Variable array");
  }

  @Override
  public R onArray(BigInteger length, BigInteger tagI) {
    return notExpected("Array");
  }

  @Override
  public R onMap(BigInteger size) {
    return notExpected("Map");
  }

  @Override
  public R onFalse() {
    return notExpected("False");
  }

  @Override
  public R onTrue() {
    return notExpected("True");
  }

  @Override
  public R onNull() {
    return null;
  }

  @Override
  public R onHalfFloat(float value) {
    return notExpected("Half float");
  }

  @Override
  public R onSingleFloat(float value) {
    return notExpected("Single float");
  }

  @Override
  public R onDoubleFloat(double value) {
    return notExpected("Double float");
  }

  @Override
  public R onTag() {
    return null;
  }

  private R notExpected(String msg) {
    throw new CborException(msg + " not expected - expected null");
  }
}
