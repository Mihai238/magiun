export enum AlgorithmImplementation {

  LinearRegressionAlgorithm = "Linear Regression",
  GeneralizedLinearRegressionAlgorithm = "Generalized Linear Regression",
  DecisionTreeRegressionAlgorithm = "Decision Tree Regression",
  RandomForestRegressionAlgorithm = "Random Forest Regression",
  GradientBoostTreeRegressionAlgorithm = "Gradient Boost Tree Regression",
  MultinomialLogisticRegressionAlgorithm = "Multinomial Logistic Regression",
  BinaryLogisticRegressionAlgorithm = "Binary Logistic Regression",
  DecisionTreeClassificationAlgorithm = "Decision Tree Classification",
  RandomForestClassificationAlgorithm = "Random Forest Classification",
  GradientBoostTreeClassificationAlgorithm = "Gradient Boost Tree Classification"

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
      implementation == AlgorithmImplementation.BinaryLogisticRegressionAlgorithm ||
      implementation == AlgorithmImplementation.DecisionTreeClassificationAlgorithm ||
      implementation == AlgorithmImplementation.RandomForestClassificationAlgorithm ||
      implementation == AlgorithmImplementation.GradientBoostTreeClassificationAlgorithm;
  }

  export function isClassificationWithCoefficients(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.MultinomialLogisticRegressionAlgorithm ||
      implementation == AlgorithmImplementation.BinaryLogisticRegressionAlgorithm;
  }

  export function isClassificationWithMeasurementsByLabel(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.MultinomialLogisticRegressionAlgorithm ||
      implementation == AlgorithmImplementation.BinaryLogisticRegressionAlgorithm;
  }

  export function isLogisticRegression(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.MultinomialLogisticRegressionAlgorithm ||
      implementation == AlgorithmImplementation.BinaryLogisticRegressionAlgorithm;
  }

  export function isTreeClassification(implementation: AlgorithmImplementation) {
    return implementation == AlgorithmImplementation.DecisionTreeClassificationAlgorithm ||
      implementation == AlgorithmImplementation.RandomForestClassificationAlgorithm ||
      implementation == AlgorithmImplementation.GradientBoostTreeClassificationAlgorithm;
  }
}

