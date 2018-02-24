import { Component, OnInit } from '@angular/core';
import {BlockComponent} from './blocks/block.component';
import {LinearRegressionBlockComponent} from './blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from './blocks/machine-learning/regression/poisson-regression-block.component';

@Component({
  selector: 'app-workflows',
  templateUrl: './workflows.component.html',
  styleUrls: ['./workflows.component.scss']
})
export class WorkflowsComponent implements OnInit {

  private defaultWorkflowTitle = 'My workflow created on '.concat(new Date().toJSON().slice(0, 10).replace(/-/g, '/'));
  private title = this.defaultWorkflowTitle;
  private showPlaceholder = true;
  private blocksDropped: Array<BlockComponent> = [];

  ngOnInit() {
  }

  private handleDropEventMouse(event) {
    if (this.showPlaceholder) {
      this.showPlaceholder = false;
    }

    const object = JSON.parse(event.dataTransfer.getData('text')).object;
    if (typeof object === 'string') {
      this.blocksDropped.push(this.getBlockComponentFromEvent(object, event.layerX, event.layerY));
    } else if (typeof object === 'object') {
      const d = document.getElementById(object.id);
      d.style.left = event.layerX + 'px';
      d.style.top =  event.layerY + 'px';
    }
  }

  private getBlockComponentFromEvent(event, x, y): BlockComponent {
    switch (event.toString()) {
      case 'linearRegression': return new LinearRegressionBlockComponent().setCoordinates(x, y);
      case 'poissonRegression': return new PoissonRegressionBlockComponent().setCoordinates(x, y);
      default: return null;
    }
  }
  private updateTitle(event: any) {
    this.title = event.target.value;

    console.log(this.title)
  }
}
