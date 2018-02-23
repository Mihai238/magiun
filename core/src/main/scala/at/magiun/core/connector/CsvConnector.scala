package at.magiun.core.connector

import at.magiun.core.model.{Column, MagiunDataSet, Schema}
import org.apache.spark.sql.SparkSession

class CsvConnector(spark: SparkSession) extends Connector {

  override def getSchema(magiunDataSet: MagiunDataSet): Schema = {
    val options = Map(
      "sep" -> ",",
      "header" -> "true"
    )

    val frame = spark.read
      .options(options)
      .option("inferSchema", "true")
      .csv(magiunDataSet.url)

    val cols = frame.schema.zipWithIndex.map { case (col, index) =>
        Column(index, col.name, mapToColumnType(col.dataType))
    }

    Schema(cols.toList)
  }

}
