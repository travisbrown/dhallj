package org.dhallj.tests.acceptance

import java.nio.file.{Files, Paths}
import munit.{FunSuite, Ignore, Slow}
import scala.io.Source

trait AcceptanceSuite extends FunSuite {
  def prefix: String = "tests"
  def base: String

  def isInputFileName(fileName: String): Boolean = fileName.endsWith("A.dhall")
  def makeName(inputFileName: String): String = inputFileName.dropRight(7)

  /**
   * Test names to ignore.
   */
  def ignored: Set[String] = Set.empty

  /**
   * Names of tests that are slow.
   */
  def slow: Set[String] = Set.empty

  /**
   * Returns a list of name-path pairs.
   */
  def testInputs: List[(String, String)] = {
    Source
      .fromResource(s"$prefix/$base")
      .getLines
      .filter(isInputFileName)
      .map {
        case inputFileName => (makeName(inputFileName), s"$prefix/$base/$inputFileName")
      }
      .toList
      .sortBy(_._1)
  }

  final override def munitTests(): Seq[Test] =
    super
      .munitTests()
      .map(test => if (ignored(test.name)) test.tag(Ignore) else if (slow(test.name)) test.tag(Slow) else test)

  final protected def readString(path: String): String =
    new String(readBytes(path))

  final protected def readBytes(path: String): Array[Byte] =
    Files.readAllBytes(Paths.get(getClass.getClassLoader.getResource(path).toURI))
}
