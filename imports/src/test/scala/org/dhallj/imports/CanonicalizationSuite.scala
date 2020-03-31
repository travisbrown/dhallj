package org.dhallj.imports

import java.net.URI

import munit.FunSuite
import org.dhallj.imports.Canonicalization._
import java.nio.file.Paths

/**
 * https://github.com/dhall-lang/dhall-lang/blob/master/standard/imports.md#canonicalization-of-directories
 */
class CanonicalizationSuite extends FunSuite {

  test("Imports - Empty path") {
    assertEquals(
      canonicalize(Paths.get("/")),
      Paths.get("/"))
  }

  test("Paths - Trailing .") {
    assertEquals(
      canonicalize(Paths.get("/foo/.")),
      Paths.get("/foo"))
  }

  test("Paths - Trailing ..") {
    assertEquals(
      canonicalize(Paths.get("/foo/bar/..")),
      Paths.get("/foo"))
  }

  //TODO determine whether spec is correct on this
  test("Paths - Root ..".fail) {
    assertEquals(
      canonicalize( Paths.get("/..")),
      Paths.get("/.."))
  }

  test("Chaining - local/local relative") {
    assertEquals(
      canonicalize(
        Paths.get("/foo/bar.dhall"),
        Paths.get("./baz.dhall")),
      Paths.get("/foo/baz.dhall"))
  }

  test("Chaining - local/local absolute") {
    assertEquals(
      canonicalize(
        Paths.get("/foo/bar.dhall"),
        Paths.get("/bar/baz.dhall")),
      Paths.get("/bar/baz.dhall"))
  }

  test("Chaining - remote/remote absolute") {
    assertEquals(
      canonicalize(
        new URI("http://foo.org/bar.dhall"),
        new URI("https://bar.com/bar/baz.dhall")),
      (new URI("https://bar.com/bar/baz.dhall")))
  }

  test("Chaining - remote/remote relative") {
    assertEquals(
      canonicalize(
        new URI("http://foo.org/bar.dhall"),
        Paths.get("./baz.dhall")),
      new URI("http://foo.org/baz.dhall"))
  }

}
