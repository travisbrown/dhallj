package org.dhallj.core;

import java.net.URI;
import java.nio.file.Path;

public interface Import {
  public enum Mode {
    CODE,
    RAW_TEXT,
    LOCATION;
  }
}
