package at.magiun.core.connector

import at.magiun.core.model._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types.{DataType, StructType}

import scala.Option.empty

trait Connector extends LazyLogging {

  def getSchema(source: DataSetSource): Schema

  def getDataFrame(source: DataSetSource): DataFrame

  final def getRows(source: DataSetSource, range: Option[Range] = Option.empty, columns: Option[Set[String]] = empty): Seq[DataRow] = {
    val df = getDataFrame(source)

    val dfRows = range.map(range => {
      df.take(range.end).drop(range.start)
    }).getOrElse(df.collect())

    mapToRowValues(dfRows, df.schema, columns)
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

  protected def mapToRowValues(dfRows: Array[Row], schema: StructType, columns: Option[Set[String]] = empty): Array[DataRow] = {
    dfRows
      .zipWithIndex
      .map { case (sparkRow, rowInd) =>

        columns.map(columns => {
          schema.zipWithIndex
            .map { case (col, colInd) =>
              sparkRow.get(colInd).toString
            }
        })

        val values = schema.zipWithIndex.flatMap {
          case (col, colInd) =>
            val shouldFetchColumn = columns.forall(_.contains(col.name))

            if (shouldFetchColumn) {
              Option(sparkRow.get(colInd).toString)
            } else {
              None
            }
        }

        DataRow(rowInd, values)
      }
  }

}
