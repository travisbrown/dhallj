package java.net;

public class URI {
  private final String input;

  public URI(String input) throws URISyntaxException {
    this.input = input;
  }

  public final String getScheme() {
    return "";
  }

  public final String getAuthority() {
    return "";
  }

  public final String getPath() {
    return "";
  }

  public final String getQuery() {
    return "";
  }
}
