package org.dhallj.core;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Map.Entry;
import org.dhallj.core.Import.Mode;

/**
 * Represents a function from a Dhall expression to a value.
 *
 * @param I The internal result type used while recursing
 * @param A The final result type
 */
public interface Visitor<I, A> {
  A onNote(I base, Source source);

  A onNatural(BigInteger value);

  A onInteger(BigInteger value);

  A onDouble(double value);

  A onBuiltIn(String name);

  A onIdentifier(String value, long index);

  A onLambda(String name, I type, I result);

  A onPi(String name, I type, I result);

  A onLet(String name, I type, I value, I body);

  A onText(String[] parts, Iterable<I> interpolated);

  A onNonEmptyList(Iterable<I> values, int size);

  A onEmptyList(I tpe);

  A onRecord(Iterable<Entry<String, I>> fields, int size);

  A onRecordType(Iterable<Entry<String, I>> fields, int size);

  A onUnionType(Iterable<Entry<String, I>> fields, int size);

  A onFieldAccess(I base, String fieldName);

  A onProjection(I base, String[] fieldNames);

  A onProjectionByType(I base, I tpe);

  A onApplication(I base, I arg);

  A onOperatorApplication(Operator operator, I lhs, I rhs);

  A onIf(I cond, I thenValue, I elseValue);

  A onAnnotated(I base, I tpe);

  A onAssert(I base);

  A onMerge(I handlers, I union, I type);

  A onToMap(I base, I type);

  A onMissingImport(Mode mode, byte[] hash);

  A onEnvImport(String value, Mode mode, byte[] hash);

  A onLocalImport(Path path, Mode mode, byte[] hash);

  A onRemoteImport(URI url, I using, Mode mode, byte[] hash);
}
