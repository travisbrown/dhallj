package org.dhallj.core.resolution;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.dhallj.core.Expr;
import org.dhallj.core.ExternalVisitor;

final class Chain extends ExternalVisitor.Constant<ExternalVisitor<Expr>> {
  private static final class NonImportVisitor extends ExternalVisitor.Constant<Expr> {
    NonImportVisitor() {
      super(null);
    }
  }

  Chain() {
    super(Chain.nonImportVisitorInstance);
  }

  private static final ExternalVisitor<Expr> nonImportVisitorInstance = new NonImportVisitor();
  static final ExternalVisitor<ExternalVisitor<Expr>> instance = new Chain();

  public ExternalVisitor<Expr> onLocalImport(
      Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
    return new Chain.FileLocalImportChain(base, components);
  }

  public ExternalVisitor<Expr> onClasspathImport(
      Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
    return new Chain.ClasspathLocalImportChain(base, components);
  }

  public ExternalVisitor<Expr> onRemoteImport(
      URI url, Expr using, Expr.ImportMode mode, byte[] hash) {
    return new Chain.RemoteImportChain(url, using);
  }

  private abstract static class ImportChain extends ExternalVisitor.Constant {
    protected ImportChain() {
      super(null);
    }

    @Override
    public final Expr onClasspathImport(
        Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
      return Expr.makeClasspathImport(base, components, mode, hash);
    }

    @Override
    public final Expr onRemoteImport(URI url, Expr using, Expr.ImportMode mode, byte[] hash) {
      return Expr.makeRemoteImport(url, using, mode, hash);
    }

    @Override
    public final Expr onEnvImport(String value, Expr.ImportMode mode, byte[] hash) {
      return Expr.makeEnvImport(value, mode, hash);
    }

    @Override
    public final Expr onMissingImport(Expr.ImportMode mode, byte[] hash) {
      return Expr.makeMissingImport(mode, hash);
    }
  }

  private abstract static class LocalImportChain extends ImportChain {
    protected final Expr.ImportBase parentBase;
    private final String[] parentComponents;

    protected LocalImportChain(Expr.ImportBase base, String[] components) {
      this.parentBase = base;
      this.parentComponents = components;
    }

    protected abstract Expr makeExpr(String[] components, Expr.ImportMode mode, byte[] hash);

    public final Expr onLocalImport(
        Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
      int parentDirectoryLength;
      String[] newComponents;

      switch (base) {
        case ABSOLUTE:
        case HOME:
          return Expr.makeLocalImport(base, components, mode, hash);
        case RELATIVE:
          parentDirectoryLength = this.parentComponents.length - 1;
          newComponents = new String[parentDirectoryLength + components.length];
          System.arraycopy(this.parentComponents, 0, newComponents, 0, parentDirectoryLength);
          System.arraycopy(components, 0, newComponents, parentDirectoryLength, components.length);
          return this.makeExpr(newComponents, mode, hash);
        case PARENT:
          parentDirectoryLength = this.parentComponents.length - 1;
          newComponents = new String[this.parentComponents.length + components.length];
          System.arraycopy(this.parentComponents, 0, newComponents, 0, parentDirectoryLength);
          newComponents[parentDirectoryLength] = "..";
          System.arraycopy(
              components, 0, newComponents, this.parentComponents.length, components.length);
          return this.makeExpr(newComponents, mode, hash);
        default:
          return null;
      }
    }
  }

  private static final class FileLocalImportChain extends LocalImportChain {
    FileLocalImportChain(Expr.ImportBase base, String[] components) {
      super(base, components);
    }

    protected Expr makeExpr(String[] components, Expr.ImportMode mode, byte[] hash) {
      return Expr.makeLocalImport(this.parentBase, components, mode, hash);
    }
  }

  private static final class ClasspathLocalImportChain extends LocalImportChain {
    ClasspathLocalImportChain(Expr.ImportBase base, String[] components) {
      super(base, components);
    }

    protected Expr makeExpr(String[] components, Expr.ImportMode mode, byte[] hash) {
      return Expr.makeClasspathImport(this.parentBase, components, mode, hash);
    }
  }

  private static final class RemoteImportChain extends ImportChain {
    private final URI parentUrl;
    private final Expr parentUsing;

    protected RemoteImportChain(URI url, Expr using) {
      this.parentUrl = url;
      this.parentUsing = using;
    }

    public final Expr onLocalImport(
        Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
      String[] parts = this.parentUrl.getPath().split("/");
      String[] parentComponents = Arrays.copyOfRange(parts, 1, parts.length);

      int parentDirectoryLength;
      String[] newComponents;

      switch (base) {
        case ABSOLUTE:
        case HOME:
          return Expr.makeLocalImport(base, components, mode, hash);
        case RELATIVE:
          parentDirectoryLength = parentComponents.length - 1;
          newComponents = new String[parentDirectoryLength + components.length];
          System.arraycopy(parentComponents, 0, newComponents, 0, parentDirectoryLength);
          System.arraycopy(components, 0, newComponents, parentDirectoryLength, components.length);
          return this.makeExpr(newComponents, mode, hash);
        case PARENT:
          parentDirectoryLength = parentComponents.length - 1;
          newComponents = new String[parentComponents.length + components.length];
          System.arraycopy(parentComponents, 0, newComponents, 0, parentDirectoryLength);
          newComponents[parentDirectoryLength] = "..";
          System.arraycopy(
              components, 0, newComponents, parentComponents.length, components.length);
          return this.makeExpr(newComponents, mode, hash);
        default:
          return null;
      }
    }

    private final Expr makeExpr(String[] components, Expr.ImportMode mode, byte[] hash) {
      StringBuilder builder = new StringBuilder();

      for (String component : components) {
        builder.append("/");
        builder.append(component);
      }

      URI newUrl;
      try {
        newUrl =
            new URI(
                this.parentUrl.getScheme(),
                this.parentUrl.getAuthority(),
                builder.toString(),
                this.parentUrl.getQuery(),
                this.parentUrl.getFragment());
      } catch (URISyntaxException cause) {
        // Should never happen; we've already parsed the original version of the URL.
        newUrl = null;
      }

      return Expr.makeRemoteImport(newUrl, this.parentUsing, mode, hash);
    }
  }
}
