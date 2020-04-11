package org.dhallj.circe

import io.circe.{Json, JsonNumber, JsonObject}

object JsonCleaner extends Json.Folder[Json] {
  def onBoolean(value: Boolean): Json = Json.fromBoolean(value)
  def onNull: Json = Json.Null
  def onNumber(value: JsonNumber): Json = {
    val asDouble = value.toDouble
    val asJson = Json.fromDoubleOrNull(asDouble)
    if (asJson.asNumber == Some(value)) {
      asJson
    } else {
      Json.fromLong(0)
    }
  }
  def onString(value: String): Json = Json.fromString(value)
  def onObject(value: JsonObject): Json = Json.fromFields(
    value.toIterable.flatMap {
      case ("", v) => None
      case (k, v)  => Some((k, v.foldWith(this)))
    }
  )
  def onArray(values: Vector[Json]): Json = Json.fromValues(values.map(_.foldWith(this)))
}
