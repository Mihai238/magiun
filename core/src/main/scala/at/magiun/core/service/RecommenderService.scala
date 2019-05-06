package at.magiun.core.service

import at.magiun.core.MagiunContext
import at.magiun.core.model.MagiunDataSet
import at.magiun.core.model.algorithm._
import at.magiun.core.model.data.DatasetMetadata
import at.magiun.core.model.ontology.OntologyClass
import at.magiun.core.model.rest.request.RecommenderRequest
import at.magiun.core.model.rest.response.RecommenderResponse
import at.magiun.core.statistics.{AlgorithmRecommender, DatasetMetadataCalculator}
import at.magiun.core.util.{DatasetUtil, MagiunDatasetUtil}
import org.apache.spark.ml.Estimator
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

class RecommenderService(
                          spark: SparkSession,
                          dataSetService: DataSetService,
                          datasetMetadataCalculator: DatasetMetadataCalculator,
                          algorithmRecommender: AlgorithmRecommender,
                          recommendationsRanker: RecommendationsRanker,
                          magiunContext: MagiunContext
                        ) {

  def recommend(request: RecommenderRequest): Future[Option[RecommenderResponse]] = {
    import scala.concurrent.duration._

    Future {
      Option {
        val dataset = Await.result(dataSetService.getDataSet(request.datasetId.toString), 30.seconds).get
        val magiunDataset = Await.result(dataSetService.find(request.datasetId.toString), 30.seconds).get
        val metadata = createMetadata(request, dataset, magiunDataset)
        val recommendations = algorithmRecommender.recommend(metadata)
        val algos = recommendationsRanker.rank(recommendations).map(mapOntologyClassToAlgorithm)

        magiunContext.addRecommenderRequest(request)
        algos.foreach(magiunContext.addRecommendation)

        RecommenderResponse(
          request.uid,
          algos
        )
      }
    }
  }

  private def createMetadata(request: RecommenderRequest, dataset: Dataset[Row], magiunDataset: MagiunDataSet): DatasetMetadata = {
    val explanatoryVariables = request.explanatoryVariables
    val responseVariable = request.responseVariable

    val cleanDataset = DatasetUtil.cleanDatasetFromUnnecessaryVariables(dataset, responseVariable, explanatoryVariables)
    val cleanMagiunDataset = MagiunDatasetUtil.cleanDatasetFromUnnecessaryVariables(magiunDataset, responseVariable, explanatoryVariables)

    magiunContext.addDataFrameToCache(magiunDataset.id, cleanDataset)
    magiunContext.addMagiunSchemaToCache(magiunDataset.id, cleanMagiunDataset.schema.get)

    datasetMetadataCalculator.compute(request, cleanDataset, cleanMagiunDataset)
  }

  private def mapOntologyClassToAlgorithm(ontology: OntologyClass): Algorithm[_ <: Estimator[_ <: Any]] = {
    ontology match {
      case OntologyClass.LinearLeastRegressionPartial | OntologyClass.LinearLeastRegressionComplete => LinearRegressionAlgorithm(name = ontology.name)
      case OntologyClass.GeneralizedLinearRegressionPartial | OntologyClass.GeneralizedLinearRegressionComplete => GeneralizedLinearRegressionAlgorithm(name = ontology.name)
      case OntologyClass.BinaryLogisticRegressionPartial | OntologyClass.BinaryLogisticRegressionComplete => BinaryLogisticRegressionAlgorithm(name = ontology.name)
      case OntologyClass.MultinomialLogisticRegressionPartial | OntologyClass.MultinomialLogisticRegressionComplete => MultinomialLogisticRegressionAlgorithm(name = ontology.name)
      case OntologyClass.IsotonicRegression => IsotonicRegressionAlgorithm(name = ontology.name)
      case OntologyClass.SurvivalRegression => SurvivalRegressionAlgorithm(name = ontology.name)
      case OntologyClass.GradientBoostTreeRegressionPartial | OntologyClass.GradientBoostTreeRegressionComplete => GradientBoostTreeRegressionAlgorithm(name = ontology.name)
      case OntologyClass.RandomForestRegressionPartial | OntologyClass.RandomForestRegressionComplete => RandomForestRegressionAlgorithm(name = ontology.name)
      case OntologyClass.DecisionTreeRegressionPartial | OntologyClass.DecisionTreeRegressionComplete => DecisionTreeRegressionAlgorithm(name = ontology.name)
      case OntologyClass.MultinomialNaiveBayesClassification => MultinomialNaiveBayesClassificationAlgorithm(name = ontology.name)
      case OntologyClass.BernoulliNaiveBayesClassification => BernoulliNaiveBayesClassificationAlgorithm(name = ontology.name)
      case OntologyClass.LinearSupportVectorMachine => LinearSupportVectorMachineAlgorithm(name = ontology.name)
      case OntologyClass.MultilayerPerceptronClassification => MultilayerPerceptronClassificationAlgorithm(name = ontology.name)
      case OntologyClass.GradientBoostTreeClassification => GradientBoostTreeClassificationAlgorithm(name = ontology.name)
      case OntologyClass.RandomForestClassificationPartial | OntologyClass.RandomForestClassificationComplete => RandomForestClassificationAlgorithm(name = ontology.name)
      case OntologyClass.DecisionTreeClassificationPartial | OntologyClass.DecisionTreeClassificationComplete => DecisionTreeClassificationAlgorithm(name = ontology.name)
      case _ => null
    }
  }

  def like(requestId: String, recommendationId: String): Unit = {
    val request = magiunContext.getRecommenderRequest(requestId)
    val recommendation = Option(magiunContext.getRecommendation(recommendationId))

    print("sss")
  }

  def dislike(requestId: String, recommendationId: String): Unit = {

  }
}
