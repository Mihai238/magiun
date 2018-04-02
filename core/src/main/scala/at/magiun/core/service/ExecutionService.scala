package at.magiun.core.service

import at.magiun.core.model.BlockType.{AddColumn, DatabaseReader, DropColumn, FileReader, FileWriter, LinearRegression}
import at.magiun.core.model._
import org.apache.spark.sql.SparkSession

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ExecutionService(
                        spark: SparkSession,
                        blockService: BlockService
                      ) {

  def execute(execution: Execution): Future[Execution] = {
    blockService.find(execution.blockId)
      .map(finalBlock => {
        val blocks = loadBlocks(finalBlock)
        execute(blocks, finalBlock)

        execution
      })
  }

  // HACK: this should be done asynchronously but it turns out is not that easy
  def loadBlocks(block: Block): Map[String, Block] = {
    if (block.inputs.isEmpty) {
      Map(block.id -> block)
    } else {
      val blockIds = block.inputs.map(_.blockId)
      val blocks = blockIds.map(blockService.find).map(Await.result(_, 2.seconds))

      val blockMap = blocks.map(loadBlocks).foldLeft(Map[String, Block]()){(acc, m) => acc ++ m}

      blockMap + ((block.id, block))
    }
  }


  def execute(blocks: Map[String, Block], finalBlock: Block): StageOutput = {
    val stage = buildStage(blocks, finalBlock)

    stage.perform
  }

  private def buildStage(blocks: Map[String, Block], block: Block): Stage = {
    block.`type` match {
      case FileReader =>
        new FileReaderStage(spark, block.params("fileName"))
      case DatabaseReader => ???
      case FileWriter =>
        val nextBlock = blocks(block.inputs.head.blockId)
        val stage = buildStage(blocks, nextBlock)
        new FileWriterStage(StageInput(stage), block.params("fileName"))
      case DropColumn =>
        val nextBlock = blocks(block.inputs.head.blockId)
        val stage = buildStage(blocks, nextBlock)
        new DropColumnStage(StageInput(stage), block.params("columnName"))
      case AddColumn => ???
      case LinearRegression => ???
    }
  }
}
