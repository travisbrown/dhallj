package org.dhallj.imports.mini;

import org.dhallj.core.DhallException.ResolutionFailure;
import org.dhallj.core.Expr;
import java.nio.file.Path;

public final class Resolver {
  public static final Expr resolve(Expr expr, boolean integrityChecks, Path currentPath)
      throws ResolutionFailure {
    return resolveWithVisitor(expr, new ResolutionVisitor.Filesystem(currentPath, integrityChecks));
  }

  public static final Expr resolve(Expr expr, boolean integrityChecks) throws ResolutionFailure {
    return resolve(expr, integrityChecks, null);
  }

  public static final Expr resolve(Expr expr) throws ResolutionFailure {
    return resolve(expr, true);
  }

  public static final Expr resolveFromResources(
      Expr expr, boolean integrityChecks, Path currentPath, ClassLoader classLoader)
      throws ResolutionFailure {
    return resolveWithVisitor(
        expr, new ResolutionVisitor.Resources(currentPath, integrityChecks, classLoader));
  }

  public static final Expr resolveFromResources(
      Expr expr, boolean integrityChecks, Path currentPath) throws ResolutionFailure {
    return resolveFromResources(
        expr, integrityChecks, currentPath, Resolver.class.getClassLoader());
  }

  public static final Expr resolveFromResources(Expr expr, boolean integrityChecks)
      throws ResolutionFailure {
    return resolveFromResources(expr, integrityChecks, null);
  }

  public static final Expr resolveFromResources(Expr expr) throws ResolutionFailure {
    return resolveFromResources(expr, true);
  }

  private static final Expr resolveWithVisitor(Expr expr, ResolutionVisitor visitor)
      throws ResolutionFailure {
    Expr result;
    try {
      result = expr.accept(visitor);
    } catch (ResolutionVisitor.WrappedParsingFailure e) {
      throw new ResolutionFailure(e.getMessage(), e.underlying);
    } catch (ResolutionVisitor.WrappedIOException e) {
      throw new ResolutionFailure(e.getMessage(), e.underlying);
    } catch (ResolutionVisitor.Missing e) {
      throw new ResolutionFailure(e.getMessage());
    } catch (ResolutionVisitor.MissingEnv e) {
      throw new ResolutionFailure(e.getMessage());
    } catch (ResolutionVisitor.IntegrityCheckException e) {
      throw new ResolutionFailure(e.getMessage());
    } catch (UnsupportedOperationException e) {
      throw new ResolutionFailure(e.getMessage());
    }
    return result;
  }
}
