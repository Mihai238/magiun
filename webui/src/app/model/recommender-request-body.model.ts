export class RecommenderRequestBody {

  constructor(
    public datasetId: number,
    public scope: string,
    public tradeOff: string,
    public responseVariable: number,
    public variablesToIgnore: number[]
  ) {
  }
}
