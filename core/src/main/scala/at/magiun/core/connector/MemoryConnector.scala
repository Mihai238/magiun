package at.magiun.core.connector
import at.magiun.core.model._
import org.apache.spark.sql.{Dataset, Row}

class MemoryConnector(executions: Map[String, StageOutput]) extends Connector {

  override def getSchema(source: DataSetSource): Schema = {
    val dataset = getDataset(source)
    val cols = dataset.schema.zipWithIndex.map { case (col, index) =>
      Column(index, col.name, mapToColumnType(col.dataType))
    }

    Schema(cols.toList, dataset.count())
  }

  override def getDataset(source: DataSetSource): Dataset[Row] = {
    executions(source.url) match {
      case DatasetOutput(ds) => ds
      case _ => throw new IllegalStateException("This execution does not have a dataset result")
    }
  }
}
