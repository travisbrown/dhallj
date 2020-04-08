package org.dhallj.imports

import java.net.URI
import java.nio.file.Paths

import cats.effect.IO
import munit.FunSuite
import org.dhallj.imports.ResolveImportsVisitor._
import org.http4s.{Header, Headers}

class CORSComplianceCheckSuite extends FunSuite {

  val fooOrigin = new URI("http://foo.org/foo.dhall")
  val fooOrigin8080 = new URI("http://foo.org:8080/foo.dhall")
  val fooOrigin2 = new URI("http://foo.org/baz.dhall")
  val barOrigin = new URI("http://bar.org/bar.dhall")

  val localPath = Paths.get("/foo/bar.dhall")

  test("Remote - same origin") {
    CORSComplianceCheck[IO](Remote(fooOrigin), Remote(fooOrigin2), Headers.empty).unsafeRunSync()
  }

  test("Remote - different origin, allow *") {
    CORSComplianceCheck[IO](Remote(fooOrigin),
                            Remote(barOrigin),
                            Headers.of(Header("Access-Control-Allow-Origin", "*"))).unsafeRunSync()
  }

  test("Remote - different origin, allow parent authority") {
    CORSComplianceCheck[IO](Remote(fooOrigin),
                            Remote(barOrigin),
                            Headers.of(Header("Access-Control-Allow-Origin", "http://foo.org"))).unsafeRunSync()
  }

  test("Remote - different origin".fail) {
    CORSComplianceCheck[IO](Remote(fooOrigin), Remote(barOrigin), Headers.empty).unsafeRunSync()
  }

  test("Remote - different origin, cors parent different authority".fail) {
    CORSComplianceCheck[IO](Remote(fooOrigin),
                            Remote(barOrigin),
                            Headers.of(Header("Access-Control-Allow-Origin", "http://bar.org"))).unsafeRunSync()
  }

  test("Remote - different origin, cors parent different scheme".fail) {
    CORSComplianceCheck[IO](Remote(fooOrigin),
                            Remote(barOrigin),
                            Headers.of(Header("Access-Control-Allow-Origin", "https://foo.org"))).unsafeRunSync()
  }

  test("Remote - different origin, cors parent different port".fail) {
    CORSComplianceCheck[IO](Remote(fooOrigin),
                            Remote(barOrigin),
                            Headers.of(Header("Access-Control-Allow-Origin", "http://foo.org:8080"))).unsafeRunSync()
  }

  test("Remote - different origin, cors parent different port 2".fail) {
    CORSComplianceCheck[IO](Remote(fooOrigin8080),
                            Remote(barOrigin),
                            Headers.of(Header("Access-Control-Allow-Origin", "http://foo.org"))).unsafeRunSync()
  }

  test("Local") {
    CORSComplianceCheck[IO](Local(localPath), Remote(fooOrigin2), Headers.empty).unsafeRunSync()
  }

  test("Env") {
    CORSComplianceCheck[IO](Env("foo"), Remote(fooOrigin2), Headers.empty).unsafeRunSync()
  }
}
