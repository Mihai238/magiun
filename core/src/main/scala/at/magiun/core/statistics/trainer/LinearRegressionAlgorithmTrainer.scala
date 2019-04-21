package at.magiun.core.statistics.trainer

import at.magiun.core.model.algorithm.LinearRegressionAlgorithm
import at.magiun.core.model.response.{CoefficientResponse, TrainAlgorithmResponse}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.DataFrame

object LinearRegressionAlgorithmTrainer {

  def train(algorithm: LinearRegressionAlgorithm, dataFrame: DataFrame, responseVariableName: String, explanatoryVariablesNames: Array[String]): TrainAlgorithmResponse = {
    try {
      val sparkAlgorithm: LinearRegression = algorithm.createAndEnhanceAlgorithm

      val vectorAssembler = new VectorAssembler()
        .setInputCols(explanatoryVariablesNames)
        .setOutputCol("features")

      sparkAlgorithm.setLabelCol(responseVariableName)
        .setFeaturesCol("features")

      val transformedDataFrame = vectorAssembler.transform(dataFrame)


      val result = sparkAlgorithm.fit(transformedDataFrame)
      val summary = result.summary
      val standardErrors = summary.coefficientStandardErrors
      val tValues = summary.tValues
      val pValues = summary.pValues


      val a = summary.devianceResiduals

      TrainAlgorithmResponse(
        CoefficientResponse(responseVariableName, result.intercept, standardErrors.last, tValues.last, pValues.last),
        createCoefficients(result.coefficients.toArray, standardErrors, tValues, pValues,  explanatoryVariablesNames),
        summary.degreesOfFreedom,
        summary.explainedVariance,
        summary.meanAbsoluteError,
        summary.meanSquaredError,
        summary.r2,
        summary.r2adj,
        summary.rootMeanSquaredError
      )
    } catch {
      case e: Throwable => new TrainAlgorithmResponse(s"An error occurred while trying to train the model! Cause: ${e.getCause}")
    }
  }

  private def createCoefficients(coefficients: Array[Double], standardErrors: Array[Double], tValues: Array[Double], pValues: Array[Double], explanatoryVariablesNames: Array[String]): Set[CoefficientResponse] = {
    explanatoryVariablesNames.indices
      .map(i => CoefficientResponse(explanatoryVariablesNames(i), coefficients(i), standardErrors(i), tValues(i), pValues(i)))
      .toSet
  }
}
