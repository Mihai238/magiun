package at.magiun.core.model.rest.response

import at.magiun.core.model.rest.AlgorithmImplementation

case class TrainAlgorithmResponse(
                                   id: String,
                                   algorithmImplementation: AlgorithmImplementation,
                                   intercept: CoefficientResponse = CoefficientResponse(),
                                   classificationIntercept: Seq[ClassificationInterceptResponse] = Seq.empty,
                                   coefficients: Seq[CoefficientResponse] = Seq.empty,
                                   degreesOfFreedom: Long = -1,
                                   explainedVariance: Double = -1,
                                   meanAbsoluteError: Double = -1,
                                   meanSquaredError: Double = -1,
                                   rSquared: Double = -1,
                                   rSquaredAdjusted: Double = -1,
                                   rootMeanSquaredError: Double = -1,
                                   aic: Double = -1,
                                   deviance: Double = -1,
                                   nullDeviance: Double = -1,
                                   dispersion: Double = -1,
                                   residualDegreeOfFreedom: Double = -1,
                                   residualDegreeOfFreedomNull: Double = -1,
                                   rank: Double = -1,
                                   accuracy: Double = -1,
                                   falsePositiveRate: Double = -1,
                                   truePositiveRate: Double = -1,
                                   fMeasure: Double = -1,
                                   precision: Double = -1,
                                   recall: Double = -1,
                                   fittedValues: Seq[Double] = Seq.empty,
                                   residuals: Seq[Double] = Seq.empty,
                                   dataSample: Seq[Double] = Seq.empty,
                                   treeDebugString: String = "",
                                   errorMessage: String = ""
                                 ) {


def this(errorMessage: String) {
    this(id = "", algorithmImplementation = AlgorithmImplementation.None, errorMessage = errorMessage)
  }
}

case class CoefficientResponse(name: String = "", value: Double = -1, standardError: Double = -1, tValue: Double = -1, pValue: Double = -1)

case class ClassificationInterceptResponse(name: String = "", falsePositiveRate: Double = -1, truePositiveRate: Double = -1, precision: Double = -1, recall: Double = -1, fMeasure: Double = -1)
