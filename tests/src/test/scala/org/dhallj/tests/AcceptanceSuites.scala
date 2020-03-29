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