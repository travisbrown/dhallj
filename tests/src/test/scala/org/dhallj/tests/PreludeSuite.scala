package org.dhallj.tests

import java.nio.file.Paths
import munit.{FunSuite, Ignore, Slow, TestOptions}
import org.dhallj.imports.mini.Resolver
import org.dhallj.core.Expr
import org.dhallj.parser.Dhall
import scala.io.Source

class PreludeSuite extends FunSuite() {
  val haskellDhallIsAvailable = HaskellDhall.isAvailable()

  val preludeFiles = Source.fromResource(s"Prelude").getLines.toList.sorted.flatMap {
    case "Monoid"        => List("Prelude/Monoid")
    case "README.md"     => Nil
    case "package.dhall" => List("Prelude/package.dhall")
    case other =>
      Source.fromResource(s"Prelude/$other").getLines.toList.sorted.map {
        case file => s"Prelude/$other/$file"
      }
  }

  def slow: Set[String] = Set(
    "Prelude/JSON/render",
    "Prelude/JSON/renderAs",
    "Prelude/JSON/renderYAML",
    "Prelude/JSON/package.dhall",
    "Prelude/package.dhall"
  )

  def checkHash(path: String)(implicit loc: munit.Location): Unit = {
    val content = Source.fromResource(path).getLines.mkString("\n")
    val parsed = Dhall.parse(content)

    val resolved = Resolver.resolveFromResources(parsed, false, Paths.get(path), this.getClass.getClassLoader)

    val name = s"$path hash matches dhall hash"

    val testOptions: TestOptions = if (haskellDhallIsAvailable) {
      if (slow(path)) name.tag(Slow) else name
    } else name.tag(Ignore)

    test(testOptions)(assert(resolved.normalize.alphaNormalize.hash == clue(HaskellDhall.hash(content))))
  }

  preludeFiles.filterNot(_.endsWith("dhall")).foreach(checkHash)
}
