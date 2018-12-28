package at.magiun.core.model.ontology

object RDFProperty extends Enumeration {

  private val RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"

  val isOfType: RDFProperty.Value = Value(RDF_NS + "type")

}
