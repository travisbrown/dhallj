package org.dhallj.core;

import java.net.URI;
import java.nio.file.Path;
import org.dhallj.core.Import;

final class IsResolved extends Visitor.Property {
  public static final Visitor<Boolean> instance = new IsResolved();

  @Override
  public Boolean onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onRemoteImport(URI url, Boolean using, Import.Mode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onEnvImport(String value, Import.Mode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onMissingImport(Import.Mode mode, byte[] hash) {
    return false;
  }
}
