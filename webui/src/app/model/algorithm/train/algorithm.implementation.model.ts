export enum AlgorithmImplementation {

  LinearRegressionAlgorithm = "Linear Regression",
  GeneralizedLinearRegressionAlgorithm = "Generalized Linear Regression",
  DecisionTreeRegressionAlgorithm = "Decision Tree Regression"

}

export namespace AlgorithmImplementation {

  export function isRegressionWithCoefficients(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.LinearRegressionAlgorithm || 
      implementation == AlgorithmImplementation.GeneralizedLinearRegressionAlgorithm;
  }
}

