package at.magiun.core.model.rest.request

import at.magiun.core.model.rest.AlgorithmImplementation

case class TrainAlgorithmRequest(datasetId: Int, responseVariable: Int, explanatoryVariables: Seq[Int], algorithm: AlgorithmRequest) extends Serializable {}

case class AlgorithmRequest(name: String, implementation: AlgorithmImplementation, parameters: Set[AlgorithmParameterRequest]) {}

case class AlgorithmParameterRequest(name: String, value: String) {}
