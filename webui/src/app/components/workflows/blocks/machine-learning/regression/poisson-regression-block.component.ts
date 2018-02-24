import {Component} from '@angular/core';
import {BlockComponent} from '../../block.component';

@Component({
  selector: 'app-poisson-regression-block',
  templateUrl: './regression-block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class PoissonRegressionBlockComponent extends BlockComponent {
  name: string;
  id: string;
  code: string;

  constructor() {
    super();
    this.name = 'Poisson Regression';
    this.code = 'poissonRegression';
    this.id = 'poissonRegression-' + new Date().getMilliseconds();
  }
}
