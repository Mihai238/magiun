package at.magiun.core.service

import org.apache.spark.sql.SparkSession

class JobService(spark: SparkSession) {

  spark.read.json()

}
