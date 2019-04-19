package at.magiun.core

import at.magiun.core.config.{AlgorithmOntologyConfig, H2Config, OntologyConfig, SparkConfig}
import at.magiun.core.feature._
import at.magiun.core.repository.{BlockRepository, DataSetRepository, DatabaseInitializer}
import at.magiun.core.rest._
import at.magiun.core.service._
import at.magiun.core.statistics.{AlgorithmRecommender, ColumnMetadataCalculator, DatasetMetadataCalculator, StatisticsCalculator}
import com.softwaremill.macwire._
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession

//noinspection TypeAnnotation
trait MainModule {

  lazy val magiunContext = wire[MagiunContext]

  // Rest
  lazy val dataSetController = wire[DataSetController]
  lazy val blockController = wire[BlockController]
  lazy val executionController = wire[ExecutionController]
  lazy val recommenderController = wire[AlgorithmController]
  lazy val restApi = wire[RestApi]

  // Services
  lazy val jobService = wire[JobService]
  lazy val blockService = wire[BlockService]
  lazy val dataSetService = wire[DataSetService]
  lazy val executionService = wire[ExecutionService]
  lazy val executionContext = wire[ExecutionContext]
  lazy val recommenderService = wire[RecommenderService]
  lazy val recommendatiosRanker = wire[RecommendationsRanker]

  // Calculators
  lazy val columnMetadataCalculator = wire[ColumnMetadataCalculator]
  lazy val datasetMetadataCalculator = wire[DatasetMetadataCalculator]
  lazy val statisticsCalculator = wire[StatisticsCalculator]

  // Recommenders
  lazy val recommender = wire[Recommender]
  lazy val restrictionBuilder = wire[RestrictionBuilder]
  lazy val algorithmRecommender = wire[AlgorithmRecommender]

  // Repositories
  lazy val blockRepository = wire[BlockRepository]
  lazy val dataSetRepository = wire[DataSetRepository]
  lazy val databaseInitializer = wire[DatabaseInitializer]

  // Configs
  lazy val config = ConfigFactory.load()
  lazy val spark: SparkSession = wireWith(SparkConfig.create _)
  lazy val h2 = wireWith(H2Config.create _)
  lazy val algorithmOntology = AlgorithmOntologyConfig.create()
  lazy val model = OntologyConfig.create()
}
