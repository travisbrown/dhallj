package org.dhallj.imports

import java.nio.file.{Files, Path, Paths}

import cats.effect.Sync
import cats.implicits._

private[imports] object Caching {

  trait ImportsCache[F[_]] {
    def get(key: String): F[Option[Array[Byte]]]

    def put(key: String, value: Array[Byte]): F[Unit]
  }

  case class ImportsCacheImpl[F[_]] private[Caching] (val rootDir: Path)(implicit F: Sync[F]) extends ImportsCache[F] {
    override def get(key: String): F[Option[Array[Byte]]] = {
      val p = path(key)
      if (Files.exists(p)) {
        F.delay(Some(Files.readAllBytes(p)))
      } else {
        F.pure(None)
      }
    }

    override def put(key: String, value: Array[Byte]): F[Unit] =
      F.delay(Files.write(path(key), value))

    private def path(key: String): Path = rootDir.resolve("1220${key}")
  }

  def mkImportsCache[F[_]](rootDir: Path)(implicit F: Sync[F]): F[Option[ImportsCache[F]]] =
    for {
      _ <- if (!Files.exists(rootDir)) F.delay(Files.createDirectory(rootDir)) else F.unit
      perms <- F.delay(Files.isReadable(rootDir) && Files.isWritable(rootDir))
    } yield (if (perms) Some(new ImportsCacheImpl[F](rootDir)) else None)

  def mkImportsCache[F[_]](implicit F: Sync[F]): F[Option[ImportsCache[F]]] = {
    def makeCache(env: String, relativePath: String): F[Option[ImportsCache[F]]] =
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
      if (isWindows)
        makeCache("LOCALAPPDATA", "")
      else makeCache("HOME", ".cache")

    def isWindows = System.getProperty("os.name").toLowerCase.contains("Windows")

    for {
      xdgCache <- makeCache("XDG_HOME", "")
      cache <- xdgCache.fold(backupCache)(c => F.pure(Some(c)))
    } yield cache

  }

}
