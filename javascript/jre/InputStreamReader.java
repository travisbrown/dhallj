package java.io;

public class InputStreamReader extends Reader {
  public InputStreamReader(InputStream in) {}
  public InputStreamReader(InputStream in, String charsetName) {}
  public int read(char[] cbuf, int off, int len) {
    return -1;
  }

  public void close() {}
}
