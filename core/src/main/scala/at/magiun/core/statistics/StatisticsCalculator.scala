package at.magiun.core.statistics

import at.magiun.core.model.statistics.StatisticsUtil
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

class StatisticsCalculator(sparkSession: SparkSession) {

  def isNormallyDistributed(column: RDD[Double]): Boolean = {
    val testResult = Statistics.kolmogorovSmirnovTest(column, "norm", 29.7, 14.5) //mean & stddev of the column
    println(testResult)

    !(testResult.pValue <= StatisticsUtil.NORMALITY_TEST_MAX_P_VALUE)
  }

}
