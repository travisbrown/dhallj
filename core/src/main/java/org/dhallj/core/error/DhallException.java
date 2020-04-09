package org.dhallj.core.error;

public class DhallException extends RuntimeException {
  DhallException(String message) {
    super(message);
  }

  DhallException(String message, Throwable cause) {
    super(message, cause);
  }
}
