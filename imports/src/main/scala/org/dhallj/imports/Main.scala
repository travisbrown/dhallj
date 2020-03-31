package org.dhallj.imports

import org.dhallj.parser.Dhall.parse
import org.dhallj.core.{Expr, Import}
import java.nio.file.{Path, Paths}
import org.dhallj.imports._
import cats.effect.IO

object Main extends App {

  def withImport(path: Path, code: String): Expr = Expr.makeLet("x", null, Expr.makeLocalImport(path, Import.Mode.CODE, null), parse(code))

  withImport(Paths.get("/tmp/foo.dhall"), "[x, x, x]").resolveImports[IO].unsafeRunSync

}
