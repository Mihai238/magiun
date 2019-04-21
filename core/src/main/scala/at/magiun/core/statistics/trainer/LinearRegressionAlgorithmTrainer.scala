package at.magiun.core.statistics.trainer

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.LinearRegressionAlgorithm
import at.magiun.core.model.response.{CoefficientResponse, TrainAlgorithmResponse}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.DataFrame

object LinearRegressionAlgorithmTrainer {

  def train(algorithm: LinearRegressionAlgorithm, dataFrame: DataFrame, responseVariableName: String, explanatoryVariablesNames: Array[String], magiunContext: MagiunContext): TrainAlgorithmResponse = {
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
      val intercept = result.intercept
      val interceptStandardError = if (intercept != 0) standardErrors.last else 0.0
      val interceptTValue = if (intercept != 0) tValues.last else 0.0
      val interceptPValue = if (intercept != 0) pValues.last else 0.0

      magiunContext.addResidualsToCache(result.uid, summary.residuals)
      magiunContext.addPredictionToCache(result.uid, summary.predictions)

      TrainAlgorithmResponse(
        result.uid,
        CoefficientResponse(responseVariableName, intercept, interceptStandardError, interceptTValue, interceptPValue),
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
