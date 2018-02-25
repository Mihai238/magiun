import {Component, ComponentFactoryResolver, OnInit, Type, ViewChild} from '@angular/core';
import {BlockComponent} from './blocks/block.component';
import {LinearRegressionBlockComponent} from './blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from './blocks/machine-learning/regression/poisson-regression-block.component';
import {WorkflowsDirective} from './workflows.directive';
import {BlockPosition} from './blocks/block-position';
import {DatabaseBlockComponent} from './blocks/import-data/database-block.component';
import {FileBlockComponent} from './blocks/import-data/file-block.component';

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
  @ViewChild(WorkflowsDirective) private workflowsDirective: WorkflowsDirective;

  private static getBlockComponentType(event): Type<any> {
    switch (event.toString()) {
      case 'database': return DatabaseBlockComponent;
      case 'file': return FileBlockComponent;
      case 'linearRegression': return LinearRegressionBlockComponent;
      case 'poissonRegression': return PoissonRegressionBlockComponent;
      default: return null;
    }
  }

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  private handleDropEventMouse(event): void {
    if (this.showPlaceholder) {
      this.showPlaceholder = false;
    }

    const object = JSON.parse(event.dataTransfer.getData('text')).object;
    if (typeof object === 'string') {
      this.createNewBlockComponent(event, object);
    } else if (typeof object === 'object') {
      this.updatePosition(event, object);
    }
  }

  private createNewBlockComponent(event, object): void {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(WorkflowsComponent.getBlockComponentType(object));
    const componentRef = this.workflowsDirective.viewContainerRef.createComponent(componentFactory);

    (<BlockComponent>componentRef.instance).position = new BlockPosition(event.layerX, event.layerY) ;
  }

  private updatePosition(event, object): void {
    const position = new BlockPosition(event.layerX, event.layerY);
    const d = document.getElementById(object.id);
    d.style.left = position.x + 'px';
    d.style.top =  position.y + 'px';
    this.blocksDropped.filter(b => b.id === object.id).forEach(b => b.position = position);
  }

  private updateTitle(event: any): void {
    this.title = event.target.value;

    console.log(this.title)
  }
}
