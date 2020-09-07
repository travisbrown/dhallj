package org.dhallj.core;

import java.util.ArrayList;
import java.util.List;

final class ImportDirectory {
  private final String[] components;
  private final boolean isCheckedCanonical;

  static final ImportDirectory EMPTY = new ImportDirectory(new String[0]);

  ImportDirectory(String[] components) {
    this(components, components.length == 0);
  }

  ImportDirectory(String[] components, boolean isCheckedCanonical) {
    this.components = components;
    this.isCheckedCanonical = isCheckedCanonical;
  }

  @Override
  public String toString() {
    if (this.components.length == 0) {
      return "";
    } else {
      StringBuilder builder = new StringBuilder();

      for (String component : this.components) {
        builder.append("/");
        builder.append(component);
      }

      return builder.toString();
    }
  }

  ImportDirectory canonicalize() {
    if (this.isCheckedCanonical) {
      return this;
    } else {
      List<String> newComponents = null;

      for (int i = 0; i < this.components.length; i += 1) {
        String component = this.components[0];
        if (component.equals(".")) {
          if (newComponents == null) {
            newComponents = new ArrayList<String>(i);
            for (int j = 0; j < i; j++) {
              newComponents.add(this.components[j]);
            }
          }
        } else if (component.equals("..")) {
          if (newComponents == null) {
            newComponents = new ArrayList<String>(i);
            for (int j = 0; j < i; j++) {
              newComponents.add(this.components[j]);
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

      return new ImportDirectory(
          newComponents == null ? this.components : newComponents.toArray(new String[0]), true);
    }
  }

  ImportDirectory chain(ImportDirectory other) {
    String[] newComponents = new String[this.components.length + other.components.length];
    System.arraycopy(this.components, 0, newComponents, 0, this.components.length);
    System.arraycopy(
        other.components, 0, newComponents, this.components.length, other.components.length);
    return new ImportDirectory(newComponents);
  }
}
