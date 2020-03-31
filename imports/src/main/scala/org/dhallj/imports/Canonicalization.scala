package org.dhallj.imports

import cats.effect.Sync

/**
 * Abstract over java.nio.Path so we can assert that their
 * normalization has the same behaviour as the Dhall spec's
 * import path canonicalization
 *
 * TODO check behaviour of `canonicalize /..` as the Dhall
 * spec seems to differ from Java here
 */
object Canonicalization {

  def canonicalize[F[_]](imp: ImportContext)(implicit F: Sync[F]): F[ImportContext] = imp match {
    case Remote(uri) => F.delay(Remote(uri.normalize))
    case Local(path) => F.delay(Local(path.toAbsolutePath.normalize))
    case i           => F.pure(i)
  }

  def canonicalize[F[_]](parent: ImportContext, child: ImportContext)(implicit F: Sync[F]): F[ImportContext] =
    parent match {
      case Remote(uri) =>
        child match {
          //A transitive relative import is parsed as local but is resolved as a remote import
          // eg https://github.com/dhall-lang/dhall-lang/blob/master/Prelude/Integer/add has a local import but we still
          //need to resolve this as a remote import
          //Also note that if the path is absolute then this violates referential sanity but we handle that elsewhere
          case Local(path) =>
            if (path.isAbsolute) canonicalize(child) else canonicalize(Remote(uri.resolve(path.toString)))
          case _ => canonicalize(child)
        }
      case Local(path) =>
        child match {
          case Local(path2) => canonicalize(Local(path.getParent.resolve(path2)))
          case _            => canonicalize(child)
        }
      case _ => canonicalize(child)
    }

}
