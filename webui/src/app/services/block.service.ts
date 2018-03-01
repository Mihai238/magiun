import {Injectable} from '@angular/core';
import {BlockComponent} from '../components/workflows/blocks/block.component';
import {BlockType} from '../components/workflows/blocks/block-type';
import {Tuple} from '../util/tuple';
import {BlockComponentsRelation} from '../components/workflows/blocks/block-components-relation';
import {Utils} from '../util/utils';

declare var LeaderLine: any;

@Injectable()
export class BlockService {

  private startComponent: BlockComponent;
  private startId: string;
  private outputType: BlockType;
  private outputIndex: number;
  private compnentsMap = new Map<Tuple<string, string>, Array<BlockComponentsRelation>>();

  startLine(component: BlockComponent, startId: string, outputType: BlockType, outputIndex: number): void {
    if (this.isAStartPointAlreadySelected()) {
      this.startComponent = component;
      this.startId = startId;
      this.outputType = outputType;
      this.outputIndex = outputIndex;

      const e = document.getElementById(startId);
      if (e.classList.contains('unset')) {
        this.changeFromUnsetToSelected(e);
      }
    }
  }

  endLine(endComponent: BlockComponent, endId: string, inputType: BlockType, inputIndex: number): void {
    if (this.isPointAValidEndPint(endComponent, endId, inputType)) {
      const line = new LeaderLine(
        document.getElementById(this.startId),
        document.getElementById(endId),
        {dropShadow: true}
      );

      this.addBlockRelationToMap(endComponent, inputIndex, line);
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

  updatePosition(id: string): void {
    const entries = this.compnentsMap.entries();
    let entry = entries.next();
    while (entry !== null && entry !== undefined && entry.value !== null && entry.value !== undefined) {
      const entryValue = entry.value;
      if (entryValue[0]._1 === id || entryValue[0]._2 === id) {
        const relations: Array<BlockComponentsRelation> = entryValue[1];
        if (relations !== undefined && relations !== null) {
          relations.forEach(r => r.line.position());
        }
      }
      entry = entries.next();
    }
  }

  // TODO change bullet color after deleting the line
  deleteComponent(component: BlockComponent): void {
    const componentId = component.id;
    const entries = this.compnentsMap.entries();
    let entry = entries.next().value;
    while (entry !== null && entry !== undefined) {
      if (entry[0]._1 === componentId || entry[0]._2 === componentId) {
        const relations: Array<BlockComponentsRelation> = entry[1];
        if (relations !== undefined && relations !== null) {
          relations.forEach(r => r.line.remove());
        }
        this.compnentsMap.delete(entry[0]);
      }

      entry = entries.next().value;
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
    this.outputIndex = null;
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

  private addBlockRelationToMap(endComponent: BlockComponent, inputIndex: number, line: any): void {
    const key = new Tuple<string, string>(this.startComponent.id, endComponent.id);
    const value = new BlockComponentsRelation(this.startComponent, this.outputIndex, endComponent, inputIndex, line);

    if (Utils.isKeyPresentInTheMap(this.compnentsMap, key)) {
      const array = this.compnentsMap.get(key);
      array.push(value);

      this.compnentsMap.set(key, array)
    } else {
      this.compnentsMap.set(key, [value]);
    }
  }

}
