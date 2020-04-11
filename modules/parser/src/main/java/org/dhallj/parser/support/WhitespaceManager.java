package org.dhallj.parser.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Special tokenization for whitespace.
 *
 * <p>Dhall's definition of whitespace doesn't fit cleanly with JavaCC's model, so if we want no
 * consecutive whitespace tokens, we have to tokenize ourselves.
 *
 * <p>This is roughly working, except for the sources, but desperately needs clean-up.
 */
final class WhitespaceManager {
  private final List<Comment> comments = new ArrayList();
  private char curr = 0;

  private boolean advance(SimpleCharStream stream) {
    try {
      this.curr = stream.readChar();
      return false;
    } catch (IOException e) {
      return true;
    }
  }

  private void advanceNotEof(SimpleCharStream stream) {
    if (advance(stream)) {
      fail(this.curr, stream);
    }
  }

  private static boolean isCommentChar(char c) {
    return (c >= '\u0020' && c <= '\ud7ff') || (c >= '\ue000' && c <= '\ufffd') || (c == '\t');
  }

  private static void fail(int current, SimpleCharStream stream) {
    throw new TokenMgrException(
        false,
        0,
        stream.getEndLine(),
        stream.getEndColumn(),
        stream.GetImage(),
        current,
        TokenMgrException.LEXICAL_ERROR);
  }

  List<Comment> consume(SimpleCharStream stream, char first) {
    if (first == '{') {
      return consumeWithComments(stream, true);
    } else if (first == '-') {
      return consumeWithComments(stream, false);
    } else {
      if (advance(stream)) {
        return null;
      } else if (this.curr == '{' || this.curr == '-') {
        boolean startsWithBlock = this.curr == '{';
        if (advance(stream)) {
          stream.backup(1);
          return null;
        } else if (this.curr == '-') {
          return consumeWithComments(stream, startsWithBlock);
        } else {
          stream.backup(2);
          return null;
        }
      } else {
        stream.backup(1);
        return null;
      }
    }
  }

  private Comment consumeLineComment(SimpleCharStream stream) {
    int commentLen = 2;
    int commentBeginLine = stream.getBeginLine();
    int commentBeginColumn = stream.getBeginColumn();

    while (true) {
      advanceNotEof(stream);

      if (isCommentChar(this.curr)) {
        commentLen += 1;
      } else if (this.curr == '\n') {
        break;
      } else if (this.curr == '\r') {
        if (advance(stream) || this.curr != '\n') {
          fail(this.curr, stream);
        }
        break;
      } else if (Character.isHighSurrogate(this.curr)) {
        if (advance(stream) || !Character.isLowSurrogate(this.curr)) {
          fail(this.curr, stream);
        }
        commentLen += 2;
      } else {
        fail(this.curr, stream);
      }
    }

    return new Comment(
        new String(stream.GetSuffix(commentLen)),
        commentBeginLine,
        commentBeginColumn,
        stream.getEndLine(),
        stream.getEndColumn());
  }

  private Comment consumeBlockComment(SimpleCharStream stream) {
    int blockCommentLevel = 1;
    int commentLen = 2;
    int commentBeginLine = stream.getBeginLine();
    int commentBeginColumn = stream.getBeginColumn();

    advanceNotEof(stream);

    do {
      if (this.curr == '-') {
        advanceNotEof(stream);
        if (this.curr == '}') {
          blockCommentLevel -= 1;
        } else {
          commentLen += 1;
          advanceNotEof(stream);
        }
      } else if (this.curr == '{') {
        advanceNotEof(stream);
        if (this.curr == '-') {
          blockCommentLevel += 1;
        } else {
          commentLen += 1;
          advanceNotEof(stream);
        }
      } else if (isCommentChar(this.curr) || this.curr == '\n') {
        commentLen += 1;
        advanceNotEof(stream);
      } else if (this.curr == '\r') {
        if (advance(stream) || this.curr != '\n') {
          fail(this.curr, stream);
        }
        commentLen += 1;
        advanceNotEof(stream);
      } else if (Character.isHighSurrogate(this.curr)) {
        if (advance(stream) || !Character.isLowSurrogate(this.curr)) {
          fail(this.curr, stream);
        }
        commentLen += 2;
        advanceNotEof(stream);
      } else {
        fail(this.curr, stream);
      }
    } while (blockCommentLevel > 0);

    return new Comment(
        new String(stream.GetSuffix(commentLen)),
        commentBeginLine,
        commentBeginColumn,
        stream.getEndLine(),
        stream.getEndColumn());
  }

  private List<Comment> consumeWithComments(SimpleCharStream stream, boolean startsWithBlock) {
    comments.clear();

    if (startsWithBlock) {
      comments.add(consumeBlockComment(stream));
    } else {
      comments.add(consumeLineComment(stream));
    }

    while (true) {
      if (advance(stream)) {
        return comments;
      }

      switch (this.curr) {
        case ' ':
        case '\t':
        case '\n':
          break;
        case '{':
          advance(stream);
          if (this.curr == '-') {
            comments.add(consumeBlockComment(stream));
          } else {
            stream.backup(2);
            return comments;
          }
          break;
        case '-':
          advance(stream);
          if (this.curr == '-') {
            comments.add(consumeLineComment(stream));
          } else {
            stream.backup(2);
            return comments;
          }
          break;
        case '\r':
          advanceNotEof(stream);
          if (this.curr != '\n') {
            stream.backup(2);
            return comments;
          }
          break;
        default:
          stream.backup(1);
          return null;
      }
    }
  }
}
