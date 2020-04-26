package java.nio.file;

public class Paths {
  /**
   * @throws  InvalidPathException
   *          if the path string cannot be converted to a {@code Path}
   */
  public static final Path get(String input) {
    return new Path(input);
  }
}
