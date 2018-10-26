package at.magiun.core.feature

import openllet.jena.PelletReasonerFactory
import org.apache.jena.ontology.{Individual, OntModel}
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.reasoner.ReasonerRegistry
import org.apache.jena.util.FileUtils.langTurtle

import scala.collection.JavaConversions._

object ReasonerRecommender {

  val NS = "http://www.semanticweb.org/mihai/ontologies/2018/9/ml#"

  def main(args: Array[String]): Unit = {
    val model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC)
    val is = this.getClass.getClassLoader.getResourceAsStream("data_reasoner.ttl")
    model.read(is, null, langTurtle)

//    val gc = model.createIndividual(model.getOntClass(NS + "GenderColumn"))

//    taset: Individual = createIndividualForOntClass(ontology, OntologyClass.Dataset.toString)
//    val responseVariable: Individual = createIndividualForOntClass(ontology, OntologyClass.ResponseVariable.toString)
//    val regression: Individual = createIndividualForOntClass(ontology, OntologyClass.Regression.toString)
//    val responseVariableDistribution: Individual = createDistributionIndividual(ontology, datasetMetadata.variableDistributions(datasetMetadata.responseVariableIndex))
//    val responseVariableType: Individual = createIndividualForOntClass(ontology, OntologyClass.Continuous.toString)
//
//    val hasDistribution: Property = getObjectProperty(ontology, OntologyObjectProperty.hasDistribution)
//    val hasVariableType: Property = getObjectProperty(ontology, OntologyObjectProperty.hasVariableType)
//    val hasResponseVariable: Property = getObjectProperty(ontology, OntologyObjectProperty.hasResponseVariable)
//    val hasDataset: Property = getObjectProperty(ontology, OntologyObjectProperty.hasDataset)
//
//    responseVariable.addProperty(hasDistribution, responseVariableDistribution)
//    responseVariable.addProperty(hasVariableType, responseVariableType)
//    dataset.addProperty(hasResponseVariable, responseVariable)
//    regression.addProperty(hasDataset, dataset)

    println()
    println("following individuals were created: " + model.listIndividuals().size)
    model.listIndividuals().toList.toList
      .foreach(indv => println(indv.getLocalName + " --- " + indv.getOntClass.getLocalName))

    val gcolIndv = model.listIndividuals().toList.toList.find(_.getLocalName == "gcol").get

//    val reasoner = ReasonerRegistry.getOWLReasoner
//    println()
//    val i = gc.listRDFTypes(true)
//    while ( {
//      i.hasNext
//    }) println(regression.getId.getLabelString + " is asserted in class " + i.next)

    println()
    if (model.validate().isValid) {
      println("model is valid!")
    }

    println()
    val statements = model.listStatements(gcolIndv.asResource(), null, null).toList.toList
      .filter(model.contains)
        .filter(_.getPredicate.getLocalName == "type")
    println(statements.size)
    statements.foreach(s => println(s.toString))
    println()

//    val deduction = model.getDeductionsModel
//    val deductionStatements = deduction.listStatements(gcolIndv.asResource(), null, null).toList.toList
//      .filter(model.contains)
//    println(deductionStatements.size)
//    deductionStatements.foreach(s => println(s.toString))
//    println()

    println()
    println("stop")

  }

  private def createIndividual(ontology: OntModel, ontClass: String): Individual = {
    ontology.createIndividual(ontology.getOntClass(ontClass))
  }

}
