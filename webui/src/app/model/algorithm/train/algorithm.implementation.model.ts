export enum AlgorithmImplementation {

  LinearRegressionAlgorithm = "Linear Regression",
  GeneralizedLinearRegressionAlgorithm = "Generalized Linear Regression",
  DecisionTreeRegressionAlgorithm = "Decision Tree Regression",
  RandomForestRegressionAlgorithm = "Random Forest Regression",
  GradientBoostTreeRegressionAlgorithm = "Gradient Boost Tree Regression",
  MultinomialLogisticRegressionAlgorithm = "Multinomial Logistic Regression",
  BinaryLogisticRegressionAlgorithm = "Binary Logistic Regression"

}

export namespace AlgorithmImplementation {

  export function isRegression(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.LinearRegressionAlgorithm ||
      implementation == AlgorithmImplementation.GeneralizedLinearRegressionAlgorithm ||
      implementation == AlgorithmImplementation.DecisionTreeRegressionAlgorithm ||
      implementation == AlgorithmImplementation.GradientBoostTreeRegressionAlgorithm ||
      implementation == AlgorithmImplementation.RandomForestRegressionAlgorithm ;
  }

  export function isTreeRegression(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.DecisionTreeRegressionAlgorithm ||
      implementation == AlgorithmImplementation.GradientBoostTreeRegressionAlgorithm ||
      implementation == AlgorithmImplementation.RandomForestRegressionAlgorithm;
  }

  export function isRegressionWithCoefficients(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.LinearRegressionAlgorithm ||
      implementation == AlgorithmImplementation.GeneralizedLinearRegressionAlgorithm;
  }

  export function isClassification(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.MultinomialLogisticRegressionAlgorithm ||
      implementation == AlgorithmImplementation.BinaryLogisticRegressionAlgorithm;
  }
}

