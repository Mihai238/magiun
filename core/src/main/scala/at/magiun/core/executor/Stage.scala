package at.magiun.core.executor

import at.magiun.core.executor.Stage.getDataSetInput
import at.magiun.core.model.{DatasetOutput, MultiOutput, StageOutput}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import org.apache.spark.sql.functions.expr


abstract class Stage {
  def perform: StageOutput
}

object Stage {
  def getDataSetInput(stageInput: StageInput): DatasetOutput = {
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
// Initiators
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

// ***
// Decorators
// ***

class DropColumnStage(input: StageInput, columnName: String) extends Stage {
  override def perform: StageOutput = {
    val dataSet = getDataSetInput(input).dataSet
    DatasetOutput(dataSet.drop(columnName))
  }
}

//"age * alta_coloana + 10"

class MapStage(input: StageInput, newColName: String, e: String) extends Stage {
  override def perform: DatasetOutput = {
    val dataSet = getDataSetInput(input).dataSet
    val result = dataSet.withColumn(newColName, expr(e))
    DatasetOutput(result)
  }

}