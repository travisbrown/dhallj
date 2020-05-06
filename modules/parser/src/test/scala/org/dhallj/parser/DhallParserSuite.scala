package org.dhallj.parser

import java.net.URI
import java.nio.file.Paths

import munit.{FunSuite, Ignore}
import org.dhallj.core.DhallException.ParsingFailure
import org.dhallj.core.Expr
import org.dhallj.core.Expr.ImportMode

class DhallParserSuite extends FunSuite() {
  test("parse empty list with annotation on element type") {
    val expected = Expr.makeEmptyListLiteral(
      Expr.makeAnnotated(Expr.makeApplication(Expr.Constants.LIST, Expr.Constants.NATURAL), Expr.Constants.TYPE)
    )
    // Output from dhall-haskell.
    val expectedBytes: Array[Byte] = Array(-126, 24, 28, -125, 24, 26, -125, 0, 100, 76, 105, 115, 116, 103, 78, 97,
      116, 117, 114, 97, 108, 100, 84, 121, 112, 101)
    val parsed = DhallParser.parse("[]: List Natural: Type")

    assert(parsed == expected)
    assert(parsed.getEncodedBytes.sameElements(expectedBytes))
  }

  test("parse toMap with empty record with annotation on type") {
    // Output from dhall-haskell.
    val expectedBytes: Array[Byte] = Array(-125, 24, 27, -126, 7, -96, -125, 24, 26, -125, 0, 100, 76, 105, 115, 116,
      -126, 7, -94, 102, 109, 97, 112, 75, 101, 121, 100, 84, 101, 120, 116, 104, 109, 97, 112, 86, 97, 108, 117, 101,
      100, 66, 111, 111, 108, 100, 84, 121, 112, 101)
    val parsed = DhallParser.parse("toMap {}: List { mapKey : Text, mapValue: Bool }: Type")

    assert(parsed.getEncodedBytes.sameElements(expectedBytes))
  }

  test("parse toMap with empty non-record with annotation on type") {
    // Output from dhall-haskell.
    val expectedBytes: Array[Byte] = Array(-125, 24, 27, -126, 8, -95, 97, 97, -11, -125, 24, 26, -125, 0, 100, 76, 105,
      115, 116, -126, 7, -94, 102, 109, 97, 112, 75, 101, 121, 100, 84, 101, 120, 116, 104, 109, 97, 112, 86, 97, 108,
      117, 101, 100, 66, 111, 111, 108, 100, 84, 121, 112, 101)
    val parsed = DhallParser.parse("toMap {a=True}: List { mapKey : Text, mapValue: Bool }: Type")

    assert(parsed.getEncodedBytes.sameElements(expectedBytes))
  }

  test("parse merge with annotation on type") {
    // Output from dhall-haskell.
    val expectedBytes: Array[Byte] = Array(-124, 6, -126, 8, -95, 97, 97, -126, 15, 1, -125, 9, -126, 11, -95, 97, 97,
      -10, 97, 97, -125, 24, 26, 103, 78, 97, 116, 117, 114, 97, 108, 100, 84, 121, 112, 101)
    val parsed = DhallParser.parse("merge {a=1} <a>.a: Natural: Type")

    assert(clue(parsed.getEncodedBytes).sameElements(clue(expectedBytes)))
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

  test("fail on URLs with quoted paths") {
    intercept[ParsingFailure](DhallParser.parse("https://example.com/foo/\"bar?baz\"?qux"))
  }

  test("handle single-quoted escape sequences") {
    val expected = Expr.makeTextLiteral("foo '' bar ${ baz '''' qux")

    val input = """''
foo ''' bar ''${ baz '''''' qux''"""

    assertEquals(DhallParser.parse(input): Expr, expected)

  }
}
