package org.dhallj.cbor

import co.nstant.in.cbor.{CborBuilder, CborEncoder}
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import munit.ScalaCheckSuite
import org.scalacheck.Prop

class CborSuite extends ScalaCheckSuite {
  def roundTripDouble(value: Double): Option[Double] = {
    val writer = new Writer.ByteArrayWriter()
    writer.writeDouble(value)

    val bytes = writer.getBytes
    val reader = new Reader.ByteArrayReader(bytes)
    reader.nextSymbol(DoubleValueVisitor)
  }

  def encodeDoubleWithCborJava(value: Double): Array[Byte] = {
    val stream = new ByteArrayOutputStream()
    new CborEncoder(stream).encode(new CborBuilder().add(value).build())

    return stream.toByteArray
  }

  property("Writer and Reader should round-trip doubles") {
    Prop.forAll((value: Double) => roundTripDouble(value) == Some(value))
  }

  property("Writer should agree with cbor-java") {
    Prop.forAll { (value: Double) =>
      val writer = new Writer.ByteArrayWriter()
      writer.writeDouble(value)

      writer.getBytes.sameElements(encodeDoubleWithCborJava(value))
    }
  }

  test("Writer and Reader should round-trip special-case doubles") {
    assertEquals(roundTripDouble(0.0), Some(0.0))
    assertEquals(roundTripDouble(-0.0), Some(-0.0))
    assertEquals(roundTripDouble(Double.PositiveInfinity), Some(Double.PositiveInfinity))
    assertEquals(roundTripDouble(Double.NegativeInfinity), Some(Double.NegativeInfinity))
    assert(roundTripDouble(Double.NaN).exists(_.isNaN))
  }

  object DoubleValueVisitor extends Visitor[Option[Double]] {
    def onUnsignedInteger(value: BigInteger): Option[Double] = None
    def onNegativeInteger(value: BigInteger): Option[Double] = None
    def onByteString(value: Array[Byte]): Option[Double] = None
    def onTextString(value: String): Option[Double] = None
    def onVariableArray(length: BigInteger, name: String): Option[Double] = None
    def onArray(length: BigInteger, tagI: BigInteger): Option[Double] = None
    def onMap(size: BigInteger): Option[Double] = None
    def onFalse: Option[Double] = None
    def onTrue: Option[Double] = None
    def onNull: Option[Double] = None
    def onHalfFloat(value: Float): Option[Double] = Some(value.toDouble)
    def onSingleFloat(value: Float): Option[Double] = Some(value.toDouble)
    def onDoubleFloat(value: Double): Option[Double] = Some(value)
    def onTag: Option[Double] = None
  }
}
