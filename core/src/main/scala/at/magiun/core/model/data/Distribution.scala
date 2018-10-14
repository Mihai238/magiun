package at.magiun.core.model.data

import enumeratum._

import scala.collection.immutable

sealed abstract class Distribution(val mean: Double, val variance: Double) extends EnumEntry

object Distribution extends Enum[Distribution] with CirceEnum[Distribution] {

  case object Normal extends Distribution(0, 1)

  val values: immutable.IndexedSeq[Distribution] = findValues
}

