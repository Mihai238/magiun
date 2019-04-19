package at.magiun.core.model.request

case class TrainAlgorithmRequest(datasetId: Int, responseVariable: Int, explanatoryVariables: Seq[Int], algorithm: AlgorithmRequest) extends Serializable {}

case class AlgorithmRequest(name: String, implementation: String, parameters: Set[AlgorithmParameterRequest]) {}

case class AlgorithmParameterRequest(name: String, value: String) {}