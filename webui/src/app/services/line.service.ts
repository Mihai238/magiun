import {Injectable} from '@angular/core';
import {BlockComponent} from '../components/workflows/blocks/block.component';
import {WireType} from '../components/workflows/blocks/wire-type';
import {Tuple} from '../util/tuple';
import {BlockComponentsRelation} from '../components/workflows/blocks/block-components-relation';

declare var LeaderLine: any;

@Injectable()
export class LineService {

  startComponent: BlockComponent;
  startId: string;
  outputType: WireType;
  outputIndex: number;
  componentsMap = new Map<Tuple<string, string>, Array<BlockComponentsRelation>>();

  startLine(component: BlockComponent, startId: string, outputType: WireType, outputIndex: number): void {
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

  endLine(endComponent: BlockComponent, endId: string, inputType: WireType, inputIndex: number): Tuple<string, number> {
    if (this.isPointValidEndPoint(endComponent, endId, inputType)) {
      const line = new LeaderLine(
        document.getElementById(this.startId),
        document.getElementById(endId),
        {dropShadow: true}
      );

      this.addBlockRelationToMap(endComponent, inputIndex, line);
      this.changeFromSelectedToSet(document.getElementById(this.startId));
      this.changeFromUnsetToSet(document.getElementById(endId));
      const input = new Tuple<string, number>(this.startComponent.id, this.outputIndex);
      this.reset();
      return input;
    } else {
      return null;
    }
  }

  deselectLineStartPoint(id: string): void {
    if (this.startId === id) {
      this.reset();
      this.changeFromSelectedToUnset(document.getElementById(id));
    }
  }

  updatePosition(id: string): void {
    const entries = this.componentsMap.entries();
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

  deleteComponent(component: BlockComponent): void {
    const componentId = component.id;
    const entries = this.componentsMap.entries();
    let entry = entries.next().value;
    while (entry !== null && entry !== undefined) {
      if (entry[0]._1 === componentId) {
        this.deleteLinesUnsetBulletsAndDeleteComponent(entry[0], entry[1], true);
      } else if (entry[0]._2 === componentId) {
        this.deleteLinesUnsetBulletsAndDeleteComponent(entry[0], entry[1], false);
      }

      entry = entries.next().value;
    }
  }

  private deleteLinesUnsetBulletsAndDeleteComponent(key: Tuple<string, string>, relations: Array<BlockComponentsRelation>, start: boolean) {
    if (relations !== undefined && relations !== null) {
      relations.forEach(r => {
        if (start) {
          this.changeFromSetToUnset(document.getElementById(r.line.end.id));
        } else {
          const entries = this.componentsMap.entries();
          let entry = entries.next().value;
          let count = 0;
          while (entry !== null && entry !== undefined) {
            if (entry[0] !== key) {
              if (entry[1].filter(rl => rl.component1OutputIndex === r.component1OutputIndex).length > 0) {
                count++;
              }
            }
            entry = entries.next().value;
          }
          if (count === 0) {
            this.changeFromSetToUnset(document.getElementById(r.line.start.id));
          }
        }

        r.line.remove();
      });
    }
    this.componentsMap.delete(key);
  }

  // noinspection JSMethodCanBeStatic
  private changeFromUnsetToSelected(e) {
    e.classList.remove('unset');
    e.classList.add('selected')
  }

  // noinspection JSMethodCanBeStatic
  private changeFromSelectedToSet(e) {
    e.classList.remove('selected');
    e.classList.add('set');
  }

  // noinspection JSMethodCanBeStatic
  private changeFromSelectedToUnset(e) {
    e.classList.remove('selected');
    e.classList.add('unset');
  }

  // noinspection JSMethodCanBeStatic
  private changeFromUnsetToSet(e) {
    e.classList.remove('unset');
    e.classList.add('set');
  }

  // noinspection JSMethodCanBeStatic
  private changeFromSetToUnset(e) {
    e.classList.remove('set');
    e.classList.add('unset');
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

  private isPointValidEndPoint(endComponent: BlockComponent, endId: string, inputType: WireType): boolean {
    return this.startId !== endId &&
      this.startComponent !== endComponent &&
      document.getElementById(endId).classList.contains('unset') &&
      this.outputType === inputType;
  }

  private addBlockRelationToMap(endComponent: BlockComponent, inputIndex: number, line: any): void {
    const key = new Tuple<string, string>(this.startComponent.id, endComponent.id);
    const value = new BlockComponentsRelation(this.startComponent, this.outputIndex, endComponent, inputIndex, line);

    if (this.componentsMap.has(key)) {
      const array = this.componentsMap.get(key);
      array.push(value);

      this.componentsMap.set(key, array)
    } else {
      this.componentsMap.set(key, [value]);
    }
  }

}
