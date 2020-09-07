package org.dhallj.core;

import java.net.URI;

final class IsResolved extends Visitor.Property {
  public static final Visitor<Boolean> instance = new IsResolved();

  @Override
  public Boolean onLocalImport(
      Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onClasspathImport(
      Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onRemoteImport(URI url, Boolean using, Expr.ImportMode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onEnvImport(String value, Expr.ImportMode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onMissingImport(Expr.ImportMode mode, byte[] hash) {
    return false;
  }
}
