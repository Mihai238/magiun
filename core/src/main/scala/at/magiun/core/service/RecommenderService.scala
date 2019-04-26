package at.magiun.core.service

import at.magiun.core.MagiunContext
import at.magiun.core.model.MagiunDataSet
import at.magiun.core.model.algorithm._
import at.magiun.core.model.data.DatasetMetadata
import at.magiun.core.model.ontology.OntologyClass
import at.magiun.core.model.rest.request.RecommenderRequest
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

  def recommend(request: RecommenderRequest): Future[Option[List[Algorithm[_ <: Estimator[_ <: Any]]]]] = {
    import scala.concurrent.duration._

    Future {
      Option {
        val dataset = Await.result(dataSetService.getDataSet(request.datasetId.toString), 30.seconds).get
        val magiunDataset = Await.result(dataSetService.find(request.datasetId.toString), 30.seconds).get
        val metadata = createMetadata(request, dataset, magiunDataset)
        val recommendations = algorithmRecommender.recommend(metadata)
        recommendationsRanker.rank(recommendations).map(mapOntologyClassToAlgorithm)
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
      case OntologyClass.LinearLeastRegressionPartial | OntologyClass.LinearLeastRegressionComplete => LinearRegressionAlgorithm(ontology.name)
      case OntologyClass.GeneralizedLinearRegressionPartial | OntologyClass.GeneralizedLinearRegressionComplete => GeneralizedLinearRegressionAlgorithm(ontology.name)
      case OntologyClass.BinaryLogisticRegressionPartial | OntologyClass.BinaryLogisticRegressionComplete => BinaryLogisticRegressionAlgorithm(ontology.name)
      case OntologyClass.OrdinalLogisticRegressionPartial | OntologyClass.OrdinalLogisticRegressionComplete => OrdinalLogisticRegressionAlgorithm(ontology.name)
      case OntologyClass.IsotonicRegression => IsotonicRegressionAlgorithm(ontology.name)
      case OntologyClass.SurvivalRegression => SurvivalRegressionAlgorithm(ontology.name)
      case OntologyClass.GradientBoostTreeRegressionPartial | OntologyClass.GradientBoostTreeRegressionComplete => GradientBoostTreeRegressionAlgorithm(ontology.name)
      case OntologyClass.RandomForestRegressionPartial | OntologyClass.RandomForestRegressionComplete => RandomForestRegressionAlgorithm(ontology.name)
      case OntologyClass.DecisionTreeRegressionPartial | OntologyClass.DecisionTreeRegressionComplete => DecisionTreeRegressionAlgorithm(ontology.name)
      case OntologyClass.MultinomialNaiveBayesClassification => MultinomialNaiveBayesClassificationAlgorithm(ontology.name)
      case OntologyClass.BernoulliNaiveBayesClassification => BernoulliNaiveBayesClassificationAlgorithm(ontology.name)
      case OntologyClass.LinearSupportVectorMachine => LinearSupportVectorMachineAlgorithm(ontology.name)
      case OntologyClass.MultilayerPerceptronClassification => MultilayerPerceptronClassificationAlgorithm(ontology.name)
      case OntologyClass.GradientBoostTreeClassification => GradientBoostTreeClassificationAlgorithm(ontology.name)
      case OntologyClass.RandomForestClassificationPartial | OntologyClass.RandomForestClassificationComplete => RandomForestClassificationAlgorithm(ontology.name)
      case OntologyClass.DecisionTreeClassificationPartial | OntologyClass.DecisionTreeClassificationComplete => DecisionTreeClassificationAlgorithm(ontology.name)
      case _ => null
    }
  }
}
