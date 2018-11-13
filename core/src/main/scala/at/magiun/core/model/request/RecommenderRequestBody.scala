package at.magiun.core.model.request

case class RecommenderRequestBody(datasetId: Int, scope: String, tradeOff: String, responseVariable: Int, variablesToIgnore: Seq[Int]) {

}
