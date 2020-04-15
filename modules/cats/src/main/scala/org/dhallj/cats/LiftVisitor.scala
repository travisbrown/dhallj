package org.dhallj.cats

import cats.Applicative
import java.math.BigInteger
import java.net.URI
import java.nio.file.Path
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.ArrayList
import java.util.{List => JList, Map => JMap}
import org.dhallj.core.{Expr, Operator, Source, Visitor}

/**
 * Represents a function lifting an `Expr` into an `F[Expr]`.
 *
 * This is a convenience class designed to help with implementations
 * that don't need effects for most cases.
 */
class LiftVisitor[F[_] <: AnyRef](
  private[this] val F: Applicative[F]
) extends Visitor.NoPrepareEvents[F[Expr]] {
  def onNote(base: F[Expr], source: Source): F[Expr] = base
  def onNatural(self: Expr, value: BigInteger): F[Expr] = F.pure(self)
  def onInteger(self: Expr, value: BigInteger): F[Expr] = F.pure(self)
  def onDouble(self: Expr, value: Double): F[Expr] = F.pure(self)
  def onBuiltIn(self: Expr, name: String): F[Expr] = F.pure(self)
  def onIdentifier(self: Expr, value: String, index: Long): F[Expr] = F.pure(self)

  def onLambda(name: String, tpe: F[Expr], result: F[Expr]): F[Expr] =
    F.map2(tpe, result)(Expr.makeLambda(name, _, _))

  def onPi(name: String, tpe: F[Expr], result: F[Expr]): F[Expr] =
    F.map2(tpe, result)(Expr.makePi(name, _, _))

  def onLet(bindings: JList[Expr.LetBinding[F[Expr]]], body: F[Expr]): F[Expr] =
    F.map2(sequenceBindings(bindings), body)(Expr.makeLet(_, _))

  def onText(parts: Array[String], interpolated: JList[F[Expr]]): F[Expr] =
    F.map(sequenceValues(interpolated))(Expr.makeTextLiteral(parts, _))

  def onNonEmptyList(values: JList[F[Expr]]): F[Expr] =
    F.map(sequenceValues(values))(Expr.makeNonEmptyListLiteral(_))

  def onEmptyList(tpe: F[Expr]): F[Expr] = F.map(tpe)(Expr.makeEmptyListLiteral(_))

  def onRecord(fields: JList[JMap.Entry[String, F[Expr]]]): F[Expr] =
    F.map(sequenceFields(fields, false))(Expr.makeRecordLiteral(_))

  def onRecordType(fields: JList[JMap.Entry[String, F[Expr]]]): F[Expr] =
    F.map(sequenceFields(fields, false))(Expr.makeRecordType(_))

  def onUnionType(fields: JList[JMap.Entry[String, F[Expr]]]): F[Expr] =
    F.map(sequenceFields(fields, true))(Expr.makeUnionType(_))

  def onFieldAccess(base: F[Expr], fieldName: String): F[Expr] =
    F.map(base)(Expr.makeFieldAccess(_, fieldName))

  def onProjection(base: F[Expr], fieldNames: Array[String]): F[Expr] =
    F.map(base)(Expr.makeProjection(_, fieldNames))

  def onProjectionByType(base: F[Expr], tpe: F[Expr]): F[Expr] =
    F.map2(base, tpe)(Expr.makeProjectionByType(_, _))

  def onApplication(base: F[Expr], args: JList[F[Expr]]): F[Expr] =
    F.map2(base, sequenceValues(args))(Expr.makeApplication(_, _))

  def onOperatorApplication(operator: Operator, lhs: F[Expr], rhs: F[Expr]): F[Expr] =
    F.map2(lhs, rhs)(Expr.makeOperatorApplication(operator, _, _))

  def onIf(predicate: F[Expr], thenValue: F[Expr], elseValue: F[Expr]): F[Expr] =
    F.map3(predicate, thenValue, elseValue)(Expr.makeIf(_, _, _))

  def onAnnotated(base: F[Expr], tpe: F[Expr]): F[Expr] =
    F.map2(base, tpe)(Expr.makeAnnotated(_, _))

  def onAssert(base: F[Expr]): F[Expr] = F.map(base)(Expr.makeAssert(_))

  def onMerge(handlers: F[Expr], union: F[Expr], tpe: F[Expr]): F[Expr] =
    if (tpe.eq(null)) {
      F.map2(handlers, union)(Expr.makeMerge(_, _, null))
    } else {
      F.map3(handlers, union, tpe)(Expr.makeMerge(_, _, _))
    }

  def onToMap(base: F[Expr], tpe: F[Expr]): F[Expr] =
    if (tpe.eq(null)) {
      F.map(base)(Expr.makeToMap(_, null))
    } else {
      F.map2(base, tpe)(Expr.makeToMap(_, _))
    }

  def onMissingImport(mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    F.pure(Expr.makeMissingImport(mode, hash))

  def onEnvImport(name: String, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    F.pure(Expr.makeEnvImport(name, mode, hash))

  def onLocalImport(path: Path, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    F.pure(Expr.makeLocalImport(path, mode, hash))

  def onClasspathImport(path: Path, mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    F.pure(Expr.makeClasspathImport(path, mode, hash))

  def onRemoteImport(url: URI, headers: F[Expr], mode: Expr.ImportMode, hash: Array[Byte]): F[Expr] =
    if (headers.eq(null)) {
      F.pure(Expr.makeRemoteImport(url, null, mode, hash))
    } else {
      F.map(headers)(Expr.makeRemoteImport(url, _, mode, hash))
    }

  final private[this] def sequenceValues(values: JList[F[Expr]]): F[Array[Expr]] = {
    var result = F.pure(new Array[Expr](values.size))
    var i = 0

    while (i < values.size) {
      val index = i
      result = F.map2(result, values.get(index)) {
        case (acc, next) =>
          acc(index) = next
          acc
      }
      i += 1
    }

    result
  }

  final private[this] def sequenceFields(
    fields: JList[JMap.Entry[String, F[Expr]]],
    checkNull: Boolean
  ): F[Array[JMap.Entry[String, Expr]]] = {
    var result = F.pure(new Array[JMap.Entry[String, Expr]](fields.size))
    var i = 0

    while (i < fields.size) {
      val index = i
      val entry = fields.get(index)
      val fieldName = entry.getKey
      val value = entry.getValue

      if (checkNull && value.eq(null)) {
        result = F.map(result) { acc =>
          acc(index) = new SimpleImmutableEntry(fieldName, null)
          acc
        }
      } else {
        result = F.map2(result, value) {
          case (acc, nextValue) =>
            acc(index) = new SimpleImmutableEntry(fieldName, nextValue)
            acc
        }
      }

      i += 1
    }

    result
  }

  final private[this] def sequenceBindings(
    bindings: JList[Expr.LetBinding[F[Expr]]]
  ): F[JList[Expr.LetBinding[Expr]]] = {
    var result: F[JList[Expr.LetBinding[Expr]]] =
      F.pure(new ArrayList[Expr.LetBinding[Expr]](bindings.size))
    var i = 0

    while (i < bindings.size) {
      val index = i
      val binding = bindings.get(index)

      if (binding.hasType) {
        result = F.map3(result, binding.getType, binding.getValue) {
          case (acc, nextType, nextValue) =>
            acc.add(new Expr.LetBinding(binding.getName, nextType, nextValue))
            acc
        }
      } else {
        result = F.map2(result, binding.getValue) {
          case (acc, nextValue) =>
            acc.add(new Expr.LetBinding(binding.getName, null, nextValue))
            acc
        }
      }

      i += 1
    }

    result
  }
}
