package org.dhallj.tests.acceptance

import java.nio.file.{Files, Path, Paths}

import munit.{FunSuite, Ignore, Slow}

import scala.collection.JavaConverters._

trait AcceptanceSuite extends FunSuite {
  def prefix: String = "./dhall-lang/tests"

  def base: String
  def recurse: Boolean = false

  def isInputFileName(fileName: String): Boolean = fileName.endsWith("A.dhall")
  def makeName(inputFileName: String): String = inputFileName.dropRight(7)

  /**
   * Test names to ignore.
   */
  def ignored: String => Boolean = Set.empty

  /**
   * Names of tests that are slow.
   */
  def slow: Set[String] = Set.empty

  private def basePath: Path = Paths.get(s"$prefix/$base")

  /**
   * Returns a list of name-path pairs.
   */
  def testInputs: List[(String, Path)] = (
    if (this.recurse) Files.walk(basePath) else Files.list(basePath)
  ).iterator.asScala
    .map(path => (basePath.relativize(path).toString, path))
    .flatMap { case (name, path) =>
      if (isInputFileName(name)) {
        Some((makeName(name), path))
      } else None
    }
    .toList
    .sortBy(_._2)

  final override def munitTests(): Seq[Test] =
    super
      .munitTests()
      .map(test => if (ignored(test.name)) test.tag(Ignore) else if (slow(test.name)) test.tag(Slow) else test)

  final protected def readString(path: Path): String =
    new String(readBytes(path))

  final protected def readBytes(path: Path): Array[Byte] =
    Files.readAllBytes(path)
}
