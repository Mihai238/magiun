package at.magiun.core.model

import org.apache.spark.sql.{Dataset, Row, SparkSession}


abstract class Stage {
  def perform: StageOutput
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
}
