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

    public ParsingFailure(String message) {
      super(message);
    }
  }

  public static final class ResolutionFailure extends DhallException {
    private boolean isAbsentImport;

    @Override
    public Throwable fillInStackTrace() {
      // This is a failure type; stack traces aren't useful.
      return this;
    }

    public ResolutionFailure(String message, Throwable cause, boolean isAbsentImport) {
      super(message, cause);
      this.isAbsentImport = isAbsentImport;
    }

    public ResolutionFailure(String message, boolean isAbsentImport) {
      super(message);
      this.isAbsentImport = isAbsentImport;
    }

    public ResolutionFailure(String message, Throwable cause) {
      this(message, cause, false);
    }

    public ResolutionFailure(String message) {
      this(message, false);
    }

    public boolean isAbsentImport() {
      return this.isAbsentImport;
    }
  }
}
