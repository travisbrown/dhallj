package org.dhallj.imports

import java.net.URI
import java.nio.file.Paths

import cats.effect.IO
import munit.FunSuite
import org.dhallj.imports.ImportContext._
import org.http4s.{Header, Headers}

class CorsComplianceCheckSuite extends FunSuite {

  val fooOrigin = new URI("http://foo.org/foo.dhall")
  val fooOrigin8080 = new URI("http://foo.org:8080/foo.dhall")
  val fooOrigin2 = new URI("http://foo.org/baz.dhall")
  val barOrigin = new URI("http://bar.org/bar.dhall")

  val localPath = Paths.get("/foo/bar.dhall")

  test("Remote - same origin") {
    CorsComplianceCheck[IO](Remote(fooOrigin, null), Remote(fooOrigin2, null), Headers.empty).unsafeRunSync()
  }

  test("Remote - different origin, allow *") {
    CorsComplianceCheck[IO](Remote(fooOrigin, null),
                            Remote(barOrigin, null),
                            Headers.of(Header("Access-Control-Allow-Origin", "*"))).unsafeRunSync()
  }

  test("Remote - different origin, allow parent authority") {
    CorsComplianceCheck[IO](Remote(fooOrigin, null),
                            Remote(barOrigin, null),
                            Headers.of(Header("Access-Control-Allow-Origin", "http://foo.org"))).unsafeRunSync()
  }

  test("Remote - different origin".fail) {
    CorsComplianceCheck[IO](Remote(fooOrigin, null), Remote(barOrigin, null), Headers.empty).unsafeRunSync()
  }

  test("Remote - different origin, cors parent different authority".fail) {
    CorsComplianceCheck[IO](Remote(fooOrigin, null),
                            Remote(barOrigin, null),
                            Headers.of(Header("Access-Control-Allow-Origin", "http://bar.org"))).unsafeRunSync()
  }

  test("Remote - different origin, cors parent different scheme".fail) {
    CorsComplianceCheck[IO](Remote(fooOrigin, null),
                            Remote(barOrigin, null),
                            Headers.of(Header("Access-Control-Allow-Origin", "https://foo.org"))).unsafeRunSync()
  }

  test("Remote - different origin, cors parent different port".fail) {
    CorsComplianceCheck[IO](Remote(fooOrigin, null),
                            Remote(barOrigin, null),
                            Headers.of(Header("Access-Control-Allow-Origin", "http://foo.org:8080"))).unsafeRunSync()
  }

  test("Remote - different origin, cors parent different port 2".fail) {
    CorsComplianceCheck[IO](Remote(fooOrigin8080, null),
                            Remote(barOrigin, null),
                            Headers.of(Header("Access-Control-Allow-Origin", "http://foo.org"))).unsafeRunSync()
  }

  test("Local") {
    CorsComplianceCheck[IO](Local(localPath), Remote(fooOrigin2, null), Headers.empty).unsafeRunSync()
  }

  test("Env") {
    CorsComplianceCheck[IO](Env("foo"), Remote(fooOrigin2, null), Headers.empty).unsafeRunSync()
  }
}
