package at.magiun.core.statistics.distribution

import at.magiun.core.model.data.Distribution
import at.magiun.core.model.statistics.distribution.DistributionFitterArgument
import org.apache.spark.rdd.RDD

object DistributionUtil {

  /**
    *
    * @see https://github.com/cran/fitdistrplus/blob/master/R/fitdist.R
    * @see https://github.com/cran/fitdistrplus/blob/master/R/util-startarg.R
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

  private def computeDefaultArguments(data: RDD[Double], distribution: Distribution): DistributionFitterArgument = {
    null
  }
}
