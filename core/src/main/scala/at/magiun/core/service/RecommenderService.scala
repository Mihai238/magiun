package at.magiun.core.service

import at.magiun.core.model.data.{DatasetMetadata, Distribution, VariableType}
import at.magiun.core.model.request.RecommenderRequest
import at.magiun.core.model.statistics.StatisticsUtil
import at.magiun.core.model.{ColumnType, MagiunDataSet}
import at.magiun.core.statistics.AlgorithmRecommender
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.concurrent.Await

class RecommenderService(spark: SparkSession, dataSetService: DataSetService, algoRecommender: AlgorithmRecommender) {

  def recommend(request: RecommenderRequest): Unit = {
    import scala.concurrent.duration._

    val dataset = Await.result(dataSetService.getDataSet(request.datasetId.toString), 10.seconds).get
    val magiunDataset = Await.result(dataSetService.find(request.datasetId.toString), 10.seconds).get
    val metadata = createMetadata(request, dataset, magiunDataset)
    val result = algoRecommender.recommend(metadata)
    println(result)
  }

  private def createMetadata(request: RecommenderRequest, dataset: Dataset[Row], magiunDataset: MagiunDataSet): DatasetMetadata = {
    calculateDistributions(dataset)

    DatasetMetadata(
      getVariableTypes(magiunDataset),
      Seq.fill(dataset.columns.length)(Distribution.Normal),
      request.responseVariable,
      request.variablesToIgnore,
      dataset.columns.length,
      dataset.count()
    )
  }

  private def getVariableTypes(magiunDataset: MagiunDataSet): Seq[VariableType] = {
    magiunDataset
      .schema
      .get
      .columns
      .map(c => getVariableTypeForColumnType(c.`type`))
  }

  private def getVariableTypeForColumnType(columnType: ColumnType): VariableType = {
    columnType match {
      case ColumnType.String => VariableType.Text
      case ColumnType.Boolean => VariableType.Binary
      case ColumnType.Date => VariableType.Categorical
      case ColumnType.Int | ColumnType.Double => VariableType.Continuous
      case _ => VariableType.Text
    }
  }

  private def calculateDistributions(dataset: Dataset[Row]): Seq[Distribution] = {
    import spark.implicits._

    dataset.describe().show()

    val doubleCol = dataset.map(r => r.getAs[Double]("Age")).rdd

    println("Age is normally distributed:  " + isNormallyDistributed(doubleCol))

    Seq()
  }

  private def isNormallyDistributed(column: RDD[Double]): Boolean = {
    val testResult = Statistics.kolmogorovSmirnovTest(column, "norm", 29.7, 14.5) //mean & stddev of the column
    println(testResult)

    !(testResult.pValue <= StatisticsUtil.NORMALITY_TEST_MAX_P_VALUE)
  }

}
