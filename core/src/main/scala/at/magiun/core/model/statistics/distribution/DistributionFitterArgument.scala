package at.magiun.core.model.statistics.distribution

trait DistributionFitterArgument {
  val numArgs: Double
}

sealed case class NormalDistributionFitterArgument(mean: Double, sd: Double) extends DistributionFitterArgument {
  val numArgs: Double = 2
}
sealed case class PoissonDistributionFitterArgument(lambda: Double) extends DistributionFitterArgument {
  val numArgs: Double = 1
}
sealed case class GammaDistributionFitterArgument(shape: Double, rate: Double) extends DistributionFitterArgument {
  val numArgs: Double = 2
}
sealed case class ExponentialDistributionFitterArgument(rate: Double) extends DistributionFitterArgument {
  val numArgs: Double = 1
}
sealed case class BinomialDistributionFitterArgument(size: Double, mu: Double) extends DistributionFitterArgument {
  val numArgs: Double = 2
}
sealed case class BernoulliDistributionFitterArgument(mu: Double) extends DistributionFitterArgument {
  val numArgs: Double = 1
}