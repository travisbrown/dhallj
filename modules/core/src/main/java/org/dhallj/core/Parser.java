package org.dhallj.core;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

/** Parses text input into Dhall expressions. */
public abstract class Parser {
  public abstract Expr parse(String input);

  public abstract Expr parse(InputStream stream) throws IOException;

  public static final BigInteger parseBigInteger(String input) {
    if (input.startsWith("0x")) {
      return new BigInteger(input.substring(2), 16);
    } else if (input.startsWith("-0x")) {
      return new BigInteger(input.substring(3), 16).negate();
    } else {
      return new BigInteger(input);
    }
  }

  public static final Expr makeTextLiteral(List<Entry<String, Expr>> pairs, String last) {
    int size = pairs.size();

    String[] parts = new String[size + 1];
    Expr[] interpolated = new Expr[size];

    for (int i = 0; i < size; i += 1) {
      Entry<String, Expr> pair = pairs.get(i);
      parts[i] = unescapeText(pair.getKey());
      interpolated[i] = pair.getValue();
    }

    parts[size] = unescapeText(last);

    return Expr.makeTextLiteral(parts, interpolated);
  }

  public static final Expr makeSingleQuotedTextLiteral(
      List<Entry<String, Expr>> pairs, String last) {
    int size = pairs.size();

    String[] parts = new String[size + 1];
    Expr[] interpolated = new Expr[size];

    for (int i = 0; i < size; i += 1) {
      Entry<String, Expr> pair = pairs.get(i);
      parts[i] = unescapeText(pair.getKey());
      interpolated[i] = pair.getValue();
    }

    parts[size] = unescapeText(last);

    dedentLines(parts);
    return Expr.makeTextLiteral(parts, interpolated);
  }

  public static final Expr makeSingleQuotedTextLiteral(List<Entry<String, Expr>> chunks) {
    Collections.reverse(chunks);

    List<String> parts = new ArrayList<>(1);
    List<Expr> interpolated = new ArrayList<>();

    for (Entry<String, Expr> chunk : chunks) {
      if (chunk.getKey() == null) {
        if (parts.isEmpty()) {
          parts.add("");
        }
        interpolated.add(chunk.getValue());
      } else {
        if (parts.size() > interpolated.size()) {
          parts.set(parts.size() - 1, parts.get(parts.size() - 1) + chunk.getKey());
        } else {
          parts.add(chunk.getKey());
        }
      }
    }

    if (interpolated.size() == parts.size()) {
      parts.add("");
    }

    String[] partArray = parts.toArray(new String[parts.size()]);
    dedentLines(partArray);

    return Expr.makeTextLiteral(partArray, (List) interpolated);
  }

  private static final void dedentLines(String[] input) {
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
        input[i] = escapeText(input[i]);
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

        input[i] = escapeText(builder.toString());
      }
    }
  }

  public static final String unescapeText(String in) {
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

  private static final String escapeText(String input) {
    return input.replace("\\", "\\\\").replace("\n", "\\n");
  }
}
