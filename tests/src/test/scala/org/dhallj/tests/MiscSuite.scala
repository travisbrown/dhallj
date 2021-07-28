package org.dhallj.tests

import java.time.Instant
import munit.ScalaCheckSuite
import org.dhallj.ast._
import org.dhallj.core.binary.Decode
import org.dhallj.core.Expr
import org.dhallj.parser.DhallParser
import org.scalacheck.{Arbitrary, Gen, Prop}

class MiscSuite extends ScalaCheckSuite {
  case class AsciiPrintableString(value: String)

  implicit val arbitraryAsciiPrintableString: Arbitrary[AsciiPrintableString] =
    Arbitrary(Gen.alphaNumStr.map(AsciiPrintableString(_)))

  def checkParse(name: String, input: String, expected: Expr)(implicit loc: munit.Location): Unit =
    test(name)(assert(clue(DhallParser.parse(input)).equivalent(clue(expected))))

  def checkBetaNormalization(name: String, input: String, expected: Expr)(implicit loc: munit.Location): Unit =
    test(name)(assert(clue(DhallParser.parse(input).normalize).equivalent(clue(expected))))

  def parsesTo(input: String, expected: Expr): Boolean = DhallParser.parse(input).equivalent(expected)

  test("getFirstDiff should work for classpath imports") {
    assert(
      Option(Expr.Util.getFirstDiff(DhallParser.parse("classpath:/foo"), DhallParser.parse("classpath:/bar"))).nonEmpty
    )
  }

  property("doubles")(Prop.forAll((value: Double) => parsesTo(value.toString, DoubleLiteral(value))))
  property("naturals")(Prop.forAll((value: BigInt) => parsesTo(value.abs.toString, NaturalLiteral(value.abs).get)))
  property("strings")(
    Prop.forAll((value: AsciiPrintableString) => parsesTo(s""""${value.value}"""", TextLiteral(value.value)))
  )

  property("round-trip instants")(
    Prop.forAll { (value: Instant) =>
      val asString = value.toString
      val parts = asString.split("-")

      // We need a reasonable four-digit year.
      val cleaned = if (parts(0).isEmpty) {
        // If it's negative we just make something up.
        "2021" + "-" + parts.drop(2).mkString("-")
      } else if (parts(0).size != 4) {
        // If it's not four digits we just make something up.
        "1999" + "-" + parts.drop(1).mkString("-")
      } else {
        asString
      }
      val expected = Instant.parse(cleaned)
      val result = Decode.decode(DhallParser.parse(cleaned).getEncodedBytes).toString

      // Keep it low-tech.
      val resultString = result.substring(8, 18) + "T" + result.substring(46).takeWhile(_ != '}') + "Z"

      Instant.parse(resultString) == expected
    }
  )
}
