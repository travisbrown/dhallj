package org.dhallj.circe

import io.circe.Json
import java.math.BigInteger
import java.util.{ArrayDeque, ArrayList, Deque, LinkedHashMap, List}
import org.dhallj.core.converters.JsonHandler
import scala.collection.JavaConverters._

sealed private trait Context {
  def add(key: String): Unit
  def add(value: Json): Unit
  def result: Json
}

final private class RootContext(value: Json) extends Context {
  def add(key: String): Unit = ()
  def add(value: Json): Unit = ()
  def result: Json = value
}

final private class ObjectContext() extends Context {
  private[this] var key: String = null
  private[this] val fields: LinkedHashMap[String, Json] = new LinkedHashMap()

  def add(key: String): Unit = this.key = key
  def add(value: Json): Unit = this.fields.put(this.key, value)
  def result: Json = Json.fromFields(this.fields.entrySet.asScala.map(entry => entry.getKey -> entry.getValue))
}

final private class ArrayContext() extends Context {
  private[this] val values: List[Json] = new ArrayList()

  def add(key: String): Unit = ()
  def add(value: Json): Unit = this.values.add(value)
  def result: Json = Json.fromValues(this.values.asScala)
}

class CirceHandler extends JsonHandler {
  private[this] val stack: Deque[Context] = new ArrayDeque()

  protected[this] def addValue(value: Json): Unit =
    if (stack.isEmpty) {
      stack.push(new RootContext(value))
    } else {
      stack.peek.add(value)
    }

  final def result: Json = stack.pop().result

  final def onNull(): Unit = addValue(Json.Null)
  final def onBoolean(value: Boolean): Unit = addValue(if (value) Json.True else Json.False)
  final def onNumber(value: BigInteger): Unit = addValue(Json.fromBigInt(new BigInt(value)))
  def onDouble(value: Double): Unit = addValue(Json.fromDoubleOrNull(value))
  final def onString(value: String): Unit = addValue(Json.fromString(value))

  final def onArrayStart(): Unit = stack.push(new ArrayContext())

  final def onArrayEnd(): Unit = {
    val current = stack.pop()

    if (stack.isEmpty) {
      stack.push(current)
    } else {
      stack.peek.add(current.result)
    }
  }

  final def onArrayElementGap(): Unit = ()

  final def onObjectStart(): Unit = stack.push(new ObjectContext())

  final def onObjectEnd(): Unit = {
    val current = stack.pop()

    if (stack.isEmpty) {
      stack.push(current)
    } else {
      stack.peek.add(current.result)
    }
  }

  final def onObjectField(name: String): Unit = stack.peek.add(name)

  final def onObjectFieldGap(): Unit = ()
}
