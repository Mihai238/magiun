package at.magiun.core.model.request

import enumeratum._

import scala.collection.immutable

case class TrainAlgorithmRequest(datasetId: Int, responseVariable: Int, explanatoryVariables: Seq[Int], algorithm: AlgorithmRequest) extends Serializable {}

case class AlgorithmRequest(name: String, implementation: AlgorithmImplementation, parameters: Set[AlgorithmParameterRequest]) {}

case class AlgorithmParameterRequest(name: String, value: String) {}

sealed abstract class AlgorithmImplementation extends EnumEntry

object AlgorithmImplementation extends Enum[AlgorithmImplementation] with CirceEnum[AlgorithmImplementation] {

  val values: immutable.IndexedSeq[AlgorithmImplementation] = findValues

  case object LinearRegressionAlgorithm extends AlgorithmImplementation
  case object GeneralizedLinearRegressionAlgorithm extends AlgorithmImplementation
  case object BinaryLogisticRegressionAlgorithm extends AlgorithmImplementation
  case object OrdinalLogisticRegressionAlgorithm extends AlgorithmImplementation
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
}