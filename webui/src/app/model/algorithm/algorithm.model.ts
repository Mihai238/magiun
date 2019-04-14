import {AlgorithmParameter} from "./algorithm.parameter.model";

export interface Algorithm {

  readonly name: string;
  readonly formula: string;
  parameters: AlgorithmParameter<any>[];
}
