package org.dhallj.imports

import cats.effect.Sync
import org.dhallj.core.DhallException.ResolutionFailure
import org.dhallj.imports.ResolveImportsVisitor._

object ReferentialSanityCheck {

  def apply[F[_]](parent: ImportContext, child: ImportContext)(implicit F: Sync[F]): F[Unit] = parent match {
    case Remote(uri, _) =>
      child match {
        case Remote(_, _) => F.unit
        case Missing      => F.unit
        case Local(path) =>
          F.raiseError(
            new ResolutionFailure(
              "Referential sanity violation - remote import $uri cannot reference local import $path"
            )
          )
        case Classpath(path) =>
          F.raiseError(
            new ResolutionFailure(
              "Referential sanity violation - remote import $uri cannot reference classpath import $path"
            )
          )
        case Env(v) =>
          F.raiseError(
            new ResolutionFailure("Referential sanity violation - remote import $uri cannot reference env import $v")
          )
      }
    case Missing => F.raiseError(new ResolutionFailure(s"Missing import cannot reference import $child"))
    case _       => F.unit
  }

}
