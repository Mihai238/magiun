import { Component, OnInit } from '@angular/core';
import {BlockComponent} from './blocks/block.component';
import {LinearRegressionBlockComponent} from './blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from './blocks/machine-learning/regression/poisson-regression-block.component';

@Component({
  selector: 'app-workflows',
  templateUrl: './workflows.component.html',
  styleUrls: ['./workflows.component.css']
})
export class WorkflowsComponent implements OnInit {

  private showPlaceholder = true;
  private blocksDropped: Array<BlockComponent> = [];

  constructor() { }

  ngOnInit() {
  }

  private addDropItem(event) {
    if (this.showPlaceholder) {
      this.showPlaceholder = false;
    }
    this.blocksDropped.push(this.getBlockComponentFromEvent(event));
  }

  private getBlockComponentFromEvent(event): BlockComponent {
    switch (event.toString()) {
      case 'linearRegression': return new LinearRegressionBlockComponent();
      case 'poissonRegression': return new PoissonRegressionBlockComponent();
      default: return null;
    }
  }
}
