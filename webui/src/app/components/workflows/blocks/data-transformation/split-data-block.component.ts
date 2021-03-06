import {Component} from '@angular/core';
import {BlockType} from '../block-type';
import {BlockService} from '../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';
import {DataTransformationBlockComponent} from './data-transformation-block.component';
import {BlockParameter} from '../block-parameter';
import {WireType} from '../wire-type';
import {BlockComponent} from '../block.component';

@Component({
  selector: 'app-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./data-transformation-block.component.scss']
})
export class SplitDataBlockComponent extends DataTransformationBlockComponent {

  id = BlockType.SPLIT_DATA.name + '-' + new Date().getMilliseconds();
  name = BlockType.SPLIT_DATA.name;
  i18nValue = BlockComponent.base_path + BlockType.SPLIT_DATA.i18nValue;

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.outputs = [WireType.DATASET, WireType.DATASET];
    this.configurationParameters = [BlockParameter.SPLITTING_MODE, BlockParameter.FRACTION, BlockParameter.RANDOMIZED];
  }
}
