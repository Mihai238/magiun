package at.magiun.core.model.rest.response

import at.magiun.core.model.rest.AlgorithmImplementation

case class TrainAlgorithmResponse(
                                   id: String,
                                   algorithmImplementation: AlgorithmImplementation,
                                   intercept: CoefficientResponse,
                                   coefficients: Seq[CoefficientResponse],
                                   degreesOfFreedom: Long,
                                   explainedVariance: Double,
                                   meanAbsoluteError: Double,
                                   meanSquaredError: Double,
                                   rSquared: Double,
                                   rSquaredAdjusted: Double,
                                   rootMeanSquaredError: Double,
                                   fittedValues: Seq[Double],
                                   residuals: Seq[Double],
                                   dataSample: Seq[Double],
                                   errorMessage: String = ""
                                 ) {


def this(errorMessage: String) {
    this("", AlgorithmImplementation.None, CoefficientResponse("", -1, -1, -1, -1), Seq.empty, -1, -1, -1, -1, -1, -1, -1, Seq.empty, Seq.empty, Seq.empty, errorMessage)
  }
}

case class CoefficientResponse(name: String, value: Double, standardError: Double, tValue: Double, pValue: Double)
