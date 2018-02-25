import {Component} from '@angular/core';
import {BlockType} from '../../block-type';
import {RegressionBlockComponent} from './regression-block.component';
import {BlockService} from '../../../../../services/block.service';

@Component({
  selector: 'app-linear-regression-block',
  templateUrl: '../../block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class LinearRegressionBlockComponent extends RegressionBlockComponent {

  constructor(blockService: BlockService) {
    super(blockService);
    this.name = BlockType.LINEAR_REGRESSION.name;
    this.type = BlockType.LINEAR_REGRESSION.type;
    this.id = BlockType.LINEAR_REGRESSION.type + '-' + new Date().getMilliseconds();
  }
}
