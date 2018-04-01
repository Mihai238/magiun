import {Component, ComponentFactoryResolver, ComponentRef, Type, ViewChild} from '@angular/core';
import {BlockComponent} from './blocks/block.component';
import {LinearRegressionBlockComponent} from './blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from './blocks/machine-learning/regression/poisson-regression-block.component';
import {WorkflowDirective} from './workflow.directive';
import {BlockPosition} from './blocks/block-position';
import {DatabaseBlockComponent} from './blocks/import-data/database-block.component';
import {FileBlockComponent} from './blocks/import-data/file-block.component';
import {BlockService} from '../../services/block.service';
import {SplitDataBlockComponent} from './blocks/data-transformation/split-data-block.component';
import {DropColumnsBlockComponent} from './blocks/feature-selection/drop-columns-block.component';

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
      case 'splitData': return SplitDataBlockComponent;
      case 'dropColumns': return DropColumnsBlockComponent;
      case 'linearRegression': return LinearRegressionBlockComponent;
      case 'poissonRegression': return PoissonRegressionBlockComponent;
      default: return null;
    }
  }

  constructor(private componentFactoryResolver: ComponentFactoryResolver, private blockService: BlockService) { }

  handleDropEventMouse(event): void {
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
    blockInstance.onDelete.subscribe(() => {this.deleteComponent(blockInstance, componentRef)});
    this.blocksDropped.push(blockInstance);
    this.blockService.upsertBlock(blockInstance);
  }

  private updatePosition(event, id): void {
    const position = new BlockPosition(event.layerX, event.layerY);
    const d = document.getElementById(id);
    d.style.left = position.x + 'px';
    d.style.top =  position.y + 'px';
    this.blocksDropped.filter(b => b.id === id).forEach(b => b.position = position);
    this.blockService.updatePosition(id);
  }

  updateTitle(event: any): void {
    this.title = event.target.value;
  }

  private deleteComponent(component: BlockComponent, componentRef: ComponentRef<any>): void {
    const index = this.blocksDropped.indexOf(component, 0);
    if (index >= 0) {
      this.blocksDropped.splice(index, 1);
    }

    componentRef.destroy();
    this.blockService.deleteComponent(component);
  }

  private run() {
    this.blockService.run();
  }

  private export() {
    // TODO: paulcurcean
  }

  private import() {
    // TODO: paulcurcean
  }
}
