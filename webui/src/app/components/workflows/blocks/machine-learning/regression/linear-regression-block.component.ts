import {Component} from '@angular/core';
import {BlockComponent} from '../../block.component';
import {BlockType} from '../../block-type';

@Component({
  selector: 'app-linear-regression-block',
  templateUrl: '../../block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class LinearRegressionBlockComponent extends BlockComponent {
  name: string;
  id: string;
  code: string;

  constructor() {
    super();
    this.name = BlockType.LINEAR_REGRESSION.name;
    this.code = BlockType.LINEAR_REGRESSION.code;
    this.id = BlockType.LINEAR_REGRESSION.code + '-' + new Date().getMilliseconds();
  }
}
