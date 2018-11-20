package at.magiun.core.feature

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.execution.stat.StatFunctions
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.immutable

/**
  * Computes value types and other metadata for each column of the data set
  */
class ColumnMetaDataComputer(
                              sparkSession: SparkSession
                            ) extends LazyLogging with Serializable {

  import sparkSession.implicits._

  def compute(ds: Dataset[Row], restrictions: Map[String, Restriction]): Seq[ColumnMetaData] = {
    logger.info("Computing column metadata")

    val columnsMeta = ds.reduce((row1, row2) => {
      val (left, right) =
        if (!isColMeta(row1) && !isColMeta(row2)) {
          (
            computeValueTypeForRow(row1, restrictions),
            computeValueTypeForRow(row2, restrictions)
          )
        } else if (isColMeta(row1) && !isColMeta(row2)) {
          (
            row1.toSeq.asInstanceOf[Seq[ColumnMetaData]],
            computeValueTypeForRow(row2, restrictions)
          )
        } else if (!isColMeta(row1) && isColMeta(row2)) {
          (
            computeValueTypeForRow(row2, restrictions),
            row2.toSeq.asInstanceOf[Seq[ColumnMetaData]]
          )
        } else if (isColMeta(row1) && isColMeta(row2)) {
          (
            row1.toSeq.asInstanceOf[Seq[ColumnMetaData]],
            row2.toSeq.asInstanceOf[Seq[ColumnMetaData]]
          )
        } else {
          throw new IllegalStateException
        }

      Row.fromSeq(combine(left, right))
    }).toSeq.map(_.asInstanceOf[ColumnMetaData])

    val distinctCounts = ds.select(ds.columns.map(c => countDistinct(col(s"`$c`")).alias(c)): _*).first().toSeq
    val summaryStatistics = computeSummaryStatistics(ds)
    val normalDistributions = checkNormalDistribution(ds, summaryStatistics)

    columnsMeta
      .zip(distinctCounts).map { case (meta, distinctCount) =>
      meta.copy(uniqueValues = distinctCount.asInstanceOf[Long])
    }
      .zip(summaryStatistics).map { case (meta, stats) =>
      meta.copy(stats = stats)
    }
      .zip(normalDistributions).map { case (meta, normalDistributed) =>
        meta.copy(normalDistributed = normalDistributed)
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
    val stats =
      StatFunctions.summary(ds, Seq("count", "mean", "stddev", "min", "max", "50%")).collect().toSeq
        .map(row => {
          row.toSeq.drop(1)
            .map(e => Option(e))
            .map(e => e.map(_.asInstanceOf[String]))
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

  private def checkNormalDistribution(ds: Dataset[Row], summaryStatistics: Seq[SummaryStatistics]): immutable.Seq[Boolean] = {
    ds.schema.indices.map { colIndex =>
      val mean = summaryStatistics(colIndex).mean
      val stddev = summaryStatistics(colIndex).stddev
      if (mean.isDefined && stddev.isDefined) {
        val doubleCol = ds
          .map(e => Option(e.get(colIndex)).map(_.toString))
          .map(_.flatMap(e => parseDouble(e)))
          .filter(_.isDefined)
          .map(_.get)
          .rdd
        isNormalDistributed(doubleCol, summaryStatistics(colIndex))
      } else {
        false
      }
    }
  }

  private def isNormalDistributed(doubleCol: RDD[Double], stats: SummaryStatistics) = {
    val testResult = Statistics.kolmogorovSmirnovTest(doubleCol, "norm", stats.mean.get, stats.stddev.get)
    logger.info(testResult.toString())
    testResult.pValue > 0.05
  }

}
