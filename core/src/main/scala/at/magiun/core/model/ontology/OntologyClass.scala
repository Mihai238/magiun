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

  /** Algorithm */
  val Algorithm: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Algorithm")

  /** Algorithm - Classification */
  val Classification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Classification")
  val Binary: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Binary")
  val Multiclass: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Multiclass")

  /** Algorithm - Regression */
  val Regression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Regression")
  val LinearLeastRegression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Linear_Least_Regression")

  /** Variable Type */
  val VariableType: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Variable_Type")
  val Categorical: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Categorical")
  val Numerical: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Numerical")
  val Continuous: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Continuous")


  def getOntologyClass(variableType: at.magiun.core.model.data.VariableType): OntologyClass.Value = {
    variableType match {
      case at.magiun.core.model.data.VariableType.Continuous => Continuous
      case at.magiun.core.model.data.VariableType.Categorical => Categorical
    }
  }

}
