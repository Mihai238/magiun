package at.magiun.core.feature

import at.magiun.core.config.FeatureEngOntology
import org.apache.jena.ontology.OntModel

import scala.collection.JavaConversions._
import at.magiun.core.config.OntologyConfig._
import com.softwaremill.tagging.@@
import org.apache.jena.rdf.model.{RDFList, Resource}

/**
  * Assigns for each value type (IntValue, HumanAgeValue etc.) a restriction
  */
class RestrictionBuilder {

  def build(model: OntModel @@ FeatureEngOntology): Map[String, Restriction] = {
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
          case "minInclusive" => new MinInclusiveRestriction(stmt.getLiteral.getInt)
          case "maxInclusive" => new MaxInclusiveRestriction(stmt.getLiteral.getInt)
          case "datatype" => new DataTypeRestriction(stmt.getObject.asResource().getLocalName)
          case "or" =>
            val it = stmt.getObject.asResource().as(classOf[RDFList]).iterator()
            val rs = for {
              orEntry <- it
              restriction = buildRestriction(orEntry.asResource())
            } yield restriction

            new OrRestriction(rs.toList)

          case "and" =>
            val it = stmt.getObject.asResource().as(classOf[RDFList]).iterator()
            val rs = for {
              orEntry <- it
              restriction = buildRestriction(orEntry.asResource())
            } yield restriction

            new AndRestriction(rs.toList)

          case _ => new NoOpRestriction()
        }
      )

    new AndRestriction(restrictions)
  }

}


