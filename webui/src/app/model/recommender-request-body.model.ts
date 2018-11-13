export class RecommenderRequestBody {

  datasetId: number;
  scope: string;
  tradeOff: string;
  variablesToIgnore: number[];

  constructor(datasetId: number, scope: string, tradeOff: string, variablesToIgnore: number[]) {
    this.datasetId = datasetId;
    this.scope = scope;
    this.tradeOff = tradeOff;
    this.variablesToIgnore = variablesToIgnore;
  }
}
