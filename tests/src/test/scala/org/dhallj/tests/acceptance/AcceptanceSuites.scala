package org.dhallj.tests.acceptance

class NormalizationSimpleSuite extends NormalizationUSuite("normalization/success/simple")
class NormalizationRegressionSuite extends NormalizationSuite("normalization/success/regression")
class NormalizationUnitSuite extends NormalizationUSuite("normalization/success/unit")
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

class TypeCheckingSimpleSuite extends CachingTypeCheckingSuite("type-inference/success/simple")
class TypeCheckingUnitSuite extends CachingTypeCheckingSuite("type-inference/success/unit")
class TypeCheckingRegressionSuite extends TypeCheckingSuite("type-inference/success/regression")
class TypeCheckingOtherSuite extends TypeCheckingSuite("type-inference/success") {
  //TODO prelude is WAY too slow
  override def ignored = Set("prelude")

  override def slow = Set("prelude")
}
class TypeCheckingFailureUnitSuite extends TypeCheckingFailureSuite("type-inference/failure/unit")

class ParsingUnitSuite extends ParsingSuite("parser/success/unit")
class ParsingOtherSuite extends ParsingSuite("parser/success")

class ParsingFailureUnitSuite extends ParsingFailureSuite("parser/failure/unit")
class ParsingFailureSpacingSuite extends ParsingFailureSuite("parser/failure/spacing")
class ParsingFailureOtherSuite extends ParsingFailureSuite("parser/failure")

class BinaryDecodingUnitSuite extends BinaryDecodingSuite("binary-decode/success/unit")
class BinaryDecodingImportsUnitSuite extends BinaryDecodingSuite("binary-decode/success/unit/imports")
class BinaryDecodingFailureUnitSuite extends BinaryDecodingFailureSuite("binary-decode/failure/unit")

class ImportResolutionSuccessSuite extends ImportResolutionSuite("import/success")
class ImportResolutionSuccessUnitSuite extends ImportResolutionSuite("import/success/unit")
class ImportResolutionSuccessUnitAsLocationSuite extends ImportResolutionSuite("import/success/unit/asLocation")
