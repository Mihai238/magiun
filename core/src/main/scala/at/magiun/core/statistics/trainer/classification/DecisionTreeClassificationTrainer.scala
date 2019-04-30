package at.magiun.core.statistics.trainer.classification

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.DecisionTreeClassificationAlgorithm
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.{CoefficientResponse, TrainAlgorithmResponse}
import at.magiun.core.util.DatasetUtil
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier, RandomForestClassificationModel}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel
import org.apache.spark.sql.DataFrame

object DecisionTreeClassificationTrainer extends ClassificationAlgorithmTrainer {

  def train(algorithm: DecisionTreeClassificationAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {

    train(algorithm.createAndEnhanceAlgorithm, dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, sampleSize, AlgorithmImplementation.DecisionTreeClassificationAlgorithm)
  }
/*
  def train(algorithm: RandomForestClassificationAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {

    train(algorithm.createAndEnhanceAlgorithm, dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, sampleSize, AlgorithmImplementation.RandomForestClassificationAlgorithm)
  }

  def train(algorithm: GradientBoostTreeClassificationAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {

    train(algorithm.createAndEnhanceAlgorithm, dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, sampleSize, AlgorithmImplementation.GradientBoostTreeClassificationAlgorithm)
  }
*/

  private def train(algorithm: DecisionTreeClassifier,
                    dataFrame: DataFrame,
                    responseVariableName: String,
                    explanatoryVariablesNames: Array[String],
                    magiunContext: MagiunContext,
                    sampleSize: Int,
                    algorithmImplementation: AlgorithmImplementation): TrainAlgorithmResponse = {

    val transformedDf = transformDF(dataFrame, explanatoryVariablesNames)

    val labelIndexer = new StringIndexer()
      .setInputCol(responseVariableName)
      .setOutputCol("indexedLabel")
      .fit(transformedDf)

    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(8) // features with > 8 distinct values are treated as continuous.
      .fit(transformedDf)

    val Array(trainingData, testData) = transformedDf.randomSplit(Array(0.7, 0.3))

    val sparkAlgorithm = algorithm
      .setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures")

    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labels)

    val pipeline = new Pipeline()
      .setStages(Array(labelIndexer, featureIndexer, sparkAlgorithm, labelConverter))

    val fit = pipeline.fit(trainingData)
    val predictions = fit.transform(testData)

    val debutString = algorithmImplementation match {
      case AlgorithmImplementation.DecisionTreeClassificationAlgorithm => fit.stages(2).asInstanceOf[DecisionTreeClassificationModel].toDebugString
      case AlgorithmImplementation.RandomForestClassificationAlgorithm => fit.stages(2).asInstanceOf[RandomForestClassificationModel].toDebugString
      case AlgorithmImplementation.GradientBoostTreeClassificationAlgorithm => fit.stages(2).asInstanceOf[GradientBoostedTreesModel].toDebugString
      case _ => "No debug string!"
    }

    val dataSampleAndPredictions = getDataSampleAndPredictedValues(testData.select(responseVariableName), predictions, sampleSize)

    TrainAlgorithmResponse(
      id = fit.uid,
      algorithmImplementation = algorithmImplementation,
      intercept = CoefficientResponse(responseVariableName),
      accuracy = evaluateFit(predictions, "accuracy"),
      weightedPrecision = evaluateFit(predictions, "weightedPrecision"),
      weightedRecall = evaluateFit(predictions, "weightedRecall"),
      f1 = evaluateFit(predictions, "f1"),
      fittedValues = dataSampleAndPredictions._1,
      dataSample = dataSampleAndPredictions._2,
      treeDebugString = debutString
    )
  }

  private def evaluateFit(predictions: DataFrame, metric: String): Double = {
    new MulticlassClassificationEvaluator()
      .setLabelCol("indexedLabel")
      .setPredictionCol("prediction")
      .setMetricName(metric)
      .evaluate(predictions)
  }



  private def getDataSampleAndPredictedValues(responseVariable: DataFrame, predictions: DataFrame, sampleSize: Int): (Seq[Double], Seq[Double]) = {
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
}
