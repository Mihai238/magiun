package at.magiun.core.statistics.trainer.regression

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.DecisionTreeRegressionAlgorithm
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.{CoefficientResponse, TrainAlgorithmResponse}
import org.apache.spark.ml.regression.{DecisionTreeRegressionModel, DecisionTreeRegressor}
import org.apache.spark.sql.DataFrame

object DecisionTreeRegressionTrainer extends TreeRegressionAlgorithmTrainer {

  def train(algorithm: DecisionTreeRegressionAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int
           ): TrainAlgorithmResponse = {
    val sparkAlgorithm: DecisionTreeRegressor = new DecisionTreeRegressor()
    val transformedDF = transformDF(dataFrame, explanatoryVariablesNames)

    sparkAlgorithm.setLabelCol(responseVariableName)
      .setFeaturesCol("features")

    val Array(trainingData, testData) = transformedDF.randomSplit(Array(0.7, 0.3))

    val fit: DecisionTreeRegressionModel = sparkAlgorithm.fit(trainingData)
    magiunContext.addModelToCache(fit.uid, fit)

    val predictions = fit.transform(testData)

    val dataSamplePredictionsAndResiduals = getDataSampleFittedValuesAndResiduals(testData.select(responseVariableName), predictions, sampleSize)

    TrainAlgorithmResponse(
      id = fit.uid,
      algorithmImplementation = AlgorithmImplementation.DecisionTreeRegressionAlgorithm,
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
