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
  private static final Expr f000343 = Expr.makeBuiltIn("List/length");
  private static final Expr f000344 = Expr.makeFieldAccess(f000002, "tail");
  private static final Expr f000345 = Expr.makeApplication(f000009, new Expr[] {f000332});
  private static final Expr f000346 = Expr.makeMerge(f000337, f000003, null);
  private static final Expr f000347 = Expr.makeNonEmptyListLiteral(new Expr[] {f000346});
  private static final Expr f000348 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000347, f000002);
  private static final Expr f000349 = Expr.makeLambda("_", f000345, f000348);
  private static final Expr f000350 = Expr.makeLambda("_", f000338, f000349);
  private static final Expr f000351 = Expr.makeEmptyListLiteral(f000345);
  private static final Expr f000352 =
      Expr.makeApplication(f000000, new Expr[] {f000338, f000344, f000345, f000350, f000351});
  private static final Expr f000353 = Expr.makeApplication(f000343, new Expr[] {f000332, f000352});
  private static final Expr f000354 = Expr.makeApplication(f000063, new Expr[] {f000353});
  private static final Expr f000355 = Expr.makeFieldAccess(f000002, "head");
  private static final Expr f000356 = Expr.makeMerge(f000337, f000355, null);
  private static final Expr f000357 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000356),
            new SimpleImmutableEntry<String, Expr>("tail", f000352)
          });
  private static final Expr f000358 = Expr.makeFieldAccess(f000356, "tail");
  private static final Expr f000359 = Expr.makeApplication(f000343, new Expr[] {f000183, f000358});
  private static final Expr f000360 = Expr.makeApplication(f000063, new Expr[] {f000359});
  private static final Expr f000361 = Expr.makeFieldAccess(f000356, "head");
  private static final Expr f000362 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000361});
  private static final Expr f000363 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000362),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000364 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000183)
          });
  private static final Expr f000365 = Expr.makeBuiltIn("List/indexed");
  private static final Expr f000366 = Expr.makeApplication(f000365, new Expr[] {f000183, f000358});
  private static final Expr f000367 = Expr.makeFieldAccess(f000003, "index");
  private static final Expr f000368 = Expr.makeNaturalLiteral(new BigInteger("1"));
  private static final Expr f000369 = Expr.makeFieldAccess(f000014, "head");
  private static final Expr f000370 = Expr.makeMerge(f000337, f000369, null);
  private static final Expr f000371 = Expr.makeFieldAccess(f000370, "tail");
  private static final Expr f000372 = Expr.makeApplication(f000343, new Expr[] {f000183, f000371});
  private static final Expr f000373 = Expr.makeApplication(f000076, new Expr[] {f000368, f000372});
  private static final Expr f000374 = Expr.makeApplication(f000076, new Expr[] {f000367, f000373});
  private static final Expr f000375 = Expr.makeApplication(f000063, new Expr[] {f000374});
  private static final Expr f000376 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000375, f000012);
  private static final Expr f000377 = Expr.makeIf(f000376, f000270, f000002);
  private static final Expr f000378 = Expr.makeLambda("_", f000331, f000377);
  private static final Expr f000379 = Expr.makeLambda("_", f000364, f000378);
  private static final Expr f000380 =
      Expr.makeApplication(f000000, new Expr[] {f000364, f000366, f000331, f000379, f000334});
  private static final Expr f000381 = Expr.makeIf(f000375, f000270, f000002);
  private static final Expr f000382 = Expr.makeLambda("_", f000331, f000381);
  private static final Expr f000383 = Expr.makeLambda("_", f000364, f000382);
  private static final Expr f000384 =
      Expr.makeApplication(f000000, new Expr[] {f000364, f000366, f000331, f000383, f000334});
  private static final Expr f000385 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000003});
  private static final Expr f000386 = Expr.makeNonEmptyListLiteral(new Expr[] {f000385});
  private static final Expr f000387 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000386, f000002);
  private static final Expr f000388 = Expr.makeLambda("_", f000331, f000387);
  private static final Expr f000389 = Expr.makeLambda("_", f000183, f000388);
  private static final Expr f000390 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000384, f000331, f000389, f000334});
  private static final Expr f000391 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000380, f000390);
  private static final Expr f000392 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000361),
            new SimpleImmutableEntry<String, Expr>("tail", f000391)
          });
  private static final Expr f000393 = Expr.makeIf(f000360, f000363, f000392);
  private static final Expr f000394 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000332)
          });
  private static final Expr f000395 = Expr.makeApplication(f000365, new Expr[] {f000332, f000352});
  private static final Expr f000396 = Expr.makeFieldAccess(f000014, "tail");
  private static final Expr f000397 =
      Expr.makeApplication(f000000, new Expr[] {f000338, f000396, f000345, f000350, f000351});
  private static final Expr f000398 = Expr.makeApplication(f000343, new Expr[] {f000332, f000397});
  private static final Expr f000399 = Expr.makeApplication(f000076, new Expr[] {f000368, f000398});
  private static final Expr f000400 = Expr.makeApplication(f000076, new Expr[] {f000367, f000399});
  private static final Expr f000401 = Expr.makeApplication(f000063, new Expr[] {f000400});
  private static final Expr f000402 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000401, f000012);
  private static final Expr f000403 = Expr.makeIf(f000402, f000270, f000002);
  private static final Expr f000404 = Expr.makeLambda("_", f000345, f000403);
  private static final Expr f000405 = Expr.makeLambda("_", f000394, f000404);
  private static final Expr f000406 =
      Expr.makeApplication(f000000, new Expr[] {f000394, f000395, f000345, f000405, f000351});
  private static final Expr f000407 = Expr.makeFieldAccess(f000003, "tail");
  private static final Expr f000408 = Expr.makeApplication(f000343, new Expr[] {f000183, f000407});
  private static final Expr f000409 = Expr.makeApplication(f000063, new Expr[] {f000408});
  private static final Expr f000410 = Expr.makeFieldAccess(f000003, "head");
  private static final Expr f000411 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000410});
  private static final Expr f000412 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000411),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000413 = Expr.makeApplication(f000365, new Expr[] {f000183, f000407});
  private static final Expr f000414 = Expr.makeFieldAccess(f000025, "tail");
  private static final Expr f000415 = Expr.makeApplication(f000343, new Expr[] {f000183, f000414});
  private static final Expr f000416 = Expr.makeApplication(f000076, new Expr[] {f000368, f000415});
  private static final Expr f000417 = Expr.makeApplication(f000076, new Expr[] {f000367, f000416});
  private static final Expr f000418 = Expr.makeApplication(f000063, new Expr[] {f000417});
  private static final Expr f000419 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000418, f000012);
  private static final Expr f000420 = Expr.makeIf(f000419, f000270, f000002);
  private static final Expr f000421 = Expr.makeLambda("_", f000331, f000420);
  private static final Expr f000422 = Expr.makeLambda("_", f000364, f000421);
  private static final Expr f000423 =
      Expr.makeApplication(f000000, new Expr[] {f000364, f000413, f000331, f000422, f000334});
  private static final Expr f000424 = Expr.makeIf(f000418, f000270, f000002);
  private static final Expr f000425 = Expr.makeLambda("_", f000331, f000424);
  private static final Expr f000426 = Expr.makeLambda("_", f000364, f000425);
  private static final Expr f000427 =
      Expr.makeApplication(f000000, new Expr[] {f000364, f000413, f000331, f000426, f000334});
  private static final Expr f000428 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000427, f000331, f000389, f000334});
  private static final Expr f000429 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000423, f000428);
  private static final Expr f000430 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000410),
            new SimpleImmutableEntry<String, Expr>("tail", f000429)
          });
  private static final Expr f000431 = Expr.makeIf(f000409, f000412, f000430);
  private static final Expr f000432 = Expr.makeNonEmptyListLiteral(new Expr[] {f000431});
  private static final Expr f000433 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000432, f000002);
  private static final Expr f000434 = Expr.makeLambda("_", f000345, f000433);
  private static final Expr f000435 = Expr.makeLambda("_", f000332, f000434);
  private static final Expr f000436 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000406, f000345, f000435, f000351});
  private static final Expr f000437 = Expr.makeIf(f000401, f000270, f000002);
  private static final Expr f000438 = Expr.makeLambda("_", f000345, f000437);
  private static final Expr f000439 = Expr.makeLambda("_", f000394, f000438);
  private static final Expr f000440 =
      Expr.makeApplication(f000000, new Expr[] {f000394, f000395, f000345, f000439, f000351});
  private static final Expr f000441 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000436, f000440);
  private static final Expr f000442 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000393),
            new SimpleImmutableEntry<String, Expr>("tail", f000441)
          });
  private static final Expr f000443 = Expr.makeIf(f000354, f000357, f000442);
  private static final Expr f000444 = Expr.makeFieldAccess(f000443, "head");
  private static final Expr f000445 = Expr.makeFieldAccess(f000444, "tail");
  private static final Expr f000446 = Expr.makeFieldAccess(f000443, "tail");
  private static final Expr f000447 = Expr.makeNonEmptyListLiteral(new Expr[] {f000355});
  private static final Expr f000448 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000447, f000344);
  private static final Expr f000449 = Expr.makeLambda("_", f000331, f000307);
  private static final Expr f000450 = Expr.makeLambda("_", f000183, f000449);
  private static final Expr f000451 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000448, f000331, f000450});
  private static final Expr f000452 = Expr.makeLambda("_", f000332, f000451);
  private static final Expr f000453 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000446, f000331, f000452, f000334});
  private static final Expr f000454 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000445, f000453);
  private static final Expr f000455 = Expr.makeApplication(f000343, new Expr[] {f000183, f000454});
  private static final Expr f000456 = Expr.makeApplication(f000063, new Expr[] {f000455});
  private static final Expr f000457 = Expr.makeFieldAccess(f000444, "head");
  private static final Expr f000458 =
      Expr.makeTextLiteral(new String[] {"[ ", " ]"}, new Expr[] {f000457});
  private static final Expr f000459 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000458),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000460 = Expr.makeTextLiteral("[");
  private static final Expr f000461 =
      Expr.makeTextLiteral(new String[] {"  ", ""}, new Expr[] {f000457});
  private static final Expr f000462 = Expr.makeNonEmptyListLiteral(new Expr[] {f000461});
  private static final Expr f000463 =
      Expr.makeTextLiteral(new String[] {"  ", ""}, new Expr[] {f000003});
  private static final Expr f000464 = Expr.makeNonEmptyListLiteral(new Expr[] {f000463});
  private static final Expr f000465 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000464, f000002);
  private static final Expr f000466 = Expr.makeLambda("_", f000331, f000465);
  private static final Expr f000467 = Expr.makeLambda("_", f000183, f000466);
  private static final Expr f000468 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000454, f000331, f000467, f000334});
  private static final Expr f000469 = Expr.makeTextLiteral("]");
  private static final Expr f000470 = Expr.makeNonEmptyListLiteral(new Expr[] {f000469});
  private static final Expr f000471 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000468, f000470);
  private static final Expr f000472 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000462, f000471);
  private static final Expr f000473 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000460),
            new SimpleImmutableEntry<String, Expr>("tail", f000472)
          });
  private static final Expr f000474 = Expr.makeIf(f000456, f000459, f000473);
  private static final Expr f000475 = Expr.makeApplication(f000342, new Expr[] {f000474});
  private static final Expr f000476 = Expr.makeApplication(f000009, new Expr[] {f000338});
  private static final Expr f000477 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000338),
            new SimpleImmutableEntry<String, Expr>("tail", f000476)
          });
  private static final Expr f000478 = Expr.makeLambda("_", f000477, f000475);
  private static final Expr f000479 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000341),
            new SimpleImmutableEntry<String, Expr>("Some", f000478)
          });
  private static final Expr f000480 = Expr.makeApplication(f000177, new Expr[] {f000477});
  private static final Expr f000481 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000338)
          });
  private static final Expr f000482 = Expr.makeApplication(f000365, new Expr[] {f000338, f000003});
  private static final Expr f000483 = Expr.makeApplication(f000076, new Expr[] {f000367, f000368});
  private static final Expr f000484 = Expr.makeApplication(f000063, new Expr[] {f000483});
  private static final Expr f000485 = Expr.makeIf(f000484, f000270, f000002);
  private static final Expr f000486 = Expr.makeLambda("_", f000476, f000485);
  private static final Expr f000487 = Expr.makeLambda("_", f000481, f000486);
  private static final Expr f000488 = Expr.makeEmptyListLiteral(f000476);
  private static final Expr f000489 =
      Expr.makeApplication(f000000, new Expr[] {f000481, f000482, f000476, f000487, f000488});
  private static final Expr f000490 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000002),
            new SimpleImmutableEntry<String, Expr>("tail", f000489)
          });
  private static final Expr f000491 = Expr.makeApplication(f000175, new Expr[] {f000490});
  private static final Expr f000492 = Expr.makeLambda("_", f000338, f000491);
  private static final Expr f000493 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000480),
            new SimpleImmutableEntry<String, Expr>("Some", f000492)
          });
  private static final Expr f000494 = Expr.makeBuiltIn("List/head");
  private static final Expr f000495 = Expr.makeApplication(f000494, new Expr[] {f000338, f000002});
  private static final Expr f000496 = Expr.makeMerge(f000493, f000495, null);
  private static final Expr f000497 = Expr.makeMerge(f000479, f000496, null);
  private static final Expr f000498 = Expr.makeLambda("_", f000476, f000497);
  private static final Expr f000499 = Expr.makeTextLiteral("true");
  private static final Expr f000500 = Expr.makeTextLiteral("false");
  private static final Expr f000501 = Expr.makeIf(f000002, f000499, f000500);
  private static final Expr f000502 = Expr.makeApplication(f000339, new Expr[] {f000501});
  private static final Expr f000503 = Expr.makeLambda("_", f000001, f000502);
  private static final Expr f000504 = Expr.makeApplication(f000048, new Expr[] {f000002});
  private static final Expr f000505 = Expr.makeApplication(f000339, new Expr[] {f000504});
  private static final Expr f000506 = Expr.makeLambda("_", f000190, f000505);
  private static final Expr f000507 = Expr.makeBuiltIn("Natural/show");
  private static final Expr f000508 = Expr.makeApplication(f000507, new Expr[] {f000065});
  private static final Expr f000509 = Expr.makeApplication(f000155, new Expr[] {f000002});
  private static final Expr f000510 = Expr.makeIf(f000119, f000508, f000509);
  private static final Expr f000511 = Expr.makeApplication(f000339, new Expr[] {f000510});
  private static final Expr f000512 = Expr.makeLambda("_", f000071, f000511);
  private static final Expr f000513 = Expr.makeTextLiteral("null");
  private static final Expr f000514 = Expr.makeApplication(f000339, new Expr[] {f000513});
  private static final Expr f000515 = Expr.makeTextLiteral("{}");
  private static final Expr f000516 = Expr.makeApplication(f000339, new Expr[] {f000515});
  private static final Expr f000517 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000338)
          });
  private static final Expr f000518 = Expr.makeMerge(f000337, f000249, null);
  private static final Expr f000519 = Expr.makeBuiltIn("Text/show");
  private static final Expr f000520 = Expr.makeApplication(f000519, new Expr[] {f000248});
  private static final Expr f000521 = Expr.makeFieldAccess(f000518, "head");
  private static final Expr f000522 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000520, f000521});
  private static final Expr f000523 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000522)});
  private static final Expr f000524 =
      Expr.makeOperatorApplication(Operator.PREFER, f000518, f000523);
  private static final Expr f000525 = Expr.makeNonEmptyListLiteral(new Expr[] {f000524});
  private static final Expr f000526 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000525, f000002);
  private static final Expr f000527 = Expr.makeLambda("_", f000345, f000526);
  private static final Expr f000528 = Expr.makeLambda("_", f000517, f000527);
  private static final Expr f000529 =
      Expr.makeApplication(f000000, new Expr[] {f000517, f000344, f000345, f000528, f000351});
  private static final Expr f000530 = Expr.makeApplication(f000343, new Expr[] {f000332, f000529});
  private static final Expr f000531 = Expr.makeApplication(f000063, new Expr[] {f000530});
  private static final Expr f000532 = Expr.makeFieldAccess(f000355, "mapValue");
  private static final Expr f000533 = Expr.makeMerge(f000337, f000532, null);
  private static final Expr f000534 = Expr.makeFieldAccess(f000355, "mapKey");
  private static final Expr f000535 = Expr.makeApplication(f000519, new Expr[] {f000534});
  private static final Expr f000536 = Expr.makeFieldAccess(f000533, "head");
  private static final Expr f000537 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000535, f000536});
  private static final Expr f000538 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000537)});
  private static final Expr f000539 =
      Expr.makeOperatorApplication(Operator.PREFER, f000533, f000538);
  private static final Expr f000540 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000539),
            new SimpleImmutableEntry<String, Expr>("tail", f000529)
          });
  private static final Expr f000541 = Expr.makeFieldAccess(f000533, "tail");
  private static final Expr f000542 = Expr.makeApplication(f000343, new Expr[] {f000183, f000541});
  private static final Expr f000543 = Expr.makeApplication(f000063, new Expr[] {f000542});
  private static final Expr f000544 =
      Expr.makeTextLiteral(new String[] {"", ": ", ","}, new Expr[] {f000535, f000536});
  private static final Expr f000545 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000544),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000546 = Expr.makeApplication(f000365, new Expr[] {f000183, f000541});
  private static final Expr f000547 = Expr.makeFieldAccess(f000369, "mapValue");
  private static final Expr f000548 = Expr.makeMerge(f000337, f000547, null);
  private static final Expr f000549 = Expr.makeFieldAccess(f000548, "tail");
  private static final Expr f000550 = Expr.makeApplication(f000343, new Expr[] {f000183, f000549});
  private static final Expr f000551 = Expr.makeApplication(f000076, new Expr[] {f000368, f000550});
  private static final Expr f000552 = Expr.makeApplication(f000076, new Expr[] {f000367, f000551});
  private static final Expr f000553 = Expr.makeApplication(f000063, new Expr[] {f000552});
  private static final Expr f000554 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000553, f000012);
  private static final Expr f000555 = Expr.makeIf(f000554, f000270, f000002);
  private static final Expr f000556 = Expr.makeLambda("_", f000331, f000555);
  private static final Expr f000557 = Expr.makeLambda("_", f000364, f000556);
  private static final Expr f000558 =
      Expr.makeApplication(f000000, new Expr[] {f000364, f000546, f000331, f000557, f000334});
  private static final Expr f000559 = Expr.makeIf(f000553, f000270, f000002);
  private static final Expr f000560 = Expr.makeLambda("_", f000331, f000559);
  private static final Expr f000561 = Expr.makeLambda("_", f000364, f000560);
  private static final Expr f000562 =
      Expr.makeApplication(f000000, new Expr[] {f000364, f000546, f000331, f000561, f000334});
  private static final Expr f000563 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000562, f000331, f000389, f000334});
  private static final Expr f000564 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000558, f000563);
  private static final Expr f000565 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000537),
            new SimpleImmutableEntry<String, Expr>("tail", f000564)
          });
  private static final Expr f000566 = Expr.makeIf(f000543, f000545, f000565);
  private static final Expr f000567 = Expr.makeApplication(f000365, new Expr[] {f000332, f000529});
  private static final Expr f000568 =
      Expr.makeApplication(f000000, new Expr[] {f000517, f000396, f000345, f000528, f000351});
  private static final Expr f000569 = Expr.makeApplication(f000343, new Expr[] {f000332, f000568});
  private static final Expr f000570 = Expr.makeApplication(f000076, new Expr[] {f000368, f000569});
  private static final Expr f000571 = Expr.makeApplication(f000076, new Expr[] {f000367, f000570});
  private static final Expr f000572 = Expr.makeApplication(f000063, new Expr[] {f000571});
  private static final Expr f000573 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000572, f000012);
  private static final Expr f000574 = Expr.makeIf(f000573, f000270, f000002);
  private static final Expr f000575 = Expr.makeLambda("_", f000345, f000574);
  private static final Expr f000576 = Expr.makeLambda("_", f000394, f000575);
  private static final Expr f000577 =
      Expr.makeApplication(f000000, new Expr[] {f000394, f000567, f000345, f000576, f000351});
  private static final Expr f000578 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000577, f000345, f000435, f000351});
  private static final Expr f000579 = Expr.makeIf(f000572, f000270, f000002);
  private static final Expr f000580 = Expr.makeLambda("_", f000345, f000579);
  private static final Expr f000581 = Expr.makeLambda("_", f000394, f000580);
  private static final Expr f000582 =
      Expr.makeApplication(f000000, new Expr[] {f000394, f000567, f000345, f000581, f000351});
  private static final Expr f000583 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000578, f000582);
  private static final Expr f000584 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000566),
            new SimpleImmutableEntry<String, Expr>("tail", f000583)
          });
  private static final Expr f000585 = Expr.makeIf(f000531, f000540, f000584);
  private static final Expr f000586 = Expr.makeFieldAccess(f000585, "head");
  private static final Expr f000587 = Expr.makeFieldAccess(f000586, "tail");
  private static final Expr f000588 = Expr.makeFieldAccess(f000585, "tail");
  private static final Expr f000589 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000588, f000331, f000452, f000334});
  private static final Expr f000590 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000587, f000589);
  private static final Expr f000591 = Expr.makeApplication(f000343, new Expr[] {f000183, f000590});
  private static final Expr f000592 = Expr.makeApplication(f000063, new Expr[] {f000591});
  private static final Expr f000593 = Expr.makeFieldAccess(f000586, "head");
  private static final Expr f000594 =
      Expr.makeTextLiteral(new String[] {"{ ", " }"}, new Expr[] {f000593});
  private static final Expr f000595 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000594),
            new SimpleImmutableEntry<String, Expr>("tail", f000334)
          });
  private static final Expr f000596 = Expr.makeTextLiteral("{");
  private static final Expr f000597 =
      Expr.makeTextLiteral(new String[] {"  ", ""}, new Expr[] {f000593});
  private static final Expr f000598 = Expr.makeNonEmptyListLiteral(new Expr[] {f000597});
  private static final Expr f000599 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000590, f000331, f000467, f000334});
  private static final Expr f000600 = Expr.makeTextLiteral("}");
  private static final Expr f000601 = Expr.makeNonEmptyListLiteral(new Expr[] {f000600});
  private static final Expr f000602 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000599, f000601);
  private static final Expr f000603 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000598, f000602);
  private static final Expr f000604 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000596),
            new SimpleImmutableEntry<String, Expr>("tail", f000603)
          });
  private static final Expr f000605 = Expr.makeIf(f000592, f000595, f000604);
  private static final Expr f000606 = Expr.makeApplication(f000342, new Expr[] {f000605});
  private static final Expr f000607 = Expr.makeApplication(f000009, new Expr[] {f000517});
  private static final Expr f000608 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000517),
            new SimpleImmutableEntry<String, Expr>("tail", f000607)
          });
  private static final Expr f000609 = Expr.makeLambda("_", f000608, f000606);
  private static final Expr f000610 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000516),
            new SimpleImmutableEntry<String, Expr>("Some", f000609)
          });
  private static final Expr f000611 = Expr.makeApplication(f000177, new Expr[] {f000608});
  private static final Expr f000612 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000517)
          });
  private static final Expr f000613 = Expr.makeApplication(f000365, new Expr[] {f000517, f000003});
  private static final Expr f000614 = Expr.makeLambda("_", f000607, f000485);
  private static final Expr f000615 = Expr.makeLambda("_", f000612, f000614);
  private static final Expr f000616 = Expr.makeEmptyListLiteral(f000607);
  private static final Expr f000617 =
      Expr.makeApplication(f000000, new Expr[] {f000612, f000613, f000607, f000615, f000616});
  private static final Expr f000618 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000002),
            new SimpleImmutableEntry<String, Expr>("tail", f000617)
          });
  private static final Expr f000619 = Expr.makeApplication(f000175, new Expr[] {f000618});
  private static final Expr f000620 = Expr.makeLambda("_", f000517, f000619);
  private static final Expr f000621 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000611),
            new SimpleImmutableEntry<String, Expr>("Some", f000620)
          });
  private static final Expr f000622 = Expr.makeApplication(f000494, new Expr[] {f000517, f000002});
  private static final Expr f000623 = Expr.makeMerge(f000621, f000622, null);
  private static final Expr f000624 = Expr.makeMerge(f000610, f000623, null);
  private static final Expr f000625 = Expr.makeLambda("_", f000607, f000624);
  private static final Expr f000626 = Expr.makeApplication(f000519, new Expr[] {f000002});
  private static final Expr f000627 = Expr.makeApplication(f000339, new Expr[] {f000626});
  private static final Expr f000628 = Expr.makeLambda("_", f000183, f000627);
  private static final Expr f000629 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000498),
            new SimpleImmutableEntry<String, Expr>("bool", f000503),
            new SimpleImmutableEntry<String, Expr>("double", f000506),
            new SimpleImmutableEntry<String, Expr>("integer", f000512),
            new SimpleImmutableEntry<String, Expr>("null", f000514),
            new SimpleImmutableEntry<String, Expr>("object", f000625),
            new SimpleImmutableEntry<String, Expr>("string", f000628)
          });
  private static final Expr f000630 = Expr.makeApplication(f000002, new Expr[] {f000338, f000629});
  private static final Expr f000631 = Expr.makeMerge(f000337, f000630, null);
  private static final Expr f000632 = Expr.makeFieldAccess(f000631, "head");
  private static final Expr f000633 = Expr.makeNonEmptyListLiteral(new Expr[] {f000632});
  private static final Expr f000634 = Expr.makeFieldAccess(f000631, "tail");
  private static final Expr f000635 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000633, f000634);
  private static final Expr f000636 =
      Expr.makeTextLiteral(new String[] {"", "\n", ""}, new Expr[] {f000003, f000002});
  private static final Expr f000637 = Expr.makeLambda("_", f000183, f000636);
  private static final Expr f000638 = Expr.makeLambda("_", f000183, f000637);
  private static final Expr f000639 = Expr.makeTextLiteral("");
  private static final Expr f000640 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000635, f000183, f000638, f000639});
  private static final Expr f000641 = Expr.makeLambda("_", f000199, f000640);
  private static final Expr f000642 = Expr.makeLambda("_", f000071, f000510);
  private static final Expr f000643 =
      Expr.makeTextLiteral(new String[] {"- ", ""}, new Expr[] {f000361});
  private static final Expr f000644 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000358, f000331, f000467, f000334});
  private static final Expr f000645 = Expr.makeFieldAccess(f000346, "tail");
  private static final Expr f000646 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000645, f000331, f000467, f000334});
  private static final Expr f000647 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("tail", f000646)});
  private static final Expr f000648 =
      Expr.makeOperatorApplication(Operator.PREFER, f000346, f000647);
  private static final Expr f000649 = Expr.makeFieldAccess(f000346, "head");
  private static final Expr f000650 =
      Expr.makeTextLiteral(new String[] {"- ", ""}, new Expr[] {f000649});
  private static final Expr f000651 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000650)});
  private static final Expr f000652 =
      Expr.makeOperatorApplication(Operator.PREFER, f000648, f000651);
  private static final Expr f000653 = Expr.makeNonEmptyListLiteral(new Expr[] {f000652});
  private static final Expr f000654 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000653, f000002);
  private static final Expr f000655 = Expr.makeLambda("_", f000345, f000654);
  private static final Expr f000656 = Expr.makeLambda("_", f000338, f000655);
  private static final Expr f000657 =
      Expr.makeApplication(f000000, new Expr[] {f000338, f000344, f000345, f000656, f000351});
  private static final Expr f000658 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000657, f000331, f000452, f000334});
  private static final Expr f000659 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000644, f000658);
  private static final Expr f000660 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000643),
            new SimpleImmutableEntry<String, Expr>("tail", f000659)
          });
  private static final Expr f000661 = Expr.makeApplication(f000342, new Expr[] {f000660});
  private static final Expr f000662 = Expr.makeLambda("_", f000477, f000661);
  private static final Expr f000663 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000341),
            new SimpleImmutableEntry<String, Expr>("Some", f000662)
          });
  private static final Expr f000664 = Expr.makeMerge(f000663, f000496, null);
  private static final Expr f000665 = Expr.makeLambda("_", f000476, f000664);
  private static final Expr f000666 = Expr.makeFieldAccess(f000410, "mapKey");
  private static final Expr f000667 = Expr.makeApplication(f000519, new Expr[] {f000666});
  private static final Expr f000668 =
      Expr.makeTextLiteral(new String[] {"", ":"}, new Expr[] {f000667});
  private static final Expr f000669 = Expr.makeFieldAccess(f000410, "mapValue");
  private static final Expr f000670 = Expr.makeMerge(f000337, f000669, null);
  private static final Expr f000671 = Expr.makeFieldAccess(f000670, "head");
  private static final Expr f000672 = Expr.makeNonEmptyListLiteral(new Expr[] {f000671});
  private static final Expr f000673 = Expr.makeFieldAccess(f000670, "tail");
  private static final Expr f000674 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000672, f000673);
  private static final Expr f000675 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000674, f000331, f000467, f000334});
  private static final Expr f000676 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000668),
            new SimpleImmutableEntry<String, Expr>("tail", f000675)
          });
  private static final Expr f000677 = Expr.makeLambda("_", f000332, f000676);
  private static final Expr f000678 =
      Expr.makeTextLiteral(new String[] {"", ": ", ""}, new Expr[] {f000667, f000002});
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
  private static final Expr f000682 = Expr.makeMerge(f000681, f000532, null);
  private static final Expr f000683 = Expr.makeFieldAccess(f000682, "head");
  private static final Expr f000684 = Expr.makeFieldAccess(f000682, "tail");
  private static final Expr f000685 = Expr.makeFieldAccess(f000014, "mapKey");
  private static final Expr f000686 = Expr.makeApplication(f000519, new Expr[] {f000685});
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
      Expr.makeApplication(f000000, new Expr[] {f000183, f000693, f000331, f000467, f000334});
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
  private static final Expr f000704 = Expr.makeLambda("_", f000345, f000703);
  private static final Expr f000705 = Expr.makeLambda("_", f000517, f000704);
  private static final Expr f000706 =
      Expr.makeApplication(f000000, new Expr[] {f000517, f000344, f000345, f000705, f000351});
  private static final Expr f000707 =
      Expr.makeApplication(f000000, new Expr[] {f000332, f000706, f000331, f000452, f000334});
  private static final Expr f000708 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000684, f000707);
  private static final Expr f000709 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000683),
            new SimpleImmutableEntry<String, Expr>("tail", f000708)
          });
  private static final Expr f000710 = Expr.makeApplication(f000342, new Expr[] {f000709});
  private static final Expr f000711 = Expr.makeLambda("_", f000608, f000710);
  private static final Expr f000712 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000516),
            new SimpleImmutableEntry<String, Expr>("Some", f000711)
          });
  private static final Expr f000713 = Expr.makeMerge(f000712, f000623, null);
  private static final Expr f000714 = Expr.makeLambda("_", f000607, f000713);
  private static final Expr f000715 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000665),
            new SimpleImmutableEntry<String, Expr>("bool", f000503),
            new SimpleImmutableEntry<String, Expr>("double", f000506),
            new SimpleImmutableEntry<String, Expr>("integer", f000512),
            new SimpleImmutableEntry<String, Expr>("null", f000514),
            new SimpleImmutableEntry<String, Expr>("object", f000714),
            new SimpleImmutableEntry<String, Expr>("string", f000628)
          });
  private static final Expr f000716 = Expr.makeApplication(f000002, new Expr[] {f000338, f000715});
  private static final Expr f000717 = Expr.makeMerge(f000337, f000716, null);
  private static final Expr f000718 = Expr.makeFieldAccess(f000717, "head");
  private static final Expr f000719 = Expr.makeNonEmptyListLiteral(new Expr[] {f000718});
  private static final Expr f000720 = Expr.makeFieldAccess(f000717, "tail");
  private static final Expr f000721 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000719, f000720);
  private static final Expr f000722 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000721, f000183, f000638, f000639});
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
            new SimpleImmutableEntry<String, Expr>("render", f000641),
            new SimpleImmutableEntry<String, Expr>("renderInteger", f000642),
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
  private static final Expr f000780 = Expr.makeBuiltIn("Optional");
  private static final Expr f000781 = Expr.makeApplication(f000780, new Expr[] {f000187});
  private static final Expr f000782 = Expr.makeLambda("_", f000781, f000779);
  private static final Expr f000783 = Expr.makeLambda("_", f000017, f000782);
  private static final Expr f000784 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000003)
          });
  private static final Expr f000785 = Expr.makeApplication(f000365, new Expr[] {f000003, f000002});
  private static final Expr f000786 = Expr.makeApplication(f000076, new Expr[] {f000367, f000052});
  private static final Expr f000787 = Expr.makeApplication(f000063, new Expr[] {f000786});
  private static final Expr f000788 = Expr.makeIf(f000787, f000270, f000002);
  private static final Expr f000789 = Expr.makeLambda("_", f000205, f000788);
  private static final Expr f000790 = Expr.makeLambda("_", f000784, f000789);
  private static final Expr f000791 =
      Expr.makeApplication(f000000, new Expr[] {f000784, f000785, f000201, f000790, f000208});
  private static final Expr f000792 = Expr.makeLambda("_", f000187, f000791);
  private static final Expr f000793 = Expr.makeLambda("_", f000017, f000792);
  private static final Expr f000794 = Expr.makeLambda("_", f000178, f000793);
  private static final Expr f000795 = Expr.makeEmptyListLiteral(f000187);
  private static final Expr f000796 = Expr.makeLambda("_", f000017, f000795);
  private static final Expr f000797 = Expr.makeIf(f000742, f000307, f000002);
  private static final Expr f000798 = Expr.makeLambda("_", f000271, f000797);
  private static final Expr f000799 = Expr.makeLambda("_", f000014, f000798);
  private static final Expr f000800 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000205, f000799, f000274});
  private static final Expr f000801 = Expr.makeLambda("_", f000201, f000800);
  private static final Expr f000802 = Expr.makeLambda("_", f000748, f000801);
  private static final Expr f000803 = Expr.makeLambda("_", f000017, f000802);
  private static final Expr f000804 = Expr.Constants.EMPTY_RECORD_TYPE;
  private static final Expr f000805 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000804)
          });
  private static final Expr f000806 = Expr.makeBuiltIn("Natural/fold");
  private static final Expr f000807 = Expr.makeApplication(f000009, new Expr[] {f000804});
  private static final Expr f000808 = Expr.Constants.EMPTY_RECORD_LITERAL;
  private static final Expr f000809 = Expr.makeNonEmptyListLiteral(new Expr[] {f000808});
  private static final Expr f000810 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000809, f000002);
  private static final Expr f000811 = Expr.makeLambda("_", f000807, f000810);
  private static final Expr f000812 = Expr.makeEmptyListLiteral(f000807);
  private static final Expr f000813 =
      Expr.makeApplication(f000806, new Expr[] {f000014, f000807, f000811, f000812});
  private static final Expr f000814 = Expr.makeApplication(f000365, new Expr[] {f000804, f000813});
  private static final Expr f000815 = Expr.makeApplication(f000014, new Expr[] {f000367});
  private static final Expr f000816 = Expr.makeNonEmptyListLiteral(new Expr[] {f000815});
  private static final Expr f000817 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000816, f000002);
  private static final Expr f000818 = Expr.makeLambda("_", f000205, f000817);
  private static final Expr f000819 = Expr.makeLambda("_", f000805, f000818);
  private static final Expr f000820 =
      Expr.makeApplication(f000000, new Expr[] {f000805, f000814, f000201, f000819, f000208});
  private static final Expr f000821 = Expr.makePi("_", f000178, f000003);
  private static final Expr f000822 = Expr.makeLambda("_", f000821, f000820);
  private static final Expr f000823 = Expr.makeLambda("_", f000017, f000822);
  private static final Expr f000824 = Expr.makeLambda("_", f000178, f000823);
  private static final Expr f000825 = Expr.makeApplication(f000494, new Expr[] {f000003, f000791});
  private static final Expr f000826 = Expr.makeLambda("_", f000187, f000825);
  private static final Expr f000827 = Expr.makeLambda("_", f000017, f000826);
  private static final Expr f000828 = Expr.makeLambda("_", f000178, f000827);
  private static final Expr f000829 =
      Expr.makeApplication(f000806, new Expr[] {f000025, f000807, f000811, f000812});
  private static final Expr f000830 = Expr.makeApplication(f000365, new Expr[] {f000804, f000829});
  private static final Expr f000831 =
      Expr.makeApplication(f000806, new Expr[] {f000367, f000052, f000025, f000014});
  private static final Expr f000832 = Expr.makeNonEmptyListLiteral(new Expr[] {f000831});
  private static final Expr f000833 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000832, f000002);
  private static final Expr f000834 = Expr.makeLambda("_", f000271, f000833);
  private static final Expr f000835 = Expr.makeLambda("_", f000805, f000834);
  private static final Expr f000836 =
      Expr.makeApplication(f000000, new Expr[] {f000805, f000830, f000205, f000835, f000274});
  private static final Expr f000837 = Expr.makeLambda("_", f000003, f000836);
  private static final Expr f000838 = Expr.makePi("_", f000002, f000003);
  private static final Expr f000839 = Expr.makeLambda("_", f000838, f000837);
  private static final Expr f000840 = Expr.makeLambda("_", f000017, f000839);
  private static final Expr f000841 = Expr.makeLambda("_", f000178, f000840);
  private static final Expr f000842 = Expr.makeBuiltIn("List/last");
  private static final Expr f000843 = Expr.makeNonEmptyListLiteral(new Expr[] {f000742});
  private static final Expr f000844 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000843, f000002);
  private static final Expr f000845 = Expr.makeLambda("_", f000271, f000844);
  private static final Expr f000846 = Expr.makeLambda("_", f000025, f000845);
  private static final Expr f000847 =
      Expr.makeApplication(f000000, new Expr[] {f000025, f000002, f000205, f000846, f000274});
  private static final Expr f000848 = Expr.makeLambda("_", f000205, f000847);
  private static final Expr f000849 = Expr.makePi("_", f000003, f000003);
  private static final Expr f000850 = Expr.makeLambda("_", f000849, f000848);
  private static final Expr f000851 = Expr.makeLambda("_", f000017, f000850);
  private static final Expr f000852 = Expr.makeLambda("_", f000017, f000851);
  private static final Expr f000853 = Expr.makeApplication(f000343, new Expr[] {f000003, f000002});
  private static final Expr f000854 = Expr.makeApplication(f000063, new Expr[] {f000853});
  private static final Expr f000855 = Expr.makeLambda("_", f000187, f000854);
  private static final Expr f000856 = Expr.makeLambda("_", f000017, f000855);
  private static final Expr f000857 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000205),
            new SimpleImmutableEntry<String, Expr>("true", f000205)
          });
  private static final Expr f000858 = Expr.makeFieldAccess(f000002, "false");
  private static final Expr f000859 = Expr.makeFieldAccess(f000002, "true");
  private static final Expr f000860 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000306, f000859);
  private static final Expr f000861 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000858),
            new SimpleImmutableEntry<String, Expr>("true", f000860)
          });
  private static final Expr f000862 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000306, f000858);
  private static final Expr f000863 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000862),
            new SimpleImmutableEntry<String, Expr>("true", f000859)
          });
  private static final Expr f000864 = Expr.makeIf(f000742, f000861, f000863);
  private static final Expr f000865 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000271),
            new SimpleImmutableEntry<String, Expr>("true", f000271)
          });
  private static final Expr f000866 = Expr.makeLambda("_", f000865, f000864);
  private static final Expr f000867 = Expr.makeLambda("_", f000014, f000866);
  private static final Expr f000868 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000274),
            new SimpleImmutableEntry<String, Expr>("true", f000274)
          });
  private static final Expr f000869 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000857, f000867, f000868});
  private static final Expr f000870 = Expr.makeLambda("_", f000201, f000869);
  private static final Expr f000871 = Expr.makeLambda("_", f000748, f000870);
  private static final Expr f000872 = Expr.makeLambda("_", f000017, f000871);
  private static final Expr f000873 = Expr.makeLambda("_", f000201, f000307);
  private static final Expr f000874 =
      Expr.makeApplication(f000806, new Expr[] {f000014, f000201, f000873, f000208});
  private static final Expr f000875 = Expr.makeLambda("_", f000002, f000874);
  private static final Expr f000876 = Expr.makeLambda("_", f000017, f000875);
  private static final Expr f000877 = Expr.makeLambda("_", f000178, f000876);
  private static final Expr f000878 = Expr.makeBuiltIn("List/reverse");
  private static final Expr f000879 = Expr.makeApplication(f000009, new Expr[] {f000784});
  private static final Expr f000880 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000014)
          });
  private static final Expr f000881 = Expr.makeApplication(f000009, new Expr[] {f000880});
  private static final Expr f000882 = Expr.makePi("_", f000178, f000881);
  private static final Expr f000883 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000178),
            new SimpleImmutableEntry<String, Expr>("diff", f000882)
          });
  private static final Expr f000884 = Expr.makeFieldAccess(f000002, "count");
  private static final Expr f000885 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000025)
          });
  private static final Expr f000886 = Expr.makeApplication(f000343, new Expr[] {f000885, f000003});
  private static final Expr f000887 = Expr.makeOperatorApplication(Operator.PLUS, f000884, f000886);
  private static final Expr f000888 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000052)
          });
  private static final Expr f000889 = Expr.makeApplication(f000009, new Expr[] {f000888});
  private static final Expr f000890 = Expr.makeOperatorApplication(Operator.PLUS, f000367, f000014);
  private static final Expr f000891 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000890),
            new SimpleImmutableEntry<String, Expr>("value", f000268)
          });
  private static final Expr f000892 = Expr.makeNonEmptyListLiteral(new Expr[] {f000891});
  private static final Expr f000893 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000892, f000002);
  private static final Expr f000894 = Expr.makeIdentifier("_", 5);
  private static final Expr f000895 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000894)
          });
  private static final Expr f000896 = Expr.makeApplication(f000009, new Expr[] {f000895});
  private static final Expr f000897 = Expr.makeLambda("_", f000896, f000893);
  private static final Expr f000898 = Expr.makeLambda("_", f000888, f000897);
  private static final Expr f000899 = Expr.makeFieldAccess(f000003, "diff");
  private static final Expr f000900 = Expr.makeApplication(f000343, new Expr[] {f000888, f000014});
  private static final Expr f000901 = Expr.makeOperatorApplication(Operator.PLUS, f000002, f000900);
  private static final Expr f000902 = Expr.makeApplication(f000899, new Expr[] {f000901});
  private static final Expr f000903 =
      Expr.makeApplication(f000000, new Expr[] {f000888, f000014, f000889, f000898, f000902});
  private static final Expr f000904 = Expr.makeLambda("_", f000178, f000903);
  private static final Expr f000905 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000887),
            new SimpleImmutableEntry<String, Expr>("diff", f000904)
          });
  private static final Expr f000906 = Expr.makeApplication(f000009, new Expr[] {f000885});
  private static final Expr f000907 = Expr.makePi("_", f000178, f000906);
  private static final Expr f000908 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000178),
            new SimpleImmutableEntry<String, Expr>("diff", f000907)
          });
  private static final Expr f000909 = Expr.makeLambda("_", f000908, f000905);
  private static final Expr f000910 = Expr.makeLambda("_", f000879, f000909);
  private static final Expr f000911 = Expr.makeNaturalLiteral(new BigInteger("0"));
  private static final Expr f000912 = Expr.makeEmptyListLiteral(f000881);
  private static final Expr f000913 = Expr.makeLambda("_", f000178, f000912);
  private static final Expr f000914 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000911),
            new SimpleImmutableEntry<String, Expr>("diff", f000913)
          });
  private static final Expr f000915 =
      Expr.makeApplication(f000000, new Expr[] {f000879, f000002, f000883, f000910, f000914});
  private static final Expr f000916 = Expr.makeFieldAccess(f000915, "diff");
  private static final Expr f000917 = Expr.makeApplication(f000916, new Expr[] {f000911});
  private static final Expr f000918 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000178),
            new SimpleImmutableEntry<String, Expr>("value", f000002)
          });
  private static final Expr f000919 = Expr.makeApplication(f000009, new Expr[] {f000918});
  private static final Expr f000920 = Expr.makeApplication(f000009, new Expr[] {f000919});
  private static final Expr f000921 = Expr.makeLambda("_", f000920, f000917);
  private static final Expr f000922 = Expr.makeLambda("_", f000017, f000921);
  private static final Expr f000923 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000787, f000012);
  private static final Expr f000924 = Expr.makeIf(f000923, f000270, f000002);
  private static final Expr f000925 = Expr.makeLambda("_", f000205, f000924);
  private static final Expr f000926 = Expr.makeLambda("_", f000784, f000925);
  private static final Expr f000927 =
      Expr.makeApplication(f000000, new Expr[] {f000784, f000785, f000201, f000926, f000208});
  private static final Expr f000928 = Expr.makeLambda("_", f000187, f000927);
  private static final Expr f000929 = Expr.makeLambda("_", f000017, f000928);
  private static final Expr f000930 = Expr.makeLambda("_", f000178, f000929);
  private static final Expr f000931 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000014),
            new SimpleImmutableEntry<String, Expr>("_2", f000003)
          });
  private static final Expr f000932 = Expr.makeFieldAccess(f000003, "_1");
  private static final Expr f000933 = Expr.makeNonEmptyListLiteral(new Expr[] {f000932});
  private static final Expr f000934 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000933, f000002);
  private static final Expr f000935 = Expr.makeLambda("_", f000271, f000934);
  private static final Expr f000936 = Expr.makeLambda("_", f000931, f000935);
  private static final Expr f000937 =
      Expr.makeApplication(f000000, new Expr[] {f000931, f000002, f000205, f000936, f000274});
  private static final Expr f000938 = Expr.makeFieldAccess(f000003, "_2");
  private static final Expr f000939 = Expr.makeNonEmptyListLiteral(new Expr[] {f000938});
  private static final Expr f000940 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000939, f000002);
  private static final Expr f000941 = Expr.makeLambda("_", f000205, f000940);
  private static final Expr f000942 = Expr.makeLambda("_", f000931, f000941);
  private static final Expr f000943 =
      Expr.makeApplication(f000000, new Expr[] {f000931, f000002, f000201, f000942, f000208});
  private static final Expr f000944 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000937),
            new SimpleImmutableEntry<String, Expr>("_2", f000943)
          });
  private static final Expr f000945 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000003),
            new SimpleImmutableEntry<String, Expr>("_2", f000002)
          });
  private static final Expr f000946 = Expr.makeApplication(f000009, new Expr[] {f000945});
  private static final Expr f000947 = Expr.makeLambda("_", f000946, f000944);
  private static final Expr f000948 = Expr.makeLambda("_", f000017, f000947);
  private static final Expr f000949 = Expr.makeLambda("_", f000017, f000948);
  private static final Expr f000950 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("all", f000750),
            new SimpleImmutableEntry<String, Expr>("any", f000757),
            new SimpleImmutableEntry<String, Expr>("build", f000758),
            new SimpleImmutableEntry<String, Expr>("concat", f000768),
            new SimpleImmutableEntry<String, Expr>("concatMap", f000776),
            new SimpleImmutableEntry<String, Expr>("default", f000783),
            new SimpleImmutableEntry<String, Expr>("drop", f000794),
            new SimpleImmutableEntry<String, Expr>("empty", f000796),
            new SimpleImmutableEntry<String, Expr>("filter", f000803),
            new SimpleImmutableEntry<String, Expr>("fold", f000000),
            new SimpleImmutableEntry<String, Expr>("generate", f000824),
            new SimpleImmutableEntry<String, Expr>("head", f000494),
            new SimpleImmutableEntry<String, Expr>("index", f000828),
            new SimpleImmutableEntry<String, Expr>("indexed", f000365),
            new SimpleImmutableEntry<String, Expr>("iterate", f000841),
            new SimpleImmutableEntry<String, Expr>("last", f000842),
            new SimpleImmutableEntry<String, Expr>("length", f000343),
            new SimpleImmutableEntry<String, Expr>("map", f000852),
            new SimpleImmutableEntry<String, Expr>("null", f000856),
            new SimpleImmutableEntry<String, Expr>("partition", f000872),
            new SimpleImmutableEntry<String, Expr>("replicate", f000877),
            new SimpleImmutableEntry<String, Expr>("reverse", f000878),
            new SimpleImmutableEntry<String, Expr>("shifted", f000922),
            new SimpleImmutableEntry<String, Expr>("take", f000930),
            new SimpleImmutableEntry<String, Expr>("unzip", f000949)
          });
  private static final Expr f000951 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Environment", f000183),
            new SimpleImmutableEntry<String, Expr>("Local", f000183),
            new SimpleImmutableEntry<String, Expr>("Missing", null),
            new SimpleImmutableEntry<String, Expr>("Remote", f000183)
          });
  private static final Expr f000952 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("Type", f000951)});
  private static final Expr f000953 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000003),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000002)
          });
  private static final Expr f000954 = Expr.makeLambda("_", f000017, f000953);
  private static final Expr f000955 = Expr.makeLambda("_", f000017, f000954);
  private static final Expr f000956 = Expr.makeApplication(f000009, new Expr[] {f000953});
  private static final Expr f000957 = Expr.makeLambda("_", f000017, f000956);
  private static final Expr f000958 = Expr.makeLambda("_", f000017, f000957);
  private static final Expr f000959 = Expr.makeEmptyListLiteral(f000956);
  private static final Expr f000960 = Expr.makeLambda("_", f000017, f000959);
  private static final Expr f000961 = Expr.makeLambda("_", f000017, f000960);
  private static final Expr f000962 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000014),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000003)
          });
  private static final Expr f000963 = Expr.makeNonEmptyListLiteral(new Expr[] {f000248});
  private static final Expr f000964 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000963, f000002);
  private static final Expr f000965 = Expr.makeLambda("_", f000271, f000964);
  private static final Expr f000966 = Expr.makeLambda("_", f000962, f000965);
  private static final Expr f000967 =
      Expr.makeApplication(f000000, new Expr[] {f000962, f000002, f000205, f000966, f000274});
  private static final Expr f000968 = Expr.makeLambda("_", f000956, f000967);
  private static final Expr f000969 = Expr.makeLambda("_", f000017, f000968);
  private static final Expr f000970 = Expr.makeLambda("_", f000017, f000969);
  private static final Expr f000971 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000052),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000025)
          });
  private static final Expr f000972 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000052),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000014)
          });
  private static final Expr f000973 = Expr.makeApplication(f000009, new Expr[] {f000972});
  private static final Expr f000974 = Expr.makeApplication(f000025, new Expr[] {f000249});
  private static final Expr f000975 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000248),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000974)
          });
  private static final Expr f000976 = Expr.makeNonEmptyListLiteral(new Expr[] {f000975});
  private static final Expr f000977 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000976, f000002);
  private static final Expr f000978 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000894),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000025)
          });
  private static final Expr f000979 = Expr.makeApplication(f000009, new Expr[] {f000978});
  private static final Expr f000980 = Expr.makeLambda("_", f000979, f000977);
  private static final Expr f000981 = Expr.makeLambda("_", f000971, f000980);
  private static final Expr f000982 = Expr.makeEmptyListLiteral(f000973);
  private static final Expr f000983 =
      Expr.makeApplication(f000000, new Expr[] {f000971, f000002, f000973, f000981, f000982});
  private static final Expr f000984 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000025),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000014)
          });
  private static final Expr f000985 = Expr.makeApplication(f000009, new Expr[] {f000984});
  private static final Expr f000986 = Expr.makeLambda("_", f000985, f000983);
  private static final Expr f000987 = Expr.makeLambda("_", f000849, f000986);
  private static final Expr f000988 = Expr.makeLambda("_", f000017, f000987);
  private static final Expr f000989 = Expr.makeLambda("_", f000017, f000988);
  private static final Expr f000990 = Expr.makeLambda("_", f000017, f000989);
  private static final Expr f000991 = Expr.makeNonEmptyListLiteral(new Expr[] {f000249});
  private static final Expr f000992 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000991, f000002);
  private static final Expr f000993 = Expr.makeLambda("_", f000205, f000992);
  private static final Expr f000994 = Expr.makeLambda("_", f000962, f000993);
  private static final Expr f000995 =
      Expr.makeApplication(f000000, new Expr[] {f000962, f000002, f000201, f000994, f000208});
  private static final Expr f000996 = Expr.makeLambda("_", f000956, f000995);
  private static final Expr f000997 = Expr.makeLambda("_", f000017, f000996);
  private static final Expr f000998 = Expr.makeLambda("_", f000017, f000997);
  private static final Expr f000999 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Entry", f000955),
            new SimpleImmutableEntry<String, Expr>("Type", f000958),
            new SimpleImmutableEntry<String, Expr>("empty", f000961),
            new SimpleImmutableEntry<String, Expr>("keyText", f000232),
            new SimpleImmutableEntry<String, Expr>("keyValue", f000235),
            new SimpleImmutableEntry<String, Expr>("keys", f000970),
            new SimpleImmutableEntry<String, Expr>("map", f000990),
            new SimpleImmutableEntry<String, Expr>("values", f000998)
          });
  private static final Expr f001000 = Expr.makeLambda("_", f000017, f000188);
  private static final Expr f001001 = Expr.makeBuiltIn("Natural/build");
  private static final Expr f001002 =
      Expr.makeApplication(f000806, new Expr[] {f000002, f000807, f000811, f000812});
  private static final Expr f001003 = Expr.makeApplication(f000365, new Expr[] {f000804, f001002});
  private static final Expr f001004 = Expr.makeApplication(f000009, new Expr[] {f000178});
  private static final Expr f001005 = Expr.makeNonEmptyListLiteral(new Expr[] {f000367});
  private static final Expr f001006 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001005, f000002);
  private static final Expr f001007 = Expr.makeLambda("_", f001004, f001006);
  private static final Expr f001008 = Expr.makeLambda("_", f000805, f001007);
  private static final Expr f001009 = Expr.makeEmptyListLiteral(f001004);
  private static final Expr f001010 =
      Expr.makeApplication(f000000, new Expr[] {f000805, f001003, f001004, f001008, f001009});
  private static final Expr f001011 = Expr.makeLambda("_", f000178, f001010);
  private static final Expr f001012 = Expr.makeApplication(f000076, new Expr[] {f000002, f000003});
  private static final Expr f001013 = Expr.makeApplication(f000063, new Expr[] {f001012});
  private static final Expr f001014 = Expr.makeApplication(f000076, new Expr[] {f000003, f000002});
  private static final Expr f001015 = Expr.makeApplication(f000063, new Expr[] {f001014});
  private static final Expr f001016 = Expr.makeOperatorApplication(Operator.AND, f001013, f001015);
  private static final Expr f001017 = Expr.makeLambda("_", f000178, f001016);
  private static final Expr f001018 = Expr.makeLambda("_", f000178, f001017);
  private static final Expr f001019 = Expr.makeBuiltIn("Natural/even");
  private static final Expr f001020 =
      Expr.makeOperatorApplication(Operator.EQUALS, f001013, f000012);
  private static final Expr f001021 = Expr.makeLambda("_", f000178, f001020);
  private static final Expr f001022 = Expr.makeLambda("_", f000178, f001021);
  private static final Expr f001023 = Expr.makeLambda("_", f000178, f001015);
  private static final Expr f001024 = Expr.makeLambda("_", f000178, f001023);
  private static final Expr f001025 =
      Expr.makeOperatorApplication(Operator.EQUALS, f001015, f000012);
  private static final Expr f001026 = Expr.makeLambda("_", f000178, f001025);
  private static final Expr f001027 = Expr.makeLambda("_", f000178, f001026);
  private static final Expr f001028 = Expr.makeLambda("_", f000178, f001013);
  private static final Expr f001029 = Expr.makeLambda("_", f000178, f001028);
  private static final Expr f001030 = Expr.makeIf(f001013, f000002, f000003);
  private static final Expr f001031 = Expr.makeLambda("_", f000178, f001030);
  private static final Expr f001032 = Expr.makeLambda("_", f000178, f001031);
  private static final Expr f001033 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000003, f000178, f001032, f000002});
  private static final Expr f001034 = Expr.makeApplication(f000175, new Expr[] {f001033});
  private static final Expr f001035 = Expr.makeLambda("_", f000178, f001034);
  private static final Expr f001036 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000179),
            new SimpleImmutableEntry<String, Expr>("Some", f001035)
          });
  private static final Expr f001037 = Expr.makeApplication(f000494, new Expr[] {f000178, f000002});
  private static final Expr f001038 = Expr.makeMerge(f001036, f001037, null);
  private static final Expr f001039 = Expr.makeLambda("_", f001004, f001038);
  private static final Expr f001040 = Expr.makeApplication(f000063, new Expr[] {f000002});
  private static final Expr f001041 = Expr.makeIf(f001013, f000003, f000002);
  private static final Expr f001042 = Expr.makeLambda("_", f000178, f001041);
  private static final Expr f001043 = Expr.makeLambda("_", f000178, f001042);
  private static final Expr f001044 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000003, f000178, f001043, f000002});
  private static final Expr f001045 = Expr.makeIf(f001040, f000002, f001044);
  private static final Expr f001046 = Expr.makeApplication(f000175, new Expr[] {f001045});
  private static final Expr f001047 = Expr.makeLambda("_", f000178, f001046);
  private static final Expr f001048 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000179),
            new SimpleImmutableEntry<String, Expr>("Some", f001047)
          });
  private static final Expr f001049 = Expr.makeMerge(f001048, f001037, null);
  private static final Expr f001050 = Expr.makeLambda("_", f001004, f001049);
  private static final Expr f001051 = Expr.makeBuiltIn("Natural/odd");
  private static final Expr f001052 =
      Expr.makeOperatorApplication(Operator.TIMES, f000003, f000002);
  private static final Expr f001053 = Expr.makeLambda("_", f000178, f001052);
  private static final Expr f001054 = Expr.makeLambda("_", f000178, f001053);
  private static final Expr f001055 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000002, f000178, f001054, f000368});
  private static final Expr f001056 = Expr.makeLambda("_", f001004, f001055);
  private static final Expr f001057 = Expr.makeApplication(f000343, new Expr[] {f000178, f000002});
  private static final Expr f001058 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f001004),
            new SimpleImmutableEntry<String, Expr>("sorted", f001004)
          });
  private static final Expr f001059 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f001009),
            new SimpleImmutableEntry<String, Expr>("true", f001009)
          });
  private static final Expr f001060 = Expr.makeFieldAccess(f000003, "rest");
  private static final Expr f001061 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f001004),
            new SimpleImmutableEntry<String, Expr>("true", f001004)
          });
  private static final Expr f001062 = Expr.makeApplication(f000076, new Expr[] {f000014, f000003});
  private static final Expr f001063 = Expr.makeApplication(f000063, new Expr[] {f001062});
  private static final Expr f001064 = Expr.makeIf(f001063, f000861, f000863);
  private static final Expr f001065 = Expr.makeLambda("_", f001061, f001064);
  private static final Expr f001066 = Expr.makeLambda("_", f000178, f001065);
  private static final Expr f001067 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f001060, f001061, f001066, f001059});
  private static final Expr f001068 = Expr.makeLambda("_", f000178, f001067);
  private static final Expr f001069 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001059),
            new SimpleImmutableEntry<String, Expr>("Some", f001068)
          });
  private static final Expr f001070 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f001060, f000178, f001043, f000002});
  private static final Expr f001071 = Expr.makeIf(f001040, f000002, f001070);
  private static final Expr f001072 = Expr.makeApplication(f000175, new Expr[] {f001071});
  private static final Expr f001073 = Expr.makeLambda("_", f000178, f001072);
  private static final Expr f001074 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000179),
            new SimpleImmutableEntry<String, Expr>("Some", f001073)
          });
  private static final Expr f001075 = Expr.makeFieldAccess(f000002, "rest");
  private static final Expr f001076 = Expr.makeApplication(f000494, new Expr[] {f000178, f001075});
  private static final Expr f001077 = Expr.makeMerge(f001074, f001076, null);
  private static final Expr f001078 = Expr.makeMerge(f001069, f001077, null);
  private static final Expr f001079 = Expr.makeFieldAccess(f001078, "false");
  private static final Expr f001080 = Expr.makeFieldAccess(f000002, "sorted");
  private static final Expr f001081 = Expr.makeFieldAccess(f001078, "true");
  private static final Expr f001082 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001080, f001081);
  private static final Expr f001083 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f001079),
            new SimpleImmutableEntry<String, Expr>("sorted", f001082)
          });
  private static final Expr f001084 = Expr.makeLambda("_", f001058, f001083);
  private static final Expr f001085 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f000002),
            new SimpleImmutableEntry<String, Expr>("sorted", f001009)
          });
  private static final Expr f001086 =
      Expr.makeApplication(f000806, new Expr[] {f001057, f001058, f001084, f001085});
  private static final Expr f001087 = Expr.makeFieldAccess(f001086, "sorted");
  private static final Expr f001088 = Expr.makeLambda("_", f001004, f001087);
  private static final Expr f001089 = Expr.makeOperatorApplication(Operator.PLUS, f000003, f000002);
  private static final Expr f001090 = Expr.makeLambda("_", f000178, f001089);
  private static final Expr f001091 = Expr.makeLambda("_", f000178, f001090);
  private static final Expr f001092 =
      Expr.makeApplication(f000000, new Expr[] {f000178, f000002, f000178, f001091, f000911});
  private static final Expr f001093 = Expr.makeLambda("_", f001004, f001092);
  private static final Expr f001094 = Expr.makeApplication(f000081, new Expr[] {f000002});
  private static final Expr f001095 = Expr.makeApplication(f000174, new Expr[] {f001094});
  private static final Expr f001096 = Expr.makeLambda("_", f000178, f001095);
  private static final Expr f001097 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("build", f001001),
            new SimpleImmutableEntry<String, Expr>("enumerate", f001011),
            new SimpleImmutableEntry<String, Expr>("equal", f001018),
            new SimpleImmutableEntry<String, Expr>("even", f001019),
            new SimpleImmutableEntry<String, Expr>("fold", f000806),
            new SimpleImmutableEntry<String, Expr>("greaterThan", f001022),
            new SimpleImmutableEntry<String, Expr>("greaterThanEqual", f001024),
            new SimpleImmutableEntry<String, Expr>("isZero", f000063),
            new SimpleImmutableEntry<String, Expr>("lessThan", f001027),
            new SimpleImmutableEntry<String, Expr>("lessThanEqual", f001029),
            new SimpleImmutableEntry<String, Expr>("listMax", f001039),
            new SimpleImmutableEntry<String, Expr>("listMin", f001050),
            new SimpleImmutableEntry<String, Expr>("max", f001032),
            new SimpleImmutableEntry<String, Expr>("min", f001043),
            new SimpleImmutableEntry<String, Expr>("odd", f001051),
            new SimpleImmutableEntry<String, Expr>("product", f001056),
            new SimpleImmutableEntry<String, Expr>("show", f000507),
            new SimpleImmutableEntry<String, Expr>("sort", f001088),
            new SimpleImmutableEntry<String, Expr>("subtract", f000076),
            new SimpleImmutableEntry<String, Expr>("sum", f001093),
            new SimpleImmutableEntry<String, Expr>("toDouble", f001096),
            new SimpleImmutableEntry<String, Expr>("toInteger", f000081)
          });
  private static final Expr f001098 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000007),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001099 = Expr.makeMerge(f001098, f000002, null);
  private static final Expr f001100 = Expr.makeApplication(f000780, new Expr[] {f000003});
  private static final Expr f001101 = Expr.makeLambda("_", f001100, f001099);
  private static final Expr f001102 = Expr.makeLambda("_", f000748, f001101);
  private static final Expr f001103 = Expr.makeLambda("_", f000017, f001102);
  private static final Expr f001104 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000012),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001105 = Expr.makeMerge(f001104, f000002, null);
  private static final Expr f001106 = Expr.makeLambda("_", f001100, f001105);
  private static final Expr f001107 = Expr.makeLambda("_", f000748, f001106);
  private static final Expr f001108 = Expr.makeLambda("_", f000017, f001107);
  private static final Expr f001109 = Expr.makeApplication(f000175, new Expr[] {f000002});
  private static final Expr f001110 = Expr.makeLambda("_", f000003, f001109);
  private static final Expr f001111 = Expr.makeApplication(f000177, new Expr[] {f000003});
  private static final Expr f001112 =
      Expr.makeApplication(f000002, new Expr[] {f001100, f001110, f001111});
  private static final Expr f001113 = Expr.makePi("_", f000849, f000015);
  private static final Expr f001114 = Expr.makePi("_", f000017, f001113);
  private static final Expr f001115 = Expr.makeLambda("_", f001114, f001112);
  private static final Expr f001116 = Expr.makeLambda("_", f000017, f001115);
  private static final Expr f001117 = Expr.makeLambda("_", f001100, f000002);
  private static final Expr f001118 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001111),
            new SimpleImmutableEntry<String, Expr>("Some", f001117)
          });
  private static final Expr f001119 = Expr.makeMerge(f001118, f000002, null);
  private static final Expr f001120 = Expr.makeApplication(f000780, new Expr[] {f000002});
  private static final Expr f001121 = Expr.makeApplication(f000780, new Expr[] {f001120});
  private static final Expr f001122 = Expr.makeLambda("_", f001121, f001119);
  private static final Expr f001123 = Expr.makeLambda("_", f000017, f001122);
  private static final Expr f001124 = Expr.makeLambda("_", f000014, f000002);
  private static final Expr f001125 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000003),
            new SimpleImmutableEntry<String, Expr>("Some", f001124)
          });
  private static final Expr f001126 = Expr.makeMerge(f001125, f000002, null);
  private static final Expr f001127 = Expr.makeLambda("_", f001100, f001126);
  private static final Expr f001128 = Expr.makeLambda("_", f000002, f001127);
  private static final Expr f001129 = Expr.makeLambda("_", f000017, f001128);
  private static final Expr f001130 = Expr.makeApplication(f000177, new Expr[] {f000014});
  private static final Expr f001131 = Expr.makeApplication(f000177, new Expr[] {f000025});
  private static final Expr f001132 = Expr.makeIf(f000050, f001109, f001131);
  private static final Expr f001133 = Expr.makeLambda("_", f000014, f001132);
  private static final Expr f001134 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001130),
            new SimpleImmutableEntry<String, Expr>("Some", f001133)
          });
  private static final Expr f001135 = Expr.makeMerge(f001134, f000002, null);
  private static final Expr f001136 = Expr.makeLambda("_", f001100, f001135);
  private static final Expr f001137 = Expr.makeLambda("_", f000748, f001136);
  private static final Expr f001138 = Expr.makeLambda("_", f000017, f001137);
  private static final Expr f001139 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000002),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001140 = Expr.makeMerge(f001139, f000025, null);
  private static final Expr f001141 = Expr.makeLambda("_", f000003, f001140);
  private static final Expr f001142 = Expr.makePi("_", f000014, f000003);
  private static final Expr f001143 = Expr.makeLambda("_", f001142, f001141);
  private static final Expr f001144 = Expr.makeLambda("_", f000017, f001143);
  private static final Expr f001145 = Expr.makeLambda("_", f001120, f001144);
  private static final Expr f001146 = Expr.makeLambda("_", f000017, f001145);
  private static final Expr f001147 = Expr.makeLambda("_", f000025, f001109);
  private static final Expr f001148 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000002),
            new SimpleImmutableEntry<String, Expr>("Some", f001147)
          });
  private static final Expr f001149 = Expr.makeMerge(f001148, f000003, null);
  private static final Expr f001150 = Expr.makeApplication(f000780, new Expr[] {f000014});
  private static final Expr f001151 = Expr.makeLambda("_", f001150, f001149);
  private static final Expr f001152 = Expr.makeLambda("_", f001100, f001151);
  private static final Expr f001153 =
      Expr.makeApplication(f000000, new Expr[] {f001100, f000002, f001100, f001152, f001111});
  private static final Expr f001154 = Expr.makeApplication(f000009, new Expr[] {f001120});
  private static final Expr f001155 = Expr.makeLambda("_", f001154, f001153);
  private static final Expr f001156 = Expr.makeLambda("_", f000017, f001155);
  private static final Expr f001157 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000003),
            new SimpleImmutableEntry<String, Expr>("Some", f001147)
          });
  private static final Expr f001158 = Expr.makeMerge(f001157, f000002, null);
  private static final Expr f001159 = Expr.makeLambda("_", f001150, f001158);
  private static final Expr f001160 = Expr.makeLambda("_", f001100, f001159);
  private static final Expr f001161 =
      Expr.makeApplication(f000000, new Expr[] {f001100, f000002, f001100, f001160, f001111});
  private static final Expr f001162 = Expr.makeLambda("_", f001154, f001161);
  private static final Expr f001163 = Expr.makeLambda("_", f000017, f001162);
  private static final Expr f001164 = Expr.makeLambda("_", f000003, f000368);
  private static final Expr f001165 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000911),
            new SimpleImmutableEntry<String, Expr>("Some", f001164)
          });
  private static final Expr f001166 = Expr.makeMerge(f001165, f000002, null);
  private static final Expr f001167 = Expr.makeLambda("_", f001120, f001166);
  private static final Expr f001168 = Expr.makeLambda("_", f000017, f001167);
  private static final Expr f001169 = Expr.makeApplication(f000175, new Expr[] {f000050});
  private static final Expr f001170 = Expr.makeLambda("_", f000025, f001169);
  private static final Expr f001171 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001130),
            new SimpleImmutableEntry<String, Expr>("Some", f001170)
          });
  private static final Expr f001172 = Expr.makeMerge(f001171, f000002, null);
  private static final Expr f001173 = Expr.makeLambda("_", f001150, f001172);
  private static final Expr f001174 = Expr.makeLambda("_", f000849, f001173);
  private static final Expr f001175 = Expr.makeLambda("_", f000017, f001174);
  private static final Expr f001176 = Expr.makeLambda("_", f000017, f001175);
  private static final Expr f001177 = Expr.makeLambda("_", f000003, f000012);
  private static final Expr f001178 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000007),
            new SimpleImmutableEntry<String, Expr>("Some", f001177)
          });
  private static final Expr f001179 = Expr.makeMerge(f001178, f000002, null);
  private static final Expr f001180 = Expr.makeLambda("_", f001120, f001179);
  private static final Expr f001181 = Expr.makeLambda("_", f000017, f001180);
  private static final Expr f001182 = Expr.makeNonEmptyListLiteral(new Expr[] {f000002});
  private static final Expr f001183 = Expr.makeLambda("_", f000003, f001182);
  private static final Expr f001184 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000208),
            new SimpleImmutableEntry<String, Expr>("Some", f001183)
          });
  private static final Expr f001185 = Expr.makeMerge(f001184, f000002, null);
  private static final Expr f001186 = Expr.makeLambda("_", f001120, f001185);
  private static final Expr f001187 = Expr.makeLambda("_", f000017, f001186);
  private static final Expr f001188 = Expr.makeFieldAccess(f000002, "_1");
  private static final Expr f001189 = Expr.makeApplication(f000175, new Expr[] {f001188});
  private static final Expr f001190 = Expr.makeLambda("_", f000931, f001189);
  private static final Expr f001191 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001130),
            new SimpleImmutableEntry<String, Expr>("Some", f001190)
          });
  private static final Expr f001192 = Expr.makeMerge(f001191, f000002, null);
  private static final Expr f001193 = Expr.makeFieldAccess(f000002, "_2");
  private static final Expr f001194 = Expr.makeApplication(f000175, new Expr[] {f001193});
  private static final Expr f001195 = Expr.makeLambda("_", f000931, f001194);
  private static final Expr f001196 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001111),
            new SimpleImmutableEntry<String, Expr>("Some", f001195)
          });
  private static final Expr f001197 = Expr.makeMerge(f001196, f000002, null);
  private static final Expr f001198 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001192),
            new SimpleImmutableEntry<String, Expr>("_2", f001197)
          });
  private static final Expr f001199 = Expr.makeApplication(f000780, new Expr[] {f000945});
  private static final Expr f001200 = Expr.makeLambda("_", f001199, f001198);
  private static final Expr f001201 = Expr.makeLambda("_", f000017, f001200);
  private static final Expr f001202 = Expr.makeLambda("_", f000017, f001201);
  private static final Expr f001203 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("all", f001103),
            new SimpleImmutableEntry<String, Expr>("any", f001108),
            new SimpleImmutableEntry<String, Expr>("build", f001116),
            new SimpleImmutableEntry<String, Expr>("concat", f001123),
            new SimpleImmutableEntry<String, Expr>("default", f001129),
            new SimpleImmutableEntry<String, Expr>("filter", f001138),
            new SimpleImmutableEntry<String, Expr>("fold", f001146),
            new SimpleImmutableEntry<String, Expr>("head", f001156),
            new SimpleImmutableEntry<String, Expr>("last", f001163),
            new SimpleImmutableEntry<String, Expr>("length", f001168),
            new SimpleImmutableEntry<String, Expr>("map", f001176),
            new SimpleImmutableEntry<String, Expr>("null", f001181),
            new SimpleImmutableEntry<String, Expr>("toList", f001187),
            new SimpleImmutableEntry<String, Expr>("unzip", f001202)
          });
  private static final Expr f001204 =
      Expr.makeTextLiteral(new String[] {"", "", ""}, new Expr[] {f000003, f000002});
  private static final Expr f001205 = Expr.makeLambda("_", f000183, f001204);
  private static final Expr f001206 = Expr.makeLambda("_", f000183, f001205);
  private static final Expr f001207 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000002, f000183, f001206, f000639});
  private static final Expr f001208 = Expr.makeLambda("_", f000331, f001207);
  private static final Expr f001209 =
      Expr.makeTextLiteral(new String[] {"", "", ""}, new Expr[] {f000742, f000002});
  private static final Expr f001210 = Expr.makeLambda("_", f000183, f001209);
  private static final Expr f001211 = Expr.makeLambda("_", f000014, f001210);
  private static final Expr f001212 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f000183, f001211, f000639});
  private static final Expr f001213 = Expr.makeLambda("_", f000201, f001212);
  private static final Expr f001214 = Expr.makePi("_", f000002, f000183);
  private static final Expr f001215 = Expr.makeLambda("_", f001214, f001213);
  private static final Expr f001216 = Expr.makeLambda("_", f000017, f001215);
  private static final Expr f001217 = Expr.makeLambda("_", f000183, f000002);
  private static final Expr f001218 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f000639),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001217)
          });
  private static final Expr f001219 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", null),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000183)
          });
  private static final Expr f001220 = Expr.makeFieldAccess(f001219, "NonEmpty");
  private static final Expr f001221 = Expr.makeApplication(f001220, new Expr[] {f000742});
  private static final Expr f001222 = Expr.makeApplication(f000052, new Expr[] {f000014});
  private static final Expr f001223 = Expr.makeIdentifier("_", 6);
  private static final Expr f001224 =
      Expr.makeTextLiteral(new String[] {"", "", "", ""}, new Expr[] {f001222, f001223, f000002});
  private static final Expr f001225 = Expr.makeApplication(f001220, new Expr[] {f001224});
  private static final Expr f001226 = Expr.makeLambda("_", f000183, f001225);
  private static final Expr f001227 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f001221),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001226)
          });
  private static final Expr f001228 = Expr.makeMerge(f001227, f000002, null);
  private static final Expr f001229 = Expr.makeLambda("_", f001219, f001228);
  private static final Expr f001230 = Expr.makeLambda("_", f000014, f001229);
  private static final Expr f001231 = Expr.makeFieldAccess(f001219, "Empty");
  private static final Expr f001232 =
      Expr.makeApplication(f000000, new Expr[] {f000014, f000002, f001219, f001230, f001231});
  private static final Expr f001233 = Expr.makeMerge(f001218, f001232, null);
  private static final Expr f001234 = Expr.makeLambda("_", f000201, f001233);
  private static final Expr f001235 = Expr.makeLambda("_", f001214, f001234);
  private static final Expr f001236 = Expr.makeLambda("_", f000017, f001235);
  private static final Expr f001237 = Expr.makeLambda("_", f000183, f001236);
  private static final Expr f001238 = Expr.makeApplication(f001220, new Expr[] {f000003});
  private static final Expr f001239 =
      Expr.makeTextLiteral(new String[] {"", "", "", ""}, new Expr[] {f000014, f000052, f000002});
  private static final Expr f001240 = Expr.makeApplication(f001220, new Expr[] {f001239});
  private static final Expr f001241 = Expr.makeLambda("_", f000183, f001240);
  private static final Expr f001242 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f001238),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001241)
          });
  private static final Expr f001243 = Expr.makeMerge(f001242, f000002, null);
  private static final Expr f001244 = Expr.makeLambda("_", f001219, f001243);
  private static final Expr f001245 = Expr.makeLambda("_", f000183, f001244);
  private static final Expr f001246 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f000002, f001219, f001245, f001231});
  private static final Expr f001247 = Expr.makeMerge(f001218, f001246, null);
  private static final Expr f001248 = Expr.makeLambda("_", f000331, f001247);
  private static final Expr f001249 = Expr.makeLambda("_", f000183, f001248);
  private static final Expr f001250 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000639),
            new SimpleImmutableEntry<String, Expr>("Some", f001217)
          });
  private static final Expr f001251 = Expr.makeMerge(f001250, f000002, null);
  private static final Expr f001252 = Expr.makeApplication(f000780, new Expr[] {f000183});
  private static final Expr f001253 = Expr.makeLambda("_", f001252, f001251);
  private static final Expr f001254 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000639),
            new SimpleImmutableEntry<String, Expr>("Some", f000003)
          });
  private static final Expr f001255 = Expr.makeMerge(f001254, f000002, null);
  private static final Expr f001256 = Expr.makeLambda("_", f001100, f001255);
  private static final Expr f001257 = Expr.makeLambda("_", f001214, f001256);
  private static final Expr f001258 = Expr.makeLambda("_", f000017, f001257);
  private static final Expr f001259 =
      Expr.makeApplication(f000806, new Expr[] {f000003, f000331, f000449, f000334});
  private static final Expr f001260 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f001259, f000183, f001206, f000639});
  private static final Expr f001261 = Expr.makeLambda("_", f000183, f001260);
  private static final Expr f001262 = Expr.makeLambda("_", f000178, f001261);
  private static final Expr f001263 = Expr.makeTextLiteral(" ");
  private static final Expr f001264 = Expr.makeNonEmptyListLiteral(new Expr[] {f001263});
  private static final Expr f001265 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001264, f000002);
  private static final Expr f001266 = Expr.makeLambda("_", f000331, f001265);
  private static final Expr f001267 =
      Expr.makeApplication(f000806, new Expr[] {f000002, f000331, f001266, f000334});
  private static final Expr f001268 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f001267, f000183, f001206, f000639});
  private static final Expr f001269 = Expr.makeLambda("_", f000178, f001268);
  private static final Expr f001270 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("concat", f001208),
            new SimpleImmutableEntry<String, Expr>("concatMap", f001216),
            new SimpleImmutableEntry<String, Expr>("concatMapSep", f001237),
            new SimpleImmutableEntry<String, Expr>("concatSep", f001249),
            new SimpleImmutableEntry<String, Expr>("default", f001253),
            new SimpleImmutableEntry<String, Expr>("defaultMap", f001258),
            new SimpleImmutableEntry<String, Expr>("replicate", f001262),
            new SimpleImmutableEntry<String, Expr>("show", f000519),
            new SimpleImmutableEntry<String, Expr>("spaces", f001269)
          });
  private static final Expr f001271 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000183),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000183)
          });
  private static final Expr f001272 = Expr.makeApplication(f000009, new Expr[] {f001271});
  private static final Expr f001273 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001272),
            new SimpleImmutableEntry<String, Expr>("content", f000187),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001274 = Expr.makePi("_", f001273, f000003);
  private static final Expr f001275 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("element", f001274),
            new SimpleImmutableEntry<String, Expr>("text", f000196)
          });
  private static final Expr f001276 = Expr.makePi("_", f001275, f000003);
  private static final Expr f001277 = Expr.makePi("_", f000017, f001276);
  private static final Expr f001278 = Expr.makeFieldAccess(f000002, "element");
  private static final Expr f001279 = Expr.makeFieldAccess(f000014, "attributes");
  private static final Expr f001280 = Expr.makeFieldAccess(f000014, "content");
  private static final Expr f001281 = Expr.makeLambda("_", f001277, f000206);
  private static final Expr f001282 =
      Expr.makeApplication(f000000, new Expr[] {f001277, f001280, f000201, f001281, f000208});
  private static final Expr f001283 = Expr.makeFieldAccess(f000014, "name");
  private static final Expr f001284 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001279),
            new SimpleImmutableEntry<String, Expr>("content", f001282),
            new SimpleImmutableEntry<String, Expr>("name", f001283)
          });
  private static final Expr f001285 = Expr.makeApplication(f001278, new Expr[] {f001284});
  private static final Expr f001286 = Expr.makeLambda("_", f001275, f001285);
  private static final Expr f001287 = Expr.makeLambda("_", f000017, f001286);
  private static final Expr f001288 = Expr.makeApplication(f000009, new Expr[] {f001277});
  private static final Expr f001289 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001272),
            new SimpleImmutableEntry<String, Expr>("content", f001288),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001290 = Expr.makeLambda("_", f001289, f001287);
  private static final Expr f001291 = Expr.makeEmptyListLiteral(f001272);
  private static final Expr f001292 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001279),
            new SimpleImmutableEntry<String, Expr>("content", f000208),
            new SimpleImmutableEntry<String, Expr>("name", f001283)
          });
  private static final Expr f001293 = Expr.makeApplication(f001278, new Expr[] {f001292});
  private static final Expr f001294 = Expr.makeLambda("_", f001275, f001293);
  private static final Expr f001295 = Expr.makeLambda("_", f000017, f001294);
  private static final Expr f001296 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001272),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001297 = Expr.makeLambda("_", f001296, f001295);
  private static final Expr f001298 = Expr.makeFieldAccess(f000002, "name");
  private static final Expr f001299 = Expr.makeFieldAccess(f000002, "attributes");
  private static final Expr f001300 =
      Expr.makeTextLiteral(
          new String[] {" ", "=\"", "\"", ""}, new Expr[] {f000248, f000249, f000002});
  private static final Expr f001301 = Expr.makeLambda("_", f000183, f001300);
  private static final Expr f001302 = Expr.makeLambda("_", f001271, f001301);
  private static final Expr f001303 =
      Expr.makeApplication(f000000, new Expr[] {f001271, f001299, f000183, f001302, f000639});
  private static final Expr f001304 = Expr.makeFieldAccess(f000002, "content");
  private static final Expr f001305 = Expr.makeApplication(f000343, new Expr[] {f000183, f001304});
  private static final Expr f001306 = Expr.makeApplication(f000063, new Expr[] {f001305});
  private static final Expr f001307 = Expr.makeTextLiteral("/>");
  private static final Expr f001308 =
      Expr.makeApplication(f000000, new Expr[] {f000183, f001304, f000183, f001206, f000639});
  private static final Expr f001309 =
      Expr.makeTextLiteral(new String[] {">", "</", ">"}, new Expr[] {f001308, f001298});
  private static final Expr f001310 = Expr.makeIf(f001306, f001307, f001309);
  private static final Expr f001311 =
      Expr.makeTextLiteral(new String[] {"<", "", "", ""}, new Expr[] {f001298, f001303, f001310});
  private static final Expr f001312 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001272),
            new SimpleImmutableEntry<String, Expr>("content", f000331),
            new SimpleImmutableEntry<String, Expr>("name", f000183)
          });
  private static final Expr f001313 = Expr.makeLambda("_", f001312, f001311);
  private static final Expr f001314 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("element", f001313),
            new SimpleImmutableEntry<String, Expr>("text", f001217)
          });
  private static final Expr f001315 = Expr.makeApplication(f000002, new Expr[] {f000183, f001314});
  private static final Expr f001316 = Expr.makeLambda("_", f001277, f001315);
  private static final Expr f001317 = Expr.makeFieldAccess(f000002, "text");
  private static final Expr f001318 = Expr.makeApplication(f001317, new Expr[] {f000014});
  private static final Expr f001319 = Expr.makeLambda("_", f001275, f001318);
  private static final Expr f001320 = Expr.makeLambda("_", f000017, f001319);
  private static final Expr f001321 = Expr.makeLambda("_", f000183, f001320);
  private static final Expr f001322 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Type", f001277),
            new SimpleImmutableEntry<String, Expr>("attribute", f000232),
            new SimpleImmutableEntry<String, Expr>("element", f001290),
            new SimpleImmutableEntry<String, Expr>("emptyAttributes", f001291),
            new SimpleImmutableEntry<String, Expr>("leaf", f001297),
            new SimpleImmutableEntry<String, Expr>("render", f001316),
            new SimpleImmutableEntry<String, Expr>("text", f001321)
          });
  private static final Expr f001323 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Bool", f000047),
            new SimpleImmutableEntry<String, Expr>("Double", f000049),
            new SimpleImmutableEntry<String, Expr>("Function", f000062),
            new SimpleImmutableEntry<String, Expr>("Integer", f000182),
            new SimpleImmutableEntry<String, Expr>("JSON", f000741),
            new SimpleImmutableEntry<String, Expr>("List", f000950),
            new SimpleImmutableEntry<String, Expr>("Location", f000952),
            new SimpleImmutableEntry<String, Expr>("Map", f000999),
            new SimpleImmutableEntry<String, Expr>("Monoid", f001000),
            new SimpleImmutableEntry<String, Expr>("Natural", f001097),
            new SimpleImmutableEntry<String, Expr>("Optional", f001203),
            new SimpleImmutableEntry<String, Expr>("Text", f001270),
            new SimpleImmutableEntry<String, Expr>("XML", f001322)
          });

  public static final Expr instance = f001323;
}
