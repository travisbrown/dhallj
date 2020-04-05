package org.dhallj.imports.mini;

public class ResolutionException extends Exception {
  ResolutionException(String message, Exception underlying) {
    super(message, underlying);
  }

  ResolutionException(String message) {
    super(message);
  }
}
