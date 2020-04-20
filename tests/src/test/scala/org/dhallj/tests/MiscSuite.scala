package org.dhallj.tests

import munit.FunSuite
import org.dhallj.ast._
import org.dhallj.core.Expr
import org.dhallj.parser.DhallParser
import org.scalacheck.{Arbitrary, Gen}

class MiscSuite extends CheckersFunSuite() {
  case class AsciiPrintableString(value: String)

  implicit val arbitraryAsciiPrintableString: Arbitrary[AsciiPrintableString] =
    Arbitrary(Gen.alphaNumStr.map(AsciiPrintableString(_)))

  def checkParse(name: String, input: String, expected: Expr)(implicit loc: munit.Location): Unit =
    test(name)(assert(clue(DhallParser.parse(input)).equivalent(clue(expected))))

  def checkBetaNormalization(name: String, input: String, expected: Expr)(implicit loc: munit.Location): Unit =
    test(name)(assert(clue(DhallParser.parse(input).normalize).equivalent(clue(expected))))

  def parsesTo(input: String, expected: Expr): Boolean = DhallParser.parse(input).equivalent(expected)

  testAll1("doubles")((value: Double) => parsesTo(value.toString, DoubleLiteral(value)))
  testAll1("naturals")((value: BigInt) => parsesTo(value.abs.toString, NaturalLiteral(value.abs).get))
  testAll1("strings")((value: AsciiPrintableString) => parsesTo(s""""${value.value}"""", TextLiteral(value.value)))
}
