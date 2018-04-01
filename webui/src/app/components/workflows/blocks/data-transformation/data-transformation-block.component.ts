import {BlockComponent} from '../block.component';
import {BlockService} from '../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';
import {WireType} from '../wire-type';

export abstract class DataTransformationBlockComponent extends BlockComponent {

  abstract id: string;
  abstract name: string;

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.numberOfInputs = 1;
    this.inputs = [WireType.DATASET];
  }
}
