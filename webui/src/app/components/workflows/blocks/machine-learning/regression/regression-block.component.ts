import {BlockComponent} from '../../block.component';
import {BlockType} from '../../block-type';
import {BlockService} from '../../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';

export class RegressionBlockComponent extends BlockComponent {

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.numberOfInputs = 1;
    this.inputs = [BlockType.DATASET];
    this.numberOfOutputs = 1;
    this.outputs = [BlockType.REGRESSION_MODEL];
  }
}
