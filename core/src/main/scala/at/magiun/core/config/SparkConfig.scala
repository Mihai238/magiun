package at.magiun.core.config

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object SparkConfig extends LazyLogging {

  def create(config: Config): SparkSession = {
    logger.info("Initialising Spark")

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
