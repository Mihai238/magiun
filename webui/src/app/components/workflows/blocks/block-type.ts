export class BlockType {

  /**
   * SHARED
    */
  static DATASET = new BlockType('Dataset', 'dataset', 'DATASET');
  static REGRESSION_MODEL = new BlockType('Regression Model', 'regressionModel', 'REGRESSION_MODEL');

  /**
   * IMPORT DATA
   */
  static DATABASE = new BlockType('Database', 'database', 'DATABASE');
  static FILE = new BlockType('File', 'file', 'FILE');

  /**
   * DATA TRANSFORMATION
   */
  static SPLIT_DATA = new BlockType('Split Data', 'splitData', 'SPLIT_DATA');

  /**
   * MACHINE LEARNING
   */

  /**
   * REGRESSION
   */
  static LINEAR_REGRESSION = new BlockType('Linear Regression', 'linearRegression', 'LINEAR_REGRESSION');
  static POISSON_REGRESSION = new BlockType('Poisson Regression', 'poissonRegression', 'POISSON_REGRESSION');

  private constructor (public name: string, public type: string, public value: string) {}

}
