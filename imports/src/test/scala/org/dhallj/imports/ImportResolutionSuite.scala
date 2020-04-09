package org.dhallj.imports

import java.security.MessageDigest

import cats.effect.concurrent.Ref
import cats.effect.{ContextShift, IO, Resource}
import cats.implicits._
import munit.FunSuite
import org.dhallj.core.Expr
import org.dhallj.core.binary.Decode
import org.dhallj.imports.Caching.ImportsCache
import org.dhallj.imports.ResolutionConfig._
import org.dhallj.parser.DhallParser.parse
import org.http4s.client._
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext.global

class ImportResolutionSuite extends FunSuite {

  implicit val cs: ContextShift[IO] = IO.contextShift(global)

  implicit val client: Resource[IO, Client[IO]] = BlazeClientBuilder[IO](global).resource

  test("Local import") {
    val expr = parse("let x = /local/package.dhall in x")
    val expected = parse("let x = 1 in x").normalize

    assert(resolve(expr) == expected)
  }

  test("Local -> local relative import") {
    val expr = parse("let x = /local-local-relative/package.dhall in x")
    val expected = parse("let x = 1 in x").normalize

    assert(resolve(expr) == expected)
  }

  test("Local -> local absolute import") {
    val expr = parse("let x = /local-local-absolute/package.dhall in x")
    val expected = parse("let x = 1 in x").normalize

    assert(resolve(expr) == expected)
  }

  test("Local -> remote import") {
    val expr = parse("let any = /local-remote/package.dhall in any Natural Natural/even [2,3,5]")
    val expected = parse("True").normalize

    assert(resolve(expr) == expected)
  }

  test("Remote import") {
    val expr = parse(
      "let any = https://raw.githubusercontent.com/dhall-lang/dhall-lang/master/Prelude/List/any in any Natural Natural/even [2,3,5]"
    )
    val expected = parse("True").normalize

    assert(resolve(expr) == expected)
  }

  test("Multiple imports") {
    val expr = parse("let x = /multiple-imports/package.dhall in x")
    val expected = parse("let x = [1,2] in x").normalize

    assert(resolve(expr) == expected)
  }

  test("Import as text") {
    val expr = parse("let x = /text-import/package.dhall as Text in x")
    val expected = parse("\"let x = 1 in x\"").normalize

    assert(resolve(expr) == expected)
  }

  test("Import as local location") {
    val expr = parse("let x = /foo/bar.dhall as Location in x")
    val expected =
      parse("< Local : Text | Remote : Text | Environment : Text | Missing >.Local \"/foo/bar.dhall\"").normalize

    assert(resolve(expr) == expected)
  }

  test("Import as remote location") {
    val expr = parse("let x = http://example.com/foo.dhall as Location in x")
    val expected = parse(
      "< Local : Text | Remote : Text | Environment : Text | Missing >.Remote \"http://example.com/foo.dhall\""
    ).normalize

    assert(resolve(expr) == expected)
  }

  test("Import as env location") {
    val expr = parse("let x = env:foo as Location in x")
    val expected =
      parse("< Local : Text | Remote : Text | Environment : Text | Missing >.Environment \"foo\"").normalize

    assert(resolve(expr) == expected)
  }

  test("Cyclic imports".fail) {
    val expr = parse("let x = /cyclic-relative-paths/package.dhall in x")
    val expected = parse("True").normalize

    assert(resolve(expr) == expected)
  }

  test("Cyclic imports - all relative".fail) {
    val expr = parse("let x = /cyclic-relative-paths/package.dhall in x")
    val expected = parse("True").normalize

    assert(resolve(expr) == expected)
  }

  test("Alternate imports - first succeeds") {
    val expr = parse("let x = /alternate/package.dhall ? /alternate/other.dhall in x")
    val expected = parse("let x = 1 in x").normalize

    assert(resolve(expr) == expected)
  }

  test("Alternate imports - first fails") {

    val expr = parse("let x = /alternate/not_present.dhall ? /alternate/package.dhall in x")
    val expected = parse("let x = 1 in x").normalize

    assert(resolve(expr) == expected)
  }

  test("Valid hash") {

    val expr = parse(
      "let x = /hashed/package.dhall sha256:d60d8415e36e86dae7f42933d3b0c4fe3ca238f057fba206c7e9fbf5d784fe15 in x"
    )
    val expected = parse("let x = 1 in x").normalize

    assert(resolve(expr) == expected)
  }

  test("Invalid hash".fail) {

    val expr = parse(
      "let x = /hashed/package.dhall sha256:e60d8415e36e86dae7f42933d3b0c4fe3ca238f057fba206c7e9fbf5d784fe15 in x"
    )
    val expected = parse("let x = 1 in x").normalize

    assert(resolve(expr) == expected)
  }

  test("Read from cache, cached value present") {
    val cache = InMemoryCache()

    val expected = parse("let x = 2 in x")
    val encoded = expected.normalize.getEncodedBytes
    val hash = MessageDigest.getInstance("SHA-256").digest(encoded)

    val expr =
      parse("let x = /does-not-exist sha256:4caf97e8c445d4d4b5c5b992973e098ed4ae88a355915f5a59db640a589bc9cb in x")

    assert((cache.put(hash, encoded) >> resolveWithCustomCache(cache, expr)).unsafeRunSync == expected)
  }

  test("Read from cache, incorrect hash".fail) {
    val cache = InMemoryCache()

    val cached = parse("let x = 1 in x")
    val expected = parse("let x = 2 in x")
    val encoded = cached.normalize.getEncodedBytes
    val hash = MessageDigest.getInstance("SHA-256").digest(expected.normalize.getEncodedBytes) //Hash doesn't match what is stored

    val expr =
      parse("let x = /does-not-exist sha256:4caf97e8c445d4d4b5c5b992973e098ed4ae88a355915f5a59db640a589bc9cb in x")

    assert((cache.put(hash, encoded) >> resolveWithCustomCache(cache, expr)).unsafeRunSync == expected)
  }

  test("Write to cache") {
    val cache = InMemoryCache()

    val expected = parse("let x = 2 in x")
    val encoded = expected.normalize.getEncodedBytes
    val hash = MessageDigest.getInstance("SHA-256").digest(encoded)

    val expr = parse(
      "let x = /cache-write/package.dhall sha256:4caf97e8c445d4d4b5c5b992973e098ed4ae88a355915f5a59db640a589bc9cb in x"
    )

    val prog = resolveWithCustomCache(cache, expr) >> cache.get(hash)

    assert(prog.unsafeRunSync match {
      case None     => false
      case Some(bs) => Decode.decode(bs) == expected
    })
  }

  private def resolve(e: Expr): Expr =
    client.use { c =>
      implicit val http: Client[IO] = c

      e.resolveImports[IO](ResolutionConfig(FromResources)).map(_.normalize)
    }.unsafeRunSync

  private def resolveWithCustomCache(cache: ImportsCache[IO], e: Expr): IO[Expr] =
    client.use { c =>
      implicit val http: Client[IO] = c

      e.acceptVis(ResolveImportsVisitor[IO](ResolutionConfig(FromResources), cache, Nil))
    }

  private case class InMemoryCache() extends ImportsCache[IO] {

    private val store: Ref[IO, Map[List[Byte], Array[Byte]]] = Ref.unsafe(Map.empty)

    override def get(key: Array[Byte]): IO[Option[Array[Byte]]] = store.get.map(_.get(key.toList))

    override def put(key: Array[Byte], value: Array[Byte]): IO[Unit] = store.update(_ + (key.toList -> value))
  }

}
