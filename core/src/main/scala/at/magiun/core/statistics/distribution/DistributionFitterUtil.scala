package at.magiun.core.statistics.distribution

import at.magiun.core.model.data.Distribution
import at.magiun.core.model.statistics.distribution._
import org.apache.spark.rdd.RDD

object DistributionFitterUtil {

  /**
    *
    * @see https://github.com/cran/fitdistrplus/blob/master/R/fitdist.R
    */
  def manageParameters(startArgument: DistributionFitterArgument, fixedArgument: DistributionFitterArgument, data: RDD[Double], distribution: Distribution): Unit = {
    manageStartArgument(startArgument, data, distribution)

  }

  private def manageStartArgument(startArgument: DistributionFitterArgument, data: RDD[Double], distribution: Distribution): DistributionFitterArgument = {
    if (startArgument == null) {
      computeDefaultArguments(data, distribution)
    } else {
      startArgument
    }
  }

  /**
    * @see https://github.com/cran/fitdistrplus/blob/master/R/util-startarg.R
    */
  private def computeDefaultArguments(data: RDD[Double], distribution: Distribution): DistributionFitterArgument = {
    val n = data.count()
    val mean = data.mean()

    if (distribution.equals(Distribution.Normal)) {
      val sd0 = Math.sqrt((n - 1.0) / n) * data.stdev()
      NormalDistributionFitterArgument(mean, sd0)
    } else if (distribution.equals(Distribution.LogNormal)) {
      require(data.min() > 0, "The values must be positive in order to fit a lognormal distribution")
      val logData = data.map(d => Math.log(d))

      val sd0 = Math.sqrt((n - 1.0) / n) * logData.stdev()
      NormalDistributionFitterArgument(logData.mean(), sd0)
    } else if (distribution.equals(Distribution.Poisson)) {
      PoissonDistributionFitterArgument(mean)
    } else if (distribution.equals(Distribution.Gamma)) {
      require(data.min() > 0, "The values must be positive in order to fit a gamma distribution")

      val v: Double = (n - 1.0)/n * data.variance()
      GammaDistributionFitterArgument(Math.exp(mean)/v, mean/v)
    } else if (distribution.equals(Distribution.Exponential)) {
      require(data.min() > 0, "The values must be positive in order to fit an exponential distribution")

      ExponentialDistributionFitterArgument(1/mean)
    } else if (distribution.equals(Distribution.Binomial)) {
      val v: Double = (n - 1.0)/n * data.variance()

      val size = if (v > mean) Math.exp(mean)/(v - mean) else 100
      BinomialDistributionFitterArgument(size, mean)
    } else {
      throw new IllegalArgumentException(s"Unsupported or unknown distribution ${distribution.name}")
    }
  }
}
