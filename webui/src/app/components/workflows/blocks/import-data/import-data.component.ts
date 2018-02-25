import {BlockComponent} from '../block.component';
import {BlockType} from '../block-type';
import {BlockService} from '../../../../services/block.service';

export class ImportDataComponent extends BlockComponent {

  constructor(blockService: BlockService) {
    super(blockService);
    this.numberOfOutputs = 1;
    this.outputs = [BlockType.DATASET];
  }
}
