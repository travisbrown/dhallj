package org.dhallj.imports

import java.net.URI
import java.nio.file.Paths

import cats.effect.IO
import munit.FunSuite
import org.dhallj.imports.Canonicalization._
import org.dhallj.imports.ResolutionConfig._
import org.dhallj.imports.ResolveImportsVisitor._
import org.dhallj.parser.DhallParser.parse

/**
 * https://github.com/dhall-lang/dhall-lang/blob/master/standard/imports.md#canonicalization-of-directories
 */
class CanonicalizationSuite extends FunSuite {

  val fromFileSystemConfig = ResolutionConfig(FromFileSystem)

  val headers1 = parse("/headers1.dhall")
  val headers2 = parse("/headers2.dhall")

  test("Imports - Local, empty path") {
    assertEquals(canonicalize[IO](fromFileSystemConfig, Local(Paths.get("/"))).unsafeRunSync, Local(Paths.get("/")))
  }

  test("Paths - Trailing .") {
    assertEquals(canonicalize[IO](fromFileSystemConfig, Local(Paths.get("/foo/."))).unsafeRunSync,
                 Local(Paths.get("/foo")))
  }

  test("Paths - Trailing ..") {
    assertEquals(canonicalize[IO](fromFileSystemConfig, Local(Paths.get("/foo/bar/.."))).unsafeRunSync,
                 Local(Paths.get("/foo")))
  }

  //TODO determine whether spec is correct on this
  test("Paths - Root ..".fail) {
    assertEquals(canonicalize[IO](fromFileSystemConfig, Local(Paths.get("/.."))).unsafeRunSync, Local(Paths.get("/..")))
  }

  test("Chaining - local/local relative") {
    assertEquals(
      canonicalize[IO](fromFileSystemConfig, Local(Paths.get("/foo/bar.dhall")), Local(Paths.get("./baz.dhall"))).unsafeRunSync,
      Local(Paths.get("/foo/baz.dhall"))
    )
  }

  test("Chaining - local/local absolute") {
    assertEquals(
      canonicalize[IO](fromFileSystemConfig, Local(Paths.get("/foo/bar.dhall")), Local(Paths.get("/bar/baz.dhall"))).unsafeRunSync,
      Local(Paths.get("/bar/baz.dhall"))
    )
  }

  test("Chaining - local/remote") {
    assertEquals(
      canonicalize[IO](fromFileSystemConfig,
                       Local(Paths.get("/foo/bar.dhall")),
                       Remote(new URI("http://foo.org/bar.dhall"), headers1)).unsafeRunSync,
      Remote(new URI("http://foo.org/bar.dhall"), headers1)
    )
  }

  test("Chaining - remote/remote absolute") {
    assertEquals(
      canonicalize[IO](fromFileSystemConfig,
                       Remote(new URI("http://foo.org/bar.dhall"), headers1),
                       Remote(new URI("https://bar.com/bar/baz.dhall"), headers2)).unsafeRunSync,
      Remote((new URI("https://bar.com/bar/baz.dhall")), headers2)
    )
  }

  test("Chaining - remote/local relative") {
    assertEquals(
      canonicalize[IO](fromFileSystemConfig,
                       Remote(new URI("http://foo.org/bar.dhall"), headers1),
                       Local(Paths.get("./baz.dhall"))).unsafeRunSync,
      Remote(new URI("http://foo.org/baz.dhall"), headers1)
    )
  }

  //This is actually prohibited by the sanity check but we don't worry about it here
  test("Chaining - remote/local absolute") {
    assertEquals(
      canonicalize[IO](fromFileSystemConfig,
                       Remote(new URI("http://foo.org/bar.dhall"), headers1),
                       Local(Paths.get("/baz.dhall"))).unsafeRunSync,
      Local(Paths.get("/baz.dhall"))
    )
  }

}
