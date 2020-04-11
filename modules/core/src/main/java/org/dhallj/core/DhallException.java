package org.dhallj.core;

/** Base class of exceptions that may be thrown or returned by DhallJ. */
public class DhallException extends RuntimeException {
  public DhallException(String message) {
    super(message);
  }

  public DhallException(String message, Throwable cause) {
    super(message, cause);
  }

  /** Represents a parsing failure, generally wrapping an underlying exception. */
  public static final class ParsingFailure extends DhallException {
    @Override
    public Throwable fillInStackTrace() {
      // This is a failure type; stack traces aren't useful.
      return this;
    }

    public ParsingFailure(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
