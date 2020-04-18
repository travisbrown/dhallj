package org.dhallj.core.binary;

import org.dhallj.cbor.Reader;
import org.dhallj.core.Expr;

public class Decode {
  public static final Expr decode(byte[] bytes) {
    Reader reader = new Reader.ByteArrayReader(bytes);
    // TODO check: if identifier then must be builtin using Expr.Constants.isBuiltInConstant
    Expr e = reader.nextSymbol(new CborDecodingVisitor(reader));
    return e;
  }
}
