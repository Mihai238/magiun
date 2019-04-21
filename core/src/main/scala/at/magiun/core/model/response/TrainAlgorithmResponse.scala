package at.magiun.core.model.response

case class TrainAlgorithmResponse(
                                   id: String,
                                   intercept: CoefficientResponse,
                                   coefficients: Set[CoefficientResponse],
                                   degreesOfFreedom: Long,
                                   explainedVariance: Double,
                                   meanAbsoluteError: Double,
                                   meanSquaredError: Double,
                                   rSquared: Double,
                                   rSquaredAdjusted: Double,
                                   rootMeanSquaredError: Double,
                                   errorMessage: String = ""
                                 ) {
  def this(errorMessage: String) {
    this("", CoefficientResponse("", -1, -1, -1, -1), Set.empty, -1, -1, -1, -1, -1, -1, -1, errorMessage)
  }
}

case class CoefficientResponse(name: String, value: Double, standardError: Double, tValue: Double, pValue: Double)
