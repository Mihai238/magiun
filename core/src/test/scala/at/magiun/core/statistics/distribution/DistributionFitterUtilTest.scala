package at.magiun.core.statistics.distribution

import at.magiun.core.model.data.Distribution
import at.magiun.core.model.statistics.distribution._
import at.magiun.core.{MainModule, UnitTest}
import org.apache.commons.math3.distribution.{BinomialDistribution, ExponentialDistribution, NormalDistribution, PoissonDistribution}
import org.apache.spark.rdd.RDD
import org.scalatest.PrivateMethodTester

class DistributionFitterUtilTest extends UnitTest with PrivateMethodTester {

  private val mainModule = new MainModule {}
  private val sparkSession = mainModule.spark
  private val sampleSize = 100


  it should s"compute the default arguments for ${Distribution.Normal}" in {
    // given
    val mean = 0.0
    val sd = 1.0
    val data: RDD[Double] = sparkSession.sparkContext.parallelize(new NormalDistribution(mean, sd).sample(sampleSize))

    // when
    val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
    val defaultArguments = (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Normal)).asInstanceOf[DistributionFitterArgument]

    // then
    assert(defaultArguments.isInstanceOf[NormalDistributionFitterArgument])

    val defArg = defaultArguments.asInstanceOf[NormalDistributionFitterArgument]
    defArg.mean shouldBe (mean +- 0.5)
    defArg.sd shouldBe (sd +- 0.2)
  }

  it should s"compute the default arguments for ${Distribution.Poisson}" in {
    // given
    val p = 3.0
    val data: RDD[Double] = sparkSession.sparkContext.parallelize(new PoissonDistribution(p).sample(sampleSize).map(i => i.toDouble))

    // when
    val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
    val defaultArguments = (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Poisson)).asInstanceOf[DistributionFitterArgument]

    // then
    assert(defaultArguments.isInstanceOf[PoissonDistributionFitterArgument])

    val defArg = defaultArguments.asInstanceOf[PoissonDistributionFitterArgument]
    defArg.lambda shouldBe (p +- 0.5)
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
    val data: RDD[Double] = sparkSession.sparkContext.parallelize(new ExponentialDistribution(mean).sample(sampleSize))

    // when
    val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
    val defaultArguments = (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Exponential)).asInstanceOf[DistributionFitterArgument]

    // then
    assert(defaultArguments.isInstanceOf[ExponentialDistributionFitterArgument])

    val defArg = defaultArguments.asInstanceOf[ExponentialDistributionFitterArgument]
    defArg.rate shouldBe (1/mean +- 0.5)
  }

  it should s"compute the default arguments for ${Distribution.Binomial}" in {
    // given
    val trials = 5
    val p = .25
    val data: RDD[Double] = sparkSession.sparkContext.parallelize(new BinomialDistribution(trials, p).sample(sampleSize).map(i => i.toDouble))

    // when
    val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
    val defaultArguments = (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Binomial)).asInstanceOf[DistributionFitterArgument]

    // then
    assert(defaultArguments.isInstanceOf[BinomialDistributionFitterArgument])

    val defArg = defaultArguments.asInstanceOf[BinomialDistributionFitterArgument]
    defArg.size shouldBe 100
    defArg.mu shouldBe (1.0 +- .6)
  }

  it should s"compute the default arguments for ${Distribution.Bernoulli}" in {
    // given
    val trials = 1
    val p = .6
    val data: RDD[Double] = sparkSession.sparkContext.parallelize(new BinomialDistribution(trials, p).sample(sampleSize).map(i => i.toDouble))

    // when
    val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
    val defaultArguments = (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Bernoulli)).asInstanceOf[DistributionFitterArgument]

    // then
    assert(defaultArguments.isInstanceOf[BernoulliDistributionFitterArgument])

    val defArg = defaultArguments.asInstanceOf[BernoulliDistributionFitterArgument]
    defArg.mu shouldBe (p +- .2)
  }

  it should "throw an exception when trying to compute the default arguments for an unsupported distribution" in {
    // given
    val trials = 5
    val p = .25
    val data: RDD[Double] = sparkSession.sparkContext.parallelize(new BinomialDistribution(trials, p).sample(sampleSize).map(i => i.toDouble))

    // when
    assertThrows[IllegalArgumentException] {
      val computeDefaultArguments = PrivateMethod[DistributionFitterUtil.type]('computeDefaultArguments)
      (DistributionFitterUtil invokePrivate computeDefaultArguments(data, Distribution.Uniform)).asInstanceOf[DistributionFitterArgument]
    }
  }

}
