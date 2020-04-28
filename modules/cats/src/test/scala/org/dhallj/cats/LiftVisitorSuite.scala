package org.dhallj.cats

import cats.Applicative
import cats.instances.option._
import munit.ScalaCheckSuite
import org.dhallj.core.Expr
import org.dhallj.testing.instances._
import org.scalacheck.Prop

class ToStringSuite extends ScalaCheckSuite {
  property("LiftVisitor with no overrides is pure") {
    Prop.forAll { (expr: Expr) =>
      val lift = new LiftVisitor[Option](Applicative[Option])

      expr.accept(lift) == Some(expr)
    }
  }
}
