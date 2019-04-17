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

  /** Algorithm */
  case object Algorithm extends OntologyClass(NS + "Algorithm", "Algorithm")

  /** Algorithm - Classification */
  case object Classification extends OntologyClass(NS + "Classification", "Classification")
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
  case object BinaryLogisticRegressionPartial extends OntologyClass(NS + "BinaryLogisticRegressionPartial", "Binary Logistic Regression Partial")
  case object BinaryLogisticRegressionComplete extends OntologyClass(NS + "BinaryLogisticRegressionComplete", "Binary Logistic Regression Complete")
  case object OrdinalLogisticRegressionPartial extends OntologyClass(NS + "OrdinalLogisticRegressionPartial", "Ordinal Logistic Regression Partial")
  case object OrdinalLogisticRegressionComplete extends OntologyClass(NS + "OrdinalLogisticRegressionComplete", "Ordinal Logistic Regression Complete")
  case object GeneralizedLinearRegressionPartial extends OntologyClass(NS + "GeneralizedLinearRegressionPartial", "Generalized Linear Regression Partial")
  case object GeneralizedLinearRegressionComplete extends OntologyClass(NS + "GeneralizedLinearRegressionComplete", "Generalized Linear Regression Complete")
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

  def isAbstractType(ontologyClass: OntologyClass): Boolean = {
    ontologyClass.equals(Algorithm) || ontologyClass.equals(Classification) || ontologyClass.equals(Regression) || ontologyClass.equals(VariableType)
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
