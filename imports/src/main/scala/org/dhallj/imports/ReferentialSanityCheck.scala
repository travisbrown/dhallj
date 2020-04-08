package org.dhallj.imports

import cats.effect.Sync
import org.dhallj.imports.ResolveImportsVisitor._

object ReferentialSanityCheck {

  def apply[F[_]](parent: ImportContext, child: ImportContext)(implicit F: Sync[F]): F[Unit] = parent match {
    case Remote(uri) =>
      child match {
        case Remote(_) => F.unit
        case Missing   => F.unit
        case Local(path) =>
          F.raiseError(
            new RuntimeException(
              "Referential sanity violation - remote import $uri cannot reference local import $path"
            )
          )
        case Env(v) =>
          F.raiseError(
            new RuntimeException("Referential sanity violation - remote import $uri cannot reference env import $v")
          )
      }
    case Missing => F.raiseError(new RuntimeException(s"Missing import cannot reference import $child"))
    case _       => F.unit
  }

}
