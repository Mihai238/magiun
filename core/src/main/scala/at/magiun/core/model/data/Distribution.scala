package at.magiun.core.model.data

import enumeratum._

import scala.collection.immutable

sealed abstract class Distribution(val name: String) extends EnumEntry

object Distribution extends Enum[Distribution] with CirceEnum[Distribution] {

  case object Normal extends Distribution("Normal Distribution")
  case object Uniform extends Distribution("Uniform Distribution")
  case object Exponential extends Distribution("Exponential Distribution")
  case object Log extends Distribution("Log Distribution")
  case object Binomial extends Distribution("Binomial Distribution")


  val values: immutable.IndexedSeq[Distribution] = findValues
}

