package at.magiun.core

import at.magiun.core.config.{H2Config, SparkConfig}
import at.magiun.core.repository.StageRepository
import at.magiun.core.rest.{StageController, RestApi, UserController}
import at.magiun.core.service.{JobService, StageService}
import com.softwaremill.macwire._
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession

//noinspection TypeAnnotation
trait MainModule {

  lazy val magiunContext = wire[MagiunContext]

  // Rest
  lazy val userController = wire[UserController]
  lazy val otherController = wire[StageController]
  lazy val restApi = wire[RestApi]

  // Services
  lazy val jobService = wire[JobService]
  lazy val stageService = wire[StageService]

  // Repositories
  lazy val stageRepository = wire[StageRepository]

  // Configs
  lazy val config = ConfigFactory.load()
  lazy val spark: SparkSession = wireWith(SparkConfig.create _)
  lazy val h2 = wireWith(H2Config.create _)
}
