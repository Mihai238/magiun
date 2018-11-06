package at.magiun.core.feature

import java.net.URI
import java.util.UUID

import openllet.jena.PelletReasonerFactory
import org.apache.jena.ontology.{Individual, OntModel}
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils.langTurtle

import scala.collection.JavaConversions._

object ReasonerRecommender {

  val NS = "http://www.magiun.io/ontologies/ml#"

  def main(args: Array[String]): Unit = {
    val model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC)
    val is = this.getClass.getClassLoader.getResourceAsStream("data_reasoner.ttl")
    model.read(is, null, langTurtle)

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

    val gcolIndv = model.createIndividual(NS + "col1", model.getOntClass(NS + "Column"))
    val femaleIndv = model.createIndividual(model.getOntClass(NS + "FemaleValue"))
    val maleIndv = model.createIndividual(model.getOntClass(NS + "MaleValue"))
    gcolIndv.addProperty(model.getProperty(NS + "hasValue"), femaleIndv)
    gcolIndv.addProperty(model.getProperty(NS + "hasValue"), maleIndv)
    gcolIndv.addProperty(model.getProperty(NS + "cardinality"), model.createTypedLiteral(2.asInstanceOf[Integer]))

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
