package org.dhallj.core.binary;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.cbor.Writer;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.LetBinding;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Vis;

public final class Encode implements Vis<Writer> {
  public static Vis<Writer> instance = new Encode();

  public Writer onNote(Writer base, Source source) {
    return base;
  }

  public Writer onNatural(final BigInteger value) {
    return new Writer() {
      public void writeToStream(OutputStream stream) throws IOException {
        this.writeArrayStart(stream, 2);
        this.writeLong(stream, Label.NATURAL);
        this.writeBigInteger(stream, value);
      }
    };
  }

  public Writer onInteger(final BigInteger value) {
    return new Writer() {
      public void writeToStream(OutputStream stream) throws IOException {
        this.writeArrayStart(stream, 2);
        this.writeLong(stream, Label.INTEGER);
        this.writeBigInteger(stream, value);
      }
    };
  }

  public Writer onDouble(final double value) {
    return new Writer() {
      public void writeToStream(OutputStream stream) throws IOException {
        this.writeDouble(stream, value);
      }
    };
  }

  public Writer onBuiltIn(final String name) {
    return new Writer() {
      public void writeToStream(OutputStream stream) throws IOException {
        if (name.equals("True")) {
          this.writeBoolean(stream, true);
        } else if (name.equals("False")) {
          this.writeBoolean(stream, false);
        } else {
          this.writeString(stream, name);
        }
      }
    };
  }

  public Writer onIdentifier(final String name, final long index) {
    return new Writer() {
      public void writeToStream(OutputStream stream) throws IOException {

        if (name.equals("_")) {
          this.writeLong(stream, index);
        } else {
          this.writeArrayStart(stream, 2);
          this.writeString(stream, name);
          this.writeLong(stream, index);
        }
      }
    };
  }

  public void bind(String param, Expr type) {}

