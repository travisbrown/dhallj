package org.dhallj.core;

import java.net.URI;
import java.nio.file.Path;

public interface Import {
  public enum Mode {
    CODE,
    RAW_TEXT,
    LOCATION;

    public String toString() {
      switch (this) {
        case RAW_TEXT:
          return "Text";
        case LOCATION:
          return "Location";
        default:
          return "Code";
      }
    }
  }
}
