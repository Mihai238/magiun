export enum AlgorithmImplementation {

  LinearRegressionAlgorithm = "Linear Regression"

}

export namespace AlgorithmImplementation {

  export function isRegression(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.LinearRegressionAlgorithm;
  }
}

