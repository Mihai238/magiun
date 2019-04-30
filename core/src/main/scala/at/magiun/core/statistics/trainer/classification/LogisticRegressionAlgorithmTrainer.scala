package at.magiun.core.statistics.trainer.classification

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.{BinaryLogisticRegressionAlgorithm, MultinomialLogisticRegressionAlgorithm}
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.response.{ClassificationInterceptResponse, CoefficientResponse, TrainAlgorithmResponse}
import org.apache.spark.ml.attribute.AttributeGroup
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionTrainingSummary}
import org.apache.spark.sql.DataFrame

object LogisticRegressionAlgorithmTrainer extends ClassificationAlgorithmTrainer {

  def train(algorithm: BinaryLogisticRegressionAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {
    train(algorithm.createAndEnhanceAlgorithm, dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, sampleSize, AlgorithmImplementation.BinaryLogisticRegressionAlgorithm)
  }

  def train(algorithm: MultinomialLogisticRegressionAlgorithm,
            dataFrame: DataFrame,
            responseVariableName: String,
            explanatoryVariablesNames: Array[String],
            magiunContext: MagiunContext,
            sampleSize: Int): TrainAlgorithmResponse = {
    train(algorithm.createAndEnhanceAlgorithm, dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, sampleSize, AlgorithmImplementation.MultinomialLogisticRegressionAlgorithm)
  }

  private def train(sparkAlgorithm: LogisticRegression,
                    dataFrame: DataFrame,
                    responseVariableName: String,
                    explanatoryVariablesNames: Array[String],
                    magiunContext: MagiunContext,
                    sampleSize: Int,
                    implementation: AlgorithmImplementation): TrainAlgorithmResponse = {
    val transformedDF = transformDF(dataFrame, explanatoryVariablesNames)
    sparkAlgorithm.setLabelCol(responseVariableName)
      .setFeaturesCol("features")

    val fit = sparkAlgorithm.fit(transformedDF)
    val summary = fit.summary

    val schema = fit.transformSchema(transformedDF.schema)
    val featureAttrs = AttributeGroup.fromStructField(schema(fit.getFeaturesCol)).attributes.get
    val features: Array[String] = featureAttrs.map(_.name.get)
    val featureNames: Array[String] = if (fit.getFitIntercept) {
      Array(responseVariableName) ++ features
    } else {
      features
    }

    val lrModelCoeffs = fit.coefficientMatrix.toArray
    val coeffs = if (fit.getFitIntercept) {
      lrModelCoeffs ++ fit.interceptVector.toArray
    } else {
      lrModelCoeffs
    }

    val coeffsWithNames = featureNames.zip(coeffs)
    val intercept = coeffsWithNames.head

    magiunContext.addModelToCache(fit.uid, fit)

    TrainAlgorithmResponse(
      id = fit.uid,
      algorithmImplementation = implementation,
      intercept = CoefficientResponse(responseVariableName, intercept._2),
      classificationIntercept = createClassificationIntercept(summary),
      coefficients = createCoefficients(coeffsWithNames.filterNot(c => c._1.equals(responseVariableName))),
      accuracy = summary.accuracy,
      falsePositiveRate = summary.weightedFalsePositiveRate,
      truePositiveRate = summary.weightedTruePositiveRate,
      fMeasure = summary.weightedFMeasure,
      precision = summary.weightedPrecision,
      recall = summary.weightedRecall
    )
  }

  private def createClassificationIntercept(summary: LogisticRegressionTrainingSummary): Seq[ClassificationInterceptResponse] = {
    val fpr = summary.falsePositiveRateByLabel.zipWithIndex
    val tpr = summary.truePositiveRateByLabel.zipWithIndex
    val precision = summary.precisionByLabel.zipWithIndex
    val recall = summary.recallByLabel.zipWithIndex
    val fMeasure = summary.fMeasureByLabel.zipWithIndex

    fpr.indices.map(i => ClassificationInterceptResponse(i.toString, fpr(i)._1, tpr(i)._1, precision(i)._1, recall(i)._1, fMeasure(i)._1))
  }
}
