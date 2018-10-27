package at.magiun.core.feature

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection._

class ValueTypeComputer(
                       sparkSession: SparkSession
                     ) extends LazyLogging with Serializable {

  def process(ds: Dataset[Row], restrictions: Map[String, Restriction]): Seq[Set[String]] = {
    val colCount = ds.schema.indices.size

    ds.reduce((row1, row2) => {
      if (!row1.get(0).isInstanceOf[Set[String]] && !row2.get(0).isInstanceOf[Set[String]]) {
        val sets1 = compute(row1, restrictions)
        compute(row2, restrictions)
        Row.fromSeq(sets1)  // TODO
      } else if (row1.get(0).isInstanceOf[Set[String]] && !row2.get(0).isInstanceOf[Set[String]]) {
        val left = row1.toSeq.asInstanceOf[Seq[Set[String]]]
        val right = compute(row2, restrictions)
        val result = (left zip right).map{ case (l, r) => l ++ r}
        Row.fromSeq(result)
      } else {
        throw new IllegalStateException
      }
    }).toSeq.map(_.asInstanceOf[Set[String]])
  }


  def compute(row: Row, restrictions: Map[String, Restriction]): Seq[Set[String]] = {
    (0 until row.size).map { colIndex =>
      restrictions.map { case (valueType, restr) =>
        val value = row.get(colIndex)
        logger.info(s"Checking type $valueType for value $value")
        if (value != null && restr.check(value)) {
          valueType
        } else {
          null
        }
      }
        .filter(_ != null)
        .toSet
    }
  }

}
