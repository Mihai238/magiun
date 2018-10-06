package at.magiun.core.feature

import at.magiun.core.config.OntologyConfig._
import org.apache.jena.ontology.OntModel
import org.apache.jena.rdf.model.Property

import scala.collection.JavaConversions._
import scala.collection.mutable

class OperationRecommender(model: OntModel) {

  def recommend(colTypesMap: Map[Int, List[String]]): Map[Int, List[String]] = {
    val result = mutable.Map[Int, List[String]]()

    val operations = model.getOntClass(NS + OperationClass).listSubClasses().toList.toList
    operations.foreach { operation =>
      val opCondition = operation.listSuperClasses().toList.toList
        .filter(_.isResource)
        .map(_.asResource())
        .head
        .getRequiredProperty(prop(mlIf))
        .getObject.asResource()

      val columnCondition = opCondition.getProperty(prop(mlColumn))
      val requiredClass = columnCondition.getObject.asResource().getLocalName

      colTypesMap.foreach{case (colIndex, colTypes) =>
        if (colTypes.contains(requiredClass)) {
          val prevList = result.getOrElse(colIndex, List())
          result.put(colIndex, operation.getLocalName :: prevList)
        }
      }
    }

    result.toMap
  }

  private def prop(uri: String): Property = {
    model.getProperty(uri)
  }

}
