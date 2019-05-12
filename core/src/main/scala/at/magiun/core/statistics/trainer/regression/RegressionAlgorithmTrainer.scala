package at.magiun.core.statistics.trainer.regression

import at.magiun.core.model.rest.response.CoefficientResponse
import at.magiun.core.util.DatasetUtil
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{DataFrame, Row}

trait RegressionAlgorithmTrainer {

  protected def transformDF(
                            dataFrame: DataFrame,
                            explanatoryVariablesNames: Array[String]
                           ): DataFrame = {
    val vectorAssembler = new VectorAssembler()
      .setInputCols(explanatoryVariablesNames)
      .setOutputCol("features")

    vectorAssembler.transform(dataFrame)
  }

  protected def createIntercept(
                                 responseVariableName: String,
                                 intercept: Double,
                                 standardErrors: Array[Double],
                                 tValues: Array[Double],
                                 pValues: Array[Double]
                               ): CoefficientResponse = {
    val interceptStandardError = if (intercept != 0) standardErrors.last else 0.0
    val interceptTValue = if (intercept != 0) tValues.last else 0.0
    val interceptPValue = if (intercept != 0) pValues.last else 0.0

    CoefficientResponse(responseVariableName, intercept, interceptStandardError, interceptTValue, interceptPValue)
  }


  protected def createCoefficients(
                                    coefficients: Array[Double],
                                    standardErrors: Array[Double],
                                    tValues: Array[Double],
                                    pValues: Array[Double],
                                    explanatoryVariablesNames: Array[String]
                                  ): Seq[CoefficientResponse] = {
    explanatoryVariablesNames.indices
      .map(i => CoefficientResponse(
        explanatoryVariablesNames(i),
        coefficients(i),
        standardErrors(i),
        tValues(i),
        pValues(i)
      ))
  }

  protected def getDataSampleFittedValuesAndResiduals(
                                                       data: DataFrame,
                                                       predictions: DataFrame,
                                                       residuals: DataFrame,
                                                       sampleSize: Int
                                                     ): (Seq[Double], Seq[Double], Seq[Double]) = {
    val dataCount: Int = data.count().intValue()

    var dataSample: Array[Row] = Array.empty
    var predictionsArray: Array[Row] = Array.empty
    var residualsArray: Array[Row] = Array.empty

    if (sampleSize > dataCount) {
      dataSample = data.collect()
      predictionsArray = predictions.collect()
      residualsArray = residuals.collect()
    } else {
      val randomIndices = DatasetUtil.getRandomIndices(sampleSize, dataCount)
      dataSample = DatasetUtil.getRowsByIndices(data, randomIndices)
      predictionsArray = DatasetUtil.getRowsByIndices(predictions, randomIndices)
      residualsArray = DatasetUtil.getRowsByIndices(residuals, randomIndices)
    }

    (
      predictionsArray.map(r => r.getDouble(0)),
      residualsArray.map(r => r.getDouble(0)),
      dataSample.map(r => r.get(0).toString.toDouble)
    )
  }

  protected def getDataSampleFittedValuesAndResiduals(
                                                       data: Seq[Double],
                                                       predictions: Seq[Double],
                                                       residuals: Seq[Double],
                                                       sampleSize: Int
                                                     ): (Seq[Double], Seq[Double], Seq[Double]) = {
    val dataCount: Int = data.size

    if (sampleSize > dataCount) {
      (
        predictions,
        residuals,
        data
      )
    } else {
      val randomIndices = DatasetUtil.getRandomIndices(sampleSize, dataCount)
      (
        DatasetUtil.getValuesByIndices(predictions, randomIndices),
        DatasetUtil.getValuesByIndices(residuals, randomIndices),
        DatasetUtil.getValuesByIndices(data, randomIndices)
      )
    }
  }
}
