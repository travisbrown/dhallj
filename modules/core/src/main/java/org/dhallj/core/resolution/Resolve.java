package org.dhallj.core.resolution;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;

public abstract class Resolve extends Visitor.Identity {
  public final Expr onMissingImport(Expr.ImportMode mode, byte[] hash) {
    switch (mode) {
      case CODE:
        return this.onMissingImport(hash);
      case RAW_TEXT:
        return this.onMissingImportAsText(hash);
      case LOCATION:
        return Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Missing");
      default:
        return null;
    }
  }

  public final Expr onEnvImport(String value, Expr.ImportMode mode, byte[] hash) {
    switch (mode) {
      case CODE:
        return this.onEnvImport(value, hash);
      case RAW_TEXT:
        return this.onEnvImportAsText(value, hash);
      case LOCATION:
        return Expr.makeApplication(
            Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Environment"),
            Expr.makeTextLiteral(value));
      default:
        return null;
    }
  }

  public final Expr onLocalImport(
      Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
    switch (mode) {
      case CODE:
        return this.onLocalImport(base, components, hash);
      case RAW_TEXT:
        return this.onLocalImportAsText(base, components, hash);
      case LOCATION:
        return Expr.makeApplication(
            Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Local"),
            Expr.makeTextLiteral(Resolve.toPathString(base, components, false)));
      default:
        return null;
    }
  }

  public final Expr onClasspathImport(
      Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
    switch (mode) {
      case CODE:
        return this.onClasspathImport(base, components, hash);
      case RAW_TEXT:
        return this.onClasspathImportAsText(base, components, hash);
      case LOCATION:
        return Expr.makeApplication(
            Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Local"),
            Expr.makeTextLiteral(Resolve.toPathString(base, components, true)));
      default:
        return null;
    }
  }

  public final Expr onRemoteImport(URI url, Expr using, Expr.ImportMode mode, byte[] hash) {
    switch (mode) {
      case CODE:
        return this.onRemoteImport(url, using, hash);
      case RAW_TEXT:
        return this.onRemoteImportAsText(url, using, hash);
      case LOCATION:
        return Expr.makeApplication(
            Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Remote"),
            Expr.makeTextLiteral(url.toString()));
      default:
        return null;
    }
  }

  protected abstract Expr onMissingImport(byte[] hash);

  protected abstract Expr onEnvImport(String value, byte[] hash);

  protected abstract Expr onLocalImport(Expr.ImportBase base, String[] components, byte[] hash);

  protected abstract Expr onClasspathImport(Expr.ImportBase base, String[] components, byte[] hash);

  protected abstract Expr onRemoteImport(URI url, Expr using, byte[] hash);

  protected abstract Expr onMissingImportAsText(byte[] hash);

  protected abstract Expr onEnvImportAsText(String value, byte[] hash);

  protected abstract Expr onLocalImportAsText(
      Expr.ImportBase base, String[] components, byte[] hash);

  protected abstract Expr onClasspathImportAsText(
      Expr.ImportBase base, String[] components, byte[] hash);

  protected abstract Expr onRemoteImportAsText(URI url, Expr using, byte[] hash);

  public static final Expr chain(Expr a, Expr b) {
    return b.accept(a.accept(Chain.instance));
  }

  private static final String toPathString(
      Expr.ImportBase base, String[] components, boolean isClasspath) {
    StringBuilder builder = new StringBuilder(base.toString());

    if (isClasspath) {
      builder.append("classpath:");
    }

    for (String component : components) {
      builder.append("/");
      builder.append(component);
    }

    return builder.toString();
  }
}
