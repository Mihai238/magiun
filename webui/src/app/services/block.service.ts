import {Injectable} from '@angular/core';
import {BlockComponent} from '../components/workflows/blocks/block.component';
import {CollectionsUtils} from '../util/collections.utils';
import {WireType} from '../components/workflows/blocks/wire-type';
import {LineService} from './line.service';
import {BlockRestService} from '../rest/block.rest.service';
import {BlockPosition} from '../components/workflows/blocks/block-position';

@Injectable()
export class BlockService {


  blocks: Map<string, BlockComponent> = new Map<string, BlockComponent>();

  constructor(private blockRestService: BlockRestService, private lineService: LineService) {}

  run(): void {
    // TODO: paulcurcean implement
  }

  addBlock(block: BlockComponent): void {
    this.blocks.set(block.id, block);
    this.blockRestService.upsertBlock(block);
  }

  updateBlock(block: BlockComponent): void {
    this.blocks.set(block.id, block);
    this.blockRestService.upsertBlock(block);
  }

  deleteBlock(block: BlockComponent): void {
    this.blocks.delete(block.id);
    this.deleteBlockFromInputsArray(block);
    this.lineService.deleteComponent(block);
    this.blockRestService.deleteBlock(block.id)
  }

  private deleteBlockFromInputsArray(block: BlockComponent) {
    this.blocks.forEach((value: BlockComponent, key: string) => {
      if (value.setInputs.length > 0) {
        const inputs = value.setInputs;
        inputs.forEach(input => {
          if (input._1 === block.id) {
            value.setInputs = CollectionsUtils.deleteEntryFromArray(value.setInputs, input);
            this.blockRestService.upsertBlock(value);
          }
        })
      }
    })
  }

  updatePosition(id: string, position: BlockPosition): void {
    const d = document.getElementById(id);
    d.style.left = position.x + 'px';
    d.style.top =  position.y + 'px';
    this.blocks.get(id).position = position;
    this.lineService.updatePosition(id);
  }

  startLine(component: BlockComponent, startId: string, outputType: WireType, outputIndex: number): void {
    this.lineService.startLine(component, startId, outputType, outputIndex);
  }

  endLine(endComponent: BlockComponent, endId: string, inputType: WireType, inputIndex: number): void {
    const input = this.lineService.endLine(endComponent, endId, inputType, inputIndex);
    if (input !== null) {
      this.blocks.get(endComponent.id).setInputs.push(input);
      this.blockRestService.upsertBlock(this.blocks.get(endComponent.id));
    }
  }

  deselectLineStartPoint(id: string) {
    this.lineService.deselectLineStartPoint(id);
  }
}
