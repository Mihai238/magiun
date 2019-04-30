package at.magiun.core.statistics.trainer.classification

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.LinearSupportVectorMachineAlgorithm
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.{CoefficientResponse, TrainAlgorithmResponse}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.DataFrame

object LinearSupportVectorMachineTrainer extends ClassificationAlgorithmTrainer {

  def train(algorithm: LinearSupportVectorMachineAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {

    val transformedDF = transformDF(dataFrame, explanatoryVariablesNames)

    val sparkAlgorithm = algorithm.createAndEnhanceAlgorithm
      .setLabelCol(responseVariableName)
      .setFeaturesCol("features")

    val Array(trainingData, testData) = transformedDF.randomSplit(Array(0.7, 0.3))
    val fit = sparkAlgorithm.fit(trainingData)
    val predictions = fit.transform(testData)

    val dataSampleAndPredictions = getDataSampleAndPredictedValues(testData.select(responseVariableName), predictions, sampleSize)
    magiunContext.addModelToCache(fit.uid, fit)


    val coeffsWithNames = explanatoryVariablesNames.zip(fit.coefficients.toArray)

    TrainAlgorithmResponse(
      id = fit.uid,
      algorithmImplementation = AlgorithmImplementation.LinearSupportVectorMachineAlgorithm,
      intercept = CoefficientResponse(responseVariableName, if (fit.getFitIntercept) fit.intercept else 0),
      coefficients = createCoefficients(coeffsWithNames),
      accuracy = evaluateFit(predictions, responseVariableName, "accuracy"),
      weightedPrecision = evaluateFit(predictions, responseVariableName, "weightedPrecision"),
      weightedRecall = evaluateFit(predictions, responseVariableName, "weightedRecall"),
      f1 = evaluateFit(predictions, responseVariableName, "f1"),
      fittedValues = dataSampleAndPredictions._1,
      dataSample = dataSampleAndPredictions._2
    )
  }

  private def evaluateFit(predictions: DataFrame, metric: String): Double = {
    new MulticlassClassificationEvaluator()
      .setLabelCol("indexedLabel")
      .setPredictionCol("prediction")
      .setMetricName(metric)
      .evaluate(predictions)
  }

}
