package org.dhallj.imports

import java.net.URI
import java.nio.file.Path

import cats.effect.{ExitCode, IO, IOApp}
import org.dhallj.core.{Expr, Import}
import org.dhallj.parser.Dhall.parse
import org.http4s.client._
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = BlazeClientBuilder[IO](global).resource.use { client =>
    implicit val c: Client[IO] = client

    for {
      e1 <- IO.pure(withRemoteImport(new URI("https://raw.githubusercontent.com/dhall-lang/dhall-lang/master/Prelude/List/any"), "x Natural Natural/even [ 2, 3, 5 ]"))
      e2 <- e1.resolveImports[IO]
      _ <- IO(println(e2.normalize))
    } yield ExitCode.Success
  }

  def withLocalImport(path: Path, code: String): Expr = Expr.makeLet("x", null, Expr.makeLocalImport(path, Import.Mode.CODE, null), parse(code))

  def withRemoteImport(uri: URI, code: String): Expr = Expr.makeLet("x", null, Expr.makeRemoteImport(uri, null, Import.Mode.CODE, null), parse(code))

}
