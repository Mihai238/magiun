import {BlockComponent} from "../block.component";
import {BlockService} from "../../../../services/block.service";
import {DialogService} from "ng2-bootstrap-modal";
import {WireType} from "../wire-type";

export abstract class FeatureSelectionBlockComponent extends BlockComponent {
  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.numberOfInputs = 1;
    this.inputs = [WireType.DATASET];
    this.numberOfOutputs = 1;
    this.outputs = [WireType.DATASET];
  }
}
