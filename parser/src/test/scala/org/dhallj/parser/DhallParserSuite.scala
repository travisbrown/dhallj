package org.dhallj.parser

import munit.{FunSuite, Ignore}
import org.dhallj.core.Expr

class DhallParserSuite extends FunSuite() {
  test("parse empty list with annotation on element type".tag(Ignore)) {
    val expected = Expr.makeEmptyListLiteral(
      Expr.makeApplication(Expr.Constants.LIST, Expr.makeAnnotated(Expr.Constants.NATURAL, Expr.Constants.TYPE))
    )

    assert(DhallParser.parse("[]: List Natural: Type") == expected)
  }
}
