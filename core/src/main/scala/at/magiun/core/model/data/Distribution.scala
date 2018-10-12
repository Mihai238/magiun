package at.magiun.core.model.data

import enumeratum._

import scala.collection.immutable

sealed abstract class Distribution(name: String) extends EnumEntry

object Distribution extends Enum[Distribution] with CirceEnum[Distribution] {

  case object Normal extends Distribution("Normal")

  val values: immutable.IndexedSeq[Distribution] = findValues
}

