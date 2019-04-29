export interface ClassificationInterceptResponse {
  name: string
  falsePositiveRate: number
  truePositiveRate: number
  precision: number
  recall: number
  fMeasure: number
}
