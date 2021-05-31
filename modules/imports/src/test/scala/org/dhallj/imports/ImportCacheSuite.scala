package org.dhallj.imports

import java.nio.file.{Files, Path}

import cats.effect.IO
import cats.implicits._
import munit.FunSuite

class ImportCacheSuite extends FunSuite {

  val rootDir = FunFixture[(ImportCache[IO], Path)](
    setup = { test =>
      val rootDir = Files.createTempDirectory(test.name).resolve("dhall")
      ImportCache[IO](rootDir).unsafeRunSync().get -> rootDir
    },
    teardown = { case (_, rootDir) => }
  )

  val key = "0f86d".getBytes

  val bytes: Array[Byte] = "test".getBytes

  rootDir.test("Get-if-absent") { case (cache, _) =>
    val prog = cache.get(key)

    assertEquals(prog.unsafeRunSync(), None)
  }

  rootDir.test("Get-if-present") { case (cache, _) =>
    val prog = cache.put(key, bytes) >> cache.get(key)

    assert(prog.unsafeRunSync().exists(_.sameElements(bytes)))
  }

}
