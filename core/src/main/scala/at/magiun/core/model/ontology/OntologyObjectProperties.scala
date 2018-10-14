package at.magiun.core.model.ontology

import at.magiun.core.config.AlgorithmOntologyConfig

object OntologyObjectProperties extends Enumeration {

  val hasDataset: OntologyObjectProperties.Value = Value(AlgorithmOntologyConfig.NS + "hasDataset")
  val hasDistribution: OntologyObjectProperties.Value = Value(AlgorithmOntologyConfig.NS + "hasDistribution")
  val hasObservation: OntologyObjectProperties.Value = Value(AlgorithmOntologyConfig.NS + "hasObservation")
  val hasOutlier: OntologyObjectProperties.Value = Value(AlgorithmOntologyConfig.NS + "hasOutlier")
  val hasParameter: OntologyObjectProperties.Value = Value(AlgorithmOntologyConfig.NS + "hasParameter")
  val hasResidualDistribution: OntologyObjectProperties.Value = Value(AlgorithmOntologyConfig.NS + "hasResidualDistribution")
  val hasResponseVariable: OntologyObjectProperties.Value = Value(AlgorithmOntologyConfig.NS + "hasResponseVariable")
  val hasVariableType: OntologyObjectProperties.Value = Value(AlgorithmOntologyConfig.NS + "hasVariableType")
  val isDistributionOf: OntologyObjectProperties.Value = Value(AlgorithmOntologyConfig.NS + "isDistributionOf")
}
