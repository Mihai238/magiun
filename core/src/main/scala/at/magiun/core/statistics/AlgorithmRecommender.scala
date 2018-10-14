package at.magiun.core.statistics

import at.magiun.core.model.data.DatasetMetadata
import at.magiun.core.model.ontology.OntologyClass._
import at.magiun.core.model.ontology.OntologyObjectProperties._
import org.apache.jena.ontology.{Individual, OntModel}
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.spark.sql.SparkSession

class AlgorithmRecommender {

  def recommend(spark: SparkSession, ontology: OntModel, datasetMetadata: DatasetMetadata): Unit = {

    val model: Model = ModelFactory.createDefaultModel()

    val dataset: Individual = ontology.createIndividual(ontology.getOntClass(Dataset.toString))
    val responseVariable: Individual = ontology.createIndividual(ontology.getOntClass(ResponseVariable.toString))
    val distribution: Individual = ontology.createIndividual(ontology.getOntClass(NormalDistribution.toString))

    val hasDistributionProperty = ontology.getProperty(hasDistribution.toString)
    val hasResponseVariableProperty = ontology.getProperty(hasResponseVariable.toString)

    dataset.addProperty(hasResponseVariableProperty, responseVariable)

  }
}
