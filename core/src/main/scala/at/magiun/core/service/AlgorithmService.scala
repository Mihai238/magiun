package at.magiun.core.service

import at.magiun.core.MagiunContext
import at.magiun.core.model.Schema
import at.magiun.core.model.algorithm._
import at.magiun.core.model.rest.AlgorithmImplementation
import at.magiun.core.model.rest.request.{AlgorithmParameterRequest, AlgorithmRequest, TrainAlgorithmRequest}
import at.magiun.core.model.rest.response.TrainAlgorithmResponse
import at.magiun.core.statistics.trainer.AlgorithmTrainer
import at.magiun.core.util.{DatasetUtil, MagiunDatasetUtil}
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.ml.Estimator
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class AlgorithmService (
                         spark: SparkSession,
                         dataSetService: DataSetService,
                         algorithmTrainer: AlgorithmTrainer,
                         magiunContext: MagiunContext,
                         config: Config
                       ) extends LazyLogging {

  def train(trainAlgorithmRequest: TrainAlgorithmRequest): Future[Option[TrainAlgorithmResponse]] = {
    val algorithm = toAlgorithm(trainAlgorithmRequest.algorithm)

    val responseVariable = trainAlgorithmRequest.responseVariable
    val explanatoryVariables = trainAlgorithmRequest.explanatoryVariables
    val datasetId = trainAlgorithmRequest.datasetId.toString
    val dataFrame = magiunContext.getDataFrame(datasetId).getOrElse(getDataFrame(datasetId, responseVariable, explanatoryVariables))
    val magiunSchema = magiunContext.getMagiunSchema(datasetId).getOrElse(getMagiunSchema(datasetId, responseVariable, explanatoryVariables))

    val responseVariableName = magiunSchema.columns.find(c => c.index == responseVariable).get.name
    val explanatoryVariablesNames = magiunSchema.columns.filterNot(c => c.index == responseVariable).map(c => c.name).toArray

    Future {
      Option {
        algorithmTrainer.train(algorithm, dataFrame, responseVariableName, explanatoryVariablesNames)
      }
    }
  }

  private def getDataFrame(datasetId: String, responseVariable: Int, explanatoryVariables: Seq[Int]): DataFrame = {
    val dataFrame = Await.result(dataSetService.getDataSet(datasetId), 30.seconds).get
    val cleanDataFrame = DatasetUtil.cleanDatasetFromUnnecessaryVariables(dataFrame, responseVariable, explanatoryVariables)
    magiunContext.addDataFrameToCache(datasetId, cleanDataFrame)
    cleanDataFrame
  }

  private def getMagiunSchema(datasetId: String, responseVariable: Int, explanatoryVariables: Seq[Int]): Schema = {
    val magiunDataset = Await.result(dataSetService.find(datasetId), 30.seconds).get
    val schema = MagiunDatasetUtil.cleanDatasetFromUnnecessaryVariables(magiunDataset, responseVariable, explanatoryVariables).schema.get
    magiunContext.addMagiunSchemaToCache(datasetId, schema)
    schema
  }

  private def toAlgorithm(algorithmRequest: AlgorithmRequest): Algorithm[_ <: Estimator[_ <: Any]] = {
    algorithmRequest.implementation match {
      case AlgorithmImplementation.LinearRegressionAlgorithm => LinearRegressionAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.GeneralizedLinearRegressionAlgorithm => GeneralizedLinearRegressionAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.BinaryLogisticRegressionAlgorithm => BinaryLogisticRegressionAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.MultinomialLogisticRegressionAlgorithm => MultinomialLogisticRegressionAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.IsotonicRegressionAlgorithm => IsotonicRegressionAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.SurvivalRegressionAlgorithm => SurvivalRegressionAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.GradientBoostTreeRegressionAlgorithm => GradientBoostTreeRegressionAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.RandomForestRegressionAlgorithm => RandomForestRegressionAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.DecisionTreeRegressionAlgorithm => DecisionTreeRegressionAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.MultinomialNaiveBayesClassificationAlgorithm => MultinomialNaiveBayesClassificationAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.BernoulliNaiveBayesClassificationAlgorithm => BernoulliNaiveBayesClassificationAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.LinearSupportVectorMachineAlgorithm => LinearSupportVectorMachineAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.MultilayerPerceptronClassificationAlgorithm => MultilayerPerceptronClassificationAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.RandomForestClassificationAlgorithm => RandomForestClassificationAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.GradientBoostTreeClassificationAlgorithm => GradientBoostTreeClassificationAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case AlgorithmImplementation.DecisionTreeClassificationAlgorithm => DecisionTreeClassificationAlgorithm(name = algorithmRequest.name, parameters = toParameters(algorithmRequest.parameters))
      case _ => throw new IllegalArgumentException(s"Unknown algorithm implementation ${algorithmRequest.implementation} !")
    }
  }

  private def toParameters(parameters: Set[AlgorithmParameterRequest]): Set[AlgorithmParameter[_ <: Any]] = {
    parameters.map{p => AlgorithmParameter.createParameterWithValueByName(p.name, p.value)}
  }

  def remove(id: String): Unit = {
    magiunContext.removeModel(id)
  }

  def save(id: String): Unit = {
    val model = magiunContext.getModel(id)
    model.foreach { m =>
      m.save(System.getProperty("user.dir").concat(config.getString("models.saveFolder")).concat(m.uid))
    }
  }
}
