package at.magiun.core.feature
import org.apache.jena.rdf.model.{RDFNode, Resource, Statement}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

class NoopChecker extends Checker {

  override def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, restriction: Statement): Boolean = true

}
