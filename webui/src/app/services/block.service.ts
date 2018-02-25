import {Injectable} from '@angular/core';
import {BlockComponent} from '../components/workflows/blocks/block.component';
import {BlockType} from '../components/workflows/blocks/block-type';

declare var LeaderLine: any;

@Injectable()
export class BlockService {

  private startComponent: BlockComponent;
  private startId: string;
  private outputType: BlockType;
  private linesMap = new Map<any, Array<string>>();

  startLine(component: BlockComponent, startId: string, outputType: BlockType): void {
    if (this.isAStartPointAlreadySelected()) {
      this.startComponent = component;
      this.startId = startId;
      this.outputType = outputType;

      const e = document.getElementById(startId);
      if (e.classList.contains('unset')) {
        this.changeFromUnsetToSelected(e);
      }
    }
  }

  endLine(endComponent: BlockComponent, endId: string, inputType: BlockType): void {
    if (this.isPointAValidEndPint(endComponent, endId, inputType)) {
      const line = new LeaderLine(
        document.getElementById(this.startId),
        document.getElementById(endId)
      );

      this.linesMap.set(line, [this.startId, endId]);

      this.makeComponentUndragable(this.startComponent.id);
      this.makeComponentUndragable(endComponent.id);
      this.changeFromSelectedToSet(document.getElementById(this.startId));
      this.changeFromUnsetToSet(document.getElementById(endId));
      this.reset();
    }
  }

  deselectLineStartPoint(id: string): void {
    if (this.startId === id) {
      this.reset();
      this.changeFromSelectedToUnset(document.getElementById(id));
    }
  }

  private changeFromUnsetToSelected(e) {
    e.classList.remove('unset');
    e.classList.add('selected')
  }

  private changeFromSelectedToSet(e) {
    e.classList.remove('selected');
    e.classList.add('set');
  }

  private changeFromSelectedToUnset(e) {
    e.classList.remove('selected');
    e.classList.add('unset');
  }

  private changeFromUnsetToSet(e) {
    e.classList.remove('unset');
    e.classList.add('set');
  }

  private reset(): void {
    this.startId = null;
    this.startComponent = null;
    this.outputType = null;
  }

  private makeComponentUndragable(id: string): void {
    document.getElementById(id).draggable = false;
  }

  private isAStartPointAlreadySelected(): boolean {
    return this.startId === null || this.startId === undefined;
  }

  private isPointAValidEndPint(endComponent: BlockComponent, endId: string, inputType: BlockType): boolean {
    return this.startId !== endId &&
      this.startComponent !== endComponent &&
      document.getElementById(endId).classList.contains('unset') &&
      this.outputType === inputType;
  }
}
