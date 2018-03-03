package at.magiun.core.connector

import at.magiun.core.model._
import org.apache.spark.sql.{DataFrame, SparkSession}

class CsvConnector(spark: SparkSession) extends Connector {

  private val readOptions = Map(
    "sep" -> ",",
    "header" -> "true",
    "inferSchema" -> "true"
  )

  override def getSchema(source: DataSetSource): Schema = {
    val dataFrame = spark.read
      .options(readOptions)
      .csv(source.url)

    val cols = dataFrame.schema.zipWithIndex.map { case (col, index) =>
      Column(index, col.name, mapToColumnType(col.dataType))
    }

    Schema(cols.toList)
  }

  override def getDataFrame(source: DataSetSource): DataFrame = {
    spark.read
      .options(readOptions)
      .csv(source.url)
  }
}
