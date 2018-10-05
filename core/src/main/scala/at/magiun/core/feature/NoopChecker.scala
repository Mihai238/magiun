package at.magiun.core.feature

import com.typesafe.scalalogging.LazyLogging
import org.apache.jena.rdf.model.Statement
import org.apache.spark.sql.{Dataset, Row, SparkSession}

class NoopChecker(predicateName: String) extends Checker with LazyLogging {

  override def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, restriction: Statement): Boolean = {
    if (predicateName.startsWith(Recommender.shacl)) {
      logger.warn("No checker for {}", predicateName)
    }
    true
  }

}
