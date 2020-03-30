package org.dhallj.core;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Constructors for the Dhall expression abstract syntax tree.
 *
 * <p>Note that nothing in this file is public, and that custom code shouldn't be added here, since
 * this is generated from the visitor definition.
 */
class Constructors {
  static final class DoubleLiteral extends Expr {
    private final double value;

    DoubleLiteral(double value) {
      this.value = value;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onDoubleLiteral(this.value);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onDoubleLiteral(this.value);
    }
  }

  static final class NaturalLiteral extends Expr {
    private final BigInteger value;

    NaturalLiteral(BigInteger value) {
      this.value = value;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onNaturalLiteral(this.value);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onNaturalLiteral(this.value);
    }
  }

  static final class IntegerLiteral extends Expr {
    private final BigInteger value;

    IntegerLiteral(BigInteger value) {
      this.value = value;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onIntegerLiteral(this.value);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onIntegerLiteral(this.value);
    }
  }

  static final class TextLiteral extends Expr {
    private final String[] parts;
    private final Expr[] interpolated;

    TextLiteral(String[] parts, Expr[] interpolated) {
      this.parts = parts;
      this.interpolated = interpolated;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Iterable<Thunk<A>> interpolatedVisited =
          new ExprUtilities.MappedExprArray(visitor, this.interpolated);
      return visitor.onTextLiteral(this.parts, interpolatedVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onTextLiteral(this.parts, new ExprUtilities.IdentityArray<Expr>(interpolated));
    }
  }

  static final class Application extends Expr {
    private final Expr base;
    private final Expr arg;

    Application(Expr base, Expr arg) {
      this.base = base;
      this.arg = arg;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> baseVisited = new ExprUtilities.ExprThunk(visitor, this.base);
      Thunk<A> argVisited = new ExprUtilities.ExprThunk(visitor, this.arg);
      return visitor.onApplication(baseVisited, argVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onApplication(base, arg);
    }
  }

  static final class OperatorApplication extends Expr {
    private final Operator operator;
    private final Expr lhs;
    private final Expr rhs;

    OperatorApplication(Operator operator, Expr lhs, Expr rhs) {
      this.operator = operator;
      this.lhs = lhs;
      this.rhs = rhs;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> lhsVisited = new ExprUtilities.ExprThunk(visitor, this.lhs);
      Thunk<A> rhsVisited = new ExprUtilities.ExprThunk(visitor, this.rhs);
      return visitor.onOperatorApplication(this.operator, lhsVisited, rhsVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onOperatorApplication(this.operator, lhs, rhs);
    }
  }

  static final class If extends Expr {
    private final Expr cond;
    private final Expr thenValue;
    private final Expr elseValue;

    If(Expr cond, Expr thenValue, Expr elseValue) {
      this.cond = cond;
      this.thenValue = thenValue;
      this.elseValue = elseValue;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> condVisited = new ExprUtilities.ExprThunk(visitor, this.cond);
      Thunk<A> thenValueVisited = new ExprUtilities.ExprThunk(visitor, this.thenValue);
      Thunk<A> elseValueVisited = new ExprUtilities.ExprThunk(visitor, this.elseValue);
      return visitor.onIf(condVisited, thenValueVisited, elseValueVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onIf(cond, thenValue, elseValue);
    }
  }

  static final class Lambda extends Expr {
    private final String param;
    private final Expr input;
    private final Expr result;

    Lambda(String param, Expr input, Expr result) {
      this.param = param;
      this.input = input;
      this.result = result;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> inputVisited = new ExprUtilities.ExprThunk(visitor, this.input);
      Thunk<A> resultVisited = new ExprUtilities.ExprThunk(visitor, this.result);
      return visitor.onLambda(this.param, inputVisited, resultVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onLambda(this.param, input, result);
    }
  }

  static final class Pi extends Expr {
    private final String param;
    private final Expr input;
    private final Expr result;

