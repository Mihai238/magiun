package at.magiun.core.feature

import at.magiun.core.feature.Recommender._
import com.typesafe.scalalogging.LazyLogging
import org.apache.jena.ontology.OntClass
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils.langTurtle
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
  *
  */
object Recommender {
  val NS = "http://www.magiun.io/ontologies/ml#"

  val ColumnClass = "Column"
  val OperationClass = "Operation"

  val HasTypeProperty = "hasType"
  val ValuesProperty = "values"

  val shacl = "http://www.w3.org/ns/shacl#"

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
class Recommender(sparkSession: SparkSession) extends LazyLogging {

  def recommendFeatureOperation(ds: Dataset[Row]): Recommendations = {
    val model = ModelFactory.createOntologyModel()
    val is = this.getClass.getClassLoader.getResourceAsStream("data.ttl")
    model.read(is, null, langTurtle)

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

    val operations = model.getOntClass(NS + ColumnClass).listSubClasses().toList
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