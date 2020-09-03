package org.dhallj.prelude;

import java.math.BigInteger;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;

public final class Prelude {
  private static final Expr f000000 = Expr.makeBuiltIn("List/fold");
  private static final Expr f000001 = Expr.makeBuiltIn("Bool");
  private static final Expr f000002 = Expr.makeIdentifier("_", 0);
  private static final Expr f000003 = Expr.makeIdentifier("_", 1);
  private static final Expr f000004 = Expr.makeOperatorApplication(Operator.AND, f000003, f000002);
  private static final Expr f000005 = Expr.makeLambda("_", f000001, f000004);
  private static final Expr f000006 = Expr.makeLambda("_", f000001, f000005);
  private static final Expr f000007 = Expr.Constants.TRUE;
  private static final Expr f000008 =
      Expr.makeApplication(f000000, new Expr[] {f000001, f000002, f000001, f000006, f000007});
  private static final Expr f000009 = Expr.Constants.LIST;
  private static final Expr f000010 = Expr.makeApplication(f000009, new Expr[] {f000001});
  private static final Expr f000011 = Expr.makeLambda("_", f000010, f000008);
  private static final Expr f000012 = Expr.Constants.FALSE;
  private static final Expr f000013 =
      Expr.makeApplication(f000002, new Expr[] {f000001, f000007, f000012});
  private static final Expr f000014 = Expr.makeIdentifier("_", 2);
  private static final Expr f000015 = Expr.makePi("_", f000003, f000014);
  private static final Expr f000016 = Expr.makePi("_", f000002, f000015);
  private static final Expr f000017 = Expr.Constants.TYPE;
  private static final Expr f000018 = Expr.makePi("_", f000017, f000016);
  private static final Expr f000019 = Expr.makeLambda("_", f000018, f000013);
  private static final Expr f000020 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000003, f000002);
  private static final Expr f000021 = Expr.makeLambda("_", f000001, f000020);
  private static final Expr f000022 = Expr.makeLambda("_", f000001, f000021);
  private static final Expr f000023 =
      Expr.makeApplication(f000000, new Expr[] {f000001, f000002, f000001, f000022, f000007});
  private static final Expr f000024 = Expr.makeLambda("_", f000010, f000023);
  private static final Expr f000025 = Expr.makeIdentifier("_", 3);
  private static final Expr f000026 = Expr.makeIf(f000025, f000003, f000002);
  private static final Expr f000027 = Expr.makeLambda("_", f000003, f000026);
  private static final Expr f000028 = Expr.makeLambda("_", f000002, f000027);
  private static final Expr f000029 = Expr.makeLambda("_", f000017, f000028);
  private static final Expr f000030 = Expr.makeLambda("_", f000001, f000029);
  private static final Expr f000031 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000002, f000012);
  private static final Expr f000032 = Expr.makeLambda("_", f000001, f000031);
  private static final Expr f000033 =
      Expr.makeOperatorApplication(Operator.NOT_EQUALS, f000003, f000002);
  private static final Expr f000034 = Expr.makeLambda("_", f000001, f000033);
  private static final Expr f000035 = Expr.makeLambda("_", f000001, f000034);
  private static final Expr f000036 =
      Expr.makeApplication(f000000, new Expr[] {f000001, f000002, f000001, f000035, f000012});
  private static final Expr f000037 = Expr.makeLambda("_", f000010, f000036);
  private static final Expr f000038 = Expr.makeOperatorApplication(Operator.OR, f000003, f000002);
  private static final Expr f000039 = Expr.makeLambda("_", f000001, f000038);
  private static final Expr f000040 = Expr.makeLambda("_", f000001, f000039);
  private static final Expr f000041 =
      Expr.makeApplication(f000000, new Expr[] {f000001, f000002, f000001, f000040, f000012});
  private static final Expr f000042 = Expr.makeLambda("_", f000010, f000041);
  private static final Expr f000043 = Expr.makeTextLiteral("True");
  private static final Expr f000044 = Expr.makeTextLiteral("False");
  private static final Expr f000045 = Expr.makeIf(f000002, f000043, f000044);
  private static final Expr f000046 = Expr.makeLambda("_", f000001, f000045);
  private static final Expr f000047 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("and", f000011),
            new SimpleImmutableEntry<String, Expr>("build", f000019),
            new SimpleImmutableEntry<String, Expr>("even", f000024),
            new SimpleImmutableEntry<String, Expr>("fold", f000030),
            new SimpleImmutableEntry<String, Expr>("not", f000032),
            new SimpleImmutableEntry<String, Expr>("odd", f000037),
            new SimpleImmutableEntry<String, Expr>("or", f000042),
            new SimpleImmutableEntry<String, Expr>("show", f000046)
          });
  private static final Expr f000048 = Expr.makeBuiltIn("Double/show");
  private static final Expr f000049 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("show", f000048)});
  private static final Expr f000050 = Expr.makeApplication(f000014, new Expr[] {f000002});
  private static final Expr f000051 = Expr.makeApplication(f000003, new Expr[] {f000050});
  private static final Expr f000052 = Expr.makeIdentifier("_", 4);
  private static final Expr f000053 = Expr.makeLambda("_", f000052, f000051);
  private static final Expr f000054 = Expr.makePi("_", f000014, f000014);
  private static final Expr f000055 = Expr.makeLambda("_", f000054, f000053);
  private static final Expr f000056 = Expr.makeLambda("_", f000054, f000055);
  private static final Expr f000057 = Expr.makeLambda("_", f000017, f000056);
  private static final Expr f000058 = Expr.makeLambda("_", f000017, f000057);
  private static final Expr f000059 = Expr.makeLambda("_", f000017, f000058);
  private static final Expr f000060 = Expr.makeLambda("_", f000002, f000002);
  private static final Expr f000061 = Expr.makeLambda("_", f000017, f000060);
  private static final Expr f000062 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("compose", f000059),
            new SimpleImmutableEntry<String, Expr>("identity", f000061)
          });
  private static final Expr f000063 = Expr.makeBuiltIn("Natural/isZero");
  private static final Expr f000064 = Expr.makeBuiltIn("Integer/clamp");
  private static final Expr f000065 = Expr.makeApplication(f000064, new Expr[] {f000002});
  private static final Expr f000066 = Expr.makeApplication(f000063, new Expr[] {f000065});
  private static final Expr f000067 = Expr.makeBuiltIn("Integer/negate");
  private static final Expr f000068 = Expr.makeApplication(f000067, new Expr[] {f000002});
  private static final Expr f000069 = Expr.makeApplication(f000064, new Expr[] {f000068});
  private static final Expr f000070 = Expr.makeIf(f000066, f000069, f000065);
  private static final Expr f000071 = Expr.Constants.INTEGER;
  private static final Expr f000072 = Expr.makeLambda("_", f000071, f000070);
  private static final Expr f000073 = Expr.makeApplication(f000067, new Expr[] {f000003});
  private static final Expr f000074 = Expr.makeApplication(f000064, new Expr[] {f000073});
  private static final Expr f000075 = Expr.makeApplication(f000063, new Expr[] {f000074});
  private static final Expr f000076 = Expr.makeBuiltIn("Natural/subtract");
  private static final Expr f000077 = Expr.makeApplication(f000067, new Expr[] {f000073});
  private static final Expr f000078 = Expr.makeApplication(f000064, new Expr[] {f000077});
  private static final Expr f000079 = Expr.makeApplication(f000076, new Expr[] {f000069, f000078});
  private static final Expr f000080 = Expr.makeApplication(f000063, new Expr[] {f000079});
  private static final Expr f000081 = Expr.makeBuiltIn("Natural/toInteger");
  private static final Expr f000082 = Expr.makeApplication(f000076, new Expr[] {f000078, f000069});
  private static final Expr f000083 = Expr.makeApplication(f000081, new Expr[] {f000082});
  private static final Expr f000084 = Expr.makeApplication(f000067, new Expr[] {f000083});
  private static final Expr f000085 = Expr.makeApplication(f000081, new Expr[] {f000079});
  private static final Expr f000086 = Expr.makeIf(f000080, f000084, f000085);
  private static final Expr f000087 = Expr.makeOperatorApplication(Operator.PLUS, f000078, f000065);
  private static final Expr f000088 = Expr.makeApplication(f000081, new Expr[] {f000087});
  private static final Expr f000089 = Expr.makeIf(f000066, f000086, f000088);
  private static final Expr f000090 = Expr.makeOperatorApplication(Operator.PLUS, f000074, f000069);
  private static final Expr f000091 = Expr.makeApplication(f000081, new Expr[] {f000090});
  private static final Expr f000092 = Expr.makeApplication(f000067, new Expr[] {f000091});
  private static final Expr f000093 = Expr.makeApplication(f000076, new Expr[] {f000074, f000065});
  private static final Expr f000094 = Expr.makeApplication(f000063, new Expr[] {f000093});
  private static final Expr f000095 = Expr.makeApplication(f000076, new Expr[] {f000065, f000074});
  private static final Expr f000096 = Expr.makeApplication(f000081, new Expr[] {f000095});
  private static final Expr f000097 = Expr.makeApplication(f000067, new Expr[] {f000096});
  private static final Expr f000098 = Expr.makeApplication(f000081, new Expr[] {f000093});
  private static final Expr f000099 = Expr.makeIf(f000094, f000097, f000098);
  private static final Expr f000100 = Expr.makeIf(f000066, f000092, f000099);
  private static final Expr f000101 = Expr.makeIf(f000075, f000089, f000100);
  private static final Expr f000102 = Expr.makeLambda("_", f000071, f000101);
  private static final Expr f000103 = Expr.makeLambda("_", f000071, f000102);
  private static final Expr f000104 = Expr.makeApplication(f000064, new Expr[] {f000003});
  private static final Expr f000105 = Expr.makeApplication(f000076, new Expr[] {f000065, f000104});
  private static final Expr f000106 = Expr.makeApplication(f000063, new Expr[] {f000105});
  private static final Expr f000107 = Expr.makeApplication(f000076, new Expr[] {f000104, f000065});
  private static final Expr f000108 = Expr.makeApplication(f000063, new Expr[] {f000107});
  private static final Expr f000109 = Expr.makeOperatorApplication(Operator.AND, f000106, f000108);
  private static final Expr f000110 = Expr.makeApplication(f000076, new Expr[] {f000069, f000074});
  private static final Expr f000111 = Expr.makeApplication(f000063, new Expr[] {f000110});
  private static final Expr f000112 = Expr.makeApplication(f000076, new Expr[] {f000074, f000069});
  private static final Expr f000113 = Expr.makeApplication(f000063, new Expr[] {f000112});
  private static final Expr f000114 = Expr.makeOperatorApplication(Operator.AND, f000111, f000113);
  private static final Expr f000115 = Expr.makeOperatorApplication(Operator.AND, f000109, f000114);
  private static final Expr f000116 = Expr.makeLambda("_", f000071, f000115);
  private static final Expr f000117 = Expr.makeLambda("_", f000071, f000116);
  private static final Expr f000118 = Expr.makeApplication(f000063, new Expr[] {f000104});
  private static final Expr f000119 = Expr.makeApplication(f000063, new Expr[] {f000069});
  private static final Expr f000120 = Expr.makeOperatorApplication(Operator.OR, f000119, f000113);
  private static final Expr f000121 = Expr.makeIf(f000118, f000120, f000106);
  private static final Expr f000122 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000121, f000012);
  private static final Expr f000123 = Expr.makeLambda("_", f000071, f000122);
  private static final Expr f000124 = Expr.makeLambda("_", f000071, f000123);
  private static final Expr f000125 = Expr.makeOperatorApplication(Operator.OR, f000075, f000111);
  private static final Expr f000126 = Expr.makeIf(f000066, f000125, f000108);
  private static final Expr f000127 = Expr.makeLambda("_", f000071, f000126);
  private static final Expr f000128 = Expr.makeLambda("_", f000071, f000127);
  private static final Expr f000129 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000126, f000012);
  private static final Expr f000130 = Expr.makeLambda("_", f000071, f000129);
  private static final Expr f000131 = Expr.makeLambda("_", f000071, f000130);
  private static final Expr f000132 = Expr.makeLambda("_", f000071, f000121);
  private static final Expr f000133 = Expr.makeLambda("_", f000071, f000132);
  private static final Expr f000134 =
      Expr.makeOperatorApplication(Operator.TIMES, f000074, f000069);
  private static final Expr f000135 = Expr.makeApplication(f000081, new Expr[] {f000134});
  private static final Expr f000136 =
      Expr.makeOperatorApplication(Operator.TIMES, f000074, f000065);
  private static final Expr f000137 = Expr.makeApplication(f000081, new Expr[] {f000136});
  private static final Expr f000138 = Expr.makeApplication(f000067, new Expr[] {f000137});
  private static final Expr f000139 = Expr.makeIf(f000066, f000135, f000138);
  private static final Expr f000140 =
      Expr.makeOperatorApplication(Operator.TIMES, f000104, f000069);
  private static final Expr f000141 = Expr.makeApplication(f000081, new Expr[] {f000140});
  private static final Expr f000142 = Expr.makeApplication(f000067, new Expr[] {f000141});
  private static final Expr f000143 =
      Expr.makeOperatorApplication(Operator.TIMES, f000104, f000065);
  private static final Expr f000144 = Expr.makeApplication(f000081, new Expr[] {f000143});
  private static final Expr f000145 = Expr.makeIf(f000066, f000142, f000144);
  private static final Expr f000146 = Expr.makeIf(f000118, f000139, f000145);
  private static final Expr f000147 = Expr.makeLambda("_", f000071, f000146);
  private static final Expr f000148 = Expr.makeLambda("_", f000071, f000147);
  private static final Expr f000149 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000119, f000012);
  private static final Expr f000150 = Expr.makeLambda("_", f000071, f000149);
  private static final Expr f000151 = Expr.makeLambda("_", f000071, f000119);
  private static final Expr f000152 = Expr.makeLambda("_", f000071, f000066);
  private static final Expr f000153 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000066, f000012);
  private static final Expr f000154 = Expr.makeLambda("_", f000071, f000153);
  private static final Expr f000155 = Expr.makeBuiltIn("Integer/show");
  private static final Expr f000156 = Expr.makeApplication(f000081, new Expr[] {f000112});
  private static final Expr f000157 = Expr.makeApplication(f000067, new Expr[] {f000156});
  private static final Expr f000158 = Expr.makeApplication(f000081, new Expr[] {f000110});
  private static final Expr f000159 = Expr.makeIf(f000111, f000157, f000158);
  private static final Expr f000160 = Expr.makeOperatorApplication(Operator.PLUS, f000074, f000065);
  private static final Expr f000161 = Expr.makeApplication(f000081, new Expr[] {f000160});
  private static final Expr f000162 = Expr.makeIf(f000066, f000159, f000161);
  private static final Expr f000163 = Expr.makeOperatorApplication(Operator.PLUS, f000104, f000069);
  private static final Expr f000164 = Expr.makeApplication(f000081, new Expr[] {f000163});
  private static final Expr f000165 = Expr.makeApplication(f000067, new Expr[] {f000164});
  private static final Expr f000166 = Expr.makeApplication(f000081, new Expr[] {f000105});
  private static final Expr f000167 = Expr.makeApplication(f000067, new Expr[] {f000166});
  private static final Expr f000168 = Expr.makeApplication(f000081, new Expr[] {f000107});
  private static final Expr f000169 = Expr.makeIf(f000108, f000167, f000168);
  private static final Expr f000170 = Expr.makeIf(f000066, f000165, f000169);
  private static final Expr f000171 = Expr.makeIf(f000118, f000162, f000170);
  private static final Expr f000172 = Expr.makeLambda("_", f000071, f000171);
  private static final Expr f000173 = Expr.makeLambda("_", f000071, f000172);
  private static final Expr f000174 = Expr.makeBuiltIn("Integer/toDouble");
  private static final Expr f000175 = Expr.makeBuiltIn("Some");
  private static final Expr f000176 = Expr.makeApplication(f000175, new Expr[] {f000065});
  private static final Expr f000177 = Expr.makeBuiltIn("None");
  private static final Expr f000178 = Expr.Constants.NATURAL;
  private static final Expr f000179 = Expr.makeApplication(f000177, new Expr[] {f000178});
  private static final Expr f000180 = Expr.makeIf(f000119, f000176, f000179);
  private static final Expr f000181 = Expr.makeLambda("_", f000071, f000180);
  private static final Expr f000182 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("abs", f000072),
            new SimpleImmutableEntry<String, Expr>("add", f000103),
            new SimpleImmutableEntry<String, Expr>("clamp", f000064),
            new SimpleImmutableEntry<String, Expr>("equal", f000117),
            new SimpleImmutableEntry<String, Expr>("greaterThan", f000124),
            new SimpleImmutableEntry<String, Expr>("greaterThanEqual", f000128),
            new SimpleImmutableEntry<String, Expr>("lessThan", f000131),
            new SimpleImmutableEntry<String, Expr>("lessThanEqual", f000133),
            new SimpleImmutableEntry<String, Expr>("multiply", f000148),
            new SimpleImmutableEntry<String, Expr>("negate", f000067),
            new SimpleImmutableEntry<String, Expr>("negative", f000150),
            new SimpleImmutableEntry<String, Expr>("nonNegative", f000151),
            new SimpleImmutableEntry<String, Expr>("nonPositive", f000152),
            new SimpleImmutableEntry<String, Expr>("positive", f000154),
            new SimpleImmutableEntry<String, Expr>("show", f000155),
            new SimpleImmutableEntry<String, Expr>("subtract", f000173),
            new SimpleImmutableEntry<String, Expr>("toDouble", f000174),
            new SimpleImmutableEntry<String, Expr>("toNatural", f000181)
          });
  private static final Expr f000183 = Expr.Constants.TEXT;
  private static final Expr f000184 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Inline", null),
            new SimpleImmutableEntry<String, Expr>("Nested", f000183)
          });
  private static final Expr f000185 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("contents", f000002),
            new SimpleImmutableEntry<String, Expr>("field", f000183),
            new SimpleImmutableEntry<String, Expr>("nesting", f000184)
          });
  private static final Expr f000186 = Expr.makeLambda("_", f000017, f000185);
  private static final Expr f000187 = Expr.makeApplication(f000009, new Expr[] {f000002});
  private static final Expr f000188 = Expr.makePi("_", f000187, f000003);
  private static final Expr f000189 = Expr.makePi("_", f000001, f000003);
  private static final Expr f000190 = Expr.Constants.DOUBLE;
  private static final Expr f000191 = Expr.makePi("_", f000190, f000003);
  private static final Expr f000192 = Expr.makePi("_", f000071, f000003);
  private static final Expr f000193 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000002)
          });
  private static final Expr f000194 = Expr.makeApplication(f000009, new Expr[] {f000193});
  private static final Expr f000195 = Expr.makePi("_", f000194, f000003);
  private static final Expr f000196 = Expr.makePi("_", f000183, f000003);
  private static final Expr f000197 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000188),
            new SimpleImmutableEntry<String, Expr>("bool", f000189),
            new SimpleImmutableEntry<String, Expr>("double", f000191),
            new SimpleImmutableEntry<String, Expr>("integer", f000192),
            new SimpleImmutableEntry<String, Expr>("null", f000002),
            new SimpleImmutableEntry<String, Expr>("object", f000195),
            new SimpleImmutableEntry<String, Expr>("string", f000196)
          });
  private static final Expr f000198 = Expr.makePi("_", f000197, f000003);
  private static final Expr f000199 = Expr.makePi("_", f000017, f000198);
  private static final Expr f000200 = Expr.makeFieldAccess(f000002, "array");
  private static final Expr f000201 = Expr.makeApplication(f000009, new Expr[] {f000003});
  private static final Expr f000202 = Expr.makeApplication(f000003, new Expr[] {f000025, f000014});
  private static final Expr f000203 = Expr.makeNonEmptyListLiteral(new Expr[] {f000202});
  private static final Expr f000204 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000203, f000002);
  private static final Expr f000205 = Expr.makeApplication(f000009, new Expr[] {f000014});
  private static final Expr f000206 = Expr.makeLambda("_", f000205, f000204);
  private static final Expr f000207 = Expr.makeLambda("_", f000199, f000206);
  private static final Expr f000208 = Expr.makeEmptyListLiteral(f000201);
  private static final Expr f000209 =
      Expr.makeApplication(f000000, new Expr[] {f000199, f000014, f000201, f000207, f000208});
  private static final Expr f000210 = Expr.makeApplication(f000200, new Expr[] {f000209});
  private static final Expr f000211 = Expr.makeLambda("_", f000197, f000210);
  private static final Expr f000212 = Expr.makeLambda("_", f000017, f000211);
  private static final Expr f000213 = Expr.makeApplication(f000009, new Expr[] {f000199});
  private static final Expr f000214 = Expr.makeLambda("_", f000213, f000212);
  private static final Expr f000215 = Expr.makeFieldAccess(f000002, "bool");
  private static final Expr f000216 = Expr.makeApplication(f000215, new Expr[] {f000014});
  private static final Expr f000217 = Expr.makeLambda("_", f000197, f000216);
  private static final Expr f000218 = Expr.makeLambda("_", f000017, f000217);
  private static final Expr f000219 = Expr.makeLambda("_", f000001, f000218);
  private static final Expr f000220 = Expr.makeFieldAccess(f000002, "double");
  private static final Expr f000221 = Expr.makeApplication(f000220, new Expr[] {f000014});
  private static final Expr f000222 = Expr.makeLambda("_", f000197, f000221);
  private static final Expr f000223 = Expr.makeLambda("_", f000017, f000222);
  private static final Expr f000224 = Expr.makeLambda("_", f000190, f000223);
  private static final Expr f000225 = Expr.makeFieldAccess(f000002, "integer");
  private static final Expr f000226 = Expr.makeApplication(f000225, new Expr[] {f000014});
  private static final Expr f000227 = Expr.makeLambda("_", f000197, f000226);
  private static final Expr f000228 = Expr.makeLambda("_", f000017, f000227);
  private static final Expr f000229 = Expr.makeLambda("_", f000071, f000228);
  private static final Expr f000230 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000003),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000002)
          });
  private static final Expr f000231 = Expr.makeLambda("_", f000183, f000230);
  private static final Expr f000232 = Expr.makeLambda("_", f000183, f000231);
  private static final Expr f000233 = Expr.makeLambda("_", f000003, f000230);
  private static final Expr f000234 = Expr.makeLambda("_", f000183, f000233);
  private static final Expr f000235 = Expr.makeLambda("_", f000017, f000234);
  private static final Expr f000236 = Expr.makeApplication(f000081, new Expr[] {f000014});
  private static final Expr f000237 = Expr.makeApplication(f000225, new Expr[] {f000236});
  private static final Expr f000238 = Expr.makeLambda("_", f000197, f000237);
  private static final Expr f000239 = Expr.makeLambda("_", f000017, f000238);
  private static final Expr f000240 = Expr.makeLambda("_", f000178, f000239);
  private static final Expr f000241 = Expr.makeFieldAccess(f000002, "null");
  private static final Expr f000242 = Expr.makeLambda("_", f000197, f000241);
  private static final Expr f000243 = Expr.makeLambda("_", f000017, f000242);
  private static final Expr f000244 = Expr.makeFieldAccess(f000002, "object");
  private static final Expr f000245 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000199)
          });
  private static final Expr f000246 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000003)
          });
  private static final Expr f000247 = Expr.makeApplication(f000009, new Expr[] {f000246});
  private static final Expr f000248 = Expr.makeFieldAccess(f000003, "mapKey");
  private static final Expr f000249 = Expr.makeFieldAccess(f000003, "mapValue");
  private static final Expr f000250 = Expr.makeApplication(f000249, new Expr[] {f000025, f000014});
  private static final Expr f000251 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000248),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000250)
          });
  private static final Expr f000252 = Expr.makeNonEmptyListLiteral(new Expr[] {f000251});
  private static final Expr f000253 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000252, f000002);
  private static final Expr f000254 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000014)
          });
  private static final Expr f000255 = Expr.makeApplication(f000009, new Expr[] {f000254});
  private static final Expr f000256 = Expr.makeLambda("_", f000255, f000253);
  private static final Expr f000257 = Expr.makeLambda("_", f000245, f000256);
  private static final Expr f000258 = Expr.makeEmptyListLiteral(f000247);
  private static final Expr f000259 =
      Expr.makeApplication(f000000, new Expr[] {f000245, f000014, f000247, f000257, f000258});
  private static final Expr f000260 = Expr.makeApplication(f000244, new Expr[] {f000259});
  private static final Expr f000261 = Expr.makeLambda("_", f000197, f000260);
  private static final Expr f000262 = Expr.makeLambda("_", f000017, f000261);
  private static final Expr f000263 = Expr.makeApplication(f000009, new Expr[] {f000245});
  private static final Expr f000264 = Expr.makeLambda("_", f000263, f000262);
  private static final Expr f000265 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000001),
            new SimpleImmutableEntry<String, Expr>("value", f000003)
          });
  private static final Expr f000266 = Expr.makeFieldAccess(f000003, "array");
  private static final Expr f000267 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000001),
            new SimpleImmutableEntry<String, Expr>("value", f000014)
          });
  private static final Expr f000268 = Expr.makeFieldAccess(f000003, "value");
  private static final Expr f000269 = Expr.makeNonEmptyListLiteral(new Expr[] {f000268});
  private static final Expr f000270 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000269, f000002);
  private static final Expr f000271 = Expr.makeApplication(f000009, new Expr[] {f000025});
  private static final Expr f000272 = Expr.makeLambda("_", f000271, f000270);
  private static final Expr f000273 = Expr.makeLambda("_", f000267, f000272);
  private static final Expr f000274 = Expr.makeEmptyListLiteral(f000205);
  private static final Expr f000275 =
      Expr.makeApplication(f000000, new Expr[] {f000267, f000002, f000205, f000273, f000274});
  private static final Expr f000276 = Expr.makeApplication(f000266, new Expr[] {f000275});
  private static final Expr f000277 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000012),
            new SimpleImmutableEntry<String, Expr>("value", f000276)
          });
  private static final Expr f000278 = Expr.makeApplication(f000009, new Expr[] {f000265});
  private static final Expr f000279 = Expr.makeLambda("_", f000278, f000277);
  private static final Expr f000280 = Expr.makeFieldAccess(f000003, "bool");
  private static final Expr f000281 = Expr.makeApplication(f000280, new Expr[] {f000002});
  private static final Expr f000282 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000012),
            new SimpleImmutableEntry<String, Expr>("value", f000281)
          });
  private static final Expr f000283 = Expr.makeLambda("_", f000001, f000282);
  private static final Expr f000284 = Expr.makeFieldAccess(f000003, "double");
  private static final Expr f000285 = Expr.makeApplication(f000284, new Expr[] {f000002});
  private static final Expr f000286 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000012),
            new SimpleImmutableEntry<String, Expr>("value", f000285)
          });
  private static final Expr f000287 = Expr.makeLambda("_", f000190, f000286);
  private static final Expr f000288 = Expr.makeFieldAccess(f000003, "integer");
  private static final Expr f000289 = Expr.makeApplication(f000288, new Expr[] {f000002});
  private static final Expr f000290 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000012),
            new SimpleImmutableEntry<String, Expr>("value", f000289)
          });
  private static final Expr f000291 = Expr.makeLambda("_", f000071, f000290);
  private static final Expr f000292 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000007),
            new SimpleImmutableEntry<String, Expr>("value", f000241)
          });
  private static final Expr f000293 = Expr.makeFieldAccess(f000003, "object");
  private static final Expr f000294 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000267)
          });
  private static final Expr f000295 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000025)
          });
  private static final Expr f000296 = Expr.makeFieldAccess(f000002, "mapValue");
  private static final Expr f000297 = Expr.makeFieldAccess(f000296, "isNull");
  private static final Expr f000298 = Expr.makeApplication(f000009, new Expr[] {f000295});
  private static final Expr f000299 = Expr.makeEmptyListLiteral(f000298);
  private static final Expr f000300 = Expr.makeProjection(f000002, new String[] {"mapKey"});
  private static final Expr f000301 = Expr.makeFieldAccess(f000296, "value");
  private static final Expr f000302 =
      Expr.makeRecordLiteral(
          new Entry[] {new SimpleImmutableEntry<String, Expr>("mapValue", f000301)});
  private static final Expr f000303 =
      Expr.makeOperatorApplication(Operator.COMBINE, f000300, f000302);
  private static final Expr f000304 = Expr.makeNonEmptyListLiteral(new Expr[] {f000303});
  private static final Expr f000305 = Expr.makeIf(f000297, f000299, f000304);
  private static final Expr f000306 = Expr.makeNonEmptyListLiteral(new Expr[] {f000003});
  private static final Expr f000307 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000306, f000002);
  private static final Expr f000308 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000052)
          });
  private static final Expr f000309 = Expr.makeApplication(f000009, new Expr[] {f000308});
  private static final Expr f000310 = Expr.makeLambda("_", f000309, f000307);
  private static final Expr f000311 = Expr.makeLambda("_", f000295, f000310);
  private static final Expr f000312 =
      Expr.makeApplication(f000000, new Expr[] {f000295, f000305, f000298, f000311});
  private static final Expr f000313 = Expr.makeLambda("_", f000294, f000312);
  private static final Expr f000314 = Expr.makeEmptyListLiteral(f000255);
  private static final Expr f000315 =
      Expr.makeApplication(f000000, new Expr[] {f000294, f000002, f000255, f000313, f000314});
  private static final Expr f000316 = Expr.makeApplication(f000293, new Expr[] {f000315});
  private static final Expr f000317 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000012),
            new SimpleImmutableEntry<String, Expr>("value", f000316)
          });
  private static final Expr f000318 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000265)
          });
  private static final Expr f000319 = Expr.makeApplication(f000009, new Expr[] {f000318});
  private static final Expr f000320 = Expr.makeLambda("_", f000319, f000317);
  private static final Expr f000321 = Expr.makeFieldAccess(f000003, "string");
  private static final Expr f000322 = Expr.makeApplication(f000321, new Expr[] {f000002});
  private static final Expr f000323 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000012),
            new SimpleImmutableEntry<String, Expr>("value", f000322)
          });
  private static final Expr f000324 = Expr.makeLambda("_", f000183, f000323);
  private static final Expr f000325 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000279),
            new SimpleImmutableEntry<String, Expr>("bool", f000283),
            new SimpleImmutableEntry<String, Expr>("double", f000287),
            new SimpleImmutableEntry<String, Expr>("integer", f000291),
            new SimpleImmutableEntry<String, Expr>("null", f000292),
            new SimpleImmutableEntry<String, Expr>("object", f000320),
            new SimpleImmutableEntry<String, Expr>("string", f000324)
          });
  private static final Expr f000326 = Expr.makeApplication(f000014, new Expr[] {f000265, f000325});
  private static final Expr f000327 = Expr.makeFieldAccess(f000326, "value");
  private static final Expr f000328 = Expr.makeLambda("_", f000197, f000327);
  private static final Expr f000329 = Expr.makeLambda("_", f000017, f000328);
  private static final Expr f000330 = Expr.makeLambda("_", f000199, f000329);
  private static final Expr f000331 = Expr.makeApplication(f000009, new Expr[] {f000183});
  private static final Expr f000332 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000183),
            new SimpleImmutableEntry<String, Expr>("tail", f000331)
          });
  private static final Expr f000333 = Expr.makeLambda("_", f000332, f000002);
  private static final Expr f000334 = Expr.makeEmptyListLiteral(f000331);
  private static final Expr f000335 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000002),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000336 = Expr.makeLambda("_", f000183, f000335);
  private static final Expr f000337 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000333),
            new SimpleImmutableEntry<String, Expr>("Simple", f000336)
          });
  private static final Expr f000338 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000332),
            new SimpleImmutableEntry<String, Expr>("Simple", f000183)
          });
  private static final Expr f000339 = Expr.makeFieldAccess(f000338, "Simple");
  private static final Expr f000340 = Expr.makeTextLiteral("[]");
  private static final Expr f000341 = Expr.makeApplication(f000339, new Expr[] {f000340});
  private static final Expr f000342 = Expr.makeFieldAccess(f000338, "Complex");
  private static final Expr f000343 = Expr.makeFieldAccess(f000002, "head");
  private static final Expr f000344 = Expr.makeMerge(f000337, f000343, null);
  private static final Expr f000345 = Expr.makeFieldAccess(f000002, "tail");
  private static final Expr f000346 = Expr.makeApplication(f000009, new Expr[] {f000332});
  private static final Expr f000347 = Expr.makeMerge(f000337, f000003, null);
  private static final Expr f000348 = Expr.makeNonEmptyListLiteral(new Expr[] {f000347});
  private static final Expr f000349 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000348, f000002);
  private static final Expr f000350 = Expr.makeLambda("_", f000346, f000349);
  private static final Expr f000351 = Expr.makeLambda("_", f000338, f000350);
  private static final Expr f000352 = Expr.makeEmptyListLiteral(f000346);
  private static final Expr f000353 =
      Expr.makeApplication(f000000, new Expr[] {f000338, f000345, f000346, f000351, f000352});
  private static final Expr f000354 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000344),
            new SimpleImmutableEntry<String, Expr>("tail", f000353)
          });
  private static final Expr f000355 = Expr.makeFieldAccess(f000003, "head");
  private static final Expr f000356 = Expr.makeMerge(f000337, f000355, null);
  private static final Expr f000357 = Expr.makeFieldAccess(f000356, "head");
  private static final Expr f000358 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000357});
  private static final Expr f000359 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000358),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000360 = Expr.makeFieldAccess(f000014, "head");
  private static final Expr f000361 = Expr.makeMerge(f000337, f000360, null);
  private static final Expr f000362 = Expr.makeFieldAccess(f000002, "init");
  private static final Expr f000363 = Expr.makeFieldAccess(f000002, "last");
  private static final Expr f000364 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000363});
  private static final Expr f000365 = Expr.makeNonEmptyListLiteral(new Expr[] {f000364});
  private static final Expr f000366 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000362, f000365);
  private static final Expr f000367 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("tail", f000366)});
  private static final Expr f000368 =
      Expr.makeOperatorApplication(Operator.PREFER, f000361, f000367);
  private static final Expr f000369 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("init", f000331),
            new SimpleImmutableEntry<String, Expr>("last", f000183)
          });
  private static final Expr f000370 = Expr.makeLambda("_", f000369, f000368);
  private static final Expr f000371 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000359),
            new SimpleImmutableEntry<String, Expr>("Some", f000370)
          });
  private static final Expr f000372 = Expr.makeFieldAccess(f000356, "tail");
  private static final Expr f000373 = Expr.makeBuiltIn("Optional");
  private static final Expr f000374 = Expr.makeApplication(f000373, new Expr[] {f000369});
  private static final Expr f000375 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("init", f000334),
            new SimpleImmutableEntry<String, Expr>("last", f000003)
          });
  private static final Expr f000376 = Expr.makeApplication(f000175, new Expr[] {f000375});
  private static final Expr f000377 = Expr.makeNonEmptyListLiteral(new Expr[] {f000014});
  private static final Expr f000378 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000377, f000362);
  private static final Expr f000379 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("init", f000378)});
  private static final Expr f000380 =
      Expr.makeOperatorApplication(Operator.PREFER, f000002, f000379);
  private static final Expr f000381 = Expr.makeApplication(f000175, new Expr[] {f000380});
  private static final Expr f000382 = Expr.makeLambda("_", f000369, f000381);
  private static final Expr f000383 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000376),
            new SimpleImmutableEntry<String, Expr>("Some", f000382)
          });
  private static final Expr f000384 = Expr.makeMerge(f000383, f000002, null);
  private static final Expr f000385 = Expr.makeLambda("_", f000374, f000384);
  private static final Expr f000386 = Expr.makeLambda("_", f000183, f000385);
  private static final Expr f000387 = Expr.makeApplication(f000177, new Expr[] {f000369});
  private static final Expr f000388 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000372, f000374, f000386, f000387});
  private static final Expr f000389 = Expr.makeMerge(f000371, f000388, null);
  private static final Expr f000390 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000355});
  private static final Expr f000391 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000390),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000392 =
      Expr.makeOperatorApplication(Operator.PREFER, f000014, f000367);
  private static final Expr f000393 = Expr.makeLambda("_", f000369, f000392);
  private static final Expr f000394 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000391),
            new SimpleImmutableEntry<String, Expr>("Some", f000393)
          });
  private static final Expr f000395 = Expr.makeFieldAccess(f000003, "tail");
  private static final Expr f000396 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000395, f000374, f000386, f000387});
  private static final Expr f000397 = Expr.makeMerge(f000394, f000396, null);
  private static final Expr f000398 = Expr.makeNonEmptyListLiteral(new Expr[] {f000397});
  private static final Expr f000399 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000398, f000002);
  private static final Expr f000400 = Expr.makeLambda("_", f000346, f000399);
  private static final Expr f000401 = Expr.makeLambda("_", f000332, f000400);
  private static final Expr f000402 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000362, f000346, f000401, f000352});
  private static final Expr f000403 = Expr.makeNonEmptyListLiteral(new Expr[] {f000363});
  private static final Expr f000404 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000402, f000403);
  private static final Expr f000405 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000389),
            new SimpleImmutableEntry<String, Expr>("tail", f000404)
          });
  private static final Expr f000406 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("init", f000346),
            new SimpleImmutableEntry<String, Expr>("last", f000332)
          });
  private static final Expr f000407 = Expr.makeLambda("_", f000406, f000405);
  private static final Expr f000408 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000354),
            new SimpleImmutableEntry<String, Expr>("Some", f000407)
          });
  private static final Expr f000409 = Expr.makeApplication(f000373, new Expr[] {f000406});
  private static final Expr f000410 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("init", f000352),
            new SimpleImmutableEntry<String, Expr>("last", f000003)
          });
  private static final Expr f000411 = Expr.makeApplication(f000175, new Expr[] {f000410});
  private static final Expr f000412 = Expr.makeLambda("_", f000406, f000381);
  private static final Expr f000413 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000411),
            new SimpleImmutableEntry<String, Expr>("Some", f000412)
          });
  private static final Expr f000414 = Expr.makeMerge(f000413, f000002, null);
  private static final Expr f000415 = Expr.makeLambda("_", f000409, f000414);
  private static final Expr f000416 = Expr.makeLambda("_", f000332, f000415);
  private static final Expr f000417 = Expr.makeApplication(f000177, new Expr[] {f000406});
  private static final Expr f000418 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000353, f000409, f000416, f000417});
  private static final Expr f000419 = Expr.makeMerge(f000408, f000418, null);
  private static final Expr f000420 = Expr.makeFieldAccess(f000419, "head");
  private static final Expr f000421 = Expr.makeFieldAccess(f000420, "head");
  private static final Expr f000422 =
      Expr.makeTextLiteral(new String[] {"[ ", " ]"}, new Expr[] {f000421});
  private static final Expr f000423 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000422),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000424 = Expr.makeTextLiteral("[");
  private static final Expr f000425 =
      Expr.makeApplication(f000000, new Expr[] {f000338, f000395, f000346, f000351, f000352});
  private static final Expr f000426 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000356),
            new SimpleImmutableEntry<String, Expr>("tail", f000425)
          });
  private static final Expr f000427 = Expr.makeFieldAccess(f000361, "head");
  private static final Expr f000428 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000427});
  private static final Expr f000429 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000428),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000430 = Expr.makeFieldAccess(f000025, "head");
  private static final Expr f000431 = Expr.makeMerge(f000337, f000430, null);
  private static final Expr f000432 =
      Expr.makeOperatorApplication(Operator.PREFER, f000431, f000367);
  private static final Expr f000433 = Expr.makeLambda("_", f000369, f000432);
  private static final Expr f000434 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000429),
            new SimpleImmutableEntry<String, Expr>("Some", f000433)
          });
  private static final Expr f000435 = Expr.makeFieldAccess(f000361, "tail");
  private static final Expr f000436 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000435, f000374, f000386, f000387});
  private static final Expr f000437 = Expr.makeMerge(f000434, f000436, null);
  private static final Expr f000438 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000437),
            new SimpleImmutableEntry<String, Expr>("tail", f000404)
          });
  private static final Expr f000439 = Expr.makeLambda("_", f000406, f000438);
  private static final Expr f000440 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000426),
            new SimpleImmutableEntry<String, Expr>("Some", f000439)
          });
  private static final Expr f000441 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000425, f000409, f000416, f000417});
  private static final Expr f000442 = Expr.makeMerge(f000440, f000441, null);
  private static final Expr f000443 = Expr.makeFieldAccess(f000442, "head");
  private static final Expr f000444 = Expr.makeFieldAccess(f000443, "head");
  private static final Expr f000445 = Expr.makeNonEmptyListLiteral(new Expr[] {f000444});
  private static final Expr f000446 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000445, f000362);
  private static final Expr f000447 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000446, f000403);
  private static final Expr f000448 =
      Expr.makeTextLiteral(new String[] {"  ", ""}, new Expr[] {f000003});
  private static final Expr f000449 = Expr.makeNonEmptyListLiteral(new Expr[] {f000448});
  private static final Expr f000450 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000449, f000002);
  private static final Expr f000451 = Expr.makeLambda("_", f000331, f000450);
  private static final Expr f000452 = Expr.makeLambda("_", f000183, f000451);
  private static final Expr f000453 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000447, f000331, f000452, f000334});
  private static final Expr f000454 = Expr.makeTextLiteral("]");
  private static final Expr f000455 = Expr.makeNonEmptyListLiteral(new Expr[] {f000454});
  private static final Expr f000456 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000453, f000455);
  private static final Expr f000457 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000424),
            new SimpleImmutableEntry<String, Expr>("tail", f000456)
          });
  private static final Expr f000458 = Expr.makeLambda("_", f000369, f000457);
  private static final Expr f000459 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000423),
            new SimpleImmutableEntry<String, Expr>("Some", f000458)
          });
  private static final Expr f000460 = Expr.makeFieldAccess(f000420, "tail");
  private static final Expr f000461 = Expr.makeFieldAccess(f000419, "tail");
  private static final Expr f000462 = Expr.makeNonEmptyListLiteral(new Expr[] {f000343});
  private static final Expr f000463 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000462, f000345);
  private static final Expr f000464 = Expr.makeLambda("_", f000331, f000307);
  private static final Expr f000465 = Expr.makeLambda("_", f000183, f000464);
  private static final Expr f000466 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000463, f000331, f000465});
  private static final Expr f000467 = Expr.makeLambda("_", f000332, f000466);
  private static final Expr f000468 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000461, f000331, f000467, f000334});
  private static final Expr f000469 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000460, f000468);
  private static final Expr f000470 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000469, f000374, f000386, f000387});
  private static final Expr f000471 = Expr.makeMerge(f000459, f000470, null);
  private static final Expr f000472 = Expr.makeApplication(f000342, new Expr[] {f000471});
  private static final Expr f000473 = Expr.makeApplication(f000009, new Expr[] {f000338});
  private static final Expr f000474 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000338),
            new SimpleImmutableEntry<String, Expr>("tail", f000473)
          });
  private static final Expr f000475 = Expr.makeLambda("_", f000474, f000472);
  private static final Expr f000476 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000341),
            new SimpleImmutableEntry<String, Expr>("Some", f000475)
          });
  private static final Expr f000477 = Expr.makeBuiltIn("List/reverse");
  private static final Expr f000478 = Expr.makeApplication(f000477, new Expr[] {f000338, f000002});
  private static final Expr f000479 = Expr.makeApplication(f000373, new Expr[] {f000474});
  private static final Expr f000480 = Expr.makeEmptyListLiteral(f000473);
  private static final Expr f000481 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000003),
            new SimpleImmutableEntry<String, Expr>("tail", f000480)
          });
  private static final Expr f000482 = Expr.makeApplication(f000175, new Expr[] {f000481});
  private static final Expr f000483 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000345, f000377);
  private static final Expr f000484 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("tail", f000483)});
  private static final Expr f000485 =
      Expr.makeOperatorApplication(Operator.PREFER, f000002, f000484);
  private static final Expr f000486 = Expr.makeApplication(f000175, new Expr[] {f000485});
  private static final Expr f000487 = Expr.makeLambda("_", f000474, f000486);
  private static final Expr f000488 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000482),
            new SimpleImmutableEntry<String, Expr>("Some", f000487)
          });
  private static final Expr f000489 = Expr.makeMerge(f000488, f000002, null);
  private static final Expr f000490 = Expr.makeLambda("_", f000479, f000489);
  private static final Expr f000491 = Expr.makeLambda("_", f000338, f000490);
  private static final Expr f000492 = Expr.makeApplication(f000177, new Expr[] {f000474});
  private static final Expr f000493 =
      Expr.makeApplication(f000000, new Expr[] {f000338, f000478, f000479, f000491, f000492});
  private static final Expr f000494 = Expr.makeMerge(f000476, f000493, null);
  private static final Expr f000495 = Expr.makeLambda("_", f000473, f000494);
  private static final Expr f000496 = Expr.makeTextLiteral("true");
  private static final Expr f000497 = Expr.makeTextLiteral("false");
  private static final Expr f000498 = Expr.makeIf(f000002, f000496, f000497);
  private static final Expr f000499 = Expr.makeApplication(f000339, new Expr[] {f000498});
  private static final Expr f000500 = Expr.makeLambda("_", f000001, f000499);
  private static final Expr f000501 = Expr.makeApplication(f000048, new Expr[] {f000002});
  private static final Expr f000502 = Expr.makeApplication(f000339, new Expr[] {f000501});
  private static final Expr f000503 = Expr.makeLambda("_", f000190, f000502);
  private static final Expr f000504 = Expr.makeBuiltIn("Natural/show");
  private static final Expr f000505 = Expr.makeApplication(f000504, new Expr[] {f000065});
  private static final Expr f000506 = Expr.makeApplication(f000155, new Expr[] {f000002});
  private static final Expr f000507 = Expr.makeIf(f000119, f000505, f000506);
  private static final Expr f000508 = Expr.makeApplication(f000339, new Expr[] {f000507});
  private static final Expr f000509 = Expr.makeLambda("_", f000071, f000508);
  private static final Expr f000510 = Expr.makeTextLiteral("null");
  private static final Expr f000511 = Expr.makeApplication(f000339, new Expr[] {f000510});
  private static final Expr f000512 = Expr.makeTextLiteral("{}");
  private static final Expr f000513 = Expr.makeApplication(f000339, new Expr[] {f000512});
  private static final Expr f000514 = Expr.makeFieldAccess(f000343, "mapValue");
  private static final Expr f000515 = Expr.makeMerge(f000337, f000514, null);
  private static final Expr f000516 = Expr.makeBuiltIn("Text/show");
  private static final Expr f000517 = Expr.makeFieldAccess(f000343, "mapKey");
  private static final Expr f000518 = Expr.makeApplication(f000516, new Expr[] {f000517});
  private static final Expr f000519 = Expr.makeFieldAccess(f000515, "head");
  private static final Expr f000520 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000518, f000519});
  private static final Expr f000521 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000520)});
  private static final Expr f000522 =
      Expr.makeOperatorApplication(Operator.PREFER, f000515, f000521);
  private static final Expr f000523 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000338)
          });
  private static final Expr f000524 = Expr.makeMerge(f000337, f000249, null);
  private static final Expr f000525 = Expr.makeApplication(f000516, new Expr[] {f000248});
  private static final Expr f000526 = Expr.makeFieldAccess(f000524, "head");
  private static final Expr f000527 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000525, f000526});
  private static final Expr f000528 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000527)});
  private static final Expr f000529 =
      Expr.makeOperatorApplication(Operator.PREFER, f000524, f000528);
  private static final Expr f000530 = Expr.makeNonEmptyListLiteral(new Expr[] {f000529});
  private static final Expr f000531 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000530, f000002);
  private static final Expr f000532 = Expr.makeLambda("_", f000346, f000531);
  private static final Expr f000533 = Expr.makeLambda("_", f000523, f000532);
  private static final Expr f000534 =
      Expr.makeApplication(f000000, new Expr[] {f000523, f000345, f000346, f000533, f000352});
  private static final Expr f000535 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000522),
            new SimpleImmutableEntry<String, Expr>("tail", f000534)
          });
  private static final Expr f000536 = Expr.makeFieldAccess(f000355, "mapKey");
  private static final Expr f000537 = Expr.makeApplication(f000516, new Expr[] {f000536});
  private static final Expr f000538 = Expr.makeFieldAccess(f000355, "mapValue");
  private static final Expr f000539 = Expr.makeMerge(f000337, f000538, null);
  private static final Expr f000540 = Expr.makeFieldAccess(f000539, "head");
  private static final Expr f000541 =
      Expr.makeTextLiteral(new String[] {"", ": ", ","}, new Expr[] {f000537, f000540});
  private static final Expr f000542 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000541),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000543 = Expr.makeFieldAccess(f000360, "mapValue");
  private static final Expr f000544 = Expr.makeMerge(f000337, f000543, null);
  private static final Expr f000545 = Expr.makeFieldAccess(f000360, "mapKey");
  private static final Expr f000546 = Expr.makeApplication(f000516, new Expr[] {f000545});
  private static final Expr f000547 = Expr.makeFieldAccess(f000544, "head");
  private static final Expr f000548 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000546, f000547});
  private static final Expr f000549 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000548)});
  private static final Expr f000550 =
      Expr.makeOperatorApplication(Operator.PREFER, f000544, f000549);
  private static final Expr f000551 =
      Expr.makeOperatorApplication(Operator.PREFER, f000550, f000367);
  private static final Expr f000552 = Expr.makeLambda("_", f000369, f000551);
  private static final Expr f000553 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000542),
            new SimpleImmutableEntry<String, Expr>("Some", f000552)
          });
  private static final Expr f000554 = Expr.makeFieldAccess(f000539, "tail");
  private static final Expr f000555 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000554, f000374, f000386, f000387});
  private static final Expr f000556 = Expr.makeMerge(f000553, f000555, null);
  private static final Expr f000557 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000556),
            new SimpleImmutableEntry<String, Expr>("tail", f000404)
          });
  private static final Expr f000558 = Expr.makeLambda("_", f000406, f000557);
  private static final Expr f000559 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000535),
            new SimpleImmutableEntry<String, Expr>("Some", f000558)
          });
  private static final Expr f000560 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000534, f000409, f000416, f000417});
  private static final Expr f000561 = Expr.makeMerge(f000559, f000560, null);
  private static final Expr f000562 = Expr.makeFieldAccess(f000561, "head");
  private static final Expr f000563 = Expr.makeFieldAccess(f000562, "head");
  private static final Expr f000564 =
      Expr.makeTextLiteral(new String[] {"{ ", " }"}, new Expr[] {f000563});
  private static final Expr f000565 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000564),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000566 = Expr.makeTextLiteral("{");
  private static final Expr f000567 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000537, f000540});
  private static final Expr f000568 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000567)});
  private static final Expr f000569 =
      Expr.makeOperatorApplication(Operator.PREFER, f000539, f000568);
  private static final Expr f000570 =
      Expr.makeApplication(f000000, new Expr[] {f000523, f000395, f000346, f000533, f000352});
  private static final Expr f000571 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000569),
            new SimpleImmutableEntry<String, Expr>("tail", f000570)
          });
  private static final Expr f000572 =
      Expr.makeTextLiteral(new String[] {"", ": ", ","}, new Expr[] {f000546, f000547});
  private static final Expr f000573 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000572),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000574 = Expr.makeFieldAccess(f000430, "mapValue");
  private static final Expr f000575 = Expr.makeMerge(f000337, f000574, null);
  private static final Expr f000576 = Expr.makeFieldAccess(f000430, "mapKey");
  private static final Expr f000577 = Expr.makeApplication(f000516, new Expr[] {f000576});
  private static final Expr f000578 = Expr.makeFieldAccess(f000575, "head");
  private static final Expr f000579 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000577, f000578});
  private static final Expr f000580 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000579)});
  private static final Expr f000581 =
      Expr.makeOperatorApplication(Operator.PREFER, f000575, f000580);
  private static final Expr f000582 =
      Expr.makeOperatorApplication(Operator.PREFER, f000581, f000367);
  private static final Expr f000583 = Expr.makeLambda("_", f000369, f000582);
  private static final Expr f000584 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000573),
            new SimpleImmutableEntry<String, Expr>("Some", f000583)
          });
  private static final Expr f000585 = Expr.makeFieldAccess(f000544, "tail");
  private static final Expr f000586 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000585, f000374, f000386, f000387});
  private static final Expr f000587 = Expr.makeMerge(f000584, f000586, null);
  private static final Expr f000588 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000587),
            new SimpleImmutableEntry<String, Expr>("tail", f000404)
          });
  private static final Expr f000589 = Expr.makeLambda("_", f000406, f000588);
  private static final Expr f000590 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000571),
            new SimpleImmutableEntry<String, Expr>("Some", f000589)
          });
  private static final Expr f000591 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000570, f000409, f000416, f000417});
  private static final Expr f000592 = Expr.makeMerge(f000590, f000591, null);
  private static final Expr f000593 = Expr.makeFieldAccess(f000592, "head");
  private static final Expr f000594 = Expr.makeFieldAccess(f000593, "head");
  private static final Expr f000595 = Expr.makeNonEmptyListLiteral(new Expr[] {f000594});
  private static final Expr f000596 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000595, f000362);
  private static final Expr f000597 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000596, f000403);
  private static final Expr f000598 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000597, f000331, f000452, f000334});
  private static final Expr f000599 = Expr.makeTextLiteral("}");
  private static final Expr f000600 = Expr.makeNonEmptyListLiteral(new Expr[] {f000599});
  private static final Expr f000601 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000598, f000600);
  private static final Expr f000602 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000566),
            new SimpleImmutableEntry<String, Expr>("tail", f000601)
          });
  private static final Expr f000603 = Expr.makeLambda("_", f000369, f000602);
  private static final Expr f000604 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000565),
            new SimpleImmutableEntry<String, Expr>("Some", f000603)
          });
  private static final Expr f000605 = Expr.makeFieldAccess(f000562, "tail");
  private static final Expr f000606 = Expr.makeFieldAccess(f000561, "tail");
  private static final Expr f000607 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000606, f000331, f000467, f000334});
  private static final Expr f000608 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000605, f000607);
  private static final Expr f000609 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000608, f000374, f000386, f000387});
  private static final Expr f000610 = Expr.makeMerge(f000604, f000609, null);
  private static final Expr f000611 = Expr.makeApplication(f000342, new Expr[] {f000610});
  private static final Expr f000612 = Expr.makeApplication(f000009, new Expr[] {f000523});
  private static final Expr f000613 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000523),
            new SimpleImmutableEntry<String, Expr>("tail", f000612)
          });
  private static final Expr f000614 = Expr.makeLambda("_", f000613, f000611);
  private static final Expr f000615 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000513),
            new SimpleImmutableEntry<String, Expr>("Some", f000614)
          });
  private static final Expr f000616 = Expr.makeApplication(f000477, new Expr[] {f000523, f000002});
  private static final Expr f000617 = Expr.makeApplication(f000373, new Expr[] {f000613});
  private static final Expr f000618 = Expr.makeEmptyListLiteral(f000612);
  private static final Expr f000619 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000003),
            new SimpleImmutableEntry<String, Expr>("tail", f000618)
          });
  private static final Expr f000620 = Expr.makeApplication(f000175, new Expr[] {f000619});
  private static final Expr f000621 = Expr.makeLambda("_", f000613, f000486);
  private static final Expr f000622 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000620),
            new SimpleImmutableEntry<String, Expr>("Some", f000621)
          });
  private static final Expr f000623 = Expr.makeMerge(f000622, f000002, null);
  private static final Expr f000624 = Expr.makeLambda("_", f000617, f000623);
  private static final Expr f000625 = Expr.makeLambda("_", f000523, f000624);
  private static final Expr f000626 = Expr.makeApplication(f000177, new Expr[] {f000613});
  private static final Expr f000627 =
      Expr.makeApplication(f000000, new Expr[] {f000523, f000616, f000617, f000625, f000626});
  private static final Expr f000628 = Expr.makeMerge(f000615, f000627, null);
  private static final Expr f000629 = Expr.makeLambda("_", f000612, f000628);
  private static final Expr f000630 = Expr.makeApplication(f000516, new Expr[] {f000002});
  private static final Expr f000631 = Expr.makeApplication(f000339, new Expr[] {f000630});
  private static final Expr f000632 = Expr.makeLambda("_", f000183, f000631);
  private static final Expr f000633 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000495),
            new SimpleImmutableEntry<String, Expr>("bool", f000500),
            new SimpleImmutableEntry<String, Expr>("double", f000503),
            new SimpleImmutableEntry<String, Expr>("integer", f000509),
            new SimpleImmutableEntry<String, Expr>("null", f000511),
            new SimpleImmutableEntry<String, Expr>("object", f000629),
            new SimpleImmutableEntry<String, Expr>("string", f000632)
          });
  private static final Expr f000634 = Expr.makeApplication(f000002, new Expr[] {f000338, f000633});
  private static final Expr f000635 = Expr.makeMerge(f000337, f000634, null);
  private static final Expr f000636 = Expr.makeFieldAccess(f000635, "head");
  private static final Expr f000637 = Expr.makeNonEmptyListLiteral(new Expr[] {f000636});
  private static final Expr f000638 = Expr.makeFieldAccess(f000635, "tail");
  private static final Expr f000639 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000637, f000638);
  private static final Expr f000640 =
      Expr.makeTextLiteral(new String[] {"", "\n", ""}, new Expr[] {f000003, f000002});
  private static final Expr f000641 = Expr.makeLambda("_", f000183, f000640);
  private static final Expr f000642 = Expr.makeLambda("_", f000183, f000641);
  private static final Expr f000643 = Expr.makeTextLiteral("");
  private static final Expr f000644 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000639, f000183, f000642, f000643});
  private static final Expr f000645 = Expr.makeLambda("_", f000199, f000644);
  private static final Expr f000646 = Expr.makeLambda("_", f000071, f000507);
  private static final Expr f000647 = Expr.makeFieldAccess(f000344, "head");
  private static final Expr f000648 =
      Expr.makeTextLiteral(new String[] {"- ", ""}, new Expr[] {f000647});
  private static final Expr f000649 = Expr.makeFieldAccess(f000344, "tail");
  private static final Expr f000650 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000649, f000331, f000452, f000334});
  private static final Expr f000651 = Expr.makeFieldAccess(f000347, "tail");
  private static final Expr f000652 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000651, f000331, f000452, f000334});
  private static final Expr f000653 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("tail", f000652)});
  private static final Expr f000654 =
      Expr.makeOperatorApplication(Operator.PREFER, f000347, f000653);
  private static final Expr f000655 = Expr.makeFieldAccess(f000347, "head");
  private static final Expr f000656 =
      Expr.makeTextLiteral(new String[] {"- ", ""}, new Expr[] {f000655});
  private static final Expr f000657 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000656)});
  private static final Expr f000658 =
      Expr.makeOperatorApplication(Operator.PREFER, f000654, f000657);
  private static final Expr f000659 = Expr.makeNonEmptyListLiteral(new Expr[] {f000658});
  private static final Expr f000660 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000659, f000002);
  private static final Expr f000661 = Expr.makeLambda("_", f000346, f000660);
  private static final Expr f000662 = Expr.makeLambda("_", f000338, f000661);
  private static final Expr f000663 =
      Expr.makeApplication(f000000, new Expr[] {f000338, f000345, f000346, f000662, f000352});
  private static final Expr f000664 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000663, f000331, f000467, f000334});
  private static final Expr f000665 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000650, f000664);
  private static final Expr f000666 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000648),
            new SimpleImmutableEntry<String, Expr>("tail", f000665)
          });
  private static final Expr f000667 = Expr.makeApplication(f000342, new Expr[] {f000666});
  private static final Expr f000668 = Expr.makeLambda("_", f000474, f000667);
  private static final Expr f000669 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000341),
            new SimpleImmutableEntry<String, Expr>("Some", f000668)
          });
  private static final Expr f000670 = Expr.makeMerge(f000669, f000493, null);
  private static final Expr f000671 = Expr.makeLambda("_", f000473, f000670);
  private static final Expr f000672 =
      Expr.makeTextLiteral(new String[] {"", ":"}, new Expr[] {f000537});
  private static final Expr f000673 = Expr.makeNonEmptyListLiteral(new Expr[] {f000540});
  private static final Expr f000674 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000673, f000554);
  private static final Expr f000675 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000674, f000331, f000452, f000334});
  private static final Expr f000676 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000672),
            new SimpleImmutableEntry<String, Expr>("tail", f000675)
          });
  private static final Expr f000677 = Expr.makeLambda("_", f000332, f000676);
  private static final Expr f000678 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000537, f000002});
  private static final Expr f000679 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000678),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000680 = Expr.makeLambda("_", f000183, f000679);
  private static final Expr f000681 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000677),
            new SimpleImmutableEntry<String, Expr>("Simple", f000680)
          });
  private static final Expr f000682 = Expr.makeMerge(f000681, f000514, null);
  private static final Expr f000683 = Expr.makeFieldAccess(f000682, "head");
  private static final Expr f000684 = Expr.makeFieldAccess(f000682, "tail");
  private static final Expr f000685 = Expr.makeFieldAccess(f000014, "mapKey");
  private static final Expr f000686 = Expr.makeApplication(f000516, new Expr[] {f000685});
  private static final Expr f000687 =
      Expr.makeTextLiteral(new String[] {"", ":"}, new Expr[] {f000686});
  private static final Expr f000688 = Expr.makeFieldAccess(f000014, "mapValue");
  private static final Expr f000689 = Expr.makeMerge(f000337, f000688, null);
  private static final Expr f000690 = Expr.makeFieldAccess(f000689, "head");
  private static final Expr f000691 = Expr.makeNonEmptyListLiteral(new Expr[] {f000690});
  private static final Expr f000692 = Expr.makeFieldAccess(f000689, "tail");
  private static final Expr f000693 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000691, f000692);
  private static final Expr f000694 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000693, f000331, f000452, f000334});
  private static final Expr f000695 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000687),
            new SimpleImmutableEntry<String, Expr>("tail", f000694)
          });
  private static final Expr f000696 = Expr.makeLambda("_", f000332, f000695);
  private static final Expr f000697 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000686, f000002});
  private static final Expr f000698 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000697),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000699 = Expr.makeLambda("_", f000183, f000698);
  private static final Expr f000700 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000696),
            new SimpleImmutableEntry<String, Expr>("Simple", f000699)
          });
  private static final Expr f000701 = Expr.makeMerge(f000700, f000249, null);
  private static final Expr f000702 = Expr.makeNonEmptyListLiteral(new Expr[] {f000701});
  private static final Expr f000703 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000702, f000002);
  private static final Expr f000704 = Expr.makeLambda("_", f000346, f000703);
  private static final Expr f000705 = Expr.makeLambda("_", f000523, f000704);
  private static final Expr f000706 =
      Expr.makeApplication(f000000, new Expr[] {f000523, f000345, f000346, f000705, f000352});
  private static final Expr f000707 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000706, f000331, f000467, f000334});
  private static final Expr f000708 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000684, f000707);
  private static final Expr f000709 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000683),
            new SimpleImmutableEntry<String, Expr>("tail", f000708)
          });
  private static final Expr f000710 = Expr.makeApplication(f000342, new Expr[] {f000709});
  private static final Expr f000711 = Expr.makeLambda("_", f000613, f000710);
  private static final Expr f000712 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000513),
            new SimpleImmutableEntry<String, Expr>("Some", f000711)
          });
  private static final Expr f000713 = Expr.makeMerge(f000712, f000627, null);
  private static final Expr f000714 = Expr.makeLambda("_", f000612, f000713);
  private static final Expr f000715 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000671),
            new SimpleImmutableEntry<String, Expr>("bool", f000500),
            new SimpleImmutableEntry<String, Expr>("double", f000503),
            new SimpleImmutableEntry<String, Expr>("integer", f000509),
            new SimpleImmutableEntry<String, Expr>("null", f000511),
            new SimpleImmutableEntry<String, Expr>("object", f000714),
            new SimpleImmutableEntry<String, Expr>("string", f000632)
          });
  private static final Expr f000716 = Expr.makeApplication(f000002, new Expr[] {f000338, f000715});
  private static final Expr f000717 = Expr.makeMerge(f000337, f000716, null);
  private static final Expr f000718 = Expr.makeFieldAccess(f000717, "head");
  private static final Expr f000719 = Expr.makeNonEmptyListLiteral(new Expr[] {f000718});
  private static final Expr f000720 = Expr.makeFieldAccess(f000717, "tail");
  private static final Expr f000721 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000719, f000720);
  private static final Expr f000722 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000721, f000183, f000642, f000643});
  private static final Expr f000723 = Expr.makeLambda("_", f000199, f000722);
  private static final Expr f000724 = Expr.makeFieldAccess(f000002, "string");
  private static final Expr f000725 = Expr.makeApplication(f000724, new Expr[] {f000014});
  private static final Expr f000726 = Expr.makeLambda("_", f000197, f000725);
  private static final Expr f000727 = Expr.makeLambda("_", f000017, f000726);
  private static final Expr f000728 = Expr.makeLambda("_", f000183, f000727);
  private static final Expr f000729 = Expr.makeFieldAccess(f000184, "Inline");
  private static final Expr f000730 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("contents", f000002),
            new SimpleImmutableEntry<String, Expr>("field", f000014),
            new SimpleImmutableEntry<String, Expr>("nesting", f000729)
          });
  private static final Expr f000731 = Expr.makeLambda("_", f000002, f000730);
  private static final Expr f000732 = Expr.makeLambda("_", f000017, f000731);
  private static final Expr f000733 = Expr.makeLambda("_", f000183, f000732);
  private static final Expr f000734 = Expr.makeFieldAccess(f000184, "Nested");
  private static final Expr f000735 = Expr.makeApplication(f000734, new Expr[] {f000025});
  private static final Expr f000736 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("contents", f000002),
            new SimpleImmutableEntry<String, Expr>("field", f000014),
            new SimpleImmutableEntry<String, Expr>("nesting", f000735)
          });
  private static final Expr f000737 = Expr.makeLambda("_", f000002, f000736);
  private static final Expr f000738 = Expr.makeLambda("_", f000017, f000737);
  private static final Expr f000739 = Expr.makeLambda("_", f000183, f000738);
  private static final Expr f000740 = Expr.makeLambda("_", f000183, f000739);
  private static final Expr f000741 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Nesting", f000184),
            new SimpleImmutableEntry<String, Expr>("Tagged", f000186),
            new SimpleImmutableEntry<String, Expr>("Type", f000199),
            new SimpleImmutableEntry<String, Expr>("array", f000214),
            new SimpleImmutableEntry<String, Expr>("bool", f000219),
            new SimpleImmutableEntry<String, Expr>("double", f000224),
            new SimpleImmutableEntry<String, Expr>("integer", f000229),
            new SimpleImmutableEntry<String, Expr>("keyText", f000232),
            new SimpleImmutableEntry<String, Expr>("keyValue", f000235),
            new SimpleImmutableEntry<String, Expr>("natural", f000240),
            new SimpleImmutableEntry<String, Expr>("null", f000243),
            new SimpleImmutableEntry<String, Expr>("number", f000224),
            new SimpleImmutableEntry<String, Expr>("object", f000264),
            new SimpleImmutableEntry<String, Expr>("omitNullFields", f000330),
            new SimpleImmutableEntry<String, Expr>("render", f000645),
            new SimpleImmutableEntry<String, Expr>("renderInteger", f000646),
            new SimpleImmutableEntry<String, Expr>("renderYAML", f000723),
            new SimpleImmutableEntry<String, Expr>("string", f000728),
            new SimpleImmutableEntry<String, Expr>("tagInline", f000733),
            new SimpleImmutableEntry<String, Expr>("tagNested", f000740)
          });
  private static final Expr f000742 = Expr.makeApplication(f000025, new Expr[] {f000003});
  private static final Expr f000743 = Expr.makeOperatorApplication(Operator.AND, f000742, f000002);
  private static final Expr f000744 = Expr.makeLambda("_", f000001, f000743);
  private static final Expr f000745 = Expr.makeLambda("_", f000014, f000744);
  private static final Expr f000746 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000001, f000745, f000007});
  private static final Expr f000747 = Expr.makeLambda("_", f000201, f000746);
  private static final Expr f000748 = Expr.makePi("_", f000002, f000001);
  private static final Expr f000749 = Expr.makeLambda("_", f000748, f000747);
  private static final Expr f000750 = Expr.makeLambda("_", f000017, f000749);
  private static final Expr f000751 = Expr.makeOperatorApplication(Operator.OR, f000742, f000002);
  private static final Expr f000752 = Expr.makeLambda("_", f000001, f000751);
  private static final Expr f000753 = Expr.makeLambda("_", f000014, f000752);
  private static final Expr f000754 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000001, f000753, f000012});
  private static final Expr f000755 = Expr.makeLambda("_", f000201, f000754);
  private static final Expr f000756 = Expr.makeLambda("_", f000748, f000755);
  private static final Expr f000757 = Expr.makeLambda("_", f000017, f000756);
  private static final Expr f000758 = Expr.makeBuiltIn("List/build");
  private static final Expr f000759 = Expr.makeApplication(f000009, new Expr[] {f000052});
  private static final Expr f000760 = Expr.makeLambda("_", f000759, f000307);
  private static final Expr f000761 = Expr.makeLambda("_", f000025, f000760);
  private static final Expr f000762 =
      Expr.makeApplication(f000000, new Expr[] {f000025, f000003, f000271, f000761, f000002});
  private static final Expr f000763 = Expr.makeLambda("_", f000205, f000762);
  private static final Expr f000764 = Expr.makeLambda("_", f000201, f000763);
  private static final Expr f000765 =
      Expr.makeApplication(f000000, new Expr[] {f000201, f000002, f000201, f000764, f000208});
  private static final Expr f000766 = Expr.makeApplication(f000009, new Expr[] {f000187});
  private static final Expr f000767 = Expr.makeLambda("_", f000766, f000765);
  private static final Expr f000768 = Expr.makeLambda("_", f000017, f000767);
  private static final Expr f000769 =
      Expr.makeApplication(f000000, new Expr[] {f000025, f000050, f000271, f000761});
  private static final Expr f000770 = Expr.makeLambda("_", f000025, f000769);
  private static final Expr f000771 =
      Expr.makeApplication(f000000, new Expr[] {f000025, f000002, f000205, f000770, f000274});
  private static final Expr f000772 = Expr.makeLambda("_", f000205, f000771);
  private static final Expr f000773 = Expr.makePi("_", f000003, f000201);
  private static final Expr f000774 = Expr.makeLambda("_", f000773, f000772);
  private static final Expr f000775 = Expr.makeLambda("_", f000017, f000774);
  private static final Expr f000776 = Expr.makeLambda("_", f000017, f000775);
  private static final Expr f000777 = Expr.makeLambda("_", f000201, f000002);
  private static final Expr f000778 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000208),
            new SimpleImmutableEntry<String, Expr>("Some", f000777)
          });
  private static final Expr f000779 = Expr.makeMerge(f000778, f000002, null);
  private static final Expr f000780 = Expr.makeApplication(f000373, new Expr[] {f000187});
  private static final Expr f000781 = Expr.makeLambda("_", f000780, f000779);
  private static final Expr f000782 = Expr.makeLambda("_", f000017, f000781);
  private static final Expr f000783 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000003)
          });
  private static final Expr f000784 = Expr.makeBuiltIn("List/indexed");
  private static final Expr f000785 = Expr.makeApplication(f000784, new Expr[] {f000003, f000002});
  private static final Expr f000786 = Expr.makeFieldAccess(f000003, "index");
  private static final Expr f000787 = Expr.makeApplication(f000076, new Expr[] {f000786, f000052});
  private static final Expr f000788 = Expr.makeApplication(f000063, new Expr[] {f000787});
  private static final Expr f000789 = Expr.makeIf(f000788, f000270, f000002);
  private static final Expr f000790 = Expr.makeLambda("_", f000205, f000789);
  private static final Expr f000791 = Expr.makeLambda("_", f000783, f000790);
  private static final Expr f000792 =
      Expr.makeApplication(f000000, new Expr[] {f000783, f000785, f000201, f000791, f000208});
  private static final Expr f000793 = Expr.makeLambda("_", f000187, f000792);
  private static final Expr f000794 = Expr.makeLambda("_", f000017, f000793);
  private static final Expr f000795 = Expr.makeLambda("_", f000178, f000794);
  private static final Expr f000796 = Expr.makeEmptyListLiteral(f000187);
  private static final Expr f000797 = Expr.makeLambda("_", f000017, f000796);
  private static final Expr f000798 = Expr.makeIf(f000742, f000307, f000002);
  private static final Expr f000799 = Expr.makeLambda("_", f000271, f000798);
  private static final Expr f000800 = Expr.makeLambda("_", f000014, f000799);
  private static final Expr f000801 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000205, f000800, f000274});
  private static final Expr f000802 = Expr.makeLambda("_", f000201, f000801);
  private static final Expr f000803 = Expr.makeLambda("_", f000748, f000802);
  private static final Expr f000804 = Expr.makeLambda("_", f000017, f000803);
  private static final Expr f000805 = Expr.Constants.EMPTY_RECORD_TYPE;
  private static final Expr f000806 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000805)
          });
  private static final Expr f000807 = Expr.makeBuiltIn("Natural/fold");
  private static final Expr f000808 = Expr.makeApplication(f000009, new Expr[] {f000805});
  private static final Expr f000809 = Expr.Constants.EMPTY_RECORD_LITERAL;
  private static final Expr f000810 = Expr.makeNonEmptyListLiteral(new Expr[] {f000809});
  private static final Expr f000811 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000810, f000002);
  private static final Expr f000812 = Expr.makeLambda("_", f000808, f000811);
  private static final Expr f000813 = Expr.makeEmptyListLiteral(f000808);
  private static final Expr f000814 =
      Expr.makeApplication(f000807, new Expr[] {f000014, f000808, f000812, f000813});
  private static final Expr f000815 = Expr.makeApplication(f000784, new Expr[] {f000805, f000814});
  private static final Expr f000816 = Expr.makeApplication(f000014, new Expr[] {f000786});
  private static final Expr f000817 = Expr.makeNonEmptyListLiteral(new Expr[] {f000816});
  private static final Expr f000818 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000817, f000002);
  private static final Expr f000819 = Expr.makeLambda("_", f000205, f000818);
  private static final Expr f000820 = Expr.makeLambda("_", f000806, f000819);
  private static final Expr f000821 =
      Expr.makeApplication(f000000, new Expr[] {f000806, f000815, f000201, f000820, f000208});
  private static final Expr f000822 = Expr.makePi("_", f000178, f000003);
  private static final Expr f000823 = Expr.makeLambda("_", f000822, f000821);
  private static final Expr f000824 = Expr.makeLambda("_", f000017, f000823);
  private static final Expr f000825 = Expr.makeLambda("_", f000178, f000824);
  private static final Expr f000826 = Expr.makeBuiltIn("List/head");
  private static final Expr f000827 = Expr.makeApplication(f000826, new Expr[] {f000003, f000792});
  private static final Expr f000828 = Expr.makeLambda("_", f000187, f000827);
  private static final Expr f000829 = Expr.makeLambda("_", f000017, f000828);
  private static final Expr f000830 = Expr.makeLambda("_", f000178, f000829);
  private static final Expr f000831 =
      Expr.makeApplication(f000807, new Expr[] {f000025, f000808, f000812, f000813});
  private static final Expr f000832 = Expr.makeApplication(f000784, new Expr[] {f000805, f000831});
  private static final Expr f000833 =
      Expr.makeApplication(f000807, new Expr[] {f000786, f000052, f000025, f000014});
  private static final Expr f000834 = Expr.makeNonEmptyListLiteral(new Expr[] {f000833});
  private static final Expr f000835 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000834, f000002);
  private static final Expr f000836 = Expr.makeLambda("_", f000271, f000835);
  private static final Expr f000837 = Expr.makeLambda("_", f000806, f000836);
  private static final Expr f000838 =
      Expr.makeApplication(f000000, new Expr[] {f000806, f000832, f000205, f000837, f000274});
  private static final Expr f000839 = Expr.makeLambda("_", f000003, f000838);
  private static final Expr f000840 = Expr.makePi("_", f000002, f000003);
  private static final Expr f000841 = Expr.makeLambda("_", f000840, f000839);
  private static final Expr f000842 = Expr.makeLambda("_", f000017, f000841);
  private static final Expr f000843 = Expr.makeLambda("_", f000178, f000842);
  private static final Expr f000844 = Expr.makeBuiltIn("List/last");
  private static final Expr f000845 = Expr.makeBuiltIn("List/length");
  private static final Expr f000846 = Expr.makeNonEmptyListLiteral(new Expr[] {f000742});
  private static final Expr f000847 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000846, f000002);
  private static final Expr f000848 = Expr.makeLambda("_", f000271, f000847);
  private static final Expr f000849 = Expr.makeLambda("_", f000025, f000848);
  private static final Expr f000850 =
      Expr.makeApplication(f000000, new Expr[] {f000025, f000002, f000205, f000849, f000274});
  private static final Expr f000851 = Expr.makeLambda("_", f000205, f000850);
  private static final Expr f000852 = Expr.makePi("_", f000003, f000003);
  private static final Expr f000853 = Expr.makeLambda("_", f000852, f000851);
  private static final Expr f000854 = Expr.makeLambda("_", f000017, f000853);
  private static final Expr f000855 = Expr.makeLambda("_", f000017, f000854);
  private static final Expr f000856 = Expr.makeApplication(f000845, new Expr[] {f000003, f000002});
  private static final Expr f000857 = Expr.makeApplication(f000063, new Expr[] {f000856});
  private static final Expr f000858 = Expr.makeLambda("_", f000187, f000857);
  private static final Expr f000859 = Expr.makeLambda("_", f000017, f000858);
  private static final Expr f000860 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000205),
            new SimpleImmutableEntry<String, Expr>("true", f000205)
          });
  private static final Expr f000861 = Expr.makeFieldAccess(f000002, "false");
  private static final Expr f000862 = Expr.makeFieldAccess(f000002, "true");
  private static final Expr f000863 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000306, f000862);
  private static final Expr f000864 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000861),
            new SimpleImmutableEntry<String, Expr>("true", f000863)
          });
  private static final Expr f000865 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000306, f000861);
  private static final Expr f000866 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000865),
            new SimpleImmutableEntry<String, Expr>("true", f000862)
          });
  private static final Expr f000867 = Expr.makeIf(f000742, f000864, f000866);
  private static final Expr f000868 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000271),
            new SimpleImmutableEntry<String, Expr>("true", f000271)
          });
  private static final Expr f000869 = Expr.makeLambda("_", f000868, f000867);
  private static final Expr f000870 = Expr.makeLambda("_", f000014, f000869);
  private static final Expr f000871 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000274),
            new SimpleImmutableEntry<String, Expr>("true", f000274)
          });
  private static final Expr f000872 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000860, f000870, f000871});
  private static final Expr f000873 = Expr.makeLambda("_", f000201, f000872);
  private static final Expr f000874 = Expr.makeLambda("_", f000748, f000873);
  private static final Expr f000875 = Expr.makeLambda("_", f000017, f000874);
  private static final Expr f000876 = Expr.makeLambda("_", f000201, f000307);
  private static final Expr f000877 =
      Expr.makeApplication(f000807, new Expr[] {f000014, f000201, f000876, f000208});
  private static final Expr f000878 = Expr.makeLambda("_", f000002, f000877);
  private static final Expr f000879 = Expr.makeLambda("_", f000017, f000878);
  private static final Expr f000880 = Expr.makeLambda("_", f000178, f000879);
  private static final Expr f000881 = Expr.makeApplication(f000009, new Expr[] {f000783});
  private static final Expr f000882 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000014)
          });
  private static final Expr f000883 = Expr.makeApplication(f000009, new Expr[] {f000882});
  private static final Expr f000884 = Expr.makePi("_", f000178, f000883);
  private static final Expr f000885 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000178),
            new SimpleImmutableEntry<String, Expr>("diff", f000884)
          });
  private static final Expr f000886 = Expr.makeFieldAccess(f000002, "count");
  private static final Expr f000887 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000025)
          });
  private static final Expr f000888 = Expr.makeApplication(f000845, new Expr[] {f000887, f000003});
  private static final Expr f000889 = Expr.makeOperatorApplication(Operator.PLUS, f000886, f000888);
  private static final Expr f000890 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000052)
          });
  private static final Expr f000891 = Expr.makeApplication(f000009, new Expr[] {f000890});
  private static final Expr f000892 = Expr.makeOperatorApplication(Operator.PLUS, f000786, f000014);
  private static final Expr f000893 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000892),
            new SimpleImmutableEntry<String, Expr>("value", f000268)
          });
  private static final Expr f000894 = Expr.makeNonEmptyListLiteral(new Expr[] {f000893});
  private static final Expr f000895 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000894, f000002);
  private static final Expr f000896 = Expr.makeIdentifier("_", 5);
  private static final Expr f000897 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000896)
          });
  private static final Expr f000898 = Expr.makeApplication(f000009, new Expr[] {f000897});
  private static final Expr f000899 = Expr.makeLambda("_", f000898, f000895);
  private static final Expr f000900 = Expr.makeLambda("_", f000890, f000899);
  private static final Expr f000901 = Expr.makeFieldAccess(f000003, "diff");
  private static final Expr f000902 = Expr.makeApplication(f000845, new Expr[] {f000890, f000014});
  private static final Expr f000903 = Expr.makeOperatorApplication(Operator.PLUS, f000002, f000902);
  private static final Expr f000904 = Expr.makeApplication(f000901, new Expr[] {f000903});
  private static final Expr f000905 =
      Expr.makeApplication(f000000, new Expr[] {f000890, f000014, f000891, f000900, f000904});
  private static final Expr f000906 = Expr.makeLambda("_", f000178, f000905);
  private static final Expr f000907 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000889),
            new SimpleImmutableEntry<String, Expr>("diff", f000906)
          });
  private static final Expr f000908 = Expr.makeApplication(f000009, new Expr[] {f000887});
  private static final Expr f000909 = Expr.makePi("_", f000178, f000908);
  private static final Expr f000910 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000178),
            new SimpleImmutableEntry<String, Expr>("diff", f000909)
          });
  private static final Expr f000911 = Expr.makeLambda("_", f000910, f000907);
  private static final Expr f000912 = Expr.makeLambda("_", f000881, f000911);
  private static final Expr f000913 = Expr.makeNaturalLiteral(new BigInteger("0"));
  private static final Expr f000914 = Expr.makeEmptyListLiteral(f000883);
  private static final Expr f000915 = Expr.makeLambda("_", f000178, f000914);
  private static final Expr f000916 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000913),
            new SimpleImmutableEntry<String, Expr>("diff", f000915)
          });
  private static final Expr f000917 =
      Expr.makeApplication(f000000, new Expr[] {f000881, f000002, f000885, f000912, f000916});
  private static final Expr f000918 = Expr.makeFieldAccess(f000917, "diff");
  private static final Expr f000919 = Expr.makeApplication(f000918, new Expr[] {f000913});
  private static final Expr f000920 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000002)
          });
  private static final Expr f000921 = Expr.makeApplication(f000009, new Expr[] {f000920});
  private static final Expr f000922 = Expr.makeApplication(f000009, new Expr[] {f000921});
  private static final Expr f000923 = Expr.makeLambda("_", f000922, f000919);
  private static final Expr f000924 = Expr.makeLambda("_", f000017, f000923);
  private static final Expr f000925 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000788, f000012);
  private static final Expr f000926 = Expr.makeIf(f000925, f000270, f000002);
  private static final Expr f000927 = Expr.makeLambda("_", f000205, f000926);
  private static final Expr f000928 = Expr.makeLambda("_", f000783, f000927);
  private static final Expr f000929 =
      Expr.makeApplication(f000000, new Expr[] {f000783, f000785, f000201, f000928, f000208});
  private static final Expr f000930 = Expr.makeLambda("_", f000187, f000929);
  private static final Expr f000931 = Expr.makeLambda("_", f000017, f000930);
  private static final Expr f000932 = Expr.makeLambda("_", f000178, f000931);
  private static final Expr f000933 = Expr.makeApplication(f000373, new Expr[] {f000003});
  private static final Expr f000934 = Expr.makeNonEmptyListLiteral(new Expr[] {f000002});
  private static final Expr f000935 = Expr.makeLambda("_", f000014, f000934);
  private static final Expr f000936 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000274),
            new SimpleImmutableEntry<String, Expr>("Some", f000935)
          });
  private static final Expr f000937 = Expr.makeMerge(f000936, f000002, null);
  private static final Expr f000938 = Expr.makeLambda("_", f000271, f000307);
  private static final Expr f000939 = Expr.makeLambda("_", f000014, f000938);
  private static final Expr f000940 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000937, f000205, f000939});
  private static final Expr f000941 = Expr.makeLambda("_", f000933, f000940);
  private static final Expr f000942 =
      Expr.makeApplication(f000000, new Expr[] {f000933, f000002, f000201, f000941, f000208});
  private static final Expr f000943 = Expr.makeApplication(f000373, new Expr[] {f000002});
  private static final Expr f000944 = Expr.makeApplication(f000009, new Expr[] {f000943});
  private static final Expr f000945 = Expr.makeLambda("_", f000944, f000942);
  private static final Expr f000946 = Expr.makeLambda("_", f000017, f000945);
  private static final Expr f000947 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000014),
            new SimpleImmutableEntry<String, Expr>("_2", f000003)
          });
  private static final Expr f000948 = Expr.makeFieldAccess(f000003, "_1");
  private static final Expr f000949 = Expr.makeNonEmptyListLiteral(new Expr[] {f000948});
  private static final Expr f000950 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000949, f000002);
  private static final Expr f000951 = Expr.makeLambda("_", f000271, f000950);
  private static final Expr f000952 = Expr.makeLambda("_", f000947, f000951);
  private static final Expr f000953 =
      Expr.makeApplication(f000000, new Expr[] {f000947, f000002, f000205, f000952, f000274});
  private static final Expr f000954 = Expr.makeFieldAccess(f000003, "_2");
  private static final Expr f000955 = Expr.makeNonEmptyListLiteral(new Expr[] {f000954});
  private static final Expr f000956 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000955, f000002);
  private static final Expr f000957 = Expr.makeLambda("_", f000205, f000956);
  private static final Expr f000958 = Expr.makeLambda("_", f000947, f000957);
  private static final Expr f000959 =
      Expr.makeApplication(f000000, new Expr[] {f000947, f000002, f000201, f000958, f000208});
  private static final Expr f000960 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000953),
            new SimpleImmutableEntry<String, Expr>("_2", f000959)
          });
  private static final Expr f000961 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000003),
            new SimpleImmutableEntry<String, Expr>("_2", f000002)
          });
  private static final Expr f000962 = Expr.makeApplication(f000009, new Expr[] {f000961});
  private static final Expr f000963 = Expr.makeLambda("_", f000962, f000960);
  private static final Expr f000964 = Expr.makeLambda("_", f000017, f000963);
  private static final Expr f000965 = Expr.makeLambda("_", f000017, f000964);
  private static final Expr f000966 = Expr.makeApplication(f000784, new Expr[] {f000025, f000014});
  private static final Expr f000967 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000025),
            new SimpleImmutableEntry<String, Expr>("_2", f000003)
          });
  private static final Expr f000968 = Expr.makeApplication(f000009, new Expr[] {f000967});
  private static final Expr f000969 = Expr.makeFieldAccess(f000014, "value");
  private static final Expr f000970 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000969),
            new SimpleImmutableEntry<String, Expr>("_2", f000002)
          });
  private static final Expr f000971 = Expr.makeNonEmptyListLiteral(new Expr[] {f000970});
  private static final Expr f000972 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000971, f000003);
  private static final Expr f000973 = Expr.makeLambda("_", f000025, f000972);
  private static final Expr f000974 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000002),
            new SimpleImmutableEntry<String, Expr>("Some", f000973)
          });
  private static final Expr f000975 = Expr.makeFieldAccess(f000025, "index");
  private static final Expr f000976 = Expr.makeApplication(f000076, new Expr[] {f000786, f000975});
  private static final Expr f000977 = Expr.makeApplication(f000063, new Expr[] {f000976});
  private static final Expr f000978 = Expr.makeIf(f000977, f000270, f000002);
  private static final Expr f000979 = Expr.makeLambda("_", f000759, f000978);
  private static final Expr f000980 = Expr.makeLambda("_", f000887, f000979);
  private static final Expr f000981 = Expr.makeEmptyListLiteral(f000271);
  private static final Expr f000982 =
      Expr.makeApplication(f000000, new Expr[] {f000887, f000966, f000271, f000980, f000981});
  private static final Expr f000983 = Expr.makeApplication(f000826, new Expr[] {f000025, f000982});
  private static final Expr f000984 = Expr.makeMerge(f000974, f000983, null);
  private static final Expr f000985 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000052),
            new SimpleImmutableEntry<String, Expr>("_2", f000014)
          });
  private static final Expr f000986 = Expr.makeApplication(f000009, new Expr[] {f000985});
  private static final Expr f000987 = Expr.makeLambda("_", f000986, f000984);
  private static final Expr f000988 = Expr.makeLambda("_", f000887, f000987);
  private static final Expr f000989 = Expr.makeEmptyListLiteral(f000968);
  private static final Expr f000990 =
      Expr.makeApplication(f000000, new Expr[] {f000887, f000966, f000968, f000988, f000989});
  private static final Expr f000991 = Expr.makeLambda("_", f000187, f000990);
  private static final Expr f000992 = Expr.makeLambda("_", f000017, f000991);
  private static final Expr f000993 = Expr.makeLambda("_", f000187, f000992);
  private static final Expr f000994 = Expr.makeLambda("_", f000017, f000993);
  private static final Expr f000995 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("all", f000750),
            new SimpleImmutableEntry<String, Expr>("any", f000757),
            new SimpleImmutableEntry<String, Expr>("build", f000758),
            new SimpleImmutableEntry<String, Expr>("concat", f000768),
            new SimpleImmutableEntry<String, Expr>("concatMap", f000776),
            new SimpleImmutableEntry<String, Expr>("default", f000782),
            new SimpleImmutableEntry<String, Expr>("drop", f000795),
            new SimpleImmutableEntry<String, Expr>("empty", f000797),
            new SimpleImmutableEntry<String, Expr>("filter", f000804),
            new SimpleImmutableEntry<String, Expr>("fold", f000000),
            new SimpleImmutableEntry<String, Expr>("generate", f000825),
            new SimpleImmutableEntry<String, Expr>("head", f000826),
            new SimpleImmutableEntry<String, Expr>("index", f000830),
            new SimpleImmutableEntry<String, Expr>("indexed", f000784),
            new SimpleImmutableEntry<String, Expr>("iterate", f000843),
            new SimpleImmutableEntry<String, Expr>("last", f000844),
            new SimpleImmutableEntry<String, Expr>("length", f000845),
            new SimpleImmutableEntry<String, Expr>("map", f000855),
            new SimpleImmutableEntry<String, Expr>("null", f000859),
            new SimpleImmutableEntry<String, Expr>("partition", f000875),
            new SimpleImmutableEntry<String, Expr>("replicate", f000880),
            new SimpleImmutableEntry<String, Expr>("reverse", f000477),
            new SimpleImmutableEntry<String, Expr>("shifted", f000924),
            new SimpleImmutableEntry<String, Expr>("take", f000932),
            new SimpleImmutableEntry<String, Expr>("unpackOptionals", f000946),
            new SimpleImmutableEntry<String, Expr>("unzip", f000965),
            new SimpleImmutableEntry<String, Expr>("zip", f000994)
          });
  private static final Expr f000996 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Environment", f000183),
            new SimpleImmutableEntry<String, Expr>("Local", f000183),
            new SimpleImmutableEntry<String, Expr>("Missing", null),
            new SimpleImmutableEntry<String, Expr>("Remote", f000183)
          });
  private static final Expr f000997 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("Type", f000996)});
  private static final Expr f000998 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000003),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000002)
          });
  private static final Expr f000999 = Expr.makeLambda("_", f000017, f000998);
  private static final Expr f001000 = Expr.makeLambda("_", f000017, f000999);
  private static final Expr f001001 = Expr.makeApplication(f000009, new Expr[] {f000998});
  private static final Expr f001002 = Expr.makeLambda("_", f000017, f001001);
  private static final Expr f001003 = Expr.makeLambda("_", f000017, f001002);
  private static final Expr f001004 = Expr.makeEmptyListLiteral(f001001);
  private static final Expr f001005 = Expr.makeLambda("_", f000017, f001004);
  private static final Expr f001006 = Expr.makeLambda("_", f000017, f001005);
  private static final Expr f001007 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000014),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000003)
          });
  private static final Expr f001008 = Expr.makeNonEmptyListLiteral(new Expr[] {f000248});
  private static final Expr f001009 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001008, f000002);
  private static final Expr f001010 = Expr.makeLambda("_", f000271, f001009);
  private static final Expr f001011 = Expr.makeLambda("_", f001007, f001010);
  private static final Expr f001012 =
      Expr.makeApplication(f000000, new Expr[] {f001007, f000002, f000205, f001011, f000274});
  private static final Expr f001013 = Expr.makeLambda("_", f001001, f001012);
  private static final Expr f001014 = Expr.makeLambda("_", f000017, f001013);
  private static final Expr f001015 = Expr.makeLambda("_", f000017, f001014);
  private static final Expr f001016 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000052),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000025)
          });
  private static final Expr f001017 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000052),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000014)
          });
  private static final Expr f001018 = Expr.makeApplication(f000009, new Expr[] {f001017});
  private static final Expr f001019 = Expr.makeApplication(f000025, new Expr[] {f000249});
  private static final Expr f001020 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000248),
            new SimpleImmutableEntry<String, Expr>("mapValue", f001019)
          });
  private static final Expr f001021 = Expr.makeNonEmptyListLiteral(new Expr[] {f001020});
  private static final Expr f001022 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001021, f000002);
  private static final Expr f001023 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000896),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000025)
          });
  private static final Expr f001024 = Expr.makeApplication(f000009, new Expr[] {f001023});
  private static final Expr f001025 = Expr.makeLambda("_", f001024, f001022);
  private static final Expr f001026 = Expr.makeLambda("_", f001016, f001025);
  private static final Expr f001027 = Expr.makeEmptyListLiteral(f001018);
  private static final Expr f001028 =
      Expr.makeApplication(f000000, new Expr[] {f001016, f000002, f001018, f001026, f001027});
  private static final Expr f001029 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000025),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000014)
          });
  private static final Expr f001030 = Expr.makeApplication(f000009, new Expr[] {f001029});
  private static final Expr f001031 = Expr.makeLambda("_", f001030, f001028);
  private static final Expr f001032 = Expr.makeLambda("_", f000852, f001031);
  private static final Expr f001033 = Expr.makeLambda("_", f000017, f001032);
  private static final Expr f001034 = Expr.makeLambda("_", f000017, f001033);
  private static final Expr f001035 = Expr.makeLambda("_", f000017, f001034);
  private static final Expr f001036 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000014),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000933)
          });
  private static final Expr f001037 = Expr.makeApplication(f000009, new Expr[] {f001007});
  private static final Expr f001038 = Expr.makeEmptyListLiteral(f001030);
  private static final Expr f001039 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000248),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000002)
          });
  private static final Expr f001040 = Expr.makeNonEmptyListLiteral(new Expr[] {f001039});
  private static final Expr f001041 = Expr.makeLambda("_", f000014, f001040);
  private static final Expr f001042 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001038),
            new SimpleImmutableEntry<String, Expr>("Some", f001041)
          });
  private static final Expr f001043 = Expr.makeMerge(f001042, f000296, null);
  private static final Expr f001044 = Expr.makeApplication(f000009, new Expr[] {f001016});
  private static final Expr f001045 = Expr.makeLambda("_", f001044, f000307);
  private static final Expr f001046 = Expr.makeLambda("_", f001029, f001045);
  private static final Expr f001047 =
      Expr.makeApplication(f000000, new Expr[] {f001029, f001043, f001030, f001046});
  private static final Expr f001048 = Expr.makeLambda("_", f001036, f001047);
  private static final Expr f001049 = Expr.makeEmptyListLiteral(f001037);
  private static final Expr f001050 =
      Expr.makeApplication(f000000, new Expr[] {f001036, f000002, f001037, f001048, f001049});
  private static final Expr f001051 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000003),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000943)
          });
  private static final Expr f001052 = Expr.makeApplication(f000009, new Expr[] {f001051});
  private static final Expr f001053 = Expr.makeLambda("_", f001052, f001050);
  private static final Expr f001054 = Expr.makeLambda("_", f000017, f001053);
  private static final Expr f001055 = Expr.makeLambda("_", f000017, f001054);
  private static final Expr f001056 = Expr.makeNonEmptyListLiteral(new Expr[] {f000249});
  private static final Expr f001057 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001056, f000002);
  private static final Expr f001058 = Expr.makeLambda("_", f000205, f001057);
  private static final Expr f001059 = Expr.makeLambda("_", f001007, f001058);
  private static final Expr f001060 =
      Expr.makeApplication(f000000, new Expr[] {f001007, f000002, f000201, f001059, f000208});
  private static final Expr f001061 = Expr.makeLambda("_", f001001, f001060);
  private static final Expr f001062 = Expr.makeLambda("_", f000017, f001061);
  private static final Expr f001063 = Expr.makeLambda("_", f000017, f001062);
  private static final Expr f001064 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Entry", f001000),
            new SimpleImmutableEntry<String, Expr>("Type", f001003),
            new SimpleImmutableEntry<String, Expr>("empty", f001006),
            new SimpleImmutableEntry<String, Expr>("keyText", f000232),
            new SimpleImmutableEntry<String, Expr>("keyValue", f000235),
            new SimpleImmutableEntry<String, Expr>("keys", f001015),
            new SimpleImmutableEntry<String, Expr>("map", f001035),
            new SimpleImmutableEntry<String, Expr>("unpackOptionals", f001055),
            new SimpleImmutableEntry<String, Expr>("values", f001063)
          });
  private static final Expr f001065 = Expr.makeLambda("_", f000017, f000188);
  private static final Expr f001066 = Expr.makeBuiltIn("Natural/build");
  private static final Expr f001067 =
      Expr.makeApplication(f000807, new Expr[] {f000002, f000808, f000812, f000813});
  private static final Expr f001068 = Expr.makeApplication(f000784, new Expr[] {f000805, f001067});
  private static final Expr f001069 = Expr.makeApplication(f000009, new Expr[] {f000178});
  private static final Expr f001070 = Expr.makeNonEmptyListLiteral(new Expr[] {f000786});
  private static final Expr f001071 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001070, f000002);
  private static final Expr f001072 = Expr.makeLambda("_", f001069, f001071);
  private static final Expr f001073 = Expr.makeLambda("_", f000806, f001072);
  private static final Expr f001074 = Expr.makeEmptyListLiteral(f001069);
  private static final Expr f001075 =
      Expr.makeApplication(f000000, new Expr[] {f000806, f001068, f001069, f001073, f001074});
  private static final Expr f001076 = Expr.makeLambda("_", f000178, f001075);
  private static final Expr f001077 = Expr.makeApplication(f000076, new Expr[] {f000002, f000003});
  private static final Expr f001078 = Expr.makeApplication(f000063, new Expr[] {f001077});
  private static final Expr f001079 = Expr.makeApplication(f000076, new Expr[] {f000003, f000002});
  private static final Expr f001080 = Expr.makeApplication(f000063, new Expr[] {f001079});
  private static final Expr f001081 = Expr.makeOperatorApplication(Operator.AND, f001078, f001080);
  private static final Expr f001082 = Expr.makeLambda("_", f000178, f001081);
  private static final Expr f001083 = Expr.makeLambda("_", f000178, f001082);
  private static final Expr f001084 = Expr.makeBuiltIn("Natural/even");
  private static final Expr f001085 =
      Expr.makeOperatorApplication(Operator.EQUALS, f001078, f000012);
  private static final Expr f001086 = Expr.makeLambda("_", f000178, f001085);
  private static final Expr f001087 = Expr.makeLambda("_", f000178, f001086);
  private static final Expr f001088 = Expr.makeLambda("_", f000178, f001080);
  private static final Expr f001089 = Expr.makeLambda("_", f000178, f001088);
  private static final Expr f001090 =
      Expr.makeOperatorApplication(Operator.EQUALS, f001080, f000012);
  private static final Expr f001091 = Expr.makeLambda("_", f000178, f001090);
  private static final Expr f001092 = Expr.makeLambda("_", f000178, f001091);
  private static final Expr f001093 = Expr.makeLambda("_", f000178, f001078);
  private static final Expr f001094 = Expr.makeLambda("_", f000178, f001093);
  private static final Expr f001095 = Expr.makeIf(f001078, f000002, f000003);
  private static final Expr f001096 = Expr.makeLambda("_", f000178, f001095);
  private static final Expr f001097 = Expr.makeLambda("_", f000178, f001096);
  private static final Expr f001098 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000003, f000178, f001097, f000002});
  private static final Expr f001099 = Expr.makeApplication(f000175, new Expr[] {f001098});
  private static final Expr f001100 = Expr.makeLambda("_", f000178, f001099);
  private static final Expr f001101 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000179),
            new SimpleImmutableEntry<String, Expr>("Some", f001100)
          });
  private static final Expr f001102 = Expr.makeApplication(f000826, new Expr[] {f000178, f000002});
  private static final Expr f001103 = Expr.makeMerge(f001101, f001102, null);
  private static final Expr f001104 = Expr.makeLambda("_", f001069, f001103);
  private static final Expr f001105 = Expr.makeApplication(f000063, new Expr[] {f000002});
  private static final Expr f001106 = Expr.makeIf(f001078, f000003, f000002);
  private static final Expr f001107 = Expr.makeLambda("_", f000178, f001106);
  private static final Expr f001108 = Expr.makeLambda("_", f000178, f001107);
  private static final Expr f001109 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000003, f000178, f001108, f000002});
  private static final Expr f001110 = Expr.makeIf(f001105, f000002, f001109);
  private static final Expr f001111 = Expr.makeApplication(f000175, new Expr[] {f001110});
  private static final Expr f001112 = Expr.makeLambda("_", f000178, f001111);
  private static final Expr f001113 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000179),
            new SimpleImmutableEntry<String, Expr>("Some", f001112)
          });
  private static final Expr f001114 = Expr.makeMerge(f001113, f001102, null);
  private static final Expr f001115 = Expr.makeLambda("_", f001069, f001114);
  private static final Expr f001116 = Expr.makeBuiltIn("Natural/odd");
  private static final Expr f001117 =
      Expr.makeOperatorApplication(Operator.TIMES, f000003, f000002);
  private static final Expr f001118 = Expr.makeLambda("_", f000178, f001117);
  private static final Expr f001119 = Expr.makeLambda("_", f000178, f001118);
  private static final Expr f001120 = Expr.makeNaturalLiteral(new BigInteger("1"));
  private static final Expr f001121 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000002, f000178, f001119, f001120});
  private static final Expr f001122 = Expr.makeLambda("_", f001069, f001121);
  private static final Expr f001123 = Expr.makeApplication(f000845, new Expr[] {f000178, f000002});
  private static final Expr f001124 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f001069),
            new SimpleImmutableEntry<String, Expr>("sorted", f001069)
          });
  private static final Expr f001125 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f001074),
            new SimpleImmutableEntry<String, Expr>("true", f001074)
          });
  private static final Expr f001126 = Expr.makeFieldAccess(f000003, "rest");
  private static final Expr f001127 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f001069),
            new SimpleImmutableEntry<String, Expr>("true", f001069)
          });
  private static final Expr f001128 = Expr.makeApplication(f000076, new Expr[] {f000014, f000003});
  private static final Expr f001129 = Expr.makeApplication(f000063, new Expr[] {f001128});
  private static final Expr f001130 = Expr.makeIf(f001129, f000864, f000866);
  private static final Expr f001131 = Expr.makeLambda("_", f001127, f001130);
  private static final Expr f001132 = Expr.makeLambda("_", f000178, f001131);
  private static final Expr f001133 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f001126, f001127, f001132, f001125});
  private static final Expr f001134 = Expr.makeLambda("_", f000178, f001133);
  private static final Expr f001135 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001125),
            new SimpleImmutableEntry<String, Expr>("Some", f001134)
          });
  private static final Expr f001136 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f001126, f000178, f001108, f000002});
  private static final Expr f001137 = Expr.makeIf(f001105, f000002, f001136);
  private static final Expr f001138 = Expr.makeApplication(f000175, new Expr[] {f001137});
  private static final Expr f001139 = Expr.makeLambda("_", f000178, f001138);
  private static final Expr f001140 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000179),
            new SimpleImmutableEntry<String, Expr>("Some", f001139)
          });
  private static final Expr f001141 = Expr.makeFieldAccess(f000002, "rest");
  private static final Expr f001142 = Expr.makeApplication(f000826, new Expr[] {f000178, f001141});
  private static final Expr f001143 = Expr.makeMerge(f001140, f001142, null);
  private static final Expr f001144 = Expr.makeMerge(f001135, f001143, null);
  private static final Expr f001145 = Expr.makeFieldAccess(f001144, "false");
  private static final Expr f001146 = Expr.makeFieldAccess(f000002, "sorted");
  private static final Expr f001147 = Expr.makeFieldAccess(f001144, "true");
  private static final Expr f001148 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001146, f001147);
  private static final Expr f001149 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f001145),
            new SimpleImmutableEntry<String, Expr>("sorted", f001148)
          });
  private static final Expr f001150 = Expr.makeLambda("_", f001124, f001149);
  private static final Expr f001151 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f000002),
            new SimpleImmutableEntry<String, Expr>("sorted", f001074)
          });
  private static final Expr f001152 =
      Expr.makeApplication(f000807, new Expr[] {f001123, f001124, f001150, f001151});
  private static final Expr f001153 = Expr.makeFieldAccess(f001152, "sorted");
  private static final Expr f001154 = Expr.makeLambda("_", f001069, f001153);
  private static final Expr f001155 = Expr.makeOperatorApplication(Operator.PLUS, f000003, f000002);
  private static final Expr f001156 = Expr.makeLambda("_", f000178, f001155);
  private static final Expr f001157 = Expr.makeLambda("_", f000178, f001156);
  private static final Expr f001158 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000002, f000178, f001157, f000913});
  private static final Expr f001159 = Expr.makeLambda("_", f001069, f001158);
  private static final Expr f001160 = Expr.makeApplication(f000081, new Expr[] {f000002});
  private static final Expr f001161 = Expr.makeApplication(f000174, new Expr[] {f001160});
  private static final Expr f001162 = Expr.makeLambda("_", f000178, f001161);
  private static final Expr f001163 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("build", f001066),
            new SimpleImmutableEntry<String, Expr>("enumerate", f001076),
            new SimpleImmutableEntry<String, Expr>("equal", f001083),
            new SimpleImmutableEntry<String, Expr>("even", f001084),
            new SimpleImmutableEntry<String, Expr>("fold", f000807),
            new SimpleImmutableEntry<String, Expr>("greaterThan", f001087),
            new SimpleImmutableEntry<String, Expr>("greaterThanEqual", f001089),
            new SimpleImmutableEntry<String, Expr>("isZero", f000063),
            new SimpleImmutableEntry<String, Expr>("lessThan", f001092),
            new SimpleImmutableEntry<String, Expr>("lessThanEqual", f001094),
            new SimpleImmutableEntry<String, Expr>("listMax", f001104),
            new SimpleImmutableEntry<String, Expr>("listMin", f001115),
            new SimpleImmutableEntry<String, Expr>("max", f001097),
            new SimpleImmutableEntry<String, Expr>("min", f001108),
            new SimpleImmutableEntry<String, Expr>("odd", f001116),
            new SimpleImmutableEntry<String, Expr>("product", f001122),
            new SimpleImmutableEntry<String, Expr>("show", f000504),
            new SimpleImmutableEntry<String, Expr>("sort", f001154),
            new SimpleImmutableEntry<String, Expr>("subtract", f000076),
            new SimpleImmutableEntry<String, Expr>("sum", f001159),
            new SimpleImmutableEntry<String, Expr>("toDouble", f001162),
            new SimpleImmutableEntry<String, Expr>("toInteger", f000081)
          });
  private static final Expr f001164 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000007),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001165 = Expr.makeMerge(f001164, f000002, null);
  private static final Expr f001166 = Expr.makeLambda("_", f000933, f001165);
  private static final Expr f001167 = Expr.makeLambda("_", f000748, f001166);
  private static final Expr f001168 = Expr.makeLambda("_", f000017, f001167);
  private static final Expr f001169 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000012),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001170 = Expr.makeMerge(f001169, f000002, null);
  private static final Expr f001171 = Expr.makeLambda("_", f000933, f001170);
  private static final Expr f001172 = Expr.makeLambda("_", f000748, f001171);
  private static final Expr f001173 = Expr.makeLambda("_", f000017, f001172);
  private static final Expr f001174 = Expr.makeApplication(f000175, new Expr[] {f000002});
  private static final Expr f001175 = Expr.makeLambda("_", f000003, f001174);
  private static final Expr f001176 = Expr.makeApplication(f000177, new Expr[] {f000003});
  private static final Expr f001177 =
      Expr.makeApplication(f000002, new Expr[] {f000933, f001175, f001176});
  private static final Expr f001178 = Expr.makePi("_", f000852, f000015);
  private static final Expr f001179 = Expr.makePi("_", f000017, f001178);
  private static final Expr f001180 = Expr.makeLambda("_", f001179, f001177);
  private static final Expr f001181 = Expr.makeLambda("_", f000017, f001180);
  private static final Expr f001182 = Expr.makeLambda("_", f000933, f000002);
  private static final Expr f001183 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001176),
            new SimpleImmutableEntry<String, Expr>("Some", f001182)
          });
  private static final Expr f001184 = Expr.makeMerge(f001183, f000002, null);
  private static final Expr f001185 = Expr.makeApplication(f000373, new Expr[] {f000943});
  private static final Expr f001186 = Expr.makeLambda("_", f001185, f001184);
  private static final Expr f001187 = Expr.makeLambda("_", f000017, f001186);
  private static final Expr f001188 = Expr.makeLambda("_", f000014, f000002);
  private static final Expr f001189 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000003),
            new SimpleImmutableEntry<String, Expr>("Some", f001188)
          });
  private static final Expr f001190 = Expr.makeMerge(f001189, f000002, null);
  private static final Expr f001191 = Expr.makeLambda("_", f000933, f001190);
  private static final Expr f001192 = Expr.makeLambda("_", f000002, f001191);
  private static final Expr f001193 = Expr.makeLambda("_", f000017, f001192);
  private static final Expr f001194 = Expr.makeApplication(f000177, new Expr[] {f000014});
  private static final Expr f001195 = Expr.makeApplication(f000177, new Expr[] {f000025});
  private static final Expr f001196 = Expr.makeIf(f000050, f001174, f001195);
  private static final Expr f001197 = Expr.makeLambda("_", f000014, f001196);
  private static final Expr f001198 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001194),
            new SimpleImmutableEntry<String, Expr>("Some", f001197)
          });
  private static final Expr f001199 = Expr.makeMerge(f001198, f000002, null);
  private static final Expr f001200 = Expr.makeLambda("_", f000933, f001199);
  private static final Expr f001201 = Expr.makeLambda("_", f000748, f001200);
  private static final Expr f001202 = Expr.makeLambda("_", f000017, f001201);
  private static final Expr f001203 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000002),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001204 = Expr.makeMerge(f001203, f000025, null);
  private static final Expr f001205 = Expr.makeLambda("_", f000003, f001204);
  private static final Expr f001206 = Expr.makePi("_", f000014, f000003);
  private static final Expr f001207 = Expr.makeLambda("_", f001206, f001205);
  private static final Expr f001208 = Expr.makeLambda("_", f000017, f001207);
  private static final Expr f001209 = Expr.makeLambda("_", f000943, f001208);
  private static final Expr f001210 = Expr.makeLambda("_", f000017, f001209);
  private static final Expr f001211 = Expr.makeLambda("_", f000025, f001174);
  private static final Expr f001212 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000002),
            new SimpleImmutableEntry<String, Expr>("Some", f001211)
          });
  private static final Expr f001213 = Expr.makeMerge(f001212, f000003, null);
  private static final Expr f001214 = Expr.makeApplication(f000373, new Expr[] {f000014});
  private static final Expr f001215 = Expr.makeLambda("_", f001214, f001213);
  private static final Expr f001216 = Expr.makeLambda("_", f000933, f001215);
  private static final Expr f001217 =
      Expr.makeApplication(f000000, new Expr[] {f000933, f000002, f000933, f001216, f001176});
  private static final Expr f001218 = Expr.makeLambda("_", f000944, f001217);
  private static final Expr f001219 = Expr.makeLambda("_", f000017, f001218);
  private static final Expr f001220 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000003),
            new SimpleImmutableEntry<String, Expr>("Some", f001211)
          });
  private static final Expr f001221 = Expr.makeMerge(f001220, f000002, null);
  private static final Expr f001222 = Expr.makeLambda("_", f001214, f001221);
  private static final Expr f001223 = Expr.makeLambda("_", f000933, f001222);
  private static final Expr f001224 =
      Expr.makeApplication(f000000, new Expr[] {f000933, f000002, f000933, f001223, f001176});
  private static final Expr f001225 = Expr.makeLambda("_", f000944, f001224);
  private static final Expr f001226 = Expr.makeLambda("_", f000017, f001225);
  private static final Expr f001227 = Expr.makeLambda("_", f000003, f001120);
  private static final Expr f001228 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000913),
            new SimpleImmutableEntry<String, Expr>("Some", f001227)
          });
  private static final Expr f001229 = Expr.makeMerge(f001228, f000002, null);
  private static final Expr f001230 = Expr.makeLambda("_", f000943, f001229);
  private static final Expr f001231 = Expr.makeLambda("_", f000017, f001230);
  private static final Expr f001232 = Expr.makeApplication(f000175, new Expr[] {f000050});
  private static final Expr f001233 = Expr.makeLambda("_", f000025, f001232);
  private static final Expr f001234 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001194),
            new SimpleImmutableEntry<String, Expr>("Some", f001233)
          });
  private static final Expr f001235 = Expr.makeMerge(f001234, f000002, null);
  private static final Expr f001236 = Expr.makeLambda("_", f001214, f001235);
  private static final Expr f001237 = Expr.makeLambda("_", f000852, f001236);
  private static final Expr f001238 = Expr.makeLambda("_", f000017, f001237);
  private static final Expr f001239 = Expr.makeLambda("_", f000017, f001238);
  private static final Expr f001240 = Expr.makeLambda("_", f000003, f000012);
  private static final Expr f001241 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000007),
            new SimpleImmutableEntry<String, Expr>("Some", f001240)
          });
  private static final Expr f001242 = Expr.makeMerge(f001241, f000002, null);
  private static final Expr f001243 = Expr.makeLambda("_", f000943, f001242);
  private static final Expr f001244 = Expr.makeLambda("_", f000017, f001243);
  private static final Expr f001245 = Expr.makeLambda("_", f000003, f000934);
  private static final Expr f001246 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000208),
            new SimpleImmutableEntry<String, Expr>("Some", f001245)
          });
  private static final Expr f001247 = Expr.makeMerge(f001246, f000002, null);
  private static final Expr f001248 = Expr.makeLambda("_", f000943, f001247);
  private static final Expr f001249 = Expr.makeLambda("_", f000017, f001248);
  private static final Expr f001250 = Expr.makeFieldAccess(f000002, "_1");
  private static final Expr f001251 = Expr.makeApplication(f000175, new Expr[] {f001250});
  private static final Expr f001252 = Expr.makeLambda("_", f000947, f001251);
  private static final Expr f001253 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001194),
            new SimpleImmutableEntry<String, Expr>("Some", f001252)
          });
  private static final Expr f001254 = Expr.makeMerge(f001253, f000002, null);
  private static final Expr f001255 = Expr.makeFieldAccess(f000002, "_2");
  private static final Expr f001256 = Expr.makeApplication(f000175, new Expr[] {f001255});
  private static final Expr f001257 = Expr.makeLambda("_", f000947, f001256);
  private static final Expr f001258 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001176),
            new SimpleImmutableEntry<String, Expr>("Some", f001257)
          });
  private static final Expr f001259 = Expr.makeMerge(f001258, f000002, null);
  private static final Expr f001260 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001254),
            new SimpleImmutableEntry<String, Expr>("_2", f001259)
          });
  private static final Expr f001261 = Expr.makeApplication(f000373, new Expr[] {f000961});
  private static final Expr f001262 = Expr.makeLambda("_", f001261, f001260);
  private static final Expr f001263 = Expr.makeLambda("_", f000017, f001262);
  private static final Expr f001264 = Expr.makeLambda("_", f000017, f001263);
  private static final Expr f001265 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("all", f001168),
            new SimpleImmutableEntry<String, Expr>("any", f001173),
            new SimpleImmutableEntry<String, Expr>("build", f001181),
            new SimpleImmutableEntry<String, Expr>("concat", f001187),
            new SimpleImmutableEntry<String, Expr>("default", f001193),
            new SimpleImmutableEntry<String, Expr>("filter", f001202),
            new SimpleImmutableEntry<String, Expr>("fold", f001210),
            new SimpleImmutableEntry<String, Expr>("head", f001219),
            new SimpleImmutableEntry<String, Expr>("last", f001226),
            new SimpleImmutableEntry<String, Expr>("length", f001231),
            new SimpleImmutableEntry<String, Expr>("map", f001239),
            new SimpleImmutableEntry<String, Expr>("null", f001244),
            new SimpleImmutableEntry<String, Expr>("toList", f001249),
            new SimpleImmutableEntry<String, Expr>("unzip", f001264)
          });
  private static final Expr f001266 =
      Expr.makeTextLiteral(new String[] {"", "", ""}, new Expr[] {f000003, f000002});
  private static final Expr f001267 = Expr.makeLambda("_", f000183, f001266);
  private static final Expr f001268 = Expr.makeLambda("_", f000183, f001267);
  private static final Expr f001269 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000002, f000183, f001268, f000643});
  private static final Expr f001270 = Expr.makeLambda("_", f000331, f001269);
  private static final Expr f001271 =
      Expr.makeTextLiteral(new String[] {"", "", ""}, new Expr[] {f000742, f000002});
  private static final Expr f001272 = Expr.makeLambda("_", f000183, f001271);
  private static final Expr f001273 = Expr.makeLambda("_", f000014, f001272);
  private static final Expr f001274 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000183, f001273, f000643});
  private static final Expr f001275 = Expr.makeLambda("_", f000201, f001274);
  private static final Expr f001276 = Expr.makePi("_", f000002, f000183);
  private static final Expr f001277 = Expr.makeLambda("_", f001276, f001275);
  private static final Expr f001278 = Expr.makeLambda("_", f000017, f001277);
  private static final Expr f001279 = Expr.makeLambda("_", f000183, f000002);
  private static final Expr f001280 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f000643),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001279)
          });
  private static final Expr f001281 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", null),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000183)
          });
  private static final Expr f001282 = Expr.makeFieldAccess(f001281, "NonEmpty");
  private static final Expr f001283 = Expr.makeApplication(f001282, new Expr[] {f000742});
  private static final Expr f001284 = Expr.makeApplication(f000052, new Expr[] {f000014});
  private static final Expr f001285 = Expr.makeIdentifier("_", 6);
  private static final Expr f001286 =
      Expr.makeTextLiteral(new String[] {"", "", "", ""}, new Expr[] {f001284, f001285, f000002});
  private static final Expr f001287 = Expr.makeApplication(f001282, new Expr[] {f001286});
  private static final Expr f001288 = Expr.makeLambda("_", f000183, f001287);
  private static final Expr f001289 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f001283),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001288)
          });
  private static final Expr f001290 = Expr.makeMerge(f001289, f000002, null);
  private static final Expr f001291 = Expr.makeLambda("_", f001281, f001290);
  private static final Expr f001292 = Expr.makeLambda("_", f000014, f001291);
  private static final Expr f001293 = Expr.makeFieldAccess(f001281, "Empty");
  private static final Expr f001294 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f001281, f001292, f001293});
  private static final Expr f001295 = Expr.makeMerge(f001280, f001294, null);
  private static final Expr f001296 = Expr.makeLambda("_", f000201, f001295);
  private static final Expr f001297 = Expr.makeLambda("_", f001276, f001296);
  private static final Expr f001298 = Expr.makeLambda("_", f000017, f001297);
  private static final Expr f001299 = Expr.makeLambda("_", f000183, f001298);
  private static final Expr f001300 = Expr.makeApplication(f001282, new Expr[] {f000003});
  private static final Expr f001301 =
      Expr.makeTextLiteral(new String[] {"", "", "", ""}, new Expr[] {f000014, f000052, f000002});
  private static final Expr f001302 = Expr.makeApplication(f001282, new Expr[] {f001301});
  private static final Expr f001303 = Expr.makeLambda("_", f000183, f001302);
  private static final Expr f001304 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f001300),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001303)
          });
  private static final Expr f001305 = Expr.makeMerge(f001304, f000002, null);
  private static final Expr f001306 = Expr.makeLambda("_", f001281, f001305);
  private static final Expr f001307 = Expr.makeLambda("_", f000183, f001306);
  private static final Expr f001308 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000002, f001281, f001307, f001293});
  private static final Expr f001309 = Expr.makeMerge(f001280, f001308, null);
  private static final Expr f001310 = Expr.makeLambda("_", f000331, f001309);
  private static final Expr f001311 = Expr.makeLambda("_", f000183, f001310);
  private static final Expr f001312 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000643),
            new SimpleImmutableEntry<String, Expr>("Some", f001279)
          });
  private static final Expr f001313 = Expr.makeMerge(f001312, f000002, null);
  private static final Expr f001314 = Expr.makeApplication(f000373, new Expr[] {f000183});
  private static final Expr f001315 = Expr.makeLambda("_", f001314, f001313);
  private static final Expr f001316 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000643),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001317 = Expr.makeMerge(f001316, f000002, null);
  private static final Expr f001318 = Expr.makeLambda("_", f000933, f001317);
  private static final Expr f001319 = Expr.makeLambda("_", f001276, f001318);
  private static final Expr f001320 = Expr.makeLambda("_", f000017, f001319);
  private static final Expr f001321 =
      Expr.makeApplication(f000807, new Expr[] {f000003, f000331, f000464, f000334});
  private static final Expr f001322 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f001321, f000183, f001268, f000643});
  private static final Expr f001323 = Expr.makeLambda("_", f000183, f001322);
  private static final Expr f001324 = Expr.makeLambda("_", f000178, f001323);
  private static final Expr f001325 = Expr.makeTextLiteral(" ");
  private static final Expr f001326 = Expr.makeNonEmptyListLiteral(new Expr[] {f001325});
  private static final Expr f001327 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001326, f000002);
  private static final Expr f001328 = Expr.makeLambda("_", f000331, f001327);
  private static final Expr f001329 =
      Expr.makeApplication(f000807, new Expr[] {f000002, f000331, f001328, f000334});
  private static final Expr f001330 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f001329, f000183, f001268, f000643});
  private static final Expr f001331 = Expr.makeLambda("_", f000178, f001330);
  private static final Expr f001332 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("concat", f001270),
            new SimpleImmutableEntry<String, Expr>("concatMap", f001278),
            new SimpleImmutableEntry<String, Expr>("concatMapSep", f001299),
            new SimpleImmutableEntry<String, Expr>("concatSep", f001311),
            new SimpleImmutableEntry<String, Expr>("default", f001315),
            new SimpleImmutableEntry<String, Expr>("defaultMap", f001320),
            new SimpleImmutableEntry<String, Expr>("replicate", f001324),
            new SimpleImmutableEntry<String, Expr>("show", f000516),
            new SimpleImmutableEntry<String, Expr>("spaces", f001331)
          });
  private static final Expr f001333 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000183)
          });
  private static final Expr f001334 = Expr.makeApplication(f000009, new Expr[] {f001333});
  private static final Expr f001335 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001334),
            new SimpleImmutableEntry<String, Expr>("content", f000187),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001336 = Expr.makePi("_", f001335, f000003);
  private static final Expr f001337 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("element", f001336),
            new SimpleImmutableEntry<String, Expr>("text", f000196)
          });
  private static final Expr f001338 = Expr.makePi("_", f001337, f000003);
  private static final Expr f001339 = Expr.makePi("_", f000017, f001338);
  private static final Expr f001340 = Expr.makeFieldAccess(f000002, "element");
  private static final Expr f001341 = Expr.makeFieldAccess(f000014, "attributes");
  private static final Expr f001342 = Expr.makeFieldAccess(f000014, "content");
  private static final Expr f001343 = Expr.makeLambda("_", f001339, f000206);
  private static final Expr f001344 =
      Expr.makeApplication(f000000, new Expr[] {f001339, f001342, f000201, f001343, f000208});
  private static final Expr f001345 = Expr.makeFieldAccess(f000014, "name");
  private static final Expr f001346 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001341),
            new SimpleImmutableEntry<String, Expr>("content", f001344),
            new SimpleImmutableEntry<String, Expr>("name", f001345)
          });
  private static final Expr f001347 = Expr.makeApplication(f001340, new Expr[] {f001346});
  private static final Expr f001348 = Expr.makeLambda("_", f001337, f001347);
  private static final Expr f001349 = Expr.makeLambda("_", f000017, f001348);
  private static final Expr f001350 = Expr.makeApplication(f000009, new Expr[] {f001339});
  private static final Expr f001351 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001334),
            new SimpleImmutableEntry<String, Expr>("content", f001350),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001352 = Expr.makeLambda("_", f001351, f001349);
  private static final Expr f001353 = Expr.makeEmptyListLiteral(f001334);
  private static final Expr f001354 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001341),
            new SimpleImmutableEntry<String, Expr>("content", f000208),
            new SimpleImmutableEntry<String, Expr>("name", f001345)
          });
  private static final Expr f001355 = Expr.makeApplication(f001340, new Expr[] {f001354});
  private static final Expr f001356 = Expr.makeLambda("_", f001337, f001355);
  private static final Expr f001357 = Expr.makeLambda("_", f000017, f001356);
  private static final Expr f001358 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001334),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001359 = Expr.makeLambda("_", f001358, f001357);
  private static final Expr f001360 = Expr.makeFieldAccess(f000002, "name");
  private static final Expr f001361 = Expr.makeFieldAccess(f000002, "attributes");
  private static final Expr f001362 =
      Expr.makeTextLiteral(
          new String[] {" ", "=\"", "\"", ""}, new Expr[] {f000248, f000249, f000002});
  private static final Expr f001363 = Expr.makeLambda("_", f000183, f001362);
  private static final Expr f001364 = Expr.makeLambda("_", f001333, f001363);
  private static final Expr f001365 =
      Expr.makeApplication(f000000, new Expr[] {f001333, f001361, f000183, f001364, f000643});
  private static final Expr f001366 = Expr.makeFieldAccess(f000002, "content");
  private static final Expr f001367 = Expr.makeApplication(f000845, new Expr[] {f000183, f001366});
  private static final Expr f001368 = Expr.makeApplication(f000063, new Expr[] {f001367});
  private static final Expr f001369 = Expr.makeTextLiteral("/>");
  private static final Expr f001370 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f001366, f000183, f001268, f000643});
  private static final Expr f001371 =
      Expr.makeTextLiteral(new String[] {">", "</", ">"}, new Expr[] {f001370, f001360});
  private static final Expr f001372 = Expr.makeIf(f001368, f001369, f001371);
  private static final Expr f001373 =
      Expr.makeTextLiteral(new String[] {"<", "", "", ""}, new Expr[] {f001360, f001365, f001372});
  private static final Expr f001374 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001334),
            new SimpleImmutableEntry<String, Expr>("content", f000331),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001375 = Expr.makeLambda("_", f001374, f001373);
  private static final Expr f001376 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("element", f001375),
            new SimpleImmutableEntry<String, Expr>("text", f001279)
          });
  private static final Expr f001377 = Expr.makeApplication(f000002, new Expr[] {f000183, f001376});
  private static final Expr f001378 = Expr.makeLambda("_", f001339, f001377);
  private static final Expr f001379 = Expr.makeFieldAccess(f000002, "text");
  private static final Expr f001380 = Expr.makeApplication(f001379, new Expr[] {f000014});
  private static final Expr f001381 = Expr.makeLambda("_", f001337, f001380);
  private static final Expr f001382 = Expr.makeLambda("_", f000017, f001381);
  private static final Expr f001383 = Expr.makeLambda("_", f000183, f001382);
  private static final Expr f001384 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Type", f001339),
            new SimpleImmutableEntry<String, Expr>("attribute", f000232),
            new SimpleImmutableEntry<String, Expr>("element", f001352),
            new SimpleImmutableEntry<String, Expr>("emptyAttributes", f001353),
            new SimpleImmutableEntry<String, Expr>("leaf", f001359),
            new SimpleImmutableEntry<String, Expr>("render", f001378),
            new SimpleImmutableEntry<String, Expr>("text", f001383)
          });
  private static final Expr f001385 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Bool", f000047),
            new SimpleImmutableEntry<String, Expr>("Double", f000049),
            new SimpleImmutableEntry<String, Expr>("Function", f000062),
            new SimpleImmutableEntry<String, Expr>("Integer", f000182),
            new SimpleImmutableEntry<String, Expr>("JSON", f000741),
            new SimpleImmutableEntry<String, Expr>("List", f000995),
            new SimpleImmutableEntry<String, Expr>("Location", f000997),
            new SimpleImmutableEntry<String, Expr>("Map", f001064),
            new SimpleImmutableEntry<String, Expr>("Monoid", f001065),
            new SimpleImmutableEntry<String, Expr>("Natural", f001163),
            new SimpleImmutableEntry<String, Expr>("Optional", f001265),
            new SimpleImmutableEntry<String, Expr>("Text", f001332),
            new SimpleImmutableEntry<String, Expr>("XML", f001384)
          });

  public static final Expr instance = f001385;
}
