package org.dhallj.imports.mini;

import org.dhallj.core.DhallException;

public final class ResolutionException extends DhallException {
  ResolutionException(String message, Throwable cause) {
    super(message, cause);
  }

  ResolutionException(String message) {
    super(message);
  }
}
