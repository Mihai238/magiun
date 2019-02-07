package at.magiun.core.model.ontology

import at.magiun.core.config.AlgorithmOntologyConfig

object OntologyProperty extends Enumeration {

  /** Object properties */
  val hasDataset: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasDataset")
  val hasResponseVariableDistribution: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasResponseVariableDistribution")
  val hasResponseVariableType: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasResponseVariableType")
  val isDatasetOf: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "isDatasetOf")
  val hasGoal: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasGoal")

  /** Data properties */
  val hasNormalDistributionPercentage: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasNormalDistributionPercentage")
  val hasBernoulliDistributionPercentage: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasBernoulliDistributionPercentage")
  val hasMultinomialDistributionPercentage: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasMultinomialDistributionPercentage")
  val hasContinuousVariableTypePercentage: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasContinuousVariableTypePercentage")
  val hasDiscreteVariableTypePercentage: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasDiscreteVariableTypePercentage")
  val hasBinaryVariableTypePercentage: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasBinaryVariableTypePercentage")
  val hasObservationVariableRatio: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasObservationVariableRatio")
  val hasMulticollinearity: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasMulticollinearity")
  val hasOutlieres: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasOutliers")
  val hasIndependentObservations: OntologyProperty.Value = Value(AlgorithmOntologyConfig.NS + "hasIndependentObservations")
}