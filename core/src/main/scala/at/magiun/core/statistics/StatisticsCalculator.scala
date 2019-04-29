package at.magiun.core.statistics

import at.magiun.core.model.data.Distribution
import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.math3.distribution._
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

class StatisticsCalculator(sparkSession: SparkSession) extends LazyLogging with Serializable {

  def calculateDistributions(df: DataFrame): Map[String, Distribution] = {
    val schema = df.schema
    import sparkSession.implicits._

    schema.indices.filter{ i =>
      val dataType = schema(i).dataType.typeName
      dataType.equals("integer") || dataType.equals("double")
    }.map{ i =>
      val columnName = schema(i).name
      val values = df.select(s"`$columnName`")
        .rdd
        .map(r => r.get(0))
        .filter(v => v != null)
        .map(v => v.toString.toDouble)

      val mean = values.mean()
      val sd = values.stdev()
      val min = values.min()
      val max = values.max()

      val binomialTrials = (values.count() * 0.10).intValue()

      val normal = isDistributed(values, new NormalDistribution(mean, sd))
      val gamma = isDistributed(values, new GammaDistribution(1, 2))
      val exponential = isDistributed(values, new ExponentialDistribution(null, Math.max(1, mean)))
      val uniform = isDistributed(values, new UniformRealDistribution(min, max))
      val bernoulli = isDistributed(values, new BinomialDistribution(1, 0.5))
      val binomial = isDistributed(values, new BinomialDistribution(binomialTrials, 1/binomialTrials))

      val distributions = Set[Distribution](
        if (normal) Distribution.Normal else null,
        if (uniform) Distribution.Uniform else null,
        if (exponential) Distribution.Exponential else null,
        if (gamma) Distribution.Gamma else null,
        if (bernoulli) Distribution.Bernoulli else null,
        if (binomial) Distribution.Binomial else null
      ).filter(e => e != null)

      columnName -> distributions.find(d => d != null).getOrElse(Distribution.Unknown)
    }.toMap
  }

  private def isDistributed(doubleCol: RDD[Double], dist: RealDistribution) = {
    val testResult = Statistics.kolmogorovSmirnovTest(doubleCol, (x: Double) => dist.cumulativeProbability(x))
    testResult.pValue > 0.05
  }
  private def isDistributed(doubleCol: RDD[Double], dist: BinomialDistribution) = {
    val testResult = Statistics.kolmogorovSmirnovTest(doubleCol, (x: Double) => dist.cumulativeProbability(x.intValue()))
    testResult.pValue > 0.05
  }

}
