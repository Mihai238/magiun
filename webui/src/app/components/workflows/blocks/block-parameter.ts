import {Type} from '@angular/core';

export class BlockParameter {

  static FILE_URL = new BlockParameter('FILE_URL', true, String, 'file');

  /**
   * SPLIT DATA
   */
  static SPLITTING_MODE = new BlockParameter('SPLITTING_MODE', true, String, 'select', ['Split Rows', 'Regular Expression']);

  public value: any;

  private constructor(public name: string, public required: boolean, public type: Type<any>, public htmlType: string, public dependencies?: any, public defaultValue?: any) {}

  getNameFromLocale() {
    return 'WORKFLOWS.BLOCKS.PARAMETERS.' + this.name;
  }
}
