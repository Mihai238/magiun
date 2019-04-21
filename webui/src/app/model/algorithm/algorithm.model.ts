import {AlgorithmParameter} from "./algorithm.parameter.model";

export interface Algorithm {

  readonly name: string;
  implementation: string;
  parameters: AlgorithmParameter<any>[];
}
