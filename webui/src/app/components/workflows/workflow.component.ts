import {Component, ComponentFactoryResolver, ComponentRef, OnInit, Type, ViewChild} from '@angular/core';
import {BlockComponent} from './blocks/block.component';
import {LinearRegressionBlockComponent} from './blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from './blocks/machine-learning/regression/poisson-regression-block.component';
import {WorkflowDirective} from './workflow.directive';
import {BlockPosition} from './blocks/block-position';
import {DatabaseBlockComponent} from './blocks/import-data/database-block.component';
import {FileBlockComponent} from './blocks/import-data/file-block.component';
import {BlockService} from '../../services/block.service';

@Component({
  selector: 'app-workflow',
  templateUrl: './workflow.component.html',
  styleUrls: ['./workflow.component.scss']
})
export class WorkflowComponent {

  private defaultWorkflowTitle = 'My workflow created on '.concat(new Date().toJSON().slice(0, 10).replace(/-/g, '/'));
  private title = this.defaultWorkflowTitle;
  private showPlaceholder = true;
  private blocksDropped: Array<BlockComponent> = [];
  @ViewChild(WorkflowDirective) private workflowsDirective: WorkflowDirective;

  private static getBlockComponentType(event): Type<any> {
    switch (event.toString()) {
      case 'database': return DatabaseBlockComponent;
      case 'file': return FileBlockComponent;
      case 'linearRegression': return LinearRegressionBlockComponent;
      case 'poissonRegression': return PoissonRegressionBlockComponent;
      default: return null;
    }
  }

  constructor(private componentFactoryResolver: ComponentFactoryResolver, private blockService: BlockService) { }

  private handleDropEventMouse(event): void {
    if (this.showPlaceholder) {
      this.showPlaceholder = false;
    }

    const object = JSON.parse(event.dataTransfer.getData('text')).object;

    if (typeof object === 'string') {
      this.createNewBlockComponent(event, object);
    } else if (typeof object === 'object') {
      this.updatePosition(event, object.id);
    }
  }

  private createNewBlockComponent(event, object): void {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(WorkflowComponent.getBlockComponentType(object));
    const componentRef = this.workflowsDirective.viewContainerRef.createComponent(componentFactory);
    const blockInstance = componentRef.instance as BlockComponent;

    blockInstance.position = new BlockPosition(event.layerX, event.layerY);
    blockInstance._ref = blockInstance;
    blockInstance.onDelete.subscribe(() => {this.deleteComponent(blockInstance, componentRef)});
    this.blocksDropped.push(blockInstance);
  }

  private updatePosition(event, id): void {
    const position = new BlockPosition(event.layerX, event.layerY);
    const d = document.getElementById(id);
    d.style.left = position.x + 'px';
    d.style.top =  position.y + 'px';
    this.blocksDropped.filter(b => b.id === id).forEach(b => b.position = position);
    this.blockService.updatePosition(id);
  }

  private updateTitle(event: any): void {
    this.title = event.target.value;
  }

  private deleteComponent(component: BlockComponent, componentRef: ComponentRef<any>): void {
    const index = this.blocksDropped.indexOf(component, 0);
    if (index >= 0) {
      this.blocksDropped.splice(index, 1);
    }

    componentRef.destroy();

    // TODO implement deletion of block-component in block.service
  }
}
