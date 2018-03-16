export class BlockType {

  /**
   * IMPORT DATA
   */
  static DATABASE = new BlockType('Database', 'DATABASE');
  static FILE = new BlockType('File', 'FILE');

  /**
   * DATA TRANSFORMATION
   */
  static SPLIT_DATA = new BlockType('Split Data', 'SPLIT_DATA');

  /**
   * MACHINE LEARNING
   */

  /**
   * REGRESSION
   */
  static LINEAR_REGRESSION = new BlockType('Linear Regression', 'LINEAR_REGRESSION');
  static POISSON_REGRESSION = new BlockType('Poisson Regression', 'POISSON_REGRESSION');

  private constructor (public name: string, public value: string) {}

}
