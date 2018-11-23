package at.magiun.core.feature

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
class ColumnMetaDataComputer(
                              sparkSession: SparkSession
                            ) extends LazyLogging with Serializable {

  import sparkSession.implicits._

  def compute(ds: Dataset[Row], restrictions: Map[String, Restriction]): Seq[ColumnMetaData] = {
    logger.info("Computing column metadata.")

    val columnsMeta = ds.reduce((row1, row2) => {
      val left = if (isColMeta(row1)) row1.toSeq.asInstanceOf[Seq[ColumnMetaData]] else computeValueTypeForRow(row1, restrictions)
      val right = if (isColMeta(row2)) row2.toSeq.asInstanceOf[Seq[ColumnMetaData]] else computeValueTypeForRow(row2, restrictions)

      Row.fromSeq(combine(left, right))
    }).toSeq.map(_.asInstanceOf[ColumnMetaData])

    val distinctCounts = ds.select(ds.columns.map(c => countDistinct(col(s"`$c`")).alias(c)): _*).first().toSeq

    logger.info("Computing summary statistics for column metadata.")
    val summaryStatistics = computeSummaryStatistics(ds)

    logger.info("Computing distributions for column metadata.")
    val distributions = computeDistributions(ds, summaryStatistics)

    columnsMeta
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

  private def combine(left: Seq[ColumnMetaData], right: Seq[ColumnMetaData]): Seq[ColumnMetaData] = {
    (left zip right).map { case (l, r) => l.combine(r) }
  }

  private def isColMeta(row: Row): Boolean = {
    row.get(0).isInstanceOf[ColumnMetaData]
  }

  def computeValueTypeForRow(row: Row, restrictions: Map[String, Restriction]): Seq[ColumnMetaData] = {
    (0 until row.size).map { colIndex => {
      val value = row.get(colIndex)

      if (isMissingValue(value)) {
        ColumnMetaData(Set(), 1)

      } else {
        val valueTypes = restrictions.map { case (valueType, restr) =>
          logger.debug(s"Checking type $valueType for value $value")
          if (restr.check(value)) {
            valueType
          } else {
            null
          }
        }.filter(_ != null)

        //        if (colIndex == 5 && !valueTypes.toSet.contains("HumanAgeValue")) {
        //          logger.error(s"$value is wrong")
        //        }

        ColumnMetaData(valueTypes.toSet, 0)
      }
    }
    }
  }

  def isMissingValue(value: Any): Boolean = {
    value match {
      case null => true
      case v: String => v == "" || v.equalsIgnoreCase("NA")
      case _ => false
    }
  }

  private def computeSummaryStatistics(ds: Dataset[Row]): Seq[SummaryStatistics] = {
    val statsSummary = StatFunctions.summary(ds, Seq("count", "mean", "stddev", "min", "max", "50%"))

    val stats = statsSummary.collect().toSeq
        .map(row => {
          row.toSeq.drop(1)
            .map(e => Option(e).map(_.asInstanceOf[String]))
        })

    val count = stats(0).map(e => e.get.toLong)
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

    ds.schema.indices.map { colIndex =>
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
        val uniform = isDistributed(doubles, new UniformRealDistribution(stats.min.get, stats.max.get))
        val exponential = isDistributed(doubles, new ExponentialDistribution(null, stats.mean.get))

        Set[Distribution](
          if (normal) Distribution.Normal else null,
          if (uniform) Distribution.Uniform else null ,
          if (exponential) Distribution.Exponential else null
        ).filter(e => e != null)
      } else {
        Set[Distribution]()
      }
    }
  }

  private def isDistributed(doubleCol: RDD[Double], dist: RealDistribution) = {
    val testResult = Statistics.kolmogorovSmirnovTest(doubleCol, (x:Double) => dist.cumulativeProbability(x))
    testResult.pValue > 0.05
  }

}
