import {Component} from '@angular/core';
import {BlockType} from '../../block-type';
import {RegressionBlockComponent} from './regression-block.component';
import {BlockService} from '../../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';
import {BlockComponent} from '../../block.component';

@Component({
  selector: 'app-block',
  templateUrl: '../../block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class LinearRegressionBlockComponent extends RegressionBlockComponent {

  id: string = BlockType.LINEAR_REGRESSION.name + '-' + new Date().getMilliseconds();
  name: string = BlockType.LINEAR_REGRESSION.name;
  i18nValue = BlockComponent.base_path + BlockType.LINEAR_REGRESSION.i18nValue;

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
  }
}
