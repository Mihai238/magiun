package at.magiun.core.statistics.distribution

import at.magiun.core.model.data.Distribution
import at.magiun.core.model.statistics.distribution.DistributionFitterArgument
import org.apache.spark.rdd.RDD

object DistributionFitter {

  /**
    * @see https://www.rdocumentation.org/packages/fitdistrplus/versions/1.0-14/topics/fitdist
    * @see https://github.com/cran/fitdistrplus/blob/master/R/fitdist.R
    */
  def fitDistribution(
                       data: RDD[Double],
                       distribution: Distribution,
                       discrete: Boolean = false,
                       startArgument: DistributionFitterArgument = null,
                       fixedArgument: DistributionFitterArgument = null
                     ): Unit = {
    require(distribution != null, "The distribution can not be null!")

  }

}
