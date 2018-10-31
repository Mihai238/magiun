package at.magiun.core.feature

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
  * Computes value types and other metadata for each column of the data set
  */
class ColumnMetaDataComputer(
                              sparkSession: SparkSession
                            ) extends LazyLogging with Serializable {

  def compute(ds: Dataset[Row], restrictions: Map[String, Restriction]): Seq[ColumnMetaData] = {
    val colCount = ds.schema.indices.size

    ds.reduce((row1, row2) => {
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
        ColumnMetaData(Set(), Set())

      } else {
        val valueTypes = restrictions.map { case (valueType, restr) =>
          logger.debug(s"Checking type $valueType for value $value")
          if (restr.check(value)) {
            valueType
          } else {
            null
          }
        }.filter(_ != null)

        //        if (colIndex == 5 && !valueTypes.toSet.contains("MaritalStatusValue")) {
        //          logger.error(s"$value is wrong")
        //        }

        ColumnMetaData(Set(value.toString), valueTypes.toSet)
      }
    }
    }
  }

  def isMissingValue(value: Any): Boolean = {
    value match {
      case null => true
      case v: String => v == ""
      case v: Int => v == 0
      case v: Double => v == 0
      case _ => false
    }
  }

}
