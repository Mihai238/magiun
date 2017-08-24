package at.magiun.core.config

import org.apache.spark.sql.SparkSession

object SparkConfig {

  def create(baseConfig: BaseConfig): SparkSession = {
    SparkSession
      .builder()
      .appName(baseConfig.config.getString("spark.app_name"))
      .master(baseConfig.config.getString("spark.master"))
      .config("spark.serializer", baseConfig.config.getString("spark.serializer"))
      .getOrCreate()
  }

}
