package org.dhallj.imports

import cats.effect.{ExitCode, IO, IOApp}
import org.dhallj.parser.DhallParser.parse
import org.http4s.client._
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = BlazeClientBuilder[IO](global).resource.use { client =>
    implicit val c: Client[IO] = client

    for {
      e1 <- IO.pure(parse("let any = http://raw.githubusercontent.com/dhall-lang/dhall-lang/master/Prelude/List/any using ./headers.dhall in any"))
      e2 <- e1.resolveImports[IO]()
      _ <- IO(println(e2.normalize))
    } yield ExitCode.Success
  }

}
