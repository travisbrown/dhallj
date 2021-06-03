package org.dhallj.imports

import java.nio.file.{Path, Paths}

import cats.{ApplicativeError, MonadError}
import cats.implicits._
import cats.effect.Sync
import org.dhallj.core.DhallException.ResolutionFailure
import org.dhallj.imports.ImportContext._

import scala.collection.JavaConverters._

object Canonicalization {

  def canonicalize[F[_]](imp: ImportContext)(implicit F: ApplicativeError[F, Throwable]): F[ImportContext] = imp match {
    case Remote(uri, headers) => F.pure(Remote(uri.normalize, headers))
    case Local(path)          => LocalFile[F](path).map(_.canonicalize.toPath).map(Local)
    case Classpath(path)      => LocalFile[F](path).map(_.canonicalize.toPath).map(Classpath)
    case i                    => F.pure(i)
  }

  def canonicalize[F[_]](parent: ImportContext, child: ImportContext)(implicit
    F: MonadError[F, Throwable]
  ): F[ImportContext] =
    parent match {
      case Remote(uri, headers) =>
        child match {
          //A transitive relative import is parsed as local but is resolved as a remote import
          // eg https://github.com/dhall-lang/dhall-lang/blob/master/Prelude/Integer/add has a local import but we still
          //need to resolve this as a remote import
          //Also note that if the path is absolute then this violates referential sanity but we handle that elsewhere
          case Local(path) =>
            if (path.isAbsolute) canonicalize[F](child)
            else canonicalize[F](Remote(uri.resolve(path.toString), headers))
          case _ => canonicalize[F](child)
        }
      case Local(path) =>
        child match {
          case Local(path2) =>
            for {
              parent <- LocalFile[F](path)
              c <- LocalFile[F](path2)
            } yield Local(parent.chain(c).canonicalize.toPath)
          case _ => canonicalize[F](child)
        }
      //TODO - determine semantics of classpath imports
      case Classpath(path) =>
        child match {
          //Also note that if the path is absolute then this violates referential sanity but we handle that elsewhere
          case Local(path2) =>
            for {
              parent <- LocalFile[F](path)
              c <- LocalFile[F](path2)
            } yield Classpath(parent.chain(c).canonicalize.toPath)
          case _ => canonicalize[F](child)
        }
      case _ => canonicalize[F](child)
    }

  private case class LocalFile(dirs: LocalDirs, filename: String) {
    def toPath: Path = {
      def toPath(l: List[String]) = "/" + l.intercalate("/")

      val s: String = dirs.ds match {
        case Nil => ""
        case l @ (h :: t) =>
          h match {
            case h if h == "." || h == ".." || h == "~" => s"$h${toPath(t)}"
            case _                                      => toPath(l)
          }
      }
      Paths.get(s"$s/$filename")
    }

    def chain(other: LocalFile): LocalFile = LocalFile.chain(this, other)

    def canonicalize: LocalFile = LocalFile.canonicalize(this)
  }

  private object LocalFile {
    def apply[F[_]](path: Path)(implicit F: ApplicativeError[F, Throwable]): F[LocalFile] =
      path.iterator().asScala.toList.map(_.toString) match {
        case Nil => F.raiseError(new ResolutionFailure("This shouldn't happen - / can't import a dhall expression"))
        case l   => F.pure(LocalFile(LocalDirs(l.take(l.length - 1)), l.last))
      }

    def canonicalize(f: LocalFile): LocalFile =
      LocalFile(f.dirs.canonicalize, f.filename.stripPrefix("\"").stripSuffix("\""))

    def chain(lhs: LocalFile, rhs: LocalFile): LocalFile = LocalFile(LocalDirs.chain(lhs.dirs, rhs.dirs), rhs.filename)
  }

  private case class LocalDirs(ds: List[String]) {
    def isRelative: Boolean = ds.nonEmpty && (ds.head == "." || ds.head == "..")

    def canonicalize: LocalDirs = LocalDirs.canonicalize(this)

    def chain(other: LocalDirs): LocalDirs = LocalDirs.chain(this, other)
  }

  private object LocalDirs {
    def chain(lhs: LocalDirs, rhs: LocalDirs): LocalDirs = if (rhs.isRelative) LocalDirs(lhs.ds ++ rhs.ds) else rhs

    def canonicalize(d: LocalDirs): LocalDirs = d.ds match {
      case Nil => d
      case l   => LocalDirs(canonicalize(l.map(_.stripPrefix("\"").stripSuffix("\""))))
    }

    def canonicalize(l: List[String]): List[String] =
      l.tail
        .foldLeft(List(l.head)) { (acc, next) =>
          next match {
            case "."  => acc
            case ".." => acc.tail
            case o    => o :: acc
          }
        }
        .reverse

  }

}
