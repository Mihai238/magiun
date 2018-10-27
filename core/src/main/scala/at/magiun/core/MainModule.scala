package at.magiun.core

import at.magiun.core.config.{H2Config, OntologyConfig, SparkConfig}
import at.magiun.core.feature.{ValueTypeComputer, _}
import at.magiun.core.repository.{BlockRepository, DataSetRepository, DatabaseInitializer}
import at.magiun.core.rest.{BlockController, DataSetController, ExecutionController, RestApi}
import at.magiun.core.service.{ExecutionContext, _}
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
  lazy val restApi = wire[RestApi]

  // Services
  lazy val jobService = wire[JobService]
  lazy val blockService = wire[BlockService]
  lazy val dataSetService = wire[DataSetService]
  lazy val executionService = wire[ExecutionService]
  lazy val executionContext = wire[ExecutionContext]

  // Recommenders
  lazy val recommender = wire[Recommender]
  lazy val operationRecommender = wire[OperationRecommender]
  lazy val valueTypeComputer = wire[ValueTypeComputer]
  lazy val columnTypeRecommender = wire[ColumnTypeRecommender]
  lazy val restrictionBuilder = wire[RestrictionBuilder]

  // Repositories
  lazy val blockRepository = wire[BlockRepository]
  lazy val dataSetRepository = wire[DataSetRepository]
  lazy val databaseInitializer = wire[DatabaseInitializer]

  // Configs
  lazy val config = ConfigFactory.load()
  lazy val spark: SparkSession = wireWith(SparkConfig.create _)
  lazy val h2 = wireWith(H2Config.create _)
  lazy val model = OntologyConfig.create()
}
