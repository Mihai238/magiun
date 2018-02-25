import {BlockComponent} from '../../block.component';
import {BlockType} from '../../block-type';

export class RegressionBlockComponent extends BlockComponent {

  constructor() {
    super();
    this.numberOfInputs = 1;
    this.inputs = [BlockType.DATASET];
    this.numberOfOutputs = 1;
  }
}
