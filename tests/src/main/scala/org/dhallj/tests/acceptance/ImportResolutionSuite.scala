package org.dhallj.tests.acceptance

import java.nio.file.{Files, Paths}

import cats.effect.IO
import cats.effect.unsafe.IORuntime

import org.dhallj.core.Expr
import org.dhallj.imports.{ImportCache, Resolver}
import org.dhallj.parser.DhallParser

import org.http4s.client._
import org.http4s.blaze.client._

import scala.concurrent.ExecutionContext.global
import scala.io.Source

class ImportResolutionSuite(val base: String)
    extends ExprOperationAcceptanceSuite(_.normalize)
    with CachedResolvingInput {

  setEnv("DHALL_TEST_VAR", "6 * 7") //Yes, this is SUPER hacky but the JVM doesn't really support setting env vars

  override def parseInput(path: String, input: String): Expr = {
    val parsed = DhallParser.parse(s"./$path")

    if (parsed.isResolved) parsed
    else {
      implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global
      val cache = initializeCache
      BlazeClientBuilder[IO](global).resource
        .use { client =>
          implicit val c: Client[IO] = client
          Resolver.resolve[IO](cache, new ImportCache.NoopImportCache[IO])(parsed)
        }
        .unsafeRunSync()
    }
  }

  private def initializeCache: ImportCache[IO] = {
    implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global
    val dir = Files.createTempDirectory("dhallj")
    val cache = ImportCache[IO](dir).unsafeRunSync().get
    Source
      .fromResource("tests/import/cache/dhall")
      .getLines()
      .foreach { p =>
        val content = readBytes(Paths.get(s"dhall-lang/tests/import/cache/dhall/$p"))
        Files.write(dir.resolve(p), content)
      }
    cache
  }

  def setEnv(key: String, value: String): Unit = {
    val field = System.getenv().getClass.getDeclaredField("m")
    field.setAccessible(true)
    val map = field.get(System.getenv()).asInstanceOf[java.util.Map[java.lang.String, java.lang.String]]
    map.put(key, value)
  }
}
