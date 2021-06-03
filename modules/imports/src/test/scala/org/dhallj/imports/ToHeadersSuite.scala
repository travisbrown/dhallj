package org.dhallj.imports

import munit.FunSuite
import org.dhallj.core.Expr
import org.http4s.{Header, Headers}
import org.typelevel.ci._

import scala.collection.JavaConverters._

class ToHeadersSuite extends FunSuite {

  test("Success case") {
    val expr = Expr.makeNonEmptyListLiteral(
      Array(
        Expr.makeRecordLiteral(
          Map("header" -> Expr.makeTextLiteral("foo"), "value" -> Expr.makeTextLiteral("bar")).asJava.entrySet()
        ),
        Expr.makeRecordLiteral(
          Map("header" -> Expr.makeTextLiteral("baz"), "value" -> Expr.makeTextLiteral("x")).asJava.entrySet()
        )
      )
    )

    val expected = Headers(
      List(
        Header.Raw(ci"foo", "bar"),
        Header.Raw(ci"baz", "x")
      )
    )

    assertEquals(ToHeaders(expr), expected)
  }

  test("Success case 2") {
    val expr = Expr.makeNonEmptyListLiteral(
      Array(
        Expr.makeRecordLiteral(
          Map("mapKey" -> Expr.makeTextLiteral("foo"), "mapValue" -> Expr.makeTextLiteral("bar")).asJava.entrySet()
        ),
        Expr.makeRecordLiteral(
          Map("mapKey" -> Expr.makeTextLiteral("baz"), "mapValue" -> Expr.makeTextLiteral("x")).asJava.entrySet()
        )
      )
    )

    val expected = Headers(
      List(
        Header.Raw(ci"foo", "bar"),
        Header.Raw(ci"baz", "x")
      )
    )

    assertEquals(ToHeaders(expr), expected)
  }

  test("Failure case 1") {
    val expr = Expr.makeNonEmptyListLiteral(
      Array(
        Expr.makeRecordLiteral(
          Map("header" -> Expr.makeTextLiteral("foo"), "mapValue" -> Expr.makeTextLiteral("bar")).asJava.entrySet()
        ),
        Expr.makeRecordLiteral(
          Map("mapKey" -> Expr.makeTextLiteral("baz"), "value" -> Expr.makeTextLiteral("x")).asJava.entrySet()
        )
      )
    )

    val expected = Headers(Nil)

    assertEquals(ToHeaders(expr), expected)
  }

  test("Failure case 2") {
    val expr = Expr.makeTextLiteral("foo")

    val expected = Headers(Nil)

    assertEquals(ToHeaders(expr), expected)
  }

}
