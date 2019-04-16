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
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class OrdinalLogisticRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class IsotonicRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class SurvivalRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class GradientBoostTreeRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class RandomForestRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class DecisionTreeRegressionAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class MultinomialNaiveBayesClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class BernoulliNaiveBayesClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class GaussianNaiveBayesClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class LinearSupportVectorMachineAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class MultilayerPerceptronClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class RandomForestClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class GradientBoostTreeClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}

case class DecisionTreeClassificationAlgorithm(name: String,
                                             formula: String,
                                             parameters: Set[AlgorithmParameter[_ <: Any]] = Set.empty
                                            ) extends Algorithm {
  override def enhanceAlgorithm[T](algorithm: T): Unit = ???
}
