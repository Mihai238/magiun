package at.magiun.core.feature

import org.apache.jena.rdf.model.{RDFList, Statement}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConversions._

class OrChecker(checkers: => Map[String, Checker]) extends Checker {

  override def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, orRestriction: Statement): Boolean = {
    val orIterator = orRestriction.getObject.asResource().as(classOf[RDFList]).iterator()

    var fulfilled = false

    while (orIterator.hasNext && !fulfilled) {
      val restrictions = orIterator.next().asResource().listProperties().toList.toList

      fulfilled = restrictions.foldLeft(true) { (fulfilled, restriction) =>
          if (fulfilled) {
            val predicateName = restriction.getPredicate.toString
            checkers.getOrElse(predicateName, new NoopChecker(predicateName))
              .check(sparkSession, ds, colIndex, restriction)
          } else {
            false
          }
        }
    }

    fulfilled
  }

}