package at.magiun.core.statistics

import at.magiun.core.config.{AlgorithmOntologyConfig, AlgorithmSelectionOntology}
import at.magiun.core.model.data.{DatasetMetadata, Distribution}
import at.magiun.core.model.ontology.{OntologyClass, OntologyProperty}
import com.softwaremill.tagging.@@
import org.apache.jena.ontology.{DatatypeProperty, Individual, ObjectProperty, OntModel}

import scala.collection.JavaConversions._

class AlgorithmRecommender(ontology: OntModel @@ AlgorithmSelectionOntology) {

  def recommend(metadata: DatasetMetadata): Set[OntologyClass.Value] = {
    val dataset: Individual = createIndividualForOntClass(OntologyClass.Dataset.toString)
    val algorithm: Individual = createIndividualForOntClass(OntologyClass.Algorithm.toString)

    val hasContinuousVariableTypePercentage: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasContinuousVariableTypePercentage)
    val hasDataset: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasDataset)
    val hasNormalDistributionPercentage: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasNormalDistributionPercentage)
    val hasObservations: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasObservations)
    val hasObservationVariableRatio: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasObservationVariableRatio)
    val hasResponseVariableDistribution: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasResponseVariableDistribution)
    val hasResponseVariableType: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasResponseVariableType)
    val hasVariables: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasVariables)

    algorithm.addProperty(hasDataset, dataset)
    dataset.addLiteral(hasVariables, metadata.variablesCount - 1)
    dataset.addLiteral(hasObservations, metadata.observationsCount)
    dataset.addProperty(hasResponseVariableDistribution, createDistributionIndividual(metadata.responseVariableDistribution))
    dataset.addProperty(hasResponseVariableType, createIndividualForOntClass(OntologyClass.getOntologyClass(metadata.responseVariableType).toString))
    dataset.addLiteral(hasNormalDistributionPercentage, metadata.normalDistributionPercentage)
    dataset.addLiteral(hasContinuousVariableTypePercentage, metadata.continuousVariableTypePercentage)
    dataset.addLiteral(hasObservationVariableRatio, metadata.observationVariableRatio)

    val rdfTypes = asScalaSet(algorithm.listRDFTypes(false).toSet)
      .filter(p => p.getNameSpace.equals(AlgorithmOntologyConfig.NS))

    val ontClasses = rdfTypes.map(a => OntologyClass.withName(a.getURI)).toSet

    for (i <- ontology.listIndividuals()) {
      ontology.removeAll(i.asResource(), null, null)
    }

    ontClasses
  }

  private def createDistributionIndividual(distribution: Distribution): Individual = {
    distribution match {
      case Distribution.Normal => createIndividualForOntClass(OntologyClass.NormalDistribution.toString)
      case Distribution.Binomial => createIndividualForOntClass(OntologyClass.BinomialDistribution.toString)
      case Distribution.Exponential => createIndividualForOntClass(OntologyClass.ExponentialDistribution.toString)
      case _ => throw new IllegalArgumentException(s"Unknown distribution ${distribution.toString} !")
    }
  }

  private def createIndividualForOntClass(ontClass: String): Individual = {
    ontology.createIndividual(ontology.getOntClass(ontClass))
  }

  private def getObjectProperty(ontology: OntModel, property: OntologyProperty.Value): ObjectProperty = {
    ontology.getObjectProperty(property.toString)
  }

  private def getDataProperty(ontology: OntModel, property: OntologyProperty.Value): DatatypeProperty = {
    ontology.getDatatypeProperty(property.toString)
  }
}
