package org.dhallj.core.binary.cbor;

import org.dhallj.core.Expr;
import org.dhallj.core.Operator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
    if (length.longValue() < 3) {
      throw new RuntimeException("Function application must have at least one argument");
    }
    Expr fn = readFunction();
    ArrayList<Expr> args = new ArrayList<>();
    for (int i = 0; i < length.longValue() - 2; i++) {
      Expr arg = readExpr();
      args.add(arg);
    }
    return Expr.makeApplication(fn, args);
  }

  private Expr readFunction() {
    return readExpr();
  }

  private Expr readPi() {
    long len = length.longValue();
    if (len == 3) {
      Expr tpe = readExpr();
      Expr resultTpe = readExpr();
      return Expr.makePi(tpe, resultTpe);
    } else if (len == 4) {
      Expr param = readTextLiteral();
      if (param.equals("_")) {
        throw new RuntimeException(("Illegal explicit bound variable '_'"));
      }
      Expr tpe = readExpr();
      Expr resultTpe = readExpr();
      return Expr.makePi(tpe, resultTpe);
    } else {
      throw new RuntimeException("Pi types must be encoded in an array of length 3 or 4");
    }
  }

  private Expr readOperator() {
    if (length.longValue() != 4) {
      throw new RuntimeException("Operator application must be encoded in an array of length 4");
    }
    int op = decoder.readUnsignedInteger().intValue();
    Expr lhs = readExpr();
    Expr rhs = readExpr();
    if (op == 0) {
      return Expr.makeOperatorApplication(Operator.OR, lhs, rhs);
    } else if (op == 1) {
      return Expr.makeOperatorApplication(Operator.AND, lhs, rhs);
    } else if (op == 2) {
      return Expr.makeOperatorApplication(Operator.EQUALS, lhs, rhs);
    } else if (op == 3) {
      return Expr.makeOperatorApplication(Operator.NOT_EQUALS, lhs, rhs);
    } else if (op == 4) {
      return Expr.makeOperatorApplication(Operator.PLUS, lhs, rhs);
    } else if (op == 5) {
      return Expr.makeOperatorApplication(Operator.TIMES, lhs, rhs);
    } else if (op == 6) {
      return Expr.makeOperatorApplication(Operator.TEXT_APPEND, lhs, rhs);
    } else if (op == 7) {
      return Expr.makeOperatorApplication(Operator.LIST_APPEND, lhs, rhs);
    } else if (op == 8) {
      return Expr.makeOperatorApplication(Operator.COMBINE, lhs, rhs);
    } else if (op == 9) {
      return Expr.makeOperatorApplication(Operator.PREFER, lhs, rhs);
    } else if (op == 10) {
      return Expr.makeOperatorApplication(Operator.COMBINE_TYPES, lhs, rhs);
    } else if (op == 11) {
      return Expr.makeOperatorApplication(Operator.IMPORT_ALT, lhs, rhs);
    } else if (op == 12) {
      return Expr.makeOperatorApplication(Operator.EQUIVALENT, lhs, rhs);
    } else if (op == 13) {
      return Expr.makeOperatorApplication(Operator.COMPLETE, lhs, rhs);
    } else {
      throw new RuntimeException(String.format("Operator tag %d is undefined", op));
    }
  }

  private Expr readList() {
    Expr tpe = readExpr();
    if (length.intValue() == 2) {
      if (tpe == null) {
        throw new RuntimeException("Type must be specified if list is empty");
      } else {
        return Expr.makeEmptyListLiteral(tpe);
      }
    } else {
      List<Expr> exprs = new ArrayList<>();
      for (int i = 0; i < length.longValue(); i++) {
        exprs.add(readExpr());
      }
      return Expr.makeNonEmptyListLiteral(exprs);
    }
  }

  private Expr readSome() {
    Expr nullExpr = readExpr();
    Expr value = readExpr();
    return Expr.makeApplication(Expr.Constants.SOME, value);
  }

  private Expr readMerge() {
    int len = length.intValue();
    if (len == 3) {
      Expr l = readExpr();
      Expr r = readExpr();
      return Expr.makeMerge(l, r);
    } else if (len == 4) {
      Expr l = readExpr();
      Expr r = readExpr();
      Expr tpe = readExpr();
      return Expr.makeMerge(l, r, tpe);
    } else {
      throw new RuntimeException("Merge must be encoded in an array of length 3 or 4");
    }
  }

  private Expr readMap() {
    int len = length.intValue();
    if (len == 2) {
      Expr e = readExpr();
      return Expr.makeToMap(e);
    } else if (len == 3) {
      Expr e = readExpr();
      Expr tpe = readExpr();
      return Expr.makeToMap(e, tpe);
    } else {
      throw new RuntimeException("ToMap must be encoded in an array of length 2 or 3");
    }
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
    int len = length.intValue();
    if (len != 3) {
      throw new RuntimeException("Type annotation must be encoded in array of length 3");
    } else {
      Expr e = readExpr();
      Expr tpe = readExpr();
      return Expr.makeAnnotated(e, tpe);
    }
  }

  private Expr readLet() {
    return null;
  }

  private Expr readImport() {
    return null;
  }

  private Expr readAssert() {
    int len = length.intValue();
    if (len != 2) {
      throw new RuntimeException("Assert must be encoded in array of length 2");
    } else {
      Expr e = readExpr();
      return Expr.makeAssert(e);
    }
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
