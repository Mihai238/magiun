import {Type} from '@angular/core';
import {UUID} from 'angular2-uuid';

export class BlockParameter<T> {

  static FILE_URL = new BlockParameter<String>('FILE_URL', true, String, 'file');

  static MULTI_INPUT = new BlockParameter<Array<String>>('MULTI_INPUT', true, undefined, 'multi-input');

  /**
   * SPLIT DATA
   */
  static FRACTION = new BlockParameter<Number>('FRACTION', true, Number, 'input', null, 0.5, 0.5);
  static RANDOMIZED = new BlockParameter<Boolean>('RANDOMIZED', true, Boolean, 'checkbox', null, true, true);
  static REGULAR_EXPRESSION_VALUE = new BlockParameter<String>('REGULAR_EXPRESSION_VALUE', true, String, 'input', null, '"colName":^[0-9]', '"colName":^[0-9]');
  static SPLIT_ROWS = new BlockParameter('SPLIT_ROWS', false, Object, 'option', [BlockParameter.FRACTION, BlockParameter.RANDOMIZED]);
  static REGULAR_EXPRESSION = new BlockParameter('REGULAR_EXPRESSION', false, Object, 'option', [BlockParameter.REGULAR_EXPRESSION_VALUE]);
  static SPLITTING_MODE = new BlockParameter('SPLITTING_MODE', true, Object, 'select', [BlockParameter.SPLIT_ROWS, BlockParameter.REGULAR_EXPRESSION], BlockParameter.SPLIT_ROWS, BlockParameter.SPLIT_ROWS);

  public id: string;
  private constructor(
    public name: string,
    public required: boolean,
    public type: Type<T>,
    public htmlType: string,
    public dependencies?: Array<BlockParameter<any>>,
    public value?: T,
    public defaultValue?: T
  ) {
    this.id = UUID.UUID();
  }

  getNameFromLocale() {
    return 'WORKFLOWS.BLOCKS.PARAMETERS.' + this.name;
  }

  clone() {
    return Object.assign({}, this);
  }

  equals(parameter: BlockParameter<any>) {
    return this.id === parameter.id;
  }
}
