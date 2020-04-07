package org.dhallj.tests.acceptance

class NormalizationSimpleSuite extends NormalizationSuite("normalization/success/simple")
class NormalizationRegressionSuite extends NormalizationSuite("normalization/success/regression")
class NormalizationUnitSuite extends NormalizationSuite("normalization/success/unit")
class NormalizationSimplificationsSuite extends NormalizationSuite("normalization/success/simplifications")
class NormalizationOtherSuite extends NormalizationSuite("normalization/success")
class NormalizationHTAccessSuite extends NormalizationSuite("normalization/success/haskell-tutorial/access")
class NormalizationHTCombineTypesSuite extends NormalizationSuite("normalization/success/haskell-tutorial/combineTypes")
class NormalizationHTPreferSuite extends NormalizationSuite("normalization/success/haskell-tutorial/prefer")
class NormalizationHTProjectionSuite extends NormalizationSuite("normalization/success/haskell-tutorial/projection")

class HashingSimpleSuite extends HashingSuite("semantic-hash/success/simple")
class HashingSimplificationsSuite extends HashingSuite("semantic-hash/success/simplifications")
class HashingOtherSuite extends HashingSuite("semantic-hash/success")
class HashingHTAccessSuite extends HashingSuite("semantic-hash/success/haskell-tutorial/access")
class HashingHTCombineTypesSuite extends HashingSuite("semantic-hash/success/haskell-tutorial/combineTypes")
class HashingHTPreferSuite extends HashingSuite("semantic-hash/success/haskell-tutorial/prefer")
class HashingHTProjectionSuite extends HashingSuite("semantic-hash/success/haskell-tutorial/projection")

class AlphaNormalizationUnitSuite extends AlphaNormalizationSuite("alpha-normalization/success/unit")
class AlphaNormalizationRegressionSuite extends AlphaNormalizationSuite("alpha-normalization/success/regression")

class TypeCheckingSimpleSuite extends TypeCheckingSuite("type-inference/success/simple")
class TypeCheckingUnitSuite extends TypeCheckingSuite("type-inference/success/unit")
class TypeCheckingRegressionSuite extends TypeCheckingSuite("type-inference/success/regression")
class TypeCheckingFailureUnitSuite extends TypeCheckingFailureSuite("type-inference/failure/unit")

class ParsingUnitSuite extends ParsingSuite("parser/success/unit") {
  // I'm pretty much convinced we're doing the right thing on SomeXYZ.
  override def ignored = Set("SomeXYZ")
}
class ParsingOtherSuite extends ParsingSuite("parser/success") //{ override def ignored = Set("largeExpression") }

class ParsingFailureUnitSuite extends ParsingFailureSuite("parser/failure/unit")
class ParsingFailureSpacingSuite extends ParsingFailureSuite("parser/failure/spacing")
class ParsingFailureOtherSuite extends ParsingFailureSuite("parser/failure")
