package at.magiun.core.statistics.distribution

import at.magiun.core.model.data.Distribution
import at.magiun.core.model.statistics.distribution._
import at.magiun.core.{MainModule, UnitTest}
import org.apache.commons.math3.distribution.{ExponentialDistribution, NormalDistribution, PoissonDistribution}
import org.apache.spark.rdd.RDD
import org.scalatest.PrivateMethodTester

class DistributionFitterUtilTest extends UnitTest with PrivateMethodTester {

  private val mainModule = new MainModule {}
  private val sparkSession = mainModule.spark



  it should s"compute the default arguments for ${Distribution.Normal}" in {
    // given
    val mean = 0.0
    val sd = 1.0
    val data: RDD[Double] = sparkSession.sparkContext.parallelize(new NormalDistribution(mean, sd).sample(100))

    // when
    val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
    val defaultArguments = (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Normal)).asInstanceOf[DistributionFitterArgument]

    // then
    assert(defaultArguments.isInstanceOf[NormalDistributionFitterArgument])

    val defArg = defaultArguments.asInstanceOf[NormalDistributionFitterArgument]
    defArg.mean shouldBe (mean +- 0.2)
    defArg.sd shouldBe 0
  }

  it should s"compute the default arguments for ${Distribution.Poisson}" in {
    // given
    val p = 3.0
    val data: RDD[Double] = sparkSession.sparkContext.parallelize(new PoissonDistribution(p).sample(100).map(i => i.toDouble))

    // when
    val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
    val defaultArguments = (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Poisson)).asInstanceOf[DistributionFitterArgument]

    // then
    assert(defaultArguments.isInstanceOf[PoissonDistributionFitterArgument])

    val defArg = defaultArguments.asInstanceOf[PoissonDistributionFitterArgument]
    defArg.lambda shouldBe (p +- 0.3)
  }

  it should s"throw exception when passing data < 0 for ${Distribution.Exponential}" in {
    assertThrows[IllegalArgumentException]{
      // given
      val data: RDD[Double] = sparkSession.sparkContext.parallelize(Seq(-2.0, 0.0, 1.0))

      // when
      val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
      (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Exponential)).asInstanceOf[DistributionFitterArgument]
    }
  }

  it should s"compute the default arguments for ${Distribution.Exponential}" in {
    // given
    val mean = 3.0
    val data: RDD[Double] = sparkSession.sparkContext.parallelize(new ExponentialDistribution(mean).sample(100))

    // when
    val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
    val defaultArguments = (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Exponential)).asInstanceOf[DistributionFitterArgument]

    // then
    assert(defaultArguments.isInstanceOf[ExponentialDistributionFitterArgument])

    val defArg = defaultArguments.asInstanceOf[ExponentialDistributionFitterArgument]
    defArg.rate shouldBe (1/mean +- 0.03)
  }

}
