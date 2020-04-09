package org.dhallj.core;

public class DhallException extends RuntimeException {
  public DhallException(String message) {
    super(message);
  }

  public DhallException(String message, Throwable cause) {
    super(message, cause);
  }
}
