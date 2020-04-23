package org.dhallj.yaml

import munit.ScalaCheckSuite
import org.dhallj.ast._
import org.dhallj.core.Expr
import org.dhallj.parser.DhallParser
import org.scalacheck.Prop
import org.yaml.snakeyaml.DumperOptions

class YamlConverterSuite extends ScalaCheckSuite {
  property("convert integers") {
    Prop.forAll { (value: BigInt) =>
      val asDhall = IntegerLiteral(value.underlying)
      Option(YamlConverter.toYamlString(asDhall)) == Some(value.toString + "\n")
    }
  }

  test("convert nested lists") {
    val expr = DhallParser.parse("[[]: List Bool]")

    assert(clue(Option(YamlConverter.toYamlString(expr))) == Some("- []\n"))
  }

  test("convert None") {
    val expr = DhallParser.parse("None Bool")

    assert(clue(Option(YamlConverter.toYamlString(expr))) == Some("null\n"))
  }

  test("convert Some") {
    val expr = DhallParser.parse("""Some "foo"""")

    assert(clue(Option(YamlConverter.toYamlString(expr))) == Some("foo\n"))
  }

  test("convert records") {
    val expr1 = DhallParser.parse("{foo = [{bar = [1]}, {bar = [1, 2, 3]}]}")
    val expected =
      """|foo:
         |- bar:
         |  - 1
         |- bar:
         |  - 1
         |  - 2
         |  - 3
         |""".stripMargin

    assert(clue(Option(YamlConverter.toYamlString(expr1))) == Some(expected))
  }

  test("convert unions (nullary constructors)") {
    val expr1 = DhallParser.parse("[((\\(x: Natural) -> <foo: Bool|bar>) 1).bar]").normalize()

    assert(clue(Option(YamlConverter.toYamlString(expr1))) == Some("- bar\n"))
  }

  test("convert unions") {
    val expr1 = DhallParser.parse("[<foo: Bool|bar>.foo True]").normalize()

    assert(clue(Option(YamlConverter.toYamlString(expr1))) == Some("- true\n"))
  }

  test("convert text containing newlines") {
    val expr1 = DhallParser.parse(""" { a = "foo\nbar" } """).normalize()

    assert(clue(Option(YamlConverter.toYamlString(expr1))) == Some("a: |-\n  foo\n  bar\n"))
  }

  test("convert test containing quotes") {
    val expr1 = DhallParser.parse(""" { a = "\"" } """).normalize()
    val expr2 = DhallParser.parse(""" { a = "\"\n" } """).normalize()

    assert(clue(Option(YamlConverter.toYamlString(expr1))) == Some("a: '\"'\n"))
    assert(clue(Option(YamlConverter.toYamlString(expr2))) == Some("a: |\n  \"\n"))
  }

  test("convert text containing newlines and respect SnakeYAML configuration") {
    val expr1 = DhallParser.parse(""" { a = "foo\nbar" } """).normalize()
    val expected = "\"a\": \"foo\\nbar\"\n"

    val options = new DumperOptions()
    options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED)

    assert(clue(Option(YamlConverter.toYamlString(expr1, options))) == clue(Some(expected)))
  }

  test("fail safely on unconvertible expressions") {
    val expr1 = Lambda("x", Expr.Constants.NATURAL, Identifier("x"))

    assert(Option(YamlConverter.toYamlString(expr1)) == None)
  }
}
