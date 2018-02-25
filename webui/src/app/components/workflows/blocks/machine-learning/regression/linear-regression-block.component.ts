import {Component} from '@angular/core';
import {BlockType} from '../../block-type';
import {RegressionBlockComponent} from './regression-block.component';

@Component({
  selector: 'app-linear-regression-block',
  templateUrl: '../../block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class LinearRegressionBlockComponent extends RegressionBlockComponent {

  constructor() {
    super();
    this.name = BlockType.LINEAR_REGRESSION.name;
    this.code = BlockType.LINEAR_REGRESSION.code;
    this.id = BlockType.LINEAR_REGRESSION.code + '-' + new Date().getMilliseconds();
  }
}
