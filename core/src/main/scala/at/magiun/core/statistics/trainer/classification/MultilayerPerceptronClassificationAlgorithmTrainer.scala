package at.magiun.core.statistics.trainer.classification

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.MultilayerPerceptronClassificationAlgorithm
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.{CoefficientResponse, TrainAlgorithmResponse}
import org.apache.spark.sql.DataFrame

object MultilayerPerceptronClassificationAlgorithmTrainer extends ClassificationAlgorithmTrainer {

  def train(algorithm: MultilayerPerceptronClassificationAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {

    val sparkAlgorithm = algorithm.createAndEnhanceAlgorithm
      .setLabelCol(responseVariableName)
      .setFeaturesCol("features")

    val transformedDF = transformDF(dataFrame, explanatoryVariablesNames)
    val Array(trainingData, testData) = transformedDF.randomSplit(Array(0.7, 0.3))

    val fit = sparkAlgorithm.fit(trainingData)
    val predictions = fit.transform(testData)

    predictions.show()
    val dataSampleAndPredictions = getDataSampleAndPredictedValues(testData.select(responseVariableName), predictions, sampleSize)
    magiunContext.addModelToCache(fit.uid, fit)

    TrainAlgorithmResponse(
      id = fit.uid,
      algorithmImplementation = AlgorithmImplementation.MultilayerPerceptronClassificationAlgorithm,
      intercept = CoefficientResponse(responseVariableName),
      accuracy = evaluateFit(predictions, responseVariableName, "accuracy"),
      weightedPrecision = evaluateFit(predictions, responseVariableName, "weightedPrecision"),
      weightedRecall = evaluateFit(predictions, responseVariableName, "weightedRecall"),
      f1 = evaluateFit(predictions, responseVariableName, "f1"),
      fittedValues = dataSampleAndPredictions._1,
      dataSample = dataSampleAndPredictions._2
    )
  }

}
