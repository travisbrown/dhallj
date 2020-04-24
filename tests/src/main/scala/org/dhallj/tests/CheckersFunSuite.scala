package org.dhallj.tests

import munit.{FunSuite, Location}
import org.scalacheck.{Arbitrary, Prop, Shrink, Test}
import org.scalacheck.util.Pretty

class CheckersFunSuite(
  defaultTestConfig: Test.Parameters = Test.Parameters.default,
  defaultPrettyConfig: Pretty.Params = Pretty.defaultParams
) extends FunSuite {
  def check(
    prop: Prop,
    testConfig: Test.Parameters = defaultTestConfig,
    prettyConfig: Pretty.Params = defaultPrettyConfig
  )(implicit loc: Location): Unit = {
    val result = Test.check(testConfig, prop)
    val reason = Pretty.prettyTestRes(result)(prettyConfig)
    assert(result.passed, reason)
  }

  def check1[A, P](
    f: A => P,
    testConfig: Test.Parameters = defaultTestConfig,
    prettyConfig: Pretty.Params = defaultPrettyConfig
  )(implicit loc: Location, tp: P => Prop, AA: Arbitrary[A], AS: Shrink[A], AP: A => Pretty): Unit =
    check(Prop.forAll(f)(tp, AA, AS, AP), testConfig, prettyConfig)

  def testAll1[A, P](name: String)(
    f: A => P,
    testConfig: Test.Parameters = defaultTestConfig,
    prettyConfig: Pretty.Params = defaultPrettyConfig
  )(implicit loc: Location, tp: P => Prop, AA: Arbitrary[A], AS: Shrink[A], AP: A => Pretty): Unit =
    test(name)(check1(f, testConfig, prettyConfig))
}
