package at.magiun.core.config

import openllet.jena.PelletReasonerFactory
import org.apache.jena.ontology.OntModel
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils.langTurtle

object OntologyConfig {

  val NS = "http://www.magiun.io/ontologies/ml#"
  val mlIf = NS + "if"
  val mlColumn = NS + "Column"

  val ColumnClass = "Column"
  val OperationClass = "Operation"
  val ValueClass = "Value"

  val HasTypeProperty = "hasType"
  val ValuesProperty = "values"

  val shacl = "http://www.w3.org/ns/shacl#"


  private val fileName = "data_reasoner.ttl"

  def create(): OntModel = {
    val model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC)
    val is = this.getClass.getClassLoader.getResourceAsStream(fileName)
    model.read(is, null, langTurtle)
    model
  }

}
