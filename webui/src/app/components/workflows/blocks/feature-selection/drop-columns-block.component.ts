import {Component} from "@angular/core";
import {FeatureSelectionBlockComponent} from "./feature-selection-block.component";
import {BlockService} from "../../../../services/block.service";
import {DialogService} from "ng2-bootstrap-modal";
import {BlockType} from "../block-type";
import {BlockParameter} from "../block-parameter";

@Component({
  selector: 'app-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./feature-selection-block.component.scss']
})
export class DropColumnsBlockComponent extends FeatureSelectionBlockComponent {

  name = BlockType.DROP_COLUMNS.name;
  id = BlockType.DROP_COLUMNS.name + '-' + new Date().getMilliseconds();

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.configurationParameters = [BlockParameter.MULTI_INPUT];
  }
}
