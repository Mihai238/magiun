import {Distribution} from "../statistics/distribution.type.model";

export class RecommenderRequest {

  constructor(
    public uid: string,
    public datasetId: number,
    public goal: string,
    public tradeOff: string,
    public responseVariable: number,
    public explanatoryVariables: number[],
    public responseVariableDistribution: Distribution,
    public explanatoryVariablesDistributions: Distribution[]
  ) {
  }
}
