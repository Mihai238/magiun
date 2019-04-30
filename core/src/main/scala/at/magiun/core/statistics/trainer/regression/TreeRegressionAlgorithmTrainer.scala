package at.magiun.core.statistics.trainer.regression

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.sql.DataFrame

trait TreeRegressionAlgorithmTrainer extends RegressionAlgorithmTrainer {


  protected def evaluateFit(predictions: DataFrame, responseVariableName: String, metric: String): Double = {
    new RegressionEvaluator()
      .setLabelCol(responseVariableName)
      .setPredictionCol("prediction")
      .setMetricName(metric)
      .evaluate(predictions)
  }

  protected def getDataSampleFittedValuesAndResiduals(responseVariable: DataFrame, predictions: DataFrame, sampleSize: Int): (Seq[Double], Seq[Double], Seq[Double]) = {
    val predictionsSeq = predictions.select("prediction").collect().map(r => r.getDouble(0)).toSeq
    val responseVariableSeq = responseVariable.collect().map(r => r.getDouble(0)).toSeq

    val residuals = responseVariableSeq.indices.map(i => responseVariableSeq(i) - predictionsSeq(i))

    getDataSampleFittedValuesAndResiduals(responseVariableSeq, predictionsSeq, residuals, sampleSize)
  }
}
