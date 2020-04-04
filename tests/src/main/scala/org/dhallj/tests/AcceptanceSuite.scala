package org.dhallj.tests

import java.nio.file.{Files, Paths}
import munit.{FunSuite, Ignore}
import org.dhallj.core.Expr
import org.dhallj.parser.Dhall
import scala.io.Source

trait AcceptanceSuite[A, E, B] extends FunSuite {
  def base: String

  def loadExpected(input: Array[Byte]): B

  def parseInput(input: String): A
  def transform(input: A): B
  def compare(result: B, expected: B): Boolean

  def isInputFileName(fileName: String): Boolean
  def toName(inputFileName: String): String
  def toExpectedFileName(inputFileName: String): String

  def ignored: Set[String] = Set.empty

  final private def readString(path: String): String =
    new String(readBytes(path))

  final private def readBytes(path: String): Array[Byte] =
    Files.readAllBytes(Paths.get(getClass.getClassLoader.getResource(path).toURI))

  val acceptanceTestFiles = Source.fromResource(s"tests/$base").getLines.toSet

  val acceptanceTestPairs: List[(String, String, B)] = acceptanceTestFiles.filter(isInputFileName).toList.sorted.map {
    case inputFileName =>
      val name = toName(inputFileName)
      val expectedFileName = toExpectedFileName(inputFileName)
      (name, readString(s"tests/$base/$inputFileName"), loadExpected(readBytes(s"tests/$base/$expectedFileName")))
  }

  acceptanceTestPairs.map {
    case (name, input, expected) =>
      if (ignored(name)) {
        test(name.tag(Ignore)) {
          assert(compare(clue(transform(parseInput(clue(input)))), clue(expected)))
        }
      } else {
        test(name) {
          assert(compare(clue(transform(parseInput(clue(input)))), clue(expected)))
        }
      }
  }

}

class ExprTypeCheckingFailureSuite(val base: String) extends FunSuite {
  def isInputFileName(fileName: String): Boolean = fileName.endsWith(".dhall")
  def toName(inputFileName: String): String = inputFileName.dropRight(7)

  val acceptanceTestFiles = Source.fromResource(s"tests/$base").getLines.toSet

  val acceptanceTests = acceptanceTestFiles.filter(isInputFileName).toList.sorted.map {
    case inputFileName =>
      val name = toName(inputFileName)
      (name, Source.fromResource(s"tests/$base/$inputFileName").getLines.mkString("\n"))
  }

  acceptanceTests.map {
    case (name, input) =>
      test(name) {
        intercept[RuntimeException](Dhall.parse(input).typeCheck())
      }
  }

}

class ExprAcceptanceSuite(val base: String, transformation: Expr => Expr) extends AcceptanceSuite[Expr, String, Expr] {
  def parseInput(input: String): Expr = Dhall.parse(input)
  def loadExpected(input: Array[Byte]): Expr = Dhall.parse(new String(input))
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
    extends AcceptanceSuite[Expr, String, String] {
  def loadExpected(input: Array[Byte]): String = new String(input).trim
  def parseInput(input: String): Expr = Dhall.parse(input)
  def transform(input: Expr): String = transformation(input)
  def compare(result: String, expected: String): Boolean = result == expected

  def isInputFileName(fileName: String): Boolean = fileName.endsWith("A.dhall")
  def toName(inputFileName: String): String = inputFileName.dropRight(7)
  def toExpectedFileName(inputFileName: String): String = toName(inputFileName) + "B.hash"
}

class ParserAcceptanceSuite(val base: String) extends AcceptanceSuite[Expr, Array[Byte], Array[Byte]] {
  def loadExpected(input: Array[Byte]): Array[Byte] = input
  def parseInput(input: String): Expr = Dhall.parse(input)
  def transform(input: Expr): Array[Byte] = input.encodeToByteArray
  def compare(result: Array[Byte], expected: Array[Byte]): Boolean = result.sameElements(expected)

  def isInputFileName(fileName: String): Boolean = fileName.endsWith("A.dhall")
  def toName(inputFileName: String): String = inputFileName.dropRight(7)
  def toExpectedFileName(inputFileName: String): String = toName(inputFileName) + "B.dhallb"
}
