package org.dhallj.testing

import org.scalacheck.{Arbitrary, Gen}

package object instances extends ArbitraryInstances {
  def genNameString: Gen[String] = Gen.oneOf(Gen.alphaStr, Gen.asciiPrintableStr)
  def genTextString: Gen[String] = Arbitrary.arbitrary[String]
}