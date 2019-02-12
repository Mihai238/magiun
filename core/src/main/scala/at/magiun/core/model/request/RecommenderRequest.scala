package at.magiun.core.model.request

case class RecommenderRequest(datasetId: Int, goal: String, tradeOff: String, responseVariable: Int, variablesToIgnore: Seq[Int]) {

}
