package at.magiun.core.connector

import at.magiun.core.model.{ColumnType, DataSetSource, MagiunDataSet, Schema}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.types.DataType

trait Connector extends LazyLogging {

  def getSchema(source: DataSetSource): Schema

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
