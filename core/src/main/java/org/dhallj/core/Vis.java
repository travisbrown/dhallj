package org.dhallj.core;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;

/**
 * Represents a function from a Dhall expression to a value that recurses through the structure of
 * the expression.
 *
 * @param A The final result type
 */
public interface Vis<A> {
  A onNote(A base, Source source);

  A onNatural(Expr self, BigInteger value);

  A onInteger(Expr self, BigInteger value);

  A onDouble(Expr self, double value);

  A onBuiltIn(Expr self, String value);

  A onIdentifier(Expr self, String value, long index);

  void bind(String name, Expr type);

  A onLambda(String name, A type, A result);

  A onPi(String name, A type, A result);

  A onLet(List<LetBinding<A>> bindings, A body);

  A onText(String[] parts, List<A> interpolated);

  A onNonEmptyList(List<A> values);

  A onEmptyList(Expr typeExpr, A type);

  A onRecord(List<Entry<String, A>> fields);

  A onRecordType(List<Entry<String, A>> fields);

  A onUnionType(List<Entry<String, A>> fields);

  A onFieldAccess(A base, String fieldName);

  A onProjection(A base, String[] fieldNames);

  A onProjectionByType(A base, A type);

  A onApplication(Expr baseExpr, A base, List<A> args);

  A onOperatorApplication(Operator operator, A lhs, A rhs);

  A onIf(A predicate, A thenValue, A elseValue);

  A onAnnotated(A base, A type);

  A onAssert(A base);

  A onMerge(A handlers, A union, A type);

  A onToMap(A base, A type);

  A onMissingImport(Import.Mode mode, byte[] hash);

  A onEnvImport(String value, Import.Mode mode, byte[] hash);

  A onLocalImport(Path path, Import.Mode mode, byte[] hash);

  A onRemoteImport(URI url, A using, Import.Mode mode, byte[] hash);
}
