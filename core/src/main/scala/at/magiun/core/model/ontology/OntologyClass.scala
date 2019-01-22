package at.magiun.core.model.ontology

import at.magiun.core.config.AlgorithmOntologyConfig

object OntologyClass extends Enumeration {

  /** Dataset stuff */
  val Dataset: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Dataset")

  /** Distribution */
  val Distribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Distribution")
  val BernoulliDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Bernoulli_Distribution")
  val BinomialDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Binomial_Distribution")
  val NormalDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Normal_Distribution")
  val ExponentialDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Exponential_Distribution")

  /** Algorithm */
  val Algorithm: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Algorithm")

  /** Algorithm - Classification */
  val Classification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Classification")
  val Multiclass: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Multiclass")

  /** Algorithm - Regression */
  val Regression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Regression")
  val LinearLeastRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Linear_Least_Regression_Partial")
  val LinearLeastRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Linear_Least_Regression_Complete")
  val BinaryLogisticRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Binary_Logistic_Regression_Partial")
  val BinaryLogisticRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Binary_Logistic_Regression_Complete")
  val OrdinalLogisticRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Ordinal_Logistic_Regression_Partial")
  val OrdinalLogisticRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Ordinal_Logistic_Regression_Complete")
  val GeneralizedLinearRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Generalized_Linear_Regression_Partial")
  val GeneralizedLinearRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Generalized_Linear_Regression_Complete")
  val DecisionTreePartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Decision_Tree_Regression_Partial")
  val DecisionTreeComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Decision_Tree_Regression_Complete")

  /** Variable Type */
  val VariableType: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Variable_Type")
  val Categorical: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Categorical")
  val Numerical: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Numerical")
  val Continuous: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Continuous")
  val Binary: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Binary")


  def getOntologyClass(variableType: at.magiun.core.model.data.VariableType): OntologyClass.Value = {
    variableType match {
      case at.magiun.core.model.data.VariableType.Continuous => Continuous
      case at.magiun.core.model.data.VariableType.Categorical => Categorical
      case at.magiun.core.model.data.VariableType.Binary => Binary
    }
  }

}
