package at.magiun.core.feature

import org.apache.jena.ontology.OntModel

import scala.collection.JavaConversions._
import at.magiun.core.config.OntologyConfig._
import org.apache.jena.rdf.model.Resource

class RestrictionBuilder {

  def build(model: OntModel): Map[String, Restriction] = {
    val shaclProperty = model.getProperty(shacl + "property")

    model.getOntClass(NS + ValueClass).listSubClasses().toList.toList
      .filter(_.getNameSpace == NS)
      .map(valueClass => {
      Option(valueClass.getProperty(shaclProperty))
        .map(_.getResource)
        .map(shaclProp => (valueClass.getLocalName, buildRestriction(shaclProp)))
        .getOrElse((valueClass.getLocalName, new NoOpRestriction()))
    }).toMap
  }

  private def buildRestriction(shaclProperties: Resource): Restriction = {
    val restrictions = shaclProperties.listProperties().toList.toList
      .map(stmt =>
        stmt.getPredicate.getLocalName match {
          case "pattern" => new PatternRestriction(stmt.getLiteral.getString)
          case _ => new NoOpRestriction()
        }
      )

    new AndRestriction(restrictions)
  }

}


