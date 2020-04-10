package org.dhallj.tests

import munit.{Ignore, ScalaCheckSuite}
import org.dhallj.core.Expr
import org.dhallj.parser.DhallParser
import org.dhallj.testing.WellTypedExpr
import org.dhallj.testing.instances._
import org.scalacheck.{Arbitrary, Prop}

class ToStringSuite extends ScalaCheckSuite() {
  property("toString produces parseable code given well-typed values") {
    Prop.forAll { (expr: WellTypedExpr) =>
      DhallParser.parse(clue(expr.value.toString)) == expr.value
    }
  }

  property("toString produces parseable code".tag(Ignore)) {
    Prop.forAll { (expr: Expr) =>
      DhallParser.parse(clue(expr.toString)) == expr
    }
  }
}