package at.magiun.core.config

import com.softwaremill.macwire._
import org.apache.spark.sql.SparkSession

class SparkConfig {

  lazy val baseConfig = wire[BaseConfig]

  def spark = SparkSession
    .builder()
    .appName(baseConfig.config.getString("spark.app_name"))
    .master(baseConfig.config.getString("spark.master"))
    .config("spark.serializer", baseConfig.config.getString("spark.serializer"))
}
