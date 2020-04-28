package org.dhallj.imports

import cats.effect.Sync
import org.dhallj.core.DhallException.ResolutionFailure

object ReferentialSanityCheck {

  def apply[F[_]](parent: ImportContext, child: ImportContext)(implicit F: Sync[F]): F[Unit] = parent match {
    case ImportContext.Remote(uri, _) =>
      child match {
        case ImportContext.Remote(_, _) => F.unit
        case ImportContext.Missing      => F.unit
        case ImportContext.Local(path) =>
          F.raiseError(
            new ResolutionFailure(
              s"Referential sanity violation - remote import $uri cannot reference local import $path"
            )
          )
        case ImportContext.Classpath(path) =>
          F.raiseError(
            new ResolutionFailure(
              s"Referential sanity violation - remote import $uri cannot reference classpath import $path"
            )
          )
        case ImportContext.Env(v) =>
          F.raiseError(
            new ResolutionFailure(s"Referential sanity violation - remote import $uri cannot reference env import $v")
          )
      }
    case ImportContext.Missing => F.raiseError(new ResolutionFailure(s"Missing import cannot reference import $child"))
    case _                     => F.unit
  }

}
