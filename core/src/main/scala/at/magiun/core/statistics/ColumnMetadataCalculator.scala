package at.magiun.core.statistics

import at.magiun.core.feature.{BasicMeta, ColumnMetaData, Restriction, SummaryStatistics}
import at.magiun.core.model.data.Distribution
import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.math3.distribution._
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.execution.stat.StatFunctions
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
  * Computes value types and other metadata for each column of the data set
  */
class ColumnMetadataCalculator(
                              sparkSession: SparkSession
                            ) extends LazyLogging with Serializable {

  import sparkSession.implicits._

  def compute(ds: Dataset[Row], restrictions: Map[String, Restriction]): Seq[ColumnMetaData] = {
    logger.info("Computing column metadata.")

    val basicMeta = ds
      .map(row => computeValueTypeForRow(row, restrictions))
      .reduce((colMeta1, colMeta2) => combine(colMeta1, colMeta2))

    val distinctCounts = ds.select(ds.columns.map(c => countDistinct(col(s"`$c`")).alias(c)): _*).first().toSeq

    logger.info("Computing summary statistics for column metadata.")
    val summaryStatistics = computeSummaryStatistics(ds)

    logger.info("Computing distributions for column metadata.")
    val distributions = computeDistributions(ds, summaryStatistics)

    basicMeta.map(ColumnMetaData(_))
      .zip(distinctCounts).map { case (meta, distinctCount) =>
      meta.copy(uniqueValues = distinctCount.asInstanceOf[Long])
    }
      .zip(summaryStatistics).map { case (meta, s) =>
      meta.copy(stats = s)
    }
      .zip(distributions).map { case (meta, d) =>
      meta.copy(distributions = d)
    }
  }

  private def combine(left: Seq[BasicMeta], right: Seq[BasicMeta]): Seq[BasicMeta] = {
    (left zip right).map { case (l, r) => l.combine(r) }
  }

  private def isColMeta(row: Row): Boolean = {
    row.get(0).isInstanceOf[ColumnMetaData]
  }

  def computeValueTypeForRow(row: Row, restrictions: Map[String, Restriction]): Seq[BasicMeta] = {
    (0 until row.size).map { colIndex => {
      val value = row.get(colIndex)

      if (isMissingValue(value, restrictions("MissingValue"))) {
        BasicMeta(Set(), Set(), 1)

      } else {
        val valueTypes = restrictions
          .map { case (valueType, restr) =>
            logger.debug(s"Checking type $valueType for value $value")
            if (restr.check(value)) {
              valueType
            } else {
              null
            }
          }.filter(_ != null)

        BasicMeta(valueTypes.toSet, valueTypes.toSet, 0)
      }
    }
    }
  }

  private def isMissingValue(value: Any, restriction: Restriction): Boolean = {
    value match {
      case null => true
      case v => restriction.check(v)
    }
  }

  private def computeSummaryStatistics(ds: Dataset[Row]): Seq[SummaryStatistics] = {
    val statsSummary = StatFunctions.summary(ds, Seq("count", "mean", "stddev", "min", "max", "50%"))

    val stats = statsSummary.collect().toSeq
      .map(row => {
        row.toSeq.drop(1)
          .map(e => Option(e).map(_.asInstanceOf[String]))
      })

    val count = stats.head.map(e => e.get.toLong)
    val mean = stats(1).map(_.flatMap(e => parseDouble(e)))
    val stddev = stats(2).map(_.flatMap(e => parseDouble(e)))
    val min = stats(3).map(_.flatMap(e => parseDouble(e)))
    val max = stats(4).map(_.flatMap(e => parseDouble(e)))
    val median = stats(5).map(_.flatMap(e => parseDouble(e)))

    ds.schema.indices.map { colIndex =>
      SummaryStatistics(count(colIndex), mean(colIndex), stddev(colIndex), min(colIndex), max(colIndex), median(colIndex))
    }
  }

  private def parseDouble(s: String): Option[Double] = try {
    Some(s.toDouble)
  } catch {
    case _: Exception => None
  }

  private def computeDistributions(ds: Dataset[Row], summaryStatistics: Seq[SummaryStatistics]): Seq[Set[Distribution]] = {
    val schema = ds.schema

    schema.indices.map { colIndex =>
      val stats = summaryStatistics(colIndex)
      val colType = schema(colIndex).dataType.typeName
      if (colType == "integer" || colType == "double") {
        val doubles = ds
          .map(e => Option(e.get(colIndex)).map(_.toString))
          .map(_.flatMap(e => parseDouble(e)))
          .filter(_.isDefined)
          .map(_.get)
          .rdd

        val normal = isDistributed(doubles, new NormalDistribution(stats.mean.get, stats.stddev.get))
        val gamma = isDistributed(doubles, new GammaDistribution(1, 2))
        val exponential = isDistributed(doubles, new ExponentialDistribution(null, stats.mean.get))
        val uniform = isDistributed(doubles, new UniformRealDistribution(stats.min.get, stats.max.get))

        Set[Distribution](
          if (normal) Distribution.Normal else null,
          if (uniform) Distribution.Uniform else null,
          if (exponential) Distribution.Exponential else null,
          if (gamma) Distribution.Gamma else null
        ).filter(e => e != null)
      } else {
        Set[Distribution]()
      }
    }
  }

  private def isDistributed(doubleCol: RDD[Double], dist: RealDistribution) = {
    val testResult = Statistics.kolmogorovSmirnovTest(doubleCol, (x: Double) => dist.cumulativeProbability(x))
    testResult.pValue > 0.05
  }

}
