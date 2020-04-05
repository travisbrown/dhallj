package org.dhallj.imports.mini;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.visitor.IdentityVis;
import org.dhallj.parser.Dhall;
import org.dhallj.parser.ParseException;

abstract class ResolutionVisitor extends IdentityVis {
  private final Path currentPath;

  ResolutionVisitor(Path currentPath) {
    this.currentPath = currentPath;
  }

  protected abstract String readContents(Path path) throws IOException, URISyntaxException;

  protected abstract ResolutionVisitor withCurrentPath(Path newCurrentPath);

  @Override
  public Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    if (operator.equals(Operator.IMPORT_ALT)) {
      return lhs;
    } else {
      return Expr.makeOperatorApplication(operator, lhs, rhs);
    }
  }

  @Override
  public Expr onMissingImport(Import.Mode mode, byte[] hash) {
    Expr result;

    if (mode.equals(Import.Mode.LOCATION)) {
      result = Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Missing");
    } else {
      throw new Missing();
    }

    return checkHash(result, hash);
  }

  @Override
  public Expr onEnvImport(String name, Import.Mode mode, byte[] hash) {
    Expr result;

    if (mode.equals(Import.Mode.LOCATION)) {
      result =
          Expr.makeApplication(
              Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Environment"),
              Expr.makeTextLiteral(name));
    } else {
      String value = System.getenv(name);

      if (value != null) {
        if (mode.equals(Import.Mode.RAW_TEXT)) {
          result = Expr.makeTextLiteral(value);
        } else {
          try {
            result = Dhall.parse(value).acceptVis(this);
          } catch (ParseException underlying) {
            throw new WrappedParseException(name, underlying);
          }
        }
      } else {
        throw new MissingEnv(name);
      }
    }

    return checkHash(result, hash);
  }

  @Override
  public Expr onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    Expr result;

    if (mode.equals(Import.Mode.LOCATION)) {
      result =
          Expr.makeApplication(
              Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Local"),
              Expr.makeTextLiteral(path.toString()));
    } else {
      Path resolvedPath = (currentPath == null) ? path : currentPath.resolve(path);
      String contents;

      try {
        contents = this.readContents(resolvedPath);
      } catch (IOException underlying) {
        throw new WrappedIOException(resolvedPath, underlying);
      } catch (URISyntaxException underlying) {
        throw new WrappedIOException(resolvedPath, underlying);
      }

      if (mode.equals(Import.Mode.RAW_TEXT)) {
        result = Expr.makeTextLiteral(contents);
      } else {
        try {
          result = Dhall.parse(contents).acceptVis(this.withCurrentPath(resolvedPath.getParent()));
        } catch (ParseException underlying) {
          throw new WrappedParseException(path.toString(), underlying);
        }
      }
    }

    return checkHash(result, hash);
  }

  @Override
  public Expr onRemoteImport(URI url, Expr using, Import.Mode mode, byte[] hash) {
    Expr result;

    if (mode.equals(Import.Mode.LOCATION)) {
      result =
          Expr.makeApplication(
              Expr.makeFieldAccess(Expr.Constants.LOCATION_TYPE, "Remote"),
              Expr.makeTextLiteral(url.toString()));
    } else {
      throw new UnsupportedOperationException("Remote import resolution not currently supported");
    }

    return checkHash(result, hash);
  }

  private static Expr checkHash(Expr result, byte[] expected) {
    if (expected != null) {
      byte[] received = result.normalize().alphaNormalize().hashBytes();
      if (!Arrays.equals(received, expected)) {
        throw new IntegrityCheckException(expected, received);
      }
    }
    return result;
  }

  static final class Filesystem extends ResolutionVisitor {
    Filesystem(Path currentPath) {
      super(currentPath);
    }

    protected ResolutionVisitor withCurrentPath(Path newCurrentPath) {
      return new Filesystem(newCurrentPath);
    }

    protected String readContents(Path path) throws IOException, URISyntaxException {
      return new String(Files.readAllBytes(path));
    }
  }

  static final class Resources extends ResolutionVisitor {
    private final ClassLoader classLoader;

    Resources(Path currentPath, ClassLoader classLoader) {
      super(currentPath);
      this.classLoader = classLoader;
    }

    protected ResolutionVisitor withCurrentPath(Path newCurrentPath) {
      return new Resources(newCurrentPath, this.classLoader);
    }

    protected String readContents(Path path) throws IOException, URISyntaxException {
      return new String(
          Files.readAllBytes(Paths.get(this.classLoader.getResource(path.toString()).toURI())));
    }
  }

  static final class WrappedParseException extends RuntimeException {
    String location;
    ParseException underlying;

    WrappedParseException(String location, ParseException underlying) {
      super(String.format("Can't parse import: %s", location), underlying);
      this.location = location;
    }
  }

  static final class WrappedIOException extends RuntimeException {
    Path path;
    Exception underlying;

    WrappedIOException(Path path, Exception underlying) {
      super(String.format("Missing file %s", path), underlying);
      this.path = path;
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
              "Import integrity check failed (received: %s)", Expr.Util.encodeBytes(received)));
      this.expected = expected;
      this.received = received;
    }
  }
}