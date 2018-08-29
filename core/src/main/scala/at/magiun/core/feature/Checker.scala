package at.magiun.core.feature

import org.apache.jena.rdf.model.{RDFNode, Resource, Statement}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

trait Checker {

  def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, restriction: Statement): Boolean

}
