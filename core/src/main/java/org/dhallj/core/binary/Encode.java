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
import org.dhallj.core.Visitor;

public final class Encode implements Visitor<Writer> {
  public static final Visitor<Writer> instance = new Encode();

  public Writer onNote(Writer base, Source source) {
    return base;
  }

  private static final class BigIntegerWriter extends Writer {
    private final BigInteger value;
    private final long label;

    BigIntegerWriter(BigInteger value, boolean isNatural) {
      this.value = value;
      this.label = isNatural ? Label.NATURAL : Label.INTEGER;
    }

    public void writeToStream(OutputStream stream) throws IOException {
      this.writeArrayStart(stream, 2);
      this.writeLong(stream, this.label);
      this.writeBigInteger(stream, this.value);
    }
  }

  public Writer onNatural(Expr self, BigInteger value) {
    return new BigIntegerWriter(value, true);
  }

  public Writer onInteger(Expr self, BigInteger value) {
    return new BigIntegerWriter(value, false);
  }

  private static final class DoubleWriter extends Writer {
    private final double value;

    DoubleWriter(double value) {
      this.value = value;
    }

    public void writeToStream(OutputStream stream) throws IOException {
      this.writeDouble(stream, value);
    }
  }

  public Writer onDouble(Expr self, double value) {
    return new DoubleWriter(value);
  }

  private static final class BuiltInWriter extends Writer {
    private final String name;

    BuiltInWriter(String name) {
      this.name = name;
    }

    public void writeToStream(OutputStream stream) throws IOException {
      if (this.name.equals("True")) {
        this.writeBoolean(stream, true);
      } else if (this.name.equals("False")) {
        this.writeBoolean(stream, false);
      } else {
        this.writeString(stream, this.name);
      }
    }
  }

  public Writer onBuiltIn(Expr self, String name) {
    return new BuiltInWriter(name);
  }

  private static final class IdentifierWriter extends Writer {
    private final String name;
    private final long index;

    IdentifierWriter(String name, long index) {
      this.name = name;
      this.index = index;
    }

    public void writeToStream(OutputStream stream) throws IOException {
      if (this.name.equals("_")) {
        this.writeLong(stream, this.index);
      } else {
        this.writeArrayStart(stream, 2);
        this.writeString(stream, this.name);
        this.writeLong(stream, this.index);
      }
    }
  }

  public Writer onIdentifier(Expr self, final String name, final long index) {
    return new IdentifierWriter(name, index);
  }

  public void bind(String param, Expr type) {}

  private static final class FunctionWriter extends Writer {
    private final String name;
    private final long label;

    FunctionWriter(String name, boolean isLambda) {
      this.name = name;
      this.label = isLambda ? Label.LAMBDA : Label.PI;
    }

    public void writeToStream(OutputStream stream) throws IOException {
      if (this.name.equals("_")) {
        this.writeArrayStart(stream, 3);
        this.writeLong(stream, this.label);
      } else {
        this.writeArrayStart(stream, 4);
        this.writeLong(stream, this.label);
        this.writeString(stream, this.name);
      }
    }
  }

  public Writer onLambda(final String name, Writer type, Writer result) {
    List<Writer> writers = new ArrayList<Writer>(3);

    writers.add(new FunctionWriter(name, true));
    writers.add(type);
    writers.add(result);

    return new Writer.Nested(writers);
  }

  public Writer onPi(final String name, Writer type, Writer result) {
    List<Writer> writers = new ArrayList<Writer>(3);

    writers.add(new FunctionWriter(name, false));
    writers.add(type);
    writers.add(result);

    return new Writer.Nested(writers);
  }

  private static final class SizeAndLabelWriter extends Writer {
    private final int size;
    private final long label;
    private final boolean addNull;
    private final int mapSize;

    private SizeAndLabelWriter(int size, long label, boolean addNull, int mapSize) {
      this.size = size;
      this.label = label;
      this.addNull = addNull;
      this.mapSize = mapSize;
    }

    SizeAndLabelWriter(int size, long label, boolean addNull) {
      this(size, label, addNull, -1);
    }

    SizeAndLabelWriter(int size, long label, int mapSize) {
      this(size, label, false, mapSize);
    }

    SizeAndLabelWriter(int size, long label) {
      this(size, label, false, -1);
    }

