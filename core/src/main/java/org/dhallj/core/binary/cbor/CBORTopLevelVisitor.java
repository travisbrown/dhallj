package org.dhallj.core.binary.cbor;

import org.dhallj.core.Expr;

import java.math.BigInteger;

/**
 * Pattern match against the top-level CBOR representation of Dhall expressions At this level,
 * expressions may be represented as arrays, strings, bools or doubles
 */
public final class CBORTopLevelVisitor implements Visitor<Expr> {

  private CBORDecoder decoder;

  public CBORTopLevelVisitor(CBORDecoder decoder) {
    this.decoder = decoder;
  }

  @Override
  public Expr onUnsignedInteger(BigInteger value) {
    return notExpected("Unsigned integer " + value);
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
    return Expr.makeIdentifier(value);
  }

  @Override
  public Expr onArray(BigInteger length) {
    return decoder.nextSymbol(new CBORArrayVisitor(decoder, length));
  }

  @Override
  public Expr onMap(BigInteger size) {
    return notExpected("Map");
  }

  @Override
  public Expr onFalse() {
    return Expr.makeIdentifier("False");
  }

  @Override
  public Expr onTrue() {
    return Expr.makeIdentifier("True");
  }

  @Override
  public Expr onNull() {
    return notExpected("Null");
  }

  @Override
  public Expr onHalfFloat(float value) {
    return Expr.makeDoubleLiteral(value);
  }

  @Override
  public Expr onSingleFloat(float value) {
    return Expr.makeDoubleLiteral(value);
  }

  @Override
  public Expr onDoubleFloat(double value) {
    return Expr.makeDoubleLiteral(value);
  }

  @Override
  public Expr onTag() {
    throw new RuntimeException("TODO");
  }

  private Expr notExpected(String msg) {
    throw new RuntimeException(
        "Expected String or UnsignedInteger as first element of array. Found " + msg);
  }
}
