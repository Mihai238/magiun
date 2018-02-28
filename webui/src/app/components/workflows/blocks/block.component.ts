import {AfterViewInit, EventEmitter, Output} from '@angular/core';
import {BlockPosition} from './block-position';
import {BlockType} from './block-type';
import {BlockService} from '../../../services/block.service';
import {BlockParameter} from './block-parameter';
import {DialogService} from 'ng2-bootstrap-modal';
import {ParametersModalComponent} from './parameters-modal/parameters-modal.component';

export class BlockComponent implements AfterViewInit {

  private static base_path = 'WORKFLOWS.BLOCKS.INPUT_OUTPUT.';

  name: string;
  id: string;
  type: string;
  valid = false;
  position: BlockPosition;
  numberOfInputs = 0;
  inputs: Array<BlockType> = [];
  numberOfOutputs = 0;
  outputs: Array<BlockType> = [];
  configurationParameters: Array<BlockParameter> = [];

  constructor(private blockService: BlockService, private dialogService: DialogService) {}

  protected showParametersModal() {
    this.dialogService.addDialog(ParametersModalComponent, {parameters: this.configurationParameters})
      .subscribe(
        (result) => {
        this.configurationParameters = result;
        const unSetParametersCount = this.configurationParameters.filter(p => p.value === null || p.value === undefined).length;
        if (unSetParametersCount === 0) {
          this.valid = true;
        }
      });
  }

  ngAfterViewInit() {
    const d = document.getElementById(this.id);
    d.style.left = this.position.x + 'px';
    d.style.top = this.position.y + 'px';
  }

  private range(maxValue) {
    return Array(maxValue).fill(0, maxValue - 1);
  }

  private popUpInputOutPutTitle(type: BlockType): string {
    switch (type) {
      case BlockType.DATASET: return BlockComponent.base_path + BlockType.DATASET.value;
      case BlockType.REGRESSION_MODEL: return BlockComponent.base_path + BlockType.REGRESSION_MODEL.value;
      default: return 'ERROR';
    }
  }

  private startLine(index: number) {
    this.blockService.startLine(this, this.id + '-output-' + index, this.outputs[index], index);
  }

  private endLine(index: number) {
    this.blockService.endLine(this, this.id + '-input-' + index, this.inputs[index], index);
  }

  private deselect(index: number) {
    this.blockService.deselectLineStartPoint(this.id + '-output-' + index);
  }

  private delete() {
  }
}



