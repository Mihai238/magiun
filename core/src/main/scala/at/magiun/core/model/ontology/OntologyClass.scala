package at.magiun.core.model.ontology

import at.magiun.core.config.AlgorithmOntologyConfig

object OntologyClass extends Enumeration {

  /** Dataset stuff */
  val Dataset: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Dataset")
  val Variable: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Variable")
  val ResponseVariable: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Response_Variable")
  val Observation: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Observation")

  /** Distribution */
  val Distribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Distribution")
  val BernoulliDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Bernoulli_Distribution")
  val BinomialDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Binomial_Distribution")
  val NormalDistribution: OntologyClass.Value = Value(AlgorithmOntologyConfig.NS + "Normal_Distribution")

}
