package org.dhallj.tests

import java.nio.file.{Path, Paths}

import cats.effect.{ContextShift, IO, Resource}
import munit.FunSuite
import org.dhallj.core.Expr
import org.dhallj.imports.syntax._
import org.dhallj.parser.DhallParser.parse
import org.http4s.client._
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext.global

class ImportResolutionSuite extends FunSuite {

  implicit val cs: ContextShift[IO] = IO.contextShift(global)

  implicit val client: Resource[IO, Client[IO]] = BlazeClientBuilder[IO](global).resource

  test("Resolve with different base directory") {
    //Path inside dhall-lang submodule
    val expr = parse("let x = ./success/prelude/Bool/and/0B.dhall in x")
    val expected = parse("Bool").normalize

    assert(resolveRelativeTo(Paths.get("./dhall-lang/tests/type-inference"))(expr) == expected)
  }

  private def resolveRelativeTo(relativeTo: Path)(e: Expr): Expr =
    client
      .use { c =>
        implicit val http: Client[IO] = c

        e.resolveImportsRelativeTo[IO](relativeTo).map(_.normalize)
      }
      .unsafeRunSync()

}