    public void writeToStream(OutputStream stream) throws IOException {
      this.writeArrayStart(stream, size);
      this.writeLong(stream, label);
      if (this.mapSize > -1) {
        this.writeMapStart(stream, mapSize);
      } else if (this.addNull) {
        this.writeNull(stream);
      }
    }
  }

  public Writer onLet(final List<LetBinding<Writer>> bindings, Writer body) {
    List<Writer> writers = new ArrayList<Writer>();

    writers.add(new SizeAndLabelWriter(2 + bindings.size() * 3, Label.LET));

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

  private static final String unescapeText(String input) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < input.length(); i += 1) {
      if (input.charAt(i) == '\\') {
        i += 1;
        char next = input.charAt(i);

        if (next == '"') {
          builder.append('"');
        } else if (next == '\\') {
          builder.append('\\');
        } else if (next == 'b') {
          builder.append('\b');
        } else if (next == 'f') {
          builder.append('\f');
        } else if (next == 'n') {
          builder.append('\n');
        } else if (next == 'r') {
          builder.append('\r');
        } else if (next == 't') {
          builder.append('\t');
        } else {
          builder.append('\\');
          builder.append(next);
        }
      } else {
        builder.append(input.charAt(i));
      }
    }

    return builder.toString();
  }

  private static final class StringWriter extends Writer {
    private final String value;

    StringWriter(String value) {
      this.value = value;
    }

    public void writeToStream(OutputStream stream) throws IOException {
      this.writeString(stream, this.value);
    }
  }

  public Writer onText(final String[] parts, List<Writer> interpolated) {
    List<Writer> writers = new ArrayList<Writer>(parts.length + 1);

    writers.add(new SizeAndLabelWriter(parts.length * 2, Label.TEXT));

    Iterator<Writer> it = interpolated.iterator();
    for (final String part : parts) {
      writers.add(new StringWriter(unescapeText(part)));
      if (it.hasNext()) {
        writers.add(it.next());
      }
    }
    return new Writer.Nested(writers);
  }

  public Writer onNonEmptyList(final List<Writer> values) {
    List<Writer> writers = new ArrayList<Writer>(values.size() + 1);

    writers.add(new SizeAndLabelWriter(values.size() + 2, Label.LIST, true));
    writers.addAll(values);

    return new Writer.Nested(writers);
  }

  public Writer onEmptyList(Expr typeExpr, Writer type) {
    List<Writer> writers = new ArrayList<Writer>(2);
    final Expr listElementType = Expr.Util.getListArg(typeExpr);

    writers.add(
        new SizeAndLabelWriter(
            2, (listElementType != null) ? Label.LIST : Label.EMPTY_LIST_WITH_ABSTRACT_TYPE));

    if (listElementType != null) {
      // We have to recurse explicitly.
      writers.add(listElementType.accept(Encode.instance));
    } else {
      writers.add(type);
    }

    return new Writer.Nested(writers);
  }

  public Writer onRecord(final List<Entry<String, Writer>> fields) {

    List<Writer> writers = new ArrayList<Writer>(fields.size() * 2 + 1);
    writers.add(new SizeAndLabelWriter(2, Label.RECORD_LITERAL, fields.size()));

    for (final Entry<String, Writer> field : sortFields(fields)) {
      writers.add(new StringWriter(field.getKey()));
      writers.add(field.getValue());
    }
    return new Writer.Nested(writers);
  }

  public Writer onRecordType(final List<Entry<String, Writer>> fields) {

    List<Writer> writers = new ArrayList<Writer>(fields.size() * 2 + 1);
    writers.add(new SizeAndLabelWriter(2, Label.RECORD_TYPE, fields.size()));

    for (final Entry<String, Writer> field : sortFields(fields)) {
      writers.add(new StringWriter(field.getKey()));
      writers.add(field.getValue());
    }
    return new Writer.Nested(writers);
  }

  public Writer onUnionType(final List<Entry<String, Writer>> fields) {
    List<Writer> writers = new ArrayList<Writer>(fields.size() * 2 + 1);
    writers.add(new SizeAndLabelWriter(2, Label.UNION_TYPE, fields.size()));

    for (final Entry<String, Writer> field : sortFields(fields)) {
      writers.add(new StringWriter(field.getKey()));
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

  private static final Writer fieldAccessHeaderWriter =
      new SizeAndLabelWriter(3, Label.FIELD_ACCESS);

  public Writer onFieldAccess(Writer base, final String fieldName) {
    List<Writer> writers = new ArrayList<Writer>(3);

    writers.add(fieldAccessHeaderWriter);
    writers.add(base);
    writers.add(new StringWriter(fieldName));
    return new Writer.Nested(writers);
  }

  public Writer onProjection(Writer base, final String[] fieldNames) {
    List<Writer> writers = new ArrayList<Writer>(3);

    writers.add(new SizeAndLabelWriter(fieldNames.length + 2, Label.PROJECTION));
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

  private static final Writer projectionByTypeHeaderWriter =
      new SizeAndLabelWriter(3, Label.PROJECTION);

  public Writer onProjectionByType(Writer base, Writer type) {

    List<Writer> writers = new ArrayList<Writer>(4);

    writers.add(projectionByTypeHeaderWriter);
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

  public Writer onApplication(Expr baseExpr, Writer base, final List<Writer> args) {
    List<Writer> writers = new ArrayList<Writer>(args.size() + 1);

    String asBuiltIn = Expr.Util.asBuiltIn(baseExpr);

    if (asBuiltIn != null && asBuiltIn.equals("Some")) {
      writers.add(new SizeAndLabelWriter(3, Label.SOME, true));
      writers.add(args.get(0));

    } else {
      writers.add(new SizeAndLabelWriter(args.size() + 2, Label.APPLICATION));
      writers.add(base);
      writers.addAll(args);
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

  private static final Writer ifHeaderWriter = new SizeAndLabelWriter(4, Label.IF);

  public Writer onIf(Writer predicate, Writer thenValue, Writer elseValue) {
    List<Writer> writers = new ArrayList<Writer>(4);
    writers.add(ifHeaderWriter);
    writers.add(predicate);
    writers.add(thenValue);
    writers.add(elseValue);
    return new Writer.Nested(writers);
  }

  private static final Writer annotatedHeaderWriter = new SizeAndLabelWriter(3, Label.ANNOTATED);

  public Writer onAnnotated(Writer base, Writer type) {
    List<Writer> writers = new ArrayList<Writer>(3);
    writers.add(annotatedHeaderWriter);
    writers.add(base);
    writers.add(type);
    return new Writer.Nested(writers);
  }

  private static final Writer assertHeaderWriter = new SizeAndLabelWriter(2, Label.ASSERT);

  public Writer onAssert(Writer base) {
    List<Writer> writers = new ArrayList<Writer>(2);

    writers.add(assertHeaderWriter);
    writers.add(base);
    return new Writer.Nested(writers);
  }

  public Writer onMerge(Writer handlers, Writer union, final Writer type) {
    List<Writer> writers = new ArrayList<Writer>(4);

    writers.add(new SizeAndLabelWriter((type == null) ? 3 : 4, Label.MERGE));
    writers.add(handlers);
    writers.add(union);
    if (type != null) {
      writers.add(type);
    }
    return new Writer.Nested(writers);
  }

  public Writer onToMap(Writer base, final Writer type) {
    List<Writer> writers = new ArrayList<Writer>(4);

    writers.add(new SizeAndLabelWriter((type == null) ? 2 : 3, Label.TO_MAP));
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
          this.writeByteString(stream, multihash(hash));
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
          this.writeByteString(stream, multihash(hash));
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
          this.writeByteString(stream, multihash(hash));
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
      final URI url, final Writer using, final Import.Mode mode, final byte[] hash) {
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
              this.writeByteString(stream, multihash(hash));
            }
            this.writeLong(stream, modeLabel(mode));
            this.writeLong(stream, urlLabel(url));

            if (using == null) {
              this.writeNull(stream);
            }
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

  /** Java 8 introduce {@code comparingByKey}, but we can roll our own pretty easily. */
  private static final Comparator<Entry<String, Writer>> entryComparator =
      new Comparator<Entry<String, Writer>>() {
        public int compare(Entry<String, Writer> a, Entry<String, Writer> b) {
          return a.getKey().compareTo(b.getKey());
        }
      };
}
