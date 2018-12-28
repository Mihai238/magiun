package at.magiun.core.connector

import at.magiun.core.model._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.types.{DataType, StructType}
import org.apache.spark.sql.{Dataset, Row}

import scala.Option.empty

trait Connector extends LazyLogging {

  def getSchema(source: DataSetSource): Schema

  def getDataset(source: DataSetSource): Dataset[Row]

  final def getRows(source: DataSetSource, range: Option[Range] = Option.empty, columns: Option[Seq[String]] = empty): Seq[DataRow] = {
    val ds = getDataset(source)

    val dsRows = range.map(range => {
      ds.take(range.end).drop(range.start)
    }).getOrElse(ds.collect())

    mapToRowValues(dsRows, ds.schema, columns)
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

  protected def mapToRowValues(dfRows: Array[Row], schema: StructType, columns: Option[Seq[String]] = empty): Array[DataRow] = {
    dfRows
      .zipWithIndex
      .map { case (sparkRow, rowInd) =>

        val values = if (columns.isDefined) {
          columns.map(_.flatMap {
            col =>
              val colIndex = schema.zipWithIndex.find(e => e._1.name == col).get._2
              Option(sparkRow.get(colIndex)).map(_.toString)
          }).get
        } else {
          schema.zipWithIndex.flatMap {
            case (col, colInd) =>
              Option(sparkRow.get(colInd)).orElse(Option("")).map(_.toString)
          }
        }

        DataRow(rowInd, values)
      }
  }

}
