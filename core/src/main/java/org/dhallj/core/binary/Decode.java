package org.dhallj.core.binary;

import org.dhallj.core.Expr;
import org.dhallj.core.binary.cbor.CBORDecoder;
import org.dhallj.core.binary.cbor.CBORTopLevelVisitor;

public class Decode {

  public static Expr decode(byte[] bytes) {
    CBORDecoder decoder = new CBORDecoder.ByteArrayCBORDecoder(bytes);
    // TODO check: if identifier then must be builtin using Expr.Constants.isBuiltInConstant
    Expr e = decoder.nextSymbol(new CBORTopLevelVisitor(decoder));
    return e;
  }
}
