package org.dhallj.tests

import munit.FunSuite
import org.dhallj.core.converters.JsonConverter
import org.dhallj.parser.DhallParser

class JsonConverterSuite extends FunSuite() {
  test("toCompactString correctly escapes text") {
    val expr = DhallParser.parse(
      """[{mapKey = " \n \$ \" ", mapValue = " \n \$ \" "}]"""
    )

    assert(clue(JsonConverter.toCompactString(expr)) == clue("""{" \n $ \" ":" \n $ \" "}"""))
  }

  test("toCompactString correctly escapes text from toMap") {
    val expr = DhallParser.parse(
      """toMap {` \n \$ \" ` = " \n \$ \" "}"""
    ).normalize

    assert(clue(JsonConverter.toCompactString(expr)) == clue("""{" \\n \\$ \\\" ":" \n $ \" "}"""))
  }

  test("toCompactString flattens toMap-formatted lists") {
    val expr = DhallParser.parse(
      """[{ mapKey = "foo", mapValue = 1}, {mapKey = "bar", mapValue = 2}]"""
    )

    assert(clue(JsonConverter.toCompactString(expr)) == """{"foo":1,"bar":2}""")
  }

  test("toCompactString flattens toMap-typed empty lists") {
    val expr = DhallParser.parse(
      """[]: List { mapKey: Text, mapValue: Integer }"""
    )

    assert(clue(JsonConverter.toCompactString(expr)) == """{}""")
  }

  test("toCompactString keeps last value in case of toMap-format duplicates") {
    val expr = DhallParser.parse(
      """[{ mapKey = "foo", mapValue = 100}, {mapKey = "foo", mapValue = 2 }]"""
    )

    assert(clue(JsonConverter.toCompactString(expr)) == """{"foo":2}""")
  }

  test("toCompactString doesn't flatten near-toMap-format lists") {
    val expr = DhallParser.parse(
      """[{ mapKey = "foo", mapValue = 1}, {mapKey = "bar", other = 1, mapValue = 2}]"""
    )

    val expected = """[{"mapKey":"foo","mapValue":1},{"mapKey":"bar","other":1,"mapValue":2}]"""

    assert(clue(JsonConverter.toCompactString(expr)) == clue(expected))
  }
}
