package at.magiun.core.statistics.distribution

import at.magiun.core.model.data.Distribution
import at.magiun.core.model.statistics.distribution.DistributionFitterArgument
import org.apache.spark.rdd.RDD

/**
  * @see https://www.rdocumentation.org/packages/fitdistrplus/versions/1.0-14/topics/fitdist
  * @see https://github.com/cran/fitdistrplus/blob/master/R/fitdist.R
  *      using the method 'mle - maximum likelihood estimation'
  */
object DistributionFitter {

  def fit(
           data: RDD[Double],
           distribution: Distribution,
           startArgument: DistributionFitterArgument = null,
           fixedArgument: DistributionFitterArgument = null
         ): Unit = {
    require(distribution != null, "The distribution can not be null!")

    val computedArguments = DistributionFitterUtil.manageParameters(startArgument, fixedArgument, data, distribution)
    val discrete = isDiscrete(distribution)

    val fit = MLEDistributionFitter.fit(data, distribution, startArgument)
  }

  private def isDiscrete(distribution: Distribution): Boolean = {
    import at.magiun.core.model.data.Distribution._
    distribution match {
      case Binomial | Bernoulli | Poisson => false
      case _ => true
    }
  }

}
