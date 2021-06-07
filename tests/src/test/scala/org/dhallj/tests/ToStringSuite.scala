package org.dhallj.tests

import munit.{Ignore, ScalaCheckSuite, Slow}
import org.dhallj.core.{Expr, Operator}
import org.dhallj.parser.DhallParser
import org.dhallj.testing.WellTypedExpr
import org.dhallj.testing.instances._
import org.scalacheck.Prop
import scala.util.Try

class ToStringSuite extends ScalaCheckSuite() {
  property("toString produces parseable code given well-typed values") {
    Prop.forAll((expr: WellTypedExpr) => DhallParser.parse(clue(expr.value.toString)) == expr.value)
  }

  property("toString produces parseable code") {
    Prop.forAll((expr: Expr) => DhallParser.parse(clue(expr.toString)) == expr)
  }

  property("toString produces parseable code for sequence of operators") {
    Prop.forAll { (ops: Vector[Operator]) =>
      val x = Expr.makeIdentifier("x")
      val expr1 = ops.take(16).foldLeft(x) { case (acc, op) => Expr.makeOperatorApplication(op, x, acc) }
      val expr2 = ops.take(16).foldLeft(x) { case (acc, op) => Expr.makeOperatorApplication(op, acc, x) }
      DhallParser.parse(clue(expr1.toString)) == expr1 && DhallParser.parse(clue(expr2.toString)) == expr2
    }
  }

  // TODO: make this pass (e.g. seed rBdMB22PVU38DRrcK3rrh1kEBDitFj8dL1tbCloBmgM=)
  property("toString doesn't introduce unnecessary parentheses".tag(Ignore)) {
    Prop.forAll { (expr: Expr) =>
      val asString = expr.toString

      DhallParser.parse(clue(expr.toString)) == expr && removeParentheses(asString).forall { smaller =>
        !Try(DhallParser.parse(smaller)).toOption.exists(_ == expr)
      }
    }
  }

  test("Unnormalized Prelude should round-trip through toString".tag(Slow)) {
    import org.dhallj.syntax._

    val Right(prelude) = "./dhall-lang/Prelude/package.dhall".parseExpr.flatMap(_.resolve)
    val Right(fromToString) = prelude.toString.parseExpr

    assert(prelude.hash.sameElements(fromToString.hash))
    assert(prelude.diff(fromToString).isEmpty)
  }

  private def closingIndex(input: String, startIndex: Int): Int = {
    var i = startIndex + 1
    var depth = 1

    while (i < input.length && depth > 0) {
      if (input.charAt(i) == ')') {
        depth -= 1
      } else if (input.charAt(i) == '(') {
        depth += 1
      }
      i += 1
    }

    i - 1
  }

  // Assumes valid nesting
  private def removeParentheses(input: String, first: Int = 0): List[String] = {
    val nextIndex = input.indexOf("(", first)

    if (nextIndex < 0) {
      Nil
    } else {
      val end = closingIndex(input, nextIndex)
      val next = input.take(nextIndex) + input.substring(nextIndex + 1, end) + input.substring(end + 1)

      next :: removeParentheses(input, nextIndex + 1)
    }
  }
}
