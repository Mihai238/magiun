package at.magiun.core.model

import at.magiun.core.model
import enumeratum._

import scala.collection.immutable

sealed abstract class SourceType(name: String) extends EnumEntry

object SourceType extends Enum[SourceType] with CirceEnum[SourceType] {

  case object FileCsv extends SourceType("FileCsv")
  case object Mongo extends SourceType("Mongo")
  case object Memory extends SourceType("Memory")

  val values: immutable.IndexedSeq[model.SourceType] = findValues
}
