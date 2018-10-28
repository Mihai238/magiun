package at.magiun.core.statistics

import at.magiun.core.config.AlgorithmOntologyConfig
import at.magiun.core.model.data.{DatasetMetadata, Distribution}
import at.magiun.core.model.ontology.{OntologyClass, OntologyProperty}
import org.apache.jena.ontology.{DatatypeProperty, Individual, ObjectProperty, OntModel}
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConversions._

class AlgorithmRecommender {

  def recommend(spark: SparkSession, ontology: OntModel, datasetMetadata: DatasetMetadata): Set[OntologyClass.Value] = {
    val dataset: Individual = createIndividualForOntClass(ontology, OntologyClass.Dataset.toString)
    val algorithm: Individual = createIndividualForOntClass(ontology, OntologyClass.Algorithm.toString)
    val responseVariableDistribution: Individual = createDistributionIndividual(ontology, datasetMetadata.variableDistributions(datasetMetadata.responseVariableIndex))
    val responseVariableType: Individual = createIndividualForOntClass(ontology, OntologyClass.Continuous.toString)

    val hasDataset: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasDataset)
    val hasVariables: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasVariables)
    val hasObservations: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasObservations)
    val hasResponseVariableDistribution: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasResponseVariableDistribution)
    val hasResponseVariableType: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasResponseVariableType)

    algorithm.addProperty(hasDataset, dataset)
    dataset.addLiteral(hasVariables, datasetMetadata.variablesCount - 1)
    dataset.addLiteral(hasObservations, datasetMetadata.observationsCount)
    dataset.addProperty(hasResponseVariableDistribution, responseVariableDistribution)
    dataset.addProperty(hasResponseVariableType, responseVariableType)

    val rdfTypes = asScalaSet(algorithm.listRDFTypes(false).toSet)
      .filter(p => p.getNameSpace.equals(AlgorithmOntologyConfig.NS))

    val ontClasses = rdfTypes.map(a => OntologyClass.withName(a.getURI)).toSet

    for (i <- ontology.listIndividuals()) {
      ontology.removeAll(i.asResource(), null, null)
    }

    ontClasses
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

  private def getObjectProperty(ontology: OntModel, property: OntologyProperty.Value): ObjectProperty = {
    ontology.getObjectProperty(property.toString)
  }

  private def getDataProperty(ontology: OntModel, property: OntologyProperty.Value): DatatypeProperty = {
    ontology.getDatatypeProperty(property.toString)
  }
}
