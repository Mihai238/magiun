package at.magiun.core.feature

import at.magiun.core.feature.FeatureRecommender._
import org.apache.jena.ontology.OntClass
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils.langTurtle
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
  *
  */
object FeatureRecommender {
  val NS = "http://www.magiun.io/ontologies/data-ontology#"

  val ColumnClass = "Column"

  val HasTypeProperty = "hasType"
  val ValuesProperty = "values"

  val shacl = "http://www.w3.org/ns/shacl#"

  val checkers = Map(
    (shacl + "dataType") -> new DataTypeChecker,
    (shacl + "minInclusive") -> new RangeChecker,
    (shacl + "maxInclusive") -> new RangeChecker
  )
}

/**
  *
  */
class FeatureRecommender(sparkSession: SparkSession) {

  def recommendFeatureOperation(ds: Dataset[Row]): Recommendation = {
    val model = ModelFactory.createOntologyModel()
    val is = this.getClass.getClassLoader.getResourceAsStream("data.ttl")
    model.read(is, null, langTurtle)

    val classes = model.getOntClass(NS + ColumnClass).listSubClasses().toList
    val checker = new ColumnChecker(sparkSession)

    val result = mutable.Map[Int, List[String]]()

    for (colIndex <- ds.schema.indices) {
      result.put(colIndex, List())
      classes.foreach { cls =>
        if (checker.check(ds, colIndex, cls)) {
          val colClasses = result(colIndex)
          result.put(colIndex, cls.getLocalName :: colClasses)
        }
      }
    }

    println("Done")
    Recommendation(result.toMap)
  }
}

/**
  *
  */
class ColumnChecker(sparkSession: SparkSession) {

  def check(ds: Dataset[Row], colIndex: Int, ontClass: OntClass): Boolean = {
    ontClass.listSuperClasses().toList.toList
      .filter(_.isResource)
      .map(_.asResource())
      .flatMap(restrictions => {
        restrictions.listProperties().toList.toList
          .filter(e => e.getPredicate.getLocalName == "property")
      })
      .map(_.getResource)
      .flatMap(_.listProperties().toList)
      .foldLeft(true) { (fulfilled, restriction) =>
        if (fulfilled) {
          checkers.getOrElse(restriction.getPredicate.toString, new NoopChecker)
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
case class Recommendation(map: Map[Int, List[String]])