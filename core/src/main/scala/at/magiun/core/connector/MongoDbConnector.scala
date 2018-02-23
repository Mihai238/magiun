package at.magiun.core.connector

import at.magiun.core.model.{Column, DataSetSource, Schema}
import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

class MongoDbConnector(spark: SparkSession) extends Connector with LazyLogging {

  override def getSchema(source: DataSetSource): Schema = {
    val lastSlashIndex = source.url.lastIndexOf("/")
    val mongoUri = source.url.substring(0, lastSlashIndex)
    val collectionName = source.url.substring(lastSlashIndex + 1)

    logger.info(s"Connecting to mongo uri '$mongoUri'; collection '$collectionName'")

    val readConfig = ReadConfig(Map("uri" -> mongoUri, "collection" -> collectionName))
    val dataFrame = spark.read.mongo(readConfig)

    val cols = dataFrame.schema.zipWithIndex.map { case (col, index) =>
      Column(index, col.name, mapToColumnType(col.dataType))
    }

    Schema(cols.toList)
  }

}
