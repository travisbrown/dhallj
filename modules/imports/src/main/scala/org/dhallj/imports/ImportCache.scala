package org.dhallj.imports

import java.nio.file.{Files, Path, Paths}

import cats.Applicative
import cats.effect.Async
import cats.implicits._

trait ImportCache[F[_]] {
  def get(key: Array[Byte]): F[Option[Array[Byte]]]

  def put(key: Array[Byte], value: Array[Byte]): F[Unit]
}

object ImportCache {

  /*
   * Improves the ergonomics when resolving imports if we don't have to check
   * if the cache exists. So if we fail to construct an imports cache,
   * we warn and return this instead.
   */
  class NoopImportCache[F[_]](implicit F: Applicative[F]) extends ImportCache[F] {
    override def get(key: Array[Byte]): F[Option[Array[Byte]]] = F.pure(None)

    override def put(key: Array[Byte], value: Array[Byte]): F[Unit] = F.unit
  }

  private class Impl[F[_]](rootDir: Path)(implicit F: Async[F]) extends ImportCache[F] {
    override def get(key: Array[Byte]): F[Option[Array[Byte]]] = {
      val p = path(key)
      if (Files.exists(p)) {
        F.delay(Some(Files.readAllBytes(p)))
      } else {
        F.pure(None)
      }
    }

    override def put(key: Array[Byte], value: Array[Byte]): F[Unit] =
      F.delay(Files.write(path(key), value))

    private def path(key: Array[Byte]): Path = rootDir.resolve(s"1220${toHex(key)}")

    private def toHex(bs: Array[Byte]): String = {
      val sb = new StringBuilder
      for (b <- bs) {
        sb.append(String.format("%02x", Byte.box(b)))
      }
      sb.toString
    }
  }

  def apply[F[_] <: AnyRef](rootDir: Path)(implicit F: Async[F]): F[Option[ImportCache[F]]] =
    for {
      _ <- if (!Files.exists(rootDir)) F.delay(Files.createDirectories(rootDir)).void else F.unit
      perms <- F.delay(Files.isReadable(rootDir) && Files.isWritable(rootDir))
    } yield (if (perms) Some(new Impl[F](rootDir)) else None)

  def apply[F[_] <: AnyRef](cacheName: String)(implicit F: Async[F]): F[ImportCache[F]] = {
    def makeCacheFromEnvVar(env: String, relativePath: String): F[Option[ImportCache[F]]] =
      for {
        envValO <- F.delay(sys.env.get(env))
        cache <- envValO.fold(F.pure(Option.empty[ImportCache[F]]))(envVal =>
          for {
            rootDir <- F.pure(Paths.get(envVal, relativePath, cacheName))
            c <- apply(rootDir)
          } yield c
        )
      } yield cache

    def backupCache =
      for {
        cacheO <-
          if (isWindows)
            makeCacheFromEnvVar("LOCALAPPDATA", "")
          else makeCacheFromEnvVar("HOME", ".cache")
        cache <- cacheO.fold[F[ImportCache[F]]](F.as(warnCacheNotCreated, new NoopImportCache[F]))(F.pure)
      } yield cache

    def isWindows = System.getProperty("os.name").toLowerCase.contains("windows")

    def warnCacheNotCreated: F[Unit] =
      F.delay(
        println(
          "WARNING: failed to create cache at either $XDG_CACHE_HOME}/dhall or $HOME/.cache/dhall. Are these locations writable?"
        )
      )

    for {
      xdgCache <- makeCacheFromEnvVar("XDG_CACHE_HOME", "")
      cache <- xdgCache.fold(backupCache)(F.pure)
    } yield cache

  }

}
