package at.magiun.core.model.algorithm

sealed trait AlgorithmParameter[T] extends Serializable {
  val name: String
  val value: T
  val selectOptions: Set[T]
}

/** COMMON */
case class MaxIter(name: String = "maxIter", value: Int = 100, selectOptions: Set[Int] = Set.empty[Int]) extends AlgorithmParameter[Int] {}
case class FitIntercept(name: String = "fitIntercept", value: Boolean = true, selectOptions: Set[Boolean] = Set(true, false)) extends AlgorithmParameter[Boolean] {}
case class RegParam(name: String = "regParam", value: Double = 0.0, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}
case class Tolerance(name: String = "tol", value: Double = 1E-6, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}
case class Solver(name: String = "solver", value: String = "auto", selectOptions: Set[String] = Set("auto", "normal", "l-bfgs")) extends AlgorithmParameter[String] {}
case class AggregationDepth(name: String = "aggregationDepth", value: Int = 2, selectOptions: Set[Int] = Set.empty[Int]) extends AlgorithmParameter[Int] {}
case class Standardization(name: String = "standardization", value: Boolean = true, selectOptions: Set[Boolean] = Set(true, false)) extends AlgorithmParameter[Boolean] {}

/** LINEAR REGRESSION */
case class ElasticNet(name: String = "elasticNet", value: Double = 0.0, selectOptions: Set[Double] = Set.empty[Double]) extends AlgorithmParameter[Double] {}
case class Epsilon(name: String = "epsilon", value: Double = 1.35, selectOptions: Set[Double] = Set.empty[Double]) extends AlgorithmParameter[Double] {}
case class Loss(name: String = "loss", value: String = "squaredError", selectOptions: Set[String] = Set("squaredError", "huber")) extends AlgorithmParameter[String] {}

/** GENERALIZED LINEAR REGRESSION */
case class GLMFamily(name: String = "family", value: String = "gaussian", selectOptions: Set[String] = Set("gaussian", "binomial", "poisson", "gamma", "tweedie")) extends AlgorithmParameter[String] {}
case class Link(name: String = "link", value: String = "identity", selectOptions: Set[String] = Set("identity", "log", "inverse", "logit", "probit", "cloglog", "sqrt")) extends AlgorithmParameter[String] {}
case class LinkPower(name: String = "linkPower", value: Double = 0, selectOptions: Set[Double] = Set(-1, 0, 0.5, 1)) extends AlgorithmParameter[Double] {}

/** LOGISTIC REGRESSION */
/**
  * todo: upper an lower bounds on coefficients and intercept are not modeled at the moment
  */
case class LogisticFamily(name: String = "family", value: String = "auto", selectOptions: Set[String] = Set("auto", "binomial", "multinomial")) extends AlgorithmParameter[String] {}
case class LogisticThreshold(name: String = "threshold", value: Double = 0.5, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}

/** ISOTONIC REGRESSION */
case class Isotonic(name: String = "isotonic", value: Boolean = true, selectOptions: Set[Boolean] = Set(true, false)) extends AlgorithmParameter[Boolean] {}

/** GRADIENT BOOST TREE REGRESSION */
case class CacheNodeIds(name: String = "cacheNodeIds", value: Boolean = true, selectOptions: Set[Boolean] = Set(true, false)) extends AlgorithmParameter[Boolean] {}
case class CheckpointInterval(name: String = "checkpointInterval", value: Int = 10, selectOptions: Set[Int] = Set.empty) extends AlgorithmParameter[Int] {}
case class FeatureSubsetStrategy(name: String = "featureSubsetStrategy", value: String = "auto", selectOptions: Set[String] = Set("auto", "all", "onethird", "sqrt", "log2")) extends AlgorithmParameter[String] {}
case class LossType(name: String = "lossType", value: String = "squared", selectOptions: Set[String] = Set("squared", "absolute")) extends AlgorithmParameter[String] {}
case class MaxBins(name: String = "maxBins", value: Int = 32, selectOptions: Set[Int] = Set.empty) extends AlgorithmParameter[Int] {}
case class MaxDepth(name: String = "maxDepth", value: Int = 5, selectOptions: Set[Int] = Set.empty) extends AlgorithmParameter[Int] {}
case class MaxMemoryInMB(name: String = "maxMemoryInMB", value: Int = 256, selectOptions: Set[Int] = Set.empty) extends AlgorithmParameter[Int] {}
case class MinInfoGain(name: String = "minInfoGain", value: Double = 0.0, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}
case class MinInstancesPerNode(name: String = "minInstancesPerNode", value: Int = 1, selectOptions: Set[Int] = Set.empty) extends AlgorithmParameter[Int] {}
case class Seed(name: String = "seed", value: Long = Seed.getClass.getName.hashCode.toLong, selectOptions: Set[Long] = Set.empty) extends AlgorithmParameter[Long] {}
case class StepSize(name: String = "stepSize", value: Double = 0.1, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}
case class SubsamplingRate(name: String = "subsamplingRate", value: Double = 1.0, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}

