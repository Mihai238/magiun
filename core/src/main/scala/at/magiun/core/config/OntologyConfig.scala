package at.magiun.core.config

import com.softwaremill.tagging._
import openllet.jena.PelletReasonerFactory
import org.apache.jena.ontology.OntModel
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils.langTurtle

object OntologyConfig {

  val NS = "http://www.magiun.io/ontologies/ml#"

  val ColumnClass = "Column"
  val OperationClass = "Operation"
  val ValueClass = "Value"

  val shacl = "http://www.w3.org/ns/shacl#"

  private val baseFileName = "data_reasoner.ttl"
  private val demographicFileName = "demographic.ttl"

  def create(): OntModel @@ FeatureEngOntology = {
    val model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC)
    model.read(this.getClass.getClassLoader.getResourceAsStream(baseFileName), null, langTurtle)

    val demographicModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC)
    demographicModel.read(this.getClass.getClassLoader.getResourceAsStream(demographicFileName), null, langTurtle)

    model.add(demographicModel)
    model.taggedWith[FeatureEngOntology]
  }

}

trait FeatureEngOntology
