package org.dhallj.cbor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public final class ByteArrayWriter extends Writer {
  protected final ByteArrayOutputStream stream;

  public ByteArrayWriter() {
    this.stream = new ByteArrayOutputStream();
  }

  protected OutputStream getStream() {
    return this.stream;
  }

  public byte[] getBytes() {
    try {
      stream.close();
    } catch (IOException e) {
    }
    return stream.toByteArray();
  }
}
