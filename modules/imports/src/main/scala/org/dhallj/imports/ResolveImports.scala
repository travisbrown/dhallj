package org.dhallj.imports

import java.nio.file.{Path, Paths}

import cats.effect.Sync
import org.dhallj.core.Expr
import org.http4s.client.Client

object Resolver {
  def resolve[F[_] <: AnyRef](expr: Expr)(implicit Client: Client[F], F: Sync[F]): F[Expr] =
    F.flatMap(ResolveImportsVisitor[F](cwd))(expr.accept(_))

  def resolveRelativeTo[F[_] <: AnyRef](relativeTo: Path)(expr: Expr)(implicit Client: Client[F], F: Sync[F]): F[Expr] =
    F.flatMap(ResolveImportsVisitor[F](relativeTo))(expr.accept(_))

  def resolve[F[_] <: AnyRef](
    semanticCache: ImportCache[F],
    semiSemanticCache: ImportCache[F]
  )(expr: Expr)(implicit Client: Client[F], F: Sync[F]): F[Expr] =
    expr.accept(ResolveImportsVisitor[F](semanticCache, semiSemanticCache, cwd))

  def resolve[F[_] <: AnyRef](
    semanticCache: ImportCache[F]
  )(expr: Expr)(implicit Client: Client[F], F: Sync[F]): F[Expr] =
    expr.accept(ResolveImportsVisitor[F](semanticCache, new ImportCache.NoopImportCache[F], cwd))

  private def cwd: Path = Paths.get(".")

  final class Ops(val expr: Expr) extends AnyVal {
    def resolveImports[F[_] <: AnyRef](implicit Client: Client[F], F: Sync[F]): F[Expr] =
      resolve[F](expr)

    def resolveImportsRelativeTo[F[_] <: AnyRef](relativeTo: Path)(implicit Client: Client[F], F: Sync[F]): F[Expr] =
      resolveRelativeTo[F](relativeTo)(expr)
  }
}
