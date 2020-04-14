package org.dhallj.tests.acceptance

import java.nio.file.{Files, Paths}

import cats.effect.{IO, ContextShift}

import org.dhallj.core.Expr
import org.dhallj.core.binary.Decode.decode
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
        //      Resolver.resolveFromResources(parsed, false, Paths.get(path), this.getClass.getClassLoader)
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

class TypeCheckingSuite(val base: String) extends ExprOperationAcceptanceSuite(Expr.Util.typeCheck(_))
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

class ImportResolutionSuite(val base: String) extends ExprOperationAcceptanceSuite(_.normalize) {

  setEnv("DHALL_TEST_VAR", "6 * 7") //Yes, this is SUPER hacky but the JVM doesn't really support setting env vars

  override def parseInput(path: String, input: String): Expr = {
    val parsed = DhallParser.parse(s"/$path")

    if (parsed.isResolved) parsed
    else {
      implicit val cs: ContextShift[IO] = IO.contextShift(global)
      val cache = initializeCache
      BlazeClientBuilder[IO](global).resource.use { client =>
        implicit val c: Client[IO] = client
        parsed.accept(ResolveImportsVisitor(ResolutionConfig(FromResources), cache, Nil))
      }.unsafeRunSync
    }
  }

  private def initializeCache: ImportsCache[IO] = {
    val dir = Files.createTempDirectory("dhallj")
    val cache = Caching.mkImportsCache[IO](dir).unsafeRunSync.get
    Source.fromResource("tests/import/cache/dhall")
      .getLines
      .foreach(p => {
        val content = readBytes(s"tests/import/cache/dhall/$p")
        Files.write(dir.resolve(p), content)
      })
    cache
  }

  def setEnv(key: String, value: String) = {
    val field = System.getenv().getClass.getDeclaredField("m")
    field.setAccessible(true)
    val map = field.get(System.getenv()).asInstanceOf[java.util.Map[java.lang.String, java.lang.String]]
    map.put(key, value)
  }
}
