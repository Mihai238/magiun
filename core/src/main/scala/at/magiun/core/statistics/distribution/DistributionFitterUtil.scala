package at.magiun.core.statistics.distribution

import at.magiun.core.model.data.Distribution
import at.magiun.core.model.statistics.distribution._
import org.apache.spark.rdd.RDD

object DistributionFitterUtil {

  /**
    * @see https://github.com/cran/fitdistrplus/blob/master/R/util-manageparam.R
    */
  def manageParameters(startArgument: DistributionFitterArgument,
                       fixedArgument: DistributionFitterArgument,
                       data: RDD[Double],
                       distribution: Distribution): (DistributionFitterArgument, DistributionFitterArgument) = {
    val computedStartArgument = manageStartArgument(startArgument, data, distribution)
    val computedFixedArgument = manageFixedArgument(fixedArgument, distribution)

    (computedStartArgument, computedFixedArgument)
  }

  private def manageStartArgument(startArgument: DistributionFitterArgument, data: RDD[Double], distribution: Distribution): DistributionFitterArgument = {
    if (startArgument == null) {
      computeDefaultArguments(data, distribution)
    } else {
      startArgument
    }
  }

  private def manageFixedArgument(fixedArgument: DistributionFitterArgument, distribution: Distribution): DistributionFitterArgument = {
    if (fixedArgument == null) {
      return null
    } else {
      if (distribution.equals(Distribution.Normal) && !fixedArgument.isInstanceOf[NormalDistributionFitterArgument]) {
        throw new IllegalArgumentException(s"The fixed argument for ${distribution.name} must be of type ${NormalDistributionFitterArgument.getClass.getSimpleName}")
      } else if (distribution.equals(Distribution.Poisson) && !fixedArgument.isInstanceOf[PoissonDistributionFitterArgument]) {
        throw new IllegalArgumentException(s"The fixed argument for ${distribution.name} must be of type ${PoissonDistributionFitterArgument.getClass.getSimpleName}")
      } else if (distribution.equals(Distribution.Gamma) && !fixedArgument.isInstanceOf[GammaDistributionFitterArgument]) {
        throw new IllegalArgumentException(s"The fixed argument for ${distribution.name} must be of type ${GammaDistributionFitterArgument.getClass.getSimpleName}")
      } else if (distribution.equals(Distribution.Exponential) && !fixedArgument.isInstanceOf[ExponentialDistributionFitterArgument]) {
        throw new IllegalArgumentException(s"The fixed argument for ${distribution.name} must be of type ${ExponentialDistributionFitterArgument.getClass.getSimpleName}")
      } else if (distribution.equals(Distribution.Binomial) && !fixedArgument.isInstanceOf[BinomialDistributionFitterArgument]) {
        throw new IllegalArgumentException(s"The fixed argument for ${distribution.name} must be of type ${BinomialDistributionFitterArgument.getClass.getSimpleName}")
      } else if (distribution.equals(Distribution.Bernoulli) && !fixedArgument.isInstanceOf[BernoulliDistributionFitterArgument]) {
        throw new IllegalArgumentException(s"The fixed argument for ${distribution.name} must be of type ${BernoulliDistributionFitterArgument.getClass.getSimpleName}")
      }
    }

    fixedArgument
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
      require(data.min() > 0, s"The values must be positive in order to fit a ${distribution.name}")
      val logData = data.map(d => Math.log(d))

      val sd0 = Math.sqrt((n - 1.0) / n) * logData.stdev()
      NormalDistributionFitterArgument(logData.mean(), sd0)
    } else if (distribution.equals(Distribution.Poisson)) {
      PoissonDistributionFitterArgument(mean)
    } else if (distribution.equals(Distribution.Gamma)) {
      require(data.min() > 0, s"The values must be positive in order to fit a ${distribution.name}")

      val v: Double = (n - 1.0)/n * data.variance()
      GammaDistributionFitterArgument(Math.pow(mean, 2.0)/v, mean/v)
    } else if (distribution.equals(Distribution.Exponential)) {
      require(data.min() > 0, s"The values must be positive in order to fit an ${distribution.name}")

      ExponentialDistributionFitterArgument(1/mean)
    } else if (distribution.equals(Distribution.Binomial)) {
      require(data.min() >= 0, s"The values must be positive in order to fit an ${distribution.name}")
      val v: Double = (n - 1.0)/n * data.variance()

      val size = if (v > mean) Math.pow(mean, 2.0)/(v - mean) else 100
      BinomialDistributionFitterArgument(size, mean)
    } else if (distribution.equals(Distribution.Bernoulli)) {
      require(data.min() >= 0, s"The values must be positive in order to fit a ${distribution.name}")

      BernoulliDistributionFitterArgument(mean)
    } else {
      throw new IllegalArgumentException(s"Unsupported or unknown distribution ${distribution.name}")
    }
  }
}
