import {BlockComponent} from '../block.component';
import {BlockType} from '../block-type';

export class ImportDataComponent extends BlockComponent {

  constructor() {
    super();
    this.numberOfOutputs = 1;
    this.outputs = [BlockType.DATABASE];
  }
}
