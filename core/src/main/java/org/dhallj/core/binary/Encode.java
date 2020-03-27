package org.dhallj.core.binary;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.cbor.Writer;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Thunk;
import org.dhallj.core.Visitor;
import org.dhallj.core.ast.CollectApplication;
import org.dhallj.core.visitor.ConstantVisitor;
import org.dhallj.core.util.FieldUtilities;

public final class Encode extends ConstantVisitor.External<Void> {
  private final Writer writer;

  public Encode(Writer writer) {
    super(null);
    this.writer = writer;
  }

  private void apply(Expr expr) {
    expr.acceptExternal(this);
  }

  @Override
  public Void onDoubleLiteral(double value) {
    this.writer.writeDouble(value);
    return null;
  }

  @Override
  public Void onNaturalLiteral(BigInteger value) {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(15);
    this.writer.writeBigInteger(value);
    return null;
  }

  @Override
  public Void onIntegerLiteral(BigInteger value) {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(16);
    this.writer.writeBigInteger(value);
    return null;
  }

  @Override
  public Void onTextLiteral(String[] parts, Iterable<Expr> interpolated) {
    this.writer.writeArrayStart(parts.length * 2);
    this.writer.writeLong(18);

    Iterator<Expr> it = interpolated.iterator();
    for (String part : parts) {
      this.writer.writeString(part);
      if (it.hasNext()) {
        this.apply(it.next());
      }
    }
    return null;
  }

  @Override
  public Void onApplication(Expr base, Expr arg) {
    String asSimpleIdentifier = base.asSimpleIdentifier();

    if (asSimpleIdentifier != null && asSimpleIdentifier.equals("Some")) {
      this.writer.writeArrayStart(3);
      this.writer.writeLong(5);
      this.writer.writeNull();
      this.apply(arg);

    } else {

      List<Expr> parts = base.acceptExternal(new CollectApplication(base, arg));

      this.writer.writeArrayStart(parts.size() + 1);
      this.writer.writeLong(0);

      for (Expr part : parts) {
        part.acceptExternal(Encode.this);
      }
    }
    return null;
  }

