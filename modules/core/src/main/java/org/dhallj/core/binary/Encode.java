package org.dhallj.core.binary;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.cbor.Writer;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Visitor;

public final class Encode implements Visitor<Void> {
  private final Writer writer;

  public Encode(Writer writer) {
    this.writer = writer;
  }

  public boolean sortFields() {
    return true;
  }

  public boolean flattenToMapLists() {
    return false;
  }

  public Void onNote(Void base, Source source) {
    return base;
  }

  public Void onNatural(Expr self, BigInteger value) {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(Label.NATURAL);
    this.writer.writeBigInteger(value);
    return null;
  }

  public Void onInteger(Expr self, BigInteger value) {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(Label.INTEGER);
    this.writer.writeBigInteger(value);
    return null;
  }

  public Void onDouble(Expr self, double value) {
    this.writer.writeDouble(value);
    return null;
  }

  public Void onBuiltIn(Expr self, String name) {
    if (name.equals("True")) {
      this.writer.writeBoolean(true);
    } else if (name.equals("False")) {
      this.writer.writeBoolean(false);
    } else {
      this.writer.writeString(name);
    }
    return null;
  }

  public Void onIdentifier(Expr self, String name, long index) {
    if (name.equals("_")) {
      this.writer.writeLong(index);
    } else {
      this.writer.writeArrayStart(2);
      this.writer.writeString(name);
      this.writer.writeLong(index);
    }
    return null;
  }

  public void bind(String param, Expr type) {}

  public boolean prepareLambda(String name, Expr type) {
    if (name.equals("_")) {
      this.writer.writeArrayStart(3);
      this.writer.writeLong(Label.LAMBDA);
    } else {
      this.writer.writeArrayStart(4);
      this.writer.writeLong(Label.LAMBDA);
      this.writer.writeString(name);
    }
    return true;
  }

  public Void onLambda(String name, Void type, Void result) {
    return null;
  }

  public boolean preparePi(String name, Expr type) {
    if (name.equals("_")) {
      this.writer.writeArrayStart(3);
      this.writer.writeLong(Label.PI);
    } else {
      this.writer.writeArrayStart(4);
      this.writer.writeLong(Label.PI);
      this.writer.writeString(name);
    }
    return true;
  }

  public Void onPi(String name, Void type, Void result) {
    return null;
  }

  public boolean prepareLet(int size) {
    this.writer.writeArrayStart(2 + size * 3);
    this.writer.writeLong(Label.LET);
    return true;
  }

  public boolean prepareLetBinding(String name, Expr type) {
    this.writer.writeString(name);
    if (type == null) {
      this.writer.writeNull();
    }
    return true;
  }

