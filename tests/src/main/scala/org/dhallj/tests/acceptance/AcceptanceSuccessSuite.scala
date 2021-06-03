package org.dhallj.tests.acceptance

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import java.nio.file.{Path, Paths}

import org.dhallj.core.Expr
import org.dhallj.core.binary.Decode.decode
import org.dhallj.parser.DhallParser

import org.dhallj.imports.{ImportCache, Resolver}
import org.http4s.client._
import org.http4s.blaze.client._

import scala.concurrent.ExecutionContext

trait SuccessSuite[A, B] extends AcceptanceSuite {
  self: Input[A] =>

  def makeExpectedFilename(input: String): String
  final private def makeExpectedPath(inputPath: Path): Path =
    inputPath.resolveSibling(makeExpectedFilename(inputPath.getFileName.toString))

  def transform(input: A): B
  def loadExpected(input: Array[Byte]): B
  def compare(result: B, expected: B): Boolean

  def testPairs: List[(String, Path, (String, B))] = testInputs.map { case (name, path) =>
    (name, path, (readString(path), loadExpected(readBytes(makeExpectedPath(path)))))
  }

  testPairs.foreach { case (name, path, (input, expected)) =>
    test(name) {
      assert(compare(clue(transform(parseInput(path.toString, clue(input)))), clue(expected)))
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
    val parsed = DhallParser.parse(s"./$path")

    if (parsed.isResolved) parsed
    else {
      implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global
      BlazeClientBuilder[IO](ExecutionContext.global).resource
        .use { client =>
          implicit val c: Client[IO] = client
          Resolver.resolve[IO](parsed)
        }
        .unsafeRunSync()
    }
  }

}

trait ResolvingInput extends Input[Expr] {
  def parseInput(path: String, input: String): Expr = {
    //TODO this should only be for import tests (I think)
    val parsed = DhallParser.parse(s"./$path")

    if (parsed.isResolved) parsed
    else {
      implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global
      BlazeClientBuilder[IO](ExecutionContext.global).resource
        .use { client =>
          implicit val c: Client[IO] = client
          Resolver.resolve[IO](new ImportCache.NoopImportCache[IO], new ImportCache.NoopImportCache[IO])(parsed)
        }
        .unsafeRunSync()
    }
  }
}

abstract class ExprOperationAcceptanceSuite(transformation: Expr => Expr) extends SuccessSuite[Expr, Expr] {
  self: Input[Expr] =>

  def makeExpectedFilename(input: String): String = input.dropRight(7) + "B.dhall"

  def transform(input: Expr): Expr = transformation(input)

  def loadExpected(input: Array[Byte]): Expr = DhallParser.parse(new String(input))
  def compare(result: Expr, expected: Expr): Boolean = result.sameStructure(expected) && result.equivalent(expected)
}

class ParsingTypeCheckingSuite(val base: String, override val recurse: Boolean = false)
    extends ExprOperationAcceptanceSuite(Expr.Util.typeCheck(_))
    with ParsingInput
class TypeCheckingSuite(val base: String, override val recurse: Boolean = false)
    extends ExprOperationAcceptanceSuite(Expr.Util.typeCheck(_))
    with ResolvingInput
class AlphaNormalizationSuite(val base: String) extends ExprOperationAcceptanceSuite(_.alphaNormalize) with ParsingInput
class NormalizationSuite(val base: String, override val recurse: Boolean = false)
    extends ExprOperationAcceptanceSuite(_.normalize)
    with CachedResolvingInput
class NormalizationUSuite(val base: String) extends ExprOperationAcceptanceSuite(_.normalize) with ParsingInput

class HashingSuite(val base: String, override val recurse: Boolean = false)
    extends SuccessSuite[Expr, String]
    with ResolvingInput {
  def makeExpectedFilename(input: String): String = input.dropRight(7) + "B.hash"

  def transform(input: Expr): String = input.normalize.alphaNormalize.hash
  def loadExpected(input: Array[Byte]): String = new String(input).trim.drop(7)
  def compare(result: String, expected: String): Boolean = result == expected
}

class ParsingSuite(val base: String) extends SuccessSuite[Expr, Array[Byte]] with ParsingInput {
  def makeExpectedFilename(input: String): String = input.dropRight(7) + "B.dhallb"

  def transform(input: Expr): Array[Byte] = input.getEncodedBytes
  def loadExpected(input: Array[Byte]): Array[Byte] = input
  def compare(result: Array[Byte], expected: Array[Byte]): Boolean = result.sameElements(expected)
}

class BinaryDecodingSuite(val base: String) extends SuccessSuite[Expr, Expr] with ParsingInput {
  def makeExpectedFilename(input: String): String = input.dropRight(8) + "B.dhall"

  override def isInputFileName(fileName: String): Boolean = fileName.endsWith("A.dhallb")

  override def parseInput(path: String, input: String): Expr =
    decode(readBytes(Paths.get(path)))

  def transform(input: Expr): Expr = input
  def loadExpected(input: Array[Byte]): Expr = DhallParser.parse(new String(input))
  def compare(result: Expr, expected: Expr): Boolean = result.sameStructure(expected) && result.equivalent(expected)
}
