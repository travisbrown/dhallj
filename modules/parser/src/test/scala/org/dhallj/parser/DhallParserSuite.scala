package org.dhallj.parser

import java.net.URI
import java.nio.file.Paths

import munit.{FunSuite, Ignore}
import org.dhallj.core.Expr
import org.dhallj.core.Expr.ImportMode

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

  test("parse $ in double-quoted text literals") {
    val expected = Expr.makeTextLiteral("$ $ $100 $ $")

    assert(DhallParser.parse("""let x = "100" in "$ $ $${x} $ $" """) == expected)
  }

  test("parse # in double-quoted text literals") {
    val expected = Expr.makeTextLiteral("# # # $ % ^ #")

    assert(DhallParser.parse(""""# # # $ % ^ #"""") == expected)
  }

  test("parse classpath import") {
    val expected = Expr.makeClasspathImport(Paths.get("/foo/bar.dhall"), ImportMode.RAW_TEXT, null)

    assert(DhallParser.parse("classpath:/foo/bar.dhall as Text") == expected)
  }
}
