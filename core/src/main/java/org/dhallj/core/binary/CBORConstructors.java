package org.dhallj.core.binary;

import java.math.BigInteger;

final class CBORConstructors {
   static final class CBORUnsignedInteger extends CBORExpression {

      private final BigInteger value;

      CBORUnsignedInteger(BigInteger value) {
         this.value = value;
      }
   }

   static final class CBORNegativeInteger extends CBORExpression {

      private final BigInteger value;

      CBORNegativeInteger(BigInteger value) {
         this.value = value;
      }
   }

   static final class CBORByteString extends CBORExpression {

      private final byte[] value;

      CBORByteString(byte[] value) {
         this.value = value;
      }
   }

   static final class CBORTextString extends CBORExpression {

      private final byte[] value;

      CBORTextString(byte[] value) {
         this.value = value;
      }
   }

   static final class CBORHeterogeneousArray extends CBORExpression {

   }

   static final class CBORHeterogeneousMap extends CBORExpression {

   }

   static final class CBORFalse extends CBORExpression {

   }

   static final class CBORTrue extends CBORExpression {

   }

   static final class CBORNull extends CBORExpression {

   }

   static final class CBORHalfFloat extends CBORExpression {

   }

   static final class CBORSingleFloat extends CBORExpression {

   }

   static final class CBORDoubleFloat extends CBORExpression {

   }

   static final class CBORUnsignedBignum extends CBORExpression {

   }

   static final class CBORNegativeBignum extends CBORExpression {

   }

   static final class CBORTag extends CBORExpression {

   }

}
