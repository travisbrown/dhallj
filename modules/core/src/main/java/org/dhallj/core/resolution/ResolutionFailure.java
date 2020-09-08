package org.dhallj.core.resolution;

import org.dhallj.core.DhallException;

public class ResolutionFailure extends DhallException {
  @Override
  public Throwable fillInStackTrace() {
    // This is a failure type; stack traces aren't useful.
    return this;
  }

  public ResolutionFailure(String message) {
    super(message);
  }

  public ResolutionFailure(String message, Throwable cause) {
    super(message, cause);
  }

  public static final ResolutionFailure makeMissingError() {
    return new ResolutionFailure("Cannot resolve missing import");
  }
}
