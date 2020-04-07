package org.dhallj.core.binary.cbor;

import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Decodes CBOR expressions corresponding to encoded entire Dhall expressions (hence eg a negative
 * integer by itself is an error but a single float by itself is allowed)
 */
public class CBORExpressionVisitor implements Visitor<Expr> {

  private CBORDecoder decoder;

  public CBORExpressionVisitor(CBORDecoder decoder) {
    this.decoder = decoder;
  }

  @Override
  public Expr onUnsignedInteger(BigInteger value) {
    return Expr.makeIdentifier("_", value.longValue());
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
    return Expr.makeBuiltIn(value);
  }

  @Override
  public Expr onVariableArray(BigInteger length, String name) {
    if (length.intValue() != 2) {
      throw new RuntimeException("Variables must be encoded in an array of length 2");
    } else {
      BigInteger idx = decoder.readPositiveBigNum();
      return Expr.makeIdentifier(name, idx.longValue());
    }
  }

  @Override
  public Expr onArray(BigInteger length, BigInteger tagI) {
    int tag = tagI.intValue();
    if (tag == 0) {
      return readFnApplication(length);
    } else if (tag == 1) {
      return readFunction(length);
    } else if (tag == 2) {
      return readPi(length);
    } else if (tag == 3) {
      return readOperator(length);
    } else if (tag == 4) {
      return readList(length);
    } else if (tag == 5) {
      return readSome(length);
    } else if (tag == 6) {
      return readMerge(length);
    } else if (tag == 7) {
      return readRecordType(length);
    } else if (tag == 8) {
      return readRecordLiteral(length);
    } else if (tag == 9) {
      return readFieldAccess(length);
    } else if (tag == 10) {
      return readProjection(length);
    } else if (tag == 11) {
      return readUnion(length);
    } else if (tag == 14) {
      return readIf(length);
    } else if (tag == 15) {
      return readNatural(length);
    } else if (tag == 16) {
      return readInteger(length);
    } else if (tag == 18) {
      return readTextLiteral(length);
    } else if (tag == 19) {
      return readAssert(length);
    } else if (tag == 24) {
      return readImport(length);
    } else if (tag == 25) {
      return readLet(length);
    } else if (tag == 26) {
      return readTypeAnnotation(length);
    } else if (tag == 27) {
      return readMap(length);
    } else if (tag == 28) {
      return readEmptyListAbstractType(length);
    } else throw new RuntimeException(String.format("Array tag %d undefined", tag));
  }

  @Override
  public Expr onMap(BigInteger size) {
    return notExpected("Map");
  }

  @Override
  public Expr onFalse() {
    return Expr.Constants.FALSE;
  }

  @Override
  public Expr onTrue() {
    return Expr.Constants.TRUE;
  }

  @Override
  public Expr onNull() {
    return null;
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
    // TODO
    return notExpected("Tag");
  }

  private Expr readFnApplication(BigInteger length) {
    if (length.longValue() < 3) {
      throw new RuntimeException("Function application must have at least one argument");
    }
    Expr fn = readExpr();
    ArrayList<Expr> args = new ArrayList<>();
    for (int i = 0; i < length.longValue() - 2; i++) {
      Expr arg = readExpr();
      args.add(arg);
    }
    return Expr.makeApplication(fn, args);
  }

  private Expr readFunction(BigInteger length) {
    long len = length.longValue();
    if (len == 3) {
      Expr tpe = readExpr();
      Expr result = readExpr();
      return Expr.makeLambda("_", tpe, result);
    } else if (len == 4) {
      String param = decoder.readNullableTextString();
      if (param.equals("_")) {
        throw new RuntimeException(("Illegal explicit bound variable '_' in function"));
      }
      Expr tpe = readExpr();
      Expr resultTpe = readExpr();
      return Expr.makeLambda(param, tpe, resultTpe);
    } else {
      throw new RuntimeException("Function types must be encoded in an array of length 3 or 4");
    }
  }

  private Expr readPi(BigInteger length) {
    long len = length.longValue();
    if (len == 3) {
      Expr tpe = readExpr();
      Expr resultTpe = readExpr();
      return Expr.makePi(tpe, resultTpe);
    } else if (len == 4) {
      String param = decoder.readNullableTextString();
      if (param.equals("_")) {
        throw new RuntimeException(("Illegal explicit bound variable '_' in pi type"));
      }
      Expr tpe = readExpr();
      Expr resultTpe = readExpr();
      return Expr.makePi(param, tpe, resultTpe);
    } else {
      throw new RuntimeException("Pi types must be encoded in an array of length 3 or 4");
    }
  }

