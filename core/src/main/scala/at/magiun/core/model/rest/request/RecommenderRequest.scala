package at.magiun.core.model.rest.request

import at.magiun.core.model.data.Distribution

case class RecommenderRequest(datasetId: Int,
                              goal: String,
                              tradeOff: String,
                              responseVariable: Int,
                              explanatoryVariables: Seq[Int],
                              responseVariableDistribution: Distribution,
                              explanatoryVariablesDistributions: Seq[Distribution]
                             ) {

}
