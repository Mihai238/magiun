package at.magiun.core.model

import org.apache.spark.sql.{Dataset, Row, SparkSession}


abstract class Stage {
  def perform: List[Output]
}

class ReaderStage(spark: SparkSession, fileName: String) extends Stage {
  override def perform: List[Output] = {
    val options = Map(
      "sep" -> ",",
      "header" -> "true"
    )
    val frame = spark.read.options(options).csv(fileName)
    List(DatasetOutput(frame))
  }
}

abstract class Decorator(stage: Stage) extends Stage {
}

class DropColumnDecorator(stage: Stage, columnName: String) extends Decorator(stage) {
  override def perform: List[Output] = {
    val outputs = stage.perform
    require(outputs.size == 1)

    val output = outputs.head
    output match {
      case DatasetOutput(dataSet) =>
        val frame = dataSet.drop(columnName)
        List(DatasetOutput(frame))
      case _ => throw new RuntimeException
    }
  }
}


abstract class Output
case class DatasetOutput(dataSet: Dataset[Row]) extends Output
