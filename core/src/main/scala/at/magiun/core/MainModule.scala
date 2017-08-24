package at.magiun.core

import at.magiun.core.config.{BaseConfig, SparkConfig}
import at.magiun.core.rest.{OtherController, RestApi, UserController}
import com.softwaremill.macwire._
import org.apache.spark.sql.SparkSession

//noinspection TypeAnnotation
trait MainModule {

  lazy val magiunContext = wire[MagiunContext]

  lazy val userController = wire[UserController]
  lazy val otherController = wire[OtherController]
  lazy val restApi = wire[RestApi]

  // Configs
  lazy val baseConfig = wire[BaseConfig]
  lazy val spark: SparkSession = wireWith(SparkConfig.create _)
}
