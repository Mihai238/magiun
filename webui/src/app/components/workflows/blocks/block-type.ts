export class BlockType {

  /**
   * IMPORT DATA
   */
  static DATABASE = new BlockType('DatabaseReader', 'DATABASE');
  static FILE = new BlockType('FileReader', 'FILE');

  /**
   * DATA TRANSFORMATION
   */
  static SPLIT_DATA = new BlockType('SplitData', 'SPLIT_DATA');

  /**
   * FEATURE SELECTION
   */
  static DROP_COLUMNS = new BlockType('DropColumn', 'DROP_COLUMNS');

  /**
   * MACHINE LEARNING
   */

  /**
   * REGRESSION
   */
  static LINEAR_REGRESSION = new BlockType('LinearRegression', 'LINEAR_REGRESSION');
  static POISSON_REGRESSION = new BlockType('PoissonRegression', 'POISSON_REGRESSION');

  private constructor (public name: string, public i18nValue: string) {}

}
