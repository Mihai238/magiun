package at.magiun.core.model

import enumeratum._

import scala.collection.immutable

case class Schema(
                   columns: List[Column],
                   totalCount: Long
                 ) {

}

case class Column(
                   index: Int,
                   name: String,
                   `type`: ColumnType
                 ) {

}

sealed abstract class ColumnType extends EnumEntry
object ColumnType extends Enum[ColumnType] with CirceEnum[ColumnType] {

  case object String extends ColumnType
  case object Boolean extends ColumnType
  case object Date extends ColumnType
  case object Int extends ColumnType
  case object Double extends ColumnType
  case object Unknown extends ColumnType

  val values: immutable.IndexedSeq[ColumnType] = findValues

}