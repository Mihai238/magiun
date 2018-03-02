package at.magiun.core.model

import at.magiun.core.model.Stage.getOutputOfPrevStage
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.expr


abstract class Stage {
  def perform: StageOutput
}

object Stage {
  def getOutputOfPrevStage(stageInput: StageInput): DatasetOutput = {
    stageInput.stage.perform match {
      case x@DatasetOutput(dataSet) => x
      case MultiOutput(outputs) =>
        outputs(stageInput.index) match {
          case x@DatasetOutput(dataSet) => x
          case _ => throw new RuntimeException
        }
    }
  }
}

case class StageInput(stage: Stage, index: Int = 0)

// ***
// Readers and writers
// ***

class FileReaderStage(spark: SparkSession, fileName: String) extends Stage {
  override def perform: StageOutput = {
    val options = Map(
      "sep" -> ",",
      "header" -> "true"
    )
    val frame = spark.read.options(options).csv(fileName)
    DatasetOutput(frame)
  }
}

class FileWriterStage(input: StageInput, fileName: String) extends Stage {
  override def perform: StageOutput = {
    val ds = getOutputOfPrevStage(input).dataSet
    ds.write
      .option("header", "true")
      .csv(fileName)

    EmptyOutput
  }
}

// ***
// Decorators
// ***

class DropColumnStage(input: StageInput, columnName: String) extends Stage {
  override def perform: StageOutput = {
    val dataSet = getOutputOfPrevStage(input).dataSet
    DatasetOutput(dataSet.drop(columnName))
  }
}

class AddColumnStage(input: StageInput, newColName: String, e: String) extends Stage {
  override def perform: DatasetOutput = {
    val dataSet = getOutputOfPrevStage(input).dataSet
    val result = dataSet.withColumn(newColName, expr(e))
    DatasetOutput(result)
  }
}
