import {BlockComponent} from '../block.component';
import {BlockService} from '../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';
import {WireType} from "../wire-type";

export abstract class ImportDataComponent extends BlockComponent {

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.numberOfOutputs = 1;
    this.outputs = [WireType.DATASET];
  }
}
