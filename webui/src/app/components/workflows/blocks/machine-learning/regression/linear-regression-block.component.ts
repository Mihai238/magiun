import { Component, OnInit } from '@angular/core';
import {BlockComponent} from '../../block.component';

@Component({
  selector: 'app-linear-regression-block',
  templateUrl: './regression-block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class LinearRegressionBlockComponent extends BlockComponent {
  name: string;
  id: string;
  code: string;

  constructor() {
    super();
    this.name = 'Linear Regression';
    this.code = 'linearRegression';
    this.id = 'linearRegression-' + new Date().getMilliseconds();
  }
}
