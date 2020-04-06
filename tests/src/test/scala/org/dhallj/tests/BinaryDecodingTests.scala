package org.dhallj.tests

import java.math.BigInteger
import java.nio.file.{Files, Paths}

import munit.FunSuite
import org.dhallj.core.Expr
import org.dhallj.core.binary.Decode.decode
import scala.jdk.CollectionConverters._
import org.dhallj.parser.Dhall.parse

class BinaryDecodingTests extends FunSuite {

  test("Decode natural") {
    val bytes = load("nat.bin")

    val decoded = decode(bytes)
    val expected = parse("123")

    assert(decoded.equivalent(expected))
  }

  test("Decode integer") {
    val bytes = load("int.bin")

    val decoded = decode(bytes)
    val expected = parse("-123")

    assert(decoded.equivalent(expected))
  }

  test("Decode variable") {
    val bytes = load("var.bin")

    val decoded = decode(bytes)
    val expected = parse("x@1")

    assert(decoded.equivalent(expected))
  }

  test("Decode anonymous variable") {
    val bytes = load("anon_var.bin")

    val decoded = decode(bytes)
    val expected = parse("_@1")

    assert(decoded.equivalent(expected))
  }

  test("Decode builtin") {
    val bytes = load("builtin.bin")

    val decoded = decode(bytes)
    val expected = parse("Natural/even")

    assert(decoded.equivalent(expected))
  }

  test("Decode True") {
    val bytes = load("true.bin")

    val decoded = decode(bytes)
    val expected = parse("True")

    assert(decoded.equivalent(expected))
  }

  test("Decode False") {
    val bytes = load("false.bin")

    val decoded = decode(bytes)
    val expected = parse("False")

    assert(decoded.equivalent(expected))
  }

  test("Decode empty list") {
    val bytes = load("empty_list.bin")

    val decoded = decode(bytes)
    val expected = parse("[] : List Natural")

    assert(decoded.equivalent(expected))
  }

  test("Decode non-empty list") {
    val bytes = load("list.bin")

    val decoded = decode(bytes)
    println(decoded)
    val expected = parse("[1,2,3]")

    assert(decoded.equivalent(expected))
  }

  test("Decode some") {
    val bytes = load("some.bin")

    val decoded = decode(bytes)
    val expected = parse("Some \"foo\"")
    println(expected)

    assert(decoded.equivalent(expected))
  }

  test("Decode lambda") {
    val bytes = load("lambda.bin")

    val decoded = decode(bytes)
    val expected = parse("\\(x: Text) -> x")

    assert(decoded.equivalent(expected))
  }

  test("Decode anonymous lambda") {
    val bytes = load("anon_lambda.bin")

    val decoded = decode(bytes)
    val expected = parse("\\(_: Text) -> \"foo\"")

    println(decoded)

    assert(decoded.equivalent(expected))
  }

  test("Decode pi") {
    val bytes = load("pi.bin")

    val decoded = decode(bytes)
    val expected = parse("forall (x: a) -> List x")

    assert(decoded.equivalent(expected))
  }

  test("Decode anonymous pi") {
    val bytes = load("anon_pi.bin")

    val decoded = decode(bytes)
    val expected = parse("forall (_: a) -> List Text")

    assert(decoded.equivalent(expected))
  }

  test("Decode function application") {
    val bytes = load("app.bin")

    val decoded = decode(bytes)
    val expected = parse("(\\(x: Text) -> x) \"foo\"")

    assert(decoded.equivalent(expected))
  }

  test("Decode record type") {
    val bytes = load("record_type.bin")

    val decoded = decode(bytes)
    println(decoded)
    val expected = parse("{x: Text, y: Natural}")

    assert(decoded.equivalent(expected))
  }

  test("Decode record literal") {
    val bytes = load("record_literal.bin")

    val decoded = decode(bytes)
    println(decoded)
    val expected = parse("{x = \"foo\", y = 3}")

    assert(decoded.equivalent(expected))
  }

  private def load(resource: String): Array[Byte] =
    Files.readAllBytes(Paths.get(getClass.getResource(s"/binary/$resource").toURI))

}