  public Writer onLambda(final String name, Writer type, Writer result) {
    List<Writer> writers = new ArrayList<Writer>(3);

    if (name.equals("_")) {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeArrayStart(stream, 3);
              this.writeLong(stream, Label.LAMBDA);
            }
          });
    } else {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeArrayStart(stream, 4);
              this.writeLong(stream, Label.LAMBDA);
              this.writeString(stream, name);
            }
          });
    }

    writers.add(type);
    writers.add(result);

    return new Writer.Nested(writers);
  }

  public Writer onPi(final String name, Writer type, Writer result) {
    List<Writer> writers = new ArrayList<Writer>(3);

    if (name.equals("_")) {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeArrayStart(stream, 3);
              this.writeLong(stream, Label.PI);
            }
          });
    } else {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeArrayStart(stream, 4);
              this.writeLong(stream, Label.PI);
              this.writeString(stream, name);
            }
          });
    }

    writers.add(type);
    writers.add(result);

    return new Writer.Nested(writers);
  }

  public Writer onLet(final String name, Writer type, Writer value, Writer body) {
    throw new UnsupportedOperationException("let serialization not yet implemented");

    /*
    List<Writer> writers = new ArrayList<Writer>();

    writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeArrayStart(stream, 3);
              this.writeLong(stream, Label.LET);
            }
          });
    */
  }

  public Writer onLet(final List<LetBinding<Writer>> bindings, Writer body) {
    List<Writer> writers = new ArrayList<Writer>();

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 2 + bindings.size() * 3);
            this.writeLong(stream, Label.LET);
          }
        });

    for (final LetBinding<Writer> binding : bindings) {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeString(stream, binding.getName());
              if (!binding.hasType()) {
                this.writeNull(stream);
              }
            }
          });

      if (binding.hasType()) {
        writers.add(binding.getType());
      }

      writers.add(binding.getValue());
    }
    writers.add(body);

    return new Writer.Nested(writers);
  }

  public void preText(int size) {}

  public Writer onText(final String[] parts, List<Writer> interpolated) {
    List<Writer> writers = new ArrayList<Writer>(parts.length + 1);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, parts.length * 2);
            this.writeLong(stream, Label.TEXT);
          }
        });

    Iterator<Writer> it = interpolated.iterator();
    for (final String part : parts) {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeString(stream, part);
            }
          });
      if (it.hasNext()) {
        writers.add(it.next());
      }
    }
    return new Writer.Nested(writers);
  }

  public void preNonEmptyList(int size) {}

  public Writer onNonEmptyList(final List<Writer> values) {
    List<Writer> writers = new ArrayList<Writer>(values.size() + 1);
    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, values.size() + 2);
            this.writeLong(stream, Label.LIST);
            this.writeNull(stream);
          }
        });

    for (Writer value : values) {
      writers.add(value);
    }
    return new Writer.Nested(writers);
  }

  public Writer onEmptyList(Expr typeExpr, Writer type) {
    List<Writer> writers = new ArrayList<Writer>(2);
    final Expr listElementType = Expr.Util.getListArg(typeExpr);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 2);
            if (listElementType != null) {
              this.writeLong(stream, Label.LIST);
            } else {
              this.writeLong(stream, Label.EMPTY_LIST_WITH_ABSTRACT_TYPE);
            }
          }
        });

    if (listElementType != null) {
      // We have to recurse explicitly.
      writers.add(listElementType.acceptVis(Encode.instance));
    } else {
      writers.add(type);
    }

    return new Writer.Nested(writers);
  }

  public void preRecord(int size) {}

  public Writer onRecord(final List<Entry<String, Writer>> fields) {

    List<Writer> writers = new ArrayList<Writer>(fields.size() * 2 + 1);
    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 2);
            this.writeLong(stream, Label.RECORD_LITERAL);
            this.writeMapStart(stream, fields.size());
          }
        });

    for (final Entry<String, Writer> field : sortFields(fields)) {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeString(stream, field.getKey());
            }
          });
      writers.add(field.getValue());
    }
    return new Writer.Nested(writers);
  }

  public void preRecordType(int size) {}

  public Writer onRecordType(final List<Entry<String, Writer>> fields) {

    List<Writer> writers = new ArrayList<Writer>(fields.size() * 2 + 1);
    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 2);
            this.writeLong(stream, Label.RECORD_TYPE);
            this.writeMapStart(stream, fields.size());
          }
        });

    for (final Entry<String, Writer> field : sortFields(fields)) {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeString(stream, field.getKey());
            }
          });
      writers.add(field.getValue());
    }
    return new Writer.Nested(writers);
  }

  public void preUnionType(int size) {}

  public Writer onUnionType(final List<Entry<String, Writer>> fields) {
    List<Writer> writers = new ArrayList<Writer>(fields.size() * 2 + 1);
    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 2);
            this.writeLong(stream, Label.UNION_TYPE);
            this.writeMapStart(stream, fields.size());
          }
        });

    for (final Entry<String, Writer> field : sortFields(fields)) {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeString(stream, field.getKey());
            }
          });
      Writer value = field.getValue();
      if (value != null) {
        writers.add(field.getValue());
      } else {
        writers.add(
            new Writer() {
              public void writeToStream(OutputStream stream) throws IOException {
                this.writeNull(stream);
              }
            });
      }
    }
    return new Writer.Nested(writers);
  }

  public Writer onFieldAccess(Writer base, final String fieldName) {
    List<Writer> writers = new ArrayList<Writer>(3);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 3);
            this.writeLong(stream, Label.FIELD_ACCESS);
          }
        });
    writers.add(base);
    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeString(stream, fieldName);
          }
        });
    return new Writer.Nested(writers);
  }

  public Writer onProjection(Writer base, final String[] fieldNames) {
    List<Writer> writers = new ArrayList<Writer>(3);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, fieldNames.length + 2);
            this.writeLong(stream, Label.PROJECTION);
          }
        });
    writers.add(base);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            for (String fieldName : fieldNames) {
              this.writeString(stream, fieldName);
            }
          }
        });
    return new Writer.Nested(writers);
  }

  public Writer onProjectionByType(Writer base, Writer type) {

    List<Writer> writers = new ArrayList<Writer>(4);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 3);
            this.writeLong(stream, Label.PROJECTION);
          }
        });
    writers.add(base);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 1);
          }
        });
    writers.add(type);
    return new Writer.Nested(writers);
  }

  public void preApplication(int size) {}

  public Writer onApplication(Expr baseExpr, Writer base, final List<Writer> args) {
    List<Writer> writers = new ArrayList<Writer>(args.size() + 1);

    String asBuiltIn = baseExpr.asBuiltIn();

    if (asBuiltIn != null && asBuiltIn.equals("Some")) {
      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeArrayStart(stream, 3);
              this.writeLong(stream, Label.SOME);
              this.writeNull(stream);
            }
          });
      writers.add(args.get(0));

    } else {

      writers.add(
          new Writer() {
            public void writeToStream(OutputStream stream) throws IOException {
              this.writeArrayStart(stream, args.size() + 2);
              this.writeLong(stream, Label.APPLICATION);
            }
          });

      writers.add(base);

      for (Writer arg : args) {
        writers.add(arg);
      }
    }
    return new Writer.Nested(writers);
  }

  public Writer onOperatorApplication(final Operator operator, Writer lhs, Writer rhs) {

    List<Writer> writers = new ArrayList<Writer>(3);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 4);
            this.writeLong(stream, Label.OPERATOR_APPLICATION);
            this.writeLong(stream, operator.getLabel());
          }
        });
    writers.add(lhs);
    writers.add(rhs);
    return new Writer.Nested(writers);
  }

  public Writer onIf(Writer predicate, Writer thenValue, Writer elseValue) {

    List<Writer> writers = new ArrayList<Writer>(4);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 4);
            this.writeLong(stream, Label.IF);
          }
        });
    writers.add(predicate);
    writers.add(thenValue);
    writers.add(elseValue);
    return new Writer.Nested(writers);
  }

  public Writer onAnnotated(Writer base, Writer type) {

    List<Writer> writers = new ArrayList<Writer>(3);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 3);
            this.writeLong(stream, Label.ANNOTATION);
          }
        });
    writers.add(base);
    writers.add(type);
    return new Writer.Nested(writers);
  }

  public Writer onAssert(Writer base) {
    List<Writer> writers = new ArrayList<Writer>(2);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, 2);
            this.writeLong(stream, Label.ASSERT);
          }
        });
    writers.add(base);
    return new Writer.Nested(writers);
  }

  public Writer onMerge(Writer handlers, Writer union, final Writer type) {
    List<Writer> writers = new ArrayList<Writer>(4);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, (type != null) ? 4 : 3);
            this.writeLong(stream, Label.MERGE);
          }
        });
    writers.add(handlers);
    writers.add(union);
    if (type != null) {
      writers.add(type);
    }
    return new Writer.Nested(writers);
  }

  public Writer onToMap(Writer base, final Writer type) {
    List<Writer> writers = new ArrayList<Writer>(4);

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, (type != null) ? 3 : 2);
            this.writeLong(stream, Label.TO_MAP);
          }
        });
    writers.add(base);
    if (type != null) {
      writers.add(type);
    }
    return new Writer.Nested(writers);
  }

  private final int modeLabel(Import.Mode mode) {
    if (mode.equals(Import.Mode.RAW_TEXT)) {
      return 1;
    } else if (mode.equals(Import.Mode.LOCATION)) {
      return 2;
    } else {
      return 0;
    }
  }

  private static byte[] multihash(byte[] hash) {
    byte[] bytes = new byte[34];
    // The label for SHA-256.
    bytes[0] = 18;
    // The digest size.
    bytes[1] = 32;
    System.arraycopy(hash, 0, bytes, 2, 32);
    return bytes;
  }

  public Writer onMissingImport(final Import.Mode mode, final byte[] hash) {
    return new Writer() {
      public void writeToStream(OutputStream stream) throws IOException {
        this.writeArrayStart(stream, 4);
        this.writeLong(stream, Label.IMPORT);
        if (hash == null) {
          this.writeNull(stream);
        } else {
          this.writeByteString(stream, hash);
        }
        this.writeLong(stream, modeLabel(mode));
        this.writeLong(stream, Label.IMPORT_TYPE_MISSING);
      }
    };
  }

  public Writer onEnvImport(final String value, final Import.Mode mode, final byte[] hash) {
    return new Writer() {
      public void writeToStream(OutputStream stream) throws IOException {
        this.writeArrayStart(stream, 5);
        this.writeLong(stream, Label.IMPORT);
        if (hash == null) {
          this.writeNull(stream);
        } else {
          this.writeByteString(stream, hash);
        }
        this.writeLong(stream, modeLabel(mode));
        this.writeLong(stream, Label.IMPORT_TYPE_ENV);
        this.writeString(stream, value);
      }
    };
  }

  private static int pathLabel(Path path) {
    if (path.isAbsolute()) {
      return Label.IMPORT_TYPE_LOCAL_ABSOLUTE;
    } else {
      String first = path.iterator().next().toString();
      if (first.equals(".")) {
        return Label.IMPORT_TYPE_LOCAL_HERE;
      } else if (first.equals("..")) {
        return Label.IMPORT_TYPE_LOCAL_PARENT;
      } else if (first.equals("~")) {
        return Label.IMPORT_TYPE_LOCAL_HOME;
      }
    }
    return -1;
  }

  public Writer onLocalImport(final Path path, final Import.Mode mode, final byte[] hash) {
    return new Writer() {
      public void writeToStream(OutputStream stream) throws IOException {
        int size = 4 + path.getNameCount() - (path.isAbsolute() ? 0 : 1);
        this.writeArrayStart(stream, size);
        this.writeLong(stream, Label.IMPORT);
        if (hash == null) {
          this.writeNull(stream);
        } else {
          this.writeByteString(stream, hash);
        }
        this.writeLong(stream, modeLabel(mode));
        this.writeLong(stream, pathLabel(path));

        Iterator<Path> parts = path.iterator();
        if (!path.isAbsolute()) {
          parts.next();
        }
        while (parts.hasNext()) {
          this.writeString(stream, parts.next().toString());
        }
      }
    };
  }

  private static int urlLabel(URI url) {
    if (url.getScheme().equals("https")) {
      return Label.IMPORT_TYPE_REMOTE_HTTPS;
    } else if (url.getScheme().equals("http")) {
      return Label.IMPORT_TYPE_REMOTE_HTTP;
    } else {
      return -1;
    }
  }

  public Writer onRemoteImport(
      final URI url, Writer using, final Import.Mode mode, final byte[] hash) {
    List<Writer> writers = new ArrayList<Writer>(3);

    final List<String> parts = new ArrayList<String>();
    // TODO: verify that we don't need the raw versions here (currently escaped octets are decoded).
    parts.add(url.getAuthority());

    String[] pathParts = url.getPath().split("/");

    if (pathParts.length == 1) {
      parts.add("");
    } else {
      for (int i = 1; i < pathParts.length; i++) {
        parts.add(pathParts[i]);
      }
    }

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            this.writeArrayStart(stream, parts.size() + 6);
            this.writeLong(stream, Label.IMPORT);
            if (hash == null) {
              this.writeNull(stream);
            } else {
              this.writeByteString(stream, hash);
            }
            this.writeLong(stream, modeLabel(mode));
            this.writeLong(stream, urlLabel(url));
          }
        });

    if (using != null) {
      writers.add(using);
    }

    writers.add(
        new Writer() {
          public void writeToStream(OutputStream stream) throws IOException {
            for (String part : parts) {
              this.writeString(stream, part);
            }

            if (url.getQuery() == null) {
              this.writeNull(stream);
            } else {
              this.writeString(stream, url.getQuery());
            }
          }
        });

    return new Writer.Nested(writers);
  }

  private static List<Entry<String, Writer>> sortFields(List<Entry<String, Writer>> fields) {
    List<Entry<String, Writer>> result = new ArrayList(fields.size());

    for (Entry<String, Writer> entry : fields) {
      result.add(entry);
    }

    Collections.sort(result, entryComparator);
    return result;
  }

  public static final Comparator<Entry<String, Writer>> entryComparator =
      new Comparator<Entry<String, Writer>>() {
        public int compare(Entry<String, Writer> a, Entry<String, Writer> b) {
          return a.getKey().compareTo(b.getKey());
        }
      };
}
