package at.magiun.core.feature

import org.apache.jena.rdf.model.Statement
import org.apache.spark.sql.{Dataset, Row, SparkSession}

class RangeChecker(dataTypeChecker: DataTypeChecker) extends Checker {

  override def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, restriction: Statement): Boolean = {

    if (!dataTypeChecker.check(sparkSession, ds, colIndex, "number")) {
      false

    } else {
      val rangeRestriction = restriction.getPredicate.getLocalName
      val value = restriction.getObject.asLiteral().getInt

      val rangeFunction = rangeRestriction match {
        case "minExclusive" =>
          (x: String) => value < x.toDouble
        case "maxExclusive" =>
          (x: String) => value > x.toDouble
        case "maxInclusive" =>
          (x: String) => value >= x.toDouble
        case "minInclusive" =>
          (x: String) => value <= x.toDouble
      }

      new DatasetOperator(sparkSession, ds)
        .allMatch(colIndex, rangeFunction)
    }

  }

}
