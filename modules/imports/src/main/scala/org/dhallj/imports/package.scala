package org.dhallj

import cats.effect.Sync
import cats.implicits._
import org.dhallj.core.Expr
import org.dhallj.imports.ResolutionConfig.FromFileSystem
import org.http4s.client._

package object imports {

  implicit class ResolveImports(e: Expr) {
    def resolveImports[F[_] <: AnyRef](
      resolutionConfig: ResolutionConfig = ResolutionConfig(FromFileSystem)
    )(implicit Client: Client[F], F: Sync[F]): F[Expr] =
      ResolveImportsVisitor.mkVisitor(resolutionConfig) >>= (v => e.accept(v))
  }

}
