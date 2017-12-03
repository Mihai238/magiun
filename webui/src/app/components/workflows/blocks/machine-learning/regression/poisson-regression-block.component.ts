import { Component, OnInit } from '@angular/core';
import {BlockComponent} from '../../block.component';

@Component({
  selector: 'app-poisson-regression-block',
  templateUrl: './regression-block.component.html',
  styleUrls: ['./regression-block.component.css']
})
export class PoissonRegressionBlockComponent extends BlockComponent {
  name: string;
  id: string;
  code: string;

  constructor() {
    super();
    this.name = 'Poisson Regression';
    this.code = 'poissonRegression';
  }
}
