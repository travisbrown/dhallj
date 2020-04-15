package org.dhallj.javagen

import java.math.BigInteger
import java.net.URI
import java.nio.file.Path
import java.util.{List => JList}
import java.util.Map.Entry
import org.dhallj.core.Expr
import org.dhallj.core.Operator
import org.dhallj.core.Source
import org.dhallj.core.Visitor
import scala.collection.JavaConverters._

object ToCodeVisitor {
  val instance: Visitor[Code] = new ToCodeVisitor
}

final class ToCodeVisitor extends Visitor.NoPrepareEvents[Code] {
  private val constants = Set("Natural", "Integer", "Double", "True", "False", "Type", "List", "Text")

  private def unsupported: Nothing =
    throw new RuntimeException("Java generation only supported for fully-interpreted expressions")

  def onNote(base: Code, source: Source): Code = base

  def onNatural(self: Expr, value: BigInteger): Code = Code(s"""Expr.makeNaturalLiteral(new BigInteger("$value"))""")
  def onInteger(self: Expr, value: BigInteger): Code = Code(s"""Expr.makeIntegerLiteral(new BigInteger("$value"))""")
  def onDouble(self: Expr, value: Double): Code = Code(s"""Expr.makeDoubleLiteral($value)""")
  def onBuiltIn(self: Expr, name: String): Code = Code(
    if (constants(name)) {
      s"""Expr.Constants.${name.toUpperCase}"""
    } else {
      s"""Expr.makeBuiltIn("$name")"""
    }
  )

  def onIdentifier(self: Expr, name: String, index: Long): Code = Code(s"""Expr.makeIdentifier("$name", $index)""")

  def onLambda(name: String, tpe: Code, result: Code): Code =
    result.merge(tpe) {
      case (resultContent, tpeContent) =>
        s"""Expr.makeLambda("$name", $tpeContent, $resultContent)"""
    }

  def onPi(name: String, tpe: Code, result: Code): Code =
    result.merge(tpe) {
      case (resultContent, tpeContent) =>
        s"""Expr.makePi("$name", $tpeContent, $resultContent)"""
    }

  def onLet(bindings: JList[Expr.LetBinding[Code]], body: Code): Code = unsupported

  def onText(parts: Array[String], interpolated: JList[Code]): Code =
    if (parts.length == 1) {
      Code(s"""Expr.makeTextLiteral("${parts(0)}")""")
    } else {
      Code.mergeAll(interpolated.asScala.toVector) { ids =>
        val partArray = parts.map(part => "\"" + part + "\"").mkString(", ")

        s"Expr.makeTextLiteral(new String[] {$partArray}, new Expr[] {${ids.mkString(", ")}})"
      }
    }

  def onNonEmptyList(values: JList[Code]): Code = Code.mergeAll(values.asScala.toVector) { ids =>
    s"Expr.makeNonEmptyListLiteral(new Expr[] {${ids.mkString(", ")}})"
  }

  def onEmptyList(tpe: Code): Code =
    tpe.mapContent(tpeContent => s"Expr.makeEmptyListLiteral($tpeContent)")

  private def forFields(constructor: String, fields: JList[Entry[String, Code]]): Code =
    if (fields.isEmpty) {
      constructor match {
        case "makeRecordLiteral" => Code("Expr.Constants.EMPTY_RECORD_LITERAL")
        case "makeRecordType"    => Code("Expr.Constants.EMPTY_RECORD_TYPE")
        case "makeUnionType"     => Code("Expr.Constants.EMPTY_UNION_TYPE")
      }
    } else {
      Code.mergeAll(fields.asScala.toVector)(entry => Option(entry.getValue)) { pairs =>
        val entries = pairs
          .map {
            case (entry, id) =>
              s"""new SimpleImmutableEntry<String, Expr>("${entry.getKey}", $id)"""
          }
          .mkString(", ")

        s"Expr.$constructor(new Entry[] {$entries})"
      }
    }