    Pi(String param, Expr input, Expr result) {
      this.param = param;
      this.input = input;
      this.result = result;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> inputVisited = new ExprUtilities.ExprThunk(visitor, this.input);
      Thunk<A> resultVisited = new ExprUtilities.ExprThunk(visitor, this.result);
      return visitor.onPi(this.param, inputVisited, resultVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onPi(this.param, input, result);
    }
  }

  static final class Assert extends Expr {
    private final Expr base;

    Assert(Expr base) {
      this.base = base;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> baseVisited = new ExprUtilities.ExprThunk(visitor, this.base);
      return visitor.onAssert(baseVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onAssert(base);
    }
  }

  static final class FieldAccess extends Expr {
    private final Expr base;
    private final String fieldName;

    FieldAccess(Expr base, String fieldName) {
      this.base = base;
      this.fieldName = fieldName;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> baseVisited = new ExprUtilities.ExprThunk(visitor, this.base);
      return visitor.onFieldAccess(baseVisited, this.fieldName);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onFieldAccess(base, this.fieldName);
    }
  }

  static final class Projection extends Expr {
    private final Expr base;
    private final String[] fieldNames;

    Projection(Expr base, String[] fieldNames) {
      this.base = base;
      this.fieldNames = fieldNames;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> baseVisited = new ExprUtilities.ExprThunk(visitor, this.base);
      return visitor.onProjection(baseVisited, this.fieldNames);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onProjection(base, this.fieldNames);
    }
  }

  static final class ProjectionByType extends Expr {
    private final Expr base;
    private final Expr tpe;

    ProjectionByType(Expr base, Expr tpe) {
      this.base = base;
      this.tpe = tpe;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> baseVisited = new ExprUtilities.ExprThunk(visitor, this.base);
      Thunk<A> tpeVisited = new ExprUtilities.ExprThunk(visitor, this.tpe);
      return visitor.onProjectionByType(baseVisited, tpeVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onProjectionByType(base, tpe);
    }
  }

  static final class Identifier extends Expr {
    private final String value;
    private final long index;

    Identifier(String value, long index) {
      this.value = value;
      this.index = index;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onIdentifier(this.value, this.index);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onIdentifier(this.value, this.index);
    }
  }

  static final class RecordLiteral extends Expr {
    private final Entry<String, Expr>[] fields;

    RecordLiteral(Entry<String, Expr>[] fields) {
      this.fields = fields;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onRecordLiteral(
          new ExprUtilities.MappedEntryArray<A>(visitor, this.fields), fields.length);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onRecordLiteral(
          new ExprUtilities.IdentityArray<Entry<String, Expr>>(fields), fields.length);
    }
  }

  static final class RecordType extends Expr {
    private final Entry<String, Expr>[] fields;

    RecordType(Entry<String, Expr>[] fields) {
      this.fields = fields;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onRecordType(
          new ExprUtilities.MappedEntryArray<A>(visitor, this.fields), fields.length);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onRecordType(
          new ExprUtilities.IdentityArray<Entry<String, Expr>>(fields), fields.length);
    }
  }

  static final class UnionType extends Expr {
    private final Entry<String, Expr>[] fields;

    UnionType(Entry<String, Expr>[] fields) {
      this.fields = fields;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onUnionType(
          new ExprUtilities.MappedEntryArray<A>(visitor, this.fields), fields.length);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onUnionType(
          new ExprUtilities.IdentityArray<Entry<String, Expr>>(fields), fields.length);
    }
  }

  static final class NonEmptyListLiteral extends Expr {
    private final Expr[] values;

    NonEmptyListLiteral(Expr[] values) {
      this.values = values;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Iterable<Thunk<A>> valuesVisited = new ExprUtilities.MappedExprArray(visitor, this.values);
      return visitor.onNonEmptyListLiteral(valuesVisited, this.values.length);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onNonEmptyListLiteral(
          new ExprUtilities.IdentityArray<Expr>(values), this.values.length);
    }
  }

