package org.dhallj.yaml

import munit.ScalaCheckSuite
import org.dhallj.ast._
import org.dhallj.core.Expr
import org.dhallj.parser.DhallParser
import org.scalacheck.Prop

class JawnConverterSuite extends ScalaCheckSuite {
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

  test("fail safely on unconvertible expressions") {
    val expr1 = Lambda("x", Expr.Constants.NATURAL, Identifier("x"))

    assert(Option(YamlConverter.toYamlString(expr1)) == None)
  }
}
