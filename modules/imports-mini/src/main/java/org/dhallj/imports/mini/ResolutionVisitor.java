package org.dhallj.imports.mini;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.dhallj.core.DhallException.ParsingFailure;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.Visitor;
import org.dhallj.parser.DhallParser;

abstract class ResolutionVisitor extends Visitor.Identity {
  private final Path currentPath;
  protected final boolean integrityChecks;

  ResolutionVisitor(Path currentPath, boolean integrityChecks) {
    this.currentPath = currentPath;
    this.integrityChecks = integrityChecks;
  }

  protected abstract String readContents(Path path) throws IOException, URISyntaxException;

  protected abstract ResolutionVisitor withCurrentPath(Path newCurrentPath);

  public void bind(String name, Expr type) {}

  @Override
  public Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    if (operator.equals(Operator.IMPORT_ALT)) {
      return lhs;
    } else {
      return Expr.makeOperatorApplication(operator, lhs, rhs);
    }
  }

  @Override
  public Expr onMissingImport(Expr.ImportMode mode, byte[] hash) {
    Expr result;

    if (mode.equals(Expr.ImportMode.LOCATION)) {
      result = Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Missing");
    } else {
      throw new Missing();
    }

    return checkHash(result, hash);
  }

  @Override
  public Expr onEnvImport(String name, Expr.ImportMode mode, byte[] hash) {
    Expr result;

    if (mode.equals(Expr.ImportMode.LOCATION)) {
      result =
          Expr.makeApplication(
              Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Environment"),
              Expr.makeTextLiteral(name));
    } else {
      String value = System.getenv(name);

      if (value != null) {
        if (mode.equals(Expr.ImportMode.RAW_TEXT)) {
          result = Expr.makeTextLiteral(value);
        } else {
          try {
            result = DhallParser.parse(value).accept(this);
          } catch (ParsingFailure underlying) {
            throw new WrappedParsingFailure(name, underlying);
          }
        }
      } else {
        throw new MissingEnv(name);
      }
    }

    return checkHash(result, hash);
  }

  @Override
  public Expr onLocalImport(
      Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
    Expr result;

    StringBuilder builder = new StringBuilder(base.toString());

    for (String component : components) {
      builder.append("/");
      builder.append(component);
    }

    Path path = Paths.get(builder.toString());

    if (mode.equals(Expr.ImportMode.LOCATION)) {
      result =
          Expr.makeApplication(
              Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Local"),
              Expr.makeTextLiteral(path.toString()));
    } else {
      Path resolvedPath = (currentPath == null) ? path : currentPath.resolveSibling(path);
      String contents;

      try {
        contents = this.readContents(resolvedPath);
      } catch (IOException underlying) {
        throw new WrappedIOException(resolvedPath, underlying);
      } catch (URISyntaxException underlying) {
        throw new WrappedIOException(resolvedPath, underlying);
      }

      if (mode.equals(Expr.ImportMode.RAW_TEXT)) {
        result = Expr.makeTextLiteral(contents);
      } else {
        try {
          result = DhallParser.parse(contents).accept(this.withCurrentPath(resolvedPath));
        } catch (ParsingFailure underlying) {
          throw new WrappedParsingFailure(path.toString(), underlying);
        }
      }
    }

    return checkHash(result, hash);
  }

  @Override
  public Expr onRemoteImport(URI url, Expr using, Expr.ImportMode mode, byte[] hash) {
    Expr result;

    if (mode.equals(Expr.ImportMode.LOCATION)) {
      result =
          Expr.makeApplication(
              Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Remote"),
              Expr.makeTextLiteral(url.toString()));
    } else {
      throw new UnsupportedOperationException("Remote import resolution not currently supported");
    }

    return checkHash(result, hash);
  }

  private final Expr checkHash(Expr result, byte[] expected) {
    if (expected != null && this.integrityChecks) {
      byte[] received = result.normalize().alphaNormalize().getHashBytes();
      if (!Arrays.equals(received, expected)) {
        throw new IntegrityCheckException(expected, received);
      }
    }
    return result;
  }

  static final class Filesystem extends ResolutionVisitor {
    Filesystem(Path currentPath, boolean integrityChecks) {
      super(currentPath, integrityChecks);
    }

    protected ResolutionVisitor withCurrentPath(Path newCurrentPath) {
      return new Filesystem(newCurrentPath, this.integrityChecks);
    }

    protected String readContents(Path path) throws IOException, URISyntaxException {
      return new String(Files.readAllBytes(path));
    }
  }

  static final class Resources extends ResolutionVisitor {
    private final ClassLoader classLoader;

    Resources(Path currentPath, boolean integrityChecks, ClassLoader classLoader) {
      super(currentPath, integrityChecks);
      this.classLoader = classLoader;
    }

    protected ResolutionVisitor withCurrentPath(Path newCurrentPath) {
      return new Resources(newCurrentPath, this.integrityChecks, this.classLoader);
    }

    protected String readContents(Path path) throws IOException, URISyntaxException {
      return new String(
          Files.readAllBytes(Paths.get(this.classLoader.getResource(path.toString()).toURI())));
    }
  }

  static final class WrappedParsingFailure extends RuntimeException {
    String location;
    ParsingFailure underlying;

    WrappedParsingFailure(String location, ParsingFailure underlying) {
      super(String.format("Can't parse import: %s", location), underlying);
      this.location = location;
      this.underlying = underlying;
    }
  }

  static final class WrappedIOException extends RuntimeException {
    Path path;
    Exception underlying;

    WrappedIOException(Path path, Exception underlying) {
      super(String.format("Missing file %s", path), underlying);
      this.path = path;
      this.underlying = underlying;
    }
  }

  static final class Missing extends RuntimeException {
    Missing() {
      super("No valid imports");
    }
  }

  static final class MissingEnv extends RuntimeException {
    String name;

    MissingEnv(String name) {
      super(String.format("Missing environment variable %s", name));
      this.name = name;
    }
  }

  static final class IntegrityCheckException extends RuntimeException {
    final byte[] expected;
    final byte[] received;

    IntegrityCheckException(byte[] expected, byte[] received) {
      super(
          String.format(
              "Import integrity check failed (received: %s)", Expr.Util.encodeHashBytes(received)));
      this.expected = expected;
      this.received = received;
    }
  }
}
