package org.dhallj.core.error;

public class DecodingException extends DhallException {
  public DecodingException(String message) {
    super(message);
  }

  public DecodingException(String message, Throwable cause) {
    super(message, cause);
  }
}
