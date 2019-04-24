import {CoefficientResponse} from "./coefficient.response.model";
import {AlgorithmImplementation} from "../algorithm/train/algorithm.implementation.model";

export interface TrainAlgorithmResponse {
  id: string,
  algorithmImplementation: AlgorithmImplementation,
  intercept: CoefficientResponse
  coefficients: CoefficientResponse[]
  degreesOfFreedom: number
  explainedVariance: number
  meanAbsoluteError: number
  meanSquaredError: number
  rSquared: number
  rSquaredAdjusted: number
  rootMeanSquaredError: number
  fittedValues: number[]
  residuals: number[]
  dataSample: number[]
  errorMessage: string
}
