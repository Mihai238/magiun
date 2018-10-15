package at.magiun.core.statistics

import java.util

import at.magiun.core.model.data.{DatasetMetadata, Distribution}
import at.magiun.core.model.ontology.{OntologyClass, OntologyObjectProperty}
import org.apache.jena.ontology.{Individual, OntModel}
import org.apache.jena.rdf.model.{ModelFactory, Property, Selector, SimpleSelector}
import org.apache.spark.sql.SparkSession

class AlgorithmRecommender {

  def recommend(spark: SparkSession, ontology: OntModel, datasetMetadata: DatasetMetadata): Unit = {
    val dataset: Individual = createIndividualForOntClass(ontology, OntologyClass.Dataset.toString)
    val responseVariable: Individual = createIndividualForOntClass(ontology, OntologyClass.ResponseVariable.toString)
    val regression: Individual = createIndividualForOntClass(ontology, OntologyClass.Regression.toString)
    val responseVariableDistribution: Individual = createDistributionIndividual(ontology, datasetMetadata.variableDistributions(datasetMetadata.responseVariableIndex))
    val responseVariableType: Individual = createIndividualForOntClass(ontology, OntologyClass.Continuous.toString)

    val hasDistribution: Property = getObjectProperty(ontology, OntologyObjectProperty.hasDistribution)
    val hasVariableType: Property = getObjectProperty(ontology, OntologyObjectProperty.hasVariableType)
    val hasResponseVariable: Property = getObjectProperty(ontology, OntologyObjectProperty.hasResponseVariable)
    val hasDataset: Property = getObjectProperty(ontology, OntologyObjectProperty.hasDataset)

    responseVariable.addProperty(hasDistribution, responseVariableDistribution)
    responseVariable.addProperty(hasVariableType, responseVariableType)
    dataset.addProperty(hasResponseVariable, responseVariable)
    regression.addProperty(hasDataset, dataset)

    println()
    println("following individuals were created:")
    val individuals: util.Iterator[Individual] = ontology.listIndividuals().toList.iterator()
    while ( {
      individuals.hasNext
    }) println(individuals.next.getOntClass.getLocalName)

    val reasoner = ontology.getReasoner
    val infModel = ModelFactory.createInfModel(reasoner, ontology.getBaseModel)
    val graph = infModel.getGraph
    val deduction = infModel.getDeductionsModel


    println()
    val i = regression.listRDFTypes(true)
    while ( {
      i.hasNext
    }) println(regression.getId.getLabelString + " is asserted in class " + i.next)

    println()
    if (ontology.validate().isValid) {
      println("model is valid!")
    }

    println()
    println("stop")
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
