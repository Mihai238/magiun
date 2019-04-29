import {CoefficientResponse} from "./coefficient.response.model";
import {AlgorithmImplementation} from "../algorithm/train/algorithm.implementation.model";
import {ClassificationInterceptResponse} from "./classification.intercept.response.model";

export interface TrainAlgorithmResponse {
  id: string,
  algorithmImplementation: AlgorithmImplementation,
  intercept: CoefficientResponse
  classificationIntercept: ClassificationInterceptResponse[]
  coefficients: CoefficientResponse[]
  degreesOfFreedom: number
  explainedVariance: number
  meanAbsoluteError: number
  meanSquaredError: number
  rSquared: number
  rSquaredAdjusted: number
  rootMeanSquaredError: number
  aic: number
  deviance: number
  nullDeviance: number
  dispersion: number
  residualDegreeOfFreedom: number
  residualDegreeOfFreedomNull: number
  rank: number
  accuracy: number
  falsePositiveRate: number
  truePositiveRate: number
  fMeasure: number
  precision: number
  recal: number
  fittedValues: number[]
  residuals: number[]
  dataSample: number[]
  errorMessage: string
  treeDebugString: string
}
