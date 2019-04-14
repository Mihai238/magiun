package at.magiun.core.model.algorithm

sealed trait AlgorithmParameter[T] extends Serializable {
  val name: String
  val value: T
  val selectOptions: Set[T]
}

case class MaxIter(name: String = "maxIter", value: Int = 100, selectOptions: Set[Int] = Set.empty[Int]) extends AlgorithmParameter[Int] {}
case class ElasticNet(name: String = "elasticNet", value: Double = 0.0, selectOptions: Set[Double] = Set.empty[Double]) extends AlgorithmParameter[Double] {}
case class AggregationDepth(name: String = "aggregationDepth", value: Int = 2, selectOptions: Set[Int] = Set.empty[Int]) extends AlgorithmParameter[Int] {}
case class Epsilon(name: String = "epsilon", value: Double = 1.35, selectOptions: Set[Double] = Set.empty[Double]) extends AlgorithmParameter[Double] {}
case class FitIntercept(name: String = "fitIntercept", value: Boolean = true, selectOptions: Set[Boolean] = Set(true, false)) extends AlgorithmParameter[Boolean] {}
case class RegParam(name: String = "regParam", value: Double = 0.0, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}
case class Tolerance(name: String = "tol", value: Double = 1E-6, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}
case class Standardization(name: String = "standardization", value: Boolean = true, selectOptions: Set[Boolean] = Set(true, false)) extends AlgorithmParameter[Boolean] {}


object AlgorithmParameter {
  private val MAX_ITER = MaxIter()
  private val AGGREGATION_DEPTH = AggregationDepth()
  private val ELASTIC_NET = ElasticNet()
  private val EPSILON = Epsilon()
  private val FIT_INTERCEPT = FitIntercept()
  private val REG_PARAM = RegParam()
  private val TOLERANCE = Tolerance()
  private val STANDARDIZATION = Standardization()

  val LinearRegressionParameters: Set[AlgorithmParameter[_ <: AnyVal]] = Set(MAX_ITER, AGGREGATION_DEPTH, ELASTIC_NET, EPSILON, FIT_INTERCEPT, REG_PARAM, TOLERANCE, STANDARDIZATION)
}