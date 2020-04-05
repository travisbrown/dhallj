package org.dhallj.tests

class NormalizationSimpleSuite extends ExprNormalizationSuite("normalization/success/simple")

class NormalizationHaskellTutorialAccessSuite
    extends ExprNormalizationSuite("normalization/success/haskell-tutorial/access")

class NormalizationHaskellTutorialCombineTypesSuite
    extends ExprNormalizationSuite(
      "normalization/success/haskell-tutorial/combineTypes"
    )

class NormalizationHaskellTutorialPreferSuite
    extends ExprNormalizationSuite(
      "normalization/success/haskell-tutorial/prefer"
    )

class NormalizationHaskellTutorialProjectionSuite
    extends ExprNormalizationSuite(
      "normalization/success/haskell-tutorial/projection"
    )

class NormalizationRegressionSuite extends ExprNormalizationSuite("normalization/success/regression")

class NormalizationUnitSuite extends ExprNormalizationSuite("normalization/success/unit")
class NormalizationSimplificationsSuite extends ExprNormalizationSuite("normalization/success/simplifications")
class NormalizationOtherSuite extends ExprNormalizationSuite("normalization/success")

class HashSimpleSuite extends HashAcceptanceSuite("semantic-hash/success/simple")

class HashHaskellTutorialAccessSuite extends HashAcceptanceSuite("semantic-hash/success/haskell-tutorial/access")

class HashHaskellTutorialCombineTypesSuite
    extends HashAcceptanceSuite("semantic-hash/success/haskell-tutorial/combineTypes")

class HashHaskellTutorialPreferSuite extends HashAcceptanceSuite("semantic-hash/success/haskell-tutorial/prefer")

class HashHaskellTutorialProjectionSuite
    extends HashAcceptanceSuite("semantic-hash/success/haskell-tutorial/projection")

class AlphaNormalizationUnitSuite extends ExprAcceptanceSuite("alpha-normalization/success/unit", _.alphaNormalize)
class AlphaNormalizationRegressionSuite
    extends ExprAcceptanceSuite("alpha-normalization/success/regression", _.alphaNormalize)

class TypeCheckingSimpleSuite extends ExprTypeCheckingSuite("type-inference/success/simple")
class TypeCheckingUnitSuite extends ExprTypeCheckingSuite("type-inference/success/unit")
class TypeCheckingRegressionSuite extends ExprTypeCheckingSuite("type-inference/success/regression")
class TypeCheckingFailureUnitSuite extends ExprTypeCheckingFailureSuite("type-inference/failure/unit")

class ParserUnitSuite extends ParserAcceptanceSuite("parser/success/unit") {
  override def ignored = Set("SomeXYZ")
}

class ParserSuccessSuite extends ParserAcceptanceSuite("parser/success") {
  override def ignored = Set("largeExpression")
}

class ParserFailureUnitSuite extends ParserFailureSuite("parser/failure/unit")
class ParserFailureSpacingSuite extends ParserFailureSuite("parser/failure/spacing")
class ParserFailureOtherSuite extends ParserFailureSuite("parser/failure")
