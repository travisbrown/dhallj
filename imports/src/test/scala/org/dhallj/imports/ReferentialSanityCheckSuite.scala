package org.dhallj.imports

import java.net.URI
import java.nio.file.Paths

import cats.effect.IO
import munit.FunSuite
import org.dhallj.imports.ResolveImportsVisitor._

class ReferentialSanityCheckSuite extends FunSuite {

  private val someUri = new URI("http://example.com/foo.dhall")
  private val somePath = Paths.get("/foo.dhall")

  test("Remote imports local".fail) {
    ReferentialSanityCheck[IO](Remote(someUri), Local(somePath)).unsafeRunSync
  }

  test("Remote imports env".fail) {
    ReferentialSanityCheck[IO](Remote(someUri), Env("foo")).unsafeRunSync
  }

  test("Remote imports remote") {
    ReferentialSanityCheck[IO](Remote(someUri), Remote(someUri)).unsafeRunSync
  }

  test("Remote imports missing") {
    ReferentialSanityCheck[IO](Remote(someUri), Missing).unsafeRunSync
  }

  test("Local imports local") {
    ReferentialSanityCheck[IO](Local(somePath), Local(somePath)).unsafeRunSync
  }

  test("Local imports env") {
    ReferentialSanityCheck[IO](Local(somePath), Env("foo")).unsafeRunSync
  }

  test("Local imports remote") {
    ReferentialSanityCheck[IO](Local(somePath), Remote(someUri)).unsafeRunSync
  }

  test("Local imports missing") {
    ReferentialSanityCheck[IO](Local(somePath), Missing).unsafeRunSync
  }

  test("Env imports local") {
    ReferentialSanityCheck[IO](Env("foo"), Local(somePath)).unsafeRunSync
  }

  test("Env imports env") {
    ReferentialSanityCheck[IO](Env("foo"), Env("foo")).unsafeRunSync
  }

  test("Env imports remote") {
    ReferentialSanityCheck[IO](Env("foo"), Remote(someUri)).unsafeRunSync
  }

  test("Env imports missing") {
    ReferentialSanityCheck[IO](Env("foo"), Missing).unsafeRunSync
  }
}