  public Void onLet(List<Expr.LetBinding<Void>> bindings, Void body) {
    return null;
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

  public boolean prepareText(int size) {
    this.writer.writeArrayStart(size * 2);
    this.writer.writeLong(Label.TEXT);
    return true;
  }

  public boolean prepareTextPart(String part) {
    this.writer.writeString(unescapeText(part));
    return true;
  }

  public Void onText(String[] parts, List<Void> interpolated) {
    return null;
  }

  public boolean prepareNonEmptyList(int size) {
    this.writer.writeArrayStart(size + 2);
    this.writer.writeLong(Label.LIST);
    this.writer.writeNull();
    return true;
  }

  public boolean prepareNonEmptyListElement(int index) {
    return true;
  }

  public Void onNonEmptyList(final List<Void> values) {
    return null;
  }

  public boolean prepareEmptyList(Expr type) {
    final Expr listElementType = Expr.Util.getListArg(type);

    this.writer.writeArrayStart(2);
    this.writer.writeLong(
        (listElementType != null) ? Label.LIST : Label.EMPTY_LIST_WITH_ABSTRACT_TYPE);

    if (listElementType != null) {
      // We have to recurse explicitly.
      listElementType.accept(this);
      return false;
    } else {
      return true;
    }
  }

  public Void onEmptyList(Void type) {
    return null;
  }

  public boolean prepareRecord(int size) {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(Label.RECORD_LITERAL);
    this.writer.writeMapStart(size);
    return true;
  }

  public boolean prepareRecordField(String name, Expr type, int index) {
    this.writer.writeString(name);
    return true;
  }

  public Void onRecord(final List<Entry<String, Void>> fields) {
    return null;
  }

  public boolean prepareRecordType(int size) {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(Label.RECORD_TYPE);
    this.writer.writeMapStart(size);
    return true;
  }

  public boolean prepareRecordTypeField(String name, Expr type, int index) {
    this.writer.writeString(name);
    return true;
  }

  public Void onRecordType(final List<Entry<String, Void>> fields) {
    return null;
  }

  public boolean prepareUnionType(int size) {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(Label.UNION_TYPE);
    this.writer.writeMapStart(size);
    return true;
  }

  public boolean prepareUnionTypeField(String name, Expr type, int index) {
    this.writer.writeString(name);
    if (type == null) {
      this.writer.writeNull();
    }
    return true;
  }

  public Void onUnionType(final List<Entry<String, Void>> fields) {
    return null;
  }

  public boolean prepareFieldAccess(Expr base, String fieldName) {
    this.writer.writeArrayStart(3);
    this.writer.writeLong(Label.FIELD_ACCESS);
    return true;
  }

  public Void onFieldAccess(Void base, final String fieldName) {
    this.writer.writeString(fieldName);
    return null;
  }

  public boolean prepareProjection(int size) {
    this.writer.writeArrayStart(size + 2);
    this.writer.writeLong(Label.PROJECTION);
    return true;
  }

  public Void onProjection(Void base, final String[] fieldNames) {
    for (String fieldName : fieldNames) {
      this.writer.writeString(fieldName);
    }
    return null;
  }

  public boolean prepareProjectionByType() {
    this.writer.writeArrayStart(3);
    this.writer.writeLong(Label.PROJECTION);
    return true;
  }

  public boolean prepareProjectionByType(Expr type) {
    this.writer.writeArrayStart(1);
    return true;
  }

  public Void onProjectionByType(Void base, Void type) {
    return null;
  }

  public boolean prepareApplication(Expr base, int size) {
    String asBuiltIn = Expr.Util.asBuiltIn(base);

    if (asBuiltIn != null && asBuiltIn.equals("Some")) {
      this.writer.writeArrayStart(3);
      this.writer.writeLong(Label.SOME);
      this.writer.writeNull();
      return false;
    } else {
      this.writer.writeArrayStart(size + 2);
      this.writer.writeLong(Label.APPLICATION);
      return true;
    }
  }

  public Void onApplication(Void base, final List<Void> args) {
    return null;
  }

  public boolean prepareOperatorApplication(final Operator operator) {
    this.writer.writeArrayStart(4);
    this.writer.writeLong(Label.OPERATOR_APPLICATION);
    this.writer.writeLong(operator.getLabel());
    return true;
  }

  public Void onOperatorApplication(Operator operator, Void lhs, Void rhs) {
    return null;
  }

  public boolean prepareIf() {
    this.writer.writeArrayStart(4);
    this.writer.writeLong(Label.IF);
    return true;
  }

  public Void onIf(Void predicate, Void thenValue, Void elseValue) {
    return null;
  }

  public boolean prepareAnnotated(Expr type) {
    this.writer.writeArrayStart(3);
    this.writer.writeLong(Label.ANNOTATED);
    return true;
  }

  public Void onAnnotated(Void base, Void type) {
    return null;
  }

  public boolean prepareAssert() {
    this.writer.writeArrayStart(2);
    this.writer.writeLong(Label.ASSERT);
    return true;
  }

  public Void onAssert(Void base) {
    return null;
  }

  public boolean prepareMerge(Expr type) {
    this.writer.writeArrayStart((type == null) ? 3 : 4);
    this.writer.writeLong(Label.MERGE);
    return true;
  }

  public Void onMerge(Void handlers, Void union, Void type) {
    return null;
  }

  public boolean prepareToMap(Expr type) {
    this.writer.writeArrayStart((type == null) ? 2 : 3);
    this.writer.writeLong(Label.TO_MAP);
    return true;
  }

  public Void onToMap(Void base, Void type) {
    return null;
  }

  private final int modeLabel(Expr.ImportMode mode) {
    if (mode.equals(Expr.ImportMode.RAW_TEXT)) {
      return 1;
    } else if (mode.equals(Expr.ImportMode.LOCATION)) {
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

  public Void onMissingImport(final Expr.ImportMode mode, final byte[] hash) {
    this.writer.writeArrayStart(4);
    this.writer.writeLong(Label.IMPORT);
    if (hash == null) {
      this.writer.writeNull();
    } else {
      this.writer.writeByteString(multihash(hash));
    }
    this.writer.writeLong(modeLabel(mode));
    this.writer.writeLong(Label.IMPORT_TYPE_MISSING);
    return null;
  }

  public Void onEnvImport(final String value, final Expr.ImportMode mode, final byte[] hash) {
    this.writer.writeArrayStart(5);
    this.writer.writeLong(Label.IMPORT);
    if (hash == null) {
      this.writer.writeNull();
    } else {
      this.writer.writeByteString(multihash(hash));
    }
    this.writer.writeLong(modeLabel(mode));
    this.writer.writeLong(Label.IMPORT_TYPE_ENV);
    this.writer.writeString(value);
    return null;
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

  public Void onLocalImport(final Path path, final Expr.ImportMode mode, final byte[] hash) {
    int size = 4 + path.getNameCount() - (path.isAbsolute() ? 0 : 1);
    this.writer.writeArrayStart(size);
    this.writer.writeLong(Label.IMPORT);
    if (hash == null) {
      this.writer.writeNull();
    } else {
      this.writer.writeByteString(multihash(hash));
    }
    this.writer.writeLong(modeLabel(mode));
    this.writer.writeLong(pathLabel(path));

    Iterator<Path> parts = path.iterator();
    if (!path.isAbsolute()) {
      parts.next();
    }
    while (parts.hasNext()) {
      this.writer.writeString(parts.next().toString());
    }
    return null;
  }

  @Override
  public Void onClasspathImport(Path path, Expr.ImportMode mode, byte[] hash) {
    int size = 4 + path.getNameCount();
    this.writer.writeArrayStart(size);
    this.writer.writeLong(Label.IMPORT);
    if (hash == null) {
      this.writer.writeNull();
    } else {
      this.writer.writeByteString(multihash(hash));
    }
    this.writer.writeLong(modeLabel(mode));
    this.writer.writeLong(Label.IMPORT_TYPE_CLASSPATH);

    Iterator<Path> parts = path.iterator();
    while (parts.hasNext()) {
      this.writer.writeString(parts.next().toString());
    }
    return null;
  }

  private static final int urlLabel(URI url) {
    if (url.getScheme().equals("https")) {
      return Label.IMPORT_TYPE_REMOTE_HTTPS;
    } else if (url.getScheme().equals("http")) {
      return Label.IMPORT_TYPE_REMOTE_HTTP;
    } else {
      return -1;
    }
  }

  private static final List<String> getUrlPathParts(URI url) {
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

    return parts;
  }

  public boolean prepareRemoteImport(URI url, Expr using, Expr.ImportMode mode, byte[] hash) {
    final List<String> parts = getUrlPathParts(url);

    this.writer.writeArrayStart(parts.size() + 6);
    this.writer.writeLong(Label.IMPORT);
    if (hash == null) {
      this.writer.writeNull();
    } else {
      this.writer.writeByteString(multihash(hash));
    }
    this.writer.writeLong(modeLabel(mode));
    this.writer.writeLong(urlLabel(url));

    if (using == null) {
      this.writer.writeNull();
    }
    return true;
  }

  public Void onRemoteImport(URI url, Void using, Expr.ImportMode mode, byte[] hash) {

    List<String> parts = getUrlPathParts(url);

    for (String part : parts) {
      this.writer.writeString(part);
    }

    if (url.getQuery() == null) {
      this.writer.writeNull();
    } else {
      this.writer.writeString(url.getQuery());
    }

    return null;
  }
}
