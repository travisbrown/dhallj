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
  A onDoubleLiteral(double value);

  A onNaturalLiteral(BigInteger value);

  A onIntegerLiteral(BigInteger value);

  A onTextLiteral(String[] parts, Iterable<I> interpolated);

  A onApplication(I base, I arg);

  A onOperatorApplication(Operator operator, I lhs, I rhs);

  A onIf(I cond, I thenValue, I elseValue);

  A onLambda(String param, I input, I result);

  A onPi(String param, I input, I result);

  A onAssert(I base);

  A onFieldAccess(I base, String fieldName);

  A onProjection(I base, String[] fieldNames);

  A onProjectionByType(I base, I tpe);

  A onBuiltIn(String name);

  A onIdentifier(String value, long index);

  A onRecordLiteral(Iterable<Entry<String, I>> fields, int size);

  A onRecordType(Iterable<Entry<String, I>> fields, int size);

  A onUnionType(Iterable<Entry<String, I>> fields, int size);

  A onNonEmptyListLiteral(Iterable<I> values, int size);

  A onEmptyListLiteral(I tpe);

  A onNote(I base, Source source);

  A onLet(String name, I type, I value, I body);

  A onAnnotated(I base, I tpe);

  A onToMap(I base, I tpe);

  A onMerge(I left, I right, I tpe);

  A onLocalImport(Path path, Mode mode, byte[] hash);

  A onRemoteImport(URI url, I using, Mode mode, byte[] hash);

  A onEnvImport(String value, Mode mode, byte[] hash);

  A onMissingImport(Mode mode, byte[] hash);
}
