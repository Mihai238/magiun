package at.magiun.core.feature

import at.magiun.core.feature.Recommender._
import com.typesafe.scalalogging.LazyLogging
import org.apache.jena.ontology.{OntClass, OntModel}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import at.magiun.core.config.OntologyConfig._

/**
  *
  */
object Recommender {

  val checkers: Map[String, Checker] = getCheckers

  private def getCheckers: Map[String, Checker] = {
    Map(
      (shacl + "dataType") -> new DataTypeChecker,
      (shacl + "minInclusive") -> new RangeChecker(new DataTypeChecker),
      (shacl + "maxInclusive") -> new RangeChecker(new DataTypeChecker),
      (shacl + "in") -> new EnumChecker,
      (shacl + "minCount") -> new CardinalityChecker,
      (shacl + "maxCount") -> new CardinalityChecker,
      (shacl + "or") -> new OrChecker(getCheckers)
    )
  }
}

/**
  *
  */
class Recommender(sparkSession: SparkSession, model: OntModel) extends LazyLogging {

  def recommendFeatureOperation(ds: Dataset[Row]): Recommendations = {
    val classes = model.getOntClass(NS + ColumnClass).listSubClasses().toList
    val checker = new ColumnChecker(sparkSession)

    val colTypesMap: Map[Int, List[String]] = ds.schema.indices.map(colIndex => {
      val colTypes = ListBuffer[String]()
      classes.foreach { cls =>
        if (checker.check(ds, colIndex, cls)) {
          colTypes.add(cls.getLocalName)
        }
      }
      (colIndex, colTypes.toList)
    }).toMap

    val recsMap = colTypesMap map {
      case (colIndex, colTypes) =>
        (colIndex, Recommendation(colTypes, List()))
    }

    logger.info("Done")
    Recommendations(recsMap)
  }
}

/**
  *
  */
class ColumnChecker(sparkSession: SparkSession) extends LazyLogging {

  def check(ds: Dataset[Row], colIndex: Int, ontClass: OntClass): Boolean = {
    val restrictions = ontClass.listSuperClasses().toList.toList
      .filter(_.isResource)
      .map(_.asResource())
      .flatMap(restrictions => {
        restrictions.listProperties().toList.toList
          .filter(e => e.getPredicate.getLocalName == "property")
      })
      .map(_.getResource)
      .flatMap(_.listProperties().toList)

    restrictions.foldLeft(true) { (fulfilled, restriction) =>
      if (fulfilled) {
        val predicateName = restriction.getPredicate.toString
        checkers.getOrElse(predicateName, new NoopChecker(predicateName))
          .check(sparkSession, ds, colIndex, restriction)
      } else {
        false
      }
    }
  }

}

/**
  *
  */
case class Recommendations(map: Map[Int, Recommendation])

case class Recommendation(colTypes: List[String], operations: List[String])