  private Expr readOperator(BigInteger length) {
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

  private Expr readList(BigInteger length) {
    Expr tpe = readExpr();
    if (length.intValue() == 2) {
      if (tpe == null) {
        throw new RuntimeException("Type must be specified if list is empty");
      } else {
        return Expr.makeEmptyListLiteral(Expr.makeApplication(Expr.Constants.LIST, tpe));
      }
    } else {
      List<Expr> exprs = new ArrayList<>();
      for (int i = 2; i < length.longValue(); i++) {
        exprs.add(readExpr());
      }
      return Expr.makeNonEmptyListLiteral(exprs);
    }
  }

  private Expr readEmptyListAbstractType(BigInteger length) {
    Expr tpe = readExpr();
    if (length.intValue() == 2) {
      if (tpe == null) {
        throw new RuntimeException("Type must be specified if list is empty");
      } else {
        return Expr.makeEmptyListLiteral(tpe);
      }
    } else {
      throw new RuntimeException(String.format("List of abstract type %s must be empty"));
    }
  }

  private Expr readSome(BigInteger length) {
    int len = length.intValue();
    if (len != 3) {
      throw new RuntimeException("Some must be encoded in an array of length 3");
    } else {
      Expr tpe = readExpr();
      Expr value = readExpr();
      return Expr.makeApplication(Expr.Constants.SOME, value);
    }
  }

  private Expr readMerge(BigInteger length) {
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

  private Expr readMap(BigInteger length) {
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

  private Expr readRecordType(BigInteger length) {
    long len = length.longValue();
    if (len != 2) {
      throw new RuntimeException("Record literal must be encoded in an array of length 2");
    } else {
      Map<String, Expr> entries = decoder.readMap(this);
      return Expr.makeRecordType(entries.entrySet());
    }
  }

  private Expr readRecordLiteral(BigInteger length) {
    long len = length.longValue();
    if (len != 2) {
      throw new RuntimeException("Record literal must be encoded in an array of length 2");
    } else {
      Map<String, Expr> entries = decoder.readMap(this);
      return Expr.makeRecordLiteral(entries.entrySet());
    }
  }

  private Expr readFieldAccess(BigInteger length) {
    int len = length.intValue();
    if (len != 3) {
      throw new RuntimeException("Field access must be encoded in array of length 3");
    } else {
      Expr e = readExpr();
      String field = decoder.readNullableTextString();
      return Expr.makeFieldAccess(e, field);
    }
  }

  private Expr readProjection(BigInteger length) {
    long len = length.longValue();
    Expr e = readExpr();
    if (len == 2) {
      return Expr.makeProjection(e, new String[0]);
    } else {
      // This is horrible but so is the encoding of record projections - we don't know whether
      // we expect an array or Strings next
      String first = decoder.tryReadTextString();
      if (first != null) {
        List<String> fields = new ArrayList<>();
        fields.add(first);
        for (int i = 3; i < len; i++) {
          fields.add(decoder.tryReadTextString());
        }
        return Expr.makeProjection(e, fields.toArray(new String[fields.size()]));
      } else {
        // It was actually an array
        int innerLen = decoder.readArrayStart().intValue();
        if (innerLen != 1) {
          throw new RuntimeException(
              "Type for type  projection must be encoded in an array of length 1");
        } else {
          Expr tpe = readExpr();
          return Expr.makeProjectionByType(e, tpe);
        }
      }
    }
  }

  private Expr readUnion(BigInteger length) {
    int len = length.intValue();
    if (len != 2) {
      throw new RuntimeException("Union must be encoded in array of length 2");
    } else {
      Map<String, Expr> entries = decoder.readMap(this);
      return Expr.makeUnionType(entries.entrySet());
    }
  }

  private Expr readIf(BigInteger length) {
    int len = length.intValue();
    if (len != 4) {
      throw new RuntimeException("If must be encoded in an array of length 4");
    } else {
      Expr cond = readExpr();
      Expr ifE = readExpr();
      Expr elseE = readExpr();
      return Expr.makeIf(cond, ifE, elseE);
    }
  }

  private Expr readTypeAnnotation(BigInteger length) {
    int len = length.intValue();
    if (len != 3) {
      throw new RuntimeException("Type annotation must be encoded in array of length 3");
    } else {
      Expr e = readExpr();
      Expr tpe = readExpr();
      return Expr.makeAnnotated(e, tpe);
    }
  }

  private Expr readLet(BigInteger length) {
    return readLet(length.longValue());
  }

  private Expr readLet(long len) {
    if (len == 5) {
      String name = decoder.readNullableTextString();
      Expr tpe = readExpr();
      Expr value = readExpr();
      Expr body = readExpr();
      return Expr.makeLet(name, tpe, value, body);
    } else {
      String name = decoder.readNullableTextString();
      Expr tpe = readExpr();
      Expr value = readExpr();
      return Expr.makeLet(name, tpe, value, readLet(len - 3));
    }
  }

  private Expr readImport(BigInteger length) {
    byte[] hash = decoder.readNullableByteString();
    Import.Mode mode = readMode();
    int tag = decoder.readUnsignedInteger().intValue();
    if (tag == 0) {
      Expr using = readExpr();
      return readRemoteImport(length, mode, hash, "http:/", using);
    } else if (tag == 1) {
      Expr using = readExpr();
      return readRemoteImport(length, mode, hash, "https:/", using);
    } else if (tag == 2) {
      return readLocalImport(length, mode, hash, "/");
    } else if (tag == 3) {
      return readLocalImport(length, mode, hash, "./");
    } else if (tag == 4) {
      return readLocalImport(length, mode, hash, "../");
    } else if (tag == 5) {
      return readLocalImport(length, mode, hash, "~");
    } else if (tag == 6) {
      return readEnvImport(length, mode, hash);
    } else if (tag == 7) {
      return Expr.makeMissingImport(mode, hash);
    } else {
      throw new RuntimeException(String.format("Import type %d is undefined", tag));
    }
  }

  private Import.Mode readMode() {
    int m = decoder.readUnsignedInteger().intValue();
    if (m == 0) {
      return Import.Mode.CODE;
    } else if (m == 1) {
      return Import.Mode.RAW_TEXT;
    } else if (m == 2) {
      return Import.Mode.LOCATION;
    } else {
      throw new RuntimeException(String.format("Import mode %d is undefined", m));
    }
  }

  private Expr readLocalImport(BigInteger length, Import.Mode mode, byte[] hash, String prefix) {
    Path path = Paths.get(prefix);
    int len = length.intValue();
    for (int i = 4; i < len; i++) {
      path = path.resolve(decoder.readNullableTextString());
    }
    return Expr.makeLocalImport(path, mode, hash);
  }

  private Expr readRemoteImport(
      BigInteger length, Import.Mode mode, byte[] hash, String prefix, Expr using) {
    String uri = prefix;
    int len = length.intValue();
    for (int i = 5; i < len - 1; i++) {
      uri = uri + "/" + decoder.readNullableTextString();
    }
    String query = decoder.readNullableTextString();
    if (query != null) {
      uri = uri + "?" + query;
    }
    try {
      return Expr.makeRemoteImport(new URI(uri), using, mode, hash);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Expr readEnvImport(BigInteger length, Import.Mode mode, byte[] hash) {
    String value = decoder.readNullableTextString();
    return Expr.makeEnvImport(value, mode, hash);
  }

  private Expr readAssert(BigInteger length) {
    long len = length.longValue();
    if (len != 2) {
      throw new RuntimeException("Assert must be encoded in array of length 2");
    } else {
      Expr e = readExpr();
      return Expr.makeAssert(e);
    }
  }

  private Expr readTextLiteral(BigInteger length) {
    List<String> lits = new ArrayList<>();
    List<Expr> exprs = new ArrayList<>();
    String lit = decoder.readNullableTextString();
    lits.add(lit);
    for (int i = 2; i < length.longValue(); i += 2) {
      Expr e = readExpr();
      exprs.add(e);
      lit = decoder.readNullableTextString();
      lits.add(lit);
    }
    return Expr.makeTextLiteral(lits.toArray(new String[0]), exprs.toArray(new Expr[0]));
  }

  private Expr readInteger(BigInteger length) {
    return Expr.makeIntegerLiteral(decoder.readBigNum());
  }

  private Expr readNatural(BigInteger length) {
    return Expr.makeNaturalLiteral(decoder.readPositiveBigNum());
  }

  private Expr readExpr() {
    return decoder.nextSymbol(this);
  }

  private Expr notExpected(String msg) {
    throw new RuntimeException(
        "Expected String or UnsignedInteger as first element of array. Found " + msg);
  }
}
