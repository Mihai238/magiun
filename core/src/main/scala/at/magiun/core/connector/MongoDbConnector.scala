package at.magiun.core.connector

import java.util.regex.Pattern

import at.magiun.core.model.{Column, DataRow, DataSetSource, Schema}
import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql._
import com.mongodb.{BasicDBObject, MongoClient, MongoClientOptions}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.control.NonFatal

class MongoDbConnector(spark: SparkSession) extends Connector with LazyLogging {

  private val CONNECTION_TIMEOUT = 2000
  private val hostPattern = Pattern.compile("mongodb://([^/]*)/.*")

  override def getSchema(source: DataSetSource): Schema = {
    val url = source.url
    val (mongoUri, collectionName) = getUriAndCollection(source.url)
    logger.info(s"Connecting to mongo uri '$mongoUri'; collection '$collectionName'")

    if (isMongoReachable(url)) {
      val readConfig = ReadConfig(Map("uri" -> mongoUri, "collection" -> collectionName))
      val dataFrame = spark.read.mongo(readConfig)

      val cols = dataFrame.schema.zipWithIndex.map { case (col, index) =>
        Column(index, col.name, mapToColumnType(col.dataType))
      }

      Schema(cols.toList)
    } else {
      Schema(List.empty)
    }
  }

  override def getDataFrame(source: DataSetSource): DataFrame = {
    val (mongoUri, collectionName) = getUriAndCollection(source.url)

    val readConfig = ReadConfig(Map("uri" -> mongoUri, "collection" -> collectionName))
    spark.read.mongo(readConfig)
  }

  private def getUriAndCollection(url: String): (String, String) = {
    val lastSlashIndex = url.lastIndexOf("/")
    val mongoUri = url.substring(0, lastSlashIndex)
    val collectionName = url.substring(lastSlashIndex + 1)

    (mongoUri, collectionName)
  }

  private def isMongoReachable(url: String) = {
    val options = MongoClientOptions.builder()
      .connectTimeout(CONNECTION_TIMEOUT)
      .socketTimeout(CONNECTION_TIMEOUT)
      .maxWaitTime(CONNECTION_TIMEOUT)
      .serverSelectionTimeout(CONNECTION_TIMEOUT)
      .build()

    val m = hostPattern.matcher(url)
    m.find()
    val host = m.group(1)

    val client = new MongoClient(host, options)

    try {
      client.getDatabase("soomeDb").runCommand(new BasicDBObject("ping", "1"))
      true
    } catch {
      case NonFatal(e) => false
    }
  }
}
