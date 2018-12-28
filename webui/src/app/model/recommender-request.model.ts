export class RecommenderRequest {

  constructor(
    public datasetId: number,
    public scope: string,
    public tradeOff: string,
    public responseVariable: number,
    public variablesToIgnore: number[]
  ) {
  }
}
