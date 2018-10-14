package at.magiun.core.model.ontology

import at.magiun.core.config.AlgorithmOntologyConfig

object OntologyObjectProperty extends Enumeration {

  val hasDataset: OntologyObjectProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasDataset")
  val hasDistribution: OntologyObjectProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasDistribution")
  val hasObservation: OntologyObjectProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasObservation")
  val hasOutlier: OntologyObjectProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasOutlier")
  val hasParameter: OntologyObjectProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasParameter")
  val hasResidualDistribution: OntologyObjectProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasResidualDistribution")
  val hasResponseVariable: OntologyObjectProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasResponseVariable")
  val hasVariableType: OntologyObjectProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasVariableType")
  val isDistributionOf: OntologyObjectProperty.Value = Value(AlgorithmOntologyConfig.NS + "isDistributionOf")
}
