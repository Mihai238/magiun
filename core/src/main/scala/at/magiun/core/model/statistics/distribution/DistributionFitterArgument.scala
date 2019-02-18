package at.magiun.core.model.statistics.distribution

trait DistributionFitterArgument

case class NormalDistributionFitterArgument(mean: Double, sd: Double) extends DistributionFitterArgument {}
case class PoissonDistributionFitterArgument(lambda: Double) extends DistributionFitterArgument {}
case class GammaDistributionFitterArgument(shape: Double, rate: Double) extends DistributionFitterArgument {}
case class ExponentialDistributionFitterArgument(rate: Double) extends DistributionFitterArgument {}
case class BinomialDistributionFitterArgument(size: Double, mu: Double) extends DistributionFitterArgument {}