package org.dhallj.tests

import org.dhallj.core.Expr
import org.dhallj.parser.Dhall
import scala.reflect.ClassTag

abstract class AcceptanceFailureSuite[A, E <: Throwable: ClassTag] extends AcceptanceSuite {
  def loadInput(input: String): A

  testInputs
    .map {
      case (name, path) => (name, readString(path))
    }
    .foreach {
      case (name, input) =>
        test(name) {
          intercept[E](loadInput(input))
        }
    }
}

class ParsingFailureSuite(val base: String) extends AcceptanceFailureSuite[Expr, Exception] {
  def loadInput(input: String): Expr = Dhall.parse(input)
}

class TypeCheckingFailureSuite(val base: String) extends AcceptanceFailureSuite[Expr, RuntimeException] {
  def loadInput(input: String): Expr = Dhall.parse(input).typeCheck
}
