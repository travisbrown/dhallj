package org.dhallj.core.binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.math.BigInteger;

public abstract class Decoder {
  protected abstract byte peek();

  protected abstract byte read();

  protected abstract byte[] read(int count);

  private int readMajorType() {
    return this.peek() >> 5;
  }

  private Long readLength() {
    return 0L;
  }

  public final class ByteBufferDecoder extends Decoder {
    private final ByteBuffer buffer;
    private byte next;
    private boolean seen = true;

    public ByteBufferDecoder(ByteBuffer buffer) {
      this.buffer = buffer;
    }

    protected byte peek() {
      if (this.seen) {
        this.next = this.buffer.get();
        this.seen = false;
      }
      return this.next;
    }

    protected byte read() {
      if (this.seen) {
        this.next = this.buffer.get();
      }
      this.seen = true;
      return this.next;
    }

    protected byte[] read(int count) {
      byte[] bs = new byte[count];
      this.buffer.get(bs);
      return bs;
    }
  }

  public final class ByteArrayDecoder extends Decoder {
    private final byte[] bytes;
    private int cursor = 0;

    public ByteArrayDecoder(byte[] bytes) {
      this.bytes = bytes;
    }

    protected byte peek() {
      return this.bytes[this.cursor];
    }

    protected byte read() {
      return this.bytes[this.cursor++];
    }

    protected byte[] read(int count) {
      byte[] bs = new byte[count];

      System.arraycopy(bytes, this.cursor, bs, 0, count);
      this.cursor += count;

      return bytes;
    }
  }
}
