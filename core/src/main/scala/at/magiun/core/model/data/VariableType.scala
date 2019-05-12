package at.magiun.core.model.data

import enumeratum._

import scala.collection.immutable

sealed abstract class VariableType(val name: String) extends EnumEntry

object VariableType extends Enum[VariableType] with CirceEnum[VariableType] {

  case object Categorical extends VariableType("Categorical")
  case object Binary extends VariableType("Binary")
  case object Discrete extends VariableType("Discrete")
  case object Continuous extends VariableType("Continuous")
  case object NonNegative extends VariableType("NonNegative")
  case object Text extends VariableType("Text")

  val values: immutable.IndexedSeq[VariableType] = findValues
}
