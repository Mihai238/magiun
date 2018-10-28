package at.magiun.core.feature

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{Dataset, Row, SparkSession}

class ColumnMetaDataComputer(
                         sparkSession: SparkSession
                       ) extends LazyLogging with Serializable {

  def compute(ds: Dataset[Row], restrictions: Map[String, Restriction]): Seq[ColumnMetaData] = {
    val colCount = ds.schema.indices.size

    ds.reduce((row1, row2) => {
      if (!row1.get(0).isInstanceOf[ColumnMetaData] && !row2.get(0).isInstanceOf[ColumnMetaData]) {
        val left = computeValueTypeForRow(row1, restrictions)
        val right = computeValueTypeForRow(row2, restrictions)
        val result = (left zip right).map { case (l, r) => l.combine(r) }
        Row.fromSeq(result)
      } else if (row1.get(0).isInstanceOf[ColumnMetaData] && !row2.get(0).isInstanceOf[ColumnMetaData]) {
        val left = row1.toSeq.asInstanceOf[Seq[ColumnMetaData]]
        val right = computeValueTypeForRow(row2, restrictions)
        val result = (left zip right).map { case (l, r) => l.combine(r) }
        Row.fromSeq(result)
      } else {
        throw new IllegalStateException
      }
    }).toSeq.map(_.asInstanceOf[ColumnMetaData])
  }


  def computeValueTypeForRow(row: Row, restrictions: Map[String, Restriction]): Seq[ColumnMetaData] = {
    (0 until row.size).map { colIndex => {
      val value = row.get(colIndex)

      if (value == null) {
        ColumnMetaData(Set(), Set())

      } else {
        val valueTypes = restrictions.map { case (valueType, restr) =>
          logger.debug(s"Checking type $valueType for value $value")
          if (value != null && restr.check(value)) {
            valueType
          } else {
            null
          }
        }.filter(_ != null)

        ColumnMetaData(Set(value.toString), valueTypes.toSet)
      }
    }}
  }

}
