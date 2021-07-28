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
  // Temporary workaround awaiting dhall-lang#1198 (prelude only).
  override def ignored = Set("CacheImports", "CacheImportsCanonicalize", "prelude")
}
class TypeCheckingFailureUnitSuite extends TypeCheckingFailureSuite("type-inference/failure/unit")
class TypeCheckingPreludeSuite extends TypeCheckingSuite("type-inference/success/prelude", true) {
  // Temporary workaround awaiting dhall-lang#1198.
  override def ignored = _.startsWith("Monoid/")
}

class ParsingUnitSuite extends ParsingSuite("parser/success/unit")
class ParsingTextSuite extends ParsingSuite("parser/success/text")

class ParsingTimeSuite extends ParsingSuite("parser/success/time")
class ParsingOtherSuite extends ParsingSuite("parser/success")

class ParsingFailureUnitSuite extends ParsingFailureSuite("parser/failure/unit") {
  // TODO: Fix these WithPrecedenceN failures; these are known bugs.
  override def ignored = Set("WithPrecedence2", "WithPrecedence3")
}
class ParsingFailureSpacingSuite extends ParsingFailureSuite("parser/failure/spacing")

class ParsingFailureTimeSuite extends ParsingFailureSuite("parser/failure/time")
class ParsingFailureOtherSuite extends ParsingFailureSuite("parser/failure") {
  // We ignore "nonUtf8" because by the time we see a string in Java any non-UTF-8 characters have
  // been replaced. See `DhallParserSuite` for a non-ignored test that covers the same ground.
  override def ignored = Set("nonUtf8")
}

class BinaryDecodingUnitSuite extends BinaryDecodingSuite("binary-decode/success/unit")
class BinaryDecodingImportsUnitSuite extends BinaryDecodingSuite("binary-decode/success/unit/imports")
class BinaryDecodingFailureUnitSuite extends BinaryDecodingFailureSuite("binary-decode/failure/unit")

class ImportResolutionSuccessSuite extends ImportResolutionSuite("import/success")
class ImportResolutionSuccessUnitSuite extends ImportResolutionSuite("import/success/unit") {
  // TODO: fix this bug
  override def ignored = Set("DontCacheIfHash")
}
class ImportResolutionSuccessUnitAsLocationSuite extends ImportResolutionSuite("import/success/unit/asLocation")
class ImportResolutionFailureUnitSuite extends ImportResolutionFailureSuite("import/failure/unit")
class ImportResolutionFailureOtherSuite extends ImportResolutionFailureSuite("import/failure")
