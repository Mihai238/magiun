package at.magiun.core.model.ontology

import at.magiun.core.config.AlgorithmOntologyConfig
import enumeratum._

import scala.collection.immutable

sealed abstract class OntologyClass(val ontologyUri: String, val name: String) extends EnumEntry

object OntologyClass extends Enum[OntologyClass] with CirceEnum[OntologyClass] {

  import AlgorithmOntologyConfig.NS

  /** Dataset stuff */
  case object Dataset extends OntologyClass(NS + "Dataset", "Dataset")

  /** Distribution */
  case object BernoulliDistribution extends OntologyClass(NS + "BernoulliDistribution", "Bernoulli Distribution")
  case object BinomialDistribution extends  OntologyClass(NS + "BinomialDistribution", "Binomial Distribution")
  case object NormalDistribution extends  OntologyClass(NS + "NormalDistribution", "Normal Distribution")
  case object ExponentialDistribution extends  OntologyClass(NS + "ExponentialDistribution", "Exponential Distribution")
  case object MultinomialDistribution extends  OntologyClass(NS + "MultinomialDistribution", "Multinomial Distribution")
  case object CategoricalDistribution extends  OntologyClass(NS + "CategoricalDistribution", "Categorical Distribution")
  case object GammaDistribution extends OntologyClass(NS + "GammaDistribution", "Gamma Distribution")
  case object PoissonDistribution extends OntologyClass(NS + "PoissonDistribution", "Poisson Distribution")
  case object UniformDistribution extends OntologyClass(NS + "UniformDistribution", "Uniform Distribution")

  /** Algorithm */
  case object Algorithm extends OntologyClass(NS + "Algorithm", "Algorithm")

  /** Algorithm - Classification */
  case object Classification extends OntologyClass(NS + "Classification", "Classification")
  case object BinaryLogisticRegressionPartial extends OntologyClass(NS + "BinaryLogisticRegressionPartial", "Binary Logistic Regression Partial")
  case object BinaryLogisticRegressionComplete extends OntologyClass(NS + "BinaryLogisticRegressionComplete", "Binary Logistic Regression Complete")
  case object MultinomialLogisticRegressionPartial extends OntologyClass(NS + "MultinomialLogisticRegressionPartial", "Multinomial Logistic Regression Partial")
  case object MultinomialLogisticRegressionComplete extends OntologyClass(NS + "MultinomialLogisticRegressionComplete", "Multinomial Logistic Regression Complete")
  case object MultinomialNaiveBayesClassification extends OntologyClass(NS + "MultinomialNaiveBayesClassification", "Multinomial Naiuve Bayes Classification")
  case object BernoulliNaiveBayesClassification extends OntologyClass(NS + "BernoulliNaiveBayesClassification", "Bernoulli Naiuve Bayes Classification")
  case object LinearSupportVectorMachine extends OntologyClass(NS + "LinearSupportVectorMachine", "Linear Support Vector Machine")
  case object MultilayerPerceptronClassification extends OntologyClass(NS + "MultilayerPerceptronClassification", "Multilayer Perceptron Classification")
  case object RandomForestClassificationComplete extends OntologyClass(NS + "RandomForestClassificationComplete", "Random Forest Classification Complete")
  case object RandomForestClassificationPartial extends OntologyClass(NS + "RandomForestClassificationPartial", "Random Forest Classification Partial")
  case object GradientBoostTreeClassification extends OntologyClass(NS + "GradientBoostTreeClassification", "Gradient Boost Tree Classification")
  case object DecisionTreeClassificationComplete extends OntologyClass(NS + "DecisionTreeClassificationComplete", "Decision Tree Classification Complete")
  case object DecisionTreeClassificationPartial extends OntologyClass(NS + "DecisionTreeClassificationPartial", "Decision Tree Classification Partial")

  /** Algorithm - Regression */
  case object Regression extends OntologyClass(NS + "Regression", "Regression")
  case object LinearLeastRegressionPartial extends OntologyClass(NS + "LinearLeastRegressionPartial", "Linear Least Regression Partial")
  case object LinearLeastRegressionComplete extends OntologyClass(NS + "LinearLeastRegressionComplete", "Linear Least Regression Complete")
  case object GeneralizedLinearRegressionIdentityPartial extends OntologyClass(NS + "GeneralizedLinearRegressionIdentityPartial", "Generalized Linear Regression Partial - family: gaussian & identity: link")
  case object GeneralizedLinearRegressionIdentityComplete extends OntologyClass(NS + "GeneralizedLinearRegressionIdentityComplete", "Generalized Linear Regression Complete - family: gaussian & identity: link")
  case object GeneralizedLinearRegressionBinomialLogitPartial extends OntologyClass(NS + "GeneralizedLinearRegressionBinomialLogitPartial", "Generalized Linear Regression Partial - family: binomial & identity: logit")
  case object GeneralizedLinearRegressionBinomialLogitComplete extends OntologyClass(NS + "GeneralizedLinearRegressionBinomialLogitComplete", "Generalized Linear Regression Complete - family: binomial & identity: logit")
  case object GeneralizedLinearRegressionExponentialInversePartial extends OntologyClass(NS + "GeneralizedLinearRegressionExponentialInversePartial", "Generalized Linear Regression Partial - family: gamma & identity: inverse")
  case object GeneralizedLinearRegressionExponentialInverseComplete extends OntologyClass(NS + "GeneralizedLinearRegressionExponentialInverseComplete", "Generalized Linear Regression Complete - family: gamma & identity: inverse")
  case object GeneralizedLinearRegressionPoissonLogPartial extends OntologyClass(NS + "GeneralizedLinearRegressionPoissonLogPartial", "Generalized Linear Regression Complete - family: poisson & identity: log")
  case object GeneralizedLinearRegressionPoissonLogComplete extends OntologyClass(NS + "GeneralizedLinearRegressionPoissonLogComplete", "Generalized Linear Regression Complete - family: poisson & identity: log")
  case object IsotonicRegression extends OntologyClass(NS + "IsotonicRegression", "Isotonic Regression")
  case object SurvivalRegression extends OntologyClass(NS + "SurvivalRegression", "Survival Regression")
  case object GradientBoostTreeRegressionPartial extends OntologyClass(NS + "GradientBoostTreeRegressionPartial", "Gradient Boost Tree Regression Partial")
  case object GradientBoostTreeRegressionComplete extends OntologyClass(NS + "GradientBoostTreeRegressionComplete", "Gradient Boost Tree Regression Complete")
  case object RandomForestRegressionPartial extends OntologyClass(NS + "RandomForestRegressionPartial", "Random Forest Regression Partial")
  case object RandomForestRegressionComplete extends OntologyClass(NS + "RandomForestRegressionComplete", "Random Forest Regression Complete")
  case object DecisionTreeRegressionPartial extends OntologyClass(NS + "DecisionTreeRegressionPartial", "Decision Tree Regression Partial")
  case object DecisionTreeRegressionComplete extends OntologyClass(NS + "DecisionTreeRegressionComplete", "Decision Tree Regression Complete")

