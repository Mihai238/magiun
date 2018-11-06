package at.magiun.core.statistics

import at.magiun.core.config.{AlgorithmOntologyConfig, AlgorithmSelectionOntology}
import at.magiun.core.model.data.{DatasetMetadata, Distribution, VariableType}
import at.magiun.core.model.ontology.{OntologyClass, OntologyProperty}
import com.softwaremill.tagging.@@
import org.apache.jena.ontology.{DatatypeProperty, Individual, ObjectProperty, OntModel}
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConversions._

class AlgorithmRecommender {

  def recommend(spark: SparkSession, ontology: OntModel @@ AlgorithmSelectionOntology, metadata: DatasetMetadata): Set[OntologyClass.Value] = {
    val dataset: Individual = createIndividualForOntClass(ontology, OntologyClass.Dataset.toString)
    val algorithm: Individual = createIndividualForOntClass(ontology, OntologyClass.Algorithm.toString)
    val responseVariableDistribution: Individual = createDistributionIndividual(ontology, metadata.variableDistributions(metadata.responseVariableIndex))
    val responseVariableType: Individual = createIndividualForOntClass(ontology, OntologyClass.Continuous.toString)

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
    dataset.addProperty(hasResponseVariableDistribution, responseVariableDistribution)
    dataset.addProperty(hasResponseVariableType, responseVariableType)
    dataset.addLiteral(hasNormalDistributionPercentage, getNormalDistributionPercentage(metadata))
    dataset.addLiteral(hasContinuousVariableTypePercentage, getContinuousVariableTypePercentage(metadata))
    dataset.addLiteral(hasObservationVariableRatio, getObservationVariableRatio(metadata))

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

  private def getNormalDistributionPercentage(metadata: DatasetMetadata): java.lang.Double = {
    val distributions = metadata.variableDistributions.zipWithIndex
      .filter(d => d._2 != metadata.responseVariableIndex && !metadata.variablesToIgnoreIndex.contains(d._2))
      .map(_._1)

    1.0 * distributions.count(_.equals(Distribution.Normal))/distributions.size
  }

  private def getContinuousVariableTypePercentage(metadata: DatasetMetadata): java.lang.Double = {
    val variableTypes = metadata.variableTypes.zipWithIndex
      .filter(d => d._2 != metadata.responseVariableIndex && !metadata.variablesToIgnoreIndex.contains(d._2))
      .map(_._1)

    1.0 * variableTypes.count(_.equals(VariableType.Continuous))/variableTypes.size
  }

  private def getObservationVariableRatio(metadata: DatasetMetadata): java.lang.Double = {
    val explanatoryVarCount = metadata.variablesCount - metadata.variablesToIgnoreIndex.size - 1

    1.0 * metadata.observationsCount/explanatoryVarCount
  }
}
