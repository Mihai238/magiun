package at.magiun.core.model.algorithm

sealed trait Algorithm extends Serializable {
  val name: String
  val formula: String
  val parameters: Set[AlgorithmParameter[_ <: Any]]

  def enhanceAlgorithm[T](algorithm: T): Unit
}

case class LinearRegressionAlgorithm(name: String,
                                     formula: String,
                                     parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LinearRegressionParameters) extends Algorithm {
  override def enhanceAlgorithm[LinearRegression](algorithm: LinearRegression): Unit = {
  }
}

case class GeneralizedLinearRegressionAlgorithm(name: String,
                                                formula: String,
                                                parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.GeneralizedLinearRegressionParameters) extends Algorithm {
  override def enhanceAlgorithm[GeneralizedLinearRegression](algorithm: GeneralizedLinearRegression): Unit = ???
}

case class BinaryLogisticRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LogisticRegressionParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class OrdinalLogisticRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LogisticRegressionParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class IsotonicRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.IsotonicRegressionParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class SurvivalRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.SurvivalRegressionParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class GradientBoostTreeRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.GradientBoostTreeRegressionClassificationParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class RandomForestRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.RandomForestRegressionParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class DecisionTreeRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.DecisionTreeRegressionClassificationParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class MultinomialNaiveBayesClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.NaiveBayesParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[NaiveBayes](algorithm: NaiveBayes): Unit = {
    algorithm.asInstanceOf[org.apache.spark.ml.classification.NaiveBayes].setModelType("multinomial")
  }
}

case class BernoulliNaiveBayesClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.NaiveBayesParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[NaiveBayes](algorithm: NaiveBayes): Unit = {
    algorithm.asInstanceOf[org.apache.spark.ml.classification.NaiveBayes].setModelType("bernoulli")
  }
}

case class LinearSupportVectorMachineAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.LinearSVMParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class MultilayerPerceptronClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.MultilayerPerceptronClassificationParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class RandomForestClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.RandomForestClassificationParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class GradientBoostTreeClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.GradientBoostTreeRegressionClassificationParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class DecisionTreeClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = AlgorithmParameter.DecisionTreeRegressionClassificationParameters
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}
