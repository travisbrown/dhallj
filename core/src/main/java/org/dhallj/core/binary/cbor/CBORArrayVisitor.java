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
    int tag = value.intValue();
    if (tag == 0) {
      return readFnApplication();
    } else if (tag == 1) {
      return readFunction();
    } else if (tag == 2) {
      return readPi();
    } else if (tag == 3) {
      return readOperator();
    } else if (tag == 4) {
      return readList();
    } else if (tag == 5) {
      return readSome();
    } else if (tag == 6) {
      return readMerge();
    } else if (tag == 7) {
      return readRecordType();
    } else if (tag == 8) {
      return readRecordLiteral();
    } else if (tag == 9) {
      return readFieldAccess();
    } else if (tag == 10) {
      return readProjection();
    } else if (tag == 11) {
      return readUnion();
    } else if (tag == 15) {
      return readNatural();
    } else if (tag == 16) {
      return readInteger();
    } else if (tag == 18) {
      return readTextLiteral();
    } else if (tag == 19) {
      return readAssert();
    } else if (tag == 24) {
      return readImport();
    } else if (tag == 25) {
      return readLet();
    } else if (tag == 26) {
      return readTypeAnnotation();
    } else if (tag == 27) {
      return readMap();
    } else if (tag == 28) {
      return readList();
    } else throw new RuntimeException(String.format("Array tag %d undefined", tag));
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
      Expr arg = decoder.nextSymbol(new CBORExpressionVisitor(decoder));
      args.add(arg);
    }
    return Expr.makeApplication(fn, args);
  }

  private Expr readFunction() {
    Expr fn = readExpr();
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readPi() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readOperator() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readList() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readSome() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readMerge() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readMap() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readRecordType() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readRecordLiteral() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readFieldAccess() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readProjection() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readUnion() {
    Expr fn = decoder.nextSymbol(this); // Pattern match again
    // TODO Check that this is a fn
    return fn;
  }

  private Expr readTypeAnnotation() {
    return null;
  }

  private Expr readLet() {
    return null;
  }

  private Expr readImport() {
    return null;
  }

  private Expr readAssert() {
    return null;
  }

  private Expr readTextLiteral() {
    return null;
  }

  private Expr readInteger() {
    return null;
  }

  private Expr readNatural() {
    return null;
  }

  // TODO should we have a separate visitor for this?
  private Expr readExpr() {
    return decoder.nextSymbol(new CBORExpressionVisitor(decoder));
  }

  private Expr notExpected(String msg) {
    throw new RuntimeException(
        "Expected String or UnsignedInteger as first element of array. Found " + msg);
  }
}
