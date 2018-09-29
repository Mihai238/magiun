package at.magiun.core.feature

import org.apache.jena.rdf.model.Statement
import org.apache.spark.sql.{Dataset, Row, SparkSession}

class EnumChecker extends Checker {

  override def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, restriction: Statement): Boolean = {
    ???
  }

}
