export class BlockType {

  /**
   * SHARED
    */
  static DATASET = new BlockType('Dataset', 'dataset');

  /**
   * IMPORT DATA
   */
  static DATABASE = new BlockType('Database', 'database');
  static FILE = new BlockType('File', 'file');

  /**
   * MACHINE LEARNING
   */

  /**
   * REGRESSION
   */
  static LINEAR_REGRESSION = new BlockType('Linear Regression', 'linearRegression');
  static POISSON_REGRESSION = new BlockType('Poisson Regression', 'poissonRegression');

  constructor (public name: string, public code: string) {}

}
