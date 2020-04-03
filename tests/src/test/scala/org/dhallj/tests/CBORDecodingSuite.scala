package org.dhallj.tests

import java.io.ByteArrayOutputStream
import java.math.BigInteger

import co.nstant.in.cbor.CborEncoder
import co.nstant.in.cbor.model.{ByteString, DataItem, DoublePrecisionFloat, HalfPrecisionFloat, NegativeInteger, SimpleValue, SinglePrecisionFloat, UnicodeString, UnsignedInteger, Array => CBArray, Map => CBMap}
import munit.FunSuite
import org.dhallj.core.binary.cbor.CBORConstructors._
import org.dhallj.core.binary.CBORExpression._
import org.dhallj.core.binary.cbor.CBORDecoder

import scala.jdk.CollectionConverters._
import scala.util.Random

class CBORDecodingSuite extends FunSuite {

  private def encode(dataItem: DataItem): Array[Byte] = {
    val os = new ByteArrayOutputStream()
    val encoder = new CborEncoder(os)
    encoder.encode(dataItem)
    os.toByteArray
  }

  test("Decode unsigned integer - tiny") {
    val bytes = encode(new UnsignedInteger(1))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORUnsignedInteger])
    assertEquals(result, mkUnsignedInteger(BigInteger.valueOf(1)))
  }

  test("Decode unsigned integer - short, 1 byte") {
    val bytes = encode(new UnsignedInteger(24))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORUnsignedInteger])
    assertEquals(result, mkUnsignedInteger(BigInteger.valueOf(24)))
  }

  test("Decode unsigned integers - large") {
    val initial = BigInteger.ONE
    for (i <- 0 to 63) {
      val value = initial.shiftLeft(1)
      val bytes = encode(new UnsignedInteger(value))
      val result = CBORDecoder.decode(bytes)

      assert(result.isInstanceOf[CBORUnsignedInteger])
      assertEquals(result, mkUnsignedInteger(value))

    }
  }

  test("Decode negative integer - tiny") {
    val bytes = encode(new NegativeInteger(-1))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORNegativeInteger])
    assertEquals(result, mkNegativeInteger(BigInteger.valueOf(-1)))
  }

  test("Decode negative integer - short, 1 byte") {
    val bytes = encode(new NegativeInteger(-24))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORNegativeInteger])
    assertEquals(result, mkNegativeInteger(BigInteger.valueOf(-24)))
  }

  test("Decode negative integers - large") {
    val initial = BigInteger.ONE
    for (i <- 0 to 63) {
      val value = initial.shiftLeft(1).multiply(MINUS_ONE)
      val bytes = encode(new NegativeInteger(value))
      val result = CBORDecoder.decode(bytes)

      assert(result.isInstanceOf[CBORNegativeInteger])
      assertEquals(result, mkNegativeInteger(value))
    }
  }

  test("Decode byte string - short") {
    val value = Random.nextBytes(19)
    val bytes = encode(new ByteString(value))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORByteString])
    assert(result.asInstanceOf[CBORByteString].getValue.sameElements(value))
  }

  test("Decode byte string - long") {
    val value = Random.nextBytes(1024)
    val bytes = encode(new ByteString(value))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORByteString])
    assert(result.asInstanceOf[CBORByteString].getValue.sameElements(value))
  }

  test("Decode byte string - longer") {
    val value = Random.nextBytes(16384)
    val bytes = encode(new ByteString(value))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORByteString])
    assert(result.asInstanceOf[CBORByteString].getValue.sameElements(value))
  }

  test("Decode text string - short") {
    val bytes = encode(new UnicodeString("short"))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORTextString])
    assertEquals(result, mkTextString("short"))
  }

  test("Decode text string - long") {
    val value = Random.nextString(1024)
    val bytes = encode(new UnicodeString(value))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORTextString])
    assertEquals(result, mkTextString(value))
  }

  test("Decode text string - longer") {
    val value = Random.nextString(16384)
    val bytes = encode(new UnicodeString(value))
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORTextString])
    assertEquals(result, mkTextString(value))
  }

  test("Decode array - short") {
    val value1 = new UnsignedInteger(1)
    val value2 = new NegativeInteger(-2)
    val value3 = new UnicodeString("foo")

    val value = new CBArray().add(value1).add(value2).add(value3)
    val bytes = encode(value)
    val result = CBORDecoder.decode(bytes)

    val expected = List(
      mkUnsignedInteger(BigInteger.valueOf(1)),
      mkNegativeInteger(BigInteger.valueOf(-2)),
      mkTextString("foo")
    ).asJava

    assert(result.isInstanceOf[CBORHeterogeneousArray])
    assertEquals(result, mkArray(expected))
  }

  test("Decode map - short") {
    val key1 = new UnicodeString("one")
    val value1 = new UnsignedInteger(1)
    val key2 = new UnicodeString("two")
    val value2 = new NegativeInteger(-2)
    val key3 = new UnicodeString("three")
    val value3 = new UnicodeString("foo")

    val value = new CBMap().put(key1, value1).put(key2, value2).put(key3, value3)
    val bytes = encode(value)
    val result = CBORDecoder.decode(bytes)

    val expected = Map(
      mkTextString("one") -> mkUnsignedInteger(BigInteger.valueOf(1)),
      mkTextString("two") -> mkNegativeInteger(BigInteger.valueOf(-2)),
      mkTextString("three") -> mkTextString("foo")
    ).asJava

    assert(result.isInstanceOf[CBORHeterogeneousMap])
    assertEquals(result, mkMap(expected))
  }

  test("Decode false") {
    val bytes = encode(SimpleValue.FALSE)
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORFalse])
  }

  test("Decode true") {
    val bytes = encode(SimpleValue.TRUE)
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORTrue])
  }

  test("Decode false") {
    val value = SimpleValue.NULL
    val bytes = encode(SimpleValue.NULL)
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORNull])
  }

  test("Decode half float") {
    val value = new HalfPrecisionFloat(1.1f)
    val bytes = encode(value)
    val result = CBORDecoder.decode(bytes)

    println(result.asInstanceOf[CBORHalfFloat].getValue)

    assert(result.isInstanceOf[CBORHalfFloat])
    //We lose some precision converting to and from Java 32bit floats
    assert(Math.abs(result.asInstanceOf[CBORHalfFloat].getValue - 1.1f) < 0.0005)
  }

  test("Decode single float") {
    val value = new SinglePrecisionFloat(1.1f)
    val bytes = encode(value)
    val result = CBORDecoder.decode(bytes)

    assert(result.isInstanceOf[CBORSingleFloat])
    assert(result == mkSingleFloat(1.1f))
  }

  test("Decode double float") {
    val value = new DoublePrecisionFloat(1.1f)
    val bytes = encode(value)
    val result = CBORDecoder.decode(bytes)

    println(result.asInstanceOf[CBORDoubleFloat].getValue)

    assert(result.isInstanceOf[CBORDoubleFloat])
    assert(result == mkDoubleFloat(1.1f))
  }
  final private val MINUS_ONE: BigInteger = BigInteger.valueOf(-1)

}
