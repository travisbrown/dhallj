package org.dhallj.imports.mini;

import org.dhallj.core.Expr;
import java.nio.file.Path;

public final class Resolver {
  public static final Expr resolve(Expr expr, boolean integrityChecks, Path currentPath)
      throws ResolutionException {
    return resolveWithVisitor(expr, new ResolutionVisitor.Filesystem(currentPath, integrityChecks));
  }

  public static final Expr resolve(Expr expr, boolean integrityChecks) throws ResolutionException {
    return resolve(expr, integrityChecks, null);
  }

  public static final Expr resolveFromResources(
      Expr expr, boolean integrityChecks, Path currentPath, ClassLoader classLoader)
      throws ResolutionException {
    return resolveWithVisitor(
        expr, new ResolutionVisitor.Resources(currentPath, integrityChecks, classLoader));
  }

  public static final Expr resolveFromResources(
      Expr expr, boolean integrityChecks, Path currentPath) throws ResolutionException {
    return resolveFromResources(
        expr, integrityChecks, currentPath, Resolver.class.getClassLoader());
  }

  public static final Expr resolveFromResources(Expr expr, boolean integrityChecks)
      throws ResolutionException {
    return resolveFromResources(expr, integrityChecks, null);
  }

  private static final Expr resolveWithVisitor(Expr expr, ResolutionVisitor visitor)
      throws ResolutionException {
    Expr result;
    try {
      result = expr.acceptVis(visitor);
    } catch (ResolutionVisitor.WrappedParsingFailure e) {
      throw new ResolutionException(e.getMessage(), e.underlying);
    } catch (ResolutionVisitor.WrappedIOException e) {
      throw new ResolutionException(e.getMessage(), e.underlying);
    } catch (ResolutionVisitor.Missing e) {
      throw new ResolutionException(e.getMessage());
    } catch (ResolutionVisitor.MissingEnv e) {
      throw new ResolutionException(e.getMessage());
    } catch (ResolutionVisitor.IntegrityCheckException e) {
      throw new ResolutionException(e.getMessage());
    } catch (UnsupportedOperationException e) {
      throw new ResolutionException(e.getMessage());
    }
    return result;
  }
}
