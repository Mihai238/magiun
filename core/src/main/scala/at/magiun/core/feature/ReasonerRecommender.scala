package at.magiun.core.feature

import java.net.URI
import java.util.UUID

import openllet.jena.PelletReasonerFactory
import org.apache.jena.ontology.{Individual, OntModel}
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.FileUtils.langTurtle
import org.topbraid.jenax.util.ARQFactory
import org.topbraid.shacl.arq.SHACLFunctions
import org.topbraid.shacl.engine.ShapesGraph
import org.topbraid.shacl.util.ModelPrinter
import org.topbraid.shacl.validation.ValidationEngineFactory

import scala.collection.JavaConversions._

object ReasonerRecommender {

  val NS = "http://www.semanticweb.org/mihai/ontologies/2018/9/ml#"

  def main(args: Array[String]): Unit = {
    val model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC)
    val is = this.getClass.getClassLoader.getResourceAsStream("data_reasoner.ttl")
    model.read(is, null, langTurtle)

    // Make sure all sh:Functions are registered
    SHACLFunctions.registerFunctions(model)

    // Create Dataset that contains both the data model and the shapes model
    // (here, using a temporary URI for the shapes graph)
    val shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID.toString)
    val dataset = ARQFactory.get.getDataset(model)
    dataset.addNamedModel(shapesGraphURI.toString, model)

    val shapesGraph = new ShapesGraph(model)
    val engine = ValidationEngineFactory.get.create(dataset, shapesGraphURI, shapesGraph, null)

    val report = engine.validateAll()
    println(ModelPrinter.get.print(report.getModel))

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

    val gcolIndv = model.listIndividuals().toList.toList.find(_.getLocalName == "genderColumn").get

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
