package at.magiun.core.model.data

import enumeratum._

import scala.collection.immutable

sealed abstract class VariableType(name: String) extends EnumEntry

object VariableType extends Enum[VariableType] with CirceEnum[VariableType] {

  case object Binary extends VariableType("Binary")
  case object Continuous extends VariableType("Continuous")

  val values: immutable.IndexedSeq[VariableType] = findValues
}
