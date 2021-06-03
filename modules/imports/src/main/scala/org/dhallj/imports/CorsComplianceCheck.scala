package org.dhallj.imports

import java.net.URI

import cats.effect.Sync
import org.dhallj.core.DhallException.ResolutionFailure
import org.http4s.Headers
import org.typelevel.ci.CIString

object CorsComplianceCheck {

  def apply[F[_]](parent: ImportContext, child: ImportContext, headers: Headers)(implicit F: Sync[F]): F[Unit] =
    parent match {
      case ImportContext.Remote(uri, _) =>
        child match {
          case ImportContext.Remote(uri2, _) =>
            if (sameOrigin(uri, uri2))
              F.unit
            else
              headers
                .get(CIString("Access-Control-Allow-Origin"))
                .fold(
                  F.raiseError[Unit](
                    new ResolutionFailure(
                      s"CORS compliance failure - No Access-Control-Allow-Origin header for import $uri2 from $uri"
                    )
                  )
                ) { h =>
                  if (h.head.value.trim == "*" || sameOrigin(new URI(h.head.value), uri))
                    F.unit
                  else
                    F.raiseError(
                      new ResolutionFailure(
                        s"CORS compliance failure - ${h.head.value.trim} is invalid for import $uri2 from $uri"
                      )
                    )
                }
          case _ => F.unit
        }
      case _ => F.unit
    }

  private def sameOrigin(uri: URI, uri2: URI): Boolean =
    uri.getScheme == uri2.getScheme && uri.getAuthority == uri2.getAuthority && uri.getPort == uri2.getPort

}
