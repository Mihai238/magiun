package at.magiun.core.statistics

import at.magiun.core.config.{AlgorithmOntologyConfig, AlgorithmSelectionOntology}
import at.magiun.core.model.data.{DatasetMetadata, Distribution}
import at.magiun.core.model.ontology.{OntologyClass, OntologyProperty}
import com.softwaremill.tagging.@@
import org.apache.jena.ontology.{DatatypeProperty, Individual, ObjectProperty, OntModel}

import scala.collection.JavaConversions._

class AlgorithmRecommender(ontology: OntModel @@ AlgorithmSelectionOntology) {

  def recommend(metadata: DatasetMetadata): Set[OntologyClass] = {
    val dataset: Individual = createIndividualForOntClass(OntologyClass.Dataset.ontologyUri)
    val algorithm: Individual = createIndividualForOntClass(OntologyClass.Algorithm.ontologyUri)
    val goal: Individual = createIndividualForOntClass(metadata.goal.ontologyClass.ontologyUri)

    val hasGoal: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasGoal)
    val hasDataset: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasDataset)
    val hasNormalDistributionPercentage: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasNormalDistributionPercentage)
    val hasBernoulliDistributionPercentage: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasBernoulliDistributionPercentage)
    val hasMultinomialDistributionPercentage: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasMultinomialDistributionPercentage)
    val hasContinuousVariableTypePercentage: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasContinuousVariableTypePercentage)
    val hasBinaryVariableTypePercentage: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasBinaryVariableTypePercentage)
    val hasDiscreteVariableTypePercentage: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasDiscreteVariableTypePercentage)
    val hasObservationVariableRatio: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasObservationVariableRatio)
    val hasResponseVariableDistribution: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasResponseVariableDistribution)
    val hasResponseVariableType: ObjectProperty = getObjectProperty(ontology, OntologyProperty.hasResponseVariableType)
    val hasMulticollinearity: DatatypeProperty = getDataProperty(ontology, OntologyProperty.hasMulticollinearity)

    algorithm.addProperty(hasDataset, dataset)
    algorithm.addProperty(hasGoal, goal)
    dataset.addProperty(hasResponseVariableDistribution, createDistributionIndividual(metadata.responseVariableDistribution))
    dataset.addProperty(hasResponseVariableType, createIndividualForOntClass(OntologyClass.getOntologyClass(metadata.responseVariableType).ontologyUri))
    dataset.addLiteral(hasNormalDistributionPercentage, metadata.normalDistributionPercentage)
    dataset.addLiteral(hasBernoulliDistributionPercentage, metadata.bernoulliDistributionPercentage)
    dataset.addLiteral(hasMultinomialDistributionPercentage, metadata.multinomialDistributionPercentage)
    dataset.addLiteral(hasContinuousVariableTypePercentage, metadata.continuousVariableTypePercentage)
    dataset.addLiteral(hasBinaryVariableTypePercentage, metadata.binaryVariableTypePercentage)
    dataset.addLiteral(hasDiscreteVariableTypePercentage, metadata.discreteVariableTypePercentage)
    dataset.addLiteral(hasObservationVariableRatio, metadata.observationVariableRatio)
    dataset.addLiteral(hasMulticollinearity, metadata.multicollinearity)

    val rdfTypes = asScalaSet(algorithm.listRDFTypes(false).toSet)
      .filter(p => p.getNameSpace.equals(AlgorithmOntologyConfig.NS))

    val ontClasses = rdfTypes.map(a => OntologyClass.withName(a.getLocalName)).toSet

    for (i <- ontology.listIndividuals()) {
      ontology.removeAll(i.asResource(), null, null)
    }

    ontClasses
  }

  private def createDistributionIndividual(distribution: Distribution): Individual = {
    distribution match {
      case Distribution.Normal => createIndividualForOntClass(OntologyClass.NormalDistribution.ontologyUri)
      case Distribution.Binomial => createIndividualForOntClass(OntologyClass.BinomialDistribution.ontologyUri)
      case Distribution.Exponential => createIndividualForOntClass(OntologyClass.ExponentialDistribution.ontologyUri)
      case Distribution.Bernoulli => createIndividualForOntClass(OntologyClass.BernoulliDistribution.ontologyUri)
      case Distribution.Gamma => createIndividualForOntClass(OntologyClass.GammaDistribution.ontologyUri)
      case Distribution.Exponential => createIndividualForOntClass(OntologyClass.ExponentialDistribution.ontologyUri)
      case Distribution.Multinomial => createIndividualForOntClass(OntologyClass.MultinomialDistribution.ontologyUri)
      case Distribution.Poisson => createIndividualForOntClass(OntologyClass.PoissonDistribution.ontologyUri)
      case Distribution.Uniform => createIndividualForOntClass(OntologyClass.UniformDistribution.ontologyUri)
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
