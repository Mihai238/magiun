import {Type} from '@angular/core';

export class BlockParameter {

  static FILE_URL = new BlockParameter('FILE_URL', true, String, 'file');

  /**
   * SPLIT DATA
   */
  static FRACTION = new BlockParameter('FRACTION', true, Number, 'input', null, 0.5, 0.5);
  static REGULAR_EXPRESSION_VALUE = new BlockParameter('REGULAR_EXPRESSION_VALUE', true, Number, 'input', null, '"colName":^[0-9]', '"colName":^[0-9]');
  static SPLIT_ROWS = new BlockParameter('SPLIT_ROWS', false, String, 'option', [BlockParameter.FRACTION]);
  static REGULAR_EXPRESSION = new BlockParameter('REGULAR_EXPRESSION', false, String, 'option', [BlockParameter.REGULAR_EXPRESSION_VALUE]);
  static SPLITTING_MODE = new BlockParameter('SPLITTING_MODE', true, String, 'select', [BlockParameter.SPLIT_ROWS, BlockParameter.REGULAR_EXPRESSION], BlockParameter.SPLIT_ROWS, BlockParameter.SPLIT_ROWS);

  private constructor(
    public name: string,
    public required: boolean,
    public type: Type<any>,
    public htmlType: string,
    public dependencies?: Array<BlockParameter>,
    public value?: any,
    public defaultValue?: any
  ) {}

  getNameFromLocale() {
    return 'WORKFLOWS.BLOCKS.PARAMETERS.' + this.name;
  }
}
