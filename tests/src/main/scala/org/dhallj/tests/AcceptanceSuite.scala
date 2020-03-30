package org.dhallj.tests

import munit.FunSuite
import org.dhallj.core.Expr
import org.dhallj.parser.Dhall
import scala.io.Source

trait AcceptanceSuite[A, B] extends FunSuite {
  def base: String

  def parseInput(input: String): A
  def parseExpected(input: String): B
  def transform(input: A): B
  def compare(result: B, expected: B): Boolean

  def isInputFileName(fileName: String): Boolean
  def toName(inputFileName: String): String
  def toExpectedFileName(inputFileName: String): String

  val acceptanceTestFiles = Source.fromResource(base).getLines.toSet

  val acceptanceTestPairs = acceptanceTestFiles.filter(isInputFileName).toList.sorted.map {
    case inputFileName =>
      val name = toName(inputFileName)
      val expectedFileName = toExpectedFileName(inputFileName)
      (name,
        Source.fromResource(s"$base/$inputFileName").getLines.mkString("\n"),
        Source.fromResource(s"$base/$expectedFileName").getLines.mkString("\n"))
  }

  acceptanceTestPairs.map {
    case (name, input, expected) =>
      test(name) {
        assert(compare(transform(parseInput(clue(input))), parseExpected(clue(expected))))
      }
  }

}

class ExprTypeCheckingFailureSuite(val base: String) extends FunSuite {
  def isInputFileName(fileName: String): Boolean = fileName.endsWith(".dhall")
  def toName(inputFileName: String): String = inputFileName.dropRight(7)

  val acceptanceTestFiles = Source.fromResource(base).getLines.toSet

  val acceptanceTests = acceptanceTestFiles.filter(isInputFileName).toList.sorted.map {
    case inputFileName =>
      val name = toName(inputFileName)
      (name,
        Source.fromResource(s"$base/$inputFileName").getLines.mkString("\n"))
  }

  acceptanceTests.map {
    case (name, input) =>
      test(name) {
        intercept[RuntimeException](Dhall.parse(input).typeCheck())
      }
  }

}

class ExprAcceptanceSuite(val base: String, transformation: Expr => Expr) extends AcceptanceSuite[Expr, Expr] {
  def parseInput(input: String): Expr = Dhall.parse(input)
  def parseExpected(input: String): Expr = Dhall.parse(input)
  def transform(input: Expr): Expr = transformation(input)
  def compare(result: Expr, expected: Expr): Boolean = result.equivalent(expected)

  def isInputFileName(fileName: String): Boolean = fileName.endsWith("A.dhall")
  def toName(inputFileName: String): String = inputFileName.dropRight(7)
  def toExpectedFileName(inputFileName: String): String = toName(inputFileName) + "B.dhall"
}

class ExprNormalizationSuite(base: String) extends ExprAcceptanceSuite(base, _.normalize)
class ExprTypeCheckingSuite(base: String) extends ExprAcceptanceSuite(base, _.typeCheck)

class HashAcceptanceSuite(val base: String,
                          transformation: Expr => String = expr => s"sha256:${expr.normalize.alphaNormalize.hash}")
    extends AcceptanceSuite[Expr, String] {
  def parseInput(input: String): Expr = Dhall.parse(input)
  def parseExpected(input: String): String = input
  def transform(input: Expr): String = transformation(input)
  def compare(result: String, expected: String): Boolean = result == expected

  def isInputFileName(fileName: String): Boolean = fileName.endsWith("A.dhall")
  def toName(inputFileName: String): String = inputFileName.dropRight(7)
  def toExpectedFileName(inputFileName: String): String = toName(inputFileName) + "B.hash"
}
