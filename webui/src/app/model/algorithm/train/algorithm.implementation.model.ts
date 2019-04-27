export enum AlgorithmImplementation {

  LinearRegressionAlgorithm = "Linear Regression",
  GeneralizedLinearRegressionAlgorithm = "Generalized Linear Regression",
  DecisionTreeRegressionAlgorithm = "Decision Tree Regression"

}

export namespace AlgorithmImplementation {

  export function isRegression(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.LinearRegressionAlgorithm || 
      implementation == AlgorithmImplementation.GeneralizedLinearRegressionAlgorithm ||
      implementation == AlgorithmImplementation.DecisionTreeRegressionAlgorithm;
  }

  export function isTreeRegression(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.DecisionTreeRegressionAlgorithm;
  }

  export function isRegressionWithCoefficients(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.LinearRegressionAlgorithm ||
      implementation == AlgorithmImplementation.GeneralizedLinearRegressionAlgorithm;
  }
}

