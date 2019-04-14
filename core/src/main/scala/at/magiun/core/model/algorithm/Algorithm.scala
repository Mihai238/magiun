package at.magiun.core.model.algorithm

sealed trait Algorithm extends Serializable {
  val name: String
  val formula: String
  val parameters: Set[AlgorithmParameter[_ <: AnyVal]]

  def enhanceAlgorithm[T](algorithm: T): Unit
}

case class LinearRegressionAlgorithm(name: String, formula: String, parameters: Set[AlgorithmParameter[_ <: AnyVal]] = AlgorithmParameter.LinearRegressionParameters) extends Algorithm {
  override def enhanceAlgorithm[LinearRegression](algorithm: LinearRegression): Unit = {
  }
}
case class GeneralizedLinearRegressionAlgorithm(override val name: String, override val formula: String) extends Algorithm {

  override val parameters: Set[AlgorithmParameter[_ <: AnyVal]] = Set()

  override def enhanceAlgorithm[GeneralizedLinearRegression](algorithm: GeneralizedLinearRegression): Unit = ???
}
