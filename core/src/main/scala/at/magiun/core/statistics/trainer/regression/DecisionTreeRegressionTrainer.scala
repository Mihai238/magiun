package at.magiun.core.statistics.trainer.regression

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.DecisionTreeRegressionAlgorithm
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.{CoefficientResponse, TrainAlgorithmResponse}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.{DecisionTreeRegressionModel, DecisionTreeRegressor}
import org.apache.spark.sql.DataFrame

object DecisionTreeRegressionTrainer extends RegressionAlgorithmTrainer {

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
    val responseVariable = testData.select(responseVariableName)

    val predictionsSeq = predictions.select("prediction").collect().map(r => r.getDouble(0)).toSeq
    val responseVariableSeq = responseVariable.collect().map(r => r.getDouble(0)).toSeq

    val residuals = responseVariableSeq.indices.map(i => responseVariableSeq(i) - predictionsSeq(i))

    val dataSamplePredictionsAndResiduals = getDataSampleFittedValuesAndResiduals(
      responseVariableSeq, predictionsSeq, residuals, sampleSize)

    val rmse = evaluateFit(predictions, responseVariableName, "rmse") // root mean squared error
    val mse = evaluateFit(predictions, responseVariableName, "mse") // mean squared error
    val r2 = evaluateFit(predictions, responseVariableName, "r2") // r squared
    val mae = evaluateFit(predictions, responseVariableName, "mae") // mean absolute error

    TrainAlgorithmResponse(
      id = fit.uid,
      algorithmImplementation = AlgorithmImplementation.DecisionTreeRegressionAlgorithm,
      intercept = CoefficientResponse(responseVariableName),
      meanSquaredError = mse,
      meanAbsoluteError = mae,
      rSquared = r2,
      rootMeanSquaredError = rmse,
      fittedValues = dataSamplePredictionsAndResiduals._1,
      residuals = dataSamplePredictionsAndResiduals._2,
      dataSample = dataSamplePredictionsAndResiduals._3,
      treeDebugString = fit.toDebugString
    )
  }

  def evaluateFit(predictions: DataFrame, responseVariableName: String, metric: String): Double = {
    new RegressionEvaluator()
      .setLabelCol(responseVariableName)
      .setPredictionCol("prediction")
      .setMetricName(metric)
      .evaluate(predictions)
  }

}
