package at.magiun.core.feature

import at.magiun.core.feature.FeatureRecommender._
import org.apache.jena.ontology.OntClass
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.vocabulary.{OWL2, RDF}
import org.apache.spark.sql.{Dataset, Row}

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
  *
  */
object FeatureRecommender {
  val NS = "http://www.magiun.io/ontologies/data-ontology#"

  val ColumnClass = "Column"

  val TypeIntClass = "Int"
  val TypeStringClass = "String"
  val TypeDoubleClass = "Double"

  val HasTypeProperty = "hasType"
  val ValuesProperty = "values"

  val NumericTypes = Set("integer, double")
}

/**
  *
  */
class FeatureRecommender {

  def recommendFeatureOperation(ds: Dataset[Row]): Recommendation = {
    val model = ModelFactory.createOntologyModel()
    val is = this.getClass.getClassLoader.getResourceAsStream("data.owl")
    model.read(is, null)

    val classes = model.getOntClass(NS + ColumnClass).listSubClasses().toList
    val checker = new ColumnChecker()

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
class ColumnChecker {

  def check(ds: Dataset[Row], colIndex: Int, ontClass: OntClass): Boolean = {
    ontClass.listSuperClasses().toList.toList
      .filter(_.isRestriction)
      .map(_.asRestriction())
      .foldLeft(true) { (fulfilled, restriction) =>
        if (fulfilled) {
          val restrictionName = restriction.getOnProperty.getLocalName
          if (restrictionName == HasTypeProperty) {
            if (restriction.isAllValuesFromRestriction) {
              val restrictionType = restriction.asAllValuesFromRestriction().getAllValuesFrom.getLocalName
              if (restrictionType == TypeIntClass || restrictionType == TypeStringClass || restrictionType == TypeDoubleClass) {
                checkHasTypeAllValues(ds, colIndex, restrictionType)
              } else {
                throw new IllegalArgumentException(s"Restriction with type $restrictionType not supported")
              }
            } else {
              throw new IllegalArgumentException(s"Restriction $restriction not supported")
            }
          } else if (restrictionName == ValuesProperty) {
            if (restriction.isAllValuesFromRestriction) {
              val valueRestriction = restriction.asAllValuesFromRestriction().getAllValuesFrom.getPropertyResourceValue(OWL2.withRestrictions)
                .getPropertyResourceValue(RDF.first)
                .listProperties().toList.get(0)

              checkHasValuesInRange(ds, colIndex, valueRestriction.getPredicate.getLocalName, valueRestriction.getInt)
            }
            true
          } else {
            throw new IllegalArgumentException(s"Restriction property '$restrictionName' not supported")
          }
        } else {
          false
        }
      }
  }

  def checkHasTypeAllValues(ds: Dataset[Row], colIndex: Int, restrictionType: String): Boolean = {
    if (restrictionType == TypeIntClass && ds.schema(colIndex).dataType.typeName == "integer") {
      true
    } else if (restrictionType == TypeStringClass && ds.schema(colIndex).dataType.typeName == "string") {
      true
    } else if (restrictionType == TypeDoubleClass && ds.schema(colIndex).dataType.typeName == "double") {
      true
    } else {
      false
    }
  }

  def checkHasValuesInRange(ds: Dataset[Row], colIndex: Int, rangeRestriction: String, value: Int): Boolean = {
    if (NumericTypes.contains(ds.schema(colIndex).dataType.typeName)) {
      val rangeFunction = rangeRestriction match {
        case "minExclusive" =>
          (x: Double) => value < x
        case "maxExclusive" =>
          (x: Double) => value > x
      }

      var result = true
      ds.foreach(row => {
        if (!rangeFunction(row.get(colIndex).toString.toDouble)) {
          result = false
        }
      })

      result
    } else {
      false
    }
  }
}

/**
  *
  */
case class Recommendation(map: Map[Int, List[String]])