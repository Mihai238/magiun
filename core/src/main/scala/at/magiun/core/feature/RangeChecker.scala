package at.magiun.core.feature

import org.apache.jena.rdf.model.{RDFNode, Statement}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

class RangeChecker extends Checker {

  override def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, restriction: Statement): Boolean = {
    import sparkSession.implicits._

    val rangeRestriction = restriction.getPredicate.getLocalName
    val value = restriction.getObject.asLiteral().getInt

    val rangeFunction = rangeRestriction match {
      case "minExclusive" =>
        (x: Double) => value < x
      case "maxExclusive" =>
        (x: Double) => value > x
      case "maxInclusive" =>
        (x: Double) => value >= x
      case "minInclusive" =>
        (x: Double) => value <= x
    }

    val f = ds.map { row: Row =>
      val value = row.get(colIndex)
      if (value != null && !rangeFunction(value.toString.toDouble)) {
        false
      } else {
        true
      }
    }.collect()

    !f.contains(false)
  }

}
