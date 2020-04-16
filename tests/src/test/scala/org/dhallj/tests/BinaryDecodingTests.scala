package org.dhallj.tests

import java.nio.file.{Files, Paths}

import munit.FunSuite
import org.dhallj.core.binary.Decode.decode
import org.dhallj.parser.DhallParser.parse

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
    val expected = parse("[1,2,3]")

    assert(decoded.equivalent(expected))
  }

  test("Decode some") {
    val bytes = load("some.bin")

    val decoded = decode(bytes)
    val expected = parse("Some \"foo\"")

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
    val expected = parse("{x: Text, y: Natural}")

    assert(decoded.equivalent(expected))
  }

  test("Decode record literal") {
    val bytes = load("record_literal.bin")

    val decoded = decode(bytes)
    val expected = parse("{x = \"foo\", y = 3}")

    assert(decoded.equivalent(expected))
  }

  test("Decode field access") {
    val bytes = load("field_access.bin")

    val decoded = decode(bytes)
    val expected = parse("{x: Text, y: Natural}.x")

    assert(decoded.equivalent(expected))
  }

  test("Decode record projection") {
    val bytes = load("record_projection.bin")

    val decoded = decode(bytes)
    val expected = parse("{x: Text, y: Natural, z: Bool}.{x,y}")

    assert(decoded.equivalent(expected))
  }

  test("Decode record projection by type") {
    val bytes = load("record_type_projection.bin")

    val decoded = decode(bytes)
    val expected = parse("{x = 1, y = 2, z = \"foo\"}.({x : Natural, y : Natural})")

    assert(decoded.equivalent(expected))
  }

  test("Decode union") {
    val bytes = load("union.bin")

    val decoded = decode(bytes)
    val expected = parse("<Local : Text | Remote : Text | Missing>")

    assert(decoded.equivalent(expected))
  }

  test("Decode merge") {
    val bytes = load("merge.bin")

    val decoded = decode(bytes)
    val expected = parse("merge { Foo = False, Bar = Natural/even } < Foo | Bar : Natural >")

    assert(decoded.equivalent(expected))
  }

  test("Decode to map") {
    val bytes = load("to_map.bin")

    val decoded = decode(bytes)
    val expected = parse("toMap { bar = 2, foo = 1 }")

    assert(decoded.equivalent(expected))
  }

  test("Decode assert") {
    val bytes = load("assert.bin")

    val decoded = decode(bytes)
    val expected = parse("assert : Natural/even 2 === True")

    assert(decoded.equivalent(expected))
  }

  test("Decode let, 1 variable") {
    val bytes = load("let.bin")

    val decoded = decode(bytes)
    val expected = parse("let x = 1 in [x]")

    assert(decoded.equivalent(expected))
  }

  test("Decode let, >1 variable") {
    val bytes = load("let.bin")

    val decoded = decode(bytes)
    val expected = parse("let x = 1 in [x]")

    assert(decoded.equivalent(expected))
  }

  test("Decode type annotation") {
    val bytes = load("annotation.bin")

    val decoded = decode(bytes)
    val expected = parse("1 : Natural")

    assert(decoded.equivalent(expected))
  }

  test("Decode env import") {
    val bytes = load("env_import.bin")

    val decoded = decode(bytes)
    val expected = parse("env:foo")

    assert(decoded.equivalent(expected))
  }

  test("Decode local import") {
    val bytes = load("local_import.bin")

    val decoded = decode(bytes)
    val expected = parse("~/foo/bar")

    assert(decoded.equivalent(expected))
  }

  test("Decode local import as text") {
    val bytes = load("local_import_text.bin")

    val decoded = decode(bytes)
    val expected = parse("../foo/bar as Text")

    assert(decoded.equivalent(expected))
  }

  test("Decode local import as location") {
    val bytes = load("local_import_location.bin")

    val decoded = decode(bytes)
    val expected = parse("/foo/bar as Location")

    assert(decoded.equivalent(expected))
  }

  test("Decode remote import") {
    val bytes = load("remote_import.bin")

    val decoded = decode(bytes)
    val expected = parse(
      "https://raw.githubusercontent.com/dhall-lang/dhall-lang/master/Prelude/package.dhall?foo=bar using { pwd = \"secret\"}"
    )

    assert(decoded.equivalent(expected))
  }

  test("Decode classpath import") {
    val bytes = parse("let x = classpath:/foo/bar.dhall in x").getEncodedBytes

    val decoded = decode(bytes)
    val expected = parse(
      "let x = classpath:/foo/bar.dhall in x"
    )

    assert(decoded.equivalent(expected))
  }

  test("Decode classpath import as text") {
    val bytes = parse("let x = classpath:/foo/bar.dhall as Text in x").getEncodedBytes

    val decoded = decode(bytes)
    val expected = parse(
      "let x = classpath:/foo/bar.dhall as Text in x"
    )

    assert(decoded.equivalent(expected))
  }

  private def load(resource: String): Array[Byte] =
    Files.readAllBytes(Paths.get(getClass.getResource(s"/binary/$resource").toURI))

}
