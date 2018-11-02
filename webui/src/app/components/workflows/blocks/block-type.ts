export class BlockType {

  /**
   * IMPORT DATA
   */
  static DATABASE = new BlockType('DatabaseReader', 'DATABASE');
  static FILE = new BlockType('FileReader', 'FILE');
  static DATA_SET_READER = new BlockType('DataSetReader', 'DataSetReader');

  /**
   * DATA TRANSFORMATION
   */
  static SPLIT_DATA = new BlockType('SplitData', 'SPLIT_DATA');

  /**
   * FEATURE SELECTION
   */
  static DROP_COLUMNS = new BlockType('DropColumn', 'DROP_COLUMNS');
  static ADD_COLUMN = new BlockType('AddColumn', "ADD_COLUMN");

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
