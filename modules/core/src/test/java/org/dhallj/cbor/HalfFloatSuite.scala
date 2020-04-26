package org.dhallj.cbor

import munit.FunSuite
import org.scalacheck.Prop

class HalfFloatSuite extends FunSuite {
  def roundTripInt(x: Int): Int =
    HalfFloat.toFloat(HalfFloat.fromFloat(x.toFloat)).toInt

  test("HalfFloat conversions round-trip integers with abs <= 2048") {
    0.to(2048).foreach { x =>
      assertEquals(roundTripInt(x), x)
      assertEquals(roundTripInt(-x), -x)
    }
  }

  test("HalfFloat conversions round-trip even integers with abs <= 4096") {
    1.to(1024).foreach { x =>
      assertEquals(roundTripInt((x * 2) + 2048), (x * 2) + 2048)
      assertEquals(roundTripInt(-((x * 2) + 2048)), -((x * 2) + 2048))
      assertEquals(roundTripInt((x * 2) + 2048 - 1), (x * 2) + 2048)
      assertEquals(roundTripInt(-((x * 2) + 2048 - 1)), -((x * 2) + 2048))
    }
  }

  test("HalfFloat conversions round-trip through float for all values") {
    0.until(1 << 16).foreach { x =>
      val asFloat = HalfFloat.toFloat(x)

      if (!asFloat.isNaN) {
        assertEquals(HalfFloat.fromFloat(asFloat), x)
      }
    }
  }
}
