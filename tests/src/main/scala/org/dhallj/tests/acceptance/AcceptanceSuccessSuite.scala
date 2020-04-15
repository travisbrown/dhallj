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
  self: Input[A] =>

  def makeExpectedPath(inputPath: String): String

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

trait Input[A] {
  def parseInput(path: String, input: String): A

}

trait ParsingInput extends Input[Expr] {

  override def parseInput(path: String, input: String): Expr = DhallParser.parse(input)

}

trait CachedResolvingInput extends Input[Expr] {

  override def parseInput(path: String, input: String): Expr = {
    //TODO this should only be for import tests (I think)
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

trait ResolvingInput extends Input[Expr] {
  def parseInput(path: String, input: String): Expr = {
    //TODO this should only be for import tests (I think)
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

abstract class ExprOperationAcceptanceSuite(transformation: Expr => Expr) extends SuccessSuite[Expr, Expr] {
  self: Input[Expr] =>

  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.dhall"

  def transform(input: Expr): Expr = transformation(input)

  def loadExpected(input: Array[Byte]): Expr = DhallParser.parse(new String(input))
  def compare(result: Expr, expected: Expr): Boolean = result.sameStructure(expected) && result.equivalent(expected)
}

class CachingTypeCheckingSuite(val base: String)
    extends ExprOperationAcceptanceSuite(Expr.Util.typeCheck(_))
    with CachedResolvingInput
class TypeCheckingSuite(val base: String)
    extends ExprOperationAcceptanceSuite(Expr.Util.typeCheck(_))
    with ResolvingInput
class AlphaNormalizationSuite(val base: String) extends ExprOperationAcceptanceSuite(_.alphaNormalize) with ParsingInput
class NormalizationSuite(val base: String) extends ExprOperationAcceptanceSuite(_.normalize) with CachedResolvingInput
class NormalizationUSuite(val base: String) extends ExprOperationAcceptanceSuite(_.normalize) with ParsingInput

class HashingSuite(val base: String) extends SuccessSuite[Expr, String] with CachedResolvingInput {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.hash"

  def transform(input: Expr): String = input.normalize.alphaNormalize.hash
  def loadExpected(input: Array[Byte]): String = new String(input).trim.drop(7)
  def compare(result: String, expected: String): Boolean = result == expected
}

class ParsingSuite(val base: String) extends SuccessSuite[Expr, Array[Byte]] with ParsingInput {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(7) + "B.dhallb"

  def transform(input: Expr): Array[Byte] = input.getEncodedBytes
  def loadExpected(input: Array[Byte]): Array[Byte] = input
  def compare(result: Array[Byte], expected: Array[Byte]): Boolean = result.sameElements(expected)
}

class BinaryDecodingSuite(val base: String) extends SuccessSuite[Expr, Expr] with ParsingInput {
  def makeExpectedPath(inputPath: String): String = inputPath.dropRight(8) + "B.dhall"

  override def isInputFileName(fileName: String): Boolean = fileName.endsWith("A.dhallb")

  override def parseInput(path: String, input: String): Expr =
    decode(readBytes(path))

  def transform(input: Expr): Expr = input
  def loadExpected(input: Array[Byte]): Expr = DhallParser.parse(new String(input))
  def compare(result: Expr, expected: Expr): Boolean = result.sameStructure(expected) && result.equivalent(expected)
}
