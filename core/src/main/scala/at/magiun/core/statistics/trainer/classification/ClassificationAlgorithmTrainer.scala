package at.magiun.core.statistics.trainer.classification

import at.magiun.core.model.rest.response.CoefficientResponse
import at.magiun.core.util.DatasetUtil
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.DataFrame

trait ClassificationAlgorithmTrainer {
  protected def transformDF(
                             dataFrame: DataFrame,
                             explanatoryVariablesNames: Array[String]
                           ): DataFrame = {
    val vectorAssembler = new VectorAssembler()
      .setInputCols(explanatoryVariablesNames)
      .setOutputCol("features")

    vectorAssembler.transform(dataFrame)
  }

  protected def evaluateFit(predictions: DataFrame, label: String, metric: String): Double = {
    new MulticlassClassificationEvaluator()
      .setLabelCol(label)
      .setPredictionCol("prediction")
      .setMetricName(metric)
      .evaluate(predictions)
  }

  protected def getDataSampleAndPredictedValues(responseVariable: DataFrame, predictions: DataFrame, sampleSize: Int): (Seq[Double], Seq[Double]) = {
    val predictionsSeq = predictions.select("prediction").collect().map(r => r.getDouble(0)).toSeq
    val responseVariableSeq = responseVariable.collect().map(r => r.get(0).toString.toDouble).toSeq

    val dataCount: Int = responseVariableSeq.size

    if (sampleSize > dataCount) {
      (
        predictionsSeq,
        responseVariableSeq
      )
    } else {
      val randomIndices = DatasetUtil.getRandomIndices(sampleSize, dataCount)
      (
        DatasetUtil.getValuesByIndices(predictionsSeq, randomIndices),
        DatasetUtil.getValuesByIndices(responseVariableSeq, randomIndices)
      )
    }
  }

  protected def createCoefficients(coefficientsWithNames: Array[(String, Double)]): Seq[CoefficientResponse] = {
    coefficientsWithNames.map(c => CoefficientResponse(c._1, c._2))
  }

}
