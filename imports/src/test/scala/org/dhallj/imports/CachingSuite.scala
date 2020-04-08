package org.dhallj.imports

import java.nio.file.{Files, Path, Paths}

import cats.effect.IO
import cats.implicits._
import munit.FunSuite
import org.dhallj.imports.Caching.{ImportsCache, ImportsCacheImpl}
import scala.reflect.io.Directory

class CachingSuite extends FunSuite {

  val rootDir = new FunFixture[(ImportsCache[IO], Path)](
    setup = { test =>
      val rootDir = Files.createTempDirectory(test.name).resolve("dhall")
      Caching.mkImportsCache[IO](rootDir).unsafeRunSync.get -> rootDir
    },
    teardown = { case (_, rootDir) =>
      new Directory(rootDir.toFile).deleteRecursively()
    }
  )

  val key = "0f86d".getBytes

  val bytes: Array[Byte] = "test".getBytes

  rootDir.test("Get-if-absent") { case (cache, _) =>
    val prog = cache.get(key)

    assertEquals(prog.unsafeRunSync, None)
  }

  rootDir.test("Get-if-present") { case (cache, _) =>
    val prog = cache.put(key, bytes) >> cache.get(key)

    assertEquals(prog.unsafeRunSync, Some(bytes))
  }

}
