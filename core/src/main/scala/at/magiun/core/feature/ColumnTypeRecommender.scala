package at.magiun.core.feature

import at.magiun.core.config.OntologyConfig.NS
import org.apache.jena.ontology.OntModel

import scala.collection.JavaConversions._

class ColumnTypeRecommender(model: OntModel) {

  def recommend(columnsMetadata: Seq[ColumnMetaData]): Map[Int, List[String]] = {
    // TODO copy model
    // https://stackoverflow.com/questions/22713884/how-to-clone-or-copy-a-jena-ontology-model-ontmodel-to-apply-temporary-changes

    val cardinalityProperty = model.getProperty(NS + "cardinality")
    val hasValueProperty = model.getProperty(NS + "hasValue")

    columnsMetadata.zipWithIndex.map { case (colMeta, colIndex) =>
      val valueTypes = colMeta.valueTypes
      val indv = model.createIndividual(NS + "col", model.getOntClass(NS + "Column"))
      indv.addProperty(cardinalityProperty, model.createTypedLiteral(colMeta.uniqueValues.size.asInstanceOf[Integer]))
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
