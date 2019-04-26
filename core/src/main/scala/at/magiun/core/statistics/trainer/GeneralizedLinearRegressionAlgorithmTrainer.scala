package at.magiun.core.statistics.trainer

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.GeneralizedLinearRegressionAlgorithm
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.TrainAlgorithmResponse
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.GeneralizedLinearRegression
import org.apache.spark.sql.DataFrame

object GeneralizedLinearRegressionAlgorithmTrainer extends RegressionAlgorithmTrainer {

  def train(algorithm: GeneralizedLinearRegressionAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {
    try {
      val sparkAlgorithm: GeneralizedLinearRegression = algorithm.createAndEnhanceAlgorithm

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

      magiunContext.addModelToCache(fit.uid, fit)
      val dataSamplePredictionsAndResiduals = getDataSampleFittedValuesAndResiduals(
        dataFrame.select(responseVariableName), summary.predictions.select("prediction"), summary.residuals.select("devianceResiduals"), sampleSize)

      TrainAlgorithmResponse(
        id = fit.uid,
        algorithmImplementation = AlgorithmImplementation.GeneralizedLinearRegressionAlgorithm,
        intercept = createIntercept(responseVariableName, fit.intercept, standardErrors, tValues, pValues),
        coefficients = createCoefficients(fit.coefficients.toArray, standardErrors, tValues, pValues,  explanatoryVariablesNames),
        degreesOfFreedom = summary.degreesOfFreedom,
        aic = summary.aic,
        deviance = summary.deviance,
        nullDeviance = summary.nullDeviance,
        dispersion = summary.dispersion,
        residualDegreeOfFreedom = summary.residualDegreeOfFreedom,
        residualDegreeOfFreedomNull = summary.residualDegreeOfFreedomNull,
        rank = summary.rank,
        fittedValues = dataSamplePredictionsAndResiduals._1,
        residuals = dataSamplePredictionsAndResiduals._2,
        dataSample = dataSamplePredictionsAndResiduals._3
      )

    } catch {
      case e: Exception =>
        e.printStackTrace()
        new TrainAlgorithmResponse(s"An error occurred while trying to train the model! ${e.getMessage}")
    }

  }

}
