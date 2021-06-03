package org.dhallj.imports

import java.net.URI
import java.nio.file.Paths

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import munit.FunSuite
import org.dhallj.imports.Canonicalization.canonicalize
import org.dhallj.imports.ImportContext._
import org.dhallj.parser.DhallParser.parse

/**
 * https://github.com/dhall-lang/dhall-lang/blob/master/standard/imports.md#canonicalization-of-directories
 */
class CanonicalizationSuite extends FunSuite {

  val headers1 = parse("/headers1.dhall")
  val headers2 = parse("/headers2.dhall")

  test("Imports - Local, empty path") {
    assertEquals(canonicalize[IO](Local(Paths.get("/foo.dhall"))).unsafeRunSync(), Local(Paths.get("/foo.dhall")))
  }

  test("Imports - quoted") {
    assertEquals(canonicalize[IO](Local(Paths.get("/\"foo\"/\"bar.dhall\""))).unsafeRunSync(),
                 Local(Paths.get("/foo/bar.dhall"))
    )
  }

  test("Paths - Trailing .") {
    assertEquals(canonicalize[IO](Local(Paths.get("/foo/./bar.dhall"))).unsafeRunSync(),
                 Local(Paths.get("/foo/bar.dhall"))
    )
  }

  test("Paths - Trailing ..") {
    assertEquals(canonicalize[IO](Local(Paths.get("/foo/bar/../baz.dhall"))).unsafeRunSync(),
                 Local(Paths.get("/foo/baz.dhall"))
    )
  }

  //TODO determine whether spec is correct on this
  test("Paths - Root ..") {
    assertEquals(canonicalize[IO](Local(Paths.get("/.."))).unsafeRunSync(), Local(Paths.get("/..")))
  }

  test("Chaining - local / , local /") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("/foo/bar.dhall")), Local(Paths.get("/bar/baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("/bar/baz.dhall"))
    )
  }

  test("Chaining - local / , local ~") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("/foo/bar.dhall")), Local(Paths.get("~/bar/baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("~/bar/baz.dhall"))
    )
  }

  test("Chaining - local / , local .") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("/foo/bar.dhall")), Local(Paths.get("./baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("/foo/baz.dhall"))
    )
  }

  test("Chaining - local / , local ..") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("./foo/x/bar.dhall")), Local(Paths.get("../bar/baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("./foo/bar/baz.dhall"))
    )
  }

  test("Chaining - local ~ , local /") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("~/foo/bar.dhall")), Local(Paths.get("/baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("/baz.dhall"))
    )
  }

  test("Chaining - local ~ , local ~") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("~/foo/bar.dhall")), Local(Paths.get("~/baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("~/baz.dhall"))
    )
  }

  test("Chaining - local ~ , local .") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("~/foo/bar.dhall")), Local(Paths.get("./baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("~/foo/baz.dhall"))
    )
  }

  test("Chaining - local ~ , local ..") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("~/foo/bar.dhall")), Local(Paths.get("../baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("~/baz.dhall"))
    )
  }

  test("Chaining - local . , local /") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("./foo/bar.dhall")), Local(Paths.get("/baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("/baz.dhall"))
    )
  }

  test("Chaining - local . , local ~") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("./foo/bar.dhall")), Local(Paths.get("~/baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("~/baz.dhall"))
    )
  }

  test("Chaining - local . , local .") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("./foo/bar.dhall")), Local(Paths.get("./baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("./foo/baz.dhall"))
    )
  }

  test("Chaining - local . , local ..") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("./foo/bar.dhall")), Local(Paths.get("../baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("./baz.dhall"))
    )
  }

  test("Chaining - local .. , local /") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("../foo/bar.dhall")), Local(Paths.get("/baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("/baz.dhall"))
    )
  }

  test("Chaining - local .. , local ~") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("../foo/bar.dhall")), Local(Paths.get("~/baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("~/baz.dhall"))
    )
  }

  test("Chaining - local .. , local .") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("../foo/bar.dhall")), Local(Paths.get("./baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("../foo/baz.dhall"))
    )
  }

  test("Chaining - local .. , local ..") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("../foo/bar.dhall")), Local(Paths.get("../baz.dhall"))).unsafeRunSync(),
      Local(Paths.get("../baz.dhall"))
    )
  }

  test("Chaining - local / remote") {
    assertEquals(
      canonicalize[IO](Local(Paths.get("/foo/bar.dhall")), Remote(new URI("http://foo.org/bar.dhall"), headers1))
        .unsafeRunSync(),
      Remote(new URI("http://foo.org/bar.dhall"), headers1)
    )
  }

  test("Chaining - remote / remote absolute") {
    assertEquals(
      canonicalize[IO](Remote(new URI("http://foo.org/bar.dhall"), headers1),
                       Remote(new URI("https://bar.com/bar/baz.dhall"), headers2)
      ).unsafeRunSync(),
      Remote((new URI("https://bar.com/bar/baz.dhall")), headers2)
    )
  }

  test("Chaining - remote / local relative") {
    assertEquals(
      canonicalize[IO](Remote(new URI("http://foo.org/bar.dhall"), headers1), Local(Paths.get("./baz.dhall")))
        .unsafeRunSync(),
      Remote(new URI("http://foo.org/baz.dhall"), headers1)
    )
  }

  //This is actually prohibited by the sanity check but we don't worry about it here
  test("Chaining - remote / local absolute") {
    assertEquals(
      canonicalize[IO](Remote(new URI("http://foo.org/bar.dhall"), headers1), Local(Paths.get("/baz.dhall")))
        .unsafeRunSync(),
      Local(Paths.get("/baz.dhall"))
    )
  }

}
