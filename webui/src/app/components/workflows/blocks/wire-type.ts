export class WireType {

  static DATASET = new WireType('DATASET');
  static REGRESSION_MODEL = new WireType('REGRESSION_MODEL');

  private constructor(public i18nValue: string) {
  }

}
