package at.magiun.core.model.algorithm

sealed trait AlgorithmParameter[T] extends Serializable {
  val name: String
  val value: T
  val selectOptions: Set[T]
}
/** COMMON **/
case class MaxIter(name: String = "maxIter", value: Int = 100, selectOptions: Set[Int] = Set.empty[Int]) extends AlgorithmParameter[Int] {}
case class FitIntercept(name: String = "fitIntercept", value: Boolean = true, selectOptions: Set[Boolean] = Set(true, false)) extends AlgorithmParameter[Boolean] {}
case class RegParam(name: String = "regParam", value: Double = 0.0, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}
case class Tolerance(name: String = "tol", value: Double = 1E-6, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}
case class Solver(name: String = "solver", value: String = "auto", selectOptions: Set[String] = Set("auto", "normal", "l-bfgs")) extends AlgorithmParameter[String] {}

/** LINEAR REGRESSION **/
case class ElasticNet(name: String = "elasticNet", value: Double = 0.0, selectOptions: Set[Double] = Set.empty[Double]) extends AlgorithmParameter[Double] {}
case class AggregationDepth(name: String = "aggregationDepth", value: Int = 2, selectOptions: Set[Int] = Set.empty[Int]) extends AlgorithmParameter[Int] {}
case class Epsilon(name: String = "epsilon", value: Double = 1.35, selectOptions: Set[Double] = Set.empty[Double]) extends AlgorithmParameter[Double] {}
case class Standardization(name: String = "standardization", value: Boolean = true, selectOptions: Set[Boolean] = Set(true, false)) extends AlgorithmParameter[Boolean] {}
case class Loss(name: String = "loss", value: String = "squaredError", selectOptions: Set[String] = Set("squaredError", "huber")) extends AlgorithmParameter[String] {}

/** GENERALIZED LINEAR REGRESSION */
case class Family(name: String = "family", value: String = "gaussian", selectOptions: Set[String] = Set("gaussian", "binomial", "poisson", "gamma", "tweedie")) extends AlgorithmParameter[String] {}
case class Link(name: String = "link", value: String = "identity", selectOptions: Set[String] = Set("identity", "log", "inverse", "logit", "probit", "cloglog", "sqrt")) extends AlgorithmParameter[String] {}
case class LinkPower(name: String = "linkPower", value: Double = 0, selectOptions: Set[Double] = Set(-1, 0, 0.5, 1)) extends AlgorithmParameter[Double] {}


object AlgorithmParameter {
  private val MAX_ITER = MaxIter()
  private val AGGREGATION_DEPTH = AggregationDepth()
  private val ELASTIC_NET = ElasticNet()
  private val EPSILON = Epsilon()
  private val FIT_INTERCEPT = FitIntercept()
  private val REG_PARAM = RegParam()
  private val TOLERANCE = Tolerance()
  private val STANDARDIZATION = Standardization()
  private val LOSS = Loss()
  private val SOLVER = Solver()
  private val FAMILY = Family()
  private val LINK = Link()
  private val LINK_POWER = LinkPower()


  val LinearRegressionParameters: Set[AlgorithmParameter[_ <: Any]] = Set(MAX_ITER, AGGREGATION_DEPTH, ELASTIC_NET, EPSILON, FIT_INTERCEPT, REG_PARAM, TOLERANCE, STANDARDIZATION, LOSS, SOLVER)
  val GeneralizedLinearRegressionParameters: Set[AlgorithmParameter[_ <: Any]] = Set(MAX_ITER, FIT_INTERCEPT, REG_PARAM, FAMILY, LINK, LINK_POWER, TOLERANCE, SOLVER )
}