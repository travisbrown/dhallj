package org.dhallj.prelude;

import java.math.BigInteger;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;

public final class Prelude {
  private static final Expr f000000 = Expr.makeBuiltIn("List/fold");
  private static final Expr f000001 = Expr.makeBuiltIn("Bool");
  private static final Expr f000002 = Expr.makeIdentifier("xs", 0);
  private static final Expr f000003 = Expr.makeIdentifier("l", 0);
  private static final Expr f000004 = Expr.makeIdentifier("r", 0);
  private static final Expr f000005 = Expr.makeOperatorApplication(Operator.AND, f000003, f000004);
  private static final Expr f000006 = Expr.makeLambda("r", f000001, f000005);
  private static final Expr f000007 = Expr.makeLambda("l", f000001, f000006);
  private static final Expr f000008 = Expr.Constants.TRUE;
  private static final Expr f000009 =
      Expr.makeApplication(f000000, new Expr[] {f000001, f000002, f000001, f000007, f000008});
  private static final Expr f000010 = Expr.Constants.LIST;
  private static final Expr f000011 = Expr.makeApplication(f000010, new Expr[] {f000001});
  private static final Expr f000012 = Expr.makeLambda("xs", f000011, f000009);
  private static final Expr f000013 = Expr.makeIdentifier("f", 0);
  private static final Expr f000014 = Expr.Constants.FALSE;
  private static final Expr f000015 =
      Expr.makeApplication(f000013, new Expr[] {f000001, f000008, f000014});
  private static final Expr f000016 = Expr.makeIdentifier("bool", 0);
  private static final Expr f000017 = Expr.makePi("false", f000016, f000016);
  private static final Expr f000018 = Expr.makePi("true", f000016, f000017);
  private static final Expr f000019 = Expr.Constants.TYPE;
  private static final Expr f000020 = Expr.makePi("bool", f000019, f000018);
  private static final Expr f000021 = Expr.makeLambda("f", f000020, f000015);
  private static final Expr f000022 = Expr.makeIdentifier("x", 0);
  private static final Expr f000023 = Expr.makeIdentifier("y", 0);
  private static final Expr f000024 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000022, f000023);
  private static final Expr f000025 = Expr.makeLambda("y", f000001, f000024);
  private static final Expr f000026 = Expr.makeLambda("x", f000001, f000025);
  private static final Expr f000027 =
      Expr.makeApplication(f000000, new Expr[] {f000001, f000002, f000001, f000026, f000008});
  private static final Expr f000028 = Expr.makeLambda("xs", f000011, f000027);
  private static final Expr f000029 = Expr.makeIdentifier("b", 0);
  private static final Expr f000030 = Expr.makeIdentifier("true", 0);
  private static final Expr f000031 = Expr.makeIdentifier("false", 0);
  private static final Expr f000032 = Expr.makeIf(f000029, f000030, f000031);
  private static final Expr f000033 = Expr.makeLambda("false", f000016, f000032);
  private static final Expr f000034 = Expr.makeLambda("true", f000016, f000033);
  private static final Expr f000035 = Expr.makeLambda("bool", f000019, f000034);
  private static final Expr f000036 = Expr.makeLambda("b", f000001, f000035);
  private static final Expr f000037 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000029, f000014);
  private static final Expr f000038 = Expr.makeLambda("b", f000001, f000037);
  private static final Expr f000039 =
      Expr.makeOperatorApplication(Operator.NOT_EQUALS, f000022, f000023);
  private static final Expr f000040 = Expr.makeLambda("y", f000001, f000039);
  private static final Expr f000041 = Expr.makeLambda("x", f000001, f000040);
  private static final Expr f000042 =
      Expr.makeApplication(f000000, new Expr[] {f000001, f000002, f000001, f000041, f000014});
  private static final Expr f000043 = Expr.makeLambda("xs", f000011, f000042);
  private static final Expr f000044 = Expr.makeOperatorApplication(Operator.OR, f000003, f000004);
  private static final Expr f000045 = Expr.makeLambda("r", f000001, f000044);
  private static final Expr f000046 = Expr.makeLambda("l", f000001, f000045);
  private static final Expr f000047 =
      Expr.makeApplication(f000000, new Expr[] {f000001, f000002, f000001, f000046, f000014});
  private static final Expr f000048 = Expr.makeLambda("xs", f000011, f000047);
  private static final Expr f000049 = Expr.makeTextLiteral("True");
  private static final Expr f000050 = Expr.makeTextLiteral("False");
  private static final Expr f000051 = Expr.makeIf(f000029, f000049, f000050);
  private static final Expr f000052 = Expr.makeLambda("b", f000001, f000051);
  private static final Expr f000053 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("and", f000012),
            new SimpleImmutableEntry<String, Expr>("build", f000021),
            new SimpleImmutableEntry<String, Expr>("even", f000028),
            new SimpleImmutableEntry<String, Expr>("fold", f000036),
            new SimpleImmutableEntry<String, Expr>("not", f000038),
            new SimpleImmutableEntry<String, Expr>("odd", f000043),
            new SimpleImmutableEntry<String, Expr>("or", f000048),
            new SimpleImmutableEntry<String, Expr>("show", f000052)
          });
  private static final Expr f000054 = Expr.makeBuiltIn("Double/show");
  private static final Expr f000055 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("show", f000054)});
  private static final Expr f000056 = Expr.makeIdentifier("g", 0);
  private static final Expr f000057 = Expr.makeApplication(f000013, new Expr[] {f000022});
  private static final Expr f000058 = Expr.makeApplication(f000056, new Expr[] {f000057});
  private static final Expr f000059 = Expr.makeIdentifier("A", 0);
  private static final Expr f000060 = Expr.makeLambda("x", f000059, f000058);
  private static final Expr f000061 = Expr.makeIdentifier("C", 0);
  private static final Expr f000062 = Expr.makeIdentifier("B", 0);
  private static final Expr f000063 = Expr.makePi("_", f000062, f000061);
  private static final Expr f000064 = Expr.makeLambda("g", f000063, f000060);
  private static final Expr f000065 = Expr.makePi("_", f000059, f000062);
  private static final Expr f000066 = Expr.makeLambda("f", f000065, f000064);
  private static final Expr f000067 = Expr.makeLambda("C", f000019, f000066);
  private static final Expr f000068 = Expr.makeLambda("B", f000019, f000067);
  private static final Expr f000069 = Expr.makeLambda("A", f000019, f000068);
  private static final Expr f000070 = Expr.makeIdentifier("a", 0);
  private static final Expr f000071 = Expr.makeLambda("x", f000070, f000022);
  private static final Expr f000072 = Expr.makeLambda("a", f000019, f000071);
  private static final Expr f000073 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("compose", f000069),
            new SimpleImmutableEntry<String, Expr>("identity", f000072)
          });
  private static final Expr f000074 = Expr.makeBuiltIn("Natural/isZero");
  private static final Expr f000075 = Expr.makeBuiltIn("Integer/clamp");
  private static final Expr f000076 = Expr.makeIdentifier("n", 0);
  private static final Expr f000077 = Expr.makeApplication(f000075, new Expr[] {f000076});
  private static final Expr f000078 = Expr.makeApplication(f000074, new Expr[] {f000077});
  private static final Expr f000079 = Expr.makeBuiltIn("Integer/negate");
  private static final Expr f000080 = Expr.makeApplication(f000079, new Expr[] {f000076});
  private static final Expr f000081 = Expr.makeApplication(f000075, new Expr[] {f000080});
  private static final Expr f000082 = Expr.makeIf(f000078, f000081, f000077);
  private static final Expr f000083 = Expr.Constants.INTEGER;
  private static final Expr f000084 = Expr.makeLambda("n", f000083, f000082);
  private static final Expr f000085 = Expr.makeIdentifier("m", 0);
  private static final Expr f000086 = Expr.makeApplication(f000079, new Expr[] {f000085});
  private static final Expr f000087 = Expr.makeApplication(f000075, new Expr[] {f000086});
  private static final Expr f000088 = Expr.makeApplication(f000074, new Expr[] {f000087});
  private static final Expr f000089 = Expr.makeBuiltIn("Natural/subtract");
  private static final Expr f000090 = Expr.makeApplication(f000079, new Expr[] {f000086});
  private static final Expr f000091 = Expr.makeApplication(f000075, new Expr[] {f000090});
  private static final Expr f000092 = Expr.makeApplication(f000089, new Expr[] {f000081, f000091});
  private static final Expr f000093 = Expr.makeApplication(f000074, new Expr[] {f000092});
  private static final Expr f000094 = Expr.makeBuiltIn("Natural/toInteger");
  private static final Expr f000095 = Expr.makeApplication(f000089, new Expr[] {f000091, f000081});
  private static final Expr f000096 = Expr.makeApplication(f000094, new Expr[] {f000095});
  private static final Expr f000097 = Expr.makeApplication(f000079, new Expr[] {f000096});
  private static final Expr f000098 = Expr.makeApplication(f000094, new Expr[] {f000092});
  private static final Expr f000099 = Expr.makeIf(f000093, f000097, f000098);
  private static final Expr f000100 = Expr.makeOperatorApplication(Operator.PLUS, f000091, f000077);
  private static final Expr f000101 = Expr.makeApplication(f000094, new Expr[] {f000100});
  private static final Expr f000102 = Expr.makeIf(f000078, f000099, f000101);
  private static final Expr f000103 = Expr.makeOperatorApplication(Operator.PLUS, f000087, f000081);
  private static final Expr f000104 = Expr.makeApplication(f000094, new Expr[] {f000103});
  private static final Expr f000105 = Expr.makeApplication(f000079, new Expr[] {f000104});
  private static final Expr f000106 = Expr.makeApplication(f000089, new Expr[] {f000087, f000077});
  private static final Expr f000107 = Expr.makeApplication(f000074, new Expr[] {f000106});
  private static final Expr f000108 = Expr.makeApplication(f000089, new Expr[] {f000077, f000087});
  private static final Expr f000109 = Expr.makeApplication(f000094, new Expr[] {f000108});
  private static final Expr f000110 = Expr.makeApplication(f000079, new Expr[] {f000109});
  private static final Expr f000111 = Expr.makeApplication(f000094, new Expr[] {f000106});
  private static final Expr f000112 = Expr.makeIf(f000107, f000110, f000111);
  private static final Expr f000113 = Expr.makeIf(f000078, f000105, f000112);
  private static final Expr f000114 = Expr.makeIf(f000088, f000102, f000113);
  private static final Expr f000115 = Expr.makeLambda("n", f000083, f000114);
  private static final Expr f000116 = Expr.makeLambda("m", f000083, f000115);
  private static final Expr f000117 = Expr.makeApplication(f000075, new Expr[] {f000029});
  private static final Expr f000118 = Expr.makeApplication(f000075, new Expr[] {f000070});
  private static final Expr f000119 = Expr.makeApplication(f000089, new Expr[] {f000117, f000118});
  private static final Expr f000120 = Expr.makeApplication(f000074, new Expr[] {f000119});
  private static final Expr f000121 = Expr.makeApplication(f000089, new Expr[] {f000118, f000117});
  private static final Expr f000122 = Expr.makeApplication(f000074, new Expr[] {f000121});
  private static final Expr f000123 = Expr.makeOperatorApplication(Operator.AND, f000120, f000122);
  private static final Expr f000124 = Expr.makeApplication(f000079, new Expr[] {f000029});
  private static final Expr f000125 = Expr.makeApplication(f000075, new Expr[] {f000124});
  private static final Expr f000126 = Expr.makeApplication(f000079, new Expr[] {f000070});
  private static final Expr f000127 = Expr.makeApplication(f000075, new Expr[] {f000126});
  private static final Expr f000128 = Expr.makeApplication(f000089, new Expr[] {f000125, f000127});
  private static final Expr f000129 = Expr.makeApplication(f000074, new Expr[] {f000128});
  private static final Expr f000130 = Expr.makeApplication(f000089, new Expr[] {f000127, f000125});
  private static final Expr f000131 = Expr.makeApplication(f000074, new Expr[] {f000130});
  private static final Expr f000132 = Expr.makeOperatorApplication(Operator.AND, f000129, f000131);
  private static final Expr f000133 = Expr.makeOperatorApplication(Operator.AND, f000123, f000132);
  private static final Expr f000134 = Expr.makeLambda("b", f000083, f000133);
  private static final Expr f000135 = Expr.makeLambda("a", f000083, f000134);
  private static final Expr f000136 = Expr.makeApplication(f000075, new Expr[] {f000022});
  private static final Expr f000137 = Expr.makeApplication(f000074, new Expr[] {f000136});
  private static final Expr f000138 = Expr.makeApplication(f000079, new Expr[] {f000023});
  private static final Expr f000139 = Expr.makeApplication(f000075, new Expr[] {f000138});
  private static final Expr f000140 = Expr.makeApplication(f000074, new Expr[] {f000139});
  private static final Expr f000141 = Expr.makeApplication(f000079, new Expr[] {f000022});
  private static final Expr f000142 = Expr.makeApplication(f000075, new Expr[] {f000141});
  private static final Expr f000143 = Expr.makeApplication(f000089, new Expr[] {f000142, f000139});
  private static final Expr f000144 = Expr.makeApplication(f000074, new Expr[] {f000143});
  private static final Expr f000145 = Expr.makeOperatorApplication(Operator.OR, f000140, f000144);
  private static final Expr f000146 = Expr.makeApplication(f000075, new Expr[] {f000023});
  private static final Expr f000147 = Expr.makeApplication(f000089, new Expr[] {f000146, f000136});
  private static final Expr f000148 = Expr.makeApplication(f000074, new Expr[] {f000147});
  private static final Expr f000149 = Expr.makeIf(f000137, f000145, f000148);
  private static final Expr f000150 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000149, f000014);
  private static final Expr f000151 = Expr.makeLambda("y", f000083, f000150);
  private static final Expr f000152 = Expr.makeLambda("x", f000083, f000151);
  private static final Expr f000153 = Expr.makeApplication(f000074, new Expr[] {f000146});
  private static final Expr f000154 = Expr.makeApplication(f000074, new Expr[] {f000142});
  private static final Expr f000155 = Expr.makeApplication(f000089, new Expr[] {f000139, f000142});
  private static final Expr f000156 = Expr.makeApplication(f000074, new Expr[] {f000155});
  private static final Expr f000157 = Expr.makeOperatorApplication(Operator.OR, f000154, f000156);
  private static final Expr f000158 = Expr.makeApplication(f000089, new Expr[] {f000136, f000146});
  private static final Expr f000159 = Expr.makeApplication(f000074, new Expr[] {f000158});
  private static final Expr f000160 = Expr.makeIf(f000153, f000157, f000159);
  private static final Expr f000161 = Expr.makeLambda("y", f000083, f000160);
  private static final Expr f000162 = Expr.makeLambda("x", f000083, f000161);
  private static final Expr f000163 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000160, f000014);
  private static final Expr f000164 = Expr.makeLambda("y", f000083, f000163);
  private static final Expr f000165 = Expr.makeLambda("x", f000083, f000164);
  private static final Expr f000166 = Expr.makeLambda("y", f000083, f000149);
  private static final Expr f000167 = Expr.makeLambda("x", f000083, f000166);
  private static final Expr f000168 = Expr.makeApplication(f000075, new Expr[] {f000085});
  private static final Expr f000169 = Expr.makeApplication(f000074, new Expr[] {f000168});
  private static final Expr f000170 =
      Expr.makeOperatorApplication(Operator.TIMES, f000087, f000081);
  private static final Expr f000171 = Expr.makeApplication(f000094, new Expr[] {f000170});
  private static final Expr f000172 =
      Expr.makeOperatorApplication(Operator.TIMES, f000087, f000077);
  private static final Expr f000173 = Expr.makeApplication(f000094, new Expr[] {f000172});
  private static final Expr f000174 = Expr.makeApplication(f000079, new Expr[] {f000173});
  private static final Expr f000175 = Expr.makeIf(f000078, f000171, f000174);
  private static final Expr f000176 =
      Expr.makeOperatorApplication(Operator.TIMES, f000168, f000081);
  private static final Expr f000177 = Expr.makeApplication(f000094, new Expr[] {f000176});
  private static final Expr f000178 = Expr.makeApplication(f000079, new Expr[] {f000177});
  private static final Expr f000179 =
      Expr.makeOperatorApplication(Operator.TIMES, f000168, f000077);
  private static final Expr f000180 = Expr.makeApplication(f000094, new Expr[] {f000179});
  private static final Expr f000181 = Expr.makeIf(f000078, f000178, f000180);
  private static final Expr f000182 = Expr.makeIf(f000169, f000175, f000181);
  private static final Expr f000183 = Expr.makeLambda("n", f000083, f000182);
  private static final Expr f000184 = Expr.makeLambda("m", f000083, f000183);
  private static final Expr f000185 = Expr.makeApplication(f000074, new Expr[] {f000081});
  private static final Expr f000186 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000185, f000014);
  private static final Expr f000187 = Expr.makeLambda("n", f000083, f000186);
  private static final Expr f000188 = Expr.makeLambda("n", f000083, f000185);
  private static final Expr f000189 = Expr.makeLambda("n", f000083, f000078);
  private static final Expr f000190 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000078, f000014);
  private static final Expr f000191 = Expr.makeLambda("n", f000083, f000190);
  private static final Expr f000192 = Expr.makeBuiltIn("Integer/show");
  private static final Expr f000193 = Expr.makeApplication(f000089, new Expr[] {f000081, f000087});
  private static final Expr f000194 = Expr.makeApplication(f000074, new Expr[] {f000193});
  private static final Expr f000195 = Expr.makeApplication(f000089, new Expr[] {f000087, f000081});
  private static final Expr f000196 = Expr.makeApplication(f000094, new Expr[] {f000195});
  private static final Expr f000197 = Expr.makeApplication(f000079, new Expr[] {f000196});
  private static final Expr f000198 = Expr.makeApplication(f000094, new Expr[] {f000193});
  private static final Expr f000199 = Expr.makeIf(f000194, f000197, f000198);
  private static final Expr f000200 = Expr.makeOperatorApplication(Operator.PLUS, f000087, f000077);
  private static final Expr f000201 = Expr.makeApplication(f000094, new Expr[] {f000200});
  private static final Expr f000202 = Expr.makeIf(f000078, f000199, f000201);
  private static final Expr f000203 = Expr.makeOperatorApplication(Operator.PLUS, f000168, f000081);
  private static final Expr f000204 = Expr.makeApplication(f000094, new Expr[] {f000203});
  private static final Expr f000205 = Expr.makeApplication(f000079, new Expr[] {f000204});
  private static final Expr f000206 = Expr.makeApplication(f000089, new Expr[] {f000168, f000077});
  private static final Expr f000207 = Expr.makeApplication(f000074, new Expr[] {f000206});
  private static final Expr f000208 = Expr.makeApplication(f000089, new Expr[] {f000077, f000168});
  private static final Expr f000209 = Expr.makeApplication(f000094, new Expr[] {f000208});
  private static final Expr f000210 = Expr.makeApplication(f000079, new Expr[] {f000209});
  private static final Expr f000211 = Expr.makeApplication(f000094, new Expr[] {f000206});
  private static final Expr f000212 = Expr.makeIf(f000207, f000210, f000211);
  private static final Expr f000213 = Expr.makeIf(f000078, f000205, f000212);
  private static final Expr f000214 = Expr.makeIf(f000169, f000202, f000213);
  private static final Expr f000215 = Expr.makeLambda("n", f000083, f000214);
  private static final Expr f000216 = Expr.makeLambda("m", f000083, f000215);
  private static final Expr f000217 = Expr.makeBuiltIn("Integer/toDouble");
  private static final Expr f000218 = Expr.makeBuiltIn("Some");
  private static final Expr f000219 = Expr.makeApplication(f000218, new Expr[] {f000077});
  private static final Expr f000220 = Expr.makeBuiltIn("None");
  private static final Expr f000221 = Expr.Constants.NATURAL;
  private static final Expr f000222 = Expr.makeApplication(f000220, new Expr[] {f000221});
  private static final Expr f000223 = Expr.makeIf(f000185, f000219, f000222);
  private static final Expr f000224 = Expr.makeLambda("n", f000083, f000223);
  private static final Expr f000225 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("abs", f000084),
            new SimpleImmutableEntry<String, Expr>("add", f000116),
            new SimpleImmutableEntry<String, Expr>("clamp", f000075),
            new SimpleImmutableEntry<String, Expr>("equal", f000135),
            new SimpleImmutableEntry<String, Expr>("greaterThan", f000152),
            new SimpleImmutableEntry<String, Expr>("greaterThanEqual", f000162),
            new SimpleImmutableEntry<String, Expr>("lessThan", f000165),
            new SimpleImmutableEntry<String, Expr>("lessThanEqual", f000167),
            new SimpleImmutableEntry<String, Expr>("multiply", f000184),
            new SimpleImmutableEntry<String, Expr>("negate", f000079),
            new SimpleImmutableEntry<String, Expr>("negative", f000187),
            new SimpleImmutableEntry<String, Expr>("nonNegative", f000188),
            new SimpleImmutableEntry<String, Expr>("nonPositive", f000189),
            new SimpleImmutableEntry<String, Expr>("positive", f000191),
            new SimpleImmutableEntry<String, Expr>("show", f000192),
            new SimpleImmutableEntry<String, Expr>("subtract", f000216),
            new SimpleImmutableEntry<String, Expr>("toDouble", f000217),
            new SimpleImmutableEntry<String, Expr>("toNatural", f000224)
          });
  private static final Expr f000226 = Expr.Constants.TEXT;
  private static final Expr f000227 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Inline", null),
            new SimpleImmutableEntry<String, Expr>("Nested", f000226)
          });
  private static final Expr f000228 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("contents", f000070),
            new SimpleImmutableEntry<String, Expr>("field", f000226),
            new SimpleImmutableEntry<String, Expr>("nesting", f000227)
          });
  private static final Expr f000229 = Expr.makeLambda("a", f000019, f000228);
  private static final Expr f000230 = Expr.makeIdentifier("JSON", 0);
  private static final Expr f000231 = Expr.makeApplication(f000010, new Expr[] {f000230});
  private static final Expr f000232 = Expr.makePi("_", f000231, f000230);
  private static final Expr f000233 = Expr.makePi("_", f000001, f000230);
  private static final Expr f000234 = Expr.Constants.DOUBLE;
  private static final Expr f000235 = Expr.makePi("_", f000234, f000230);
  private static final Expr f000236 = Expr.makePi("_", f000083, f000230);
  private static final Expr f000237 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000226),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000230)
          });
  private static final Expr f000238 = Expr.makeApplication(f000010, new Expr[] {f000237});
  private static final Expr f000239 = Expr.makePi("_", f000238, f000230);
  private static final Expr f000240 = Expr.makePi("_", f000226, f000230);
  private static final Expr f000241 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000232),
            new SimpleImmutableEntry<String, Expr>("bool", f000233),
            new SimpleImmutableEntry<String, Expr>("double", f000235),
            new SimpleImmutableEntry<String, Expr>("integer", f000236),
            new SimpleImmutableEntry<String, Expr>("null", f000230),
            new SimpleImmutableEntry<String, Expr>("object", f000239),
            new SimpleImmutableEntry<String, Expr>("string", f000240)
          });
  private static final Expr f000242 = Expr.makePi("json", f000241, f000230);
  private static final Expr f000243 = Expr.makePi("JSON", f000019, f000242);
  private static final Expr f000244 = Expr.makeIdentifier("json", 0);
  private static final Expr f000245 = Expr.makeFieldAccess(f000244, "array");
  private static final Expr f000246 = Expr.makeApplication(f000022, new Expr[] {f000230, f000244});
  private static final Expr f000247 = Expr.makeNonEmptyListLiteral(new Expr[] {f000246});
  private static final Expr f000248 = Expr.makeIdentifier("as", 0);
  private static final Expr f000249 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000247, f000248);
  private static final Expr f000250 = Expr.makeLambda("as", f000231, f000249);
  private static final Expr f000251 = Expr.makeLambda("x", f000243, f000250);
  private static final Expr f000252 = Expr.makeEmptyListLiteral(f000231);
  private static final Expr f000253 =
      Expr.makeApplication(f000000, new Expr[] {f000243, f000022, f000231, f000251, f000252});
  private static final Expr f000254 = Expr.makeApplication(f000245, new Expr[] {f000253});
  private static final Expr f000255 = Expr.makeLambda("json", f000241, f000254);
  private static final Expr f000256 = Expr.makeLambda("JSON", f000019, f000255);
  private static final Expr f000257 = Expr.makeApplication(f000010, new Expr[] {f000243});
  private static final Expr f000258 = Expr.makeLambda("x", f000257, f000256);
  private static final Expr f000259 = Expr.makeFieldAccess(f000244, "bool");
  private static final Expr f000260 = Expr.makeApplication(f000259, new Expr[] {f000022});
  private static final Expr f000261 = Expr.makeLambda("json", f000241, f000260);
  private static final Expr f000262 = Expr.makeLambda("JSON", f000019, f000261);
  private static final Expr f000263 = Expr.makeLambda("x", f000001, f000262);
  private static final Expr f000264 = Expr.makeFieldAccess(f000244, "double");
  private static final Expr f000265 = Expr.makeApplication(f000264, new Expr[] {f000022});
  private static final Expr f000266 = Expr.makeLambda("json", f000241, f000265);
  private static final Expr f000267 = Expr.makeLambda("JSON", f000019, f000266);
  private static final Expr f000268 = Expr.makeLambda("x", f000234, f000267);
  private static final Expr f000269 = Expr.makeFieldAccess(f000244, "integer");
  private static final Expr f000270 = Expr.makeApplication(f000269, new Expr[] {f000022});
  private static final Expr f000271 = Expr.makeLambda("json", f000241, f000270);
  private static final Expr f000272 = Expr.makeLambda("JSON", f000019, f000271);
  private static final Expr f000273 = Expr.makeLambda("x", f000083, f000272);
  private static final Expr f000274 = Expr.makeIdentifier("key", 0);
  private static final Expr f000275 = Expr.makeIdentifier("value", 0);
  private static final Expr f000276 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000274),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000275)
          });
  private static final Expr f000277 = Expr.makeLambda("value", f000226, f000276);
  private static final Expr f000278 = Expr.makeLambda("key", f000226, f000277);
  private static final Expr f000279 = Expr.makeIdentifier("v", 0);
  private static final Expr f000280 = Expr.makeLambda("value", f000279, f000276);
  private static final Expr f000281 = Expr.makeLambda("key", f000226, f000280);
  private static final Expr f000282 = Expr.makeLambda("v", f000019, f000281);
  private static final Expr f000283 = Expr.makeApplication(f000094, new Expr[] {f000022});
  private static final Expr f000284 = Expr.makeApplication(f000269, new Expr[] {f000283});
  private static final Expr f000285 = Expr.makeLambda("json", f000241, f000284);
  private static final Expr f000286 = Expr.makeLambda("JSON", f000019, f000285);
  private static final Expr f000287 = Expr.makeLambda("x", f000221, f000286);
  private static final Expr f000288 = Expr.makeFieldAccess(f000244, "null");
  private static final Expr f000289 = Expr.makeLambda("json", f000241, f000288);
  private static final Expr f000290 = Expr.makeLambda("JSON", f000019, f000289);
  private static final Expr f000291 = Expr.makeFieldAccess(f000244, "object");
  private static final Expr f000292 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000226),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000243)
          });
  private static final Expr f000293 = Expr.makeFieldAccess(f000022, "mapKey");
  private static final Expr f000294 = Expr.makeFieldAccess(f000022, "mapValue");
  private static final Expr f000295 = Expr.makeApplication(f000294, new Expr[] {f000230, f000244});
  private static final Expr f000296 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000293),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000295)
          });
  private static final Expr f000297 = Expr.makeNonEmptyListLiteral(new Expr[] {f000296});
  private static final Expr f000298 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000297, f000248);
  private static final Expr f000299 = Expr.makeLambda("as", f000238, f000298);
  private static final Expr f000300 = Expr.makeLambda("x", f000292, f000299);
  private static final Expr f000301 = Expr.makeEmptyListLiteral(f000238);
  private static final Expr f000302 =
      Expr.makeApplication(f000000, new Expr[] {f000292, f000022, f000238, f000300, f000301});
  private static final Expr f000303 = Expr.makeApplication(f000291, new Expr[] {f000302});
  private static final Expr f000304 = Expr.makeLambda("json", f000241, f000303);
  private static final Expr f000305 = Expr.makeLambda("JSON", f000019, f000304);
  private static final Expr f000306 = Expr.makeApplication(f000010, new Expr[] {f000292});
  private static final Expr f000307 = Expr.makeLambda("x", f000306, f000305);
  private static final Expr f000308 = Expr.makeIdentifier("old", 0);
  private static final Expr f000309 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000001),
            new SimpleImmutableEntry<String, Expr>("value", f000230)
          });
  private static final Expr f000310 = Expr.makeFieldAccess(f000022, "value");
  private static final Expr f000311 = Expr.makeNonEmptyListLiteral(new Expr[] {f000310});
  private static final Expr f000312 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000311, f000248);
  private static final Expr f000313 = Expr.makeLambda("as", f000231, f000312);
  private static final Expr f000314 = Expr.makeLambda("x", f000309, f000313);
  private static final Expr f000315 =
      Expr.makeApplication(f000000, new Expr[] {f000309, f000002, f000231, f000314, f000252});
  private static final Expr f000316 = Expr.makeApplication(f000245, new Expr[] {f000315});
  private static final Expr f000317 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000014),
            new SimpleImmutableEntry<String, Expr>("value", f000316)
          });
  private static final Expr f000318 = Expr.makeApplication(f000010, new Expr[] {f000309});
  private static final Expr f000319 = Expr.makeLambda("xs", f000318, f000317);
  private static final Expr f000320 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000014),
            new SimpleImmutableEntry<String, Expr>("value", f000260)
          });
  private static final Expr f000321 = Expr.makeLambda("x", f000001, f000320);
  private static final Expr f000322 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000014),
            new SimpleImmutableEntry<String, Expr>("value", f000265)
          });
  private static final Expr f000323 = Expr.makeLambda("x", f000234, f000322);
  private static final Expr f000324 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000014),
            new SimpleImmutableEntry<String, Expr>("value", f000270)
          });
  private static final Expr f000325 = Expr.makeLambda("x", f000083, f000324);
  private static final Expr f000326 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000008),
            new SimpleImmutableEntry<String, Expr>("value", f000288)
          });
  private static final Expr f000327 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000226),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000309)
          });
  private static final Expr f000328 = Expr.makeIdentifier("keyValues", 0);
  private static final Expr f000329 = Expr.makeFieldAccess(f000294, "isNull");
  private static final Expr f000330 = Expr.makeProjection(f000022, new String[] {"mapKey"});
  private static final Expr f000331 = Expr.makeFieldAccess(f000294, "value");
  private static final Expr f000332 =
      Expr.makeRecordLiteral(
          new Entry[] {new SimpleImmutableEntry<String, Expr>("mapValue", f000331)});
  private static final Expr f000333 =
      Expr.makeOperatorApplication(Operator.COMBINE, f000330, f000332);
  private static final Expr f000334 = Expr.makeNonEmptyListLiteral(new Expr[] {f000333});
  private static final Expr f000335 = Expr.makeIf(f000329, f000301, f000334);
  private static final Expr f000336 = Expr.makeNonEmptyListLiteral(new Expr[] {f000070});
  private static final Expr f000337 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000336, f000248);
  private static final Expr f000338 = Expr.makeLambda("as", f000238, f000337);
  private static final Expr f000339 = Expr.makeLambda("a", f000237, f000338);
  private static final Expr f000340 =
      Expr.makeApplication(f000000, new Expr[] {f000237, f000335, f000238, f000339});
  private static final Expr f000341 = Expr.makeLambda("x", f000327, f000340);
  private static final Expr f000342 =
      Expr.makeApplication(f000000, new Expr[] {f000327, f000328, f000238, f000341, f000301});
  private static final Expr f000343 = Expr.makeApplication(f000291, new Expr[] {f000342});
  private static final Expr f000344 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000014),
            new SimpleImmutableEntry<String, Expr>("value", f000343)
          });
  private static final Expr f000345 = Expr.makeApplication(f000010, new Expr[] {f000327});
  private static final Expr f000346 = Expr.makeLambda("keyValues", f000345, f000344);
  private static final Expr f000347 = Expr.makeFieldAccess(f000244, "string");
  private static final Expr f000348 = Expr.makeApplication(f000347, new Expr[] {f000022});
  private static final Expr f000349 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("isNull", f000014),
            new SimpleImmutableEntry<String, Expr>("value", f000348)
          });
  private static final Expr f000350 = Expr.makeLambda("x", f000226, f000349);
  private static final Expr f000351 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000319),
            new SimpleImmutableEntry<String, Expr>("bool", f000321),
            new SimpleImmutableEntry<String, Expr>("double", f000323),
            new SimpleImmutableEntry<String, Expr>("integer", f000325),
            new SimpleImmutableEntry<String, Expr>("null", f000326),
            new SimpleImmutableEntry<String, Expr>("object", f000346),
            new SimpleImmutableEntry<String, Expr>("string", f000350)
          });
  private static final Expr f000352 = Expr.makeApplication(f000308, new Expr[] {f000309, f000351});
  private static final Expr f000353 = Expr.makeFieldAccess(f000352, "value");
  private static final Expr f000354 = Expr.makeLambda("json", f000241, f000353);
  private static final Expr f000355 = Expr.makeLambda("JSON", f000019, f000354);
  private static final Expr f000356 = Expr.makeLambda("old", f000243, f000355);
  private static final Expr f000357 = Expr.makeApplication(f000010, new Expr[] {f000226});
  private static final Expr f000358 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000226),
            new SimpleImmutableEntry<String, Expr>("tail", f000357)
          });
  private static final Expr f000359 = Expr.makeLambda("x", f000358, f000022);
  private static final Expr f000360 = Expr.makeEmptyListLiteral(f000357);
  private static final Expr f000361 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000022),
            new SimpleImmutableEntry<String, Expr>("tail", f000360)
          });
  private static final Expr f000362 = Expr.makeLambda("x", f000226, f000361);
  private static final Expr f000363 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000359),
            new SimpleImmutableEntry<String, Expr>("Simple", f000362)
          });
  private static final Expr f000364 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000358),
            new SimpleImmutableEntry<String, Expr>("Simple", f000226)
          });
  private static final Expr f000365 = Expr.makeFieldAccess(f000364, "Simple");
  private static final Expr f000366 = Expr.makeTextLiteral("[]");
  private static final Expr f000367 = Expr.makeApplication(f000365, new Expr[] {f000366});
  private static final Expr f000368 = Expr.makeFieldAccess(f000364, "Complex");
  private static final Expr f000369 = Expr.makeIdentifier("inputs", 0);
  private static final Expr f000370 = Expr.makeFieldAccess(f000369, "head");
  private static final Expr f000371 = Expr.makeMerge(f000363, f000370, null);
  private static final Expr f000372 = Expr.makeFieldAccess(f000369, "tail");
  private static final Expr f000373 = Expr.makeApplication(f000010, new Expr[] {f000358});
  private static final Expr f000374 = Expr.makeMerge(f000363, f000022, null);
  private static final Expr f000375 = Expr.makeNonEmptyListLiteral(new Expr[] {f000374});
  private static final Expr f000376 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000375, f000248);
  private static final Expr f000377 = Expr.makeLambda("as", f000373, f000376);
  private static final Expr f000378 = Expr.makeLambda("x", f000364, f000377);
  private static final Expr f000379 = Expr.makeEmptyListLiteral(f000373);
  private static final Expr f000380 =
      Expr.makeApplication(f000000, new Expr[] {f000364, f000372, f000373, f000378, f000379});
  private static final Expr f000381 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000371),
            new SimpleImmutableEntry<String, Expr>("tail", f000380)
          });
  private static final Expr f000382 = Expr.makeFieldAccess(f000371, "head");
  private static final Expr f000383 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000382});
  private static final Expr f000384 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000383),
            new SimpleImmutableEntry<String, Expr>("tail", f000360)
          });
  private static final Expr f000385 = Expr.makeFieldAccess(f000022, "init");
  private static final Expr f000386 = Expr.makeFieldAccess(f000022, "last");
  private static final Expr f000387 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000386});
  private static final Expr f000388 = Expr.makeNonEmptyListLiteral(new Expr[] {f000387});
  private static final Expr f000389 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000385, f000388);
  private static final Expr f000390 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("tail", f000389)});
  private static final Expr f000391 =
      Expr.makeOperatorApplication(Operator.PREFER, f000371, f000390);
  private static final Expr f000392 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("init", f000357),
            new SimpleImmutableEntry<String, Expr>("last", f000226)
          });
  private static final Expr f000393 = Expr.makeLambda("x", f000392, f000391);
  private static final Expr f000394 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000384),
            new SimpleImmutableEntry<String, Expr>("Some", f000393)
          });
  private static final Expr f000395 = Expr.makeFieldAccess(f000371, "tail");
  private static final Expr f000396 = Expr.makeBuiltIn("Optional");
  private static final Expr f000397 = Expr.makeApplication(f000396, new Expr[] {f000392});
  private static final Expr f000398 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("init", f000360),
            new SimpleImmutableEntry<String, Expr>("last", f000022)
          });
  private static final Expr f000399 = Expr.makeApplication(f000218, new Expr[] {f000398});
  private static final Expr f000400 = Expr.makeIdentifier("ny", 0);
  private static final Expr f000401 = Expr.makeNonEmptyListLiteral(new Expr[] {f000022});
  private static final Expr f000402 = Expr.makeFieldAccess(f000400, "init");
  private static final Expr f000403 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000401, f000402);
  private static final Expr f000404 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("init", f000403)});
  private static final Expr f000405 =
      Expr.makeOperatorApplication(Operator.PREFER, f000400, f000404);
  private static final Expr f000406 = Expr.makeApplication(f000218, new Expr[] {f000405});
  private static final Expr f000407 = Expr.makeLambda("ny", f000392, f000406);
  private static final Expr f000408 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000399),
            new SimpleImmutableEntry<String, Expr>("Some", f000407)
          });
  private static final Expr f000409 = Expr.makeIdentifier("acc", 0);
  private static final Expr f000410 = Expr.makeMerge(f000408, f000409, null);
  private static final Expr f000411 = Expr.makeLambda("acc", f000397, f000410);
  private static final Expr f000412 = Expr.makeLambda("x", f000226, f000411);
  private static final Expr f000413 = Expr.makeApplication(f000220, new Expr[] {f000392});
  private static final Expr f000414 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000395, f000397, f000412, f000413});
  private static final Expr f000415 = Expr.makeMerge(f000394, f000414, null);
  private static final Expr f000416 = Expr.makeFieldAccess(f000022, "head");
  private static final Expr f000417 =
      Expr.makeTextLiteral(new String[] {"", ","}, new Expr[] {f000416});
  private static final Expr f000418 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000417),
            new SimpleImmutableEntry<String, Expr>("tail", f000360)
          });
  private static final Expr f000419 = Expr.makeIdentifier("x", 1);
  private static final Expr f000420 =
      Expr.makeOperatorApplication(Operator.PREFER, f000419, f000390);
  private static final Expr f000421 = Expr.makeLambda("x", f000392, f000420);
  private static final Expr f000422 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000418),
            new SimpleImmutableEntry<String, Expr>("Some", f000421)
          });
  private static final Expr f000423 = Expr.makeFieldAccess(f000022, "tail");
  private static final Expr f000424 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000423, f000397, f000412, f000413});
  private static final Expr f000425 = Expr.makeMerge(f000422, f000424, null);
  private static final Expr f000426 = Expr.makeNonEmptyListLiteral(new Expr[] {f000425});
  private static final Expr f000427 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000426, f000248);
  private static final Expr f000428 = Expr.makeLambda("as", f000373, f000427);
  private static final Expr f000429 = Expr.makeLambda("x", f000358, f000428);
  private static final Expr f000430 =
      Expr.makeApplication(f000000, new Expr[] {f000358, f000385, f000373, f000429, f000379});
  private static final Expr f000431 = Expr.makeNonEmptyListLiteral(new Expr[] {f000386});
  private static final Expr f000432 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000430, f000431);
  private static final Expr f000433 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000415),
            new SimpleImmutableEntry<String, Expr>("tail", f000432)
          });
  private static final Expr f000434 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("init", f000373),
            new SimpleImmutableEntry<String, Expr>("last", f000358)
          });
  private static final Expr f000435 = Expr.makeLambda("x", f000434, f000433);
  private static final Expr f000436 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000381),
            new SimpleImmutableEntry<String, Expr>("Some", f000435)
          });
  private static final Expr f000437 = Expr.makeApplication(f000396, new Expr[] {f000434});
  private static final Expr f000438 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("init", f000379),
            new SimpleImmutableEntry<String, Expr>("last", f000022)
          });
  private static final Expr f000439 = Expr.makeApplication(f000218, new Expr[] {f000438});
  private static final Expr f000440 = Expr.makeLambda("ny", f000434, f000406);
  private static final Expr f000441 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000439),
            new SimpleImmutableEntry<String, Expr>("Some", f000440)
          });
  private static final Expr f000442 = Expr.makeMerge(f000441, f000409, null);
  private static final Expr f000443 = Expr.makeLambda("acc", f000437, f000442);
  private static final Expr f000444 = Expr.makeLambda("x", f000358, f000443);
  private static final Expr f000445 = Expr.makeApplication(f000220, new Expr[] {f000434});
  private static final Expr f000446 =
      Expr.makeApplication(f000000, new Expr[] {f000358, f000380, f000437, f000444, f000445});
  private static final Expr f000447 = Expr.makeMerge(f000436, f000446, null);
  private static final Expr f000448 = Expr.makeFieldAccess(f000447, "head");
  private static final Expr f000449 = Expr.makeFieldAccess(f000448, "head");
  private static final Expr f000450 =
      Expr.makeTextLiteral(new String[] {"[ ", " ]"}, new Expr[] {f000449});
  private static final Expr f000451 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000450),
            new SimpleImmutableEntry<String, Expr>("tail", f000360)
          });
  private static final Expr f000452 = Expr.makeTextLiteral("[");
  private static final Expr f000453 = Expr.makeNonEmptyListLiteral(new Expr[] {f000449});
  private static final Expr f000454 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000453, f000402);
  private static final Expr f000455 = Expr.makeFieldAccess(f000400, "last");
  private static final Expr f000456 = Expr.makeNonEmptyListLiteral(new Expr[] {f000455});
  private static final Expr f000457 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000454, f000456);
  private static final Expr f000458 =
      Expr.makeTextLiteral(new String[] {"  ", ""}, new Expr[] {f000022});
  private static final Expr f000459 = Expr.makeNonEmptyListLiteral(new Expr[] {f000458});
  private static final Expr f000460 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000459, f000248);
  private static final Expr f000461 = Expr.makeLambda("as", f000357, f000460);
  private static final Expr f000462 = Expr.makeLambda("x", f000226, f000461);
  private static final Expr f000463 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000457, f000357, f000462, f000360});
  private static final Expr f000464 = Expr.makeTextLiteral("]");
  private static final Expr f000465 = Expr.makeNonEmptyListLiteral(new Expr[] {f000464});
  private static final Expr f000466 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000463, f000465);
  private static final Expr f000467 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000452),
            new SimpleImmutableEntry<String, Expr>("tail", f000466)
          });
  private static final Expr f000468 = Expr.makeLambda("ny", f000392, f000467);
  private static final Expr f000469 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000451),
            new SimpleImmutableEntry<String, Expr>("Some", f000468)
          });
  private static final Expr f000470 = Expr.makeFieldAccess(f000448, "tail");
  private static final Expr f000471 = Expr.makeFieldAccess(f000447, "tail");
  private static final Expr f000472 = Expr.makeNonEmptyListLiteral(new Expr[] {f000416});
  private static final Expr f000473 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000472, f000423);
  private static final Expr f000474 = Expr.makeLambda("as", f000357, f000337);
  private static final Expr f000475 = Expr.makeLambda("a", f000226, f000474);
  private static final Expr f000476 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000473, f000357, f000475});
  private static final Expr f000477 = Expr.makeLambda("x", f000358, f000476);
  private static final Expr f000478 =
      Expr.makeApplication(f000000, new Expr[] {f000358, f000471, f000357, f000477, f000360});
  private static final Expr f000479 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000470, f000478);
  private static final Expr f000480 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000479, f000397, f000412, f000413});
  private static final Expr f000481 = Expr.makeMerge(f000469, f000480, null);
  private static final Expr f000482 = Expr.makeApplication(f000368, new Expr[] {f000481});
  private static final Expr f000483 = Expr.makeApplication(f000010, new Expr[] {f000364});
  private static final Expr f000484 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000364),
            new SimpleImmutableEntry<String, Expr>("tail", f000483)
          });
  private static final Expr f000485 = Expr.makeLambda("inputs", f000484, f000482);
  private static final Expr f000486 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000367),
            new SimpleImmutableEntry<String, Expr>("Some", f000485)
          });
  private static final Expr f000487 = Expr.makeBuiltIn("List/reverse");
  private static final Expr f000488 = Expr.makeApplication(f000487, new Expr[] {f000364, f000369});
  private static final Expr f000489 = Expr.makeApplication(f000396, new Expr[] {f000484});
  private static final Expr f000490 = Expr.makeEmptyListLiteral(f000483);
  private static final Expr f000491 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000022),
            new SimpleImmutableEntry<String, Expr>("tail", f000490)
          });
  private static final Expr f000492 = Expr.makeApplication(f000218, new Expr[] {f000491});
  private static final Expr f000493 = Expr.makeIdentifier("ne", 0);
  private static final Expr f000494 = Expr.makeFieldAccess(f000493, "tail");
  private static final Expr f000495 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000494, f000401);
  private static final Expr f000496 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("tail", f000495)});
  private static final Expr f000497 =
      Expr.makeOperatorApplication(Operator.PREFER, f000493, f000496);
  private static final Expr f000498 = Expr.makeApplication(f000218, new Expr[] {f000497});
  private static final Expr f000499 = Expr.makeLambda("ne", f000484, f000498);
  private static final Expr f000500 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000492),
            new SimpleImmutableEntry<String, Expr>("Some", f000499)
          });
  private static final Expr f000501 = Expr.makeMerge(f000500, f000409, null);
  private static final Expr f000502 = Expr.makeLambda("acc", f000489, f000501);
  private static final Expr f000503 = Expr.makeLambda("x", f000364, f000502);
  private static final Expr f000504 = Expr.makeApplication(f000220, new Expr[] {f000484});
  private static final Expr f000505 =
      Expr.makeApplication(f000000, new Expr[] {f000364, f000488, f000489, f000503, f000504});
  private static final Expr f000506 = Expr.makeMerge(f000486, f000505, null);
  private static final Expr f000507 = Expr.makeLambda("inputs", f000483, f000506);
  private static final Expr f000508 = Expr.makeTextLiteral("true");
  private static final Expr f000509 = Expr.makeTextLiteral("false");
  private static final Expr f000510 = Expr.makeIf(f000022, f000508, f000509);
  private static final Expr f000511 = Expr.makeApplication(f000365, new Expr[] {f000510});
  private static final Expr f000512 = Expr.makeLambda("x", f000001, f000511);
  private static final Expr f000513 = Expr.makeApplication(f000054, new Expr[] {f000022});
  private static final Expr f000514 = Expr.makeApplication(f000365, new Expr[] {f000513});
  private static final Expr f000515 = Expr.makeLambda("x", f000234, f000514);
  private static final Expr f000516 = Expr.makeBuiltIn("Natural/show");
  private static final Expr f000517 = Expr.makeApplication(f000516, new Expr[] {f000136});
  private static final Expr f000518 = Expr.makeApplication(f000192, new Expr[] {f000022});
  private static final Expr f000519 = Expr.makeIf(f000154, f000517, f000518);
  private static final Expr f000520 = Expr.makeApplication(f000365, new Expr[] {f000519});
  private static final Expr f000521 = Expr.makeLambda("x", f000083, f000520);
  private static final Expr f000522 = Expr.makeTextLiteral("null");
  private static final Expr f000523 = Expr.makeApplication(f000365, new Expr[] {f000522});
  private static final Expr f000524 = Expr.makeTextLiteral("{}");
  private static final Expr f000525 = Expr.makeApplication(f000365, new Expr[] {f000524});
  private static final Expr f000526 = Expr.makeFieldAccess(f000370, "mapValue");
  private static final Expr f000527 = Expr.makeMerge(f000363, f000526, null);
  private static final Expr f000528 = Expr.makeBuiltIn("Text/replace");
  private static final Expr f000529 = Expr.makeTextLiteral("\"");
  private static final Expr f000530 = Expr.makeTextLiteral("\\\\\"");
  private static final Expr f000531 = Expr.makeTextLiteral("\b");
  private static final Expr f000532 = Expr.makeTextLiteral("\\\\b");
  private static final Expr f000533 = Expr.makeTextLiteral("\f");
  private static final Expr f000534 = Expr.makeTextLiteral("\\\\f");
  private static final Expr f000535 = Expr.makeTextLiteral("\n");
  private static final Expr f000536 = Expr.makeTextLiteral("\\\\n");
  private static final Expr f000537 = Expr.makeTextLiteral("\r");
  private static final Expr f000538 = Expr.makeTextLiteral("\\\\r");
  private static final Expr f000539 = Expr.makeTextLiteral("\t");
  private static final Expr f000540 = Expr.makeTextLiteral("\\\\t");
  private static final Expr f000541 = Expr.makeTextLiteral("\\\\");
  private static final Expr f000542 = Expr.makeTextLiteral("\\\\\\\\");
  private static final Expr f000543 = Expr.makeFieldAccess(f000370, "mapKey");
  private static final Expr f000544 =
      Expr.makeApplication(f000528, new Expr[] {f000541, f000542, f000543});
  private static final Expr f000545 =
      Expr.makeApplication(f000528, new Expr[] {f000539, f000540, f000544});
  private static final Expr f000546 =
      Expr.makeApplication(f000528, new Expr[] {f000537, f000538, f000545});
  private static final Expr f000547 =
      Expr.makeApplication(f000528, new Expr[] {f000535, f000536, f000546});
  private static final Expr f000548 =
      Expr.makeApplication(f000528, new Expr[] {f000533, f000534, f000547});
  private static final Expr f000549 =
      Expr.makeApplication(f000528, new Expr[] {f000531, f000532, f000548});
  private static final Expr f000550 =
      Expr.makeApplication(f000528, new Expr[] {f000529, f000530, f000549});
  private static final Expr f000551 = Expr.makeFieldAccess(f000527, "head");
  private static final Expr f000552 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000550, f000551});
  private static final Expr f000553 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000552)});
  private static final Expr f000554 =
      Expr.makeOperatorApplication(Operator.PREFER, f000527, f000553);
  private static final Expr f000555 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000226),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000364)
          });
  private static final Expr f000556 = Expr.makeMerge(f000363, f000294, null);
  private static final Expr f000557 =
      Expr.makeApplication(f000528, new Expr[] {f000541, f000542, f000293});
  private static final Expr f000558 =
      Expr.makeApplication(f000528, new Expr[] {f000539, f000540, f000557});
  private static final Expr f000559 =
      Expr.makeApplication(f000528, new Expr[] {f000537, f000538, f000558});
  private static final Expr f000560 =
      Expr.makeApplication(f000528, new Expr[] {f000535, f000536, f000559});
  private static final Expr f000561 =
      Expr.makeApplication(f000528, new Expr[] {f000533, f000534, f000560});
  private static final Expr f000562 =
      Expr.makeApplication(f000528, new Expr[] {f000531, f000532, f000561});
  private static final Expr f000563 =
      Expr.makeApplication(f000528, new Expr[] {f000529, f000530, f000562});
  private static final Expr f000564 = Expr.makeFieldAccess(f000556, "head");
  private static final Expr f000565 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000563, f000564});
  private static final Expr f000566 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000565)});
  private static final Expr f000567 =
      Expr.makeOperatorApplication(Operator.PREFER, f000556, f000566);
  private static final Expr f000568 = Expr.makeNonEmptyListLiteral(new Expr[] {f000567});
  private static final Expr f000569 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000568, f000248);
  private static final Expr f000570 = Expr.makeLambda("as", f000373, f000569);
  private static final Expr f000571 = Expr.makeLambda("x", f000555, f000570);
  private static final Expr f000572 =
      Expr.makeApplication(f000000, new Expr[] {f000555, f000372, f000373, f000571, f000379});
  private static final Expr f000573 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000554),
            new SimpleImmutableEntry<String, Expr>("tail", f000572)
          });
  private static final Expr f000574 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ","}, new Expr[] {f000550, f000551});
  private static final Expr f000575 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000574),
            new SimpleImmutableEntry<String, Expr>("tail", f000360)
          });
  private static final Expr f000576 =
      Expr.makeOperatorApplication(Operator.PREFER, f000554, f000390);
  private static final Expr f000577 = Expr.makeLambda("x", f000392, f000576);
  private static final Expr f000578 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000575),
            new SimpleImmutableEntry<String, Expr>("Some", f000577)
          });
  private static final Expr f000579 = Expr.makeFieldAccess(f000527, "tail");
  private static final Expr f000580 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000579, f000397, f000412, f000413});
  private static final Expr f000581 = Expr.makeMerge(f000578, f000580, null);
  private static final Expr f000582 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000581),
            new SimpleImmutableEntry<String, Expr>("tail", f000432)
          });
  private static final Expr f000583 = Expr.makeLambda("x", f000434, f000582);
  private static final Expr f000584 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000573),
            new SimpleImmutableEntry<String, Expr>("Some", f000583)
          });
  private static final Expr f000585 =
      Expr.makeApplication(f000000, new Expr[] {f000358, f000572, f000437, f000444, f000445});
  private static final Expr f000586 = Expr.makeMerge(f000584, f000585, null);
  private static final Expr f000587 = Expr.makeFieldAccess(f000586, "head");
  private static final Expr f000588 = Expr.makeFieldAccess(f000587, "head");
  private static final Expr f000589 =
      Expr.makeTextLiteral(new String[] {"{ ", " }"}, new Expr[] {f000588});
  private static final Expr f000590 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000589),
            new SimpleImmutableEntry<String, Expr>("tail", f000360)
          });
  private static final Expr f000591 = Expr.makeTextLiteral("{");
  private static final Expr f000592 = Expr.makeNonEmptyListLiteral(new Expr[] {f000588});
  private static final Expr f000593 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000592, f000402);
  private static final Expr f000594 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000593, f000456);
  private static final Expr f000595 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000594, f000357, f000462, f000360});
  private static final Expr f000596 = Expr.makeTextLiteral("}");
  private static final Expr f000597 = Expr.makeNonEmptyListLiteral(new Expr[] {f000596});
  private static final Expr f000598 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000595, f000597);
  private static final Expr f000599 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000591),
            new SimpleImmutableEntry<String, Expr>("tail", f000598)
          });
  private static final Expr f000600 = Expr.makeLambda("ny", f000392, f000599);
  private static final Expr f000601 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000590),
            new SimpleImmutableEntry<String, Expr>("Some", f000600)
          });
  private static final Expr f000602 = Expr.makeFieldAccess(f000587, "tail");
  private static final Expr f000603 = Expr.makeFieldAccess(f000586, "tail");
  private static final Expr f000604 =
      Expr.makeApplication(f000000, new Expr[] {f000358, f000603, f000357, f000477, f000360});
  private static final Expr f000605 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000602, f000604);
  private static final Expr f000606 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000605, f000397, f000412, f000413});
  private static final Expr f000607 = Expr.makeMerge(f000601, f000606, null);
  private static final Expr f000608 = Expr.makeApplication(f000368, new Expr[] {f000607});
  private static final Expr f000609 = Expr.makeApplication(f000010, new Expr[] {f000555});
  private static final Expr f000610 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000555),
            new SimpleImmutableEntry<String, Expr>("tail", f000609)
          });
  private static final Expr f000611 = Expr.makeLambda("inputs", f000610, f000608);
  private static final Expr f000612 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000525),
            new SimpleImmutableEntry<String, Expr>("Some", f000611)
          });
  private static final Expr f000613 = Expr.makeApplication(f000487, new Expr[] {f000555, f000369});
  private static final Expr f000614 = Expr.makeApplication(f000396, new Expr[] {f000610});
  private static final Expr f000615 = Expr.makeEmptyListLiteral(f000609);
  private static final Expr f000616 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000022),
            new SimpleImmutableEntry<String, Expr>("tail", f000615)
          });
  private static final Expr f000617 = Expr.makeApplication(f000218, new Expr[] {f000616});
  private static final Expr f000618 = Expr.makeLambda("ne", f000610, f000498);
  private static final Expr f000619 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000617),
            new SimpleImmutableEntry<String, Expr>("Some", f000618)
          });
  private static final Expr f000620 = Expr.makeMerge(f000619, f000409, null);
  private static final Expr f000621 = Expr.makeLambda("acc", f000614, f000620);
  private static final Expr f000622 = Expr.makeLambda("x", f000555, f000621);
  private static final Expr f000623 = Expr.makeApplication(f000220, new Expr[] {f000610});
  private static final Expr f000624 =
      Expr.makeApplication(f000000, new Expr[] {f000555, f000613, f000614, f000622, f000623});
  private static final Expr f000625 = Expr.makeMerge(f000612, f000624, null);
  private static final Expr f000626 = Expr.makeLambda("inputs", f000609, f000625);
  private static final Expr f000627 =
      Expr.makeApplication(f000528, new Expr[] {f000541, f000542, f000022});
  private static final Expr f000628 =
      Expr.makeApplication(f000528, new Expr[] {f000539, f000540, f000627});
  private static final Expr f000629 =
      Expr.makeApplication(f000528, new Expr[] {f000537, f000538, f000628});
  private static final Expr f000630 =
      Expr.makeApplication(f000528, new Expr[] {f000535, f000536, f000629});
  private static final Expr f000631 =
      Expr.makeApplication(f000528, new Expr[] {f000533, f000534, f000630});
  private static final Expr f000632 =
      Expr.makeApplication(f000528, new Expr[] {f000531, f000532, f000631});
  private static final Expr f000633 =
      Expr.makeApplication(f000528, new Expr[] {f000529, f000530, f000632});
  private static final Expr f000634 =
      Expr.makeTextLiteral(new String[] {"\"", "\""}, new Expr[] {f000633});
  private static final Expr f000635 = Expr.makeApplication(f000365, new Expr[] {f000634});
  private static final Expr f000636 = Expr.makeLambda("x", f000226, f000635);
  private static final Expr f000637 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000507),
            new SimpleImmutableEntry<String, Expr>("bool", f000512),
            new SimpleImmutableEntry<String, Expr>("double", f000515),
            new SimpleImmutableEntry<String, Expr>("integer", f000521),
            new SimpleImmutableEntry<String, Expr>("null", f000523),
            new SimpleImmutableEntry<String, Expr>("object", f000626),
            new SimpleImmutableEntry<String, Expr>("string", f000636)
          });
  private static final Expr f000638 = Expr.makeApplication(f000244, new Expr[] {f000364, f000637});
  private static final Expr f000639 = Expr.makeMerge(f000363, f000638, null);
  private static final Expr f000640 = Expr.makeFieldAccess(f000639, "head");
  private static final Expr f000641 = Expr.makeNonEmptyListLiteral(new Expr[] {f000640});
  private static final Expr f000642 = Expr.makeFieldAccess(f000639, "tail");
  private static final Expr f000643 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000641, f000642);
  private static final Expr f000644 =
      Expr.makeTextLiteral(new String[] {"", "\n", ""}, new Expr[] {f000022, f000023});
  private static final Expr f000645 = Expr.makeLambda("y", f000226, f000644);
  private static final Expr f000646 = Expr.makeLambda("x", f000226, f000645);
  private static final Expr f000647 = Expr.makeTextLiteral("");
  private static final Expr f000648 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000643, f000226, f000646, f000647});
  private static final Expr f000649 = Expr.makeLambda("json", f000243, f000648);
  private static final Expr f000650 = Expr.makeIdentifier("j", 0);
  private static final Expr f000651 = Expr.makeIdentifier("result", 0);
  private static final Expr f000652 = Expr.makeLambda("result", f000226, f000651);
  private static final Expr f000653 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f000647),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000652)
          });
  private static final Expr f000654 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", null),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000226)
          });
  private static final Expr f000655 = Expr.makeFieldAccess(f000654, "NonEmpty");
  private static final Expr f000656 =
      Expr.makeTextLiteral(new String[] {" ", ""}, new Expr[] {f000022});
  private static final Expr f000657 = Expr.makeApplication(f000655, new Expr[] {f000656});
  private static final Expr f000658 =
      Expr.makeTextLiteral(new String[] {" ", ",", ""}, new Expr[] {f000022, f000651});
  private static final Expr f000659 = Expr.makeApplication(f000655, new Expr[] {f000658});
  private static final Expr f000660 = Expr.makeLambda("result", f000226, f000659);
  private static final Expr f000661 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f000657),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000660)
          });
  private static final Expr f000662 = Expr.makeIdentifier("status", 0);
  private static final Expr f000663 = Expr.makeMerge(f000661, f000662, null);
  private static final Expr f000664 = Expr.makeLambda("status", f000654, f000663);
  private static final Expr f000665 = Expr.makeLambda("x", f000226, f000664);
  private static final Expr f000666 = Expr.makeFieldAccess(f000654, "Empty");
  private static final Expr f000667 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000022, f000654, f000665, f000666});
  private static final Expr f000668 = Expr.makeMerge(f000653, f000667, null);
  private static final Expr f000669 =
      Expr.makeTextLiteral(new String[] {"[", " ]"}, new Expr[] {f000668});
  private static final Expr f000670 = Expr.makeLambda("x", f000357, f000669);
  private static final Expr f000671 = Expr.makeLambda("x", f000001, f000510);
  private static final Expr f000672 = Expr.makeIdentifier("integer", 0);
  private static final Expr f000673 = Expr.makeApplication(f000079, new Expr[] {f000672});
  private static final Expr f000674 = Expr.makeApplication(f000075, new Expr[] {f000673});
  private static final Expr f000675 = Expr.makeApplication(f000074, new Expr[] {f000674});
  private static final Expr f000676 = Expr.makeApplication(f000075, new Expr[] {f000672});
  private static final Expr f000677 = Expr.makeApplication(f000516, new Expr[] {f000676});
  private static final Expr f000678 = Expr.makeApplication(f000192, new Expr[] {f000672});
  private static final Expr f000679 = Expr.makeIf(f000675, f000677, f000678);
  private static final Expr f000680 = Expr.makeLambda("integer", f000083, f000679);
  private static final Expr f000681 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000226),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000226)
          });
  private static final Expr f000682 = Expr.makeBuiltIn("Text/show");
  private static final Expr f000683 = Expr.makeApplication(f000682, new Expr[] {f000293});
  private static final Expr f000684 =
      Expr.makeTextLiteral(new String[] {" ", ": ", ""}, new Expr[] {f000683, f000294});
  private static final Expr f000685 = Expr.makeApplication(f000655, new Expr[] {f000684});
  private static final Expr f000686 =
      Expr.makeTextLiteral(
          new String[] {" ", ": ", ",", ""}, new Expr[] {f000683, f000294, f000651});
  private static final Expr f000687 = Expr.makeApplication(f000655, new Expr[] {f000686});
  private static final Expr f000688 = Expr.makeLambda("result", f000226, f000687);
  private static final Expr f000689 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f000685),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f000688)
          });
  private static final Expr f000690 = Expr.makeMerge(f000689, f000662, null);
  private static final Expr f000691 = Expr.makeLambda("status", f000654, f000690);
  private static final Expr f000692 = Expr.makeLambda("x", f000681, f000691);
  private static final Expr f000693 =
      Expr.makeApplication(f000000, new Expr[] {f000681, f000022, f000654, f000692, f000666});
  private static final Expr f000694 = Expr.makeMerge(f000653, f000693, null);
  private static final Expr f000695 =
      Expr.makeTextLiteral(new String[] {"{", " }"}, new Expr[] {f000694});
  private static final Expr f000696 = Expr.makeApplication(f000010, new Expr[] {f000681});
  private static final Expr f000697 = Expr.makeLambda("x", f000696, f000695);
  private static final Expr f000698 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000670),
            new SimpleImmutableEntry<String, Expr>("bool", f000671),
            new SimpleImmutableEntry<String, Expr>("double", f000054),
            new SimpleImmutableEntry<String, Expr>("integer", f000680),
            new SimpleImmutableEntry<String, Expr>("null", f000522),
            new SimpleImmutableEntry<String, Expr>("object", f000697),
            new SimpleImmutableEntry<String, Expr>("string", f000682)
          });
  private static final Expr f000699 = Expr.makeApplication(f000650, new Expr[] {f000226, f000698});
  private static final Expr f000700 = Expr.makeLambda("j", f000243, f000699);
  private static final Expr f000701 =
      Expr.makeTextLiteral(new String[] {"- ", ""}, new Expr[] {f000382});
  private static final Expr f000702 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000395, f000357, f000462, f000360});
  private static final Expr f000703 = Expr.makeFieldAccess(f000374, "tail");
  private static final Expr f000704 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000703, f000357, f000462, f000360});
  private static final Expr f000705 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("tail", f000704)});
  private static final Expr f000706 =
      Expr.makeOperatorApplication(Operator.PREFER, f000374, f000705);
  private static final Expr f000707 = Expr.makeFieldAccess(f000374, "head");
  private static final Expr f000708 =
      Expr.makeTextLiteral(new String[] {"- ", ""}, new Expr[] {f000707});
  private static final Expr f000709 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("head", f000708)});
  private static final Expr f000710 =
      Expr.makeOperatorApplication(Operator.PREFER, f000706, f000709);
  private static final Expr f000711 = Expr.makeNonEmptyListLiteral(new Expr[] {f000710});
  private static final Expr f000712 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000711, f000248);
  private static final Expr f000713 = Expr.makeLambda("as", f000373, f000712);
  private static final Expr f000714 = Expr.makeLambda("x", f000364, f000713);
  private static final Expr f000715 =
      Expr.makeApplication(f000000, new Expr[] {f000364, f000372, f000373, f000714, f000379});
  private static final Expr f000716 =
      Expr.makeApplication(f000000, new Expr[] {f000358, f000715, f000357, f000477, f000360});
  private static final Expr f000717 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000702, f000716);
  private static final Expr f000718 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000701),
            new SimpleImmutableEntry<String, Expr>("tail", f000717)
          });
  private static final Expr f000719 = Expr.makeApplication(f000368, new Expr[] {f000718});
  private static final Expr f000720 = Expr.makeLambda("inputs", f000484, f000719);
  private static final Expr f000721 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000367),
            new SimpleImmutableEntry<String, Expr>("Some", f000720)
          });
  private static final Expr f000722 = Expr.makeMerge(f000721, f000505, null);
  private static final Expr f000723 = Expr.makeLambda("inputs", f000483, f000722);
  private static final Expr f000724 =
      Expr.makeTextLiteral(new String[] {"\"", "\":"}, new Expr[] {f000550});
  private static final Expr f000725 = Expr.makeNonEmptyListLiteral(new Expr[] {f000551});
  private static final Expr f000726 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000725, f000579);
  private static final Expr f000727 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000726, f000357, f000462, f000360});
  private static final Expr f000728 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000724),
            new SimpleImmutableEntry<String, Expr>("tail", f000727)
          });
  private static final Expr f000729 = Expr.makeLambda("_", f000358, f000728);
  private static final Expr f000730 = Expr.makeIdentifier("line", 0);
  private static final Expr f000731 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000550, f000730});
  private static final Expr f000732 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000731),
            new SimpleImmutableEntry<String, Expr>("tail", f000360)
          });
  private static final Expr f000733 = Expr.makeLambda("line", f000226, f000732);
  private static final Expr f000734 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000729),
            new SimpleImmutableEntry<String, Expr>("Simple", f000733)
          });
  private static final Expr f000735 = Expr.makeMerge(f000734, f000526, null);
  private static final Expr f000736 = Expr.makeFieldAccess(f000735, "head");
  private static final Expr f000737 = Expr.makeFieldAccess(f000735, "tail");
  private static final Expr f000738 =
      Expr.makeTextLiteral(new String[] {"\"", "\":"}, new Expr[] {f000563});
  private static final Expr f000739 = Expr.makeNonEmptyListLiteral(new Expr[] {f000564});
  private static final Expr f000740 = Expr.makeFieldAccess(f000556, "tail");
  private static final Expr f000741 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000739, f000740);
  private static final Expr f000742 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000741, f000357, f000462, f000360});
  private static final Expr f000743 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000738),
            new SimpleImmutableEntry<String, Expr>("tail", f000742)
          });
  private static final Expr f000744 = Expr.makeLambda("_", f000358, f000743);
  private static final Expr f000745 =
      Expr.makeTextLiteral(new String[] {"\"", "\": ", ""}, new Expr[] {f000563, f000730});
  private static final Expr f000746 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000745),
            new SimpleImmutableEntry<String, Expr>("tail", f000360)
          });
  private static final Expr f000747 = Expr.makeLambda("line", f000226, f000746);
  private static final Expr f000748 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Complex", f000744),
            new SimpleImmutableEntry<String, Expr>("Simple", f000747)
          });
  private static final Expr f000749 = Expr.makeMerge(f000748, f000294, null);
  private static final Expr f000750 = Expr.makeNonEmptyListLiteral(new Expr[] {f000749});
  private static final Expr f000751 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000750, f000248);
  private static final Expr f000752 = Expr.makeLambda("as", f000373, f000751);
  private static final Expr f000753 = Expr.makeLambda("x", f000555, f000752);
  private static final Expr f000754 =
      Expr.makeApplication(f000000, new Expr[] {f000555, f000372, f000373, f000753, f000379});
  private static final Expr f000755 =
      Expr.makeApplication(f000000, new Expr[] {f000358, f000754, f000357, f000477, f000360});
  private static final Expr f000756 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000737, f000755);
  private static final Expr f000757 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000736),
            new SimpleImmutableEntry<String, Expr>("tail", f000756)
          });
  private static final Expr f000758 = Expr.makeApplication(f000368, new Expr[] {f000757});
  private static final Expr f000759 = Expr.makeLambda("inputs", f000610, f000758);
  private static final Expr f000760 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000525),
            new SimpleImmutableEntry<String, Expr>("Some", f000759)
          });
  private static final Expr f000761 = Expr.makeMerge(f000760, f000624, null);
  private static final Expr f000762 = Expr.makeLambda("inputs", f000609, f000761);
  private static final Expr f000763 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("array", f000723),
            new SimpleImmutableEntry<String, Expr>("bool", f000512),
            new SimpleImmutableEntry<String, Expr>("double", f000515),
            new SimpleImmutableEntry<String, Expr>("integer", f000521),
            new SimpleImmutableEntry<String, Expr>("null", f000523),
            new SimpleImmutableEntry<String, Expr>("object", f000762),
            new SimpleImmutableEntry<String, Expr>("string", f000636)
          });
  private static final Expr f000764 = Expr.makeApplication(f000244, new Expr[] {f000364, f000763});
  private static final Expr f000765 = Expr.makeMerge(f000363, f000764, null);
  private static final Expr f000766 = Expr.makeFieldAccess(f000765, "head");
  private static final Expr f000767 = Expr.makeNonEmptyListLiteral(new Expr[] {f000766});
  private static final Expr f000768 = Expr.makeFieldAccess(f000765, "tail");
  private static final Expr f000769 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000767, f000768);
  private static final Expr f000770 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000769, f000226, f000646, f000647});
  private static final Expr f000771 = Expr.makeLambda("json", f000243, f000770);
  private static final Expr f000772 = Expr.makeLambda("json", f000241, f000348);
  private static final Expr f000773 = Expr.makeLambda("JSON", f000019, f000772);
  private static final Expr f000774 = Expr.makeLambda("x", f000226, f000773);
  private static final Expr f000775 = Expr.makeIdentifier("contents", 0);
  private static final Expr f000776 = Expr.makeIdentifier("tagFieldName", 0);
  private static final Expr f000777 = Expr.makeFieldAccess(f000227, "Inline");
  private static final Expr f000778 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("contents", f000775),
            new SimpleImmutableEntry<String, Expr>("field", f000776),
            new SimpleImmutableEntry<String, Expr>("nesting", f000777)
          });
  private static final Expr f000779 = Expr.makeLambda("contents", f000070, f000778);
  private static final Expr f000780 = Expr.makeLambda("a", f000019, f000779);
  private static final Expr f000781 = Expr.makeLambda("tagFieldName", f000226, f000780);
  private static final Expr f000782 = Expr.makeFieldAccess(f000227, "Nested");
  private static final Expr f000783 = Expr.makeIdentifier("contentsFieldName", 0);
  private static final Expr f000784 = Expr.makeApplication(f000782, new Expr[] {f000783});
  private static final Expr f000785 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("contents", f000775),
            new SimpleImmutableEntry<String, Expr>("field", f000776),
            new SimpleImmutableEntry<String, Expr>("nesting", f000784)
          });
  private static final Expr f000786 = Expr.makeLambda("contents", f000070, f000785);
  private static final Expr f000787 = Expr.makeLambda("a", f000019, f000786);
  private static final Expr f000788 = Expr.makeLambda("tagFieldName", f000226, f000787);
  private static final Expr f000789 = Expr.makeLambda("contentsFieldName", f000226, f000788);
  private static final Expr f000790 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Nesting", f000227),
            new SimpleImmutableEntry<String, Expr>("Tagged", f000229),
            new SimpleImmutableEntry<String, Expr>("Type", f000243),
            new SimpleImmutableEntry<String, Expr>("array", f000258),
            new SimpleImmutableEntry<String, Expr>("bool", f000263),
            new SimpleImmutableEntry<String, Expr>("double", f000268),
            new SimpleImmutableEntry<String, Expr>("integer", f000273),
            new SimpleImmutableEntry<String, Expr>("keyText", f000278),
            new SimpleImmutableEntry<String, Expr>("keyValue", f000282),
            new SimpleImmutableEntry<String, Expr>("natural", f000287),
            new SimpleImmutableEntry<String, Expr>("null", f000290),
            new SimpleImmutableEntry<String, Expr>("number", f000268),
            new SimpleImmutableEntry<String, Expr>("object", f000307),
            new SimpleImmutableEntry<String, Expr>("omitNullFields", f000356),
            new SimpleImmutableEntry<String, Expr>("render", f000649),
            new SimpleImmutableEntry<String, Expr>("renderCompact", f000700),
            new SimpleImmutableEntry<String, Expr>("renderInteger", f000680),
            new SimpleImmutableEntry<String, Expr>("renderYAML", f000771),
            new SimpleImmutableEntry<String, Expr>("string", f000774),
            new SimpleImmutableEntry<String, Expr>("tagInline", f000781),
            new SimpleImmutableEntry<String, Expr>("tagNested", f000789)
          });
  private static final Expr f000791 = Expr.makeOperatorApplication(Operator.AND, f000057, f000004);
  private static final Expr f000792 = Expr.makeLambda("r", f000001, f000791);
  private static final Expr f000793 = Expr.makeLambda("x", f000070, f000792);
  private static final Expr f000794 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f000002, f000001, f000793, f000008});
  private static final Expr f000795 = Expr.makeApplication(f000010, new Expr[] {f000070});
  private static final Expr f000796 = Expr.makeLambda("xs", f000795, f000794);
  private static final Expr f000797 = Expr.makePi("_", f000070, f000001);
  private static final Expr f000798 = Expr.makeLambda("f", f000797, f000796);
  private static final Expr f000799 = Expr.makeLambda("a", f000019, f000798);
  private static final Expr f000800 = Expr.makeOperatorApplication(Operator.OR, f000057, f000004);
  private static final Expr f000801 = Expr.makeLambda("r", f000001, f000800);
  private static final Expr f000802 = Expr.makeLambda("x", f000070, f000801);
  private static final Expr f000803 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f000002, f000001, f000802, f000014});
  private static final Expr f000804 = Expr.makeLambda("xs", f000795, f000803);
  private static final Expr f000805 = Expr.makeLambda("f", f000797, f000804);
  private static final Expr f000806 = Expr.makeLambda("a", f000019, f000805);
  private static final Expr f000807 = Expr.makeBuiltIn("List/build");
  private static final Expr f000808 = Expr.makeIdentifier("xss", 0);
  private static final Expr f000809 = Expr.makeIdentifier("a", 1);
  private static final Expr f000810 = Expr.makeApplication(f000010, new Expr[] {f000809});
  private static final Expr f000811 = Expr.makeLambda("as", f000810, f000337);
  private static final Expr f000812 = Expr.makeLambda("a", f000070, f000811);
  private static final Expr f000813 = Expr.makeIdentifier("ys", 0);
  private static final Expr f000814 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f000002, f000795, f000812, f000813});
  private static final Expr f000815 = Expr.makeLambda("ys", f000795, f000814);
  private static final Expr f000816 = Expr.makeLambda("xs", f000795, f000815);
  private static final Expr f000817 = Expr.makeEmptyListLiteral(f000795);
  private static final Expr f000818 =
      Expr.makeApplication(f000000, new Expr[] {f000795, f000808, f000795, f000816, f000817});
  private static final Expr f000819 = Expr.makeApplication(f000010, new Expr[] {f000795});
  private static final Expr f000820 = Expr.makeLambda("xss", f000819, f000818);
  private static final Expr f000821 = Expr.makeLambda("a", f000019, f000820);
  private static final Expr f000822 = Expr.makeApplication(f000010, new Expr[] {f000029});
  private static final Expr f000823 = Expr.makeLambda("as", f000822, f000337);
  private static final Expr f000824 = Expr.makeLambda("a", f000029, f000823);
  private static final Expr f000825 =
      Expr.makeApplication(f000000, new Expr[] {f000029, f000057, f000822, f000824});
  private static final Expr f000826 = Expr.makeLambda("x", f000070, f000825);
  private static final Expr f000827 = Expr.makeEmptyListLiteral(f000822);
  private static final Expr f000828 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f000002, f000822, f000826, f000827});
  private static final Expr f000829 = Expr.makeLambda("xs", f000795, f000828);
  private static final Expr f000830 = Expr.makePi("_", f000070, f000822);
  private static final Expr f000831 = Expr.makeLambda("f", f000830, f000829);
  private static final Expr f000832 = Expr.makeLambda("b", f000019, f000831);
  private static final Expr f000833 = Expr.makeLambda("a", f000019, f000832);
  private static final Expr f000834 = Expr.makeLambda("l", f000795, f000003);
  private static final Expr f000835 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000817),
            new SimpleImmutableEntry<String, Expr>("Some", f000834)
          });
  private static final Expr f000836 = Expr.makeIdentifier("o", 0);
  private static final Expr f000837 = Expr.makeMerge(f000835, f000836, null);
  private static final Expr f000838 = Expr.makeApplication(f000396, new Expr[] {f000795});
  private static final Expr f000839 = Expr.makeLambda("o", f000838, f000837);
  private static final Expr f000840 = Expr.makeLambda("a", f000019, f000839);
  private static final Expr f000841 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000221),
            new SimpleImmutableEntry<String, Expr>("value", f000070)
          });
  private static final Expr f000842 = Expr.makeBuiltIn("List/indexed");
  private static final Expr f000843 = Expr.makeApplication(f000842, new Expr[] {f000070, f000002});
  private static final Expr f000844 = Expr.makeFieldAccess(f000022, "index");
  private static final Expr f000845 = Expr.makeApplication(f000089, new Expr[] {f000844, f000076});
  private static final Expr f000846 = Expr.makeApplication(f000074, new Expr[] {f000845});
  private static final Expr f000847 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000311, f000002);
  private static final Expr f000848 = Expr.makeIf(f000846, f000847, f000002);
  private static final Expr f000849 = Expr.makeLambda("xs", f000795, f000848);
  private static final Expr f000850 = Expr.makeLambda("x", f000841, f000849);
  private static final Expr f000851 =
      Expr.makeApplication(f000000, new Expr[] {f000841, f000843, f000795, f000850, f000817});
  private static final Expr f000852 = Expr.makeLambda("xs", f000795, f000851);
  private static final Expr f000853 = Expr.makeLambda("a", f000019, f000852);
  private static final Expr f000854 = Expr.makeLambda("n", f000221, f000853);
  private static final Expr f000855 = Expr.makeLambda("a", f000019, f000817);
  private static final Expr f000856 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000401, f000002);
  private static final Expr f000857 = Expr.makeIf(f000057, f000856, f000002);
  private static final Expr f000858 = Expr.makeLambda("xs", f000795, f000857);
  private static final Expr f000859 = Expr.makeLambda("x", f000070, f000858);
  private static final Expr f000860 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f000002, f000795, f000859, f000817});
  private static final Expr f000861 = Expr.makeLambda("xs", f000795, f000860);
  private static final Expr f000862 = Expr.makeLambda("f", f000797, f000861);
  private static final Expr f000863 = Expr.makeLambda("a", f000019, f000862);
  private static final Expr f000864 = Expr.makeIdentifier("list", 0);
  private static final Expr f000865 = Expr.makePi("_", f000864, f000864);
  private static final Expr f000866 = Expr.makeIdentifier("cons", 0);
  private static final Expr f000867 = Expr.makeApplication(f000866, new Expr[] {f000003, f000022});
  private static final Expr f000868 = Expr.makeApplication(f000013, new Expr[] {f000867});
  private static final Expr f000869 = Expr.makeLambda("l", f000864, f000868);
  private static final Expr f000870 = Expr.makeLambda("f", f000865, f000869);
  private static final Expr f000871 = Expr.makeLambda("x", f000070, f000870);
  private static final Expr f000872 = Expr.makeLambda("l", f000864, f000003);
  private static final Expr f000873 = Expr.makeIdentifier("nil", 0);
  private static final Expr f000874 =
      Expr.makeApplication(
          f000000, new Expr[] {f000070, f000002, f000865, f000871, f000872, f000873});
  private static final Expr f000875 = Expr.makeLambda("nil", f000864, f000874);
  private static final Expr f000876 = Expr.makePi("_", f000070, f000864);
  private static final Expr f000877 = Expr.makePi("_", f000864, f000876);
  private static final Expr f000878 = Expr.makeLambda("cons", f000877, f000875);
  private static final Expr f000879 = Expr.makeLambda("list", f000019, f000878);
  private static final Expr f000880 = Expr.makeLambda("xs", f000795, f000879);
  private static final Expr f000881 = Expr.makeLambda("a", f000019, f000880);
  private static final Expr f000882 = Expr.Constants.EMPTY_RECORD_TYPE;
  private static final Expr f000883 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000221),
            new SimpleImmutableEntry<String, Expr>("value", f000882)
          });
  private static final Expr f000884 = Expr.makeBuiltIn("Natural/fold");
  private static final Expr f000885 = Expr.makeApplication(f000010, new Expr[] {f000882});
  private static final Expr f000886 = Expr.Constants.EMPTY_RECORD_LITERAL;
  private static final Expr f000887 = Expr.makeNonEmptyListLiteral(new Expr[] {f000886});
  private static final Expr f000888 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000887, f000248);
  private static final Expr f000889 = Expr.makeLambda("as", f000885, f000888);
  private static final Expr f000890 = Expr.makeEmptyListLiteral(f000885);
  private static final Expr f000891 =
      Expr.makeApplication(f000884, new Expr[] {f000076, f000885, f000889, f000890});
  private static final Expr f000892 = Expr.makeApplication(f000842, new Expr[] {f000882, f000891});
  private static final Expr f000893 = Expr.makeApplication(f000013, new Expr[] {f000844});
  private static final Expr f000894 = Expr.makeNonEmptyListLiteral(new Expr[] {f000893});
  private static final Expr f000895 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000894, f000248);
  private static final Expr f000896 = Expr.makeLambda("as", f000795, f000895);
  private static final Expr f000897 = Expr.makeLambda("x", f000883, f000896);
  private static final Expr f000898 =
      Expr.makeApplication(f000000, new Expr[] {f000883, f000892, f000795, f000897, f000817});
  private static final Expr f000899 = Expr.makePi("_", f000221, f000070);
  private static final Expr f000900 = Expr.makeLambda("f", f000899, f000898);
  private static final Expr f000901 = Expr.makeLambda("a", f000019, f000900);
  private static final Expr f000902 = Expr.makeLambda("n", f000221, f000901);
  private static final Expr f000903 = Expr.makeBuiltIn("List/head");
  private static final Expr f000904 = Expr.makeApplication(f000903, new Expr[] {f000070, f000851});
  private static final Expr f000905 = Expr.makeLambda("xs", f000795, f000904);
  private static final Expr f000906 = Expr.makeLambda("a", f000019, f000905);
  private static final Expr f000907 = Expr.makeLambda("n", f000221, f000906);
  private static final Expr f000908 = Expr.makeFieldAccess(f000023, "index");
  private static final Expr f000909 =
      Expr.makeApplication(f000884, new Expr[] {f000908, f000070, f000013, f000022});
  private static final Expr f000910 = Expr.makeNonEmptyListLiteral(new Expr[] {f000909});
  private static final Expr f000911 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000910, f000248);
  private static final Expr f000912 = Expr.makeLambda("as", f000795, f000911);
  private static final Expr f000913 = Expr.makeLambda("y", f000883, f000912);
  private static final Expr f000914 =
      Expr.makeApplication(f000000, new Expr[] {f000883, f000892, f000795, f000913, f000817});
  private static final Expr f000915 = Expr.makeLambda("x", f000070, f000914);
  private static final Expr f000916 = Expr.makePi("_", f000070, f000070);
  private static final Expr f000917 = Expr.makeLambda("f", f000916, f000915);
  private static final Expr f000918 = Expr.makeLambda("a", f000019, f000917);
  private static final Expr f000919 = Expr.makeLambda("n", f000221, f000918);
  private static final Expr f000920 = Expr.makeBuiltIn("List/last");
  private static final Expr f000921 = Expr.makeBuiltIn("List/length");
  private static final Expr f000922 = Expr.makeNonEmptyListLiteral(new Expr[] {f000057});
  private static final Expr f000923 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000922, f000248);
  private static final Expr f000924 = Expr.makeLambda("as", f000822, f000923);
  private static final Expr f000925 = Expr.makeLambda("x", f000070, f000924);
  private static final Expr f000926 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f000002, f000822, f000925, f000827});
  private static final Expr f000927 = Expr.makeLambda("xs", f000795, f000926);
  private static final Expr f000928 = Expr.makePi("_", f000070, f000029);
  private static final Expr f000929 = Expr.makeLambda("f", f000928, f000927);
  private static final Expr f000930 = Expr.makeLambda("b", f000019, f000929);
  private static final Expr f000931 = Expr.makeLambda("a", f000019, f000930);
  private static final Expr f000932 = Expr.makeApplication(f000921, new Expr[] {f000070, f000002});
  private static final Expr f000933 = Expr.makeApplication(f000074, new Expr[] {f000932});
  private static final Expr f000934 = Expr.makeLambda("xs", f000795, f000933);
  private static final Expr f000935 = Expr.makeLambda("a", f000019, f000934);
  private static final Expr f000936 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000795),
            new SimpleImmutableEntry<String, Expr>("true", f000795)
          });
  private static final Expr f000937 = Expr.makeIdentifier("p", 0);
  private static final Expr f000938 = Expr.makeFieldAccess(f000937, "false");
  private static final Expr f000939 = Expr.makeFieldAccess(f000937, "true");
  private static final Expr f000940 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000401, f000939);
  private static final Expr f000941 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000938),
            new SimpleImmutableEntry<String, Expr>("true", f000940)
          });
  private static final Expr f000942 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000401, f000938);
  private static final Expr f000943 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000942),
            new SimpleImmutableEntry<String, Expr>("true", f000939)
          });
  private static final Expr f000944 = Expr.makeIf(f000057, f000941, f000943);
  private static final Expr f000945 = Expr.makeLambda("p", f000936, f000944);
  private static final Expr f000946 = Expr.makeLambda("x", f000070, f000945);
  private static final Expr f000947 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f000817),
            new SimpleImmutableEntry<String, Expr>("true", f000817)
          });
  private static final Expr f000948 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f000002, f000936, f000946, f000947});
  private static final Expr f000949 = Expr.makeLambda("xs", f000795, f000948);
  private static final Expr f000950 = Expr.makeLambda("f", f000797, f000949);
  private static final Expr f000951 = Expr.makeLambda("a", f000019, f000950);
  private static final Expr f000952 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000401, f000248);
  private static final Expr f000953 = Expr.makeLambda("as", f000795, f000952);
  private static final Expr f000954 =
      Expr.makeApplication(f000884, new Expr[] {f000076, f000795, f000953, f000817});
  private static final Expr f000955 = Expr.makeLambda("x", f000070, f000954);
  private static final Expr f000956 = Expr.makeLambda("a", f000019, f000955);
  private static final Expr f000957 = Expr.makeLambda("n", f000221, f000956);
  private static final Expr f000958 = Expr.makeApplication(f000010, new Expr[] {f000841});
  private static final Expr f000959 = Expr.makeIdentifier("kvss", 0);
  private static final Expr f000960 = Expr.makePi("_", f000221, f000958);
  private static final Expr f000961 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000221),
            new SimpleImmutableEntry<String, Expr>("diff", f000960)
          });
  private static final Expr f000962 = Expr.makeFieldAccess(f000023, "count");
  private static final Expr f000963 = Expr.makeIdentifier("kvs", 0);
  private static final Expr f000964 = Expr.makeApplication(f000921, new Expr[] {f000841, f000963});
  private static final Expr f000965 = Expr.makeOperatorApplication(Operator.PLUS, f000962, f000964);
  private static final Expr f000966 = Expr.makeIdentifier("kvOld", 0);
  private static final Expr f000967 = Expr.makeFieldAccess(f000966, "index");
  private static final Expr f000968 = Expr.makeOperatorApplication(Operator.PLUS, f000967, f000076);
  private static final Expr f000969 = Expr.makeFieldAccess(f000966, "value");
  private static final Expr f000970 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000968),
            new SimpleImmutableEntry<String, Expr>("value", f000969)
          });
  private static final Expr f000971 = Expr.makeNonEmptyListLiteral(new Expr[] {f000970});
  private static final Expr f000972 = Expr.makeIdentifier("z", 0);
  private static final Expr f000973 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000971, f000972);
  private static final Expr f000974 = Expr.makeLambda("z", f000958, f000973);
  private static final Expr f000975 = Expr.makeLambda("kvOld", f000841, f000974);
  private static final Expr f000976 = Expr.makeFieldAccess(f000023, "diff");
  private static final Expr f000977 = Expr.makeOperatorApplication(Operator.PLUS, f000076, f000964);
  private static final Expr f000978 = Expr.makeApplication(f000976, new Expr[] {f000977});
  private static final Expr f000979 =
      Expr.makeApplication(f000000, new Expr[] {f000841, f000963, f000958, f000975, f000978});
  private static final Expr f000980 = Expr.makeLambda("n", f000221, f000979);
  private static final Expr f000981 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000965),
            new SimpleImmutableEntry<String, Expr>("diff", f000980)
          });
  private static final Expr f000982 = Expr.makeLambda("y", f000961, f000981);
  private static final Expr f000983 = Expr.makeLambda("kvs", f000958, f000982);
  private static final Expr f000984 = Expr.makeNaturalLiteral(new BigInteger("0"));
  private static final Expr f000985 = Expr.makeEmptyListLiteral(f000958);
  private static final Expr f000986 = Expr.makeLambda("_", f000221, f000985);
  private static final Expr f000987 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("count", f000984),
            new SimpleImmutableEntry<String, Expr>("diff", f000986)
          });
  private static final Expr f000988 =
      Expr.makeApplication(f000000, new Expr[] {f000958, f000959, f000961, f000983, f000987});
  private static final Expr f000989 = Expr.makeFieldAccess(f000988, "diff");
  private static final Expr f000990 = Expr.makeApplication(f000989, new Expr[] {f000984});
  private static final Expr f000991 = Expr.makeApplication(f000010, new Expr[] {f000958});
  private static final Expr f000992 = Expr.makeLambda("kvss", f000991, f000990);
  private static final Expr f000993 = Expr.makeLambda("a", f000019, f000992);
  private static final Expr f000994 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000846, f000014);
  private static final Expr f000995 = Expr.makeIf(f000994, f000847, f000002);
  private static final Expr f000996 = Expr.makeLambda("xs", f000795, f000995);
  private static final Expr f000997 = Expr.makeLambda("x", f000841, f000996);
  private static final Expr f000998 =
      Expr.makeApplication(f000000, new Expr[] {f000841, f000843, f000795, f000997, f000817});
  private static final Expr f000999 = Expr.makeLambda("xs", f000795, f000998);
  private static final Expr f001000 = Expr.makeLambda("a", f000019, f000999);
  private static final Expr f001001 = Expr.makeLambda("n", f000221, f001000);
  private static final Expr f001002 = Expr.makeApplication(f000396, new Expr[] {f000070});
  private static final Expr f001003 = Expr.makeLambda("x", f000070, f000401);
  private static final Expr f001004 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000817),
            new SimpleImmutableEntry<String, Expr>("Some", f001003)
          });
  private static final Expr f001005 = Expr.makeMerge(f001004, f000022, null);
  private static final Expr f001006 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f001005, f000795, f000812});
  private static final Expr f001007 = Expr.makeLambda("x", f001002, f001006);
  private static final Expr f001008 =
      Expr.makeApplication(f000000, new Expr[] {f001002, f000002, f000795, f001007, f000817});
  private static final Expr f001009 = Expr.makeApplication(f000010, new Expr[] {f001002});
  private static final Expr f001010 = Expr.makeLambda("xs", f001009, f001008);
  private static final Expr f001011 = Expr.makeLambda("a", f000019, f001010);
  private static final Expr f001012 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f000070),
            new SimpleImmutableEntry<String, Expr>("_2", f000029)
          });
  private static final Expr f001013 = Expr.makeFieldAccess(f000022, "_1");
  private static final Expr f001014 = Expr.makeNonEmptyListLiteral(new Expr[] {f001013});
  private static final Expr f001015 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001014, f000248);
  private static final Expr f001016 = Expr.makeLambda("as", f000795, f001015);
  private static final Expr f001017 = Expr.makeLambda("x", f001012, f001016);
  private static final Expr f001018 =
      Expr.makeApplication(f000000, new Expr[] {f001012, f000002, f000795, f001017, f000817});
  private static final Expr f001019 = Expr.makeFieldAccess(f000022, "_2");
  private static final Expr f001020 = Expr.makeNonEmptyListLiteral(new Expr[] {f001019});
  private static final Expr f001021 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001020, f000248);
  private static final Expr f001022 = Expr.makeLambda("as", f000822, f001021);
  private static final Expr f001023 = Expr.makeLambda("x", f001012, f001022);
  private static final Expr f001024 =
      Expr.makeApplication(f000000, new Expr[] {f001012, f000002, f000822, f001023, f000827});
  private static final Expr f001025 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001018),
            new SimpleImmutableEntry<String, Expr>("_2", f001024)
          });
  private static final Expr f001026 = Expr.makeApplication(f000010, new Expr[] {f001012});
  private static final Expr f001027 = Expr.makeLambda("xs", f001026, f001025);
  private static final Expr f001028 = Expr.makeLambda("b", f000019, f001027);
  private static final Expr f001029 = Expr.makeLambda("a", f000019, f001028);
  private static final Expr f001030 = Expr.makeIdentifier("rest", 0);
  private static final Expr f001031 = Expr.makeIdentifier("ix", 0);
  private static final Expr f001032 = Expr.makeFieldAccess(f001031, "value");
  private static final Expr f001033 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001032),
            new SimpleImmutableEntry<String, Expr>("_2", f000023)
          });
  private static final Expr f001034 = Expr.makeNonEmptyListLiteral(new Expr[] {f001033});
  private static final Expr f001035 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001034, f001030);
  private static final Expr f001036 = Expr.makeLambda("y", f000029, f001035);
  private static final Expr f001037 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001030),
            new SimpleImmutableEntry<String, Expr>("Some", f001036)
          });
  private static final Expr f001038 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000221),
            new SimpleImmutableEntry<String, Expr>("value", f000029)
          });
  private static final Expr f001039 = Expr.makeApplication(f000842, new Expr[] {f000029, f000813});
  private static final Expr f001040 = Expr.makeFieldAccess(f001031, "index");
  private static final Expr f001041 = Expr.makeApplication(f000089, new Expr[] {f000844, f001040});
  private static final Expr f001042 = Expr.makeApplication(f000074, new Expr[] {f001041});
  private static final Expr f001043 = Expr.makeIf(f001042, f000847, f000002);
  private static final Expr f001044 = Expr.makeLambda("xs", f000822, f001043);
  private static final Expr f001045 = Expr.makeLambda("x", f001038, f001044);
  private static final Expr f001046 =
      Expr.makeApplication(f000000, new Expr[] {f001038, f001039, f000822, f001045, f000827});
  private static final Expr f001047 = Expr.makeApplication(f000903, new Expr[] {f000029, f001046});
  private static final Expr f001048 = Expr.makeMerge(f001037, f001047, null);
  private static final Expr f001049 = Expr.makeLambda("rest", f001026, f001048);
  private static final Expr f001050 = Expr.makeLambda("ix", f000841, f001049);
  private static final Expr f001051 = Expr.makeEmptyListLiteral(f001026);
  private static final Expr f001052 =
      Expr.makeApplication(f000000, new Expr[] {f000841, f000843, f001026, f001050, f001051});
  private static final Expr f001053 = Expr.makeLambda("ys", f000822, f001052);
  private static final Expr f001054 = Expr.makeLambda("b", f000019, f001053);
  private static final Expr f001055 = Expr.makeLambda("xs", f000795, f001054);
  private static final Expr f001056 = Expr.makeLambda("a", f000019, f001055);
  private static final Expr f001057 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("all", f000799),
            new SimpleImmutableEntry<String, Expr>("any", f000806),
            new SimpleImmutableEntry<String, Expr>("build", f000807),
            new SimpleImmutableEntry<String, Expr>("concat", f000821),
            new SimpleImmutableEntry<String, Expr>("concatMap", f000833),
            new SimpleImmutableEntry<String, Expr>("default", f000840),
            new SimpleImmutableEntry<String, Expr>("drop", f000854),
            new SimpleImmutableEntry<String, Expr>("empty", f000855),
            new SimpleImmutableEntry<String, Expr>("filter", f000863),
            new SimpleImmutableEntry<String, Expr>("fold", f000000),
            new SimpleImmutableEntry<String, Expr>("foldLeft", f000881),
            new SimpleImmutableEntry<String, Expr>("generate", f000902),
            new SimpleImmutableEntry<String, Expr>("head", f000903),
            new SimpleImmutableEntry<String, Expr>("index", f000907),
            new SimpleImmutableEntry<String, Expr>("indexed", f000842),
            new SimpleImmutableEntry<String, Expr>("iterate", f000919),
            new SimpleImmutableEntry<String, Expr>("last", f000920),
            new SimpleImmutableEntry<String, Expr>("length", f000921),
            new SimpleImmutableEntry<String, Expr>("map", f000931),
            new SimpleImmutableEntry<String, Expr>("null", f000935),
            new SimpleImmutableEntry<String, Expr>("partition", f000951),
            new SimpleImmutableEntry<String, Expr>("replicate", f000957),
            new SimpleImmutableEntry<String, Expr>("reverse", f000487),
            new SimpleImmutableEntry<String, Expr>("shifted", f000993),
            new SimpleImmutableEntry<String, Expr>("take", f001001),
            new SimpleImmutableEntry<String, Expr>("unpackOptionals", f001011),
            new SimpleImmutableEntry<String, Expr>("unzip", f001029),
            new SimpleImmutableEntry<String, Expr>("zip", f001056)
          });
  private static final Expr f001058 =
      Expr.makeUnionType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Environment", f000226),
            new SimpleImmutableEntry<String, Expr>("Local", f000226),
            new SimpleImmutableEntry<String, Expr>("Missing", null),
            new SimpleImmutableEntry<String, Expr>("Remote", f000226)
          });
  private static final Expr f001059 =
      Expr.makeRecordLiteral(new Entry[] {new SimpleImmutableEntry<String, Expr>("Type", f001058)});
  private static final Expr f001060 = Expr.makeIdentifier("k", 0);
  private static final Expr f001061 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f001060),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000279)
          });
  private static final Expr f001062 = Expr.makeLambda("v", f000019, f001061);
  private static final Expr f001063 = Expr.makeLambda("k", f000019, f001062);
  private static final Expr f001064 = Expr.makeApplication(f000010, new Expr[] {f001061});
  private static final Expr f001065 = Expr.makeLambda("v", f000019, f001064);
  private static final Expr f001066 = Expr.makeLambda("k", f000019, f001065);
  private static final Expr f001067 = Expr.makeEmptyListLiteral(f001064);
  private static final Expr f001068 = Expr.makeLambda("v", f000019, f001067);
  private static final Expr f001069 = Expr.makeLambda("k", f000019, f001068);
  private static final Expr f001070 = Expr.makeApplication(f000010, new Expr[] {f001060});
  private static final Expr f001071 = Expr.makeNonEmptyListLiteral(new Expr[] {f000293});
  private static final Expr f001072 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001071, f000248);
  private static final Expr f001073 = Expr.makeLambda("as", f001070, f001072);
  private static final Expr f001074 = Expr.makeLambda("x", f001061, f001073);
  private static final Expr f001075 = Expr.makeEmptyListLiteral(f001070);
  private static final Expr f001076 =
      Expr.makeApplication(f000000, new Expr[] {f001061, f000002, f001070, f001074, f001075});
  private static final Expr f001077 = Expr.makeLambda("xs", f001064, f001076);
  private static final Expr f001078 = Expr.makeLambda("v", f000019, f001077);
  private static final Expr f001079 = Expr.makeLambda("k", f000019, f001078);
  private static final Expr f001080 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f001060),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000070)
          });
  private static final Expr f001081 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f001060),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000029)
          });
  private static final Expr f001082 = Expr.makeApplication(f000010, new Expr[] {f001081});
  private static final Expr f001083 = Expr.makeApplication(f000013, new Expr[] {f000294});
  private static final Expr f001084 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000293),
            new SimpleImmutableEntry<String, Expr>("mapValue", f001083)
          });
  private static final Expr f001085 = Expr.makeNonEmptyListLiteral(new Expr[] {f001084});
  private static final Expr f001086 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001085, f000248);
  private static final Expr f001087 = Expr.makeLambda("as", f001082, f001086);
  private static final Expr f001088 = Expr.makeLambda("x", f001080, f001087);
  private static final Expr f001089 = Expr.makeEmptyListLiteral(f001082);
  private static final Expr f001090 =
      Expr.makeApplication(f000000, new Expr[] {f001080, f000085, f001082, f001088, f001089});
  private static final Expr f001091 = Expr.makeApplication(f000010, new Expr[] {f001080});
  private static final Expr f001092 = Expr.makeLambda("m", f001091, f001090);
  private static final Expr f001093 = Expr.makeLambda("f", f000928, f001092);
  private static final Expr f001094 = Expr.makeLambda("b", f000019, f001093);
  private static final Expr f001095 = Expr.makeLambda("a", f000019, f001094);
  private static final Expr f001096 = Expr.makeLambda("k", f000019, f001095);
  private static final Expr f001097 = Expr.makeApplication(f000396, new Expr[] {f000279});
  private static final Expr f001098 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f001060),
            new SimpleImmutableEntry<String, Expr>("mapValue", f001097)
          });
  private static final Expr f001099 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("mapKey", f000293),
            new SimpleImmutableEntry<String, Expr>("mapValue", f000279)
          });
  private static final Expr f001100 = Expr.makeNonEmptyListLiteral(new Expr[] {f001099});
  private static final Expr f001101 = Expr.makeLambda("v", f000279, f001100);
  private static final Expr f001102 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001067),
            new SimpleImmutableEntry<String, Expr>("Some", f001101)
          });
  private static final Expr f001103 = Expr.makeMerge(f001102, f000294, null);
  private static final Expr f001104 = Expr.makeLambda("as", f001064, f000337);
  private static final Expr f001105 = Expr.makeLambda("a", f001061, f001104);
  private static final Expr f001106 =
      Expr.makeApplication(f000000, new Expr[] {f001061, f001103, f001064, f001105});
  private static final Expr f001107 = Expr.makeLambda("x", f001098, f001106);
  private static final Expr f001108 =
      Expr.makeApplication(f000000, new Expr[] {f001098, f000002, f001064, f001107, f001067});
  private static final Expr f001109 = Expr.makeApplication(f000010, new Expr[] {f001098});
  private static final Expr f001110 = Expr.makeLambda("xs", f001109, f001108);
  private static final Expr f001111 = Expr.makeLambda("v", f000019, f001110);
  private static final Expr f001112 = Expr.makeLambda("k", f000019, f001111);
  private static final Expr f001113 = Expr.makeApplication(f000010, new Expr[] {f000279});
  private static final Expr f001114 = Expr.makeNonEmptyListLiteral(new Expr[] {f000294});
  private static final Expr f001115 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001114, f000248);
  private static final Expr f001116 = Expr.makeLambda("as", f001113, f001115);
  private static final Expr f001117 = Expr.makeLambda("x", f001061, f001116);
  private static final Expr f001118 = Expr.makeEmptyListLiteral(f001113);
  private static final Expr f001119 =
      Expr.makeApplication(f000000, new Expr[] {f001061, f000002, f001113, f001117, f001118});
  private static final Expr f001120 = Expr.makeLambda("xs", f001064, f001119);
  private static final Expr f001121 = Expr.makeLambda("v", f000019, f001120);
  private static final Expr f001122 = Expr.makeLambda("k", f000019, f001121);
  private static final Expr f001123 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Entry", f001063),
            new SimpleImmutableEntry<String, Expr>("Type", f001066),
            new SimpleImmutableEntry<String, Expr>("empty", f001069),
            new SimpleImmutableEntry<String, Expr>("keyText", f000278),
            new SimpleImmutableEntry<String, Expr>("keyValue", f000282),
            new SimpleImmutableEntry<String, Expr>("keys", f001079),
            new SimpleImmutableEntry<String, Expr>("map", f001096),
            new SimpleImmutableEntry<String, Expr>("unpackOptionals", f001112),
            new SimpleImmutableEntry<String, Expr>("values", f001122)
          });
  private static final Expr f001124 = Expr.makeApplication(f000010, new Expr[] {f000085});
  private static final Expr f001125 = Expr.makePi("_", f001124, f000085);
  private static final Expr f001126 = Expr.makeLambda("m", f000019, f001125);
  private static final Expr f001127 = Expr.makeBuiltIn("Natural/build");
  private static final Expr f001128 = Expr.makeApplication(f000010, new Expr[] {f000221});
  private static final Expr f001129 = Expr.makeNonEmptyListLiteral(new Expr[] {f000844});
  private static final Expr f001130 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001129, f000248);
  private static final Expr f001131 = Expr.makeLambda("as", f001128, f001130);
  private static final Expr f001132 = Expr.makeLambda("x", f000883, f001131);
  private static final Expr f001133 = Expr.makeEmptyListLiteral(f001128);
  private static final Expr f001134 =
      Expr.makeApplication(f000000, new Expr[] {f000883, f000892, f001128, f001132, f001133});
  private static final Expr f001135 = Expr.makeLambda("n", f000221, f001134);
  private static final Expr f001136 = Expr.makeApplication(f000089, new Expr[] {f000029, f000070});
  private static final Expr f001137 = Expr.makeApplication(f000074, new Expr[] {f001136});
  private static final Expr f001138 = Expr.makeApplication(f000089, new Expr[] {f000070, f000029});
  private static final Expr f001139 = Expr.makeApplication(f000074, new Expr[] {f001138});
  private static final Expr f001140 = Expr.makeOperatorApplication(Operator.AND, f001137, f001139);
  private static final Expr f001141 = Expr.makeLambda("b", f000221, f001140);
  private static final Expr f001142 = Expr.makeLambda("a", f000221, f001141);
  private static final Expr f001143 = Expr.makeBuiltIn("Natural/even");
  private static final Expr f001144 = Expr.makeApplication(f000089, new Expr[] {f000023, f000022});
  private static final Expr f001145 = Expr.makeApplication(f000074, new Expr[] {f001144});
  private static final Expr f001146 =
      Expr.makeOperatorApplication(Operator.EQUALS, f001145, f000014);
  private static final Expr f001147 = Expr.makeLambda("y", f000221, f001146);
  private static final Expr f001148 = Expr.makeLambda("x", f000221, f001147);
  private static final Expr f001149 = Expr.makeApplication(f000089, new Expr[] {f000022, f000023});
  private static final Expr f001150 = Expr.makeApplication(f000074, new Expr[] {f001149});
  private static final Expr f001151 = Expr.makeLambda("y", f000221, f001150);
  private static final Expr f001152 = Expr.makeLambda("x", f000221, f001151);
  private static final Expr f001153 =
      Expr.makeOperatorApplication(Operator.EQUALS, f001150, f000014);
  private static final Expr f001154 = Expr.makeLambda("y", f000221, f001153);
  private static final Expr f001155 = Expr.makeLambda("x", f000221, f001154);
  private static final Expr f001156 = Expr.makeLambda("y", f000221, f001145);
  private static final Expr f001157 = Expr.makeLambda("x", f000221, f001156);
  private static final Expr f001158 = Expr.makeIf(f001137, f000029, f000070);
  private static final Expr f001159 = Expr.makeLambda("b", f000221, f001158);
  private static final Expr f001160 = Expr.makeLambda("a", f000221, f001159);
  private static final Expr f001161 =
      Expr.makeApplication(f000000, new Expr[] {f000221, f000002, f000221, f001160, f000022});
  private static final Expr f001162 = Expr.makeApplication(f000218, new Expr[] {f001161});
  private static final Expr f001163 = Expr.makeLambda("x", f000221, f001162);
  private static final Expr f001164 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000222),
            new SimpleImmutableEntry<String, Expr>("Some", f001163)
          });
  private static final Expr f001165 = Expr.makeApplication(f000903, new Expr[] {f000221, f000002});
  private static final Expr f001166 = Expr.makeMerge(f001164, f001165, null);
  private static final Expr f001167 = Expr.makeLambda("xs", f001128, f001166);
  private static final Expr f001168 = Expr.makeApplication(f000074, new Expr[] {f000022});
  private static final Expr f001169 = Expr.makeIf(f001137, f000070, f000029);
  private static final Expr f001170 = Expr.makeLambda("b", f000221, f001169);
  private static final Expr f001171 = Expr.makeLambda("a", f000221, f001170);
  private static final Expr f001172 =
      Expr.makeApplication(f000000, new Expr[] {f000221, f000002, f000221, f001171, f000022});
  private static final Expr f001173 = Expr.makeIf(f001168, f000022, f001172);
  private static final Expr f001174 = Expr.makeApplication(f000218, new Expr[] {f001173});
  private static final Expr f001175 = Expr.makeLambda("x", f000221, f001174);
  private static final Expr f001176 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000222),
            new SimpleImmutableEntry<String, Expr>("Some", f001175)
          });
  private static final Expr f001177 = Expr.makeMerge(f001176, f001165, null);
  private static final Expr f001178 = Expr.makeLambda("xs", f001128, f001177);
  private static final Expr f001179 = Expr.makeBuiltIn("Natural/odd");
  private static final Expr f001180 =
      Expr.makeOperatorApplication(Operator.TIMES, f000003, f000004);
  private static final Expr f001181 = Expr.makeLambda("r", f000221, f001180);
  private static final Expr f001182 = Expr.makeLambda("l", f000221, f001181);
  private static final Expr f001183 = Expr.makeNaturalLiteral(new BigInteger("1"));
  private static final Expr f001184 =
      Expr.makeApplication(f000000, new Expr[] {f000221, f000002, f000221, f001182, f001183});
  private static final Expr f001185 = Expr.makeLambda("xs", f001128, f001184);
  private static final Expr f001186 = Expr.makeApplication(f000921, new Expr[] {f000221, f000002});
  private static final Expr f001187 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f001128),
            new SimpleImmutableEntry<String, Expr>("sorted", f001128)
          });
  private static final Expr f001188 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f001133),
            new SimpleImmutableEntry<String, Expr>("true", f001133)
          });
  private static final Expr f001189 = Expr.makeFieldAccess(f000022, "rest");
  private static final Expr f001190 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("false", f001128),
            new SimpleImmutableEntry<String, Expr>("true", f001128)
          });
  private static final Expr f001191 = Expr.makeApplication(f000089, new Expr[] {f000085, f000022});
  private static final Expr f001192 = Expr.makeApplication(f000074, new Expr[] {f001191});
  private static final Expr f001193 = Expr.makeIf(f001192, f000941, f000943);
  private static final Expr f001194 = Expr.makeLambda("p", f001190, f001193);
  private static final Expr f001195 = Expr.makeLambda("x", f000221, f001194);
  private static final Expr f001196 =
      Expr.makeApplication(f000000, new Expr[] {f000221, f001189, f001190, f001195, f001188});
  private static final Expr f001197 = Expr.makeLambda("m", f000221, f001196);
  private static final Expr f001198 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001188),
            new SimpleImmutableEntry<String, Expr>("Some", f001197)
          });
  private static final Expr f001199 = Expr.makeFieldAccess(f000419, "rest");
  private static final Expr f001200 =
      Expr.makeApplication(f000000, new Expr[] {f000221, f001199, f000221, f001171, f000022});
  private static final Expr f001201 = Expr.makeIf(f001168, f000022, f001200);
  private static final Expr f001202 = Expr.makeApplication(f000218, new Expr[] {f001201});
  private static final Expr f001203 = Expr.makeLambda("x", f000221, f001202);
  private static final Expr f001204 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000222),
            new SimpleImmutableEntry<String, Expr>("Some", f001203)
          });
  private static final Expr f001205 = Expr.makeApplication(f000903, new Expr[] {f000221, f001189});
  private static final Expr f001206 = Expr.makeMerge(f001204, f001205, null);
  private static final Expr f001207 = Expr.makeMerge(f001198, f001206, null);
  private static final Expr f001208 = Expr.makeFieldAccess(f001207, "false");
  private static final Expr f001209 = Expr.makeFieldAccess(f000022, "sorted");
  private static final Expr f001210 = Expr.makeFieldAccess(f001207, "true");
  private static final Expr f001211 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001209, f001210);
  private static final Expr f001212 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f001208),
            new SimpleImmutableEntry<String, Expr>("sorted", f001211)
          });
  private static final Expr f001213 = Expr.makeLambda("x", f001187, f001212);
  private static final Expr f001214 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("rest", f000002),
            new SimpleImmutableEntry<String, Expr>("sorted", f001133)
          });
  private static final Expr f001215 =
      Expr.makeApplication(f000884, new Expr[] {f001186, f001187, f001213, f001214});
  private static final Expr f001216 = Expr.makeFieldAccess(f001215, "sorted");
  private static final Expr f001217 = Expr.makeLambda("xs", f001128, f001216);
  private static final Expr f001218 = Expr.makeOperatorApplication(Operator.PLUS, f000003, f000004);
  private static final Expr f001219 = Expr.makeLambda("r", f000221, f001218);
  private static final Expr f001220 = Expr.makeLambda("l", f000221, f001219);
  private static final Expr f001221 =
      Expr.makeApplication(f000000, new Expr[] {f000221, f000002, f000221, f001220, f000984});
  private static final Expr f001222 = Expr.makeLambda("xs", f001128, f001221);
  private static final Expr f001223 = Expr.makeApplication(f000094, new Expr[] {f000076});
  private static final Expr f001224 = Expr.makeApplication(f000217, new Expr[] {f001223});
  private static final Expr f001225 = Expr.makeLambda("n", f000221, f001224);
  private static final Expr f001226 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("build", f001127),
            new SimpleImmutableEntry<String, Expr>("enumerate", f001135),
            new SimpleImmutableEntry<String, Expr>("equal", f001142),
            new SimpleImmutableEntry<String, Expr>("even", f001143),
            new SimpleImmutableEntry<String, Expr>("fold", f000884),
            new SimpleImmutableEntry<String, Expr>("greaterThan", f001148),
            new SimpleImmutableEntry<String, Expr>("greaterThanEqual", f001152),
            new SimpleImmutableEntry<String, Expr>("isZero", f000074),
            new SimpleImmutableEntry<String, Expr>("lessThan", f001155),
            new SimpleImmutableEntry<String, Expr>("lessThanEqual", f001157),
            new SimpleImmutableEntry<String, Expr>("listMax", f001167),
            new SimpleImmutableEntry<String, Expr>("listMin", f001178),
            new SimpleImmutableEntry<String, Expr>("max", f001160),
            new SimpleImmutableEntry<String, Expr>("min", f001171),
            new SimpleImmutableEntry<String, Expr>("odd", f001179),
            new SimpleImmutableEntry<String, Expr>("product", f001185),
            new SimpleImmutableEntry<String, Expr>("show", f000516),
            new SimpleImmutableEntry<String, Expr>("sort", f001217),
            new SimpleImmutableEntry<String, Expr>("subtract", f000089),
            new SimpleImmutableEntry<String, Expr>("sum", f001222),
            new SimpleImmutableEntry<String, Expr>("toDouble", f001225),
            new SimpleImmutableEntry<String, Expr>("toInteger", f000094)
          });
  private static final Expr f001227 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000070),
            new SimpleImmutableEntry<String, Expr>("tail", f000795)
          });
  private static final Expr f001228 = Expr.makeLambda("a", f000019, f001227);
  private static final Expr f001229 = Expr.makeFieldAccess(f000002, "head");
  private static final Expr f001230 = Expr.makeNonEmptyListLiteral(new Expr[] {f001229});
  private static final Expr f001231 = Expr.makeFieldAccess(f000002, "tail");
  private static final Expr f001232 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001230, f001231);
  private static final Expr f001233 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f001232, f000001, f000793, f000008});
  private static final Expr f001234 = Expr.makeLambda("xs", f001227, f001233);
  private static final Expr f001235 = Expr.makeLambda("f", f000797, f001234);
  private static final Expr f001236 = Expr.makeLambda("a", f000019, f001235);
  private static final Expr f001237 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f001232, f000001, f000802, f000014});
  private static final Expr f001238 = Expr.makeLambda("xs", f001227, f001237);
  private static final Expr f001239 = Expr.makeLambda("f", f000797, f001238);
  private static final Expr f001240 = Expr.makeLambda("a", f000019, f001239);
  private static final Expr f001241 = Expr.makeFieldAccess(f000808, "head");
  private static final Expr f001242 = Expr.makeFieldAccess(f001241, "head");
  private static final Expr f001243 = Expr.makeFieldAccess(f001241, "tail");
  private static final Expr f001244 = Expr.makeFieldAccess(f000808, "tail");
  private static final Expr f001245 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f000473, f000795, f000812});
  private static final Expr f001246 = Expr.makeLambda("x", f001227, f001245);
  private static final Expr f001247 =
      Expr.makeApplication(f000000, new Expr[] {f001227, f001244, f000795, f001246, f000817});
  private static final Expr f001248 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001243, f001247);
  private static final Expr f001249 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001242),
            new SimpleImmutableEntry<String, Expr>("tail", f001248)
          });
  private static final Expr f001250 = Expr.makeApplication(f000010, new Expr[] {f001227});
  private static final Expr f001251 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001227),
            new SimpleImmutableEntry<String, Expr>("tail", f001250)
          });
  private static final Expr f001252 = Expr.makeLambda("xss", f001251, f001249);
  private static final Expr f001253 = Expr.makeLambda("a", f000019, f001252);
  private static final Expr f001254 = Expr.makeApplication(f000013, new Expr[] {f001229});
  private static final Expr f001255 = Expr.makeFieldAccess(f001254, "head");
  private static final Expr f001256 = Expr.makeFieldAccess(f001254, "tail");
  private static final Expr f001257 = Expr.makeFieldAccess(f000057, "head");
  private static final Expr f001258 = Expr.makeNonEmptyListLiteral(new Expr[] {f001257});
  private static final Expr f001259 = Expr.makeFieldAccess(f000057, "tail");
  private static final Expr f001260 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001258, f001259);
  private static final Expr f001261 =
      Expr.makeApplication(f000000, new Expr[] {f000029, f001260, f000822, f000824});
  private static final Expr f001262 = Expr.makeLambda("x", f000070, f001261);
  private static final Expr f001263 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f001231, f000822, f001262, f000827});
  private static final Expr f001264 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001256, f001263);
  private static final Expr f001265 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001255),
            new SimpleImmutableEntry<String, Expr>("tail", f001264)
          });
  private static final Expr f001266 = Expr.makeLambda("xs", f001227, f001265);
  private static final Expr f001267 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000029),
            new SimpleImmutableEntry<String, Expr>("tail", f000822)
          });
  private static final Expr f001268 = Expr.makePi("_", f000070, f001267);
  private static final Expr f001269 = Expr.makeLambda("f", f001268, f001266);
  private static final Expr f001270 = Expr.makeLambda("b", f000019, f001269);
  private static final Expr f001271 = Expr.makeLambda("a", f000019, f001270);
  private static final Expr f001272 = Expr.makeLambda("xs", f001227, f001229);
  private static final Expr f001273 = Expr.makeLambda("a", f000019, f001272);
  private static final Expr f001274 = Expr.makeApplication(f000074, new Expr[] {f000076});
  private static final Expr f001275 = Expr.makeApplication(f000218, new Expr[] {f001229});
  private static final Expr f001276 = Expr.makeApplication(f000842, new Expr[] {f000070, f001231});
  private static final Expr f001277 = Expr.makeApplication(f000089, new Expr[] {f001183, f000076});
  private static final Expr f001278 = Expr.makeApplication(f000089, new Expr[] {f000844, f001277});
  private static final Expr f001279 = Expr.makeApplication(f000074, new Expr[] {f001278});
  private static final Expr f001280 = Expr.makeIf(f001279, f000847, f000002);
  private static final Expr f001281 = Expr.makeLambda("xs", f000795, f001280);
  private static final Expr f001282 = Expr.makeLambda("x", f000841, f001281);
  private static final Expr f001283 =
      Expr.makeApplication(f000000, new Expr[] {f000841, f001276, f000795, f001282, f000817});
  private static final Expr f001284 = Expr.makeApplication(f000903, new Expr[] {f000070, f001283});
  private static final Expr f001285 = Expr.makeIf(f001274, f001275, f001284);
  private static final Expr f001286 = Expr.makeLambda("xs", f001227, f001285);
  private static final Expr f001287 = Expr.makeLambda("a", f000019, f001286);
  private static final Expr f001288 = Expr.makeLambda("n", f000221, f001287);
  private static final Expr f001289 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f000984),
            new SimpleImmutableEntry<String, Expr>("value", f001229)
          });
  private static final Expr f001290 = Expr.makeOperatorApplication(Operator.PLUS, f000844, f001183);
  private static final Expr f001291 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("index", f001290),
            new SimpleImmutableEntry<String, Expr>("value", f000310)
          });
  private static final Expr f001292 = Expr.makeNonEmptyListLiteral(new Expr[] {f001291});
  private static final Expr f001293 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001292, f000248);
  private static final Expr f001294 = Expr.makeLambda("as", f000958, f001293);
  private static final Expr f001295 = Expr.makeLambda("x", f000841, f001294);
  private static final Expr f001296 =
      Expr.makeApplication(f000000, new Expr[] {f000841, f001276, f000958, f001295, f000985});
  private static final Expr f001297 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001289),
            new SimpleImmutableEntry<String, Expr>("tail", f001296)
          });
  private static final Expr f001298 = Expr.makeLambda("xs", f001227, f001297);
  private static final Expr f001299 = Expr.makeLambda("a", f000019, f001298);
  private static final Expr f001300 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001229),
            new SimpleImmutableEntry<String, Expr>("Some", f000071)
          });
  private static final Expr f001301 = Expr.makeApplication(f000920, new Expr[] {f000070, f001231});
  private static final Expr f001302 = Expr.makeMerge(f001300, f001301, null);
  private static final Expr f001303 = Expr.makeLambda("xs", f001227, f001302);
  private static final Expr f001304 = Expr.makeLambda("a", f000019, f001303);
  private static final Expr f001305 = Expr.makeApplication(f000921, new Expr[] {f000070, f001231});
  private static final Expr f001306 = Expr.makeOperatorApplication(Operator.PLUS, f001305, f001183);
  private static final Expr f001307 = Expr.makeLambda("xs", f001227, f001306);
  private static final Expr f001308 = Expr.makeLambda("a", f000019, f001307);
  private static final Expr f001309 = Expr.makeIdentifier("head", 0);
  private static final Expr f001310 = Expr.makeIdentifier("tail", 0);
  private static final Expr f001311 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001309),
            new SimpleImmutableEntry<String, Expr>("tail", f001310)
          });
  private static final Expr f001312 = Expr.makeLambda("tail", f000795, f001311);
  private static final Expr f001313 = Expr.makeLambda("head", f000070, f001312);
  private static final Expr f001314 = Expr.makeLambda("a", f000019, f001313);
  private static final Expr f001315 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f001231, f000822, f000925, f000827});
  private static final Expr f001316 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001254),
            new SimpleImmutableEntry<String, Expr>("tail", f001315)
          });
  private static final Expr f001317 = Expr.makeLambda("xs", f001227, f001316);
  private static final Expr f001318 = Expr.makeLambda("f", f000928, f001317);
  private static final Expr f001319 = Expr.makeLambda("b", f000019, f001318);
  private static final Expr f001320 = Expr.makeLambda("a", f000019, f001319);
  private static final Expr f001321 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001229),
            new SimpleImmutableEntry<String, Expr>("tail", f000817)
          });
  private static final Expr f001322 = Expr.makeApplication(f000487, new Expr[] {f000070, f001231});
  private static final Expr f001323 = Expr.makeApplication(f000842, new Expr[] {f000070, f001322});
  private static final Expr f001324 = Expr.makeApplication(f000089, new Expr[] {f000844, f001183});
  private static final Expr f001325 = Expr.makeApplication(f000074, new Expr[] {f001324});
  private static final Expr f001326 = Expr.makeIf(f001325, f000847, f000002);
  private static final Expr f001327 = Expr.makeLambda("xs", f000795, f001326);
  private static final Expr f001328 = Expr.makeLambda("x", f000841, f001327);
  private static final Expr f001329 =
      Expr.makeApplication(f000000, new Expr[] {f000841, f001323, f000795, f001328, f000817});
  private static final Expr f001330 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001329, f001230);
  private static final Expr f001331 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000023),
            new SimpleImmutableEntry<String, Expr>("tail", f001330)
          });
  private static final Expr f001332 = Expr.makeLambda("y", f000070, f001331);
  private static final Expr f001333 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001321),
            new SimpleImmutableEntry<String, Expr>("Some", f001332)
          });
  private static final Expr f001334 = Expr.makeApplication(f000903, new Expr[] {f000070, f001322});
  private static final Expr f001335 = Expr.makeMerge(f001333, f001334, null);
  private static final Expr f001336 = Expr.makeLambda("xs", f001227, f001335);
  private static final Expr f001337 = Expr.makeLambda("a", f000019, f001336);
  private static final Expr f001338 = Expr.makeFieldAccess(f000959, "head");
  private static final Expr f001339 = Expr.makeFieldAccess(f001338, "head");
  private static final Expr f001340 = Expr.makeFieldAccess(f001338, "tail");
  private static final Expr f001341 = Expr.makeNonEmptyListLiteral(new Expr[] {f001340});
  private static final Expr f001342 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000841),
            new SimpleImmutableEntry<String, Expr>("tail", f000958)
          });
  private static final Expr f001343 = Expr.makeFieldAccess(f000959, "tail");
  private static final Expr f001344 =
      Expr.makeApplication(f000000, new Expr[] {f000841, f000473, f000958, f001295, f000985});
  private static final Expr f001345 = Expr.makeNonEmptyListLiteral(new Expr[] {f001344});
  private static final Expr f001346 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001345, f000248);
  private static final Expr f001347 = Expr.makeLambda("as", f000991, f001346);
  private static final Expr f001348 = Expr.makeLambda("x", f001342, f001347);
  private static final Expr f001349 = Expr.makeEmptyListLiteral(f000991);
  private static final Expr f001350 =
      Expr.makeApplication(f000000, new Expr[] {f001342, f001343, f000991, f001348, f001349});
  private static final Expr f001351 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001341, f001350);
  private static final Expr f001352 =
      Expr.makeApplication(f000000, new Expr[] {f000958, f001351, f000961, f000983, f000987});
  private static final Expr f001353 = Expr.makeFieldAccess(f001352, "diff");
  private static final Expr f001354 = Expr.makeApplication(f001353, new Expr[] {f000984});
  private static final Expr f001355 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001339),
            new SimpleImmutableEntry<String, Expr>("tail", f001354)
          });
  private static final Expr f001356 = Expr.makeApplication(f000010, new Expr[] {f001342});
  private static final Expr f001357 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001342),
            new SimpleImmutableEntry<String, Expr>("tail", f001356)
          });
  private static final Expr f001358 = Expr.makeLambda("kvss", f001357, f001355);
  private static final Expr f001359 = Expr.makeLambda("a", f000019, f001358);
  private static final Expr f001360 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f000022),
            new SimpleImmutableEntry<String, Expr>("tail", f000817)
          });
  private static final Expr f001361 = Expr.makeLambda("x", f000070, f001360);
  private static final Expr f001362 = Expr.makeLambda("a", f000019, f001361);
  private static final Expr f001363 = Expr.makeLambda("xs", f001227, f001232);
  private static final Expr f001364 = Expr.makeLambda("a", f000019, f001363);
  private static final Expr f001365 = Expr.makeFieldAccess(f001229, "_1");
  private static final Expr f001366 =
      Expr.makeApplication(f000000, new Expr[] {f001012, f001231, f000795, f001017, f000817});
  private static final Expr f001367 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001365),
            new SimpleImmutableEntry<String, Expr>("tail", f001366)
          });
  private static final Expr f001368 = Expr.makeFieldAccess(f001229, "_2");
  private static final Expr f001369 =
      Expr.makeApplication(f000000, new Expr[] {f001012, f001231, f000822, f001023, f000827});
  private static final Expr f001370 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001368),
            new SimpleImmutableEntry<String, Expr>("tail", f001369)
          });
  private static final Expr f001371 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001367),
            new SimpleImmutableEntry<String, Expr>("_2", f001370)
          });
  private static final Expr f001372 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001012),
            new SimpleImmutableEntry<String, Expr>("tail", f001026)
          });
  private static final Expr f001373 = Expr.makeLambda("xs", f001372, f001371);
  private static final Expr f001374 = Expr.makeLambda("b", f000019, f001373);
  private static final Expr f001375 = Expr.makeLambda("a", f000019, f001374);
  private static final Expr f001376 = Expr.makeFieldAccess(f000813, "head");
  private static final Expr f001377 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001229),
            new SimpleImmutableEntry<String, Expr>("_2", f001376)
          });
  private static final Expr f001378 = Expr.makeFieldAccess(f000813, "tail");
  private static final Expr f001379 = Expr.makeApplication(f000842, new Expr[] {f000029, f001378});
  private static final Expr f001380 =
      Expr.makeApplication(f000000, new Expr[] {f001038, f001379, f000822, f001045, f000827});
  private static final Expr f001381 = Expr.makeApplication(f000903, new Expr[] {f000029, f001380});
  private static final Expr f001382 = Expr.makeMerge(f001037, f001381, null);
  private static final Expr f001383 = Expr.makeLambda("rest", f001026, f001382);
  private static final Expr f001384 = Expr.makeLambda("ix", f000841, f001383);
  private static final Expr f001385 =
      Expr.makeApplication(f000000, new Expr[] {f000841, f001276, f001026, f001384, f001051});
  private static final Expr f001386 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("head", f001377),
            new SimpleImmutableEntry<String, Expr>("tail", f001385)
          });
  private static final Expr f001387 = Expr.makeLambda("ys", f001267, f001386);
  private static final Expr f001388 = Expr.makeLambda("b", f000019, f001387);
  private static final Expr f001389 = Expr.makeLambda("xs", f001227, f001388);
  private static final Expr f001390 = Expr.makeLambda("a", f000019, f001389);
  private static final Expr f001391 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Type", f001228),
            new SimpleImmutableEntry<String, Expr>("all", f001236),
            new SimpleImmutableEntry<String, Expr>("any", f001240),
            new SimpleImmutableEntry<String, Expr>("concat", f001253),
            new SimpleImmutableEntry<String, Expr>("concatMap", f001271),
            new SimpleImmutableEntry<String, Expr>("head", f001273),
            new SimpleImmutableEntry<String, Expr>("index", f001288),
            new SimpleImmutableEntry<String, Expr>("indexed", f001299),
            new SimpleImmutableEntry<String, Expr>("last", f001304),
            new SimpleImmutableEntry<String, Expr>("length", f001308),
            new SimpleImmutableEntry<String, Expr>("make", f001314),
            new SimpleImmutableEntry<String, Expr>("map", f001320),
            new SimpleImmutableEntry<String, Expr>("reverse", f001337),
            new SimpleImmutableEntry<String, Expr>("shifted", f001359),
            new SimpleImmutableEntry<String, Expr>("singleton", f001362),
            new SimpleImmutableEntry<String, Expr>("toList", f001364),
            new SimpleImmutableEntry<String, Expr>("unzip", f001375),
            new SimpleImmutableEntry<String, Expr>("zip", f001390)
          });
  private static final Expr f001392 =
      Expr.makeOperatorApplication(Operator.NOT_EQUALS, f000085, f000076);
  private static final Expr f001393 = Expr.makeLambda("n", f000001, f001392);
  private static final Expr f001394 = Expr.makeLambda("m", f000001, f001393);
  private static final Expr f001395 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f000085, f000076);
  private static final Expr f001396 = Expr.makeIdentifier("type", 0);
  private static final Expr f001397 = Expr.makeApplication(f000010, new Expr[] {f001396});
  private static final Expr f001398 = Expr.makeLambda("n", f001397, f001395);
  private static final Expr f001399 = Expr.makeLambda("m", f001397, f001398);
  private static final Expr f001400 = Expr.makeLambda("type", f000019, f001399);
  private static final Expr f001401 = Expr.makeOperatorApplication(Operator.AND, f000085, f000076);
  private static final Expr f001402 = Expr.makeLambda("n", f000001, f001401);
  private static final Expr f001403 = Expr.makeLambda("m", f000001, f001402);
  private static final Expr f001404 =
      Expr.makeOperatorApplication(Operator.TIMES, f000085, f000076);
  private static final Expr f001405 = Expr.makeLambda("n", f000221, f001404);
  private static final Expr f001406 = Expr.makeLambda("m", f000221, f001405);
  private static final Expr f001407 = Expr.makeOperatorApplication(Operator.PLUS, f000085, f000076);
  private static final Expr f001408 = Expr.makeLambda("n", f000221, f001407);
  private static final Expr f001409 = Expr.makeLambda("m", f000221, f001408);
  private static final Expr f001410 =
      Expr.makeTextLiteral(new String[] {"", "", ""}, new Expr[] {f000085, f000076});
  private static final Expr f001411 = Expr.makeLambda("n", f000226, f001410);
  private static final Expr f001412 = Expr.makeLambda("m", f000226, f001411);
  private static final Expr f001413 =
      Expr.makeOperatorApplication(Operator.EQUALS, f000085, f000076);
  private static final Expr f001414 = Expr.makeLambda("n", f000001, f001413);
  private static final Expr f001415 = Expr.makeLambda("m", f000001, f001414);
  private static final Expr f001416 = Expr.makeOperatorApplication(Operator.OR, f000085, f000076);
  private static final Expr f001417 = Expr.makeLambda("n", f000001, f001416);
  private static final Expr f001418 = Expr.makeLambda("m", f000001, f001417);
  private static final Expr f001419 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("!=", f001394),
            new SimpleImmutableEntry<String, Expr>("#", f001400),
            new SimpleImmutableEntry<String, Expr>("&&", f001403),
            new SimpleImmutableEntry<String, Expr>("*", f001406),
            new SimpleImmutableEntry<String, Expr>("+", f001409),
            new SimpleImmutableEntry<String, Expr>("++", f001412),
            new SimpleImmutableEntry<String, Expr>("==", f001415),
            new SimpleImmutableEntry<String, Expr>("||", f001418)
          });
  private static final Expr f001420 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000008),
            new SimpleImmutableEntry<String, Expr>("Some", f000013)
          });
  private static final Expr f001421 = Expr.makeMerge(f001420, f000002, null);
  private static final Expr f001422 = Expr.makeLambda("xs", f001002, f001421);
  private static final Expr f001423 = Expr.makeLambda("f", f000797, f001422);
  private static final Expr f001424 = Expr.makeLambda("a", f000019, f001423);
  private static final Expr f001425 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000014),
            new SimpleImmutableEntry<String, Expr>("Some", f000013)
          });
  private static final Expr f001426 = Expr.makeMerge(f001425, f000002, null);
  private static final Expr f001427 = Expr.makeLambda("xs", f001002, f001426);
  private static final Expr f001428 = Expr.makeLambda("f", f000797, f001427);
  private static final Expr f001429 = Expr.makeLambda("a", f000019, f001428);
  private static final Expr f001430 = Expr.makeIdentifier("build", 0);
  private static final Expr f001431 = Expr.makeApplication(f000218, new Expr[] {f000022});
  private static final Expr f001432 = Expr.makeLambda("x", f000070, f001431);
  private static final Expr f001433 = Expr.makeApplication(f000220, new Expr[] {f000070});
  private static final Expr f001434 =
      Expr.makeApplication(f001430, new Expr[] {f001002, f001432, f001433});
  private static final Expr f001435 = Expr.makeIdentifier("optional", 0);
  private static final Expr f001436 = Expr.makePi("none", f001435, f001435);
  private static final Expr f001437 = Expr.makePi("_", f000070, f001435);
  private static final Expr f001438 = Expr.makePi("some", f001437, f001436);
  private static final Expr f001439 = Expr.makePi("optional", f000019, f001438);
  private static final Expr f001440 = Expr.makeLambda("build", f001439, f001434);
  private static final Expr f001441 = Expr.makeLambda("a", f000019, f001440);
  private static final Expr f001442 = Expr.makeLambda("y", f001002, f000023);
  private static final Expr f001443 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001433),
            new SimpleImmutableEntry<String, Expr>("Some", f001442)
          });
  private static final Expr f001444 = Expr.makeMerge(f001443, f000022, null);
  private static final Expr f001445 = Expr.makeApplication(f000396, new Expr[] {f001002});
  private static final Expr f001446 = Expr.makeLambda("x", f001445, f001444);
  private static final Expr f001447 = Expr.makeLambda("a", f000019, f001446);
  private static final Expr f001448 = Expr.makeApplication(f000220, new Expr[] {f000029});
  private static final Expr f001449 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001448),
            new SimpleImmutableEntry<String, Expr>("Some", f000013)
          });
  private static final Expr f001450 = Expr.makeMerge(f001449, f000836, null);
  private static final Expr f001451 = Expr.makeLambda("o", f001002, f001450);
  private static final Expr f001452 = Expr.makeApplication(f000396, new Expr[] {f000029});
  private static final Expr f001453 = Expr.makePi("_", f000070, f001452);
  private static final Expr f001454 = Expr.makeLambda("f", f001453, f001451);
  private static final Expr f001455 = Expr.makeLambda("b", f000019, f001454);
  private static final Expr f001456 = Expr.makeLambda("a", f000019, f001455);
  private static final Expr f001457 = Expr.makeIdentifier("default", 0);
  private static final Expr f001458 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001457),
            new SimpleImmutableEntry<String, Expr>("Some", f000071)
          });
  private static final Expr f001459 = Expr.makeMerge(f001458, f000836, null);
  private static final Expr f001460 = Expr.makeLambda("o", f001002, f001459);
  private static final Expr f001461 = Expr.makeLambda("default", f000070, f001460);
  private static final Expr f001462 = Expr.makeLambda("a", f000019, f001461);
  private static final Expr f001463 = Expr.makeIf(f000057, f001431, f001433);
  private static final Expr f001464 = Expr.makeLambda("x", f000070, f001463);
  private static final Expr f001465 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001433),
            new SimpleImmutableEntry<String, Expr>("Some", f001464)
          });
  private static final Expr f001466 = Expr.makeMerge(f001465, f000002, null);
  private static final Expr f001467 = Expr.makeLambda("xs", f001002, f001466);
  private static final Expr f001468 = Expr.makeLambda("f", f000797, f001467);
  private static final Expr f001469 = Expr.makeLambda("a", f000019, f001468);
  private static final Expr f001470 = Expr.makeIdentifier("none", 0);
  private static final Expr f001471 = Expr.makeIdentifier("some", 0);
  private static final Expr f001472 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001470),
            new SimpleImmutableEntry<String, Expr>("Some", f001471)
          });
  private static final Expr f001473 = Expr.makeMerge(f001472, f000836, null);
  private static final Expr f001474 = Expr.makeLambda("none", f001435, f001473);
  private static final Expr f001475 = Expr.makeLambda("some", f001437, f001474);
  private static final Expr f001476 = Expr.makeLambda("optional", f000019, f001475);
  private static final Expr f001477 = Expr.makeLambda("o", f001002, f001476);
  private static final Expr f001478 = Expr.makeLambda("a", f000019, f001477);
  private static final Expr f001479 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000004),
            new SimpleImmutableEntry<String, Expr>("Some", f001432)
          });
  private static final Expr f001480 = Expr.makeMerge(f001479, f000003, null);
  private static final Expr f001481 = Expr.makeLambda("r", f001002, f001480);
  private static final Expr f001482 = Expr.makeLambda("l", f001002, f001481);
  private static final Expr f001483 =
      Expr.makeApplication(f000000, new Expr[] {f001002, f000002, f001002, f001482, f001433});
  private static final Expr f001484 = Expr.makeLambda("xs", f001009, f001483);
  private static final Expr f001485 = Expr.makeLambda("a", f000019, f001484);
  private static final Expr f001486 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000003),
            new SimpleImmutableEntry<String, Expr>("Some", f001432)
          });
  private static final Expr f001487 = Expr.makeMerge(f001486, f000004, null);
  private static final Expr f001488 = Expr.makeLambda("r", f001002, f001487);
  private static final Expr f001489 = Expr.makeLambda("l", f001002, f001488);
  private static final Expr f001490 =
      Expr.makeApplication(f000000, new Expr[] {f001002, f000002, f001002, f001489, f001433});
  private static final Expr f001491 = Expr.makeLambda("xs", f001009, f001490);
  private static final Expr f001492 = Expr.makeLambda("a", f000019, f001491);
  private static final Expr f001493 = Expr.makeLambda("_", f000070, f001183);
  private static final Expr f001494 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000984),
            new SimpleImmutableEntry<String, Expr>("Some", f001493)
          });
  private static final Expr f001495 = Expr.makeMerge(f001494, f000002, null);
  private static final Expr f001496 = Expr.makeLambda("xs", f001002, f001495);
  private static final Expr f001497 = Expr.makeLambda("a", f000019, f001496);
  private static final Expr f001498 = Expr.makeApplication(f000218, new Expr[] {f000057});
  private static final Expr f001499 = Expr.makeLambda("x", f000070, f001498);
  private static final Expr f001500 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001448),
            new SimpleImmutableEntry<String, Expr>("Some", f001499)
          });
  private static final Expr f001501 = Expr.makeMerge(f001500, f000836, null);
  private static final Expr f001502 = Expr.makeLambda("o", f001002, f001501);
  private static final Expr f001503 = Expr.makeLambda("f", f000928, f001502);
  private static final Expr f001504 = Expr.makeLambda("b", f000019, f001503);
  private static final Expr f001505 = Expr.makeLambda("a", f000019, f001504);
  private static final Expr f001506 = Expr.makeLambda("_", f000070, f000014);
  private static final Expr f001507 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000008),
            new SimpleImmutableEntry<String, Expr>("Some", f001506)
          });
  private static final Expr f001508 = Expr.makeMerge(f001507, f000002, null);
  private static final Expr f001509 = Expr.makeLambda("xs", f001002, f001508);
  private static final Expr f001510 = Expr.makeLambda("a", f000019, f001509);
  private static final Expr f001511 = Expr.makeMerge(f001004, f000836, null);
  private static final Expr f001512 = Expr.makeLambda("o", f001002, f001511);
  private static final Expr f001513 = Expr.makeLambda("a", f000019, f001512);
  private static final Expr f001514 = Expr.makeApplication(f000218, new Expr[] {f001013});
  private static final Expr f001515 = Expr.makeLambda("x", f001012, f001514);
  private static final Expr f001516 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001433),
            new SimpleImmutableEntry<String, Expr>("Some", f001515)
          });
  private static final Expr f001517 = Expr.makeMerge(f001516, f000002, null);
  private static final Expr f001518 = Expr.makeApplication(f000218, new Expr[] {f001019});
  private static final Expr f001519 = Expr.makeLambda("x", f001012, f001518);
  private static final Expr f001520 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f001448),
            new SimpleImmutableEntry<String, Expr>("Some", f001519)
          });
  private static final Expr f001521 = Expr.makeMerge(f001520, f000002, null);
  private static final Expr f001522 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("_1", f001517),
            new SimpleImmutableEntry<String, Expr>("_2", f001521)
          });
  private static final Expr f001523 = Expr.makeApplication(f000396, new Expr[] {f001012});
  private static final Expr f001524 = Expr.makeLambda("xs", f001523, f001522);
  private static final Expr f001525 = Expr.makeLambda("b", f000019, f001524);
  private static final Expr f001526 = Expr.makeLambda("a", f000019, f001525);
  private static final Expr f001527 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("all", f001424),
            new SimpleImmutableEntry<String, Expr>("any", f001429),
            new SimpleImmutableEntry<String, Expr>("build", f001441),
            new SimpleImmutableEntry<String, Expr>("concat", f001447),
            new SimpleImmutableEntry<String, Expr>("concatMap", f001456),
            new SimpleImmutableEntry<String, Expr>("default", f001462),
            new SimpleImmutableEntry<String, Expr>("filter", f001469),
            new SimpleImmutableEntry<String, Expr>("fold", f001478),
            new SimpleImmutableEntry<String, Expr>("head", f001485),
            new SimpleImmutableEntry<String, Expr>("last", f001492),
            new SimpleImmutableEntry<String, Expr>("length", f001497),
            new SimpleImmutableEntry<String, Expr>("map", f001505),
            new SimpleImmutableEntry<String, Expr>("null", f001510),
            new SimpleImmutableEntry<String, Expr>("toList", f001513),
            new SimpleImmutableEntry<String, Expr>("unzip", f001526)
          });
  private static final Expr f001528 =
      Expr.makeTextLiteral(new String[] {"", "", ""}, new Expr[] {f000022, f000023});
  private static final Expr f001529 = Expr.makeLambda("y", f000226, f001528);
  private static final Expr f001530 = Expr.makeLambda("x", f000226, f001529);
  private static final Expr f001531 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f000002, f000226, f001530, f000647});
  private static final Expr f001532 = Expr.makeLambda("xs", f000357, f001531);
  private static final Expr f001533 =
      Expr.makeTextLiteral(new String[] {"", "", ""}, new Expr[] {f000057, f000023});
  private static final Expr f001534 = Expr.makeLambda("y", f000226, f001533);
  private static final Expr f001535 = Expr.makeLambda("x", f000070, f001534);
  private static final Expr f001536 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f000002, f000226, f001535, f000647});
  private static final Expr f001537 = Expr.makeLambda("xs", f000795, f001536);
  private static final Expr f001538 = Expr.makePi("_", f000070, f000226);
  private static final Expr f001539 = Expr.makeLambda("f", f001538, f001537);
  private static final Expr f001540 = Expr.makeLambda("a", f000019, f001539);
  private static final Expr f001541 = Expr.makeIdentifier("elements", 0);
  private static final Expr f001542 = Expr.makeApplication(f000655, new Expr[] {f000057});
  private static final Expr f001543 = Expr.makeIdentifier("separator", 0);
  private static final Expr f001544 =
      Expr.makeTextLiteral(new String[] {"", "", "", ""}, new Expr[] {f000057, f001543, f000651});
  private static final Expr f001545 = Expr.makeApplication(f000655, new Expr[] {f001544});
  private static final Expr f001546 = Expr.makeLambda("result", f000226, f001545);
  private static final Expr f001547 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f001542),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001546)
          });
  private static final Expr f001548 = Expr.makeMerge(f001547, f000662, null);
  private static final Expr f001549 = Expr.makeLambda("status", f000654, f001548);
  private static final Expr f001550 = Expr.makeLambda("x", f000070, f001549);
  private static final Expr f001551 =
      Expr.makeApplication(f000000, new Expr[] {f000070, f001541, f000654, f001550, f000666});
  private static final Expr f001552 = Expr.makeMerge(f000653, f001551, null);
  private static final Expr f001553 = Expr.makeLambda("elements", f000795, f001552);
  private static final Expr f001554 = Expr.makeLambda("f", f001538, f001553);
  private static final Expr f001555 = Expr.makeLambda("a", f000019, f001554);
  private static final Expr f001556 = Expr.makeLambda("separator", f000226, f001555);
  private static final Expr f001557 = Expr.makeIdentifier("element", 0);
  private static final Expr f001558 = Expr.makeApplication(f000655, new Expr[] {f001557});
  private static final Expr f001559 =
      Expr.makeTextLiteral(new String[] {"", "", "", ""}, new Expr[] {f001557, f001543, f000651});
  private static final Expr f001560 = Expr.makeApplication(f000655, new Expr[] {f001559});
  private static final Expr f001561 = Expr.makeLambda("result", f000226, f001560);
  private static final Expr f001562 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Empty", f001558),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001561)
          });
  private static final Expr f001563 = Expr.makeMerge(f001562, f000662, null);
  private static final Expr f001564 = Expr.makeLambda("status", f000654, f001563);
  private static final Expr f001565 = Expr.makeLambda("element", f000226, f001564);
  private static final Expr f001566 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f001541, f000654, f001565, f000666});
  private static final Expr f001567 = Expr.makeMerge(f000653, f001566, null);
  private static final Expr f001568 = Expr.makeLambda("elements", f000357, f001567);
  private static final Expr f001569 = Expr.makeLambda("separator", f000226, f001568);
  private static final Expr f001570 = Expr.makeIdentifier("t", 0);
  private static final Expr f001571 = Expr.makeLambda("t", f000226, f001570);
  private static final Expr f001572 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000647),
            new SimpleImmutableEntry<String, Expr>("Some", f001571)
          });
  private static final Expr f001573 = Expr.makeMerge(f001572, f000836, null);
  private static final Expr f001574 = Expr.makeApplication(f000396, new Expr[] {f000226});
  private static final Expr f001575 = Expr.makeLambda("o", f001574, f001573);
  private static final Expr f001576 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("None", f000647),
            new SimpleImmutableEntry<String, Expr>("Some", f000013)
          });
  private static final Expr f001577 = Expr.makeMerge(f001576, f000836, null);
  private static final Expr f001578 = Expr.makeLambda("o", f001002, f001577);
  private static final Expr f001579 = Expr.makeLambda("f", f001538, f001578);
  private static final Expr f001580 = Expr.makeLambda("a", f000019, f001579);
  private static final Expr f001581 = Expr.makePi("_", f000226, f000226);
  private static final Expr f001582 = Expr.makeTextLiteral("A");
  private static final Expr f001583 = Expr.makeTextLiteral("a");
  private static final Expr f001584 = Expr.makeApplication(f000528, new Expr[] {f001582, f001583});
  private static final Expr f001585 = Expr.makeTextLiteral("B");
  private static final Expr f001586 = Expr.makeTextLiteral("b");
  private static final Expr f001587 = Expr.makeApplication(f000528, new Expr[] {f001585, f001586});
  private static final Expr f001588 = Expr.makeTextLiteral("C");
  private static final Expr f001589 = Expr.makeTextLiteral("c");
  private static final Expr f001590 = Expr.makeApplication(f000528, new Expr[] {f001588, f001589});
  private static final Expr f001591 = Expr.makeTextLiteral("D");
  private static final Expr f001592 = Expr.makeTextLiteral("d");
  private static final Expr f001593 = Expr.makeApplication(f000528, new Expr[] {f001591, f001592});
  private static final Expr f001594 = Expr.makeTextLiteral("E");
  private static final Expr f001595 = Expr.makeTextLiteral("e");
  private static final Expr f001596 = Expr.makeApplication(f000528, new Expr[] {f001594, f001595});
  private static final Expr f001597 = Expr.makeTextLiteral("F");
  private static final Expr f001598 = Expr.makeTextLiteral("f");
  private static final Expr f001599 = Expr.makeApplication(f000528, new Expr[] {f001597, f001598});
  private static final Expr f001600 = Expr.makeTextLiteral("G");
  private static final Expr f001601 = Expr.makeTextLiteral("g");
  private static final Expr f001602 = Expr.makeApplication(f000528, new Expr[] {f001600, f001601});
  private static final Expr f001603 = Expr.makeTextLiteral("H");
  private static final Expr f001604 = Expr.makeTextLiteral("h");
  private static final Expr f001605 = Expr.makeApplication(f000528, new Expr[] {f001603, f001604});
  private static final Expr f001606 = Expr.makeTextLiteral("I");
  private static final Expr f001607 = Expr.makeTextLiteral("i");
  private static final Expr f001608 = Expr.makeApplication(f000528, new Expr[] {f001606, f001607});
  private static final Expr f001609 = Expr.makeTextLiteral("J");
  private static final Expr f001610 = Expr.makeTextLiteral("j");
  private static final Expr f001611 = Expr.makeApplication(f000528, new Expr[] {f001609, f001610});
  private static final Expr f001612 = Expr.makeTextLiteral("K");
  private static final Expr f001613 = Expr.makeTextLiteral("k");
  private static final Expr f001614 = Expr.makeApplication(f000528, new Expr[] {f001612, f001613});
  private static final Expr f001615 = Expr.makeTextLiteral("L");
  private static final Expr f001616 = Expr.makeTextLiteral("l");
  private static final Expr f001617 = Expr.makeApplication(f000528, new Expr[] {f001615, f001616});
  private static final Expr f001618 = Expr.makeTextLiteral("M");
  private static final Expr f001619 = Expr.makeTextLiteral("m");
  private static final Expr f001620 = Expr.makeApplication(f000528, new Expr[] {f001618, f001619});
  private static final Expr f001621 = Expr.makeTextLiteral("N");
  private static final Expr f001622 = Expr.makeTextLiteral("n");
  private static final Expr f001623 = Expr.makeApplication(f000528, new Expr[] {f001621, f001622});
  private static final Expr f001624 = Expr.makeTextLiteral("O");
  private static final Expr f001625 = Expr.makeTextLiteral("o");
  private static final Expr f001626 = Expr.makeApplication(f000528, new Expr[] {f001624, f001625});
  private static final Expr f001627 = Expr.makeTextLiteral("P");
  private static final Expr f001628 = Expr.makeTextLiteral("p");
  private static final Expr f001629 = Expr.makeApplication(f000528, new Expr[] {f001627, f001628});
  private static final Expr f001630 = Expr.makeTextLiteral("Q");
  private static final Expr f001631 = Expr.makeTextLiteral("q");
  private static final Expr f001632 = Expr.makeApplication(f000528, new Expr[] {f001630, f001631});
  private static final Expr f001633 = Expr.makeTextLiteral("R");
  private static final Expr f001634 = Expr.makeTextLiteral("r");
  private static final Expr f001635 = Expr.makeApplication(f000528, new Expr[] {f001633, f001634});
  private static final Expr f001636 = Expr.makeTextLiteral("S");
  private static final Expr f001637 = Expr.makeTextLiteral("s");
  private static final Expr f001638 = Expr.makeApplication(f000528, new Expr[] {f001636, f001637});
  private static final Expr f001639 = Expr.makeTextLiteral("T");
  private static final Expr f001640 = Expr.makeTextLiteral("t");
  private static final Expr f001641 = Expr.makeApplication(f000528, new Expr[] {f001639, f001640});
  private static final Expr f001642 = Expr.makeTextLiteral("U");
  private static final Expr f001643 = Expr.makeTextLiteral("u");
  private static final Expr f001644 = Expr.makeApplication(f000528, new Expr[] {f001642, f001643});
  private static final Expr f001645 = Expr.makeTextLiteral("V");
  private static final Expr f001646 = Expr.makeTextLiteral("v");
  private static final Expr f001647 = Expr.makeApplication(f000528, new Expr[] {f001645, f001646});
  private static final Expr f001648 = Expr.makeTextLiteral("W");
  private static final Expr f001649 = Expr.makeTextLiteral("w");
  private static final Expr f001650 = Expr.makeApplication(f000528, new Expr[] {f001648, f001649});
  private static final Expr f001651 = Expr.makeTextLiteral("X");
  private static final Expr f001652 = Expr.makeTextLiteral("x");
  private static final Expr f001653 = Expr.makeApplication(f000528, new Expr[] {f001651, f001652});
  private static final Expr f001654 = Expr.makeTextLiteral("Y");
  private static final Expr f001655 = Expr.makeTextLiteral("y");
  private static final Expr f001656 = Expr.makeApplication(f000528, new Expr[] {f001654, f001655});
  private static final Expr f001657 = Expr.makeTextLiteral("Z");
  private static final Expr f001658 = Expr.makeTextLiteral("z");
  private static final Expr f001659 = Expr.makeApplication(f000528, new Expr[] {f001657, f001658});
  private static final Expr f001660 =
      Expr.makeNonEmptyListLiteral(
          new Expr[] {
            f001584, f001587, f001590, f001593, f001596, f001599, f001602, f001605, f001608,
            f001611, f001614, f001617, f001620, f001623, f001626, f001629, f001632, f001635,
            f001638, f001641, f001644, f001647, f001650, f001653, f001656, f001659
          });
  private static final Expr f001661 = Expr.makeIdentifier("replacement", 0);
  private static final Expr f001662 = Expr.makeLambda("replacement", f001581, f001661);
  private static final Expr f001663 =
      Expr.makeApplication(f000000, new Expr[] {f001581, f001660, f000226, f001662});
  private static final Expr f001664 = Expr.makeIdentifier("num", 0);
  private static final Expr f001665 = Expr.makeIdentifier("text", 0);
  private static final Expr f001666 = Expr.makeNonEmptyListLiteral(new Expr[] {f001665});
  private static final Expr f001667 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001666, f000248);
  private static final Expr f001668 = Expr.makeLambda("as", f000357, f001667);
  private static final Expr f001669 =
      Expr.makeApplication(f000884, new Expr[] {f001664, f000357, f001668, f000360});
  private static final Expr f001670 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f001669, f000226, f001530, f000647});
  private static final Expr f001671 = Expr.makeLambda("text", f000226, f001670);
  private static final Expr f001672 = Expr.makeLambda("num", f000221, f001671);
  private static final Expr f001673 = Expr.makeTextLiteral(" ");
  private static final Expr f001674 = Expr.makeNonEmptyListLiteral(new Expr[] {f001673});
  private static final Expr f001675 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001674, f000248);
  private static final Expr f001676 = Expr.makeLambda("as", f000357, f001675);
  private static final Expr f001677 =
      Expr.makeApplication(f000884, new Expr[] {f000070, f000357, f001676, f000360});
  private static final Expr f001678 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f001677, f000226, f001530, f000647});
  private static final Expr f001679 = Expr.makeLambda("a", f000221, f001678);
  private static final Expr f001680 = Expr.makeApplication(f000528, new Expr[] {f001583, f001582});
  private static final Expr f001681 = Expr.makeApplication(f000528, new Expr[] {f001586, f001585});
  private static final Expr f001682 = Expr.makeApplication(f000528, new Expr[] {f001589, f001588});
  private static final Expr f001683 = Expr.makeApplication(f000528, new Expr[] {f001592, f001591});
  private static final Expr f001684 = Expr.makeApplication(f000528, new Expr[] {f001595, f001594});
  private static final Expr f001685 = Expr.makeApplication(f000528, new Expr[] {f001598, f001597});
  private static final Expr f001686 = Expr.makeApplication(f000528, new Expr[] {f001601, f001600});
  private static final Expr f001687 = Expr.makeApplication(f000528, new Expr[] {f001604, f001603});
  private static final Expr f001688 = Expr.makeApplication(f000528, new Expr[] {f001607, f001606});
  private static final Expr f001689 = Expr.makeApplication(f000528, new Expr[] {f001610, f001609});
  private static final Expr f001690 = Expr.makeApplication(f000528, new Expr[] {f001613, f001612});
  private static final Expr f001691 = Expr.makeApplication(f000528, new Expr[] {f001616, f001615});
  private static final Expr f001692 = Expr.makeApplication(f000528, new Expr[] {f001619, f001618});
  private static final Expr f001693 = Expr.makeApplication(f000528, new Expr[] {f001622, f001621});
  private static final Expr f001694 = Expr.makeApplication(f000528, new Expr[] {f001625, f001624});
  private static final Expr f001695 = Expr.makeApplication(f000528, new Expr[] {f001628, f001627});
  private static final Expr f001696 = Expr.makeApplication(f000528, new Expr[] {f001631, f001630});
  private static final Expr f001697 = Expr.makeApplication(f000528, new Expr[] {f001634, f001633});
  private static final Expr f001698 = Expr.makeApplication(f000528, new Expr[] {f001637, f001636});
  private static final Expr f001699 = Expr.makeApplication(f000528, new Expr[] {f001640, f001639});
  private static final Expr f001700 = Expr.makeApplication(f000528, new Expr[] {f001643, f001642});
  private static final Expr f001701 = Expr.makeApplication(f000528, new Expr[] {f001646, f001645});
  private static final Expr f001702 = Expr.makeApplication(f000528, new Expr[] {f001649, f001648});
  private static final Expr f001703 = Expr.makeApplication(f000528, new Expr[] {f001652, f001651});
  private static final Expr f001704 = Expr.makeApplication(f000528, new Expr[] {f001655, f001654});
  private static final Expr f001705 = Expr.makeApplication(f000528, new Expr[] {f001658, f001657});
  private static final Expr f001706 =
      Expr.makeNonEmptyListLiteral(
          new Expr[] {
            f001680, f001681, f001682, f001683, f001684, f001685, f001686, f001687, f001688,
            f001689, f001690, f001691, f001692, f001693, f001694, f001695, f001696, f001697,
            f001698, f001699, f001700, f001701, f001702, f001703, f001704, f001705
          });
  private static final Expr f001707 =
      Expr.makeApplication(f000000, new Expr[] {f001581, f001706, f000226, f001662});
  private static final Expr f001708 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("concat", f001532),
            new SimpleImmutableEntry<String, Expr>("concatMap", f001540),
            new SimpleImmutableEntry<String, Expr>("concatMapSep", f001556),
            new SimpleImmutableEntry<String, Expr>("concatSep", f001569),
            new SimpleImmutableEntry<String, Expr>("default", f001575),
            new SimpleImmutableEntry<String, Expr>("defaultMap", f001580),
            new SimpleImmutableEntry<String, Expr>("lowerASCII", f001663),
            new SimpleImmutableEntry<String, Expr>("replace", f000528),
            new SimpleImmutableEntry<String, Expr>("replicate", f001672),
            new SimpleImmutableEntry<String, Expr>("show", f000682),
            new SimpleImmutableEntry<String, Expr>("spaces", f001679),
            new SimpleImmutableEntry<String, Expr>("upperASCII", f001707)
          });
  private static final Expr f001709 = Expr.makeIdentifier("XML", 0);
  private static final Expr f001710 = Expr.makeApplication(f000010, new Expr[] {f001709});
  private static final Expr f001711 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f000696),
            new SimpleImmutableEntry<String, Expr>("content", f001710),
            new SimpleImmutableEntry<String, Expr>("name", f000226)
          });
  private static final Expr f001712 = Expr.makePi("_", f001711, f001709);
  private static final Expr f001713 = Expr.makePi("_", f000226, f001709);
  private static final Expr f001714 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("element", f001712),
            new SimpleImmutableEntry<String, Expr>("text", f001713)
          });
  private static final Expr f001715 = Expr.makePi("xml", f001714, f001709);
  private static final Expr f001716 = Expr.makePi("XML", f000019, f001715);
  private static final Expr f001717 = Expr.makeIdentifier("xml", 0);
  private static final Expr f001718 = Expr.makeFieldAccess(f001717, "element");
  private static final Expr f001719 = Expr.makeIdentifier("elem", 0);
  private static final Expr f001720 = Expr.makeFieldAccess(f001719, "attributes");
  private static final Expr f001721 = Expr.makeFieldAccess(f001719, "content");
  private static final Expr f001722 = Expr.makeApplication(f000022, new Expr[] {f001709, f001717});
  private static final Expr f001723 = Expr.makeNonEmptyListLiteral(new Expr[] {f001722});
  private static final Expr f001724 =
      Expr.makeOperatorApplication(Operator.LIST_APPEND, f001723, f000248);
  private static final Expr f001725 = Expr.makeLambda("as", f001710, f001724);
  private static final Expr f001726 = Expr.makeLambda("x", f001716, f001725);
  private static final Expr f001727 = Expr.makeEmptyListLiteral(f001710);
  private static final Expr f001728 =
      Expr.makeApplication(f000000, new Expr[] {f001716, f001721, f001710, f001726, f001727});
  private static final Expr f001729 = Expr.makeFieldAccess(f001719, "name");
  private static final Expr f001730 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001720),
            new SimpleImmutableEntry<String, Expr>("content", f001728),
            new SimpleImmutableEntry<String, Expr>("name", f001729)
          });
  private static final Expr f001731 = Expr.makeApplication(f001718, new Expr[] {f001730});
  private static final Expr f001732 = Expr.makeLambda("xml", f001714, f001731);
  private static final Expr f001733 = Expr.makeLambda("XML", f000019, f001732);
  private static final Expr f001734 = Expr.makeApplication(f000010, new Expr[] {f001716});
  private static final Expr f001735 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f000696),
            new SimpleImmutableEntry<String, Expr>("content", f001734),
            new SimpleImmutableEntry<String, Expr>("name", f000226)
          });
  private static final Expr f001736 = Expr.makeLambda("elem", f001735, f001733);
  private static final Expr f001737 = Expr.makeEmptyListLiteral(f000696);
  private static final Expr f001738 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f001720),
            new SimpleImmutableEntry<String, Expr>("content", f001727),
            new SimpleImmutableEntry<String, Expr>("name", f001729)
          });
  private static final Expr f001739 = Expr.makeApplication(f001718, new Expr[] {f001738});
  private static final Expr f001740 = Expr.makeLambda("xml", f001714, f001739);
  private static final Expr f001741 = Expr.makeLambda("XML", f000019, f001740);
  private static final Expr f001742 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f000696),
            new SimpleImmutableEntry<String, Expr>("name", f000226)
          });
  private static final Expr f001743 = Expr.makeLambda("elem", f001742, f001741);
  private static final Expr f001744 = Expr.makeTextLiteral("&quot;");
  private static final Expr f001745 = Expr.makeTextLiteral("'");
  private static final Expr f001746 = Expr.makeTextLiteral("&apos;");
  private static final Expr f001747 = Expr.makeTextLiteral("<");
  private static final Expr f001748 = Expr.makeTextLiteral("&lt;");
  private static final Expr f001749 = Expr.makeTextLiteral("&");
  private static final Expr f001750 = Expr.makeTextLiteral("&amp;");
  private static final Expr f001751 =
      Expr.makeApplication(f000528, new Expr[] {f001749, f001750, f000294});
  private static final Expr f001752 =
      Expr.makeApplication(f000528, new Expr[] {f001747, f001748, f001751});
  private static final Expr f001753 =
      Expr.makeApplication(f000528, new Expr[] {f001745, f001746, f001752});
  private static final Expr f001754 =
      Expr.makeApplication(f000528, new Expr[] {f000529, f001744, f001753});
  private static final Expr f001755 =
      Expr.makeTextLiteral(
          new String[] {" ", "=\"", "\"", ""}, new Expr[] {f000293, f001754, f000023});
  private static final Expr f001756 = Expr.makeLambda("y", f000226, f001755);
  private static final Expr f001757 = Expr.makeLambda("x", f000681, f001756);
  private static final Expr f001758 =
      Expr.makeApplication(f000000, new Expr[] {f000681, f001720, f000226, f001757, f000647});
  private static final Expr f001759 = Expr.makeApplication(f000921, new Expr[] {f000226, f001721});
  private static final Expr f001760 = Expr.makeApplication(f000074, new Expr[] {f001759});
  private static final Expr f001761 = Expr.makeTextLiteral("/>");
  private static final Expr f001762 =
      Expr.makeApplication(f000000, new Expr[] {f000226, f001721, f000226, f001530, f000647});
  private static final Expr f001763 =
      Expr.makeTextLiteral(new String[] {">", "</", ">"}, new Expr[] {f001762, f001729});
  private static final Expr f001764 = Expr.makeIf(f001760, f001761, f001763);
  private static final Expr f001765 =
      Expr.makeTextLiteral(new String[] {"<", "", "", ""}, new Expr[] {f001729, f001758, f001764});
  private static final Expr f001766 =
      Expr.makeRecordType(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("attributes", f000696),
            new SimpleImmutableEntry<String, Expr>("content", f000357),
            new SimpleImmutableEntry<String, Expr>("name", f000226)
          });
  private static final Expr f001767 = Expr.makeLambda("elem", f001766, f001765);
  private static final Expr f001768 = Expr.makeTextLiteral(">");
  private static final Expr f001769 = Expr.makeTextLiteral("&gt;");
  private static final Expr f001770 =
      Expr.makeApplication(f000528, new Expr[] {f001749, f001750, f001665});
  private static final Expr f001771 =
      Expr.makeApplication(f000528, new Expr[] {f001747, f001748, f001770});
  private static final Expr f001772 =
      Expr.makeApplication(f000528, new Expr[] {f001768, f001769, f001771});
  private static final Expr f001773 = Expr.makeLambda("text", f000226, f001772);
  private static final Expr f001774 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("element", f001767),
            new SimpleImmutableEntry<String, Expr>("text", f001773)
          });
  private static final Expr f001775 = Expr.makeApplication(f000022, new Expr[] {f000226, f001774});
  private static final Expr f001776 = Expr.makeLambda("x", f001716, f001775);
  private static final Expr f001777 = Expr.makeFieldAccess(f001717, "text");
  private static final Expr f001778 = Expr.makeIdentifier("d", 0);
  private static final Expr f001779 = Expr.makeApplication(f001777, new Expr[] {f001778});
  private static final Expr f001780 = Expr.makeLambda("xml", f001714, f001779);
  private static final Expr f001781 = Expr.makeLambda("XML", f000019, f001780);
  private static final Expr f001782 = Expr.makeLambda("d", f000226, f001781);
  private static final Expr f001783 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Type", f001716),
            new SimpleImmutableEntry<String, Expr>("attribute", f000278),
            new SimpleImmutableEntry<String, Expr>("element", f001736),
            new SimpleImmutableEntry<String, Expr>("emptyAttributes", f001737),
            new SimpleImmutableEntry<String, Expr>("leaf", f001743),
            new SimpleImmutableEntry<String, Expr>("render", f001776),
            new SimpleImmutableEntry<String, Expr>("text", f001782)
          });
  private static final Expr f001784 =
      Expr.makeRecordLiteral(
          new Entry[] {
            new SimpleImmutableEntry<String, Expr>("Bool", f000053),
            new SimpleImmutableEntry<String, Expr>("Double", f000055),
            new SimpleImmutableEntry<String, Expr>("Function", f000073),
            new SimpleImmutableEntry<String, Expr>("Integer", f000225),
            new SimpleImmutableEntry<String, Expr>("JSON", f000790),
            new SimpleImmutableEntry<String, Expr>("List", f001057),
            new SimpleImmutableEntry<String, Expr>("Location", f001059),
            new SimpleImmutableEntry<String, Expr>("Map", f001123),
            new SimpleImmutableEntry<String, Expr>("Monoid", f001126),
            new SimpleImmutableEntry<String, Expr>("Natural", f001226),
            new SimpleImmutableEntry<String, Expr>("NonEmpty", f001391),
            new SimpleImmutableEntry<String, Expr>("Operator", f001419),
            new SimpleImmutableEntry<String, Expr>("Optional", f001527),
            new SimpleImmutableEntry<String, Expr>("Text", f001708),
            new SimpleImmutableEntry<String, Expr>("XML", f001783)
          });

  public static final Expr instance = f001784;
}
