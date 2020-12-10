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
  private static final Expr f000516 = Expr.makeBuiltIn("Text/replace");
  private static final Expr f000517 = Expr.makeTextLiteral("\"");
  private static final Expr f000518 = Expr.makeTextLiteral("\\\"");
  private static final Expr f000519 = Expr.makeTextLiteral("\b");
  private static final Expr f000520 = Expr.makeTextLiteral("\\b");
  private static final Expr f000521 = Expr.makeTextLiteral("\f");
  private static final Expr f000522 = Expr.makeTextLiteral("\\f");
  private static final Expr f000523 = Expr.makeTextLiteral("\n");
  private static final Expr f000524 = Expr.makeTextLiteral("\\n");
  private static final Expr f000525 = Expr.makeTextLiteral("\r");
  private static final Expr f000526 = Expr.makeTextLiteral("\\r");
  private static final Expr f000527 = Expr.makeTextLiteral("\t");
  private static final Expr f000528 = Expr.makeTextLiteral("\\t");
  private static final Expr f000529 = Expr.makeTextLiteral("\\");
  private static final Expr f000530 = Expr.makeTextLiteral("\\\\");
  private static final Expr f000531 = Expr.makeFieldAccess(f000343, "mapKey");
  private static final Expr f000532 =
      Expr.makeApplication(f000516, new Expr[] {f000529, f000530, f000531});
  private static final Expr f000533 =
      Expr.makeApplication(f000516, new Expr[] {f000527, f000528, f000532});
  private static final Expr f000534 =
      Expr.makeApplication(f000516, new Expr[] {f000525, f000526, f000533});
  private static final Expr f000535 =
      Expr.makeApplication(f000516, new Expr[] {f000523, f000524, f000534});
  private static final Expr f000536 =
      Expr.makeApplication(f000516, new Expr[] {f000521, f000522, f000535});
  private static final Expr f000537 =
      Expr.makeApplication(f000516, new Expr[] {f000519, f000520, f000536});
  private static final Expr f000538 =
      Expr.makeApplication(f000516, new Expr[] {f000517, f000518, f000537});
  private static final Expr f000539 = Expr.makeFieldAccess(f000515, "head");
  private static final Expr f000540 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000538, f000539});
  private static final Expr f000541 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000540)});
  private static final Expr f000542 =
      Expr.makeOperatorApplication(Operator.PREFER, f000515, f000541);
  private static final Expr f000543 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000338)
          });
  private static final Expr f000544 = Expr.makeMerge(f000337, f000249, null);
  private static final Expr f000545 =
      Expr.makeApplication(f000516, new Expr[] {f000529, f000530, f000248});
  private static final Expr f000546 =
      Expr.makeApplication(f000516, new Expr[] {f000527, f000528, f000545});
  private static final Expr f000547 =
      Expr.makeApplication(f000516, new Expr[] {f000525, f000526, f000546});
  private static final Expr f000548 =
      Expr.makeApplication(f000516, new Expr[] {f000523, f000524, f000547});
  private static final Expr f000549 =
      Expr.makeApplication(f000516, new Expr[] {f000521, f000522, f000548});
  private static final Expr f000550 =
      Expr.makeApplication(f000516, new Expr[] {f000519, f000520, f000549});
  private static final Expr f000551 =
      Expr.makeApplication(f000516, new Expr[] {f000517, f000518, f000550});
  private static final Expr f000552 = Expr.makeFieldAccess(f000544, "head");
  private static final Expr f000553 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000551, f000552});
  private static final Expr f000554 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000553)});
  private static final Expr f000555 =
      Expr.makeOperatorApplication(Operator.PREFER, f000544, f000554);
  private static final Expr f000556 = Expr.makeNonEmptyListLiteral(new Expr[] {f000555});
  private static final Expr f000557 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000556, f000002);
  private static final Expr f000558 = Expr.makeLambda("_", f000346, f000557);
  private static final Expr f000559 = Expr.makeLambda("_", f000543, f000558);
  private static final Expr f000560 =
      Expr.makeApplication(f000000, new Expr[] {f000543, f000345, f000346, f000559, f000352});
  private static final Expr f000561 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000542),
            new SimpleImmutableEntry<String, Expr>("tail", f000560)
          });
  private static final Expr f000562 = Expr.makeFieldAccess(f000355, "mapKey");
  private static final Expr f000563 =
      Expr.makeApplication(f000516, new Expr[] {f000529, f000530, f000562});
  private static final Expr f000564 =
      Expr.makeApplication(f000516, new Expr[] {f000527, f000528, f000563});
  private static final Expr f000565 =
      Expr.makeApplication(f000516, new Expr[] {f000525, f000526, f000564});
  private static final Expr f000566 =
      Expr.makeApplication(f000516, new Expr[] {f000523, f000524, f000565});
  private static final Expr f000567 =
      Expr.makeApplication(f000516, new Expr[] {f000521, f000522, f000566});
  private static final Expr f000568 =
      Expr.makeApplication(f000516, new Expr[] {f000519, f000520, f000567});
  private static final Expr f000569 =
      Expr.makeApplication(f000516, new Expr[] {f000517, f000518, f000568});
  private static final Expr f000570 = Expr.makeFieldAccess(f000355, "mapValue");
  private static final Expr f000571 = Expr.makeMerge(f000337, f000570, null);
  private static final Expr f000572 = Expr.makeFieldAccess(f000571, "head");
  private static final Expr f000573 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ","}, new Expr[] {f000569, f000572});
  private static final Expr f000574 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000573),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000575 = Expr.makeFieldAccess(f000360, "mapValue");
  private static final Expr f000576 = Expr.makeMerge(f000337, f000575, null);
  private static final Expr f000577 = Expr.makeFieldAccess(f000360, "mapKey");
  private static final Expr f000578 =
      Expr.makeApplication(f000516, new Expr[] {f000529, f000530, f000577});
  private static final Expr f000579 =
      Expr.makeApplication(f000516, new Expr[] {f000527, f000528, f000578});
  private static final Expr f000580 =
      Expr.makeApplication(f000516, new Expr[] {f000525, f000526, f000579});
  private static final Expr f000581 =
      Expr.makeApplication(f000516, new Expr[] {f000523, f000524, f000580});
  private static final Expr f000582 =
      Expr.makeApplication(f000516, new Expr[] {f000521, f000522, f000581});
  private static final Expr f000583 =
      Expr.makeApplication(f000516, new Expr[] {f000519, f000520, f000582});
  private static final Expr f000584 =
      Expr.makeApplication(f000516, new Expr[] {f000517, f000518, f000583});
  private static final Expr f000585 = Expr.makeFieldAccess(f000576, "head");
  private static final Expr f000586 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000584, f000585});
  private static final Expr f000587 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000586)});
  private static final Expr f000588 =
      Expr.makeOperatorApplication(Operator.PREFER, f000576, f000587);
  private static final Expr f000589 =
      Expr.makeOperatorApplication(Operator.PREFER, f000588, f000367);
  private static final Expr f000590 = Expr.makeLambda("_", f000369, f000589);
  private static final Expr f000591 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000574),
            new SimpleImmutableEntry<String, Expr>("Some", f000590)
          });
  private static final Expr f000592 = Expr.makeFieldAccess(f000571, "tail");
  private static final Expr f000593 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000592, f000374, f000386, f000387});
  private static final Expr f000594 = Expr.makeMerge(f000591, f000593, null);
  private static final Expr f000595 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000594),
            new SimpleImmutableEntry<String, Expr>("tail", f000404)
          });
  private static final Expr f000596 = Expr.makeLambda("_", f000406, f000595);
  private static final Expr f000597 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000561),
            new SimpleImmutableEntry<String, Expr>("Some", f000596)
          });
  private static final Expr f000598 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000560, f000409, f000416, f000417});
  private static final Expr f000599 = Expr.makeMerge(f000597, f000598, null);
  private static final Expr f000600 = Expr.makeFieldAccess(f000599, "head");
  private static final Expr f000601 = Expr.makeFieldAccess(f000600, "head");
  private static final Expr f000602 =
      Expr.makeTextLiteral(new String[] {"{ ", " }"}, new Expr[] {f000601});
  private static final Expr f000603 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000602),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000604 = Expr.makeTextLiteral("{");
  private static final Expr f000605 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000569, f000572});
  private static final Expr f000606 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000605)});
  private static final Expr f000607 =
      Expr.makeOperatorApplication(Operator.PREFER, f000571, f000606);
  private static final Expr f000608 =
      Expr.makeApplication(f000000, new Expr[] {f000543, f000395, f000346, f000559, f000352});
  private static final Expr f000609 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000607),
            new SimpleImmutableEntry<String, Expr>("tail", f000608)
          });
  private static final Expr f000610 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ","}, new Expr[] {f000584, f000585});
  private static final Expr f000611 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000610),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000612 = Expr.makeFieldAccess(f000430, "mapValue");
  private static final Expr f000613 = Expr.makeMerge(f000337, f000612, null);
  private static final Expr f000614 = Expr.makeFieldAccess(f000430, "mapKey");
  private static final Expr f000615 =
      Expr.makeApplication(f000516, new Expr[] {f000529, f000530, f000614});
  private static final Expr f000616 =
      Expr.makeApplication(f000516, new Expr[] {f000527, f000528, f000615});
  private static final Expr f000617 =
      Expr.makeApplication(f000516, new Expr[] {f000525, f000526, f000616});
  private static final Expr f000618 =
      Expr.makeApplication(f000516, new Expr[] {f000523, f000524, f000617});
  private static final Expr f000619 =
      Expr.makeApplication(f000516, new Expr[] {f000521, f000522, f000618});
  private static final Expr f000620 =
      Expr.makeApplication(f000516, new Expr[] {f000519, f000520, f000619});
  private static final Expr f000621 =
      Expr.makeApplication(f000516, new Expr[] {f000517, f000518, f000620});
  private static final Expr f000622 = Expr.makeFieldAccess(f000613, "head");
  private static final Expr f000623 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000621, f000622});
  private static final Expr f000624 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000623)});
  private static final Expr f000625 =
      Expr.makeOperatorApplication(Operator.PREFER, f000613, f000624);
  private static final Expr f000626 =
      Expr.makeOperatorApplication(Operator.PREFER, f000625, f000367);
  private static final Expr f000627 = Expr.makeLambda("_", f000369, f000626);
  private static final Expr f000628 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000611),
            new SimpleImmutableEntry<String, Expr>("Some", f000627)
          });
  private static final Expr f000629 = Expr.makeFieldAccess(f000576, "tail");
  private static final Expr f000630 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000629, f000374, f000386, f000387});
  private static final Expr f000631 = Expr.makeMerge(f000628, f000630, null);
  private static final Expr f000632 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000631),
            new SimpleImmutableEntry<String, Expr>("tail", f000404)
          });
  private static final Expr f000633 = Expr.makeLambda("_", f000406, f000632);
  private static final Expr f000634 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000609),
            new SimpleImmutableEntry<String, Expr>("Some", f000633)
          });
  private static final Expr f000635 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000608, f000409, f000416, f000417});
  private static final Expr f000636 = Expr.makeMerge(f000634, f000635, null);
  private static final Expr f000637 = Expr.makeFieldAccess(f000636, "head");
  private static final Expr f000638 = Expr.makeFieldAccess(f000637, "head");
  private static final Expr f000639 = Expr.makeNonEmptyListLiteral(new Expr[] {f000638});
  private static final Expr f000640 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000639, f000362);
  private static final Expr f000641 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000640, f000403);
  private static final Expr f000642 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000641, f000331, f000452, f000334});
  private static final Expr f000643 = Expr.makeTextLiteral("}");
  private static final Expr f000644 = Expr.makeNonEmptyListLiteral(new Expr[] {f000643});
  private static final Expr f000645 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000642, f000644);
  private static final Expr f000646 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000604),
            new SimpleImmutableEntry<String, Expr>("tail", f000645)
          });
  private static final Expr f000647 = Expr.makeLambda("_", f000369, f000646);
  private static final Expr f000648 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000603),
            new SimpleImmutableEntry<String, Expr>("Some", f000647)
          });
  private static final Expr f000649 = Expr.makeFieldAccess(f000600, "tail");
  private static final Expr f000650 = Expr.makeFieldAccess(f000599, "tail");
  private static final Expr f000651 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000650, f000331, f000467, f000334});
  private static final Expr f000652 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000649, f000651);
  private static final Expr f000653 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000652, f000374, f000386, f000387});
  private static final Expr f000654 = Expr.makeMerge(f000648, f000653, null);
  private static final Expr f000655 = Expr.makeApplication(f000342, new Expr[] {f000654});
  private static final Expr f000656 = Expr.makeApplication(f000009, new Expr[] {f000543});
  private static final Expr f000657 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000543),
            new SimpleImmutableEntry<String, Expr>("tail", f000656)
          });
  private static final Expr f000658 = Expr.makeLambda("_", f000657, f000655);
  private static final Expr f000659 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000513),
            new SimpleImmutableEntry<String, Expr>("Some", f000658)
          });
  private static final Expr f000660 = Expr.makeApplication(f000477, new Expr[] {f000543, f000002});
  private static final Expr f000661 = Expr.makeApplication(f000373, new Expr[] {f000657});
  private static final Expr f000662 = Expr.makeEmptyListLiteral(f000656);
  private static final Expr f000663 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000003),
            new SimpleImmutableEntry<String, Expr>("tail", f000662)
          });
  private static final Expr f000664 = Expr.makeApplication(f000175, new Expr[] {f000663});
  private static final Expr f000665 = Expr.makeLambda("_", f000657, f000486);
  private static final Expr f000666 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000664),
            new SimpleImmutableEntry<String, Expr>("Some", f000665)
          });
  private static final Expr f000667 = Expr.makeMerge(f000666, f000002, null);
  private static final Expr f000668 = Expr.makeLambda("_", f000661, f000667);
  private static final Expr f000669 = Expr.makeLambda("_", f000543, f000668);
  private static final Expr f000670 = Expr.makeApplication(f000177, new Expr[] {f000657});
  private static final Expr f000671 =
      Expr.makeApplication(f000000, new Expr[] {f000543, f000660, f000661, f000669, f000670});
  private static final Expr f000672 = Expr.makeMerge(f000659, f000671, null);
  private static final Expr f000673 = Expr.makeLambda("_", f000656, f000672);
  private static final Expr f000674 =
      Expr.makeApplication(f000516, new Expr[] {f000529, f000530, f000002});
  private static final Expr f000675 =
      Expr.makeApplication(f000516, new Expr[] {f000527, f000528, f000674});
  private static final Expr f000676 =
      Expr.makeApplication(f000516, new Expr[] {f000525, f000526, f000675});
  private static final Expr f000677 =
      Expr.makeApplication(f000516, new Expr[] {f000523, f000524, f000676});
  private static final Expr f000678 =
      Expr.makeApplication(f000516, new Expr[] {f000521, f000522, f000677});
  private static final Expr f000679 =
      Expr.makeApplication(f000516, new Expr[] {f000519, f000520, f000678});
  private static final Expr f000680 =
      Expr.makeApplication(f000516, new Expr[] {f000517, f000518, f000679});
  private static final Expr f000681 =
      Expr.makeTextLiteral(new String[] {"\"", "\""}, new Expr[] {f000680});
  private static final Expr f000682 = Expr.makeApplication(f000339, new Expr[] {f000681});
  private static final Expr f000683 = Expr.makeLambda("_", f000183, f000682);
  private static final Expr f000684 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000495),
            new SimpleImmutableEntry<String, Expr>("bool", f000500),
            new SimpleImmutableEntry<String, Expr>("double", f000503),
            new SimpleImmutableEntry<String, Expr>("integer", f000509),
            new SimpleImmutableEntry<String, Expr>("null", f000511),
            new SimpleImmutableEntry<String, Expr>("object", f000673),
            new SimpleImmutableEntry<String, Expr>("string", f000683)
          });
  private static final Expr f000685 = Expr.makeApplication(f000002, new Expr[] {f000338, f000684});
  private static final Expr f000686 = Expr.makeMerge(f000337, f000685, null);
  private static final Expr f000687 = Expr.makeFieldAccess(f000686, "head");
  private static final Expr f000688 = Expr.makeNonEmptyListLiteral(new Expr[] {f000687});
  private static final Expr f000689 = Expr.makeFieldAccess(f000686, "tail");
  private static final Expr f000690 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000688, f000689);
  private static final Expr f000691 =
      Expr.makeTextLiteral(new String[] {"", "\n", ""}, new Expr[] {f000003, f000002});
  private static final Expr f000692 = Expr.makeLambda("_", f000183, f000691);
  private static final Expr f000693 = Expr.makeLambda("_", f000183, f000692);
  private static final Expr f000694 = Expr.makeTextLiteral("");
  private static final Expr f000695 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000690, f000183, f000693, f000694});
  private static final Expr f000696 = Expr.makeLambda("_", f000199, f000695);
  private static final Expr f000697 = Expr.makeLambda("_", f000183, f000002);
  private static final Expr f000698 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f000694),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000697)
          });
  private static final Expr f000699 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", null),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000183)
          });
  private static final Expr f000700 = Expr.makeFieldAccess(f000699, "NonEmpty");
  private static final Expr f000701 =
      Expr.makeTextLiteral(new String[] {" ", ""}, new Expr[] {f000003});
  private static final Expr f000702 = Expr.makeApplication(f000700, new Expr[] {f000701});
  private static final Expr f000703 =
      Expr.makeTextLiteral(new String[] {" ", ",", ""}, new Expr[] {f000014, f000002});
  private static final Expr f000704 = Expr.makeApplication(f000700, new Expr[] {f000703});
  private static final Expr f000705 = Expr.makeLambda("_", f000183, f000704);
  private static final Expr f000706 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f000702),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000705)
          });
  private static final Expr f000707 = Expr.makeMerge(f000706, f000002, null);
  private static final Expr f000708 = Expr.makeLambda("_", f000699, f000707);
  private static final Expr f000709 = Expr.makeLambda("_", f000183, f000708);
  private static final Expr f000710 = Expr.makeFieldAccess(f000699, "Empty");
  private static final Expr f000711 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000002, f000699, f000709, f000710});
  private static final Expr f000712 = Expr.makeMerge(f000698, f000711, null);
  private static final Expr f000713 =
      Expr.makeTextLiteral(new String[] {"[", " ]"}, new Expr[] {f000712});
  private static final Expr f000714 = Expr.makeLambda("_", f000331, f000713);
  private static final Expr f000715 = Expr.makeLambda("_", f000001, f000498);
  private static final Expr f000716 = Expr.makeLambda("_", f000071, f000507);
  private static final Expr f000717 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000183)
          });
  private static final Expr f000718 = Expr.makeBuiltIn("Text/show");
  private static final Expr f000719 = Expr.makeApplication(f000718, new Expr[] {f000248});
  private static final Expr f000720 =
      Expr.makeTextLiteral(new String[] {" ", ": ", ""}, new Expr[] {f000719, f000249});
  private static final Expr f000721 = Expr.makeApplication(f000700, new Expr[] {f000720});
  private static final Expr f000722 = Expr.makeFieldAccess(f000014, "mapKey");
  private static final Expr f000723 = Expr.makeApplication(f000718, new Expr[] {f000722});
  private static final Expr f000724 = Expr.makeFieldAccess(f000014, "mapValue");
  private static final Expr f000725 =
      Expr.makeTextLiteral(
          new String[] {" ", ": ", ",", ""}, new Expr[] {f000723, f000724, f000002});
  private static final Expr f000726 = Expr.makeApplication(f000700, new Expr[] {f000725});
  private static final Expr f000727 = Expr.makeLambda("_", f000183, f000726);
  private static final Expr f000728 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f000721),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000727)
          });
  private static final Expr f000729 = Expr.makeMerge(f000728, f000002, null);
  private static final Expr f000730 = Expr.makeLambda("_", f000699, f000729);
  private static final Expr f000731 = Expr.makeLambda("_", f000717, f000730);
  private static final Expr f000732 =
      Expr.makeApplication(f000000, new Expr[] {f000717, f000002, f000699, f000731, f000710});
  private static final Expr f000733 = Expr.makeMerge(f000698, f000732, null);
  private static final Expr f000734 =
      Expr.makeTextLiteral(new String[] {"{", " }"}, new Expr[] {f000733});
  private static final Expr f000735 = Expr.makeApplication(f000009, new Expr[] {f000717});
  private static final Expr f000736 = Expr.makeLambda("_", f000735, f000734);
  private static final Expr f000737 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000714),
            new SimpleImmutableEntry<String, Expr>("bool", f000715),
            new SimpleImmutableEntry<String, Expr>("double", f000048),
            new SimpleImmutableEntry<String, Expr>("integer", f000716),
            new SimpleImmutableEntry<String, Expr>("null", f000510),
            new SimpleImmutableEntry<String, Expr>("object", f000736),
            new SimpleImmutableEntry<String, Expr>("string", f000718)
          });
  private static final Expr f000738 = Expr.makeApplication(f000002, new Expr[] {f000183, f000737});
  private static final Expr f000739 = Expr.makeLambda("_", f000199, f000738);
  private static final Expr f000740 = Expr.makeFieldAccess(f000344, "head");
  private static final Expr f000741 =
      Expr.makeTextLiteral(new String[] {"- ", ""}, new Expr[] {f000740});
  private static final Expr f000742 = Expr.makeFieldAccess(f000344, "tail");
  private static final Expr f000743 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000742, f000331, f000452, f000334});
  private static final Expr f000744 = Expr.makeFieldAccess(f000347, "tail");
  private static final Expr f000745 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000744, f000331, f000452, f000334});
  private static final Expr f000746 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("tail", f000745)});
  private static final Expr f000747 =
      Expr.makeOperatorApplication(Operator.PREFER, f000347, f000746);
  private static final Expr f000748 = Expr.makeFieldAccess(f000347, "head");
  private static final Expr f000749 =
      Expr.makeTextLiteral(new String[] {"- ", ""}, new Expr[] {f000748});
  private static final Expr f000750 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000749)});
  private static final Expr f000751 =
      Expr.makeOperatorApplication(Operator.PREFER, f000747, f000750);
  private static final Expr f000752 = Expr.makeNonEmptyListLiteral(new Expr[] {f000751});
  private static final Expr f000753 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000752, f000002);
  private static final Expr f000754 = Expr.makeLambda("_", f000346, f000753);
  private static final Expr f000755 = Expr.makeLambda("_", f000338, f000754);
  private static final Expr f000756 =
      Expr.makeApplication(f000000, new Expr[] {f000338, f000345, f000346, f000755, f000352});
  private static final Expr f000757 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000756, f000331, f000467, f000334});
  private static final Expr f000758 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000743, f000757);
  private static final Expr f000759 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000741),
            new SimpleImmutableEntry<String, Expr>("tail", f000758)
          });
  private static final Expr f000760 = Expr.makeApplication(f000342, new Expr[] {f000759});
  private static final Expr f000761 = Expr.makeLambda("_", f000474, f000760);
  private static final Expr f000762 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000341),
            new SimpleImmutableEntry<String, Expr>("Some", f000761)
          });
  private static final Expr f000763 = Expr.makeMerge(f000762, f000493, null);
  private static final Expr f000764 = Expr.makeLambda("_", f000473, f000763);
  private static final Expr f000765 =
      Expr.makeTextLiteral(new String[] {"\"", "\":"}, new Expr[] {f000569});
  private static final Expr f000766 = Expr.makeNonEmptyListLiteral(new Expr[] {f000572});
  private static final Expr f000767 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000766, f000592);
  private static final Expr f000768 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000767, f000331, f000452, f000334});
  private static final Expr f000769 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000765),
            new SimpleImmutableEntry<String, Expr>("tail", f000768)
          });
  private static final Expr f000770 = Expr.makeLambda("_", f000332, f000769);
  private static final Expr f000771 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000569, f000002});
  private static final Expr f000772 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000771),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000773 = Expr.makeLambda("_", f000183, f000772);
  private static final Expr f000774 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000770),
            new SimpleImmutableEntry<String, Expr>("Simple", f000773)
          });
  private static final Expr f000775 = Expr.makeMerge(f000774, f000514, null);
  private static final Expr f000776 = Expr.makeFieldAccess(f000775, "head");
  private static final Expr f000777 = Expr.makeFieldAccess(f000775, "tail");
  private static final Expr f000778 =
      Expr.makeApplication(f000516, new Expr[] {f000529, f000530, f000722});
  private static final Expr f000779 =
      Expr.makeApplication(f000516, new Expr[] {f000527, f000528, f000778});
  private static final Expr f000780 =
      Expr.makeApplication(f000516, new Expr[] {f000525, f000526, f000779});
  private static final Expr f000781 =
      Expr.makeApplication(f000516, new Expr[] {f000523, f000524, f000780});
  private static final Expr f000782 =
      Expr.makeApplication(f000516, new Expr[] {f000521, f000522, f000781});
  private static final Expr f000783 =
      Expr.makeApplication(f000516, new Expr[] {f000519, f000520, f000782});
  private static final Expr f000784 =
      Expr.makeApplication(f000516, new Expr[] {f000517, f000518, f000783});
  private static final Expr f000785 =
      Expr.makeTextLiteral(new String[] {"\"", "\":"}, new Expr[] {f000784});
  private static final Expr f000786 = Expr.makeMerge(f000337, f000724, null);
  private static final Expr f000787 = Expr.makeFieldAccess(f000786, "head");
  private static final Expr f000788 = Expr.makeNonEmptyListLiteral(new Expr[] {f000787});
  private static final Expr f000789 = Expr.makeFieldAccess(f000786, "tail");
  private static final Expr f000790 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000788, f000789);
  private static final Expr f000791 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000790, f000331, f000452, f000334});
  private static final Expr f000792 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000785),
            new SimpleImmutableEntry<String, Expr>("tail", f000791)
          });
  private static final Expr f000793 = Expr.makeLambda("_", f000332, f000792);
  private static final Expr f000794 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000784, f000002});
  private static final Expr f000795 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000794),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000796 = Expr.makeLambda("_", f000183, f000795);
  private static final Expr f000797 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000793),
            new SimpleImmutableEntry<String, Expr>("Simple", f000796)
          });
  private static final Expr f000798 = Expr.makeMerge(f000797, f000249, null);
  private static final Expr f000799 = Expr.makeNonEmptyListLiteral(new Expr[] {f000798});
  private static final Expr f000800 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000799, f000002);
  private static final Expr f000801 = Expr.makeLambda("_", f000346, f000800);
  private static final Expr f000802 = Expr.makeLambda("_", f000543, f000801);
  private static final Expr f000803 =
      Expr.makeApplication(f000000, new Expr[] {f000543, f000345, f000346, f000802, f000352});
  private static final Expr f000804 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000803, f000331, f000467, f000334});
  private static final Expr f000805 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000777, f000804);
  private static final Expr f000806 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000776),
            new SimpleImmutableEntry<String, Expr>("tail", f000805)
          });
  private static final Expr f000807 = Expr.makeApplication(f000342, new Expr[] {f000806});
  private static final Expr f000808 = Expr.makeLambda("_", f000657, f000807);
  private static final Expr f000809 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000513),
            new SimpleImmutableEntry<String, Expr>("Some", f000808)
          });
  private static final Expr f000810 = Expr.makeMerge(f000809, f000671, null);
  private static final Expr f000811 = Expr.makeLambda("_", f000656, f000810);
  private static final Expr f000812 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000764),
            new SimpleImmutableEntry<String, Expr>("bool", f000500),
            new SimpleImmutableEntry<String, Expr>("double", f000503),
            new SimpleImmutableEntry<String, Expr>("integer", f000509),
            new SimpleImmutableEntry<String, Expr>("null", f000511),
            new SimpleImmutableEntry<String, Expr>("object", f000811),
            new SimpleImmutableEntry<String, Expr>("string", f000683)
          });
  private static final Expr f000813 = Expr.makeApplication(f000002, new Expr[] {f000338, f000812});
  private static final Expr f000814 = Expr.makeMerge(f000337, f000813, null);
  private static final Expr f000815 = Expr.makeFieldAccess(f000814, "head");
  private static final Expr f000816 = Expr.makeNonEmptyListLiteral(new Expr[] {f000815});
  private static final Expr f000817 = Expr.makeFieldAccess(f000814, "tail");
  private static final Expr f000818 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000816, f000817);
  private static final Expr f000819 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000818, f000183, f000693, f000694});
  private static final Expr f000820 = Expr.makeLambda("_", f000199, f000819);
  private static final Expr f000821 = Expr.makeFieldAccess(f000002, "string");
  private static final Expr f000822 = Expr.makeApplication(f000821, new Expr[] {f000014});
  private static final Expr f000823 = Expr.makeLambda("_", f000197, f000822);
  private static final Expr f000824 = Expr.makeLambda("_", f000017, f000823);
  private static final Expr f000825 = Expr.makeLambda("_", f000183, f000824);
  private static final Expr f000826 = Expr.makeFieldAccess(f000184, "Inline");
  private static final Expr f000827 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("contents", f000002),
            new SimpleImmutableEntry<String, Expr>("field", f000014),
            new SimpleImmutableEntry<String, Expr>("nesting", f000826)
          });
  private static final Expr f000828 = Expr.makeLambda("_", f000002, f000827);
  private static final Expr f000829 = Expr.makeLambda("_", f000017, f000828);
  private static final Expr f000830 = Expr.makeLambda("_", f000183, f000829);
  private static final Expr f000831 = Expr.makeFieldAccess(f000184, "Nested");
  private static final Expr f000832 = Expr.makeApplication(f000831, new Expr[] {f000025});
  private static final Expr f000833 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("contents", f000002),
            new SimpleImmutableEntry<String, Expr>("field", f000014),
            new SimpleImmutableEntry<String, Expr>("nesting", f000832)
          });
  private static final Expr f000834 = Expr.makeLambda("_", f000002, f000833);
  private static final Expr f000835 = Expr.makeLambda("_", f000017, f000834);
  private static final Expr f000836 = Expr.makeLambda("_", f000183, f000835);
  private static final Expr f000837 = Expr.makeLambda("_", f000183, f000836);
  private static final Expr f000838 =
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
            new SimpleImmutableEntry<String, Expr>("render", f000696),
            new SimpleImmutableEntry<String, Expr>("renderCompact", f000739),
            new SimpleImmutableEntry<String, Expr>("renderInteger", f000716),
            new SimpleImmutableEntry<String, Expr>("renderYAML", f000820),
            new SimpleImmutableEntry<String, Expr>("string", f000825),
            new SimpleImmutableEntry<String, Expr>("tagInline", f000830),
            new SimpleImmutableEntry<String, Expr>("tagNested", f000837)
          });
  private static final Expr f000839 = Expr.makeApplication(f000025, new Expr[] {f000003});
  private static final Expr f000840 = Expr.makeOperatorApplication(Operator.AND, f000839, f000002);
  private static final Expr f000841 = Expr.makeLambda("_", f000001, f000840);
  private static final Expr f000842 = Expr.makeLambda("_", f000014, f000841);
  private static final Expr f000843 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000001, f000842, f000007});
  private static final Expr f000844 = Expr.makeLambda("_", f000201, f000843);
  private static final Expr f000845 = Expr.makePi("_", f000002, f000001);
  private static final Expr f000846 = Expr.makeLambda("_", f000845, f000844);
  private static final Expr f000847 = Expr.makeLambda("_", f000017, f000846);
  private static final Expr f000848 = Expr.makeOperatorApplication(Operator.OR, f000839, f000002);
  private static final Expr f000849 = Expr.makeLambda("_", f000001, f000848);
  private static final Expr f000850 = Expr.makeLambda("_", f000014, f000849);
  private static final Expr f000851 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000001, f000850, f000012});
  private static final Expr f000852 = Expr.makeLambda("_", f000201, f000851);
  private static final Expr f000853 = Expr.makeLambda("_", f000845, f000852);
  private static final Expr f000854 = Expr.makeLambda("_", f000017, f000853);
  private static final Expr f000855 = Expr.makeBuiltIn("List/build");
  private static final Expr f000856 = Expr.makeApplication(f000009, new Expr[] {f000052});
  private static final Expr f000857 = Expr.makeLambda("_", f000856, f000307);
  private static final Expr f000858 = Expr.makeLambda("_", f000025, f000857);
  private static final Expr f000859 =
      Expr.makeApplication(f000000, new Expr[] {f000025, f000003, f000271, f000858, f000002});
  private static final Expr f000860 = Expr.makeLambda("_", f000205, f000859);
  private static final Expr f000861 = Expr.makeLambda("_", f000201, f000860);
  private static final Expr f000862 =
      Expr.makeApplication(f000000, new Expr[] {f000201, f000002, f000201, f000861, f000208});
  private static final Expr f000863 = Expr.makeApplication(f000009, new Expr[] {f000187});
  private static final Expr f000864 = Expr.makeLambda("_", f000863, f000862);
  private static final Expr f000865 = Expr.makeLambda("_", f000017, f000864);
  private static final Expr f000866 =
      Expr.makeApplication(f000000, new Expr[] {f000025, f000050, f000271, f000858});
  private static final Expr f000867 = Expr.makeLambda("_", f000025, f000866);
  private static final Expr f000868 =
      Expr.makeApplication(f000000, new Expr[] {f000025, f000002, f000205, f000867, f000274});
  private static final Expr f000869 = Expr.makeLambda("_", f000205, f000868);
  private static final Expr f000870 = Expr.makePi("_", f000003, f000201);
  private static final Expr f000871 = Expr.makeLambda("_", f000870, f000869);
  private static final Expr f000872 = Expr.makeLambda("_", f000017, f000871);
  private static final Expr f000873 = Expr.makeLambda("_", f000017, f000872);
  private static final Expr f000874 = Expr.makeLambda("_", f000201, f000002);
  private static final Expr f000875 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000208),
            new SimpleImmutableEntry<String, Expr>("Some", f000874)
          });
  private static final Expr f000876 = Expr.makeMerge(f000875, f000002, null);
  private static final Expr f000877 = Expr.makeApplication(f000373, new Expr[] {f000187});
  private static final Expr f000878 = Expr.makeLambda("_", f000877, f000876);
  private static final Expr f000879 = Expr.makeLambda("_", f000017, f000878);
  private static final Expr f000880 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000003)
          });
  private static final Expr f000881 = Expr.makeBuiltIn("List/indexed");
  private static final Expr f000882 = Expr.makeApplication(f000881, new Expr[] {f000003, f000002});
  private static final Expr f000883 = Expr.makeFieldAccess(f000003, "index");
  private static final Expr f000884 = Expr.makeApplication(f000076, new Expr[] {f000883, f000052});
  private static final Expr f000885 = Expr.makeApplication(f000063, new Expr[] {f000884});
  private static final Expr f000886 = Expr.makeIf(f000885, f000270, f000002);
  private static final Expr f000887 = Expr.makeLambda("_", f000205, f000886);
  private static final Expr f000888 = Expr.makeLambda("_", f000880, f000887);
  private static final Expr f000889 =
      Expr.makeApplication(f000000, new Expr[] {f000880, f000882, f000201, f000888, f000208});
  private static final Expr f000890 = Expr.makeLambda("_", f000187, f000889);
  private static final Expr f000891 = Expr.makeLambda("_", f000017, f000890);
  private static final Expr f000892 = Expr.makeLambda("_", f000178, f000891);
  private static final Expr f000893 = Expr.makeEmptyListLiteral(f000187);
  private static final Expr f000894 = Expr.makeLambda("_", f000017, f000893);
  private static final Expr f000895 = Expr.makeIf(f000839, f000307, f000002);
  private static final Expr f000896 = Expr.makeLambda("_", f000271, f000895);
  private static final Expr f000897 = Expr.makeLambda("_", f000014, f000896);
  private static final Expr f000898 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000205, f000897, f000274});
  private static final Expr f000899 = Expr.makeLambda("_", f000201, f000898);
  private static final Expr f000900 = Expr.makeLambda("_", f000845, f000899);
  private static final Expr f000901 = Expr.makeLambda("_", f000017, f000900);
  private static final Expr f000902 = Expr.Constants.EMPTY_RECORD_TYPE;
  private static final Expr f000903 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000902)
          });
  private static final Expr f000904 = Expr.makeBuiltIn("Natural/fold");
  private static final Expr f000905 = Expr.makeApplication(f000009, new Expr[] {f000902});
  private static final Expr f000906 = Expr.Constants.EMPTY_RECORD_LITERAL;
  private static final Expr f000907 = Expr.makeNonEmptyListLiteral(new Expr[] {f000906});
  private static final Expr f000908 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000907, f000002);
  private static final Expr f000909 = Expr.makeLambda("_", f000905, f000908);
  private static final Expr f000910 = Expr.makeEmptyListLiteral(f000905);
  private static final Expr f000911 =
      Expr.makeApplication(f000904, new Expr[] {f000014, f000905, f000909, f000910});
  private static final Expr f000912 = Expr.makeApplication(f000881, new Expr[] {f000902, f000911});
  private static final Expr f000913 = Expr.makeApplication(f000014, new Expr[] {f000883});
  private static final Expr f000914 = Expr.makeNonEmptyListLiteral(new Expr[] {f000913});
  private static final Expr f000915 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000914, f000002);
  private static final Expr f000916 = Expr.makeLambda("_", f000205, f000915);
  private static final Expr f000917 = Expr.makeLambda("_", f000903, f000916);
  private static final Expr f000918 =
      Expr.makeApplication(f000000, new Expr[] {f000903, f000912, f000201, f000917, f000208});
  private static final Expr f000919 = Expr.makePi("_", f000178, f000003);
  private static final Expr f000920 = Expr.makeLambda("_", f000919, f000918);
  private static final Expr f000921 = Expr.makeLambda("_", f000017, f000920);
  private static final Expr f000922 = Expr.makeLambda("_", f000178, f000921);
  private static final Expr f000923 = Expr.makeBuiltIn("List/head");
  private static final Expr f000924 = Expr.makeApplication(f000923, new Expr[] {f000003, f000889});
  private static final Expr f000925 = Expr.makeLambda("_", f000187, f000924);
  private static final Expr f000926 = Expr.makeLambda("_", f000017, f000925);
  private static final Expr f000927 = Expr.makeLambda("_", f000178, f000926);
  private static final Expr f000928 =
      Expr.makeApplication(f000904, new Expr[] {f000025, f000905, f000909, f000910});
  private static final Expr f000929 = Expr.makeApplication(f000881, new Expr[] {f000902, f000928});
  private static final Expr f000930 =
      Expr.makeApplication(f000904, new Expr[] {f000883, f000052, f000025, f000014});
  private static final Expr f000931 = Expr.makeNonEmptyListLiteral(new Expr[] {f000930});
  private static final Expr f000932 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000931, f000002);
  private static final Expr f000933 = Expr.makeLambda("_", f000271, f000932);
  private static final Expr f000934 = Expr.makeLambda("_", f000903, f000933);
  private static final Expr f000935 =
      Expr.makeApplication(f000000, new Expr[] {f000903, f000929, f000205, f000934, f000274});
  private static final Expr f000936 = Expr.makeLambda("_", f000003, f000935);
  private static final Expr f000937 = Expr.makePi("_", f000002, f000003);
  private static final Expr f000938 = Expr.makeLambda("_", f000937, f000936);
  private static final Expr f000939 = Expr.makeLambda("_", f000017, f000938);
  private static final Expr f000940 = Expr.makeLambda("_", f000178, f000939);
  private static final Expr f000941 = Expr.makeBuiltIn("List/last");
  private static final Expr f000942 = Expr.makeBuiltIn("List/length");
  private static final Expr f000943 = Expr.makeNonEmptyListLiteral(new Expr[] {f000839});
  private static final Expr f000944 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000943, f000002);
  private static final Expr f000945 = Expr.makeLambda("_", f000271, f000944);
  private static final Expr f000946 = Expr.makeLambda("_", f000025, f000945);
  private static final Expr f000947 =
      Expr.makeApplication(f000000, new Expr[] {f000025, f000002, f000205, f000946, f000274});
  private static final Expr f000948 = Expr.makeLambda("_", f000205, f000947);
  private static final Expr f000949 = Expr.makePi("_", f000003, f000003);
  private static final Expr f000950 = Expr.makeLambda("_", f000949, f000948);
  private static final Expr f000951 = Expr.makeLambda("_", f000017, f000950);
  private static final Expr f000952 = Expr.makeLambda("_", f000017, f000951);
  private static final Expr f000953 = Expr.makeApplication(f000942, new Expr[] {f000003, f000002});
  private static final Expr f000954 = Expr.makeApplication(f000063, new Expr[] {f000953});
  private static final Expr f000955 = Expr.makeLambda("_", f000187, f000954);
  private static final Expr f000956 = Expr.makeLambda("_", f000017, f000955);
  private static final Expr f000957 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000205),
            new SimpleImmutableEntry<String, Expr>("true", f000205)
          });
  private static final Expr f000958 = Expr.makeFieldAccess(f000002, "false");
  private static final Expr f000959 = Expr.makeFieldAccess(f000002, "true");
  private static final Expr f000960 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000306, f000959);
  private static final Expr f000961 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000958),
            new SimpleImmutableEntry<String, Expr>("true", f000960)
          });
  private static final Expr f000962 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000306, f000958);
  private static final Expr f000963 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000962),
            new SimpleImmutableEntry<String, Expr>("true", f000959)
          });
  private static final Expr f000964 = Expr.makeIf(f000839, f000961, f000963);
  private static final Expr f000965 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000271),
            new SimpleImmutableEntry<String, Expr>("true", f000271)
          });
  private static final Expr f000966 = Expr.makeLambda("_", f000965, f000964);
  private static final Expr f000967 = Expr.makeLambda("_", f000014, f000966);
  private static final Expr f000968 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000274),
            new SimpleImmutableEntry<String, Expr>("true", f000274)
          });
  private static final Expr f000969 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000957, f000967, f000968});
  private static final Expr f000970 = Expr.makeLambda("_", f000201, f000969);
  private static final Expr f000971 = Expr.makeLambda("_", f000845, f000970);
  private static final Expr f000972 = Expr.makeLambda("_", f000017, f000971);
  private static final Expr f000973 = Expr.makeLambda("_", f000201, f000307);
  private static final Expr f000974 =
      Expr.makeApplication(f000904, new Expr[] {f000014, f000201, f000973, f000208});
  private static final Expr f000975 = Expr.makeLambda("_", f000002, f000974);
  private static final Expr f000976 = Expr.makeLambda("_", f000017, f000975);
  private static final Expr f000977 = Expr.makeLambda("_", f000178, f000976);
  private static final Expr f000978 = Expr.makeApplication(f000009, new Expr[] {f000880});
  private static final Expr f000979 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000014)
          });
  private static final Expr f000980 = Expr.makeApplication(f000009, new Expr[] {f000979});
  private static final Expr f000981 = Expr.makePi("_", f000178, f000980);
  private static final Expr f000982 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000178),
            new SimpleImmutableEntry<String, Expr>("diff", f000981)
          });
  private static final Expr f000983 = Expr.makeFieldAccess(f000002, "count");
  private static final Expr f000984 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000025)
          });
  private static final Expr f000985 = Expr.makeApplication(f000942, new Expr[] {f000984, f000003});
  private static final Expr f000986 = Expr.makeOperatorApplication(Operator.PLUS, f000983, f000985);
  private static final Expr f000987 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000052)
          });
  private static final Expr f000988 = Expr.makeApplication(f000009, new Expr[] {f000987});
  private static final Expr f000989 = Expr.makeOperatorApplication(Operator.PLUS, f000883, f000014);
  private static final Expr f000990 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000989),
            new SimpleImmutableEntry<String, Expr>("value", f000268)
          });
  private static final Expr f000991 = Expr.makeNonEmptyListLiteral(new Expr[] {f000990});
  private static final Expr f000992 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000991, f000002);
  private static final Expr f000993 = Expr.makeIdentifier("_", 5);
  private static final Expr f000994 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000993)
          });
  private static final Expr f000995 = Expr.makeApplication(f000009, new Expr[] {f000994});
  private static final Expr f000996 = Expr.makeLambda("_", f000995, f000992);
  private static final Expr f000997 = Expr.makeLambda("_", f000987, f000996);
  private static final Expr f000998 = Expr.makeFieldAccess(f000003, "diff");
  private static final Expr f000999 = Expr.makeApplication(f000942, new Expr[] {f000987, f000014});
  private static final Expr f001000 = Expr.makeOperatorApplication(Operator.PLUS, f000002, f000999);
  private static final Expr f001001 = Expr.makeApplication(f000998, new Expr[] {f001000});
  private static final Expr f001002 =
      Expr.makeApplication(f000000, new Expr[] {f000987, f000014, f000988, f000997, f001001});
  private static final Expr f001003 = Expr.makeLambda("_", f000178, f001002);
  private static final Expr f001004 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000986),
            new SimpleImmutableEntry<String, Expr>("diff", f001003)
          });
  private static final Expr f001005 = Expr.makeApplication(f000009, new Expr[] {f000984});
  private static final Expr f001006 = Expr.makePi("_", f000178, f001005);
  private static final Expr f001007 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000178),
            new SimpleImmutableEntry<String, Expr>("diff", f001006)
          });
  private static final Expr f001008 = Expr.makeLambda("_", f001007, f001004);
  private static final Expr f001009 = Expr.makeLambda("_", f000978, f001008);
  private static final Expr f001010 = Expr.makeNaturalLiteral(new BigInteger("0"));
  private static final Expr f001011 = Expr.makeEmptyListLiteral(f000980);
  private static final Expr f001012 = Expr.makeLambda("_", f000178, f001011);
  private static final Expr f001013 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f001010),
            new SimpleImmutableEntry<String, Expr>("diff", f001012)
          });
  private static final Expr f001014 =
      Expr.makeApplication(f000000, new Expr[] {f000978, f000002, f000982, f001009, f001013});
  private static final Expr f001015 = Expr.makeFieldAccess(f001014, "diff");
  private static final Expr f001016 = Expr.makeApplication(f001015, new Expr[] {f001010});
  private static final Expr f001017 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000002)
          });
  private static final Expr f001018 = Expr.makeApplication(f000009, new Expr[] {f001017});
  private static final Expr f001019 = Expr.makeApplication(f000009, new Expr[] {f001018});
  private static final Expr f001020 = Expr.makeLambda("_", f001019, f001016);
  private static final Expr f001021 = Expr.makeLambda("_", f000017, f001020);
  private static final Expr f001022 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000885, f000012);
  private static final Expr f001023 = Expr.makeIf(f001022, f000270, f000002);
  private static final Expr f001024 = Expr.makeLambda("_", f000205, f001023);
  private static final Expr f001025 = Expr.makeLambda("_", f000880, f001024);
  private static final Expr f001026 =
      Expr.makeApplication(f000000, new Expr[] {f000880, f000882, f000201, f001025, f000208});
  private static final Expr f001027 = Expr.makeLambda("_", f000187, f001026);
  private static final Expr f001028 = Expr.makeLambda("_", f000017, f001027);
  private static final Expr f001029 = Expr.makeLambda("_", f000178, f001028);
  private static final Expr f001030 = Expr.makeApplication(f000373, new Expr[] {f000003});
  private static final Expr f001031 = Expr.makeNonEmptyListLiteral(new Expr[] {f000002});
  private static final Expr f001032 = Expr.makeLambda("_", f000014, f001031);
  private static final Expr f001033 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000274),
            new SimpleImmutableEntry<String, Expr>("Some", f001032)
          });
  private static final Expr f001034 = Expr.makeMerge(f001033, f000002, null);
  private static final Expr f001035 = Expr.makeLambda("_", f000271, f000307);
  private static final Expr f001036 = Expr.makeLambda("_", f000014, f001035);
  private static final Expr f001037 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f001034, f000205, f001036});
  private static final Expr f001038 = Expr.makeLambda("_", f001030, f001037);
  private static final Expr f001039 =
      Expr.makeApplication(f000000, new Expr[] {f001030, f000002, f000201, f001038, f000208});
  private static final Expr f001040 = Expr.makeApplication(f000373, new Expr[] {f000002});
  private static final Expr f001041 = Expr.makeApplication(f000009, new Expr[] {f001040});
  private static final Expr f001042 = Expr.makeLambda("_", f001041, f001039);
  private static final Expr f001043 = Expr.makeLambda("_", f000017, f001042);
  private static final Expr f001044 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000014),
            new SimpleImmutableEntry<String, Expr>("_2", f000003)
          });
  private static final Expr f001045 = Expr.makeFieldAccess(f000003, "_1");
  private static final Expr f001046 = Expr.makeNonEmptyListLiteral(new Expr[] {f001045});
  private static final Expr f001047 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001046, f000002);
  private static final Expr f001048 = Expr.makeLambda("_", f000271, f001047);
  private static final Expr f001049 = Expr.makeLambda("_", f001044, f001048);
  private static final Expr f001050 =
      Expr.makeApplication(f000000, new Expr[] {f001044, f000002, f000205, f001049, f000274});
  private static final Expr f001051 = Expr.makeFieldAccess(f000003, "_2");
  private static final Expr f001052 = Expr.makeNonEmptyListLiteral(new Expr[] {f001051});
  private static final Expr f001053 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001052, f000002);
  private static final Expr f001054 = Expr.makeLambda("_", f000205, f001053);
  private static final Expr f001055 = Expr.makeLambda("_", f001044, f001054);
  private static final Expr f001056 =
      Expr.makeApplication(f000000, new Expr[] {f001044, f000002, f000201, f001055, f000208});
  private static final Expr f001057 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001050),
            new SimpleImmutableEntry<String, Expr>("_2", f001056)
          });
  private static final Expr f001058 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000003),
            new SimpleImmutableEntry<String, Expr>("_2", f000002)
          });
  private static final Expr f001059 = Expr.makeApplication(f000009, new Expr[] {f001058});
  private static final Expr f001060 = Expr.makeLambda("_", f001059, f001057);
  private static final Expr f001061 = Expr.makeLambda("_", f000017, f001060);
  private static final Expr f001062 = Expr.makeLambda("_", f000017, f001061);
  private static final Expr f001063 = Expr.makeApplication(f000881, new Expr[] {f000025, f000014});
  private static final Expr f001064 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000025),
            new SimpleImmutableEntry<String, Expr>("_2", f000003)
          });
  private static final Expr f001065 = Expr.makeApplication(f000009, new Expr[] {f001064});
  private static final Expr f001066 = Expr.makeFieldAccess(f000014, "value");
  private static final Expr f001067 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001066),
            new SimpleImmutableEntry<String, Expr>("_2", f000002)
          });
  private static final Expr f001068 = Expr.makeNonEmptyListLiteral(new Expr[] {f001067});
  private static final Expr f001069 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001068, f000003);
  private static final Expr f001070 = Expr.makeLambda("_", f000025, f001069);
  private static final Expr f001071 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000002),
            new SimpleImmutableEntry<String, Expr>("Some", f001070)
          });
  private static final Expr f001072 = Expr.makeFieldAccess(f000025, "index");
  private static final Expr f001073 = Expr.makeApplication(f000076, new Expr[] {f000883, f001072});
  private static final Expr f001074 = Expr.makeApplication(f000063, new Expr[] {f001073});
  private static final Expr f001075 = Expr.makeIf(f001074, f000270, f000002);
  private static final Expr f001076 = Expr.makeLambda("_", f000856, f001075);
  private static final Expr f001077 = Expr.makeLambda("_", f000984, f001076);
  private static final Expr f001078 = Expr.makeEmptyListLiteral(f000271);
  private static final Expr f001079 =
      Expr.makeApplication(f000000, new Expr[] {f000984, f001063, f000271, f001077, f001078});
  private static final Expr f001080 = Expr.makeApplication(f000923, new Expr[] {f000025, f001079});
  private static final Expr f001081 = Expr.makeMerge(f001071, f001080, null);
  private static final Expr f001082 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000052),
            new SimpleImmutableEntry<String, Expr>("_2", f000014)
          });
  private static final Expr f001083 = Expr.makeApplication(f000009, new Expr[] {f001082});
  private static final Expr f001084 = Expr.makeLambda("_", f001083, f001081);
  private static final Expr f001085 = Expr.makeLambda("_", f000984, f001084);
  private static final Expr f001086 = Expr.makeEmptyListLiteral(f001065);
  private static final Expr f001087 =
      Expr.makeApplication(f000000, new Expr[] {f000984, f001063, f001065, f001085, f001086});
  private static final Expr f001088 = Expr.makeLambda("_", f000187, f001087);
  private static final Expr f001089 = Expr.makeLambda("_", f000017, f001088);
  private static final Expr f001090 = Expr.makeLambda("_", f000187, f001089);
  private static final Expr f001091 = Expr.makeLambda("_", f000017, f001090);
  private static final Expr f001092 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("all", f000847),
            new SimpleImmutableEntry<String, Expr>("any", f000854),
            new SimpleImmutableEntry<String, Expr>("build", f000855),
            new SimpleImmutableEntry<String, Expr>("concat", f000865),
            new SimpleImmutableEntry<String, Expr>("concatMap", f000873),
            new SimpleImmutableEntry<String, Expr>("default", f000879),
            new SimpleImmutableEntry<String, Expr>("drop", f000892),
            new SimpleImmutableEntry<String, Expr>("empty", f000894),
            new SimpleImmutableEntry<String, Expr>("filter", f000901),
            new SimpleImmutableEntry<String, Expr>("fold", f000000),
            new SimpleImmutableEntry<String, Expr>("generate", f000922),
            new SimpleImmutableEntry<String, Expr>("head", f000923),
            new SimpleImmutableEntry<String, Expr>("index", f000927),
            new SimpleImmutableEntry<String, Expr>("indexed", f000881),
            new SimpleImmutableEntry<String, Expr>("iterate", f000940),
            new SimpleImmutableEntry<String, Expr>("last", f000941),
            new SimpleImmutableEntry<String, Expr>("length", f000942),
            new SimpleImmutableEntry<String, Expr>("map", f000952),
            new SimpleImmutableEntry<String, Expr>("null", f000956),
            new SimpleImmutableEntry<String, Expr>("partition", f000972),
            new SimpleImmutableEntry<String, Expr>("replicate", f000977),
            new SimpleImmutableEntry<String, Expr>("reverse", f000477),
            new SimpleImmutableEntry<String, Expr>("shifted", f001021),
            new SimpleImmutableEntry<String, Expr>("take", f001029),
            new SimpleImmutableEntry<String, Expr>("unpackOptionals", f001043),
            new SimpleImmutableEntry<String, Expr>("unzip", f001062),
            new SimpleImmutableEntry<String, Expr>("zip", f001091)
          });
  private static final Expr f001093 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Environment", f000183),
            new SimpleImmutableEntry<String, Expr>("Local", f000183),
            new SimpleImmutableEntry<String, Expr>("Missing", null),
            new SimpleImmutableEntry<String, Expr>("Remote", f000183)
          });
  private static final Expr f001094 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("Type", f001093)});
  private static final Expr f001095 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000003),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000002)
          });
  private static final Expr f001096 = Expr.makeLambda("_", f000017, f001095);
  private static final Expr f001097 = Expr.makeLambda("_", f000017, f001096);
  private static final Expr f001098 = Expr.makeApplication(f000009, new Expr[] {f001095});
  private static final Expr f001099 = Expr.makeLambda("_", f000017, f001098);
  private static final Expr f001100 = Expr.makeLambda("_", f000017, f001099);
  private static final Expr f001101 = Expr.makeEmptyListLiteral(f001098);
  private static final Expr f001102 = Expr.makeLambda("_", f000017, f001101);
  private static final Expr f001103 = Expr.makeLambda("_", f000017, f001102);
  private static final Expr f001104 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000014),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000003)
          });
  private static final Expr f001105 = Expr.makeNonEmptyListLiteral(new Expr[] {f000248});
  private static final Expr f001106 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001105, f000002);
  private static final Expr f001107 = Expr.makeLambda("_", f000271, f001106);
  private static final Expr f001108 = Expr.makeLambda("_", f001104, f001107);
  private static final Expr f001109 =
      Expr.makeApplication(f000000, new Expr[] {f001104, f000002, f000205, f001108, f000274});
  private static final Expr f001110 = Expr.makeLambda("_", f001098, f001109);
  private static final Expr f001111 = Expr.makeLambda("_", f000017, f001110);
  private static final Expr f001112 = Expr.makeLambda("_", f000017, f001111);
  private static final Expr f001113 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000052),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000025)
          });
  private static final Expr f001114 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000052),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000014)
          });
  private static final Expr f001115 = Expr.makeApplication(f000009, new Expr[] {f001114});
  private static final Expr f001116 = Expr.makeApplication(f000025, new Expr[] {f000249});
  private static final Expr f001117 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000248),
            new SimpleImmutableEntry<String, Expr>("mapValue", f001116)
          });
  private static final Expr f001118 = Expr.makeNonEmptyListLiteral(new Expr[] {f001117});
  private static final Expr f001119 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001118, f000002);
  private static final Expr f001120 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000993),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000025)
          });
  private static final Expr f001121 = Expr.makeApplication(f000009, new Expr[] {f001120});
  private static final Expr f001122 = Expr.makeLambda("_", f001121, f001119);
  private static final Expr f001123 = Expr.makeLambda("_", f001113, f001122);
  private static final Expr f001124 = Expr.makeEmptyListLiteral(f001115);
  private static final Expr f001125 =
      Expr.makeApplication(f000000, new Expr[] {f001113, f000002, f001115, f001123, f001124});
  private static final Expr f001126 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000025),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000014)
          });
  private static final Expr f001127 = Expr.makeApplication(f000009, new Expr[] {f001126});
  private static final Expr f001128 = Expr.makeLambda("_", f001127, f001125);
  private static final Expr f001129 = Expr.makeLambda("_", f000949, f001128);
  private static final Expr f001130 = Expr.makeLambda("_", f000017, f001129);
  private static final Expr f001131 = Expr.makeLambda("_", f000017, f001130);
  private static final Expr f001132 = Expr.makeLambda("_", f000017, f001131);
  private static final Expr f001133 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000014),
            new SimpleImmutableEntry<String, Expr>("mapValue", f001030)
          });
  private static final Expr f001134 = Expr.makeApplication(f000009, new Expr[] {f001104});
  private static final Expr f001135 = Expr.makeEmptyListLiteral(f001127);
  private static final Expr f001136 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000248),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000002)
          });
  private static final Expr f001137 = Expr.makeNonEmptyListLiteral(new Expr[] {f001136});
  private static final Expr f001138 = Expr.makeLambda("_", f000014, f001137);
  private static final Expr f001139 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001135),
            new SimpleImmutableEntry<String, Expr>("Some", f001138)
          });
  private static final Expr f001140 = Expr.makeMerge(f001139, f000296, null);
  private static final Expr f001141 = Expr.makeApplication(f000009, new Expr[] {f001113});
  private static final Expr f001142 = Expr.makeLambda("_", f001141, f000307);
  private static final Expr f001143 = Expr.makeLambda("_", f001126, f001142);
  private static final Expr f001144 =
      Expr.makeApplication(f000000, new Expr[] {f001126, f001140, f001127, f001143});
  private static final Expr f001145 = Expr.makeLambda("_", f001133, f001144);
  private static final Expr f001146 = Expr.makeEmptyListLiteral(f001134);
  private static final Expr f001147 =
      Expr.makeApplication(f000000, new Expr[] {f001133, f000002, f001134, f001145, f001146});
  private static final Expr f001148 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000003),
            new SimpleImmutableEntry<String, Expr>("mapValue", f001040)
          });
  private static final Expr f001149 = Expr.makeApplication(f000009, new Expr[] {f001148});
  private static final Expr f001150 = Expr.makeLambda("_", f001149, f001147);
  private static final Expr f001151 = Expr.makeLambda("_", f000017, f001150);
  private static final Expr f001152 = Expr.makeLambda("_", f000017, f001151);
  private static final Expr f001153 = Expr.makeNonEmptyListLiteral(new Expr[] {f000249});
  private static final Expr f001154 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001153, f000002);
  private static final Expr f001155 = Expr.makeLambda("_", f000205, f001154);
  private static final Expr f001156 = Expr.makeLambda("_", f001104, f001155);
  private static final Expr f001157 =
      Expr.makeApplication(f000000, new Expr[] {f001104, f000002, f000201, f001156, f000208});
  private static final Expr f001158 = Expr.makeLambda("_", f001098, f001157);
  private static final Expr f001159 = Expr.makeLambda("_", f000017, f001158);
  private static final Expr f001160 = Expr.makeLambda("_", f000017, f001159);
  private static final Expr f001161 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Entry", f001097),
            new SimpleImmutableEntry<String, Expr>("Type", f001100),
            new SimpleImmutableEntry<String, Expr>("empty", f001103),
            new SimpleImmutableEntry<String, Expr>("keyText", f000232),
            new SimpleImmutableEntry<String, Expr>("keyValue", f000235),
            new SimpleImmutableEntry<String, Expr>("keys", f001112),
            new SimpleImmutableEntry<String, Expr>("map", f001132),
            new SimpleImmutableEntry<String, Expr>("unpackOptionals", f001152),
            new SimpleImmutableEntry<String, Expr>("values", f001160)
          });
  private static final Expr f001162 = Expr.makeLambda("_", f000017, f000188);
  private static final Expr f001163 = Expr.makeBuiltIn("Natural/build");
  private static final Expr f001164 =
      Expr.makeApplication(f000904, new Expr[] {f000002, f000905, f000909, f000910});
  private static final Expr f001165 = Expr.makeApplication(f000881, new Expr[] {f000902, f001164});
  private static final Expr f001166 = Expr.makeApplication(f000009, new Expr[] {f000178});
  private static final Expr f001167 = Expr.makeNonEmptyListLiteral(new Expr[] {f000883});
  private static final Expr f001168 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001167, f000002);
  private static final Expr f001169 = Expr.makeLambda("_", f001166, f001168);
  private static final Expr f001170 = Expr.makeLambda("_", f000903, f001169);
  private static final Expr f001171 = Expr.makeEmptyListLiteral(f001166);
  private static final Expr f001172 =
      Expr.makeApplication(f000000, new Expr[] {f000903, f001165, f001166, f001170, f001171});
  private static final Expr f001173 = Expr.makeLambda("_", f000178, f001172);
  private static final Expr f001174 = Expr.makeApplication(f000076, new Expr[] {f000002, f000003});
  private static final Expr f001175 = Expr.makeApplication(f000063, new Expr[] {f001174});
  private static final Expr f001176 = Expr.makeApplication(f000076, new Expr[] {f000003, f000002});
  private static final Expr f001177 = Expr.makeApplication(f000063, new Expr[] {f001176});
  private static final Expr f001178 = Expr.makeOperatorApplication(Operator.AND, f001175, f001177);
  private static final Expr f001179 = Expr.makeLambda("_", f000178, f001178);
  private static final Expr f001180 = Expr.makeLambda("_", f000178, f001179);
  private static final Expr f001181 = Expr.makeBuiltIn("Natural/even");
  private static final Expr f001182 =
      Expr.makeOperatorApplication(Operator.EQUALS, f001175, f000012);
  private static final Expr f001183 = Expr.makeLambda("_", f000178, f001182);
  private static final Expr f001184 = Expr.makeLambda("_", f000178, f001183);
  private static final Expr f001185 = Expr.makeLambda("_", f000178, f001177);
  private static final Expr f001186 = Expr.makeLambda("_", f000178, f001185);
  private static final Expr f001187 =
      Expr.makeOperatorApplication(Operator.EQUALS, f001177, f000012);
  private static final Expr f001188 = Expr.makeLambda("_", f000178, f001187);
  private static final Expr f001189 = Expr.makeLambda("_", f000178, f001188);
  private static final Expr f001190 = Expr.makeLambda("_", f000178, f001175);
  private static final Expr f001191 = Expr.makeLambda("_", f000178, f001190);
  private static final Expr f001192 = Expr.makeIf(f001175, f000002, f000003);
  private static final Expr f001193 = Expr.makeLambda("_", f000178, f001192);
  private static final Expr f001194 = Expr.makeLambda("_", f000178, f001193);
  private static final Expr f001195 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000003, f000178, f001194, f000002});
  private static final Expr f001196 = Expr.makeApplication(f000175, new Expr[] {f001195});
  private static final Expr f001197 = Expr.makeLambda("_", f000178, f001196);
  private static final Expr f001198 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000179),
            new SimpleImmutableEntry<String, Expr>("Some", f001197)
          });
  private static final Expr f001199 = Expr.makeApplication(f000923, new Expr[] {f000178, f000002});
  private static final Expr f001200 = Expr.makeMerge(f001198, f001199, null);
  private static final Expr f001201 = Expr.makeLambda("_", f001166, f001200);
  private static final Expr f001202 = Expr.makeApplication(f000063, new Expr[] {f000002});
  private static final Expr f001203 = Expr.makeIf(f001175, f000003, f000002);
  private static final Expr f001204 = Expr.makeLambda("_", f000178, f001203);
  private static final Expr f001205 = Expr.makeLambda("_", f000178, f001204);
  private static final Expr f001206 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000003, f000178, f001205, f000002});
  private static final Expr f001207 = Expr.makeIf(f001202, f000002, f001206);
  private static final Expr f001208 = Expr.makeApplication(f000175, new Expr[] {f001207});
  private static final Expr f001209 = Expr.makeLambda("_", f000178, f001208);
  private static final Expr f001210 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000179),
            new SimpleImmutableEntry<String, Expr>("Some", f001209)
          });
  private static final Expr f001211 = Expr.makeMerge(f001210, f001199, null);
  private static final Expr f001212 = Expr.makeLambda("_", f001166, f001211);
  private static final Expr f001213 = Expr.makeBuiltIn("Natural/odd");
  private static final Expr f001214 =
      Expr.makeOperatorApplication(Operator.TIMES, f000003, f000002);
  private static final Expr f001215 = Expr.makeLambda("_", f000178, f001214);
  private static final Expr f001216 = Expr.makeLambda("_", f000178, f001215);
  private static final Expr f001217 = Expr.makeNaturalLiteral(new BigInteger("1"));
  private static final Expr f001218 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000002, f000178, f001216, f001217});
  private static final Expr f001219 = Expr.makeLambda("_", f001166, f001218);
  private static final Expr f001220 = Expr.makeApplication(f000942, new Expr[] {f000178, f000002});
  private static final Expr f001221 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f001166),
            new SimpleImmutableEntry<String, Expr>("sorted", f001166)
          });
  private static final Expr f001222 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f001171),
            new SimpleImmutableEntry<String, Expr>("true", f001171)
          });
  private static final Expr f001223 = Expr.makeFieldAccess(f000003, "rest");
  private static final Expr f001224 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f001166),
            new SimpleImmutableEntry<String, Expr>("true", f001166)
          });
  private static final Expr f001225 = Expr.makeApplication(f000076, new Expr[] {f000014, f000003});
  private static final Expr f001226 = Expr.makeApplication(f000063, new Expr[] {f001225});
  private static final Expr f001227 = Expr.makeIf(f001226, f000961, f000963);
  private static final Expr f001228 = Expr.makeLambda("_", f001224, f001227);
  private static final Expr f001229 = Expr.makeLambda("_", f000178, f001228);
  private static final Expr f001230 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f001223, f001224, f001229, f001222});
  private static final Expr f001231 = Expr.makeLambda("_", f000178, f001230);
  private static final Expr f001232 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001222),
            new SimpleImmutableEntry<String, Expr>("Some", f001231)
          });
  private static final Expr f001233 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f001223, f000178, f001205, f000002});
  private static final Expr f001234 = Expr.makeIf(f001202, f000002, f001233);
  private static final Expr f001235 = Expr.makeApplication(f000175, new Expr[] {f001234});
  private static final Expr f001236 = Expr.makeLambda("_", f000178, f001235);
  private static final Expr f001237 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000179),
            new SimpleImmutableEntry<String, Expr>("Some", f001236)
          });
  private static final Expr f001238 = Expr.makeFieldAccess(f000002, "rest");
  private static final Expr f001239 = Expr.makeApplication(f000923, new Expr[] {f000178, f001238});
  private static final Expr f001240 = Expr.makeMerge(f001237, f001239, null);
  private static final Expr f001241 = Expr.makeMerge(f001232, f001240, null);
  private static final Expr f001242 = Expr.makeFieldAccess(f001241, "false");
  private static final Expr f001243 = Expr.makeFieldAccess(f000002, "sorted");
  private static final Expr f001244 = Expr.makeFieldAccess(f001241, "true");
  private static final Expr f001245 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001243, f001244);
  private static final Expr f001246 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f001242),
            new SimpleImmutableEntry<String, Expr>("sorted", f001245)
          });
  private static final Expr f001247 = Expr.makeLambda("_", f001221, f001246);
  private static final Expr f001248 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f000002),
            new SimpleImmutableEntry<String, Expr>("sorted", f001171)
          });
  private static final Expr f001249 =
      Expr.makeApplication(f000904, new Expr[] {f001220, f001221, f001247, f001248});
  private static final Expr f001250 = Expr.makeFieldAccess(f001249, "sorted");
  private static final Expr f001251 = Expr.makeLambda("_", f001166, f001250);
  private static final Expr f001252 = Expr.makeOperatorApplication(Operator.PLUS, f000003, f000002);
  private static final Expr f001253 = Expr.makeLambda("_", f000178, f001252);
  private static final Expr f001254 = Expr.makeLambda("_", f000178, f001253);
  private static final Expr f001255 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000002, f000178, f001254, f001010});
  private static final Expr f001256 = Expr.makeLambda("_", f001166, f001255);
  private static final Expr f001257 = Expr.makeApplication(f000081, new Expr[] {f000002});
  private static final Expr f001258 = Expr.makeApplication(f000174, new Expr[] {f001257});
  private static final Expr f001259 = Expr.makeLambda("_", f000178, f001258);
  private static final Expr f001260 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("build", f001163),
            new SimpleImmutableEntry<String, Expr>("enumerate", f001173),
            new SimpleImmutableEntry<String, Expr>("equal", f001180),
            new SimpleImmutableEntry<String, Expr>("even", f001181),
            new SimpleImmutableEntry<String, Expr>("fold", f000904),
            new SimpleImmutableEntry<String, Expr>("greaterThan", f001184),
            new SimpleImmutableEntry<String, Expr>("greaterThanEqual", f001186),
            new SimpleImmutableEntry<String, Expr>("isZero", f000063),
            new SimpleImmutableEntry<String, Expr>("lessThan", f001189),
            new SimpleImmutableEntry<String, Expr>("lessThanEqual", f001191),
            new SimpleImmutableEntry<String, Expr>("listMax", f001201),
            new SimpleImmutableEntry<String, Expr>("listMin", f001212),
            new SimpleImmutableEntry<String, Expr>("max", f001194),
            new SimpleImmutableEntry<String, Expr>("min", f001205),
            new SimpleImmutableEntry<String, Expr>("odd", f001213),
            new SimpleImmutableEntry<String, Expr>("product", f001219),
            new SimpleImmutableEntry<String, Expr>("show", f000504),
            new SimpleImmutableEntry<String, Expr>("sort", f001251),
            new SimpleImmutableEntry<String, Expr>("subtract", f000076),
            new SimpleImmutableEntry<String, Expr>("sum", f001256),
            new SimpleImmutableEntry<String, Expr>("toDouble", f001259),
            new SimpleImmutableEntry<String, Expr>("toInteger", f000081)
          });
  private static final Expr f001261 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000003, f000002);
  private static final Expr f001262 = Expr.makeLambda("_", f000201, f001261);
  private static final Expr f001263 = Expr.makeLambda("_", f000187, f001262);
  private static final Expr f001264 = Expr.makeLambda("_", f000017, f001263);
  private static final Expr f001265 =
      Expr.makeTextLiteral(new String[] {"", "", ""}, new Expr[] {f000003, f000002});
  private static final Expr f001266 = Expr.makeLambda("_", f000183, f001265);
  private static final Expr f001267 = Expr.makeLambda("_", f000183, f001266);
  private static final Expr f001268 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("!=", f000035),
            new SimpleImmutableEntry<String, Expr>("#", f001264),
            new SimpleImmutableEntry<String, Expr>("&&", f000006),
            new SimpleImmutableEntry<String, Expr>("*", f001216),
            new SimpleImmutableEntry<String, Expr>("+", f001254),
            new SimpleImmutableEntry<String, Expr>("++", f001267),
            new SimpleImmutableEntry<String, Expr>("==", f000022),
            new SimpleImmutableEntry<String, Expr>("||", f000040)
          });
  private static final Expr f001269 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000007),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001270 = Expr.makeMerge(f001269, f000002, null);
  private static final Expr f001271 = Expr.makeLambda("_", f001030, f001270);
  private static final Expr f001272 = Expr.makeLambda("_", f000845, f001271);
  private static final Expr f001273 = Expr.makeLambda("_", f000017, f001272);
  private static final Expr f001274 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000012),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001275 = Expr.makeMerge(f001274, f000002, null);
  private static final Expr f001276 = Expr.makeLambda("_", f001030, f001275);
  private static final Expr f001277 = Expr.makeLambda("_", f000845, f001276);
  private static final Expr f001278 = Expr.makeLambda("_", f000017, f001277);
  private static final Expr f001279 = Expr.makeApplication(f000175, new Expr[] {f000002});
  private static final Expr f001280 = Expr.makeLambda("_", f000003, f001279);
  private static final Expr f001281 = Expr.makeApplication(f000177, new Expr[] {f000003});
  private static final Expr f001282 =
      Expr.makeApplication(f000002, new Expr[] {f001030, f001280, f001281});
  private static final Expr f001283 = Expr.makePi("_", f000949, f000015);
  private static final Expr f001284 = Expr.makePi("_", f000017, f001283);
  private static final Expr f001285 = Expr.makeLambda("_", f001284, f001282);
  private static final Expr f001286 = Expr.makeLambda("_", f000017, f001285);
  private static final Expr f001287 = Expr.makeLambda("_", f001030, f000002);
  private static final Expr f001288 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001281),
            new SimpleImmutableEntry<String, Expr>("Some", f001287)
          });
  private static final Expr f001289 = Expr.makeMerge(f001288, f000002, null);
  private static final Expr f001290 = Expr.makeApplication(f000373, new Expr[] {f001040});
  private static final Expr f001291 = Expr.makeLambda("_", f001290, f001289);
  private static final Expr f001292 = Expr.makeLambda("_", f000017, f001291);
  private static final Expr f001293 = Expr.makeApplication(f000177, new Expr[] {f000014});
  private static final Expr f001294 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001293),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001295 = Expr.makeMerge(f001294, f000002, null);
  private static final Expr f001296 = Expr.makeApplication(f000373, new Expr[] {f000014});
  private static final Expr f001297 = Expr.makeLambda("_", f001296, f001295);
  private static final Expr f001298 = Expr.makePi("_", f000003, f001030);
  private static final Expr f001299 = Expr.makeLambda("_", f001298, f001297);
  private static final Expr f001300 = Expr.makeLambda("_", f000017, f001299);
  private static final Expr f001301 = Expr.makeLambda("_", f000017, f001300);
  private static final Expr f001302 = Expr.makeLambda("_", f000014, f000002);
  private static final Expr f001303 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000003),
            new SimpleImmutableEntry<String, Expr>("Some", f001302)
          });
  private static final Expr f001304 = Expr.makeMerge(f001303, f000002, null);
  private static final Expr f001305 = Expr.makeLambda("_", f001030, f001304);
  private static final Expr f001306 = Expr.makeLambda("_", f000002, f001305);
  private static final Expr f001307 = Expr.makeLambda("_", f000017, f001306);
  private static final Expr f001308 = Expr.makeApplication(f000177, new Expr[] {f000025});
  private static final Expr f001309 = Expr.makeIf(f000050, f001279, f001308);
  private static final Expr f001310 = Expr.makeLambda("_", f000014, f001309);
  private static final Expr f001311 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001293),
            new SimpleImmutableEntry<String, Expr>("Some", f001310)
          });
  private static final Expr f001312 = Expr.makeMerge(f001311, f000002, null);
  private static final Expr f001313 = Expr.makeLambda("_", f001030, f001312);
  private static final Expr f001314 = Expr.makeLambda("_", f000845, f001313);
  private static final Expr f001315 = Expr.makeLambda("_", f000017, f001314);
  private static final Expr f001316 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000002),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001317 = Expr.makeMerge(f001316, f000025, null);
  private static final Expr f001318 = Expr.makeLambda("_", f000003, f001317);
  private static final Expr f001319 = Expr.makePi("_", f000014, f000003);
  private static final Expr f001320 = Expr.makeLambda("_", f001319, f001318);
  private static final Expr f001321 = Expr.makeLambda("_", f000017, f001320);
  private static final Expr f001322 = Expr.makeLambda("_", f001040, f001321);
  private static final Expr f001323 = Expr.makeLambda("_", f000017, f001322);
  private static final Expr f001324 = Expr.makeLambda("_", f000025, f001279);
  private static final Expr f001325 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000002),
            new SimpleImmutableEntry<String, Expr>("Some", f001324)
          });
  private static final Expr f001326 = Expr.makeMerge(f001325, f000003, null);
  private static final Expr f001327 = Expr.makeLambda("_", f001296, f001326);
  private static final Expr f001328 = Expr.makeLambda("_", f001030, f001327);
  private static final Expr f001329 =
      Expr.makeApplication(f000000, new Expr[] {f001030, f000002, f001030, f001328, f001281});
  private static final Expr f001330 = Expr.makeLambda("_", f001041, f001329);
  private static final Expr f001331 = Expr.makeLambda("_", f000017, f001330);
  private static final Expr f001332 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000003),
            new SimpleImmutableEntry<String, Expr>("Some", f001324)
          });
  private static final Expr f001333 = Expr.makeMerge(f001332, f000002, null);
  private static final Expr f001334 = Expr.makeLambda("_", f001296, f001333);
  private static final Expr f001335 = Expr.makeLambda("_", f001030, f001334);
  private static final Expr f001336 =
      Expr.makeApplication(f000000, new Expr[] {f001030, f000002, f001030, f001335, f001281});
  private static final Expr f001337 = Expr.makeLambda("_", f001041, f001336);
  private static final Expr f001338 = Expr.makeLambda("_", f000017, f001337);
  private static final Expr f001339 = Expr.makeLambda("_", f000003, f001217);
  private static final Expr f001340 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001010),
            new SimpleImmutableEntry<String, Expr>("Some", f001339)
          });
  private static final Expr f001341 = Expr.makeMerge(f001340, f000002, null);
  private static final Expr f001342 = Expr.makeLambda("_", f001040, f001341);
  private static final Expr f001343 = Expr.makeLambda("_", f000017, f001342);
  private static final Expr f001344 = Expr.makeApplication(f000175, new Expr[] {f000050});
  private static final Expr f001345 = Expr.makeLambda("_", f000025, f001344);
  private static final Expr f001346 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001293),
            new SimpleImmutableEntry<String, Expr>("Some", f001345)
          });
  private static final Expr f001347 = Expr.makeMerge(f001346, f000002, null);
  private static final Expr f001348 = Expr.makeLambda("_", f001296, f001347);
  private static final Expr f001349 = Expr.makeLambda("_", f000949, f001348);
  private static final Expr f001350 = Expr.makeLambda("_", f000017, f001349);
  private static final Expr f001351 = Expr.makeLambda("_", f000017, f001350);
  private static final Expr f001352 = Expr.makeLambda("_", f000003, f000012);
  private static final Expr f001353 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000007),
            new SimpleImmutableEntry<String, Expr>("Some", f001352)
          });
  private static final Expr f001354 = Expr.makeMerge(f001353, f000002, null);
  private static final Expr f001355 = Expr.makeLambda("_", f001040, f001354);
  private static final Expr f001356 = Expr.makeLambda("_", f000017, f001355);
  private static final Expr f001357 = Expr.makeLambda("_", f000003, f001031);
  private static final Expr f001358 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000208),
            new SimpleImmutableEntry<String, Expr>("Some", f001357)
          });
  private static final Expr f001359 = Expr.makeMerge(f001358, f000002, null);
  private static final Expr f001360 = Expr.makeLambda("_", f001040, f001359);
  private static final Expr f001361 = Expr.makeLambda("_", f000017, f001360);
  private static final Expr f001362 = Expr.makeFieldAccess(f000002, "_1");
  private static final Expr f001363 = Expr.makeApplication(f000175, new Expr[] {f001362});
  private static final Expr f001364 = Expr.makeLambda("_", f001044, f001363);
  private static final Expr f001365 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001293),
            new SimpleImmutableEntry<String, Expr>("Some", f001364)
          });
  private static final Expr f001366 = Expr.makeMerge(f001365, f000002, null);
  private static final Expr f001367 = Expr.makeFieldAccess(f000002, "_2");
  private static final Expr f001368 = Expr.makeApplication(f000175, new Expr[] {f001367});
  private static final Expr f001369 = Expr.makeLambda("_", f001044, f001368);
  private static final Expr f001370 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001281),
            new SimpleImmutableEntry<String, Expr>("Some", f001369)
          });
  private static final Expr f001371 = Expr.makeMerge(f001370, f000002, null);
  private static final Expr f001372 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001366),
            new SimpleImmutableEntry<String, Expr>("_2", f001371)
          });
  private static final Expr f001373 = Expr.makeApplication(f000373, new Expr[] {f001058});
  private static final Expr f001374 = Expr.makeLambda("_", f001373, f001372);
  private static final Expr f001375 = Expr.makeLambda("_", f000017, f001374);
  private static final Expr f001376 = Expr.makeLambda("_", f000017, f001375);
  private static final Expr f001377 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("all", f001273),
            new SimpleImmutableEntry<String, Expr>("any", f001278),
            new SimpleImmutableEntry<String, Expr>("build", f001286),
            new SimpleImmutableEntry<String, Expr>("concat", f001292),
            new SimpleImmutableEntry<String, Expr>("concatMap", f001301),
            new SimpleImmutableEntry<String, Expr>("default", f001307),
            new SimpleImmutableEntry<String, Expr>("filter", f001315),
            new SimpleImmutableEntry<String, Expr>("fold", f001323),
            new SimpleImmutableEntry<String, Expr>("head", f001331),
            new SimpleImmutableEntry<String, Expr>("last", f001338),
            new SimpleImmutableEntry<String, Expr>("length", f001343),
            new SimpleImmutableEntry<String, Expr>("map", f001351),
            new SimpleImmutableEntry<String, Expr>("null", f001356),
            new SimpleImmutableEntry<String, Expr>("toList", f001361),
            new SimpleImmutableEntry<String, Expr>("unzip", f001376)
          });
  private static final Expr f001378 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000002, f000183, f001267, f000694});
  private static final Expr f001379 = Expr.makeLambda("_", f000331, f001378);
  private static final Expr f001380 =
      Expr.makeTextLiteral(new String[] {"", "", ""}, new Expr[] {f000839, f000002});
  private static final Expr f001381 = Expr.makeLambda("_", f000183, f001380);
  private static final Expr f001382 = Expr.makeLambda("_", f000014, f001381);
  private static final Expr f001383 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000183, f001382, f000694});
  private static final Expr f001384 = Expr.makeLambda("_", f000201, f001383);
  private static final Expr f001385 = Expr.makePi("_", f000002, f000183);
  private static final Expr f001386 = Expr.makeLambda("_", f001385, f001384);
  private static final Expr f001387 = Expr.makeLambda("_", f000017, f001386);
  private static final Expr f001388 = Expr.makeApplication(f000700, new Expr[] {f000839});
  private static final Expr f001389 = Expr.makeApplication(f000052, new Expr[] {f000014});
  private static final Expr f001390 = Expr.makeIdentifier("_", 6);
  private static final Expr f001391 =
      Expr.makeTextLiteral(new String[] {"", "", "", ""}, new Expr[] {f001389, f001390, f000002});
  private static final Expr f001392 = Expr.makeApplication(f000700, new Expr[] {f001391});
  private static final Expr f001393 = Expr.makeLambda("_", f000183, f001392);
  private static final Expr f001394 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f001388),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001393)
          });
  private static final Expr f001395 = Expr.makeMerge(f001394, f000002, null);
  private static final Expr f001396 = Expr.makeLambda("_", f000699, f001395);
  private static final Expr f001397 = Expr.makeLambda("_", f000014, f001396);
  private static final Expr f001398 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000699, f001397, f000710});
  private static final Expr f001399 = Expr.makeMerge(f000698, f001398, null);
  private static final Expr f001400 = Expr.makeLambda("_", f000201, f001399);
  private static final Expr f001401 = Expr.makeLambda("_", f001385, f001400);
  private static final Expr f001402 = Expr.makeLambda("_", f000017, f001401);
  private static final Expr f001403 = Expr.makeLambda("_", f000183, f001402);
  private static final Expr f001404 = Expr.makeApplication(f000700, new Expr[] {f000003});
  private static final Expr f001405 =
      Expr.makeTextLiteral(new String[] {"", "", "", ""}, new Expr[] {f000014, f000052, f000002});
  private static final Expr f001406 = Expr.makeApplication(f000700, new Expr[] {f001405});
  private static final Expr f001407 = Expr.makeLambda("_", f000183, f001406);
  private static final Expr f001408 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f001404),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001407)
          });
  private static final Expr f001409 = Expr.makeMerge(f001408, f000002, null);
  private static final Expr f001410 = Expr.makeLambda("_", f000699, f001409);
  private static final Expr f001411 = Expr.makeLambda("_", f000183, f001410);
  private static final Expr f001412 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000002, f000699, f001411, f000710});
  private static final Expr f001413 = Expr.makeMerge(f000698, f001412, null);
  private static final Expr f001414 = Expr.makeLambda("_", f000331, f001413);
  private static final Expr f001415 = Expr.makeLambda("_", f000183, f001414);
  private static final Expr f001416 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000694),
            new SimpleImmutableEntry<String, Expr>("Some", f000697)
          });
  private static final Expr f001417 = Expr.makeMerge(f001416, f000002, null);
  private static final Expr f001418 = Expr.makeApplication(f000373, new Expr[] {f000183});
  private static final Expr f001419 = Expr.makeLambda("_", f001418, f001417);
  private static final Expr f001420 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000694),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001421 = Expr.makeMerge(f001420, f000002, null);
  private static final Expr f001422 = Expr.makeLambda("_", f001030, f001421);
  private static final Expr f001423 = Expr.makeLambda("_", f001385, f001422);
  private static final Expr f001424 = Expr.makeLambda("_", f000017, f001423);
  private static final Expr f001425 =
      Expr.makeApplication(f000904, new Expr[] {f000003, f000331, f000464, f000334});
  private static final Expr f001426 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f001425, f000183, f001267, f000694});
  private static final Expr f001427 = Expr.makeLambda("_", f000183, f001426);
  private static final Expr f001428 = Expr.makeLambda("_", f000178, f001427);
  private static final Expr f001429 = Expr.makeTextLiteral(" ");
  private static final Expr f001430 = Expr.makeNonEmptyListLiteral(new Expr[] {f001429});
  private static final Expr f001431 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001430, f000002);
  private static final Expr f001432 = Expr.makeLambda("_", f000331, f001431);
  private static final Expr f001433 =
      Expr.makeApplication(f000904, new Expr[] {f000002, f000331, f001432, f000334});
  private static final Expr f001434 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f001433, f000183, f001267, f000694});
  private static final Expr f001435 = Expr.makeLambda("_", f000178, f001434);
  private static final Expr f001436 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("concat", f001379),
            new SimpleImmutableEntry<String, Expr>("concatMap", f001387),
            new SimpleImmutableEntry<String, Expr>("concatMapSep", f001403),
            new SimpleImmutableEntry<String, Expr>("concatSep", f001415),
            new SimpleImmutableEntry<String, Expr>("default", f001419),
            new SimpleImmutableEntry<String, Expr>("defaultMap", f001424),
            new SimpleImmutableEntry<String, Expr>("replace", f000516),
            new SimpleImmutableEntry<String, Expr>("replicate", f001428),
            new SimpleImmutableEntry<String, Expr>("show", f000718),
            new SimpleImmutableEntry<String, Expr>("spaces", f001435)
          });
  private static final Expr f001437 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f000735),
            new SimpleImmutableEntry<String, Expr>("content", f000187),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001438 = Expr.makePi("_", f001437, f000003);
  private static final Expr f001439 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("element", f001438),
            new SimpleImmutableEntry<String, Expr>("text", f000196)
          });
  private static final Expr f001440 = Expr.makePi("_", f001439, f000003);
  private static final Expr f001441 = Expr.makePi("_", f000017, f001440);
  private static final Expr f001442 = Expr.makeFieldAccess(f000002, "element");
  private static final Expr f001443 = Expr.makeFieldAccess(f000014, "attributes");
  private static final Expr f001444 = Expr.makeFieldAccess(f000014, "content");
  private static final Expr f001445 = Expr.makeLambda("_", f001441, f000206);
  private static final Expr f001446 =
      Expr.makeApplication(f000000, new Expr[] {f001441, f001444, f000201, f001445, f000208});
  private static final Expr f001447 = Expr.makeFieldAccess(f000014, "name");
  private static final Expr f001448 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001443),
            new SimpleImmutableEntry<String, Expr>("content", f001446),
            new SimpleImmutableEntry<String, Expr>("name", f001447)
          });
  private static final Expr f001449 = Expr.makeApplication(f001442, new Expr[] {f001448});
  private static final Expr f001450 = Expr.makeLambda("_", f001439, f001449);
  private static final Expr f001451 = Expr.makeLambda("_", f000017, f001450);
  private static final Expr f001452 = Expr.makeApplication(f000009, new Expr[] {f001441});
  private static final Expr f001453 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f000735),
            new SimpleImmutableEntry<String, Expr>("content", f001452),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001454 = Expr.makeLambda("_", f001453, f001451);
  private static final Expr f001455 = Expr.makeEmptyListLiteral(f000735);
  private static final Expr f001456 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001443),
            new SimpleImmutableEntry<String, Expr>("content", f000208),
            new SimpleImmutableEntry<String, Expr>("name", f001447)
          });
  private static final Expr f001457 = Expr.makeApplication(f001442, new Expr[] {f001456});
  private static final Expr f001458 = Expr.makeLambda("_", f001439, f001457);
  private static final Expr f001459 = Expr.makeLambda("_", f000017, f001458);
  private static final Expr f001460 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f000735),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001461 = Expr.makeLambda("_", f001460, f001459);
  private static final Expr f001462 = Expr.makeFieldAccess(f000002, "name");
  private static final Expr f001463 = Expr.makeFieldAccess(f000002, "attributes");
  private static final Expr f001464 = Expr.makeTextLiteral("<");
  private static final Expr f001465 = Expr.makeTextLiteral("\\<");
  private static final Expr f001466 = Expr.makeTextLiteral("&");
  private static final Expr f001467 = Expr.makeTextLiteral("\\&");
  private static final Expr f001468 =
      Expr.makeApplication(f000516, new Expr[] {f001466, f001467, f000249});
  private static final Expr f001469 =
      Expr.makeApplication(f000516, new Expr[] {f001464, f001465, f001468});
  private static final Expr f001470 =
      Expr.makeApplication(f000516, new Expr[] {f000517, f000518, f001469});
  private static final Expr f001471 =
      Expr.makeTextLiteral(
          new String[] {" ", "=\"", "\"", ""}, new Expr[] {f000248, f001470, f000002});
  private static final Expr f001472 = Expr.makeLambda("_", f000183, f001471);
  private static final Expr f001473 = Expr.makeLambda("_", f000717, f001472);
  private static final Expr f001474 =
      Expr.makeApplication(f000000, new Expr[] {f000717, f001463, f000183, f001473, f000694});
  private static final Expr f001475 = Expr.makeFieldAccess(f000002, "content");
  private static final Expr f001476 = Expr.makeApplication(f000942, new Expr[] {f000183, f001475});
  private static final Expr f001477 = Expr.makeApplication(f000063, new Expr[] {f001476});
  private static final Expr f001478 = Expr.makeTextLiteral("/>");
  private static final Expr f001479 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f001475, f000183, f001267, f000694});
  private static final Expr f001480 =
      Expr.makeTextLiteral(new String[] {">", "</", ">"}, new Expr[] {f001479, f001462});
  private static final Expr f001481 = Expr.makeIf(f001477, f001478, f001480);
  private static final Expr f001482 =
      Expr.makeTextLiteral(new String[] {"<", "", "", ""}, new Expr[] {f001462, f001474, f001481});
  private static final Expr f001483 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f000735),
            new SimpleImmutableEntry<String, Expr>("content", f000331),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001484 = Expr.makeLambda("_", f001483, f001482);
  private static final Expr f001485 =
      Expr.makeApplication(f000516, new Expr[] {f001466, f001467, f000002});
  private static final Expr f001486 =
      Expr.makeApplication(f000516, new Expr[] {f001464, f001465, f001485});
  private static final Expr f001487 = Expr.makeLambda("_", f000183, f001486);
  private static final Expr f001488 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("element", f001484),
            new SimpleImmutableEntry<String, Expr>("text", f001487)
          });
  private static final Expr f001489 = Expr.makeApplication(f000002, new Expr[] {f000183, f001488});
  private static final Expr f001490 = Expr.makeLambda("_", f001441, f001489);
  private static final Expr f001491 = Expr.makeFieldAccess(f000002, "text");
  private static final Expr f001492 = Expr.makeApplication(f001491, new Expr[] {f000014});
  private static final Expr f001493 = Expr.makeLambda("_", f001439, f001492);
  private static final Expr f001494 = Expr.makeLambda("_", f000017, f001493);
  private static final Expr f001495 = Expr.makeLambda("_", f000183, f001494);
  private static final Expr f001496 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Type", f001441),
            new SimpleImmutableEntry<String, Expr>("attribute", f000232),
            new SimpleImmutableEntry<String, Expr>("element", f001454),
            new SimpleImmutableEntry<String, Expr>("emptyAttributes", f001455),
            new SimpleImmutableEntry<String, Expr>("leaf", f001461),
            new SimpleImmutableEntry<String, Expr>("render", f001490),
            new SimpleImmutableEntry<String, Expr>("text", f001495)
          });
  private static final Expr f001497 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Bool", f000047),
            new SimpleImmutableEntry<String, Expr>("Double", f000049),
            new SimpleImmutableEntry<String, Expr>("Function", f000062),
            new SimpleImmutableEntry<String, Expr>("Integer", f000182),
            new SimpleImmutableEntry<String, Expr>("JSON", f000838),
            new SimpleImmutableEntry<String, Expr>("List", f001092),
            new SimpleImmutableEntry<String, Expr>("Location", f001094),
            new SimpleImmutableEntry<String, Expr>("Map", f001161),
            new SimpleImmutableEntry<String, Expr>("Monoid", f001162),
            new SimpleImmutableEntry<String, Expr>("Natural", f001260),
            new SimpleImmutableEntry<String, Expr>("Operator", f001268),
            new SimpleImmutableEntry<String, Expr>("Optional", f001377),
            new SimpleImmutableEntry<String, Expr>("Text", f001436),
            new SimpleImmutableEntry<String, Expr>("XML", f001496)
          });

  public static final Expr instance = f001497;
}
