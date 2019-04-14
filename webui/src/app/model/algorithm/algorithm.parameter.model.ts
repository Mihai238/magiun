export interface AlgorithmParameter<T> {

  readonly name: string;
  value: T;
  readonly selectOptions: T[];
}
