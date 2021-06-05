package org.dhallj.tests

import munit.{ScalaCheckSuite, Slow}
import org.dhallj.core.Expr
import org.dhallj.parser.DhallParser
import org.dhallj.testing.WellTypedExpr
import org.dhallj.testing.instances._
import org.scalacheck.Prop

class ToStringSuite extends ScalaCheckSuite() {
  property("toString produces parseable code given well-typed values") {
    Prop.forAll((expr: WellTypedExpr) => DhallParser.parse(clue(expr.value.toString)) == expr.value)
  }

  property("toString produces parseable code") {
    Prop.forAll((expr: Expr) => DhallParser.parse(clue(expr.toString)) == expr)
  }

  test("Unnormalized Prelude should round-trip through toString".tag(Slow)) {
    import org.dhallj.syntax._

    val Right(prelude) = "./dhall-lang/Prelude/package.dhall".parseExpr.flatMap(_.resolve)
    val Right(fromToString) = prelude.toString.parseExpr

    assert(prelude.hash.sameElements(fromToString.hash))
    assert(prelude.diff(fromToString).isEmpty)
  }
}
