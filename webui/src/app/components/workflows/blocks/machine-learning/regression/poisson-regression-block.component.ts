import {Component} from '@angular/core';
import {BlockType} from '../../block-type';
import {RegressionBlockComponent} from './regression-block.component';

@Component({
  selector: 'app-poisson-regression-block',
  templateUrl: '../../block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class PoissonRegressionBlockComponent extends RegressionBlockComponent {

  constructor() {
    super();
    this.name = BlockType.POISSON_REGRESSION.name;
    this.code = BlockType.POISSON_REGRESSION.code;
    this.id = BlockType.POISSON_REGRESSION.code + '-' + new Date().getMilliseconds();
  }
}
