package at.magiun.core.model.ontology

import at.magiun.core.config.AlgorithmOntologyConfig

object OntologyClass extends Enumeration {

  /** Dataset stuff */
  val Dataset: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Dataset")

  /** Distribution */
  val BernoulliDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "BernoulliDistribution")
  val BinomialDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "BinomialDistribution")
  val NormalDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "NormalDistribution")
  val ExponentialDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "ExponentialDistribution")
  val MultinomialDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "MultinomialDistribution")
  val CategoricalDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "CategoricalDistribution")
  val GammaDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "GammaDistribution")
  val PoissonDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "PoissonDistribution")

  /** Algorithm */
  val Algorithm: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Algorithm")

  /** Algorithm - Classification */
  val Classification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Classification")
  val MultinomialNaiveBayesClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "MultinomialNaiveBayesClassification")
  val BernoulliNaiveBayesClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "BernoulliNaiveBayesClassification")
  val GaussianNaiveBayesClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "GaussianNaiveBayesClassification")
  val LinearSupportVectorMachine: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "LinearSupportVectorMachine")
  val MultilayerPerceptronClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "MultilayerPerceptronClassification")
  val RandomForestClassificationComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "RandomForestClassificationComplete")
  val RandomForestClassificationPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "RandomForestClassificationPartial")
  val GradientBoostTreeClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "GradientBoostTreeClassification")
  val DecisionTreeClassificationComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "DecisionTreeClassificationComplete")
  val DecisionTreeClassificationPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "DecisionTreeClassificationPartial")

  /** Algorithm - Regression */
  val Regression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Regression")
  val LinearLeastRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "LinearLeastRegressionPartial")
  val LinearLeastRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "LinearLeastRegressionComplete")
  val BinaryLogisticRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "BinaryLogisticRegressionPartial")
  val BinaryLogisticRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "BinaryLogisticRegressionComplete")
  val OrdinalLogisticRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "OrdinalLogisticRegressionPartial")
  val OrdinalLogisticRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "OrdinalLogisticRegressionComplete")
  val GeneralizedLinearRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "GeneralizedLinearRegressionPartial")
  val GeneralizedLinearRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "GeneralizedLinearRegressionComplete")
  val DecisionTreePartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "DecisionTreeRegressionPartial")
  val DecisionTreeComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "DecisionTreeRegressionComplete")
  val IsotonicRegression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "IsotonicRegression")
  val SurvivalRegression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "SurvivalRegression")
  val GradientBoostTreeRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "GradientBoostTreeRegressionPartial")
  val GradientBoostTreeRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "GradientBoostTreeRegressionComplete")
  val RandomForestRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "RandomForestRegressionPartial")
  val RandomForestRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "RandomForestRegressionComplete")
  val DecisionTreeRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "DecisionTreeRegressionPartial")
  val DecisionTreeRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "DecisionTreeRegressionComplete")

  /** Variable Type */
  val VariableType: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "VariableType")
  val Categorical: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Categorical")
  val Numerical: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Numerical")
  val Continuous: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Continuous")
  val Binary: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Binary")

  /** Variable Type */
  val GoalClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "GoalClassification")
  val GoalRegression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "GoalRegression")


  def getOntologyClass(variableType: at.magiun.core.model.data.VariableType): OntologyClass.Value = {
    variableType match {
      case at.magiun.core.model.data.VariableType.Continuous => Continuous
      case at.magiun.core.model.data.VariableType.Categorical => Categorical
      case at.magiun.core.model.data.VariableType.Binary => Binary
    }
  }

}
