package at.magiun.core.model

import enumeratum._

import scala.collection.immutable

case class Block(id: String, `type`: BlockType, inputs: Seq[(String, Long)], params: Map[String, String])

sealed abstract class BlockType extends EnumEntry
object BlockType extends Enum[BlockType] with CirceEnum[BlockType] {

  case object FileReader extends BlockType
  case object DatabaseReader extends BlockType

  case object LinearRegression extends BlockType

  val values: immutable.IndexedSeq[BlockType] = findValues
}