  def onRecord(fields: JList[Entry[String, Code]]): Code = forFields("makeRecordLiteral", fields)
  def onRecordType(fields: JList[Entry[String, Code]]): Code = forFields("makeRecordType", fields)
  def onUnionType(fields: JList[Entry[String, Code]]): Code = forFields("makeUnionType", fields)

  def onFieldAccess(base: Code, fieldName: String): Code =
    base.mapContent(baseContent => s"""Expr.makeFieldAccess($baseContent, "$fieldName")""")

  def onProjection(base: Code, fieldNames: Array[String]): Code = {
    val fieldNameArray = fieldNames.map(fieldName => "\"" + fieldName + "\"").mkString(", ")

    base.mapContent(baseContent => s"""Expr.makeProjection($baseContent, new String[] {${fieldNameArray}})""")
  }

  def onProjectionByType(base: Code, tpe: Code): Code =
    base.merge(tpe) {
      case (baseContent, tpeContent) =>
        s"""Expr.makeProjectionByType($baseContent, $tpeContent)"""
    }

  def onApplication(base: Code, args: JList[Code]): Code =
    Code.mergeAll(base +: args.asScala.toVector) {
      case head +: tail =>
        s"Expr.makeApplication($head, new Expr[] {${tail.mkString(", ")}})"
    }

  def onOperatorApplication(operator: Operator, lhs: Code, rhs: Code): Code = {
    val operatorCode = operator match {
      case Operator.OR            => "Operator.OR"
      case Operator.AND           => "Operator.AND"
      case Operator.EQUALS        => "Operator.EQUALS"
      case Operator.NOT_EQUALS    => "Operator.NOT_EQUALS"
      case Operator.PLUS          => "Operator.PLUS"
      case Operator.TIMES         => "Operator.TIMES"
      case Operator.TEXT_APPEND   => "Operator.TEXT_APPEND"
      case Operator.LIST_APPEND   => "Operator.LIST_APPEND"
      case Operator.COMBINE       => "Operator.COMBINE"
      case Operator.PREFER        => "Operator.PREFER"
      case Operator.COMBINE_TYPES => "Operator.COMBINE_TYPES"
      case Operator.IMPORT_ALT    => "Operator.IMPORT_ALT"
      case Operator.EQUIVALENT    => "Operator.EQUIVALENT"
      case Operator.COMPLETE      => "Operator.COMPLETE"
    }

    lhs.merge(rhs) {
      case (lhsContent, rhsContent) =>
        s"Expr.makeOperatorApplication($operatorCode, $lhsContent, $rhsContent)"
    }
  }

  def onIf(predicate: Code, thenValue: Code, elseValue: Code): Code = {
    if (elseValue == null) throw new RuntimeException(predicate.toString());
    predicate.merge(thenValue, elseValue) {
      case (predicateContent, thenValueContent, elseValueContent) =>
        s"""Expr.makeIf($predicateContent, $thenValueContent, $elseValueContent)"""
    }
  }

  def onAnnotated(base: Code, tpe: Code): Code = unsupported
  def onAssert(base: Code): Code = unsupported
  def onMerge(handlers: Code, union: Code, tpe: Code): Code =
    handlers.merge(union, tpe) {
      case (handlersContent, unionContent, tpeContent) =>
        s"""Expr.makeMerge($handlersContent, $unionContent, $tpeContent)"""

    }

  def onToMap(base: Code, tpe: Code): Code = unsupported
  def onMissingImport(mode: Expr.ImportMode, hash: Array[Byte]): Code = unsupported
  def onEnvImport(value: String, mode: Expr.ImportMode, hash: Array[Byte]): Code = unsupported
  def onLocalImport(path: Path, mode: Expr.ImportMode, hash: Array[Byte]): Code = unsupported
  def onClasspathImport(path: Path, mode: Expr.ImportMode, hash: Array[Byte]): Code = unsupported
  def onRemoteImport(url: URI, `using`: Code, mode: Expr.ImportMode, hash: Array[Byte]): Code = unsupported
}
