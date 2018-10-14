package at.magiun.core.statistics

import at.magiun.core.model.data.{DatasetMetadata, Distribution}
import at.magiun.core.model.ontology.{OntologyClass, OntologyObjectProperty}
import org.apache.jena.ontology.{Individual, OntModel}
import org.apache.jena.rdf.model.Property
import org.apache.spark.sql.SparkSession

class AlgorithmRecommender {

  def recommend(spark: SparkSession, ontology: OntModel, datasetMetadata: DatasetMetadata): Unit = {

    val dataset: Individual = createIndividualForOntClass(ontology, OntologyClass.Dataset.toString)
    val responseVariable: Individual = createIndividualForOntClass(ontology, OntologyClass.ResponseVariable.toString)
    val responseVariableDistribution: Individual = createDistributionIndividual(ontology, datasetMetadata.variableDistributions(datasetMetadata.responseVariableIndex))

    val hasDistribution: Property = getObjectProperty(ontology, OntologyObjectProperty.hasDistribution)
    val hasResponseVariable: Property = getObjectProperty(ontology, OntologyObjectProperty.hasResponseVariable)

    dataset.addProperty(hasResponseVariable, responseVariable)
    responseVariable.addProperty(hasDistribution, responseVariableDistribution)
  }

  private def createDistributionIndividual(ontology: OntModel, distribution: Distribution): Individual = {
    distribution match {
      case Distribution.Normal => createIndividualForOntClass(ontology, OntologyClass.NormalDistribution.toString)
      case _ => throw new IllegalArgumentException(s"Unknown distribution ${distribution.toString} !")
    }
  }

  private def createIndividualForOntClass(ontology: OntModel, ontClass: String): Individual = {
    ontology.createIndividual(ontology.getOntClass(ontClass))
  }

  private def getObjectProperty(ontology: OntModel, property: OntologyObjectProperty.Value): Property = {
    ontology.getProperty(property.toString)
  }
}
