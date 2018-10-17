package at.magiun.core.config

import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession

object SparkConfig {

  def create(config: Config): SparkSession = {
    val spark = SparkSession
      .builder()
      .appName(config.getString("spark.app_name"))
      .master(config.getString("spark.master"))
      .config("spark.serializer", config.getString("spark.serializer"))
      .config("spark.executor.memory", "2g")
      .getOrCreate()

    spark
  }

}
