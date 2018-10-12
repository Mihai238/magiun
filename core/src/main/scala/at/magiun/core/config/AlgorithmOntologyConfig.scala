package at.magiun.core.config

import org.apache.jena.ontology.OntModel
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils

object AlgorithmOntologyConfig {

  private val fileName = "model_selection.owl"

  def create(): OntModel = {
    val model = ModelFactory.createOntologyModel()
    val inputStream = this.getClass.getClassLoader.getResourceAsStream(fileName)
    model.read(inputStream, null, FileUtils.langXML)
    model
  }

}
