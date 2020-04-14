package org.dhallj.tests.acceptance

import cats.effect.{ContextShift, IO}

import java.nio.file.{Files, Paths}
import org.dhallj.core.Expr
import org.dhallj.core.binary.Decode.decode
import org.dhallj.imports.mini.Resolver
import org.dhallj.parser.DhallParser

import org.dhallj.imports._
import org.dhallj.imports.ResolutionConfig
import org.dhallj.imports.ResolutionConfig._
import org.dhallj.imports.Caching._
import org.http4s.client._
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext.global
import scala.io.Source

trait SuccessSuite[A, B] extends AcceptanceSuite {
  def makeExpectedPath(inputPath: String): String

  def parseInput(path: String, input: String): A
  def transform(input: A): B
  def loadExpected(input: Array[Byte]): B
  def compare(result: B, expected: B): Boolean

  def testPairs: List[(String, String, (String, B))] = testInputs.map {
    case (name, path) =>
      (name, path, (readString(path), loadExpected(readBytes(makeExpectedPath(path)))))
  }

  testPairs.foreach {
    case (name, path, (input, expected)) =>
      test(name) {
        assert(compare(clue(transform(parseInput(path, clue(input)))), clue(expected)))
      }
  }
}

trait ExprAcceptanceSuite[A] extends SuccessSuite[Expr, A] {
  def parseInput(path: String, input: String): Expr = DhallParser.parse(input)
}

trait ResolvingExprAcceptanceSuite[A] extends SuccessSuite[Expr, A] {
  def parseInput(path: String, input: String): Expr = {
    val parsed = DhallParser.parse(s"/$path")

    if (parsed.isResolved) parsed
    else {
      implicit val cs: ContextShift[IO] = IO.contextShift(global)
      BlazeClientBuilder[IO](global).resource.use { client =>
        implicit val c: Client[IO] = client
        parsed.accept(ResolveImportsVisitor.mkVisitor(ResolutionConfig(FromResources), NoopImportsCache[IO]))
      }.unsafeRunSync
    }
  }
}

trait CachingResolvingExprAcceptanceSuite[A] extends SuccessSuite[Expr, A] {
  def parseInput(path: String, input: String): Expr = {
    val parsed = DhallParser.parse(s"/$path")

    if (parsed.isResolved) parsed
    else {
      implicit val cs: ContextShift[IO] = IO.contextShift(global)
      BlazeClientBuilder[IO](global).resource.use { client =>
        implicit val c: Client[IO] = client
        parsed.resolveImports[IO](ResolutionConfig(FromResources))
      }.unsafeRunSync
    }
  }
}

abstract class ExprOperationAcceptanceSuite(transformation: Expr => Expr) extends ResolvingExprAcceptanceSuite[Expr] {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.dhall"

  def transform(input: Expr): Expr = transformation(input)

  def loadExpected(input: Array[Byte]): Expr = DhallParser.parse(new String(input))
  def compare(result: Expr, expected: Expr): Boolean = result.sameStructure(expected) && result.equivalent(expected)
}

abstract class CachingExprOperationAcceptanceSuite(transformation: Expr => Expr)
    extends CachingResolvingExprAcceptanceSuite[Expr] {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.dhall"

  def transform(input: Expr): Expr = transformation(input)

  def loadExpected(input: Array[Byte]): Expr = DhallParser.parse(new String(input))
  def compare(result: Expr, expected: Expr): Boolean = result.sameStructure(expected) && result.equivalent(expected)
}

class CachingTypeCheckingSuite(val base: String) extends CachingExprOperationAcceptanceSuite(Expr.Util.typeCheck(_))
class TypeCheckingSuite(val base: String) extends ExprOperationAcceptanceSuite(Expr.Util.typeCheck(_))
class AlphaNormalizationSuite(val base: String) extends CachingExprOperationAcceptanceSuite(_.alphaNormalize)
class NormalizationSuite(val base: String) extends CachingExprOperationAcceptanceSuite(_.normalize)

class HashingSuite(val base: String) extends ResolvingExprAcceptanceSuite[String] {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.hash"

  def transform(input: Expr): String = input.normalize.alphaNormalize.hash
  def loadExpected(input: Array[Byte]): String = new String(input).trim.drop(7)
  def compare(result: String, expected: String): Boolean = result == expected
}

class ParsingSuite(val base: String) extends ExprAcceptanceSuite[Array[Byte]] {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.dhallb"

  def transform(input: Expr): Array[Byte] = input.getEncodedBytes
  def loadExpected(input: Array[Byte]): Array[Byte] = input
  def compare(result: Array[Byte], expected: Array[Byte]): Boolean = result.sameElements(expected)
}

abstract class ExprDecodingAcceptanceSuite(transformation: Expr => Expr) extends ExprAcceptanceSuite[Expr] {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(8) + "B.dhall"

  override def isInputFileName(fileName: String): Boolean = fileName.endsWith("A.dhallb")

  override def parseInput(path: String, input: String): Expr =
    decode(readBytes(path))

  def transform(input: Expr): Expr = transformation(input)
  def loadExpected(input: Array[Byte]): Expr = DhallParser.parse(new String(input))
  def compare(result: Expr, expected: Expr): Boolean = result.sameStructure(expected) && result.equivalent(expected)
}

class BinaryDecodingSuite(val base: String) extends ExprDecodingAcceptanceSuite(identity)
