package org.dhallj.imports

import java.nio.file.{Files, Path, Paths}

import cats.effect.Sync
import cats.implicits._

private[dhallj] object Caching {

  trait ImportsCache[F[_]] {
    def get(key: Array[Byte]): F[Option[Array[Byte]]]

    def put(key: Array[Byte], value: Array[Byte]): F[Unit]
  }

  /*
   * Improves the ergonomics when resolving imports if we don't have to check
   * if the cache exists. So if we fail to construct an imports cache,
   * we warn and return this instead.
   */
  case class NoopImportsCache[F[_]]()(implicit F: Sync[F]) extends ImportsCache[F] {
    override def get(key: Array[Byte]): F[Option[Array[Byte]]] = F.pure(None)

    override def put(key: Array[Byte], value: Array[Byte]): F[Unit] = F.unit
  }

  case class ImportsCacheImpl[F[_]] private[Caching] (rootDir: Path)(implicit F: Sync[F]) extends ImportsCache[F] {
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

  def mkImportsCache[F[_] <: AnyRef](rootDir: Path)(implicit F: Sync[F]): F[Option[ImportsCache[F]]] =
    for {
      _ <- if (!Files.exists(rootDir)) F.delay(Files.createDirectories(rootDir)) else F.unit
      perms <- F.delay(Files.isReadable(rootDir) && Files.isWritable(rootDir))
    } yield (if (perms) Some(new ImportsCacheImpl[F](rootDir)) else None)

  def mkImportsCache[F[_] <: AnyRef](implicit F: Sync[F]): F[ImportsCache[F]] = {
    def makeCacheFromEnvVar(env: String, relativePath: String): F[Option[ImportsCache[F]]] =
      for {
        envValO <- F.delay(sys.env.get(env))
        cache <- envValO.fold(F.pure(Option.empty[ImportsCache[F]]))(envVal =>
          for {
            rootDir <- F.pure(Paths.get(envVal, relativePath, "dhall"))
            c <- mkImportsCache(rootDir)
          } yield c
        )
      } yield cache

    def backupCache =
      for {
        cacheO <- if (isWindows)
          makeCacheFromEnvVar("LOCALAPPDATA", "")
        else makeCacheFromEnvVar("HOME", ".cache")
        cache <- cacheO.fold(warnCacheNotCreated >> F.pure[ImportsCache[F]](NoopImportsCache[F]))(F.pure)
      } yield cache

    def isWindows = System.getProperty("os.name").toLowerCase.contains("Windows")

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
