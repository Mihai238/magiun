import {Component, ComponentFactoryResolver, OnInit, Type, ViewChild} from '@angular/core';
import {BlockComponent} from './blocks/block.component';
import {LinearRegressionBlockComponent} from './blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from './blocks/machine-learning/regression/poisson-regression-block.component';
import {WorkflowsDirective} from './workflows.directive';

@Component({
  selector: 'app-workflows',
  templateUrl: './workflows.component.html',
  styleUrls: ['./workflows.component.scss']
})
export class WorkflowsComponent {

  private defaultWorkflowTitle = 'My workflow created on '.concat(new Date().toJSON().slice(0, 10).replace(/-/g, '/'));
  private title = this.defaultWorkflowTitle;
  private showPlaceholder = true;
  private blocksDropped: Array<BlockComponent> = [];
  @ViewChild(WorkflowsDirective) workflowsDirective: WorkflowsDirective;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  private handleDropEventMouse(event) {
    if (this.showPlaceholder) {
      this.showPlaceholder = false;
    }

    const object = JSON.parse(event.dataTransfer.getData('text')).object;
    if (typeof object === 'string') {
      const componentFactory = this.componentFactoryResolver.resolveComponentFactory(this.getBlockComponentType(object));
      const componentRef = this.workflowsDirective.viewContainerRef.createComponent(componentFactory);

      (<BlockComponent>componentRef.instance).x = event.layerX;
      (<BlockComponent>componentRef.instance).y = event.layerY;
      this.blocksDropped.push(componentRef.instance)

    } else if (typeof object === 'object') {
      const d = document.getElementById(object.id);
      d.style.left = event.layerX + 'px';
      d.style.top =  event.layerY + 'px';
    }
  }

  private getBlockComponentType(event): Type<any> {
    switch (event.toString()) {
      case 'linearRegression': return LinearRegressionBlockComponent;
      case 'poissonRegression': return PoissonRegressionBlockComponent;
      default: return null;
    }
  }

  private updateTitle(event: any) {
    this.title = event.target.value;

    console.log(this.title)
  }
}
