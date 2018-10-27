package at.magiun.core.feature

import at.magiun.core.config.OntologyConfig.NS
import org.apache.jena.ontology.OntModel

import scala.collection.JavaConversions._
import scala.collection.Set

class ColumnTypeRecommender(model: OntModel) {

  def f(valueTypes: Seq[Set[String]]): Map[Int, List[String]] = {
    // TODO copy model
    // https://stackoverflow.com/questions/22713884/how-to-clone-or-copy-a-jena-ontology-model-ontmodel-to-apply-temporary-changes

    valueTypes.zipWithIndex.map { case (col, colIndex) =>
      val hasValueProperty = model.getProperty(NS + "hasValue")
      val valueTypes = col.asInstanceOf[Set[String]]
      val indv = model.createIndividual(NS + "col", model.getOntClass(NS + "Column"))
      valueTypes.foreach(valueType => {
        val anonInd = model.createIndividual(model.getOntClass(NS + valueType))
        indv.addProperty(hasValueProperty, anonInd)
      })
      val colTypes = model.listStatements(indv.asResource(), null, null).toList.toList
        .filter(model.contains)
        .filter(_.getPredicate.getLocalName == "type")
        .filter(_.getObject.asResource().getNameSpace == NS)
        .map(_.getObject.asResource().getLocalName)
      model.removeAll(indv, null, null)

      (colIndex, colTypes)
    }.toMap
  }

}