  /** Variable Type */
  case object VariableType extends OntologyClass(NS + "VariableType", "Variable Type")
  case object Categorical extends OntologyClass(NS + "Categorical", "Categorical")
  case object Numerical extends OntologyClass(NS + "Numerical", "Numerical")
  case object Continuous extends OntologyClass(NS + "Continuous", "Continuous")
  case object Discrete extends OntologyClass(NS + "Discrete", "Discrete")
  case object Binary extends OntologyClass(NS + "Binary", "Binary")

  /** Variable Type */
  case object GoalClassification extends OntologyClass(NS + "GoalClassification", "Goal Classification")
  case object GoalRegression extends OntologyClass(NS + "GoalRegression", "Goal Regression")

  val values: immutable.IndexedSeq[OntologyClass] = findValues

  private val COMPLETE_PARTIAL_ALGORITHMS_MAP = Map[OntologyClass, OntologyClass](
    RandomForestClassificationComplete -> RandomForestClassificationPartial,
    DecisionTreeClassificationComplete -> DecisionTreeClassificationPartial,
    LinearLeastRegressionComplete -> LinearLeastRegressionPartial,
    BinaryLogisticRegressionComplete -> BinaryLogisticRegressionPartial,
    MultinomialLogisticRegressionComplete  -> MultinomialLogisticRegressionPartial,
    GeneralizedLinearRegressionIdentityComplete -> GeneralizedLinearRegressionIdentityPartial,
    GeneralizedLinearRegressionBinomialLogitComplete -> GeneralizedLinearRegressionBinomialLogitPartial,
    GeneralizedLinearRegressionExponentialInverseComplete -> GeneralizedLinearRegressionExponentialInversePartial,
    GeneralizedLinearRegressionPoissonLogComplete -> GeneralizedLinearRegressionPoissonLogPartial,
    GradientBoostTreeRegressionComplete -> GradientBoostTreeRegressionPartial,
    RandomForestRegressionComplete -> RandomForestRegressionPartial,
    DecisionTreeRegressionComplete -> DecisionTreeRegressionPartial
  )

  private val COMPLETE_ALGORITHMS = COMPLETE_PARTIAL_ALGORITHMS_MAP.keySet
  private val PARTIAL_ALGORITHMS = COMPLETE_PARTIAL_ALGORITHMS_MAP.values.toSet

  def isAbstractType(ontologyClass: OntologyClass): Boolean = {
    ontologyClass.equals(Algorithm) || ontologyClass.equals(Classification) || ontologyClass.equals(Regression) || ontologyClass.equals(VariableType) ||
      ontologyClass.equals(IsotonicRegression) || ontologyClass.equals(SurvivalRegression)
  }

  def isPartialAlgorithm(ontologyClass: OntologyClass): Boolean = {
    PARTIAL_ALGORITHMS.contains(ontologyClass)
  }

  def isCompleteAlgorithm(ontologyClass: OntologyClass): Boolean = {
    COMPLETE_ALGORITHMS.contains(ontologyClass)
  }

  def getPartialOfCompleteAlgorithm(ontologyClass: OntologyClass): OntologyClass = {
    COMPLETE_PARTIAL_ALGORITHMS_MAP(ontologyClass)
  }

  def isSpecialCaseAlgorithm(ontologyClass: OntologyClass): Boolean = {
    ontologyClass match {
      case  MultilayerPerceptronClassification => true
      case _ => false
    }
  }

  def isGenericAlgorithm(ontologyClass: OntologyClass): Boolean = {
    ontologyClass match {
      case LinearSupportVectorMachine | GradientBoostTreeRegressionPartial | GradientBoostTreeRegressionComplete | RandomForestRegressionPartial
           | RandomForestRegressionComplete | DecisionTreeRegressionPartial | DecisionTreeRegressionComplete |
      GradientBoostTreeClassification | RandomForestClassificationPartial | RandomForestClassificationComplete | DecisionTreeClassificationPartial
      | DecisionTreeClassificationComplete => true
      case _ => false
    }
  }

  def getOntologyClass(variableType: at.magiun.core.model.data.VariableType): OntologyClass = {
    variableType match {
      case at.magiun.core.model.data.VariableType.Continuous => Continuous
      case at.magiun.core.model.data.VariableType.Categorical => Categorical
      case at.magiun.core.model.data.VariableType.Discrete => Discrete
      case at.magiun.core.model.data.VariableType.Binary => Binary
      case _ => Continuous
    }
  }
}
