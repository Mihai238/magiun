package at.magiun.core.statistics.distribution

import at.magiun.core.model.data.Distribution
import at.magiun.core.model.statistics.distribution.DistributionFitterArgument
import at.magiun.core.statistics.optimization.GeneralPurposeOptimization
import enumeratum._
import org.apache.spark.rdd.RDD

import scala.collection.immutable

/**
  * @see https://www.rdocumentation.org/packages/fitdistrplus/versions/1.0-14/topics/mledist
  * @see https://github.com/cran/fitdistrplus/blob/master/R/mledist.R
  * using optim.method = "default"
  */
object MLEDistributionFitter {

  def fit(
           data: RDD[Double],
           distribution: Distribution,
           startArgument: DistributionFitterArgument): Any = {

    val optimizationMethod = defineOptimizationMethod(startArgument)
    val optimResult = GeneralPurposeOptimization.optimize(
      data,
      startArgument,
      () => Double, // define function
      () => Double // define function
    )

  }

  /**
    * @see https://github.com/cran/fitdistrplus/blob/79aa9dd7fd75ab0374e801411a2222a119904a15/R/mledist.R#L201
    */
  private def defineOptimizationMethod(startArgument: DistributionFitterArgument): OptimizationMethod = {
    import OptimizationMethod._

    if (startArgument.numArgs > 1) NelderMead else BFGS
  }
}

private sealed abstract class OptimizationMethod extends EnumEntry

private object OptimizationMethod extends Enum[OptimizationMethod] with CirceEnum[OptimizationMethod] {
  case object NelderMead extends OptimizationMethod()
  case object BFGS extends OptimizationMethod()

  val values: immutable.IndexedSeq[OptimizationMethod] = findValues
}
