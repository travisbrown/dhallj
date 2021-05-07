package org.dhallj.codec

import munit.FunSuite
import org.dhallj.codec.syntax._
import org.dhallj.syntax._

class DecoderSuite extends FunSuite() {
  test("Decoder[Vector[String]] decodes non-empty List Text") {
    val Right(parsed) = """[ "abc" , "def" ] : List Text""".parseExpr
    assertEquals(parsed.as[Vector[String]], Right(Vector("abc", "def")))
  }

  test("Decoder[Vector[String]] decodes empty List Text") {
    val Right(parsed) = "[] : List Text".parseExpr
    assertEquals(parsed.as[Vector[String]], Right(Vector.empty[String]))
  }

  test("Decoder[List[String]] decodes non-empty List Text") {
    val Right(parsed) = """[ "abc" , "def" ] : List Text""".parseExpr
    assertEquals(parsed.as[List[String]], Right(List("abc", "def")))
  }

  test("Decoder[List[String]] decodes empty List Text") {
    val Right(parsed) = "[] : List Text".parseExpr
    assertEquals(parsed.as[List[String]], Right(List.empty[String]))
  }

  test("Decoder[Option[String]] decodes non-empty Optional Text") {
    val Right(parsed) = """Some "abc" : Optional Text""".parseExpr
    assertEquals(parsed.as[Option[String]], Right(Some("abc")))
  }

  test("Decoder[Option[String]] decodes empty Optional Text") {
    val Right(parsed) = "None Text : Optional Text".parseExpr
    assertEquals(parsed.as[Option[String]], Right(Option.empty[String]))
  }
}
