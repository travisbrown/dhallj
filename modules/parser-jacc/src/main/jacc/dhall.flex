package org.dhallj.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;

%%

%class Lexer
%implements ParserTokens
%public
%final
%ctorarg WhitespaceHandler whitespaceHandler
%apiprivate

%line
%column

%function yylex
%int
%char

%init{
  this.whitespaceHandler = whitespaceHandler;
%init}

%{
  private final WhitespaceHandler whitespaceHandler;

  private int token;
  private String semantic;
  private final Deque<Integer> states = new ArrayDeque<>();
  private final Deque<Integer> braceDepth = new ArrayDeque<>();
  private final StringBuilder content = new StringBuilder();
  private int waitingForWhitespace;

  Lexer(Reader reader) {
    this(reader, WhitespaceHandler.noop);
  }

  public final int getToken() {
    return this.token;
  }

  public final String getSemantic() {
    return this.semantic;
  }

  public final int nextToken() {
    try {
      this.token = this.yylex();
    } catch (IOException e) {
      System.out.println("IO exception occured:\n" + e);
    }
    return this.token;
  }

  private final void pushState(int state) {
    this.states.push(this.yystate());
    this.yybegin(state);
  }

  private final int popState() {
    int state = this.states.pop();
    this.yybegin(state);
    return state;
  }

  private final void waitForWhitespace(int token) {
    this.waitingForWhitespace = token;
    this.pushState(REQUIRE_WHITESPACE);
  }

  private final int foundWhitespace() {
    int state = this.states.pop();
    this.yybegin(state);
    return this.waitingForWhitespace;
  }

  private final void startBlockComment() {
    this.content.setLength(0);
    this.content.append(this.yytext());
    this.pushState(BLOCK_COMMENT);
  }
%}

%xstate BLOCK_COMMENT
%xstate REQUIRE_WHITESPACE
%xstate DOUBLE_QUOTE
%xstate SINGLE_QUOTE

END_OF_LINE = \n | \r\n
BLOCK_COMMENT_START = "{-"
BLOCK_COMMENT_END = "-}"
BLOCK_COMMENT_CHAR = [\u0020-\u007f] | \t | {END_OF_LINE} | {VALID_NON_ASCII}

LINE_COMMENT_START = "--"
LINE_COMMENT_CHAR = [\u0020-\u007f] | \t | {VALID_NON_ASCII}
LINE_COMMENT = {LINE_COMMENT_START} {LINE_COMMENT_CHAR}* {END_OF_LINE}

WHSP_CHUNK = [ \t] | {END_OF_LINE}
VALID_NON_ASCII = [\u0080-\ud7ff] | ([\ud800-\udbff] [\udc00-\udfff]) | [\ue000-\ufffd]

