package at.magiun.core.connector

import at.magiun.core.model._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types.{DataType, StructType}

trait Connector extends LazyLogging {

  def getSchema(source: DataSetSource): Schema

  def getDataFrame(source: DataSetSource): DataFrame

  final def getRows(source: DataSetSource): Seq[DataRow] = {
    val df = getDataFrame(source)

    mapToRowValues(df.collect(), df.schema)
  }

  final def getRows(source: DataSetSource, range: Range): Seq[DataRow] = {
    val df = getDataFrame(source)
    val dfRows = df.take(range.end)
      .drop(range.start)

    mapToRowValues(dfRows, df.schema)
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

  protected def mapToRowValues(dfRows: Array[Row], schema: StructType): Array[DataRow] = {
    dfRows
      .zipWithIndex
      .map { case (sparkRow, rowInd) =>
        val values: Seq[Any] = schema.zipWithIndex
          .map { case (col, colInd) =>
            mapToColumnType(col.dataType) match {
              case ColumnType.Int => sparkRow.getAs[Int](colInd)
              case ColumnType.String => sparkRow.getAs[String](colInd)
              case ColumnType.Double => sparkRow.getAs[Double](colInd)
              case ColumnType.Boolean => sparkRow.getAs[Boolean](colInd)
              case ColumnType.Date => sparkRow.get(colInd)
              case ColumnType.Unknown => sparkRow.getString(colInd)
            }
          }

        DataRow(rowInd, values)
      }
  }

}
