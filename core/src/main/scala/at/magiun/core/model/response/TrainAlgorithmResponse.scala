package at.magiun.core.model.response

import at.magiun.core.model.DataRow

case class TrainAlgorithmResponse(
                                   id: String,
                                   intercept: CoefficientResponse,
                                   coefficients: Seq[CoefficientResponse],
                                   degreesOfFreedom: Long,
                                   explainedVariance: Double,
                                   meanAbsoluteError: Double,
                                   meanSquaredError: Double,
                                   rSquared: Double,
                                   rSquaredAdjusted: Double,
                                   rootMeanSquaredError: Double,
                                   predictions: Seq[Double],
                                   residuals: Seq[Double],
                                   errorMessage: String = ""
                                 ) {

def this(errorMessage: String) {
    this("", CoefficientResponse("", -1, -1, -1, -1), Seq.empty, -1, -1, -1, -1, -1, -1, -1, Seq.empty, Seq.empty, errorMessage)
  }
}

case class CoefficientResponse(name: String, value: Double, standardError: Double, tValue: Double, pValue: Double)
