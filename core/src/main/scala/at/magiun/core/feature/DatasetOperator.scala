package at.magiun.core.feature

import org.apache.spark.sql.{Dataset, Row, SparkSession}

class DatasetOperator(sparkSession: SparkSession, ds: Dataset[Row]) {

  import sparkSession.implicits._

  def allMatch(colIndex: Int, predicate: String => Boolean) = {
    val f = ds.map { row: Row =>
      val value = row.get(colIndex)
      if (value != null && !predicate(value.toString)) {
        false
      } else {
        true
      }
    }.collect()

    !f.contains(false)
  }

}
