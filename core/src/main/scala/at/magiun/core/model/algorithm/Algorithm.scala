package at.magiun.core.model.algorithm

import org.apache.spark.ml.classification.{DecisionTreeClassifier, GBTClassifier, LinearSVC, LogisticRegression, MultilayerPerceptronClassifier, NaiveBayes, RandomForestClassifier}
import org.apache.spark.ml.regression.{AFTSurvivalRegression, DecisionTreeRegressor, GBTRegressor, GeneralizedLinearRegression, IsotonicRegression, LinearRegression, RandomForestRegressor}

sealed trait Algorithm[T] extends Serializable {
  val name: String
  val formula: String
  val parameters: Set[AlgorithmParameter[_ <: Any]]

  def enhanceAlgorithm(algorithm: T): Unit
}

case class LinearRegressionAlgorithm(name: String,
                                     formula: String,
                                     parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LinearRegressionParameters) extends Algorithm[LinearRegression] {
  override def enhanceAlgorithm(algorithm: LinearRegression): Unit = {
  }
}

case class GeneralizedLinearRegressionAlgorithm(name: String,
                                                formula: String,
                                                parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.GeneralizedLinearRegressionParameters) extends Algorithm[GeneralizedLinearRegression] {
  override def enhanceAlgorithm(algorithm: GeneralizedLinearRegression): Unit = ???
}

case class BinaryLogisticRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LogisticRegressionParameters
                                            ) extends Algorithm[LogisticRegression] {
  override def enhanceAlgorithm(algorithm: LogisticRegression): Unit = ???
}

case class OrdinalLogisticRegressionAlgorithm(name: String,
                                              formula: String,
                                              parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LogisticRegressionParameters
                                             ) extends Algorithm[LogisticRegression] {
  override def enhanceAlgorithm(algorithm: LogisticRegression): Unit = ???
}

case class IsotonicRegressionAlgorithm(name: String,
                                       formula: String,
                                       parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.IsotonicRegressionParameters
                                      ) extends Algorithm[IsotonicRegression] {
  override def enhanceAlgorithm(algorithm: IsotonicRegression): Unit = ???
}

case class SurvivalRegressionAlgorithm(name: String,
                                       formula: String,
                                       parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.SurvivalRegressionParameters
                                      ) extends Algorithm[AFTSurvivalRegression] {
  override def enhanceAlgorithm(algorithm: AFTSurvivalRegression): Unit = ???
}

case class GradientBoostTreeRegressionAlgorithm(name: String,
                                                formula: String,
                                                parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.GradientBoostTreeRegressionClassificationParameters
                                               ) extends Algorithm[GBTRegressor] {
  override def enhanceAlgorithm(algorithm: GBTRegressor): Unit = ???
}

case class RandomForestRegressionAlgorithm(name: String,
                                           formula: String,
                                           parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.RandomForestRegressionParameters
                                          ) extends Algorithm[RandomForestRegressor] {
  override def enhanceAlgorithm(algorithm: RandomForestRegressor): Unit = ???
}

case class DecisionTreeRegressionAlgorithm(name: String,
                                           formula: String,
                                           parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.DecisionTreeRegressionClassificationParameters
                                          ) extends Algorithm[DecisionTreeRegressor] {
  override def enhanceAlgorithm(algorithm: DecisionTreeRegressor): Unit = ???
}

case class MultinomialNaiveBayesClassificationAlgorithm(name: String,
                                                        formula: String,
                                                        parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.NaiveBayesParameters
                                                       ) extends Algorithm[NaiveBayes] {
  override def enhanceAlgorithm(algorithm: NaiveBayes): Unit = {
    algorithm.setModelType("multinomial")
    val parametersMap = parameters.map(p => p.name -> p.value)
  }
}

case class BernoulliNaiveBayesClassificationAlgorithm(name: String,
                                                      formula: String,
                                                      parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.NaiveBayesParameters
                                                     ) extends Algorithm[NaiveBayes] {
  override def enhanceAlgorithm(algorithm: NaiveBayes): Unit = {
    algorithm.setModelType("bernoulli")
  }
}

case class LinearSupportVectorMachineAlgorithm(name: String,
                                               formula: String,
                                               parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LinearSVMParameters
                                              ) extends Algorithm[LinearSVC] {
  override def enhanceAlgorithm(algorithm: LinearSVC): Unit = ???
}

case class MultilayerPerceptronClassificationAlgorithm(name: String,
                                                       formula: String,
                                                       parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.MultilayerPerceptronClassificationParameters
                                                      ) extends Algorithm[MultilayerPerceptronClassifier] {
  override def enhanceAlgorithm(algorithm: MultilayerPerceptronClassifier): Unit = ???
}

case class RandomForestClassificationAlgorithm(name: String,
                                               formula: String,
                                               parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.RandomForestClassificationParameters
                                              ) extends Algorithm[RandomForestClassifier] {
  override def enhanceAlgorithm(algorithm: RandomForestClassifier): Unit = ???
}

case class GradientBoostTreeClassificationAlgorithm(name: String,
                                                    formula: String,
                                                    parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.GradientBoostTreeRegressionClassificationParameters
                                                   ) extends Algorithm[GBTClassifier] {
  override def enhanceAlgorithm(algorithm: GBTClassifier): Unit = ???
}

case class DecisionTreeClassificationAlgorithm(name: String,
                                               formula: String,
                                               parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.DecisionTreeRegressionClassificationParameters
                                              ) extends Algorithm[DecisionTreeClassifier] {
  override def enhanceAlgorithm(algorithm: DecisionTreeClassifier): Unit = ???
}
