package org.dhallj.parser

import java.net.URI
import munit.{FunSuite, Ignore}
import org.dhallj.core.Expr

class DhallParserSuite extends FunSuite() {
  test("parse empty list with annotation on element type".tag(Ignore)) {
    val expected = Expr.makeEmptyListLiteral(
      Expr.makeApplication(Expr.Constants.LIST, Expr.makeAnnotated(Expr.Constants.NATURAL, Expr.Constants.TYPE))
    )

    assert(DhallParser.parse("[]: List Natural: Type") == expected)
  }

  test("parse IPv6 address") {
    val expected = Expr.makeRemoteImport(new URI("https://[0:0:0:0:0:0:0:1]/"), null, Expr.ImportMode.CODE, null)

    assert(DhallParser.parse("https://[0:0:0:0:0:0:0:1]/") == expected)
  }
}
