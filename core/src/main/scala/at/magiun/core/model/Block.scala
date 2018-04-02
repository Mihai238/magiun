package at.magiun.core.model

import enumeratum._

import scala.collection.immutable

case class Block(id: String, `type`: BlockType, inputs: Seq[BlockInput], params: Map[String, String])

sealed abstract class BlockType extends EnumEntry
object BlockType extends Enum[BlockType] with CirceEnum[BlockType] {

  // Readers and writers
  case object FileReader extends BlockType
  case object DatabaseReader extends BlockType
  case object FileWriter extends BlockType

  // Data Transformation
  case object SplitData extends BlockType

  // Feature processors
  case object DropColumn extends BlockType
  case object AddColumn extends BlockType

  case object LinearRegression extends BlockType
  case object PoissonRegression extends BlockType

  val values: immutable.IndexedSeq[BlockType] = findValues
}

/**
  * @param blockId of the block which provides the input
  * @param index of the output of the block
  */
case class BlockInput(blockId: String, index: Long)