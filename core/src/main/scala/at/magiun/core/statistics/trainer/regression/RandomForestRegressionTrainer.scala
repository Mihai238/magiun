package at.magiun.core.statistics.trainer.regression

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.RandomForestRegressionAlgorithm
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.{CoefficientResponse, TrainAlgorithmResponse}
import org.apache.spark.ml.regression.{RandomForestRegressionModel, RandomForestRegressor}
import org.apache.spark.sql.DataFrame

object RandomForestRegressionTrainer extends TreeRegressionAlgorithmTrainer {

  def train(algorithm: RandomForestRegressionAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int
           ): TrainAlgorithmResponse = {
    val sparkAlgorithm: RandomForestRegressor = new RandomForestRegressor()

    val transformedDF = transformDF(dataFrame, explanatoryVariablesNames)

    sparkAlgorithm.setLabelCol(responseVariableName)
      .setFeaturesCol("features")

    val Array(trainingData, testData) = transformedDF.randomSplit(Array(0.7, 0.3))

    val fit: RandomForestRegressionModel = sparkAlgorithm.fit(trainingData)
    magiunContext.addModelToCache(fit.uid, fit)

    val predictions = fit.transform(testData)

    val dataSamplePredictionsAndResiduals = getDataSampleFittedValuesAndResiduals(testData.select(responseVariableName), predictions, sampleSize)

    TrainAlgorithmResponse(
      id = fit.uid,
      algorithmImplementation = AlgorithmImplementation.RandomForestRegressionAlgorithm,
      intercept = CoefficientResponse(responseVariableName),
      meanSquaredError = evaluateFit(predictions, responseVariableName, "mse"),
      meanAbsoluteError = evaluateFit(predictions, responseVariableName, "mae"),
      rSquared = evaluateFit(predictions, responseVariableName, "r2"),
      rootMeanSquaredError = evaluateFit(predictions, responseVariableName, "rmse"),
      fittedValues = dataSamplePredictionsAndResiduals._1,
      residuals = dataSamplePredictionsAndResiduals._2,
      dataSample = dataSamplePredictionsAndResiduals._3,
      treeDebugString = fit.toDebugString
    )
  }
}
