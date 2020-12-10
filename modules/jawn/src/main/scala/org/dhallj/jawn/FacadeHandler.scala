package org.dhallj.jawn

import java.math.BigInteger
import java.util.{ArrayDeque, Deque}
import org.dhallj.core.converters.JsonHandler
import org.typelevel.jawn.{FContext, Facade}

class FacadeHandler[J](facade: Facade[J]) extends JsonHandler {
  final protected[this] val stack: Deque[FContext[J]] = new ArrayDeque()
  final protected[this] val position: Int = 0

  protected[this] def addValue(value: J): Unit = {
    if (stack.isEmpty) {
      stack.push(facade.singleContext(position))
    }
    stack.peek.add(value, position)
  }

  final def result: J = stack.pop().finish(position)

  final def onNull(): Unit = addValue(facade.jnull(position))
  final def onBoolean(value: Boolean): Unit = addValue(if (value) facade.jtrue(position) else facade.jfalse(position))
  def onNumber(value: BigInteger): Unit = addValue(facade.jnum(value.toString(), -1, -1, position))
  def onDouble(value: Double): Unit =
    if (java.lang.Double.isFinite(value)) {
      val asString = java.lang.Double.toString(value)

      addValue(facade.jnum(asString, asString.indexOf('.'), asString.indexOf('E'), position))
    } else {
      addValue(facade.jnull(position))
    }

  def onString(value: String): Unit = addValue(facade.jstring(value, position))

  final def onArrayStart(): Unit = stack.push(facade.arrayContext(position))

  final def onArrayEnd(): Unit = {
    val current = stack.pop()

    if (stack.isEmpty) {
      stack.push(current)
    } else {
      stack.peek.add(current.finish(position), position)
    }
  }

  final def onArrayElementGap(): Unit = ()

  final def onObjectStart(): Unit = stack.push(facade.objectContext(position))

  final def onObjectEnd(): Unit = {
    val current = stack.pop()

    if (stack.isEmpty) {
      stack.push(current)
    } else {
      stack.peek.add(current.finish(position), position)
    }
  }

  final def onObjectField(name: String): Unit = stack.peek.add(name, position)

  final def onObjectFieldGap(): Unit = ()
}
