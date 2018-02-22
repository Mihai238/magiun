package at.magiun.core.model

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


abstract class Stage {
  def perform: StageOutput

  val requiredInputStages: Int
}

class ReaderStage(spark: SparkSession, fileName: String) extends Stage {
  override def perform: StageOutput = {
    val options = Map(
      "sep" -> ",",
      "header" -> "true"
    )
    val frame = spark.read.options(options).csv(fileName)
    DatasetOutput(frame)
  }

  override val requiredInputStages = 0
}

// ***
// Decorators
// ***

abstract class StageDecorator(stage: Stage) extends Stage {
}

class DropColumnStage(stage: Stage, columnName: String) extends StageDecorator(stage) {
  override def perform: StageOutput = {
    val output = stage.perform

    output match {
      case DatasetOutput(dataSet) =>
        val frame = dataSet.drop(columnName)
        DatasetOutput(frame)
      case _ => throw new RuntimeException
    }
  }

  override val requiredInputStages = 1
}

//"age * alta_coloana + 10"

class MapStage(stage: Stage, newColName: String, func: Any => Any, colNames: Seq[String]) extends StageDecorator(stage) {
  override def perform: DatasetOutput = {
    val output = stage.perform

    output match {
      case DatasetOutput(dataSet) =>
        val colMapper = udf(func)
        val columns = colNames.map(e => col(e))
        val newDataSet = dataSet.withColumn(newColName, colMapper(columns: _*))

        DatasetOutput(newDataSet)
    }
  }

  override val requiredInputStages = 1
}