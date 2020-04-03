package org.dhallj.core.binary.cbor;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CBORConstructors {
  public static final class CBORUnsignedInteger extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onUnsignedInteger(value);
    }

    private final BigInteger value;

    CBORUnsignedInteger(BigInteger value) {
      this.value = value;
    }

    public BigInteger getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CBORUnsignedInteger that = (CBORUnsignedInteger) o;
      return value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static final class CBORNegativeInteger extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onNegativeInteger(value);
    }

    private final BigInteger value;

    public CBORNegativeInteger(BigInteger value) {
      this.value = value;
    }

    public BigInteger getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CBORNegativeInteger that = (CBORNegativeInteger) o;
      return value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static final class CBORByteString extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onByteString(value);
    }

    private final byte[] value;

    CBORByteString(byte[] value) {
      this.value = value;
    }

    public byte[] getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CBORByteString that = (CBORByteString) o;
      return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(value);
    }
  }

  public static final class CBORTextString extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onTextString(value);
    }

    private final String value;

    CBORTextString(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CBORTextString that = (CBORTextString) o;
      return value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static final class CBORHeterogeneousArray extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onArray(value);
    }

    private final List<CBORExpression> value;

    CBORHeterogeneousArray(List<CBORExpression> value) {
      this.value = value;
    }

    public List<CBORExpression> getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CBORHeterogeneousArray that = (CBORHeterogeneousArray) o;
      return value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static final class CBORHeterogeneousMap extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onMap(value);
    }

    private final Map<CBORExpression, CBORExpression> value;

    CBORHeterogeneousMap(Map<CBORExpression, CBORExpression> value) {
      this.value = value;
    }

    public Map<CBORExpression, CBORExpression> getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CBORHeterogeneousMap that = (CBORHeterogeneousMap) o;
      return value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static final class CBORFalse extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onFalse();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      return true;
    }
  }

  public static final class CBORTrue extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onTrue();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      return true;
    }
  }

  public static final class CBORNull extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onNull();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      return true;
    }
  }

  public static final class CBORHalfFloat extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onHalfFloat(value);
    }

    private final float value;

    CBORHalfFloat(float value) {
      this.value = value;
    }

    public float getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CBORHalfFloat that = (CBORHalfFloat) o;
      return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static final class CBORSingleFloat extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onSingleFloat(value);
    }

    private final float value;

    CBORSingleFloat(float value) {
      this.value = value;
    }

    public float getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CBORSingleFloat that = (CBORSingleFloat) o;
      return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static final class CBORDoubleFloat extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onDoubleFloat(value);
    }

    private final double value;

    CBORDoubleFloat(double value) {
      this.value = value;
    }

    public double getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CBORDoubleFloat that = (CBORDoubleFloat) o;
      return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static final class CBORTag extends CBORExpression {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.onTag();
    }
  }
}
