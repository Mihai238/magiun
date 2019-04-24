package at.magiun.core.connector

import at.magiun.core.model._
import at.magiun.core.util.DatasetUtil
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.{DataFrame, Dataset, Row}

import scala.Option.empty

trait Connector extends LazyLogging {

  def getSchema(source: DataSetSource): Schema

  def getDataset(source: DataSetSource): Dataset[Row]

  final def getRows(source: DataSetSource, range: Option[Range] = Option.empty, columns: Option[Seq[String]] = empty): Seq[DataRow] = {
    val ds = getDataset(source)

    val dsRows = range.map(range => {
      ds.take(range.end).drop(range.start)
    }).getOrElse(ds.collect())

    DatasetUtil.mapToRowValues(dsRows, ds.schema, columns)
  }

  final def getRandomSample(source: DataSetSource, size: Option[Int] = Option(1000), columns: Option[Seq[String]] = empty): Seq[DataRow] = {
    val dataset = getDataset(source)
    val dataCount = dataset.count().intValue()

    if (size.get > dataCount) {
      return DatasetUtil.mapToRowValues(dataset.collect(), dataset.schema, columns)
    }

    val rows: Array[Row] = getRandomSample(dataset, size.get, dataCount)

    DatasetUtil.mapToRowValues(rows, dataset.schema, columns)
  }

  final def getRandomSampleDF(source: DataSetSource, size: Option[Int] = Option(1000)): DataFrame = {
    val dataset = getDataset(source)
    val dataCount = dataset.count().intValue()

    if (size.get > dataCount) {
      return dataset
    }

    val spark = dataset.sparkSession
    val randomSample = spark.sparkContext.parallelize(getRandomSample(dataset, size.get, dataCount))

    spark.createDataFrame(randomSample, dataset.schema)
  }

  private def getRandomSample(dataset: DataFrame, size: Int, dataCount: Int): Array[Row] = {
    val indices = DatasetUtil.getRandomIndices(size, dataCount)
    DatasetUtil.getRowsByIndices(dataset, indices)
  }

  protected def mapToColumnType(dataType: DataType): ColumnType = {
    dataType.typeName match {
      case "integer" => ColumnType.Int
      case "string" => ColumnType.String
      case "double" => ColumnType.Double
      case "boolean" => ColumnType.Boolean
      case "date" => ColumnType.Date
      case _ =>
        logger.warn(s"Unknown column type $dataType")
        ColumnType.Unknown
    }
  }
}
