package at.magiun.core.service

import at.magiun.core.model.algorithm.{Algorithm, GeneralizedLinearRegressionAlgorithm, LinearRegressionAlgorithm}
import at.magiun.core.model.data.DatasetMetadata
import at.magiun.core.model.ontology.OntologyClass
import at.magiun.core.model.request.RecommenderRequest
import at.magiun.core.model.{MagiunDataSet, Schema}
import at.magiun.core.statistics.{AlgorithmRecommender, DatasetMetadataCalculator}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

class RecommenderService(
                          spark: SparkSession,
                          dataSetService: DataSetService,
                          datasetMetadataCalculator: DatasetMetadataCalculator,
                          algorithmRecommender: AlgorithmRecommender,
                          recommendationsRanker: RecommendationsRanker
                        ) {

  def recommend(request: RecommenderRequest): Future[Option[Set[Algorithm]]] = {
    import scala.concurrent.duration._

    Future {
      Option {
        val dataset = Await.result(dataSetService.getDataSet(request.datasetId.toString), 30.seconds).get
        val magiunDataset = Await.result(dataSetService.find(request.datasetId.toString), 10.seconds).get
        val metadata = createMetadata(request, dataset, magiunDataset)
        val recommendations = algorithmRecommender.recommend(metadata)
        val r = recommendationsRanker.rank(recommendations).map(mapOntologyClassToAlgorithm).toSeq
        val a = r(2)
        r.toSet
      }
    }
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

  private def mapOntologyClassToAlgorithm(ontology: OntologyClass): Algorithm = {
    val r = Math.round(Math.random() * 10)

    if (r % 2 == 0) {
      GeneralizedLinearRegressionAlgorithm(ontology.name, "")
    } else {
      LinearRegressionAlgorithm(ontology.name, "")
    }
  }
}
