import {BlockComponent} from '../../block.component';
import {BlockService} from '../../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';
import {WireType} from "../../wire-type";

export abstract class RegressionBlockComponent extends BlockComponent {

  abstract id: string;
  abstract name: string;

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.inputs = [WireType.DATASET];
    this.outputs = [WireType.REGRESSION_MODEL];
  }
}
