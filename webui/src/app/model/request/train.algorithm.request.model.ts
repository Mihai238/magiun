export class TrainAlgorithmRequest {
  constructor(
    public datasetId: number,
    public responseVariable: number,
    public explanatoryVariables: number[],
    public algorithm: Algorithm
  ) {}
}
