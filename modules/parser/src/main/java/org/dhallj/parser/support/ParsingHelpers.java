package org.dhallj.parser.support;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.dhallj.core.DhallException.ParsingFailure;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.Parser;
import org.dhallj.core.Source;

final class ParsingHelpers {

  private static Source sourceFromToken(Token token) {
    return Source.fromString(
        token.image, token.beginLine, token.beginColumn, token.endLine, token.endColumn);
  }

  static final Expr.Parsed makeDoubleLiteral(Token token) {
    return new Expr.Parsed(
        Expr.makeDoubleLiteral(Double.parseDouble(token.image)), sourceFromToken(token));
  }

  static final Expr.Parsed makeNaturalLiteral(Token token) {
    return new Expr.Parsed(
        Expr.makeNaturalLiteral(Parser.parseBigInteger(token.image)), sourceFromToken(token));
  }

  static final Expr.Parsed makeIntegerLiteral(Token token) {
    return new Expr.Parsed(
        Expr.makeIntegerLiteral(Parser.parseBigInteger(token.image)), sourceFromToken(token));
  }

  private static String unescapeText(String in) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < in.length(); i++) {
      if (in.charAt(i) == '\\') {
        i += 1;
        char next = in.charAt(i);
        if (next == '"' || next == '$' || next == '/') {
          builder.append(next);
        } else if (next == 'u') {
          char escapeFirst = in.charAt(i + 1);

          if (escapeFirst == '{') {
            int len = 0;
            while (in.charAt(i + 2 + len) != '}') {
              len += 1;
            }

            int code = Integer.parseInt(in.substring(i + 2, i + 2 + len), 16);
            builder.appendCodePoint(code);
            i += len + 2;
          } else {
            int code = Integer.parseInt(in.substring(i + 1, i + 5), 16);
            builder.append((char) code);
            i += 4;
          }
        } else {
          builder.append('\\');
          builder.append(next);
        }
      } else {
        builder.append(in.charAt(i));
      }
    }
    return builder.toString();
  }

  static final Expr.Parsed makeTextLiteral(
      List<Entry<String, Expr.Parsed>> chunks, Token first, Token last) {
    // TODO: fix source.
    Source source =
        Source.fromString("", first.beginLine, first.beginColumn, last.endLine, last.endColumn);

    List<String> parts = new ArrayList<>(1);
    List<Expr> interpolated = new ArrayList<>();
    boolean lastWasInterpolated = true;

    for (Entry<String, Expr.Parsed> chunk : chunks) {
      if (chunk.getKey() == null) {
        if (lastWasInterpolated) {
          parts.add("");
        }
        interpolated.add(chunk.getValue());
        lastWasInterpolated = true;
      } else {
        parts.add(unescapeText(chunk.getKey()));
        lastWasInterpolated = false;
      }
    }

    if (interpolated.size() == parts.size()) {
      parts.add("");
    }

    return new Expr.Parsed(
        Expr.makeTextLiteral(parts.toArray(new String[parts.size()]), (List) interpolated), source);
  }

  static final void dedent(String[] input) {
    List<Character> candidate = null;
    String[][] partLines = new String[input.length][];

    for (int i = 0; i < input.length; i += 1) {
      String part = input[i].replace("\r\n", "\n");
      String[] lines = part.split("\n", -1);
      partLines[i] = lines;

      for (int j = (i == 0) ? 0 : 1; j < lines.length; j += 1) {
        String line = lines[j];

        if (line.length() > 0 || j == lines.length - 1) {
          if (candidate == null) {
            candidate = new ArrayList<>();
            for (int k = 0; k < line.length(); k += 1) {
              char c = line.charAt(k);
              if (c == ' ' || c == '\t') {
                candidate.add(c);
              } else {
                break;
              }
            }
          } else {
            for (int k = 0; k < candidate.size(); k += 1) {
              if (k == line.length() || line.charAt(k) != candidate.get(k).charValue()) {
                candidate = candidate.subList(0, k);
                break;
              }
            }
          }
        }
      }
    }

    int stripCount = candidate == null ? 0 : candidate.size();

    if (stripCount == 0) {
      for (int i = 0; i < input.length; i += 1) {
        input[i] = reEscape(input[i]);
      }
    } else {
      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < input.length; i += 1) {
        builder.setLength(0);

        String[] lines = partLines[i];

        for (int j = 0; j < lines.length; j += 1) {
          if (lines[j].length() != 0) {
            if (i > 0 && j == 0) {
              builder.append(lines[j]);
            } else {
              builder.append(lines[j].substring(stripCount));
            }
          }
          if (j < lines.length - 1) {
            builder.append("\n");
          }
        }

        input[i] = reEscape(builder.toString());
      }
    }
  }

  static final String reEscape(String input) {
    return input.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t");
  }

  static final Expr.Parsed makeSingleQuotedTextLiteral(
      List<Entry<String, Expr.Parsed>> chunks, Token first) {
    // TODO: fix source.
    Source source = sourceFromToken(first);

    return new Expr.Parsed(Parser.makeSingleQuotedTextLiteral((List) chunks), source);
  }

  static final Expr.Parsed makeApplication(Expr.Parsed base, Expr.Parsed arg, Token whsp) {
    return new Expr.Parsed(Expr.makeApplication(base, arg), new ESESource(base, whsp.image, arg));
  }

  static final Expr.Parsed makeOperatorApplication(
      Operator operator,
      Expr.Parsed lhs,
      Expr.Parsed rhs,
      String operatorString,
      Token whsp0,
      Token whsp1) {
    StringBuilder builder = new StringBuilder();
    if (whsp0 != null) {
      builder.append(whsp0.image);
    }
    builder.append(operatorString);
    if (whsp1 != null) {
      builder.append(whsp1.image);
    }

    Source source = new ESESource(lhs, builder.toString(), rhs);

    return new Expr.Parsed(Expr.makeOperatorApplication(operator, lhs, rhs), source);
  }

  static final Expr.Parsed makeAnnotated(
      Expr.Parsed base, Expr.Parsed tpe, Token whsp0, Token whsp1) {
    StringBuilder builder = new StringBuilder();
    if (whsp0 != null) {
      builder.append(whsp0.image);
    }
    builder.append(':');
    builder.append(whsp1.image);

    Source source = new ESESource(base, builder.toString(), tpe);

    return new Expr.Parsed(Expr.makeAnnotated(base, tpe), source);
  }

  static final Expr.Parsed makeToMap(
      Expr.Parsed base, Expr.Parsed tpe, Token first, Token whsp0, Token whsp1, Token whsp2) {
    StringBuilder builder = new StringBuilder();
    if (whsp1 != null) {
      builder.append(whsp1.image);
    }
    if (whsp2 != null) {
      builder.append(":");
      builder.append(whsp2.image);
    }
    Source source;

    if (tpe != null) {
      source =
          new SESESource(
              first.image + whsp0.image,
              base,
              builder.toString(),
              tpe,
              first.beginLine,
              first.beginColumn);
    } else {
      source = new SESource(first.image + whsp0.image, base, first.beginLine, first.beginColumn);
    }

    return new Expr.Parsed(Expr.makeToMap(base, tpe), source);
  }

  static final Expr.Parsed makeToMap(Expr.Parsed base, Token first, Token whsp) {
    Source source =
        new SESource(first.image + whsp.image, base, first.beginLine, first.beginColumn);

    return new Expr.Parsed(Expr.makeToMap(base), source);
  }

  static final Expr.Parsed makeMerge(
      Expr.Parsed left,
      Expr.Parsed right,
      Expr.Parsed tpe,
      Token first,
      Token whsp0,
      Token whsp1,
      Token whsp2,
      Token whsp3) {
    Source source;

    if (tpe != null) {
      StringBuilder builder = new StringBuilder();
      if (whsp2 != null) {
        builder.append(whsp2.image);
      }
      builder.append(":");
      builder.append(whsp3.image);
      source =
          new SESESESource(
              first.image + whsp0.image,
              left,
              whsp1.image,
              right,
              builder.toString(),
              tpe,
              first.beginLine,
              first.beginColumn);
    } else {
      source =
          new SESESource(
              first.image + whsp0.image,
              left,
              whsp1.image,
              right,
              first.beginLine,
              first.beginColumn);
    }

    return new Expr.Parsed(Expr.makeMerge(left, right, tpe), source);
  }

  static final Expr.Parsed makeLambda(
      String param, Expr.Parsed input, Expr.Parsed result, Token first) {
    // TODO: text is empty.
    Source source =
        Source.fromString(
            "",
            first.beginLine,
            first.beginColumn,
            result.getSource().getEndLine(),
            result.getSource().getEndColumn());

    return new Expr.Parsed(Expr.makeLambda(param, input, result), source);
  }

  static final Expr.Parsed makePi(
      String param, Expr.Parsed input, Expr.Parsed result, Token first) {
    // TODO: text is empty.
    Source source =
        Source.fromString(
            "",
            first.beginLine,
            first.beginColumn,
            result.getSource().getEndLine(),
            result.getSource().getEndColumn());

    return new Expr.Parsed(Expr.makePi(param, input, result), source);
  }

  static final Expr.Parsed makePi(Expr.Parsed input, Expr.Parsed result) {
    // TODO: text is empty.
    Source source =
        Source.fromString(
            "",
            input.getSource().getBeginLine(),
            input.getSource().getBeginColumn(),
            result.getSource().getEndLine(),
            result.getSource().getEndColumn());

    return new Expr.Parsed(Expr.makePi(input, result), source);
  }

  static final Expr.Parsed makeIf(
      Expr.Parsed cond, Expr.Parsed thenValue, Expr.Parsed elseValue, Token first) {
    // TODO: text is empty.
    Source source =
        Source.fromString(
            "",
            first.beginLine,
            first.beginColumn,
            elseValue.getSource().getEndLine(),
            elseValue.getSource().getEndColumn());

    return new Expr.Parsed(Expr.makeIf(cond, thenValue, elseValue), source);
  }

  static final Expr.Parsed makeLet(List<LetBinding> bindings, Expr.Parsed body, String whsp) {
    Collections.reverse(bindings);
    Expr.Parsed current = body;
    String extraText2 = whsp;

    for (LetBinding binding : bindings) {
      Source source;
      if (binding.type == null) {
        source =
            new SESESource(
                binding.text1,
                binding.value,
                binding.text2 + extraText2,
                current,
                binding.beginLine,
                binding.beginColumn);
      } else {

        source =
            new SESESESource(
                binding.text0,
                binding.type,
                binding.text1,
                binding.value,
                binding.text2 + extraText2,
                current,
                binding.beginLine,
                binding.beginColumn);
      }

      current =
          new Expr.Parsed(Expr.makeLet(binding.name, binding.type, binding.value, current), source);
      extraText2 = "";
    }

    return current;
  }

  static Expr.Parsed makeAssert(Expr.Parsed base, Token first, Token whsp0, Token whsp1) {
    StringBuilder builder = new StringBuilder("assert");
    if (whsp0 != null) {
      builder.append(whsp0.image);
    }
    builder.append(':');
    builder.append(whsp1.image);
    Source source = new SESource(builder.toString(), base, first.beginLine, first.beginColumn);

    return new Expr.Parsed(Expr.makeAssert(base), source);
  }

  static Expr.Parsed makeFieldAccess(
      Expr.Parsed base, String fieldName, Token whsp0, Token whsp1, int endLine, int endColumn) {
    StringBuilder builder = new StringBuilder();
    if (whsp0 != null) {
      builder.append(whsp0.image);
    }
    builder.append('.');
    if (whsp1 != null) {
      builder.append(whsp1.image);
    }
    builder.append(fieldName);
    Source source = new ESSource(base, builder.toString(), endLine, endColumn);

    return new Expr.Parsed(Expr.makeFieldAccess(base, fieldName), source);
  }

  static Expr.Parsed makeProjection(
      Expr.Parsed base, List<String> fieldNames, int endLine, int endColumn) {
    // TODO: text is empty.
    Source source =
        Source.fromString(
            "",
            base.getSource().getBeginLine(),
            base.getSource().getBeginColumn(),
            endLine,
            endColumn);

    return new Expr.Parsed(
        Expr.makeProjection(base, fieldNames.toArray(new String[fieldNames.size()])), source);
  }

  static Expr.Parsed makeProjectionByType(
      Expr.Parsed base, Expr.Parsed tpe, int endLine, int endColumn) {
    // TODO: text is empty.
    Source source =
        Source.fromString(
            "",
            base.getSource().getBeginLine(),
            base.getSource().getBeginColumn(),
            endLine,
            endColumn);

    return new Expr.Parsed(Expr.makeProjectionByType(base, tpe), source);
  }

  private static final boolean isBuiltIn(String input) {
    return input.charAt(0) != '`' && Expr.Constants.isBuiltIn(input);
  }

  private static final String unescapeLabel(String input) {
    return (input.charAt(0) != '`') ? input : input.substring(1, input.length() - 1);
  }

  static final Expr.Parsed makeBuiltInOrIdentifier(Token value) {
    if (isBuiltIn(value.image)) {
      return new Expr.Parsed(Expr.makeBuiltIn(value.image), sourceFromToken(value));
    } else {
      return new Expr.Parsed(
          Expr.makeIdentifier(unescapeLabel(value.image)), sourceFromToken(value));
    }
  }

  static final Expr.Parsed makeIdentifier(Token value, Token whsp0, Token whsp1, Token index) {
    StringBuilder builder = new StringBuilder();
    builder.append(value.image);
    if (whsp0 != null) {
      builder.append(whsp0.image);
    }
    builder.append("@");
    if (whsp1 != null) {
      builder.append(whsp1.image);
    }
    builder.append(index.image);
    Source source =
        Source.fromString(
            builder.toString(), value.beginLine, value.beginColumn, index.endLine, index.endColumn);

    long indexValue = Parser.parseBigInteger(index.image).longValue();

    return new Expr.Parsed(Expr.makeIdentifier(unescapeLabel(value.image), indexValue), source);
  }

  static final Expr.Parsed makeRecordLiteral(
      List<Entry<List<String>, Expr.Parsed>> fields, Token first, Token last) {
    // TODO: Get actual last token in all cases.
    int endLine = (last == null) ? first.endLine : last.endLine;
    int endColumn = (last == null) ? first.endColumn : last.endColumn;

    // TODO: text is empty.
    Source source = Source.fromString("", first.beginLine, first.beginColumn, endLine, endColumn);

    List<Entry<String, Expr>> dedotted = new ArrayList<>(fields.size());

    for (Entry<List<String>, Expr.Parsed> entry : fields) {
      List<String> parts = entry.getKey();
      String firstPart = parts.remove(0);

      Expr maybePunnedValue = entry.getValue();
      Expr value;
      if (maybePunnedValue == null) {
        // Record puns can't be dotted.
        value = Expr.makeIdentifier(firstPart);
      } else {
        value = maybePunnedValue;
      }

      if (parts.isEmpty()) {
        dedotted.add(new SimpleImmutableEntry<>(firstPart, value));
      } else {
        Collections.reverse(parts);
        Expr current = value;

        for (String part : parts) {
          current = Expr.makeRecordLiteral(part, current);
        }
        dedotted.add(new SimpleImmutableEntry<>(firstPart, current));
      }
    }

    List<Entry<String, Expr>> desugared = new ArrayList<>(dedotted.size());
    Set<String> seen = new HashSet();

    for (int i = 0; i < dedotted.size(); i++) {
      Entry<String, Expr> entry = dedotted.get(i);
      String key = entry.getKey();

      if (!seen.contains(key)) {
        Expr current = entry.getValue();

        for (int j = i + 1; j < dedotted.size(); j++) {
          Entry<String, Expr> other = dedotted.get(j);

          if (other.getKey().equals(entry.getKey())) {
            current = Expr.makeOperatorApplication(Operator.COMBINE, current, other.getValue());
          }
        }

        desugared.add(new SimpleImmutableEntry<>(key, current));

        seen.add(key);
      }
    }

    return new Expr.Parsed(Expr.makeRecordLiteral(desugared), source);
  }

  static final Expr.Parsed makeRecordType(
      List<Entry<String, Expr.Parsed>> fields, Token first, Token last) {
    // TODO: Get actual last token in all cases.
    int endLine = (last == null) ? first.endLine : last.endLine;
    int endColumn = (last == null) ? first.endColumn : last.endColumn;

    // TODO: text is empty.
    Source source = Source.fromString("", first.beginLine, first.beginColumn, endLine, endColumn);

    return new Expr.Parsed(Expr.makeRecordType((List) fields), source);
  }

  static final Expr.Parsed makeUnionType(
      List<Entry<String, Expr.Parsed>> fields, Token first, Token last) {
    // TODO: Get actual last token in all cases.
    int endLine = (last == null) ? first.endLine : last.endLine;
    int endColumn = (last == null) ? first.endColumn : last.endColumn;

    // TODO: text is empty.
    Source source = Source.fromString("", first.beginLine, first.beginColumn, endLine, endColumn);

    return new Expr.Parsed(Expr.makeUnionType((List) fields), source);
  }

  static final Expr.Parsed makeWith(Expr base, List<String> path, Expr.Parsed arg, Token first) {
    // TODO: source isn't correct.
    Source source = sourceFromToken(first);

    return new Expr.Parsed(Expr.makeWith(base, path.toArray(new String[path.size()]), arg), source);
  }

  static final Expr.Parsed makeNonEmptyListLiteral(
      List<Expr.Parsed> values, List<String> other, Token first, Token last) {
    Source source =
        new InterspersedSource(
            other, values, first.beginLine, first.beginColumn, last.endLine, last.endColumn);
    return new Expr.Parsed(
        Expr.makeNonEmptyListLiteral(values.toArray(new Expr.Parsed[values.size()])), source);
  }

  static final Expr.Parsed makeEmptyListLiteral(Expr.Parsed tpe, String other, Token first) {
    Source source = new SESource(other, tpe, first.beginLine, first.beginColumn);
    return new Expr.Parsed(Expr.makeEmptyListLiteral(tpe), source);
  }

  static final Expr.Parsed makeParenthesized(Expr.Parsed value, Token first, Token last) {
    Source source =
        new SESSource(
            "(", value, ")", first.beginLine, first.beginColumn, last.endLine, last.endColumn);
    return new Expr.Parsed(value, source);
  }

  static final Expr.Parsed makeImport(
      Token type, Token hashToken, Token modeToken, Expr.Parsed using) {
    // TODO: fix.
    Source source = sourceFromToken(type);
    byte[] hash =
        (hashToken == null) ? null : Expr.Util.decodeHashBytes(hashToken.image.substring(7));
    Expr value = null;
    Expr.ImportMode mode =
        (modeToken == null)
            ? Expr.ImportMode.CODE
            : (modeToken.image.equals("Text")
                ? Expr.ImportMode.RAW_TEXT
                : Expr.ImportMode.LOCATION);

    if (type.image.equals("missing")) {
      value = Expr.makeMissingImport(mode, hash);
    } else if (type.image.startsWith("http")) {
      try {
        value = Expr.makeRemoteImport(new URI(type.image), using, mode, hash);
      } catch (java.net.URISyntaxException e) {
        throw new ParsingFailure("Invalid URL", e);
      }
    } else if (type.image.startsWith("env:")) {
      value = Expr.makeEnvImport(type.image.substring(4), mode, hash);
    } else if (type.image.startsWith("classpath:")) {
      value = Expr.makeClasspathImport(Paths.get(type.image.substring(10)), mode, hash);
    } else {
      try {
        value = Expr.makeLocalImport(Paths.get(type.image), mode, hash);
      } catch (java.nio.file.InvalidPathException e) {
        throw new ParsingFailure("Invalid path", e);
      }
    }

    return new Expr.Parsed(value, source);
  }

  private static final class ESESource extends Source {
    private final Expr.Parsed i0;
    private final String i1;
    private final Expr.Parsed i2;

    ESESource(Expr.Parsed i0, String i1, Expr.Parsed i2) {
      super(
          i0.getSource().getBeginLine(),
          i0.getSource().getBeginColumn(),
          i2.getSource().getEndLine(),
          i2.getSource().getEndColumn());

      this.i0 = i0;
      this.i1 = i1;
      this.i2 = i2;
    }

    public final void printText(StringBuilder builder) {
      this.i0.getSource().printText(builder);
      builder.append(this.i1);
      this.i2.getSource().printText(builder);
    }
  }

  private static final class ESSource extends Source {
    private final Expr.Parsed i0;
    private final String i1;

    ESSource(Expr.Parsed i0, String i1, int endLine, int endColumn) {
      super(i0.getSource().getBeginLine(), i0.getSource().getBeginColumn(), endLine, endColumn);
      this.i0 = i0;
      this.i1 = i1;
    }

    public final void printText(StringBuilder builder) {
      this.i0.getSource().printText(builder);
      builder.append(this.i1);
    }
  }

  private static final class SESource extends Source {
    private final String i0;
    private final Expr.Parsed i1;

    SESource(String i0, Expr.Parsed i1, int beginLine, int beginColumn) {
      super(beginLine, beginColumn, i1.getSource().getEndLine(), i1.getSource().getEndColumn());
      this.i0 = i0;
      this.i1 = i1;
    }

    public final void printText(StringBuilder builder) {
      builder.append(this.i0);
      this.i1.getSource().printText(builder);
    }
  }

  private static final class SESSource extends Source {
    private final String i0;
    private final Expr.Parsed i1;
    private final String i2;

    SESSource(
        String i0,
        Expr.Parsed i1,
        String i2,
        int beginLine,
        int beginColumn,
        int endLine,
        int endColumn) {
      super(beginLine, beginColumn, endLine, endColumn);
      this.i0 = i0;
      this.i1 = i1;
      this.i2 = i2;
    }

    public final void printText(StringBuilder builder) {
      builder.append(this.i0);
      this.i1.getSource().printText(builder);
      builder.append(this.i2);
    }
  }

  private static final class SESESource extends Source {
    private final String i0;
    private final Expr.Parsed i1;
    private final String i2;
    private final Expr.Parsed i3;

    SESESource(
        String i0, Expr.Parsed i1, String i2, Expr.Parsed i3, int beginLine, int beginColumn) {
      super(beginLine, beginColumn, i3.getSource().getEndLine(), i3.getSource().getEndColumn());
      this.i0 = i0;
      this.i1 = i1;
      this.i2 = i2;
      this.i3 = i3;
    }

    public final void printText(StringBuilder builder) {
      builder.append(this.i0);
      this.i1.getSource().printText(builder);
      builder.append(this.i2);
      this.i3.getSource().printText(builder);
    }
  }

  private static final class SESESESource extends Source {
    private final String i0;
    private final Expr.Parsed i1;
    private final String i2;
    private final Expr.Parsed i3;
    private final String i4;
    private final Expr.Parsed i5;

    SESESESource(
        String i0,
        Expr.Parsed i1,
        String i2,
        Expr.Parsed i3,
        String i4,
        Expr.Parsed i5,
        int beginLine,
        int beginColumn) {
      super(beginLine, beginColumn, i5.getSource().getEndLine(), i5.getSource().getEndColumn());
      this.i0 = i0;
      this.i1 = i1;
      this.i2 = i2;
      this.i3 = i3;
      this.i4 = i4;
      this.i5 = i5;
    }

    public final void printText(StringBuilder builder) {
      builder.append(this.i0);
      this.i1.getSource().printText(builder);
      builder.append(this.i2);
      this.i3.getSource().printText(builder);
      builder.append(this.i4);
      this.i5.getSource().printText(builder);
    }
  }

  private static final class InterspersedSource extends Source {
    private final List<String> i0;
    private final List<Expr.Parsed> i1;

    InterspersedSource(
        List<String> i0,
        List<Expr.Parsed> i1,
        int beginLine,
        int beginColumn,
        int endLine,
        int endColumn) {
      super(beginLine, beginColumn, endLine, endColumn);
      this.i0 = i0;
      this.i1 = i1;
    }

    public final void printText(StringBuilder builder) {
      Iterator<String> ii0 = i0.iterator();
      Iterator<Expr.Parsed> ii1 = i1.iterator();

      while (ii0.hasNext() && ii1.hasNext()) {
        builder.append(ii0.next());
        ii1.next().getSource().printText(builder);
      }

      if (ii0.hasNext()) {
        builder.append(ii0.next());
      }
    }
  }
}
