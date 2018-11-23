package at.magiun.core.connector

import java.util.concurrent.ConcurrentHashMap

import at.magiun.core.connector.CsvConnector.cache
import at.magiun.core.model._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{Dataset, Row, SparkSession}

class CsvConnector(spark: SparkSession) extends Connector with LazyLogging {

  private val readOptions = Map(
    "sep" -> ",",
    "header" -> "true",
    "inferSchema" -> "true"
  )

  override def getSchema(source: DataSetSource): Schema = {
    val dataset = getDataset(source)

    val cols = dataset.schema.zipWithIndex.map { case (col, index) =>
      Column(index, col.name, mapToColumnType(col.dataType))
    }

    Schema(cols.toList, dataset.count())
  }

  override def getDataset(source: DataSetSource): Dataset[Row] = {
    if (cache.containsKey(source)) {
      logger.info("Dataset found in memory")
      cache.get(source)
    } else {
      logger.info("Loading the dataset in spark")

      val ds = spark.read
        .options(readOptions)
        .csv(source.url)
      cache.put(source, ds)
      ds
    }


  }

}

object CsvConnector {
  private val cache: ConcurrentHashMap[DataSetSource, Dataset[Row]] = new ConcurrentHashMap[DataSetSource, Dataset[Row]]()
}