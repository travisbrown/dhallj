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
final class Constructors {
  static final class NaturalLiteral extends Expr {
    final BigInteger value;

    NaturalLiteral(BigInteger value) {
      super(Tags.NATURAL);
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
    final BigInteger value;

    IntegerLiteral(BigInteger value) {
      super(Tags.INTEGER);
      this.value = value;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onIntegerLiteral(this.value);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onIntegerLiteral(this.value);
    }
  }

  static final class DoubleLiteral extends Expr {
    final double value;

    DoubleLiteral(double value) {
      super(Tags.DOUBLE);
      this.value = value;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onDoubleLiteral(this.value);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onDoubleLiteral(this.value);
    }
  }

  static final class TextLiteral extends Expr {
    final String[] parts;
    final Expr[] interpolated;

    TextLiteral(String[] parts, Expr[] interpolated) {
      super(Tags.TEXT);
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
    final Expr base;
    final Expr arg;

    Application(Expr base, Expr arg) {
      super(Tags.APPLICATION);
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
    final Operator operator;
    final Expr lhs;
    final Expr rhs;

    OperatorApplication(Operator operator, Expr lhs, Expr rhs) {
      super(Tags.OPERATOR_APPLICATION);
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
    final Expr predicate;
    final Expr thenValue;
    final Expr elseValue;

    If(Expr predicate, Expr thenValue, Expr elseValue) {
      super(Tags.IF);
      this.predicate = predicate;
      this.thenValue = thenValue;
      this.elseValue = elseValue;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> predicateVisited = new ExprUtilities.ExprThunk(visitor, this.predicate);
      Thunk<A> thenValueVisited = new ExprUtilities.ExprThunk(visitor, this.thenValue);
      Thunk<A> elseValueVisited = new ExprUtilities.ExprThunk(visitor, this.elseValue);
      return visitor.onIf(predicateVisited, thenValueVisited, elseValueVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onIf(predicate, thenValue, elseValue);
    }
  }

  static final class Lambda extends Expr {
    final String name;
    final Expr type;
    final Expr result;

    Lambda(String name, Expr type, Expr result) {
      super(Tags.LAMBDA);
      this.name = name;
      this.type = type;
      this.result = result;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> typeVisited = new ExprUtilities.ExprThunk(visitor, this.type);
      Thunk<A> resultVisited = new ExprUtilities.ExprThunk(visitor, this.result);
      return visitor.onLambda(this.name, typeVisited, resultVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onLambda(this.name, type, result);
    }
  }

  static final class Pi extends Expr {
    final String name;
    final Expr type;
    final Expr result;

    Pi(String name, Expr type, Expr result) {
      super(Tags.PI);
      this.name = name;
      this.type = type;
      this.result = result;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> typeVisited = new ExprUtilities.ExprThunk(visitor, this.type);
      Thunk<A> resultVisited = new ExprUtilities.ExprThunk(visitor, this.result);
      return visitor.onPi(this.name, typeVisited, resultVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onPi(this.name, type, result);
    }
  }

  static final class Assert extends Expr {
    final Expr base;

    Assert(Expr base) {
      super(Tags.ASSERT);
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
    final Expr base;
    final String fieldName;

    FieldAccess(Expr base, String fieldName) {
      super(Tags.FIELD_ACCESS);
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
    final Expr base;
    final String[] fieldNames;

    Projection(Expr base, String[] fieldNames) {
      super(Tags.PROJECTION);
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
    final Expr base;
    final Expr type;

    ProjectionByType(Expr base, Expr type) {
      super(Tags.PROJECTION_BY_TYPE);
      this.base = base;
      this.type = type;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> baseVisited = new ExprUtilities.ExprThunk(visitor, this.base);
      Thunk<A> tpeVisited = new ExprUtilities.ExprThunk(visitor, this.type);
      return visitor.onProjectionByType(baseVisited, tpeVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onProjectionByType(base, type);
    }
  }

  static final class BuiltIn extends Expr {
    final String name;

    BuiltIn(String name) {
      super(Tags.BUILT_IN);
      this.name = name;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onBuiltIn(this.name);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onBuiltIn(this.name);
    }
  }

  static final class Identifier extends Expr {
    final String name;
    final long index;

    Identifier(String name, long index) {
      super(Tags.IDENTIFIER);
      this.name = name;
      this.index = index;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onIdentifier(this.name, this.index);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onIdentifier(this.name, this.index);
    }
  }

  static final class RecordLiteral extends Expr {
    final Entry<String, Expr>[] fields;

    RecordLiteral(Entry<String, Expr>[] fields) {
      super(Tags.RECORD);
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
    final Entry<String, Expr>[] fields;

    RecordType(Entry<String, Expr>[] fields) {
      super(Tags.RECORD_TYPE);
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
    final Entry<String, Expr>[] fields;

    UnionType(Entry<String, Expr>[] fields) {
      super(Tags.UNION_TYPE);
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
    final Expr[] values;

    NonEmptyListLiteral(Expr[] values) {
      super(Tags.NON_EMPTY_LIST);
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
    final Expr type;

    EmptyListLiteral(Expr type) {
      super(Tags.EMPTY_LIST);
      this.type = type;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> tpeVisited = new ExprUtilities.ExprThunk(visitor, this.type);
      return visitor.onEmptyListLiteral(tpeVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onEmptyListLiteral(type);
    }
  }

  static final class Let extends Expr {
    final String name;
    final Expr type;
    final Expr value;
    final Expr body;

    Let(String name, Expr type, Expr value, Expr body) {
      super(Tags.LET);
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
    final Expr base;
    final Expr type;

    Annotated(Expr base, Expr type) {
      super(Tags.ANNOTATED);
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

  static final class Merge extends Expr {
    final Expr handlers;
    final Expr union;
    final Expr type;

    Merge(Expr handlers, Expr union, Expr type) {
      super(Tags.MERGE);
      this.handlers = handlers;
      this.union = union;
      this.type = type;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> handlersVisited = new ExprUtilities.ExprThunk(visitor, this.handlers);
      Thunk<A> unionVisited = new ExprUtilities.ExprThunk(visitor, this.union);
      Thunk<A> typeVisited = new ExprUtilities.ExprThunk(visitor, this.type);
      return visitor.onMerge(handlersVisited, unionVisited, typeVisited);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onMerge(handlers, union, type);
    }
  }

  static final class ToMap extends Expr {
    final Expr base;
    final Expr type;

    ToMap(Expr base, Expr type) {
      super(Tags.TO_MAP);
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

  static final class MissingImport extends Expr {
    final Import.Mode mode;
    final byte[] hash;

    MissingImport(Import.Mode mode, byte[] hash) {
      super(Tags.MISSING_IMPORT);
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

  static final class EnvImport extends Expr {
    final String name;
    final Import.Mode mode;
    final byte[] hash;

    EnvImport(String name, Import.Mode mode, byte[] hash) {
      super(Tags.ENV_IMPORT);
      this.name = name;
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      return visitor.onEnvImport(this.name, this.mode, this.hash);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onEnvImport(this.name, this.mode, this.hash);
    }
  }

  static final class LocalImport extends Expr {
    final Path path;
    final Import.Mode mode;
    final byte[] hash;

    LocalImport(Path path, Import.Mode mode, byte[] hash) {
      super(Tags.LOCAL_IMPORT);
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
    final URI url;
    final Expr using;
    final Import.Mode mode;
    final byte[] hash;

    RemoteImport(URI url, Expr using, Import.Mode mode, byte[] hash) {
      super(Tags.REMOTE_IMPORT);
      this.url = url;
      this.using = using;
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(Visitor<Thunk<A>, A> visitor) {
      Thunk<A> usingVisited = new ExprUtilities.ExprThunk(visitor, this.using);
      return visitor.onRemoteImport(this.url, usingVisited, this.mode, this.hash);
    }

    public final <A> A acceptExternal(Visitor<Expr, A> visitor) {
      return visitor.onRemoteImport(this.url, this.using, this.mode, this.hash);
    }
  }
}
