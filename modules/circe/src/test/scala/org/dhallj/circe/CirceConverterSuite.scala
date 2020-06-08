package org.dhallj.circe

import io.circe.Json
import io.circe.syntax._
import io.circe.testing.instances._
import munit.{Ignore, ScalaCheckSuite}
import org.dhallj.ast._
import org.dhallj.core.Expr
import org.dhallj.parser.DhallParser
import org.scalacheck.Prop

class CirceConverterSuite extends ScalaCheckSuite {
  property("round-trip Json values through Dhall expressions") {
    Prop.forAll { (value: Json) =>
      val cleanedJson = value.foldWith(JsonCleaner)
      val asDhall = Converter(cleanedJson)
      Converter(asDhall) == Some(cleanedJson)
    }
  }

  property("convert integers") {
    Prop.forAll { (value: BigInt) =>
      val asDhall = IntegerLiteral(value.underlying)
      Converter(asDhall) == Some(value.asJson)
    }
  }

  property("convert lists of integers") {
    Prop.forAll { (values: Vector[BigInt]) =>
      val asDhall = if (values.isEmpty) {
        EmptyListLiteral(Expr.Constants.INTEGER)
      } else {
        val exprs = values.map(value => IntegerLiteral(value.underlying))
        NonEmptyListLiteral(exprs.head, exprs.tail)
      }
      Converter(asDhall) == Some(values.asJson)
    }
  }

  property("convert lists of doubles") {
    Prop.forAll { (values: Vector[Double]) =>
      val asDhall = if (values.isEmpty) {
        EmptyListLiteral(Expr.Constants.DOUBLE)
      } else {
        val exprs = values.map(DoubleLiteral(_))
        NonEmptyListLiteral(exprs.head, exprs.tail)
      }
      Converter(asDhall) == Some(values.asJson)
    }
  }

  property("convert lists of booleans") {
    Prop.forAll { (values: Vector[Boolean]) =>
      val asDhall = if (values.isEmpty) {
        EmptyListLiteral(Expr.Constants.BOOL)
      } else {
        val exprs = values.map(BoolLiteral(_))
        NonEmptyListLiteral(exprs.head, exprs.tail)
      }
      Converter(asDhall) == Some(values.asJson)
    }
  }

  test("convert nested lists") {
    val expr = DhallParser.parse("[[]: List Bool]")

    assert(Converter(expr) == Some(Json.arr(Json.arr())))
  }

  test("convert None") {
    val expr = DhallParser.parse("None Bool")

    assert(Converter(expr) == Some(Json.Null))
  }

  test("convert Some") {
    val expr = DhallParser.parse("""Some "foo"""")

    assert(Converter(expr) == Some(Json.fromString("foo")))
  }

  test("convert records") {
    val expr1 = DhallParser.parse("{foo = [{bar = [1]}, {bar = [1, 2, 3]}]}")
    val Right(json1) = io.circe.jawn.parse("""{"foo": [{"bar": [1]}, {"bar": [1, 2, 3]}]}""")

    assert(Converter(expr1) == Some(json1))
  }

  test("convert unions (nullary constructors)") {
    val expr1 = DhallParser.parse("[((\\(x: Natural) -> <foo: Bool|bar>) 1).bar]").normalize()
    val Right(json1) = io.circe.jawn.parse("""["bar"]""")

    assert(Converter(expr1) == Some(json1))
  }

  test("convert unions") {
    val expr1 = DhallParser.parse("[<foo: Bool|bar>.foo True]").normalize()
    val Right(json1) = io.circe.jawn.parse("""[true]""")

    assert(Converter(expr1) == Some(json1))
  }

  test("fail safely on unconvertible expressions") {
    val expr1 = Lambda("x", Expr.Constants.NATURAL, Identifier("x"))

    assert(Converter(expr1) == None)
  }
}