/** SVM */
case class SVMThreshold(name: String = "threshold", value: Double = 0, selectOptions: Set[Double] = Set.empty) extends AlgorithmParameter[Double] {}

object AlgorithmParameter {

  /** COMMON */
  private val MAX_ITER = MaxIter()
  private val AGGREGATION_DEPTH = AggregationDepth()
  private val FIT_INTERCEPT = FitIntercept()
  private val REG_PARAM = RegParam()
  private val TOLERANCE = Tolerance()
  private val STANDARDIZATION = Standardization()
  private val SOLVER = Solver()

  /** LINEAR REGRESSION */
  private val ELASTIC_NET = ElasticNet()
  private val EPSILON = Epsilon()
  private val LOSS = Loss()

  /** GENERALIZED LINEAR REGRESSION */
  private val GLM_FAMILY = GLMFamily()
  private val LINK = Link()
  private val LINK_POWER = LinkPower()

  /** LOGISTIC REGRESSION */
  private val LOGISTIC_FAMILY = LogisticFamily()
  private val LOGISTIC_THRESHOLD = LogisticThreshold()

  /** ISOTONIC REGRESSION */
  private val ISOTONIC = Isotonic()

  /** GRADIENT BOOST TREE REGRESSION */
  private val CACHE_NODE_IDS = CacheNodeIds()
  private val CHECKPOINT_INTERVAL = CheckpointInterval()
  private val FEATURE_SUBSET_STRATEGY = FeatureSubsetStrategy()
  private val LOSS_TYPE = LossType()
  private val MAX_BINS = MaxBins()
  private val MAX_DEPTH = MaxDepth()
  private val MAX_MEMORY_IN_MB = MaxMemoryInMB()
  private val MIN_INFO_GAIN = MinInfoGain()
  private val MIN_INSTANCES_PER_NODE = MinInstancesPerNode()
  private val SEED = Seed()
  private val STEP_SIZE = StepSize()
  private val SUBSAMPLING_RATE = SubsamplingRate()

  /** SVM */
  private val SVM_THRESHOLD = SVMThreshold()

  val LinearRegressionParameters: Set[AlgorithmParameter[_ <: Any]] = Set(MAX_ITER, AGGREGATION_DEPTH, ELASTIC_NET, EPSILON, FIT_INTERCEPT, REG_PARAM, TOLERANCE, STANDARDIZATION, LOSS, SOLVER)
  val GeneralizedLinearRegressionParameters: Set[AlgorithmParameter[_ <: Any]] = Set(MAX_ITER, FIT_INTERCEPT, REG_PARAM, GLM_FAMILY, LINK, LINK_POWER, TOLERANCE, SOLVER )
  val LogisticRegressionParameters: Set[AlgorithmParameter[_ <: Any]] = Set(MAX_ITER, AGGREGATION_DEPTH, ELASTIC_NET,  FIT_INTERCEPT, REG_PARAM, LOGISTIC_FAMILY, TOLERANCE, STANDARDIZATION, LOGISTIC_THRESHOLD )
  val IsotonicRegressionParameters: Set[AlgorithmParameter[_ <: Any]] = Set(ISOTONIC)
  val SurvivalRegressionParameters: Set[AlgorithmParameter[_ <: Any]] = Set(MAX_ITER, AGGREGATION_DEPTH, FIT_INTERCEPT, TOLERANCE)
  val GradientBoostTreeRegressionParameters: Set[AlgorithmParameter[_ <: Any]] = Set(MAX_ITER, CACHE_NODE_IDS, CHECKPOINT_INTERVAL, FEATURE_SUBSET_STRATEGY, LOSS_TYPE, MAX_BINS, MAX_DEPTH, MAX_MEMORY_IN_MB, MIN_INFO_GAIN, MIN_INSTANCES_PER_NODE, SEED, STEP_SIZE, SUBSAMPLING_RATE)
  val LinearSVMParameters: Set[AlgorithmParameter[_ <: Any]] = Set(MAX_ITER, AGGREGATION_DEPTH, FIT_INTERCEPT, REG_PARAM, STANDARDIZATION, TOLERANCE, SVM_THRESHOLD)
}