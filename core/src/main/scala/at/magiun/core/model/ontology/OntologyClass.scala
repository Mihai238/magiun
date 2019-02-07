package at.magiun.core.model.ontology

import at.magiun.core.config.AlgorithmOntologyConfig

object OntologyClass extends Enumeration {

  /** Dataset stuff */
  val Dataset: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Dataset")

  /** Distribution */
  val BernoulliDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Bernoulli_Distribution")
  val BinomialDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Binomial_Distribution")
  val NormalDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Normal_Distribution")
  val ExponentialDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Exponential_Distribution")
  val MultinomialDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Multinomial_Distribution")
  val CategoricalDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Categorical_Distribution")
  val GammaDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Gamma_Distribution")
  val PoissonDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Poisson_Distribution")

  /** Algorithm */
  val Algorithm: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Algorithm")

  /** Algorithm - Classification */
  val Classification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Classification")
  val MultinomialNaiveBayesClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Multinomial_Naive_Bayes_Classification")
  val BernoulliNaiveBayesClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Bernoulli_Naive_Bayes_Classification")
  val GaussianNaiveBayesClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Gaussian_Naive_Bayes_Classification")
  val LinearSupportVectorMachine: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Linear_Support_Vector_Machine")
  val MultilayerPerceptronClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Multilayer_Perceptron_Classification")
  val RandomForestClassificationComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Random_Forest_Classification_Complete")
  val RandomForestClassificationPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Random_Forest_Classification_Partial")
  val GradientBoostTreeClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Gradient_Boost_Tree_Classification")
  val DecisionTreeClassificationComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Decision_Tree_Classification_Complete")
  val DecisionTreeClassificationPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Decision_Tree_Classification_Partial")

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
  val IsotonicRegression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Isotonic_Regression")
  val SurvivalRegression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Survival_Regression")
  val GradientBoostTreeRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Gradient_Boost_Tree_Regression_Partial")
  val GradientBoostTreeRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Gradient_Boost_Tree_Regression_Complete")
  val RandomForestRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Random_Forest_Regression_Partial")
  val RandomForestRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Random_Forest_Regression_Complete")
  val DecisionTreeRegressionPartial: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Decision_Tree_Regression_Partial")
  val DecisionTreeRegressionComplete: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Decision_Tree_Regression_Complete")

  /** Variable Type */
  val VariableType: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Variable_Type")
  val Categorical: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Categorical")
  val Numerical: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Numerical")
  val Continuous: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Continuous")
  val Binary: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Binary")

  /** Variable Type */
  val GoalClassification: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Goal_Classification")
  val GoalRegression: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Goal_Regression")


  def getOntologyClass(variableType: at.magiun.core.model.data.VariableType): OntologyClass.Value = {
    variableType match {
      case at.magiun.core.model.data.VariableType.Continuous => Continuous
      case at.magiun.core.model.data.VariableType.Categorical => Categorical
      case at.magiun.core.model.data.VariableType.Binary => Binary
    }
  }

}
