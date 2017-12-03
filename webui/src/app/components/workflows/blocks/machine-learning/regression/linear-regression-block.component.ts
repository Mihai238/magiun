import { Component, OnInit } from '@angular/core';
import {BlockComponent} from '../../block.component';

@Component({
  selector: 'app-linear-regression-block',
  templateUrl: './regression-block.component.html',
  styleUrls: ['./regression-block.component.css']
})
export class LinearRegressionBlockComponent implements OnInit, BlockComponent {
  name: string;
  id: string;
  code: string;

  constructor() {
    this.name = 'Linear Regression';
    this.code = 'linearRegression';
  }

  ngOnInit() {
  }
}
