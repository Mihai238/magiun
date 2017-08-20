package at.magiun.core

import at.magiun.core.config.SparkConfig
import at.magiun.core.rest.{OtherController, RestApi, UserController}
import com.softwaremill.macwire._
import org.apache.spark.sql.SparkSession

//noinspection TypeAnnotation
trait MainModule {

  lazy val magiunContext = wire[MagiunContext]

  lazy val userController = wire[UserController]
  lazy val otherController = wire[OtherController]
  lazy val restApi = wire[RestApi]

  lazy val spark: SparkSession = wire[SparkConfig].spark

  println(spark.conf.toString)
}
