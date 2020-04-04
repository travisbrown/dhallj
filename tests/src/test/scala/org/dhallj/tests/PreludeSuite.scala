package org.dhallj.tests

import munit.FunSuite
import org.dhallj.core.Expr
import org.dhallj.parser.Dhall
import scala.io.Source

class PreludeSuite extends FunSuite() {

  val preludeFiles = Source.fromResource(s"Prelude").getLines.toList.sorted.flatMap {
    case "Monoid"        => List("Prelude/Monoid")
    case "README.md"     => Nil
    case "package.dhall" => List("Prelude/package.dhall")
    case other =>
      Source.fromResource(s"Prelude/$other").getLines.toList.sorted.map {
        case file => s"Prelude/$other/$file"
      }
  }

  def checkHash(name: String)(implicit loc: munit.Location): Unit = {
    val content = Source.fromResource(name).getLines.mkString("\n")
    test(name)(assert(Dhall.parse(content).normalize.alphaNormalize.hash == clue(HaskellDhall.hash(content))))
  }

  //preludeFiles.filterNot(_.endsWith("dhall")).foreach(checkHash)
}
