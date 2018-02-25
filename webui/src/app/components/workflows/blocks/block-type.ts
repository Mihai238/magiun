export class BlockType {

  /**
   * IMPORT DATA
   */
  static DATABASE = new BlockType('Database', 'database');

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
