package org.dhallj.imports

import java.net.URI
import java.nio.file.Paths

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import munit.FunSuite
import org.dhallj.imports.ImportContext._

class ReferentialSanityCheckSuite extends FunSuite {

  private val someUri = new URI("http://example.com/foo.dhall")
  private val somePath = Paths.get("/foo.dhall")

  test("Remote imports local".fail) {
    ReferentialSanityCheck[IO](Remote(someUri, null), Local(somePath)).unsafeRunSync()
  }

  test("Remote imports env".fail) {
    ReferentialSanityCheck[IO](Remote(someUri, null), Env("foo")).unsafeRunSync()
  }

  test("Remote imports remote") {
    ReferentialSanityCheck[IO](Remote(someUri, null), Remote(someUri, null)).unsafeRunSync()
  }

  test("Remote imports missing") {
    ReferentialSanityCheck[IO](Remote(someUri, null), Missing).unsafeRunSync()
  }

  test("Remote imports classpath".fail) {
    ReferentialSanityCheck[IO](Remote(someUri, null), Classpath(somePath)).unsafeRunSync()
  }

  test("Local imports local") {
    ReferentialSanityCheck[IO](Local(somePath), Local(somePath)).unsafeRunSync()
  }

  test("Local imports env") {
    ReferentialSanityCheck[IO](Local(somePath), Env("foo")).unsafeRunSync()
  }

  test("Local imports remote") {
    ReferentialSanityCheck[IO](Local(somePath), Remote(someUri, null)).unsafeRunSync()
  }

  test("Local imports missing") {
    ReferentialSanityCheck[IO](Local(somePath), Missing).unsafeRunSync()
  }

  test("Env imports local") {
    ReferentialSanityCheck[IO](Env("foo"), Local(somePath)).unsafeRunSync()
  }

  test("Env imports env") {
    ReferentialSanityCheck[IO](Env("foo"), Env("foo")).unsafeRunSync()
  }

  test("Env imports remote") {
    ReferentialSanityCheck[IO](Env("foo"), Remote(someUri, null)).unsafeRunSync()
  }

  test("Env imports missing") {
    ReferentialSanityCheck[IO](Env("foo"), Missing).unsafeRunSync()
  }
}
