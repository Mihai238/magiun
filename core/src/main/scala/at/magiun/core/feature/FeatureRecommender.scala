package at.magiun.core.feature

import at.magiun.core.feature.FeatureRecommender._
import org.apache.jena.ontology.OntClass
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils.langTurtle
import org.apache.jena.vocabulary.{OWL2, RDF}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
  *
  */
object FeatureRecommender {
  val NS = "http://www.magiun.io/ontologies/data-ontology#"

  val ColumnClass = "Column"

  val TypeNumericClass = "Numeric"
  val TypeIntClass = "Int"
  val TypeStringClass = "String"
  val TypeDoubleClass = "Double"

  val HasTypeProperty = "hasType"
  val ValuesProperty = "values"

  val typeClassToColumnType = Map(
    TypeIntClass -> Set("integer"),
    TypeDoubleClass -> Set("double"),
    TypeNumericClass -> Set("integer", "double"),
    TypeStringClass -> Set("string")
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

  import sparkSession.implicits._

//  val x = model.getProperty("http://www.w3.org/ns/shacl#property")
//  val y = model.getProperty("http://www.w3.org/ns/shacl#maxInclusive")
//  model.getOntClass(NS + ColumnClass).listSubClasses().toList.get(1).listSuperClasses().toList.get(0).getPropertyValue(x)
//    .asResource().getProperty(y).getInt

//  model.getOntClass(NS + ColumnClass).listSubClasses().toList.get(1).listSuperClasses().toList.get(0)
//    .listProperties().toList.get(1).getObject.asResource().listProperties().toList

  def check(ds: Dataset[Row], colIndex: Int, ontClass: OntClass): Boolean = {
    ontClass.listSuperClasses().toList.toList
      .filter(_.isResource)
      .map(_.asResource())
      .foldLeft(true) { (fulfilled, restriction) =>
        if (fulfilled) {
          true

//          val restrictionName = restriction.getOnProperty.getLocalName
//          if (restrictionName == HasTypeProperty) {
//            if (restriction.isAllValuesFromRestriction) {
//              val restrictionType = restriction.asAllValuesFromRestriction().getAllValuesFrom.getLocalName
//              if (Set(TypeNumericClass, TypeIntClass, TypeDoubleClass, TypeStringClass).contains(restrictionType)) {
//                checkHasTypeAllValues(ds, colIndex, restrictionType)
//              } else {
//                throw new IllegalArgumentException(s"Restriction with type $restrictionType not supported")
//              }
//            } else {
//              throw new IllegalArgumentException(s"Restriction $restriction not supported")
//            }
//          } else if (restrictionName == ValuesProperty) {
//            if (restriction.isAllValuesFromRestriction) {
//              val valueRestriction = restriction.asAllValuesFromRestriction().getAllValuesFrom.getPropertyResourceValue(OWL2.withRestrictions)
//                .getPropertyResourceValue(RDF.first)
//                .listProperties().toList.get(0)
//
//              checkHasValuesInRange(ds, colIndex, valueRestriction.getPredicate.getLocalName, valueRestriction.getInt)
//            } else {
//              throw new IllegalArgumentException(s"Restriction of type $ValuesProperty as $restriction not supported")
//            }
//          } else {
//            throw new IllegalArgumentException(s"Restriction property '$restrictionName' not supported")
//          }
        } else {
          false
        }
      }
  }

  def checkHasTypeAllValues(ds: Dataset[Row], colIndex: Int, restrictionType: String): Boolean = {
    val typeName = ds.schema(colIndex).dataType.typeName
    typeClassToColumnType.get(restrictionType)
      .exists(_.contains(typeName))
  }

  val NumericTypes = Set("integer, double")

  def checkHasValuesInRange(ds: Dataset[Row], colIndex: Int, rangeRestriction: String, value: Int): Boolean = {
    if (typeClassToColumnType(TypeNumericClass).contains(ds.schema(colIndex).dataType.typeName)) {
      val rangeFunction = rangeRestriction match {
        case "minExclusive" =>
          (x: Double) => value < x
        case "maxExclusive" =>
          (x: Double) => value > x
      }

      val f = ds.map { row: Row =>
        val value = row.get(colIndex)
        if (value != null && !rangeFunction(value.toString.toDouble)) {
          false
        } else {
          true
        }
      }.collect()

      !f.contains(false)
    } else {
      false
    }
  }
}

/**
  *
  */
case class Recommendation(map: Map[Int, List[String]])