  static final class EmptyListLiteral extends Expr {
    private final Expr tpe;

    EmptyListLiteral(Expr tpe) {
      this.tpe = tpe;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> tpeVisited = new ExprUtilities.ExprThunk(visitor, this.tpe);
      return visitor.onEmptyListLiteral(tpeVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onEmptyListLiteral(tpe);
    }
  }

  static final class Let extends Expr {
    private final String name;
    private final Expr type;
    private final Expr value;
    private final Expr body;

    Let(String name, Expr type, Expr value, Expr body) {
      this.name = name;
      this.type = type;
      this.value = value;
      this.body = body;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> typeVisited = new ExprUtilities.ExprThunk(visitor, this.type);
      Thunk<A> valueVisited = new ExprUtilities.ExprThunk(visitor, this.value);
      Thunk<A> bodyVisited = new ExprUtilities.ExprThunk(visitor, this.body);
      return visitor.onLet(name, typeVisited, valueVisited, bodyVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onLet(name, type, value, body);
    }
  }

  static final class Annotated extends Expr {
    private final Expr base;
    private final Expr type;

    Annotated(Expr base, Expr type) {
      this.base = base;
      this.type = type;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> baseVisited = new ExprUtilities.ExprThunk(visitor, this.base);
      Thunk<A> typeVisited = new ExprUtilities.ExprThunk(visitor, this.type);
      return visitor.onAnnotated(baseVisited, typeVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onAnnotated(base, type);
    }
  }

  static final class ToMap extends Expr {
    private final Expr base;
    private final Expr type;

    ToMap(Expr base, Expr type) {
      this.base = base;
      this.type = type;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> baseVisited = new ExprUtilities.ExprThunk(visitor, this.base);
      Thunk<A> typeVisited = new ExprUtilities.ExprThunk(visitor, this.type);
      return visitor.onToMap(baseVisited, typeVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onToMap(base, type);
    }
  }

  static final class Merge extends Expr {
    private final Expr left;
    private final Expr right;
    private final Expr type;

    Merge(Expr left, Expr right, Expr type) {
      this.left = left;
      this.right = right;
      this.type = type;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> leftVisited = new ExprUtilities.ExprThunk(visitor, this.left);
      Thunk<A> rightVisited = new ExprUtilities.ExprThunk(visitor, this.right);
      Thunk<A> typeVisited = new ExprUtilities.ExprThunk(visitor, this.type);
      return visitor.onMerge(leftVisited, rightVisited, typeVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onMerge(left, right, type);
    }
  }

  static final class LocalImport extends Expr {
    private final Path path;
    private final Import.Mode mode;
    private final byte[] hash;

    LocalImport(Path path, Import.Mode mode, byte[] hash) {
      this.path = path;
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onLocalImport(this.path, this.mode, this.hash);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onLocalImport(this.path, this.mode, this.hash);
    }
  }

  static final class RemoteImport extends Expr {
    private final URI url;
    private final Import.Mode mode;
    private final byte[] hash;

    RemoteImport(URI url, Import.Mode mode, byte[] hash) {
      this.url = url;
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onRemoteImport(this.url, this.mode, this.hash);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onRemoteImport(this.url, this.mode, this.hash);
    }
  }

  static final class EnvImport extends Expr {
    private final String value;
    private final Import.Mode mode;
    private final byte[] hash;

    EnvImport(String value, Import.Mode mode, byte[] hash) {
      this.value = value;
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onEnvImport(this.value, this.mode, this.hash);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onEnvImport(this.value, this.mode, this.hash);
    }
  }

  static final class MissingImport extends Expr {
    private final Import.Mode mode;
    private final byte[] hash;

    MissingImport(Import.Mode mode, byte[] hash) {
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onMissingImport(this.mode, this.hash);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onMissingImport(this.mode, this.hash);
    }
  }
}
