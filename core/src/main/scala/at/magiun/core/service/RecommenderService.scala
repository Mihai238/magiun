package at.magiun.core.service

import at.magiun.core.model.{MagiunDataSet, Schema}
import at.magiun.core.model.data.DatasetMetadata
import at.magiun.core.model.request.RecommenderRequest
import at.magiun.core.statistics.{AlgorithmRecommender, DatasetMetadataCalculator}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.concurrent.Await

class RecommenderService(spark: SparkSession, dataSetService: DataSetService, datasetMetadataCalculator: DatasetMetadataCalculator, algorithmRecommender: AlgorithmRecommender) {

  def recommend(request: RecommenderRequest): Unit = {
    import scala.concurrent.duration._

    val dataset = Await.result(dataSetService.getDataSet(request.datasetId.toString), 10.seconds).get
    val magiunDataset = Await.result(dataSetService.find(request.datasetId.toString), 10.seconds).get
    val metadata = createMetadata(request, dataset, magiunDataset)
    val result = algorithmRecommender.recommend(metadata)
    println(result)
  }

  private def createMetadata(request: RecommenderRequest, dataset: Dataset[Row], magiunDataset: MagiunDataSet): DatasetMetadata = {
    val explanatoryVariables = request.explanatoryVariables
    val targetVariable = request.responseVariable
    val columnsToRemove: Seq[String] = dataset.columns.indices
      .filter(i => !(explanatoryVariables.contains(i) || i == targetVariable))
      .map(i => dataset.columns(i))
    val cleanDataset = columnsToRemove.foldLeft(dataset)((df, col) => df.drop(col))

    val cleanColumns = columnsToRemove.foldLeft(magiunDataset.schema.get.columns)((l, r) => l.filterNot(_.name == r))
    val cleanMagiunDataset = MagiunDataSet(magiunDataset.id, magiunDataset.name, magiunDataset.dataSetSource, Option(Schema(cleanColumns, cleanColumns.length)))

    datasetMetadataCalculator.compute(request, cleanDataset, cleanMagiunDataset)
  }
}
