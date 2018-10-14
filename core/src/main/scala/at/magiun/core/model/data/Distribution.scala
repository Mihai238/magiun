package at.magiun.core.model.data


sealed abstract class Distribution(val name: String, val mean: Double, val variance: Double)

case object Normal extends Distribution("Normal", 0, 1)


