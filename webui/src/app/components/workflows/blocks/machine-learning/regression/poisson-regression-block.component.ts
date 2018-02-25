import {Component} from '@angular/core';
import {BlockComponent} from '../../block.component';
import {BlockType} from '../../block-type';

@Component({
  selector: 'app-poisson-regression-block',
  templateUrl: '../../block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class PoissonRegressionBlockComponent extends BlockComponent {
  name: string;
  id: string;
  code: string;

  constructor() {
    super();
    this.name = BlockType.POISSON_REGRESSION.name;
    this.code = BlockType.POISSON_REGRESSION.code;
    this.id = BlockType.POISSON_REGRESSION.code + '-' + new Date().getMilliseconds();
  }
}
