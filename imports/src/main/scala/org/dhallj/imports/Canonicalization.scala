package org.dhallj.imports

import java.net.URI
import java.nio.file.{Path, Paths}

/**
 * Abstract over java.nio.Path so we can assert that their
 * normalization has the same behaviour as the Dhall spec's
 * import path canonicalization
 *
 * TODO check behaviour of `canonicalize /..` as the Dhall
 * spec seems to differ from Java here
 */
object Canonicalization {

  /**
   * Piggyback on Java's path normalization
   */
  def canonicalize(path: Path) = path.toAbsolutePath.normalize

  /**
   * Drop filename of parent and resolve child path (may be relative or absolute)
   */
  def canonicalize(parent: Path, child: Path) = parent.getParent.resolve(child).toAbsolutePath.normalize

  def canonicalize(uri: URI) = Paths.get(uri).toAbsolutePath.normalize

  def canonicalize(parent: URI, child: URI) = parent.resolve(child).normalize

  def canonicalize(parent: URI, child: Path) = parent.resolve(child.toString).normalize

}
