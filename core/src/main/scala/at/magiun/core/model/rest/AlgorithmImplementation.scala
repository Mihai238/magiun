package at.magiun.core.model.rest

import enumeratum.{CirceEnum, Enum, EnumEntry}

import scala.collection.immutable

sealed abstract class AlgorithmImplementation extends EnumEntry

object AlgorithmImplementation extends Enum[AlgorithmImplementation] with CirceEnum[AlgorithmImplementation] {

  val values: immutable.IndexedSeq[AlgorithmImplementation] = findValues

  case object LinearRegressionAlgorithm extends AlgorithmImplementation
  case object GeneralizedLinearRegressionAlgorithm extends AlgorithmImplementation
  case object BinaryLogisticRegressionAlgorithm extends AlgorithmImplementation
  case object MultinomialLogisticRegressionAlgorithm extends AlgorithmImplementation
  case object IsotonicRegressionAlgorithm extends AlgorithmImplementation
  case object SurvivalRegressionAlgorithm extends AlgorithmImplementation
  case object GradientBoostTreeRegressionAlgorithm extends AlgorithmImplementation
  case object RandomForestRegressionAlgorithm extends AlgorithmImplementation
  case object DecisionTreeRegressionAlgorithm extends AlgorithmImplementation
  case object MultinomialNaiveBayesClassificationAlgorithm extends AlgorithmImplementation
  case object BernoulliNaiveBayesClassificationAlgorithm extends AlgorithmImplementation
  case object LinearSupportVectorMachineAlgorithm extends AlgorithmImplementation
  case object MultilayerPerceptronClassificationAlgorithm extends AlgorithmImplementation
  case object RandomForestClassificationAlgorithm extends AlgorithmImplementation
  case object GradientBoostTreeClassificationAlgorithm extends AlgorithmImplementation
  case object DecisionTreeClassificationAlgorithm extends AlgorithmImplementation
  case object None extends AlgorithmImplementation
}
