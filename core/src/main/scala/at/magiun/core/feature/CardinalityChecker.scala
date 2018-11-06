package at.magiun.core.feature

import org.apache.jena.rdf.model.Statement
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.mutable

class CardinalityChecker extends Checker {

  override def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, restriction: Statement): Boolean = {
    val col = ds.col(s"`${ds.columns(colIndex)}`")
    val cardinality = ds.groupBy(col).count().count()
    val expected = restriction.getObject.asLiteral().getInt
    restriction.getPredicate.getLocalName match {
      case "minCount" =>
        expected <= cardinality
      case "maxCount" =>
        cardinality <= expected
    }
  }

}
