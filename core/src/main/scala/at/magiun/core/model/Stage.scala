package at.magiun.core.model

import org.apache.spark.sql.{Dataset, Row, SparkSession}


abstract class Stage {
  def perform: Output
}

class ReaderStage(spark: SparkSession, fileName: String) extends Stage {
  override def perform: Output = {
    val options = Map(
      "sep" -> ",",
      "header" -> "true"
    )
    val frame = spark.read.options(options).csv(fileName)
    DatasetOutput(frame)
  }
}

abstract class Decorator(stage: Stage) extends Stage {
}

class DropColumnDecorator(stage: Stage, columnName: String) extends Decorator(stage) {
  override def perform: Output = {
    val output = stage.perform

    output match {
      case DatasetOutput(dataSet) =>
        val frame = dataSet.drop(columnName)
        DatasetOutput(frame)
      case _ => throw new RuntimeException
    }
  }
}


abstract class Output
case class DatasetOutput(dataSet: Dataset[Row]) extends Output
