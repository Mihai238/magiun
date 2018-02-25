package at.magiun.core.executor

import at.magiun.core.model.{BlockOutput, DatasetOutput}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


abstract class Stage {
  def perform: BlockOutput
}

class FileReaderStage(spark: SparkSession, fileName: String) extends Stage {

  override def perform: BlockOutput = {
    val options = Map(
      "sep" -> ",",
      "header" -> "true"
    )
    val frame = spark.read.options(options).csv(fileName)
    DatasetOutput(frame)
  }

}

// ***
// Decorators
// ***

abstract class StageDecorator(block: Stage, outputIndex: Int = 0) extends Stage {
}

class DropColumnStage(block: Stage, columnName: String) extends StageDecorator(block) {
  override def perform: BlockOutput = {
    val output = block.perform

    output match {
      case DatasetOutput(dataSet) =>
        val frame = dataSet.drop(columnName)
        DatasetOutput(frame)
      case _ => throw new RuntimeException
    }
  }

}

//"age * alta_coloana + 10"

class MapStage(block: Stage, newColName: String, func: Any => Any, colNames: Seq[String]) extends StageDecorator(block) {
  override def perform: DatasetOutput = {
    val output = block.perform

    output match {
      case DatasetOutput(dataSet) =>
        val colMapper = udf(func)
        val columns = colNames.map(e => col(e))
        val newDataSet = dataSet.withColumn(newColName, colMapper(columns: _*))

        DatasetOutput(newDataSet)
    }
  }

}