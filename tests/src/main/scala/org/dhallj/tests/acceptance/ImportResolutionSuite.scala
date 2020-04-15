package org.dhallj.tests.acceptance

import java.nio.file.Files

import cats.effect.{ContextShift, IO}

import org.dhallj.core.Expr
import org.dhallj.imports._
import org.dhallj.imports.ResolutionConfig
import org.dhallj.imports.ResolutionConfig._
import org.dhallj.imports.Caching._
import org.dhallj.parser.DhallParser

import org.http4s.client._
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext.global
import scala.io.Source

class ImportResolutionSuite(val base: String) extends ExprOperationAcceptanceSuite(_.normalize) with CachedResolvingInput {

  setEnv("DHALL_TEST_VAR", "6 * 7") //Yes, this is SUPER hacky but the JVM doesn't really support setting env vars

  override def parseInput(path: String, input: String): Expr = {
    val parsed = DhallParser.parse(s"/$path")

    if (parsed.isResolved) parsed
    else {
      implicit val cs: ContextShift[IO] = IO.contextShift(global)
      val cache = initializeCache
      BlazeClientBuilder[IO](global).resource.use { client =>
        implicit val c: Client[IO] = client
        parsed.accept(ResolveImportsVisitor.mkVisitor(ResolutionConfig(FromResources), cache))
      }.unsafeRunSync
    }
  }

  private def initializeCache: ImportsCache[IO] = {
    val dir = Files.createTempDirectory("dhallj")
    val cache = Caching.mkImportsCache[IO](dir).unsafeRunSync.get
    Source
      .fromResource("tests/import/cache/dhall")
      .getLines
      .foreach { p =>
        val content = readBytes(s"tests/import/cache/dhall/$p")
        Files.write(dir.resolve(p), content)
      }
    cache
  }

  def setEnv(key: String, value: String) = {
    val field = System.getenv().getClass.getDeclaredField("m")
    field.setAccessible(true)
    val map = field.get(System.getenv()).asInstanceOf[java.util.Map[java.lang.String, java.lang.String]]
    map.put(key, value)
  }
}
