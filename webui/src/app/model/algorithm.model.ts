export class Algorithm {

  readonly id: string;
  readonly name: string;
  readonly formula: string;
  readonly parameters: Map<string, string>;

  constructor(name: string, formula: string, parameters: Map<string, string>) {
    this.id = name.replace(" ", "");
    this.name = name;
    this.formula = formula;
    this.parameters = parameters;
  }
}
