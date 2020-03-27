package org.dhallj.core;

import java.net.URI;
import java.nio.file.Path;

public interface Import {
  public <A> A accept(Visitor<A> visitor);

  public enum Mode {
    CODE,
    RAW_TEXT,
    LOCATION;
  }

  public interface Visitor<A> {
    public abstract A onLocalImport(Path path, Mode mode);

    public abstract A onRemoteImport(URI url, Mode mode);

    public abstract A onEnvImport(String value, Mode mode);

    public abstract A onMissingImport(Mode mode);
  }
}
