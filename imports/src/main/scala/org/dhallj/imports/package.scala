package org.dhallj

import cats.effect.Sync
import org.dhallj.core.Expr
import org.dhallj.imports.ResolveImportsVisitor._
import org.http4s.client._
import cats.implicits._

package object imports {

  implicit class ResolveImports(e: Expr) {
    def resolveImports[F[_]](
      resolutionConfig: ResolutionConfig = ResolutionConfig(FromFileSystem)
    )(implicit Client: Client[F], F: Sync[F]): F[Expr] = ResolveImportsVisitor.mkVisitor(resolutionConfig) >>= (v => e.acceptVis(v))
  }

}
