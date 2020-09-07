package org.dhallj.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class ImportCanonicalize extends Visitor.Identity {
  public static final Visitor<Expr> instance = new ImportCanonicalize();

  @Override
  public Expr onLocalImport(
      Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
    return Expr.makeLocalImport(base, ImportCanonicalize.canonicalize(components), mode, hash);
  }

  @Override
  public Expr onClasspathImport(
      Expr.ImportBase base, String[] components, Expr.ImportMode mode, byte[] hash) {
    return Expr.makeClasspathImport(base, ImportCanonicalize.canonicalize(components), mode, hash);
  }

  @Override
  public Expr onRemoteImport(URI url, Expr using, Expr.ImportMode mode, byte[] hash) {
    String[] parts = url.getPath().split("/");
    String[] components = Arrays.copyOfRange(parts, 1, parts.length);
    String[] canonicalized = ImportCanonicalize.canonicalize(components);

    StringBuilder builder = new StringBuilder();

    for (String component : components) {
      builder.append("/");
      builder.append(component);
    }

    URI newUrl;
    try {
      newUrl =
          new URI(
              url.getScheme(),
              url.getAuthority(),
              builder.toString(),
              url.getQuery(),
              url.getFragment());
    } catch (URISyntaxException cause) {
      // Should never happen; we've already parsed the original version of the URL.
      newUrl = null;
    }

    return Expr.makeRemoteImport(newUrl, using, mode, hash);
  }

  private static final String[] canonicalize(String[] components) {
    List<String> newComponents = null;

    for (int i = 0; i < components.length - 1; i += 1) {
      String component = components[i];
      if (component.equals(".")) {
        if (newComponents == null) {
          newComponents = new ArrayList<String>(i);
          for (int j = 0; j < i; j++) {
            newComponents.add(components[j]);
          }
        }
      } else if (component.equals("..")) {
        if (newComponents == null) {
          newComponents = new ArrayList<String>(i);
          for (int j = 0; j < i; j++) {
            newComponents.add(components[j]);
          }
        }
        if (newComponents.isEmpty() || newComponents.get(newComponents.size() - 1).equals("..")) {
          newComponents.add("..");
        } else {
          newComponents.remove(newComponents.size() - 1);
        }
      } else {
        if (newComponents != null) {
          newComponents.add(component);
        }
      }
    }

    if (newComponents != null) {
      newComponents.add(components[components.length - 1]);
    }

    return newComponents == null ? components : newComponents.toArray(new String[0]);
  }
}
