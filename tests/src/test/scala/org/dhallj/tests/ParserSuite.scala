package org.dhallj.tests

import munit.FunSuite
import org.dhallj.core.Expr
import org.dhallj.parser.Dhall
import org.dhallj.s.ast._
import org.scalacheck.{Arbitrary, Gen}

class ParserSuite extends CheckersFunSuite() {
  case class AsciiPrintableString(value: String)

  implicit val arbitraryAsciiPrintableString: Arbitrary[AsciiPrintableString] =
    Arbitrary(Gen.alphaNumStr.map(AsciiPrintableString(_)))

  def checkParse(input: String, expected: Expr, name: String)(implicit loc: munit.Location): Unit =
    test(name)(assert(Dhall.parse(input).same(expected)))

  def parsesTo(input: String, expected: Expr): Boolean = Dhall.parse(input).same(expected)

  testAll1("doubles")((value: Double) => parsesTo(value.toString, DoubleLiteral(value)))
  testAll1("naturals")((value: BigInt) => parsesTo(value.abs.toString, NaturalLiteral(value.abs)))
  testAll1("strings")((value: AsciiPrintableString) => parsesTo(s""""${value.value}"""", TextLiteral(value.value)))
}