INTERPOLATION_START = "${"
DOUBLE_QUOTE_CHARS = (\u0020 | \u0021 | "#" | [\u0025-\u005b] | [\u005d-\u007f] | (\\ [\bfnrt\$\/\"]) | (\u {UNICODE_ESCAPE}) | {VALID_NON_ASCII})+

SINGLE_QUOTE_START = "''" {END_OF_LINE}
SINGLE_QUOTE_END = "''"
SINGLE_QUOTE_ESCAPED_PAIR = "'''"
SINGLE_QUOTE_ESCAPED_INTERPOLATION = "''${"
SINGLE_QUOTE_CHAR = [\u0020-\u007f] | \t | {VALID_NON_ASCII} | {END_OF_LINE}

UNICODE_ESCAPE = {UNBRACED_ESCAPE} | "{" {BRACED_ESCAPE} "}"
UNBRACED_ESCAPE =
    (([:digit:] | [ABCabc]) {HEX_DIGIT}{3})
  | [Dd] [0-7] {HEX_DIGIT} {HEX_DIGIT}
  | [Ee] {HEX_DIGIT}{3}
  | [Ff] {HEX_DIGIT} {HEX_DIGIT} [ABCDabcd]

BRACED_ESCAPE = "0"* {BRACED_CODEPOINT}
BRACED_CODEPOINT = (([0-9A-Fa-f] | "10") {UNICODE_SUFFIX}) | {UNBRACED_ESCAPE} | ({HEX_DIGIT}{3})+

UNICODE_SUFFIX = [0-9A-Ea-e] {HEX_DIGIT}{3} | F {HEX_DIGIT} {HEX_DIGIT} [0-9A-Da-d]

SIGN = [+-]
DIGITS = [:digit:]+
HEX_DIGIT = [0-9A-Fa-f]
HEX_DIGITS = {HEX_DIGIT}+
EXPONENT = [Ee] {SIGN}? {DIGITS}

ALPHA = [A-Za-z]
QUOTED_LABEL = "`" ([\u0020-\u005f] | [\u0061-\u007e])+ "`"
SIMPLE_LABEL = ({ALPHA} | "_") ({ALPHA} | [:digit:] | "_" | "-" | "/")*

DOUBLE_LITERAL = ({SIGN}? {DIGITS} (("." {DIGITS} {EXPONENT}?) | {EXPONENT})) | ("-"? "Infinity") | "NaN"
NATURAL_LITERAL = "0" | ([1-9] [:digit:]*) | ("0x" {HEX_DIGITS})
INTEGER_LITERAL = {SIGN} {NATURAL_LITERAL}

SHA256_HASH = "sha256:" {HEX_DIGIT}{64}
ENV = "env:" ({BASH_ENV_VAR} | ("\"" {POSIX_ENV_VAR} "\""))
HTTP = "http" "s"? "://" ({USER_INFO} "@")? {HOST} (":" [:digit:]*)? {URL_PATH} ("?" {QUERY})?
LOCAL = {PARENT_PATH} | {HERE_PATH} | {HOME_PATH} | {PATH}
CLASSPATH = "classpath:" {PATH}

BASH_ENV_VAR = ({ALPHA} | "_") ({ALPHA} | [:digit:] | "_")*
POSIX_ENV_VAR = \u0020 | \u0021 | [\u0023-\u003c] | [\u003e-\u005b] | [\u005d-\u007e] | (\ [\abfnrtv\"])+

PCT_ENCODED = "%" {HEX_DIGIT} {HEX_DIGIT}
SUB_DELIM = [\!\$&'\*\+;=]
UNRESERVED = {ALPHA} | [:digit:] | [-\._\~]
PCHAR = {UNRESERVED} | {PCT_ENCODED} | {SUB_DELIM} | [:@]
SEGMENT = {PCHAR}*

USER_INFO = ({UNRESERVED} | {PCT_ENCODED} | {SUB_DELIM} | ":")*
QUERY = ({PCHAR} | "/" | "?")*
DOMAIN_LABEL = ({ALPHA} | [:digit:])+ (("-")+ ({ALPHA} | [:digit:])+)*
DOMAIN = {DOMAIN_LABEL} ("." {DOMAIN_LABEL})* (".")?

IPV4 = {DEC_OCTET} "." {DEC_OCTET} "." {DEC_OCTET} "." {DEC_OCTET}
H16 = {HEX_DIGIT}{1,4}
LS32 = ({H16} ":" {H16}) | {IPV4}
IPV6
  = (({H16} ":"){6} {LS32})
  | ("::" ({H16} ":"){5} {LS32})
  | (({H16})? "::" ({H16} ":"){4} {LS32})
  | (({H16} (":" {H16}){0, 1})? "::" ({H16} ":"){3} {LS32})
  | (({H16} (":" {H16}){0, 2})? "::" ({H16} ":"){2} {LS32})
  | (({H16} (":" {H16}){0, 3})? "::" {H16} ":" {LS32})
  | (({H16} (":" {H16}){0, 4})? "::" {LS32})
  | (({H16} (":" {H16}){0, 5})? "::" {H16})
  | (({H16} (":" {H16}){0, 6})? "::")

IPVFUTURE = "v" {HEX_DIGIT}+ "." ({UNRESERVED} | {SUB_DELIM} | ":")+
DEC_OCTET = ("25" [0-5]) | ("2" [0-4] [:digit:]) | ("1" [:digit:] [:digit:]) | ([1-9] [:digit:]) | [:digit:]
HOST = {DOMAIN} | {IPV4} | ("[" ({IPV6} | {IPVFUTURE}) "]")

%include paths.macros

%%
{WHSP_CHUNK}+ { this.whitespaceHandler.onWhitespace(this.yytext(), this.yychar); }
{LINE_COMMENT} { this.whitespaceHandler.onLineComment(this.yytext(), this.yychar); }
{BLOCK_COMMENT_START} { this.startBlockComment(); }

%include keywords.rules
%include builtins.rules
%include symbols.rules

{INTEGER_LITERAL} { semantic = yytext(); return INTEGER_LITERAL; }
{NATURAL_LITERAL} { semantic = yytext(); return NATURAL_LITERAL; }
{DOUBLE_LITERAL}  { semantic = yytext(); return DOUBLE_LITERAL; }
{QUOTED_LABEL}    { semantic = yytext(); semantic = semantic.substring(1, semantic.length() - 1); return LABEL; }
{SIMPLE_LABEL}    { semantic = yytext(); return LABEL; }
"\"" { this.content.setLength(0); this.pushState(DOUBLE_QUOTE); return DOUBLE_QUOTE_MARK; }
{SINGLE_QUOTE_START} { this.content.setLength(0); this.pushState(SINGLE_QUOTE); return SINGLE_QUOTE_START; }
"{" {
  if (this.braceDepth.isEmpty()) {
    this.braceDepth.push(1);
  } else {
    this.braceDepth.push(this.braceDepth.pop() + 1);
  }
  return BRACE_OPEN;
}
"}" {
  int currentBraceDepth = this.braceDepth.pop() - 1;

  if (currentBraceDepth >= 0) {
    this.braceDepth.push(currentBraceDepth);
  } else {
    this.popState();
  }
  return BRACE_CLOSE;
}

{SHA256_HASH} { semantic = this.yytext(); return SHA256_HASH; }
{ENV}         { semantic = this.yytext(); return ENV; }
{HTTP}        { semantic = this.yytext(); return HTTP; }
{LOCAL}       { semantic = this.yytext(); return LOCAL; }
{CLASSPATH}   { semantic = this.yytext(); return CLASSPATH; }

<BLOCK_COMMENT> {
  {BLOCK_COMMENT_START} { this.pushState(BLOCK_COMMENT); }
  {BLOCK_COMMENT_CHAR} { this.content.append(this.yytext()); }
  {BLOCK_COMMENT_END} {
    this.content.append(this.yytext());

    int state = this.popState();
    if (state != BLOCK_COMMENT) {
      String commentContent = this.content.toString();
      this.whitespaceHandler.onBlockComment(commentContent, this.yychar - commentContent.length());
      if (state == REQUIRE_WHITESPACE) {
        return this.foundWhitespace();
      }
    }
  }
}

<REQUIRE_WHITESPACE> {
  {WHSP_CHUNK}+ { this.whitespaceHandler.onWhitespace(this.yytext(), this.yychar); return this.foundWhitespace(); }
  {LINE_COMMENT} {this.whitespaceHandler.onWhitespace(this.yytext(), this.yychar); return this.foundWhitespace(); }
  {BLOCK_COMMENT_START} { this.startBlockComment(); }
}

<DOUBLE_QUOTE> {
  {INTERPOLATION_START} {
    this.pushState(YYINITIAL);
    this.braceDepth.push(0);
    this.semantic = this.content.toString();
    this.content.setLength(0);
    return INTERPOLATION_START;
  }
  "\"" { this.popState(); this.semantic = this.content.toString(); return DOUBLE_QUOTE_MARK; }
  {DOUBLE_QUOTE_CHARS} { this.content.append(this.yytext()); }
  "$" { this.content.append("$"); }
}

<SINGLE_QUOTE> {
  {INTERPOLATION_START} { this.pushState(YYINITIAL); this.braceDepth.push(0); return INTERPOLATION_START; }
  {SINGLE_QUOTE_END} { this.popState(); return SINGLE_QUOTE_END; }
  {SINGLE_QUOTE_CHAR} { this.semantic = this.yytext(); return SINGLE_QUOTE_CHAR; }
}

<<EOF>> { token = ENDINPUT; return ENDINPUT; }
[^] { System.out.println("Error?"); }
