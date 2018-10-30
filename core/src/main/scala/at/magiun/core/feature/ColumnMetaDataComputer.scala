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
      if (!row1.get(0).isInstanceOf[ColumnMetaData] && !row2.get(0).isInstanceOf[ColumnMetaData]) {
        val left = computeValueTypeForRow(row1, restrictions)
        val right = computeValueTypeForRow(row2, restrictions)
        Row.fromSeq(combine(left, right))
      } else if (row1.get(0).isInstanceOf[ColumnMetaData] && !row2.get(0).isInstanceOf[ColumnMetaData]) {
        val left = row1.toSeq.asInstanceOf[Seq[ColumnMetaData]]
        val right = computeValueTypeForRow(row2, restrictions)
        Row.fromSeq(combine(left, right))
      } else {
        throw new IllegalStateException
      }
    }).toSeq.map(_.asInstanceOf[ColumnMetaData])
  }

  def combine(left: Seq[ColumnMetaData], right: Seq[ColumnMetaData]): Seq[ColumnMetaData] = {
    (left zip right).map { case (l, r) => l.combine(r) }
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
