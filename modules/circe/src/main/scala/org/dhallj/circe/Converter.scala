package org.dhallj.circe

import io.circe.{Json, JsonNumber, JsonObject}
import java.math.BigInteger
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.Map.Entry
import org.dhallj.core.Expr
import org.dhallj.core.converters.JsonConverter

object Converter {
  def apply(expr: Expr): Option[Json] = {
    val handler = new CirceHandler()
    val wasConverted = expr.accept(new JsonConverter(handler))

    if (wasConverted) Some(handler.result) else None
  }

  private[this] val folder: Json.Folder[Expr] = new Json.Folder[Expr] {
    def onBoolean(value: Boolean): Expr = if (value) Expr.Constants.TRUE else Expr.Constants.FALSE
    def onNull: Expr = Expr.makeApplication(Expr.Constants.NONE, Expr.Constants.EMPTY_RECORD_TYPE)
    def onNumber(value: JsonNumber): Expr = {
      val asDouble = value.toDouble

      if (java.lang.Double.compare(asDouble, -0.0) == 0) {
        Expr.makeDoubleLiteral(asDouble)
      } else
        value.toBigInt match {
          case Some(integer) =>
            if (integer.underlying.compareTo(BigInteger.ZERO) > 0) {
              Expr.makeNaturalLiteral(integer.underlying)
            } else {
              Expr.makeIntegerLiteral(integer.underlying)
            }
          case None => Expr.makeDoubleLiteral(asDouble)
        }
    }
    def onString(value: String): Expr = Expr.makeTextLiteral(value)
    def onObject(value: JsonObject): Expr = Expr.makeRecordLiteral(
      value.toMap.map {
        case (k, v) =>
          new SimpleImmutableEntry(k, v.foldWith(this)): Entry[String, Expr]
      }.toArray
    )
    def onArray(value: Vector[Json]): Expr =
      if (value.isEmpty) {
        Expr.makeEmptyListLiteral(Expr.makeApplication(Expr.Constants.LIST, Expr.Constants.EMPTY_RECORD_TYPE))
      } else {
        Expr.makeNonEmptyListLiteral(value.map(_.foldWith(this)).toArray)
      }
  }

  def apply(json: Json): Expr = json.foldWith(folder)
}