  @Override
  public Void onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    this.writer.writeArrayStart(4);
    this.writer.writeLong(3);
    this.writer.writeLong(operator.getLabel());
    this.apply(lhs);
    this.apply(rhs);
    return null;
  }

  @Override
  public Void onIf(Expr cond, Expr thenValue, Expr elseValue) {
    this.writer.writeArrayStart(4);
    this.writer.writeLong(14);
    this.apply(cond);
    this.apply(thenValue);
    this.apply(elseValue);
    return null;
  }

  @Override
  public Void onLambda(String param, Expr input, Expr result) {
    if (param.equals("_")) {
      this.writer.writeArrayStart(3);
      this.writer.writeLong(1);
      this.apply(input);
      this.apply(result);
    } else {
      this.writer.writeArrayStart(4);
      this.writer.writeLong(1);
      this.writer.writeString(param);
      this.apply(input);
      this.apply(result);
    }
    return null;
  }

  @Override
  public Void onPi(String param, Expr input, Expr result) {
    if (param.equals("_")) {
      this.writer.writeArrayStart(3);
      this.writer.writeLong(2);
      this.apply(input);
      this.apply(result);
    } else {
      this.writer.writeArrayStart(4);
      this.writer.writeLong(2);
      this.writer.writeString(param);
      this.apply(input);
      this.apply(result);
    }
    return null;
  }

  @Override
  public Void onAssert(Expr base) {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(19);
    this.apply(base);
    return null;
  }

  @Override
  public Void onFieldAccess(Expr base, String fieldName) {

    this.writer.writeArrayStart(3);
    this.writer.writeLong(9);
    this.apply(base);
    this.writer.writeString(fieldName);
    return null;
  }

  @Override
  public Void onProjection(Expr base, String[] fieldNames) {

    this.writer.writeArrayStart(fieldNames.length + 2);
    this.writer.writeLong(10);
    this.apply(base);
    for (String fieldName : fieldNames) {
      this.writer.writeString(fieldName);
    }
    return null;
  }

  @Override
  public Void onProjectionByType(Expr base, Expr tpe) {

    this.writer.writeArrayStart(3);
    this.writer.writeLong(10);
    this.apply(base);
    this.writer.writeArrayStart(1);
    this.apply(tpe);
    return null;
  }

  @Override
  public Void onIdentifier(String value, long index) {

    if (value.equals("_")) {
      this.writer.writeLong(index);
    } else if (value.equals("True")) {
      this.writer.writeBoolean(true);
    } else if (value.equals("False")) {
      this.writer.writeBoolean(false);
    } else if (Expr.Constants.isBuiltInConstant(value)) {
      this.writer.writeString(value);
    } else {
      this.writer.writeArrayStart(2);
      this.writer.writeString(value);
      this.writer.writeLong(index);
    }
    return null;
  }

  @Override
  public Void onRecordLiteral(Iterable<Entry<String, Expr>> fields, int size) {

    this.writer.writeArrayStart(2);
    this.writer.writeLong(8);
    this.writer.writeMapStart(size);

    for (Entry<String, Expr> field : FieldUtilities.sortFields(fields, size)) {
      this.writer.writeString(field.getKey());
      field.getValue().acceptExternal(Encode.this);
    }
    return null;
  }

  @Override
  public Void onRecordType(Iterable<Entry<String, Expr>> fields, int size) {

    this.writer.writeArrayStart(2);
    this.writer.writeLong(7);
    this.writer.writeMapStart(size);

    for (Entry<String, Expr> field : FieldUtilities.sortFields(fields, size)) {
      this.writer.writeString(field.getKey());
      field.getValue().acceptExternal(Encode.this);
    }
    return null;
  }

  @Override
  public Void onUnionType(Iterable<Entry<String, Expr>> fields, int size) {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(11);
    this.writer.writeMapStart(size);

    for (Entry<String, Expr> field : FieldUtilities.sortFields(fields, size)) {
      this.writer.writeString(field.getKey());
      Expr value = field.getValue();

      if (value == null) {
        this.writer.writeNull();
      } else {
        this.apply(value);
      }
    }
    return null;
  }

  @Override
  public Void onNonEmptyListLiteral(Iterable<Expr> values, int size) {
    this.writer.writeArrayStart(size + 2);
    this.writer.writeLong(4);
    this.writer.writeNull();
    for (Expr value : values) {
      value.acceptExternal(Encode.this);
    }
    return null;
  }

  private static Visitor<Expr, Expr> getListType =
      new ConstantVisitor.External<Expr>(null) {
        @Override
        public Expr onApplication(Expr base, Expr arg) {
          if (base.asSimpleIdentifier().equals("List")) {
            return arg;
          } else {
            return null;
          }
        }
      };

  public Void onEmptyListLiteral(Expr tpe) {
    this.writer.writeArrayStart(2);

    Expr listType = tpe.acceptExternal(getListType);

    if (listType != null) {
      this.writer.writeLong(4);
      this.apply(listType);
    } else {
      this.writer.writeLong(28);
      this.apply(tpe);
    }

    return null;
  }

  /*public Void onLet(Iterable<Binding<Expr>> bindings, Expr body) {
    return Expr.makeLet(bindings, body);
  }

  /*public Void onAnnotated(Expr base, Expr tpe) {
    return Expr.makeAnnotated(base, tpe);
  }

  public Void onLocalImport(Path path, Import.Mode mode) {
    return Expr.makeLocalImport(path, mode);
  }

  public Void onRemoteImport(URI url, Import.Mode mode) {
    return Expr.makeRemoteImport(url, mode);
  }

  public Void onEnvImport(String value, Import.Mode mode) {
    return Expr.makeEnvImport(value, mode);
  }*/

  public Void onMissingImport(Import.Mode mode) {
    this.writer.writeArrayStart(4);
    this.writer.writeNull();
    this.writer.writeLong(0);
    this.writer.writeLong(7);
    return null;
  }
}
