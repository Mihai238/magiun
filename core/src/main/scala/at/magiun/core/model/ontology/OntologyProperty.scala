package at.magiun.core.model.ontology

import at.magiun.core.config.AlgorithmOntologyConfig

object OntologyProperty extends Enumeration {

  /** Object properties */
  val hasDataset: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasDataset")
  val hasResponseVariableDistribution: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasResponseVariableDistribution")
  val hasResponseVariableType: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasResponseVariableType")
  val isDatasetOf: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "isDatasetOf")

  /** Data properties */
  val hasObservations: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasObservations")
  val hasVariables: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasVariables")
  val hasNormalDistributionPercentage: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasNormalDistributionPercentage")
  val hasContinuousVariableTypePercentage: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasContinuousVariableTypePercentage")
}
