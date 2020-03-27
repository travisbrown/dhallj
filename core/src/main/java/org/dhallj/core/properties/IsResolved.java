package org.dhallj.core.properties;

import java.net.URI;
import java.nio.file.Path;
import org.dhallj.core.Import;
import org.dhallj.core.Thunk;
import org.dhallj.core.Visitor;
import org.dhallj.core.visitor.ConstantVisitor;

public final class IsResolved extends ConstantVisitor.Internal<Boolean> {
  public static final Visitor<Thunk<Boolean>, Boolean> instance = new IsResolved();

  IsResolved() {
    super(true);
  }

  @Override
  public Boolean onLocalImport(Path path, Import.Mode mode) {
    return false;
  }

  @Override
  public Boolean onRemoteImport(URI url, Import.Mode mode) {
    return false;
  }

  @Override
  public Boolean onEnvImport(String value, Import.Mode mode) {
    return false;
  }

  @Override
  public Boolean onMissingImport(Import.Mode mode) {
    return false;
  }
}
