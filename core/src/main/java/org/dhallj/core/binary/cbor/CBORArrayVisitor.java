package org.dhallj.core.binary.cbor;

import org.dhallj.core.Expr;

import java.math.BigInteger;
import java.util.ArrayList;

/** Pattern match on the first element of an array and then read the expected expression(s) */
public class CBORArrayVisitor implements Visitor<Expr> {

  private CBORDecoder decoder;
  private BigInteger length;

  public CBORArrayVisitor(CBORDecoder decoder, BigInteger length) {
    this.decoder = decoder;
    this.length = length;
  }

  // Needs to hold CBOR decoder so that it can keep reading symbols

  @Override
  public Expr onUnsignedInteger(BigInteger value) {
    // dispatch on value of int and read expected expression
    int type = value.intValue();
    if (type == 0) {
      return readFnApplication();
    }
    return null;
  }

  @Override
  public Expr onNegativeInteger(BigInteger value) {
    return notExpected("Negative integer " + value);
  }

  @Override
  public Expr onByteString(byte[] value) {
    return notExpected("ByteString");
  }

  @Override
  public Expr onTextString(String value) {
    // Read and return variable
    return null;
  }

  @Override
  public Expr onArray(BigInteger length) {
    return notExpected("Array");
  }

  @Override
  public Expr onMap(BigInteger size) {
    return notExpected("Map");
  }

  @Override
  public Expr onFalse() {
    return notExpected("False");
  }

  @Override
  public Expr onTrue() {
    return notExpected("True");
  }

  @Override
  public Expr onNull() {
    return notExpected("Null");
  }

  @Override
  public Expr onHalfFloat(float value) {
    return notExpected("Half float " + value);
  }

  @Override
  public Expr onSingleFloat(float value) {
    return notExpected("Single float " + value);
  }

  @Override
  public Expr onDoubleFloat(double value) {
    return notExpected("Double float " + value);
  }

  @Override
  public Expr onTag() {
    return notExpected("Tag");
  }

  private Expr readFnApplication() {
    Expr fn = readFunction();
    ArrayList<Expr> args = new ArrayList<>();
    for (int i = 0; i < length.intValue() - 2; i++) {
      Expr arg = decoder.nextSymbol(new CBORTopLevelVisitor(decoder));
      args.add(arg);
    }
    return Expr.makeApplication(fn, args);
  }

  private Expr readFunction() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // Check that this is a fn
    return fn;
  }

  private Expr notExpected(String msg) {
    throw new RuntimeException(
        "Expected String or UnsignedInteger as first element of array. Found " + msg);
  }
}
