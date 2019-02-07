package at.magiun.core.model.data

import enumeratum._

import scala.collection.immutable

sealed abstract class Distribution(val name: String, val ontClassName: String) extends EnumEntry

object Distribution extends Enum[Distribution] with CirceEnum[Distribution] {

  case object Normal extends Distribution("Normal Distribution", "NormalDistribution")
  case object Uniform extends Distribution("Uniform Distribution", "UniformDistribution")
  case object Exponential extends Distribution("Exponential Distribution", "ExponentialDistribution")
  case object Log extends Distribution("Log Distribution", "LogDistribution")
  case object Binomial extends Distribution("Binomial Distribution", "BinomialDistribution")
  case object Bernoulli extends Distribution("Bernoulli Distribution", "BernoulliDistribution")
  case object Multinomial extends Distribution("Multinomial Distribution", "MultinomialDistribution")


  val values: immutable.IndexedSeq[Distribution] = findValues
}

