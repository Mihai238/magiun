export enum AlgorithmImplementation {

  LinearRegressionAlgorithm = "Linear Regression",
  GeneralizedLinearRegressionAlgorithm = "Generalized Linear Regression",
  DecisionTreeRegressionAlgorithm = "Decision Tree Regression",
  RandomForestRegressionAlgorithm = "Random Forest Regression"

}

export namespace AlgorithmImplementation {

  export function isRegression(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.LinearRegressionAlgorithm || 
      implementation == AlgorithmImplementation.GeneralizedLinearRegressionAlgorithm ||
      implementation == AlgorithmImplementation.DecisionTreeRegressionAlgorithm ||
      implementation == AlgorithmImplementation.RandomForestRegressionAlgorithm ;
  }

  export function isTreeRegression(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.DecisionTreeRegressionAlgorithm ||
      implementation == AlgorithmImplementation.RandomForestRegressionAlgorithm;
  }

  export function isRegressionWithCoefficients(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.LinearRegressionAlgorithm ||
      implementation == AlgorithmImplementation.GeneralizedLinearRegressionAlgorithm;
  }
}

