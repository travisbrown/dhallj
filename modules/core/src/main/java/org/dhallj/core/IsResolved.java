package org.dhallj.core;

import java.net.URI;
import java.nio.file.Path;

final class IsResolved extends Visitor.Property {
  public static final Visitor<Boolean> instance = new IsResolved();

  @Override
  public Boolean onOperatorApplication(Operator operator, Boolean lhs, Boolean rhs) {
    if (operator.equals(Operator.IMPORT_ALT)) {
      return false;
    } else {
      return super.onOperatorApplication(operator, lhs, rhs);
    }
  }

  @Override
  public Boolean onLocalImport(Path path, Expr.ImportMode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onClasspathImport(Path path, Expr.ImportMode mode, byte[] hash) {
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
