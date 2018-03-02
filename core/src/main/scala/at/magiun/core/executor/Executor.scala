package at.magiun.core.executor

import at.magiun.core.model.BlockType.{AddColumn, DatabaseReader, DropColumn, FileReader, LinearRegression}
import at.magiun.core.model.{Block, StageOutput}
import org.apache.spark.sql.SparkSession

class Executor(spark: SparkSession) {

  def execute(blocks: Map[String, Block], finalBlock: Block): StageOutput = {
    val stage = buildStage(blocks, finalBlock)

    stage.perform
  }

  private def buildStage(blocks: Map[String, Block], block: Block): Stage = {
    block.`type` match {
      case FileReader =>
        new FileReaderStage(spark, block.params("fileName"))
      case DatabaseReader => ???
      case DropColumn =>
        val nextBlock = blocks(block.inputs.head._1)
        val stage = buildStage(blocks, nextBlock)
        new DropColumnStage(StageInput(stage), block.params("columnName"))
      case AddColumn => ???
      case LinearRegression => ???
    }
  }
}
