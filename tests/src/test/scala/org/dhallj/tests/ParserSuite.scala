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

  def checkParse(name: String, input: String, expected: Expr)(implicit loc: munit.Location): Unit =
    test(name)(assert(clue(Dhall.parse(input)).equivalent(clue(expected))))

  def checkBetaNormalization(name: String, input: String, expected: Expr)(implicit loc: munit.Location): Unit =
    test(name)(assert(clue(Dhall.parse(input).normalize).equivalent(clue(expected))))

  def parsesTo(input: String, expected: Expr): Boolean = Dhall.parse(input).equivalent(expected)

  testAll1("doubles")((value: Double) => parsesTo(value.toString, DoubleLiteral(value)))
  testAll1("naturals")((value: BigInt) => parsesTo(value.abs.toString, NaturalLiteral(value.abs)))
  testAll1("strings")((value: AsciiPrintableString) => parsesTo(s""""${value.value}"""", TextLiteral(value.value)))

  // Temporarily matching bug in Haskell implementation.
  checkBetaNormalization(
    "Normalize partially-applied Natural/fold",
    "Natural/fold 0",
    Dhall.parse("λ(natural : Type) → λ(succ : natural → natural) → λ(zero : natural) → zero")
  )

  checkBetaNormalization(
    "Normalize partially-applied Natural/fold safely with two arguments (A)",
    "λ(succ : Type) → Natural/fold 1 succ",
    Dhall.parse("λ(succ : Type) → λ(succ : succ → succ) → λ(zero : succ@1) → succ zero")
  )

  checkBetaNormalization(
    "Normalize partially-applied Natural/fold safely with two arguments (B)",
    "λ(zero : Type) → Natural/fold 1 zero",
    Dhall.parse("λ(zero : Type) → λ(succ : zero → zero) → λ(zero : zero) → succ zero")
  )

  checkBetaNormalization(
    "Normalize partially-applied Natural/fold safely with three arguments (A)",
    "λ(succ : Type) → λ(zero : succ → succ) → Natural/fold 1 succ zero",
    Dhall.parse("λ(succ : Type) → λ(zero : succ → succ) → λ(zero : succ) → zero@1 zero")
  )

  checkBetaNormalization(
    "Normalize partially-applied Natural/fold safely with three arguments (B)",
    "λ(zero : Type) → λ(succ : zero → zero) → Natural/fold 1 zero succ",
    Dhall.parse("λ(zero : Type) → λ(succ : zero → zero) → λ(zero : zero) → succ zero")
  )

  checkBetaNormalization(
    "Normalize partially-applied List/fold",
    "List/fold Natural [0]",
    Dhall.parse("λ(list : Type) → λ(cons : Natural → list → list) → λ(nil : list) → cons 0 nil")
  )

  checkBetaNormalization(
    "Normalize partially-applied List/fold safely with two arguments (A)",
    "λ(list : Natural) → List/fold Natural [list]",
    Dhall.parse(
      "λ(list : Natural) → λ(list : Type) → λ(cons : Natural → list → list) → λ(nil : list) → cons list@1 nil"
    )
  )

  checkBetaNormalization(
    "Normalize partially-applied List/fold safely with two arguments (B)",
    "λ(cons : Natural) → List/fold Natural [cons]",
    Dhall.parse(
      "λ(cons : Natural) → λ(list : Type) → λ(cons : Natural → list → list) → λ(nil : list) → cons cons@1 nil"
    )
  )
}
