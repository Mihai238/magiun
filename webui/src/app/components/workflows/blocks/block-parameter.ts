import {Type} from '@angular/core';

export class BlockParameter {

  static FILE_URL = new BlockParameter('FILE_URL', true, String);

  public value: any;

  private constructor(public name: string, public required: boolean, public type: Type<any>, public defaultValue?: any) {}

  getNameFromLocale() {
    return 'WORKFLOWS.BLOCKS.PARAMETERS.' + this.name;
  }
}
