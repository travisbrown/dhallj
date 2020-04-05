package org.dhallj.tests

import java.nio.file.Paths
import org.dhallj.core.Expr
import org.dhallj.imports.mini.Resolver
import org.dhallj.parser.Dhall

trait SuccessSuite[A, B] extends AcceptanceSuite {
  def makeExpectedPath(inputPath: String): String

  def parseInput(input: String): A
  def transform(input: A): B
  def loadExpected(input: Array[Byte]): B
  def compare(result: B, expected: B): Boolean

  def testPairs: List[(String, (String, B))] = testInputs.map {
    case (name, path) =>
      (name, (readString(path), loadExpected(readBytes(makeExpectedPath(path)))))
  }

  testPairs.foreach {
    case (name, (input, expected)) =>
      test(name) {
        assert(compare(clue(transform(parseInput(clue(input)))), clue(expected)))
      }
  }
}

trait ExprAcceptanceSuite[A] extends SuccessSuite[Expr, A] {
  def parseInput(input: String): Expr = Dhall.parse(input)
}

trait ResolvingExprAcceptanceSuite[A] extends SuccessSuite[Expr, A] {
  def parseInput(input: String): Expr = {
    val parsed = Dhall.parse(input)

    if (parsed.isResolved) parsed
    else {
      Resolver.resolveFromResources(parsed, false, Paths.get(s"$prefix/$base"), this.getClass.getClassLoader)
    }
  }
}

abstract class ExprOperationAcceptanceSuite(transformation: Expr => Expr) extends ResolvingExprAcceptanceSuite[Expr] {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.dhall"

  def transform(input: Expr): Expr = transformation(input)
  def loadExpected(input: Array[Byte]): Expr = Dhall.parse(new String(input))
  def compare(result: Expr, expected: Expr): Boolean = result.equivalent(expected)
}

class TypeCheckingSuite(val base: String) extends ExprOperationAcceptanceSuite(_.typeCheck)
class AlphaNormalizationSuite(val base: String) extends ExprOperationAcceptanceSuite(_.alphaNormalize)
class NormalizationSuite(val base: String) extends ExprOperationAcceptanceSuite(_.normalize)

class HashingSuite(val base: String) extends ResolvingExprAcceptanceSuite[String] {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.hash"

  def transform(input: Expr): String = input.normalize.alphaNormalize.hash
  def loadExpected(input: Array[Byte]): String = new String(input).trim.drop(7)
  def compare(result: String, expected: String): Boolean = result == expected
}

class ParsingSuite(val base: String) extends ExprAcceptanceSuite[Array[Byte]] {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.dhallb"

  def transform(input: Expr): Array[Byte] = input.encodeToByteArray
  def loadExpected(input: Array[Byte]): Array[Byte] = input
  def compare(result: Array[Byte], expected: Array[Byte]): Boolean = result.sameElements(expected)
}
