package org.dhallj.imports.mini;

import org.dhallj.core.Expr;
import java.nio.file.Path;

public final class Resolver {
  public static final Expr resolve(Expr expr, Path currentPath) throws ResolutionException {
    return resolveWithVisitor(expr, new ResolutionVisitor.Filesystem(currentPath));
  }

  public static final Expr resolve(Expr expr) throws ResolutionException {
    return resolve(expr, null);
  }

  public static final Expr resolveFromResources(
      Expr expr, Path currentPath, ClassLoader classLoader) throws ResolutionException {
    return resolveWithVisitor(expr, new ResolutionVisitor.Resources(currentPath, classLoader));
  }

  public static final Expr resolveFromResources(Expr expr, Path currentPath)
      throws ResolutionException {
    return resolveFromResources(expr, currentPath, Resolver.class.getClassLoader());
  }

  public static final Expr resolveFromResources(Expr expr) throws ResolutionException {
    return resolveFromResources(expr, null);
  }

  private static final Expr resolveWithVisitor(Expr expr, ResolutionVisitor visitor)
      throws ResolutionException {
    Expr result;
    try {
      result = expr.acceptVis(visitor);
    } catch (ResolutionVisitor.WrappedParseException e) {
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
