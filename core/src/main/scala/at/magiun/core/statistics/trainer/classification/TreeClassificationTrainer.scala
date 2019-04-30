package at.magiun.core.statistics.trainer.classification

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.{DecisionTreeClassificationAlgorithm, GradientBoostTreeClassificationAlgorithm, RandomForestClassificationAlgorithm}
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.{CoefficientResponse, TrainAlgorithmResponse}
import org.apache.spark.ml._
import org.apache.spark.ml.classification._
import org.apache.spark.ml.feature._
import org.apache.spark.sql.DataFrame

object TreeClassificationTrainer extends ClassificationAlgorithmTrainer {

  def train(algorithm: DecisionTreeClassificationAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {

    val sparkAlgorithm = algorithm.createAndEnhanceAlgorithm
      .setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures")
    train(sparkAlgorithm, dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, sampleSize, AlgorithmImplementation.DecisionTreeClassificationAlgorithm)
  }

    def train(algorithm: RandomForestClassificationAlgorithm,
              dataFrame: DataFrame,
              responseVariableName: String,
              explanatoryVariablesNames: Array[String],
              magiunContext: MagiunContext,
              sampleSize: Int): TrainAlgorithmResponse = {

      val sparkAlgorithm = algorithm.createAndEnhanceAlgorithm
        .setLabelCol("indexedLabel")
        .setFeaturesCol("indexedFeatures")
      train(sparkAlgorithm, dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, sampleSize, AlgorithmImplementation.RandomForestClassificationAlgorithm)
    }

    def train(algorithm: GradientBoostTreeClassificationAlgorithm,
              dataFrame: DataFrame,
              responseVariableName: String,
              explanatoryVariablesNames: Array[String],
              magiunContext: MagiunContext,
              sampleSize: Int): TrainAlgorithmResponse = {

      val sparkAlgorithm = algorithm.createAndEnhanceAlgorithm
        .setLabelCol("indexedLabel")
        .setFeaturesCol("indexedFeatures")
      train(sparkAlgorithm, dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, sampleSize, AlgorithmImplementation.GradientBoostTreeClassificationAlgorithm)
    }


  private def train(sparkAlgorithm: Estimator[_ <: Any],
                    dataFrame: DataFrame,
                    responseVariableName: String,
                    explanatoryVariablesNames: Array[String],
                    magiunContext: MagiunContext,
                    sampleSize: Int,
                    algorithmImplementation: AlgorithmImplementation): TrainAlgorithmResponse = {

    val transformedDf = transformDF(dataFrame, explanatoryVariablesNames)
    val labelIndexer = getLabelIndexer(transformedDf, responseVariableName)
    val featureIndexer = getFeatureIndexer(transformedDf)
    val labelConverter = getLabelConvertor(labelIndexer)

    val pipeline = new Pipeline()
      .setStages(Array(labelIndexer, featureIndexer, sparkAlgorithm, labelConverter))
    val Array(trainingData, testData) = transformedDf.randomSplit(Array(0.7, 0.3))

    val fit = pipeline.fit(trainingData)
    val predictions = fit.transform(testData)

    val debutString = getDebugString(fit.stages(2), algorithmImplementation)
    val dataSampleAndPredictions = getDataSampleAndPredictedValues(testData.select(responseVariableName), predictions, sampleSize)
    magiunContext.addModelToCache(fit.uid, fit)

    TrainAlgorithmResponse(
      id = fit.uid,
      algorithmImplementation = algorithmImplementation,
      intercept = CoefficientResponse(responseVariableName),
      accuracy = evaluateFit(predictions, "indexedLabel", "accuracy"),
      weightedPrecision = evaluateFit(predictions, "indexedLabel", "weightedPrecision"),
      weightedRecall = evaluateFit(predictions, "indexedLabel", "weightedRecall"),
      f1 = evaluateFit(predictions, "indexedLabel", "f1"),
      fittedValues = dataSampleAndPredictions._1,
      dataSample = dataSampleAndPredictions._2,
      treeDebugString = debutString
    )
  }

  private def getLabelIndexer(df: DataFrame, responseVariableName: String): StringIndexerModel = {
    new StringIndexer()
      .setInputCol(responseVariableName)
      .setOutputCol("indexedLabel")
      .fit(df)
  }

  private def getFeatureIndexer(df: DataFrame): VectorIndexerModel = {
    new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(8) // features with > 8 distinct values are treated as continuous.
      .fit(df)
  }

  private def getLabelConvertor(labelIndexer: StringIndexerModel): IndexToString =  {
    new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labels)
  }

  private def getDebugString(model: Transformer, algorithmImplementation: AlgorithmImplementation): String = {
    algorithmImplementation match {
      case AlgorithmImplementation.DecisionTreeClassificationAlgorithm => model.asInstanceOf[DecisionTreeClassificationModel].toDebugString
      case AlgorithmImplementation.RandomForestClassificationAlgorithm => model.asInstanceOf[RandomForestClassificationModel].toDebugString
      case AlgorithmImplementation.GradientBoostTreeClassificationAlgorithm => model.asInstanceOf[GBTClassificationModel].toDebugString
      case _ => "No debug string!"
    }
  }
}
