package org.dhallj.tests.acceptance

class NormalizationSimpleSuite extends NormalizationUSuite("normalization/success/simple")
class NormalizationRegressionSuite extends NormalizationSuite("normalization/success/regression")
class NormalizationUnitSuite extends NormalizationUSuite("normalization/success/unit")
class NormalizationSimplificationsSuite extends NormalizationSuite("normalization/success/simplifications")
class NormalizationOtherSuite extends NormalizationSuite("normalization/success")
class NormalizationHaskellTutorialSuite extends NormalizationSuite("normalization/success/haskell-tutorial", true)

class HashingSimpleSuite extends HashingSuite("semantic-hash/success/simple")
class HashingSimplificationsSuite extends HashingSuite("semantic-hash/success/simplifications")
class HashingOtherSuite extends HashingSuite("semantic-hash/success")
class HashingHaskellTutorialSuite extends HashingSuite("semantic-hash/success/haskell-tutorial", true)
class HashingPreludeSuite extends HashingSuite("semantic-hash/success/prelude", true)

class AlphaNormalizationUnitSuite extends AlphaNormalizationSuite("alpha-normalization/success/unit")
class AlphaNormalizationRegressionSuite extends AlphaNormalizationSuite("alpha-normalization/success/regression")

class TypeCheckingSimpleSuite extends ParsingTypeCheckingSuite("type-inference/success/simple")
class TypeCheckingUnitSuite extends ParsingTypeCheckingSuite("type-inference/success/unit")
class TypeCheckingRegressionSuite extends TypeCheckingSuite("type-inference/success/regression")
class TypeCheckingOtherSuite extends TypeCheckingSuite("type-inference/success") {
  override def slow = Set("prelude")
  // Depends on http://csrng.net/, which is rate-limited (and also currently entirely down).
  override def ignored = Set("CacheImports", "CacheImportsCanonicalize")
}
class TypeCheckingFailureUnitSuite extends TypeCheckingFailureSuite("type-inference/failure/unit")
class TypeCheckingPreludeSuite extends TypeCheckingSuite("type-inference/success/prelude", true)

class ParsingUnitSuite extends ParsingSuite("parser/success/unit")
class ParsingTextSuite extends ParsingSuite("parser/success/text")
class ParsingOtherSuite extends ParsingSuite("parser/success")

class ParsingFailureUnitSuite extends ParsingFailureSuite("parser/failure/unit")
class ParsingFailureSpacingSuite extends ParsingFailureSuite("parser/failure/spacing")
class ParsingFailureOtherSuite extends ParsingFailureSuite("parser/failure")

class BinaryDecodingUnitSuite extends BinaryDecodingSuite("binary-decode/success/unit")
class BinaryDecodingImportsUnitSuite extends BinaryDecodingSuite("binary-decode/success/unit/imports")
class BinaryDecodingFailureUnitSuite extends BinaryDecodingFailureSuite("binary-decode/failure/unit")

class ImportResolutionSuccessSuite extends ImportResolutionSuite("import/success")
class ImportResolutionSuccessUnitSuite extends ImportResolutionSuite("import/success/unit") {
  // TODO: fix this bug
  override def ignored = Set("DontCacheIfHash")
}
class ImportResolutionSuccessUnitAsLocationSuite extends ImportResolutionSuite("import/success/unit/asLocation")
