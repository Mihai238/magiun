import {BlockComponent} from '../../block.component';
import {BlockType} from '../../block-type';
import {BlockService} from '../../../../../services/block.service';

export class RegressionBlockComponent extends BlockComponent {

  constructor(blockService: BlockService) {
    super(blockService);
    this.numberOfInputs = 1;
    this.inputs = [BlockType.DATASET];
    this.numberOfOutputs = 1;
    this.outputs = [BlockType.REGRESSION_MODEL];
  }
}
