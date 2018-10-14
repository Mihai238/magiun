package at.magiun.core.config

import org.apache.jena.ontology.OntModel
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils

object AlgorithmOntologyConfig {

  private val fileName = "model_selection.owl"
  val NS = "http://www.magiun.io/ontologies/model-selection#"

  def create(): OntModel = {
    val model = ModelFactory.createOntologyModel()
    val inputStream = this.getClass.getClassLoader.getResourceAsStream(fileName)
    model.read(inputStream, null, FileUtils.langXML)
    model
  }

}
