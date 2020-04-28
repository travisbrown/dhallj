package java.nio.file;

import java.util.Iterator;

public class Path {
  private final String input;

  public Path(String input) {
    this.input = input;
  }

  public final boolean isAbsolute() {
    return this.input.charAt(0) == '/';
  }

  public final Iterator<Path> iterator() {
    return null;
  }

  public final int getNameCount() {
    return 0;
  }

  public Path resolve(String other) {
    return this;
  }
}
