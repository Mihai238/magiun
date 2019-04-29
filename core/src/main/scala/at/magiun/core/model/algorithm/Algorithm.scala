package at.magiun.core.model.algorithm

import org.apache.spark.ml.{Estimator, Model}
import org.apache.spark.ml.classification._
import org.apache.spark.ml.regression._

import scala.reflect._

sealed trait Algorithm[T <: Estimator[_ <: Model[_ <: Any]]] extends Serializable {
  val name: String
  val parameters: Set[AlgorithmParameter[_ <: Any]]

  def createAndEnhanceAlgorithm(implicit classTag: ClassTag[T]): T = {
    val algorithm = classTag.runtimeClass.newInstance().asInstanceOf[T]
    enhanceAlgorithm(algorithm)
    algorithm
  }

  protected def enhanceAlgorithm(algorithm: T): Unit = {
    val parametersMap = Map(parameters.toSeq map { p => p.name -> p.value }: _*).filter(p => algorithm.hasParam(p._1))

    parametersMap.foreach{entry =>
      val parameter = algorithm.getParam(entry._1)
      algorithm.set(parameter, entry._2)
    }
  }
}

case class LinearRegressionAlgorithm(name: String,
                                     parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LinearRegressionParameters) extends Algorithm[LinearRegression] {}

case class GeneralizedLinearRegressionAlgorithm(name: String,
                                                parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.GeneralizedLinearRegressionParameters) extends Algorithm[GeneralizedLinearRegression] {}

case class BinaryLogisticRegressionAlgorithm(name: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LogisticRegressionParameters
                                            ) extends Algorithm[LogisticRegression] {

  override def enhanceAlgorithm(algorithm: LogisticRegression): Unit = {
    super.enhanceAlgorithm(algorithm)
    algorithm.setFamily("binomial")
  }
}

case class MultinomialLogisticRegressionAlgorithm(name: String,
                                                  parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LogisticRegressionParameters
                                             ) extends Algorithm[LogisticRegression] {

  override def enhanceAlgorithm(algorithm: LogisticRegression): Unit = {
    super.enhanceAlgorithm(algorithm)
    algorithm.setFamily("multinomial")
  }
}

case class IsotonicRegressionAlgorithm(name: String,
                                       parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.IsotonicRegressionParameters
                                      ) extends Algorithm[IsotonicRegression] {}

case class SurvivalRegressionAlgorithm(name: String,
                                       parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.SurvivalRegressionParameters
                                      ) extends Algorithm[AFTSurvivalRegression] {}

case class GradientBoostTreeRegressionAlgorithm(name: String,
                                                parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.GradientBoostTreeRegressionClassificationParameters
                                               ) extends Algorithm[GBTRegressor] {}

case class RandomForestRegressionAlgorithm(name: String,
                                           parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.RandomForestRegressionParameters
                                          ) extends Algorithm[RandomForestRegressor] {}

case class DecisionTreeRegressionAlgorithm(name: String,
                                           parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.DecisionTreeRegressionClassificationParameters
                                          ) extends Algorithm[DecisionTreeRegressor] {}

case class MultinomialNaiveBayesClassificationAlgorithm(name: String,
                                                        parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.NaiveBayesParameters
                                                       ) extends Algorithm[NaiveBayes] {
  override def enhanceAlgorithm(algorithm: NaiveBayes): Unit = {
    super.enhanceAlgorithm(algorithm)
    algorithm.setModelType("multinomial")
  }
}

case class BernoulliNaiveBayesClassificationAlgorithm(name: String,
                                                      parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.NaiveBayesParameters
                                                     ) extends Algorithm[NaiveBayes] {
  override def enhanceAlgorithm(algorithm: NaiveBayes): Unit = {
    super.enhanceAlgorithm(algorithm)
    algorithm.setModelType("bernoulli")
  }
}

case class LinearSupportVectorMachineAlgorithm(name: String,
                                               parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LinearSVMParameters
                                              ) extends Algorithm[LinearSVC] {}

case class MultilayerPerceptronClassificationAlgorithm(name: String,
                                                       parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.MultilayerPerceptronClassificationParameters
                                                      ) extends Algorithm[MultilayerPerceptronClassifier] {}

case class RandomForestClassificationAlgorithm(name: String,
                                               parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.RandomForestClassificationParameters
                                              ) extends Algorithm[RandomForestClassifier] {}

case class GradientBoostTreeClassificationAlgorithm(name: String,
                                                    parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.GradientBoostTreeRegressionClassificationParameters
                                                   ) extends Algorithm[GBTClassifier] {}

case class DecisionTreeClassificationAlgorithm(name: String,
                                               parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.DecisionTreeRegressionClassificationParameters
                                              ) extends Algorithm[DecisionTreeClassifier] {}
