package org.dhallj.core;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;

/**
 * Represents a function from a Dhall expression to a value.
 *
 * @param I The internal result type used while recursing
 * @param A The final result type
 */
public interface Vis<A> {
  A onNote(A base, Source source);

  A onNatural(BigInteger value);

  A onInteger(BigInteger value);

  A onDouble(double value);

  A onIdentifier(String value, long index);

  void bind(String param, Expr type);

  A onLambda(String name, A type, A result);

  A onPi(String name, A type, A result);

  A onLet(String name, A type, A value, A body);

  void preText(int size);

  A onText(String[] parts, List<A> interpolated);

  void preNonEmptyList(int size);

  A onNonEmptyList(List<A> values);

  A onEmptyList(A type);

  void preRecord(int size);

  A onRecord(List<Entry<String, A>> fields);

  void preRecordType(int size);

  A onRecordType(List<Entry<String, A>> fields);

  void preUnionType(int size);

  A onUnionType(List<Entry<String, A>> fields);

  A onFieldAccess(A base, String fieldName);

  A onProjection(A base, String[] fieldNames);

  A onProjectionByType(A base, A type);

  void preApplication(int size);

  A onApplication(A base, List<A> args);

  A onOperatorApplication(Operator operator, A lhs, A rhs);

  A onIf(A predicate, A thenValue, A elseValue);

  A onAnnotated(A base, A type);

  A onAssert(A base);

  A onMerge(A left, A right, A type);

  A onToMap(A base, A type);

  A onMissingImport(Import.Mode mode, byte[] hash);

  A onEnvImport(String value, Import.Mode mode, byte[] hash);

  A onLocalImport(Path path, Import.Mode mode, byte[] hash);

  A onRemoteImport(URI url, A using, Import.Mode mode, byte[] hash);
}
