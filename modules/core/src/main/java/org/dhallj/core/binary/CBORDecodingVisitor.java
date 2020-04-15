package org.dhallj.core.binary;

import org.dhallj.cbor.Reader;
import org.dhallj.cbor.Visitor;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Decodes CBOR expressions corresponding to encoded Dhall expressions.
 *
 * <p>Note that e.g. a negative integer by itself is an error, but a single float by itself is
 * allowed.
 */
final class CBORDecodingVisitor implements Visitor<Expr> {

  private final Reader reader;

  CBORDecodingVisitor(Reader reader) {
    this.reader = reader;
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
      throw new DecodingException("Variables must be encoded in an array of length 2");
    } else if (name.equals("_")) {
      throw new DecodingException("Variables cannot be explicitly named _");
    } else {
      BigInteger idx = this.reader.readPositiveBigNum();
      return Expr.makeIdentifier(name, idx.longValue());
    }
  }

  @Override
  public Expr onArray(BigInteger length, BigInteger tagI) {
    int tag = tagI.intValue();

    switch (tag) {
      case Label.APPLICATION:
        return readFnApplication(length);
      case Label.LAMBDA:
        return readFunction(length);
      case Label.PI:
        return readPi(length);
      case Label.OPERATOR_APPLICATION:
        return readOperator(length);
      case Label.LIST:
        return readList(length);
      case Label.SOME:
        return readSome(length);
      case Label.MERGE:
        return readMerge(length);
      case Label.RECORD_TYPE:
        return readRecordType(length);
      case Label.RECORD_LITERAL:
        return readRecordLiteral(length);
      case Label.FIELD_ACCESS:
        return readFieldAccess(length);
      case Label.PROJECTION:
        return readProjection(length);
      case Label.UNION_TYPE:
        return readUnion(length);
      case Label.IF:
        return readIf(length);
      case Label.NATURAL:
        return readNatural(length);
      case Label.INTEGER:
        return readInteger(length);
      case Label.TEXT:
        return readTextLiteral(length);
      case Label.ASSERT:
        return readAssert(length);
      case Label.IMPORT:
        return readImport(length);
      case Label.LET:
        return readLet(length);
      case Label.ANNOTATED:
        return readTypeAnnotation(length);
      case Label.TO_MAP:
        return readMap(length);
      case Label.EMPTY_LIST_WITH_ABSTRACT_TYPE:
        return readEmptyListAbstractType(length);
      default:
        throw new DecodingException(String.format("Array tag %d undefined", tag));
    }
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
      throw new DecodingException("Function application must have at least one argument");
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
      String param = this.reader.readNullableTextString();
      if (param.equals("_")) {
        throw new DecodingException(("Illegal explicit bound variable '_' in function"));
      }
      Expr tpe = readExpr();
      Expr resultTpe = readExpr();
      return Expr.makeLambda(param, tpe, resultTpe);
    } else {
      throw new DecodingException("Function types must be encoded in an array of length 3 or 4");
    }
  }

  private Expr readPi(BigInteger length) {
    long len = length.longValue();
    if (len == 3) {
      Expr tpe = readExpr();
      Expr resultTpe = readExpr();
      return Expr.makePi(tpe, resultTpe);
    } else if (len == 4) {
      String param = this.reader.readNullableTextString();
      if (param.equals("_")) {
        throw new DecodingException(("Illegal explicit bound variable '_' in pi type"));
      }
      Expr tpe = readExpr();
      Expr resultTpe = readExpr();
      return Expr.makePi(param, tpe, resultTpe);
    } else {
      throw new DecodingException("Pi types must be encoded in an array of length 3 or 4");
    }
  }

  private Expr readOperator(BigInteger length) {
    if (length.longValue() != 4) {
      throw new DecodingException("Operator application must be encoded in an array of length 4");
    }
    int operatorLabel = this.reader.readUnsignedInteger().intValue();
    Expr lhs = readExpr();
    Expr rhs = readExpr();

    Operator operator = Operator.fromLabel(operatorLabel);

    if (operator != null) {
      return Expr.makeOperatorApplication(operator, lhs, rhs);
    } else {
      throw new DecodingException(String.format("Operator tag %d is undefined", operatorLabel));
    }
  }

  private Expr readList(BigInteger length) {
    Expr tpe = readExpr();
    if (length.intValue() == 2) {
      if (tpe == null) {
        throw new DecodingException("Type must be specified if list is empty");
      } else {
        return Expr.makeEmptyListLiteral(Expr.makeApplication(Expr.Constants.LIST, tpe));
      }
    } else {
      if (tpe == null) {
        List<Expr> exprs = new ArrayList<>();
        for (int i = 2; i < length.longValue(); i++) {
          exprs.add(readExpr());
        }
        return Expr.makeNonEmptyListLiteral(exprs);
      } else {
        throw new DecodingException("Non-empty lists must not have a type annotation");
      }
    }
  }

  private Expr readEmptyListAbstractType(BigInteger length) {
    Expr tpe = readExpr();
    if (length.intValue() == 2) {
      if (tpe == null) {
        throw new DecodingException("Type must be specified if list is empty");
      } else {
        return Expr.makeEmptyListLiteral(tpe);
      }
    } else {
      throw new DecodingException(String.format("List of abstract type %s must be empty"));
    }
  }

  private Expr readSome(BigInteger length) {
    int len = length.intValue();
    if (len != 3) {
      throw new DecodingException("Some must be encoded in an array of length 3");
    } else {
      // The spec currently says "Some expressions store the type (if present) and their value", but
      // we ignore the type, and (I think) it should always be null.
      readExpr();
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
      throw new DecodingException("Merge must be encoded in an array of length 3 or 4");
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
      throw new DecodingException("ToMap must be encoded in an array of length 2 or 3");
    }
  }

  private Expr readRecordType(BigInteger length) {
    long len = length.longValue();
    if (len != 2) {
      throw new DecodingException("Record literal must be encoded in an array of length 2");
    } else {
      Map<String, Expr> entries = this.reader.readMap(this);
      return Expr.makeRecordType(entries.entrySet());
    }
  }

  private Expr readRecordLiteral(BigInteger length) {
    long len = length.longValue();
    if (len != 2) {
      throw new DecodingException("Record literal must be encoded in an array of length 2");
    } else {
      Map<String, Expr> entries = this.reader.readMap(this);
      return Expr.makeRecordLiteral(entries.entrySet());
    }
  }

  private Expr readFieldAccess(BigInteger length) {
    int len = length.intValue();
    if (len != 3) {
      throw new DecodingException("Field access must be encoded in array of length 3");
    } else {
      Expr e = readExpr();
      String field = this.reader.readNullableTextString();
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
      String first = this.reader.tryReadTextString();
      if (first != null) {
        List<String> fields = new ArrayList<>();
        fields.add(first);
        for (int i = 3; i < len; i++) {
          fields.add(this.reader.tryReadTextString());
        }
        return Expr.makeProjection(e, fields.toArray(new String[fields.size()]));
      } else {
        // It was actually an array
        int innerLen = this.reader.readArrayStart().intValue();
        if (innerLen != 1) {
          throw new DecodingException(
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
      throw new DecodingException("Union must be encoded in array of length 2");
    } else {
      Map<String, Expr> entries = this.reader.readMap(this);
      return Expr.makeUnionType(entries.entrySet());
    }
  }

  private Expr readIf(BigInteger length) {
    int len = length.intValue();
    if (len != 4) {
      throw new DecodingException("If must be encoded in an array of length 4");
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
      throw new DecodingException("Type annotation must be encoded in array of length 3");
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
      String name = this.reader.readNullableTextString();
      Expr tpe = readExpr();
      Expr value = readExpr();
      Expr body = readExpr();
      return Expr.makeLet(name, tpe, value, body);
    } else {
      String name = this.reader.readNullableTextString();
      Expr tpe = readExpr();
      Expr value = readExpr();
      return Expr.makeLet(name, tpe, value, readLet(len - 3));
    }
  }

  private Expr readImport(BigInteger length) {
    byte[] hash = this.reader.readNullableByteString();
    Expr.ImportMode mode = readMode();
    int tag = this.reader.readUnsignedInteger().intValue();

    switch (tag) {
      case Label.IMPORT_TYPE_REMOTE_HTTP:
        Expr httpUsing = readExpr();
        return readRemoteImport(length, mode, hash, "http:/", httpUsing);
      case Label.IMPORT_TYPE_REMOTE_HTTPS:
        Expr httpsUsing = readExpr();
        return readRemoteImport(length, mode, hash, "https:/", httpsUsing);
      case Label.IMPORT_TYPE_LOCAL_ABSOLUTE:
        return readLocalImport(length, mode, hash, "/");
      case Label.IMPORT_TYPE_LOCAL_HERE:
        return readLocalImport(length, mode, hash, "./");
      case Label.IMPORT_TYPE_LOCAL_PARENT:
        return readLocalImport(length, mode, hash, "../");
      case Label.IMPORT_TYPE_LOCAL_HOME:
        return readLocalImport(length, mode, hash, "~");
      case Label.IMPORT_TYPE_ENV:
        return readEnvImport(length, mode, hash);
      case Label.IMPORT_TYPE_MISSING:
        return Expr.makeMissingImport(mode, hash);
      case Label.IMPORT_TYPE_CLASSPATH:
        return readClasspathImport(length, mode, hash, "/");
      default:
        throw new DecodingException(String.format("Import type %d is undefined", tag));
    }
  }

  private Expr.ImportMode readMode() {
    int m = this.reader.readUnsignedInteger().intValue();
    if (m == 0) {
      return Expr.ImportMode.CODE;
    } else if (m == 1) {
      return Expr.ImportMode.RAW_TEXT;
    } else if (m == 2) {
      return Expr.ImportMode.LOCATION;
    } else {
      throw new DecodingException(String.format("Import mode %d is undefined", m));
    }
  }

  private Expr readLocalImport(
      BigInteger length, Expr.ImportMode mode, byte[] hash, String prefix) {
    Path path = Paths.get(prefix);
    int len = length.intValue();
    for (int i = 4; i < len; i++) {
      path = path.resolve(this.reader.readNullableTextString());
    }
    return Expr.makeLocalImport(path, mode, hash);
  }

  private Expr readClasspathImport(
      BigInteger length, Expr.ImportMode mode, byte[] hash, String prefix) {
    Path path = Paths.get(prefix);
    int len = length.intValue();
    for (int i = 4; i < len; i++) {
      path = path.resolve(this.reader.readNullableTextString());
    }
    return Expr.makeClasspathImport(path, mode, hash);
  }

  private Expr readRemoteImport(
      BigInteger length, Expr.ImportMode mode, byte[] hash, String prefix, Expr using) {
    StringBuilder builder = new StringBuilder(prefix);
    int len = length.intValue();
    for (int i = 5; i < len - 1; i++) {
      builder.append("/");
      builder.append(this.reader.readNullableTextString());
    }
    String query = this.reader.readNullableTextString();
    if (query != null) {
      builder.append("?");
      builder.append(query);
    }
    try {
      return Expr.makeRemoteImport(new URI(builder.toString()), using, mode, hash);
    } catch (URISyntaxException cause) {
      throw new DecodingException("Invalid URL in remote import", cause);
    }
  }

  private Expr readEnvImport(BigInteger length, Expr.ImportMode mode, byte[] hash) {
    String value = this.reader.readNullableTextString();
    return Expr.makeEnvImport(value, mode, hash);
  }

  private Expr readAssert(BigInteger length) {
    long len = length.longValue();
    if (len != 2) {
      throw new DecodingException("Assert must be encoded in array of length 2");
    } else {
      Expr e = readExpr();
      return Expr.makeAssert(e);
    }
  }

  private Expr readTextLiteral(BigInteger length) {
    List<String> lits = new ArrayList<>();
    List<Expr> exprs = new ArrayList<>();
    String lit = this.reader.readNullableTextString();
    lits.add(lit);
    for (int i = 2; i < length.longValue(); i += 2) {
      Expr e = readExpr();
      exprs.add(e);
      lit = this.reader.readNullableTextString();
      lits.add(lit);
    }
    return Expr.makeTextLiteral(lits.toArray(new String[0]), exprs.toArray(new Expr[0]));
  }

  private Expr readInteger(BigInteger length) {
    return Expr.makeIntegerLiteral(this.reader.readBigNum());
  }

  private Expr readNatural(BigInteger length) {
    return Expr.makeNaturalLiteral(this.reader.readPositiveBigNum());
  }

  private Expr readExpr() {
    return this.reader.nextSymbol(this);
  }

  private Expr notExpected(String msg) {
    throw new DecodingException(
        "Expected String or UnsignedInteger as first element of array. Found " + msg);
  }
}
