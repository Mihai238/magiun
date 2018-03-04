import {Component} from '@angular/core';
import {BlockType} from '../block-type';
import {BlockService} from '../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';
import {DataTransformationBlockComponent} from './data-transformation-block.component';
import {BlockParameter} from '../block-parameter';

@Component({
  selector: 'app-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./data-transformation-block.component.scss']
})
export class SplitDataBlockComponent extends DataTransformationBlockComponent {

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.name = BlockType.SPLIT_DATA.name;
    this.type = BlockType.SPLIT_DATA.type;
    this.id = BlockType.SPLIT_DATA.type + '-' + new Date().getMilliseconds();
    this.numberOfOutputs = 2;
    this.outputs = [BlockType.DATASET, BlockType.DATASET];
    this.configurationParameters = [BlockParameter.SPLITTING_MODE, BlockParameter.FRACTION, BlockParameter.RANDOMIZED];
  }
}
