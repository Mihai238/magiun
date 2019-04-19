package at.magiun.core.model.request

import at.magiun.core.model.algorithm.Algorithm

case class TrainAlgorithmRequest(datasetId: Int, responseVariable: Int, explanatoryVariables: Seq[Int], algorithm: Algorithm[_ <: Any]) extends Serializable {}
