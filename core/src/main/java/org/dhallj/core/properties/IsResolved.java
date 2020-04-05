package org.dhallj.core.properties;

import java.net.URI;
import java.nio.file.Path;
import org.dhallj.core.Import;
import org.dhallj.core.Thunk;
import org.dhallj.core.Vis;
import org.dhallj.core.visitor.ConstantVis;

public final class IsResolved extends ConstantVis<Boolean> {
  public static final Vis<Boolean> instance = new IsResolved();

  IsResolved() {
    super(true);
  }

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
