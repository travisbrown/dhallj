package org.dhallj.core;

final class IsReferentiallyTransparent extends Visitor.Property {
  public static final Visitor<Boolean> instance = new IsReferentiallyTransparent();

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
  public Boolean onEnvImport(String value, Expr.ImportMode mode, byte[] hash) {
    return false;
  }
}
