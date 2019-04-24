package at.magiun.core.statistics.trainer

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.LinearRegressionAlgorithm
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.{CoefficientResponse, TrainAlgorithmResponse}
import at.magiun.core.util.DatasetUtil
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionTrainingSummary}
import org.apache.spark.sql.{DataFrame, Row}

object LinearRegressionAlgorithmTrainer {

  def train(algorithm: LinearRegressionAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {
    try {
      val sparkAlgorithm: LinearRegression = algorithm.createAndEnhanceAlgorithm

      val vectorAssembler = new VectorAssembler()
        .setInputCols(explanatoryVariablesNames)
        .setOutputCol("features")

      sparkAlgorithm.setLabelCol(responseVariableName)
        .setFeaturesCol("features")

      val transformedDataFrame = vectorAssembler.transform(dataFrame)


      val fit = sparkAlgorithm.fit(transformedDataFrame)
      val summary = fit.summary
      val standardErrors = summary.coefficientStandardErrors
      val tValues = summary.tValues
      val pValues = summary.pValues
      val intercept = fit.intercept
      val interceptStandardError = if (intercept != 0) standardErrors.last else 0.0
      val interceptTValue = if (intercept != 0) tValues.last else 0.0
      val interceptPValue = if (intercept != 0) pValues.last else 0.0

      magiunContext.addModelToCache(fit.uid, fit)
      val dataSamplePredictionsAndResiduals = getDataSampleFittedValuesAndResiduals(dataFrame.select(responseVariableName), summary, sampleSize)

      TrainAlgorithmResponse(
        fit.uid,
        AlgorithmImplementation.LinearRegressionAlgorithm,
        CoefficientResponse(responseVariableName, intercept, interceptStandardError, interceptTValue, interceptPValue),
        createCoefficients(fit.coefficients.toArray, standardErrors, tValues, pValues,  explanatoryVariablesNames),
        summary.degreesOfFreedom,
        summary.explainedVariance,
        summary.meanAbsoluteError,
        summary.meanSquaredError,
        summary.r2,
        summary.r2adj,
        summary.rootMeanSquaredError,
        dataSamplePredictionsAndResiduals._1,
        dataSamplePredictionsAndResiduals._2,
        dataSamplePredictionsAndResiduals._3
      )
    } catch {
      case e: Throwable => new TrainAlgorithmResponse(s"An error occurred while trying to train the model! Cause: ${e.getCause}")
    }
  }

  private def createCoefficients(coefficients: Array[Double],
                                 standardErrors: Array[Double],
                                 tValues: Array[Double],
                                 pValues: Array[Double],
                                 explanatoryVariablesNames: Array[String]): Seq[CoefficientResponse] = {
    explanatoryVariablesNames.indices
      .map(i => CoefficientResponse(
        explanatoryVariablesNames(i),
        coefficients(i),
        standardErrors(i),
        tValues(i),
        pValues(i)
      ))
  }

  private def getDataSampleFittedValuesAndResiduals(data: DataFrame, summary: LinearRegressionTrainingSummary, sampleSize: Int): (Seq[Double], Seq[Double], Seq[Double]) = {
    val dataCount: Int = data.count().intValue()
    val predictions: DataFrame = summary.predictions.select("prediction")
    val residuals: DataFrame = summary.residuals.select("residuals")

    var dataSample: Array[Row] = Array.empty
    var predictionsArray: Array[Row] = Array.empty
    var residualsArray: Array[Row] = Array.empty

    if (sampleSize > dataCount) {
      dataSample = data.collect()
      predictionsArray = predictions.collect()
      residualsArray = residuals.collect()
    } else {
      val randomIndices = DatasetUtil.getRandomIndices(sampleSize, dataCount)
      dataSample = DatasetUtil.getRowsByIndices(data, randomIndices)
      predictionsArray = DatasetUtil.getRowsByIndices(predictions, randomIndices)
      residualsArray = DatasetUtil.getRowsByIndices(residuals, randomIndices)
    }

    (
      dataSample.map(r => r.getDouble(0)),
      predictionsArray.map(r => r.getDouble(0)),
      residualsArray.map(r => r.getDouble(0))
    )
  }
}
