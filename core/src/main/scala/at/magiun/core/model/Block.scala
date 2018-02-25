package at.magiun.core.model

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


abstract class Block {
  def perform: BlockOutput
}

class ReaderBlock(spark: SparkSession, fileName: String) extends Block {
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

abstract class BlockDecorator(block: Block) extends Block {
}

class DropColumnBlock(block: Block, columnName: String) extends BlockDecorator(block) {
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

class MapBlock(block: Block, newColName: String, func: Any => Any, colNames: Seq[String]) extends BlockDecorator(block) {
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