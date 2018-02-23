package at.magiun.core.connector

import at.magiun.core.model.{ColumnType, MagiunDataSet, Schema}
import org.apache.spark.sql.types.DataType

trait Connector {

  def getSchema(magiunDataSet: MagiunDataSet): Schema

  def mapToColumnType(dataType: DataType): ColumnType = {
    dataType.typeName match {
      case "integer" => ColumnType.Int
      case "string" => ColumnType.String
      case "double" => ColumnType.Double
      case "boolean" => ColumnType.Boolean
      case "date" => ColumnType.Date
      case _ => throw new RuntimeException(s"Unknown column type $dataType")
    }
  }

}
