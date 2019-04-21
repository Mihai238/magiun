import {CoefficientResponse} from "./coefficient.response.model";

export interface TrainAlgorithmResponse {
  intercept: CoefficientResponse
  coefficients: CoefficientResponse[]
  degreesOfFreedom: number
  explainedVariance: number
  meanAbsoluteError: number
  meanSquaredError: number
  rSquared: number
  rSquaredAdjusted: number
  rootMeanSquaredError: number
  errorMessage: string
}
