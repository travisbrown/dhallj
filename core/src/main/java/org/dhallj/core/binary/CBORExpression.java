package org.dhallj.core.binary;

public abstract class CBORExpression {

    public static class Constants {

        public enum MajorType {
            UNSIGNED_INTEGER(0),
            NEGATIVE_INTEGER(1),
            BYTE_STRING(2),
            TEXT_STRING(3),
            ARRAY(4),
            MAP(5),
            SEMANTIC_TAG(6),
            PRIMITIVE(7);

            private final int value;

            private MajorType(int value) {
                this.value = value;
            }

            public static MajorType fromByte(int b) {
                switch (b >> 5) {
                    case 0:
                        return UNSIGNED_INTEGER;
                    case 1:
                        return NEGATIVE_INTEGER;
                    case 2:
                        return BYTE_STRING;
                    case 3:
                        return TEXT_STRING;
                    case 4:
                        return ARRAY;
                    case 5:
                        return MAP;
                    case 6:
                        return SEMANTIC_TAG;
                    case 7:
                        return PRIMITIVE;
                    default:
                        throw new RuntimeException(String.format("Invalid CBOR major type %d", b));
                }
            }
        }

        public enum AdditionalInfo {

            DIRECT(0), // 0-23
            ONE_BYTE(24), // 24
            TWO_BYTES(25), // 25
            FOUR_BYTES(26), // 26
            EIGHT_BYTES(27), // 27
            RESERVED(28), // 28-30
            INDEFINITE(31); // 31

            private final int value;

            private AdditionalInfo(int value) {
                this.value = value;
            }

            public static AdditionalInfo fromByte(int b) {
                switch (b & 31) {
                    case 24:
                        return ONE_BYTE;
                    case 25:
                        return TWO_BYTES;
                    case 26:
                        return FOUR_BYTES;
                    case 27:
                        return EIGHT_BYTES;
                    case 28:
                    case 29:
                    case 30:
                        return RESERVED;
                    case 31:
                        return INDEFINITE;
                    default:
                        return DIRECT;
                }
            }
        }
    }

}

