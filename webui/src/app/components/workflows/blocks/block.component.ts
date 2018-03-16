import {AfterViewInit, EventEmitter, Output} from '@angular/core';
import {BlockPosition} from './block-position';
import {BlockService} from '../../../services/block.service';
import {BlockParameter} from './block-parameter';
import {DialogService} from 'ng2-bootstrap-modal';
import {ParametersModalComponent} from './parameters-modal/parameters-modal.component';
import {WireType} from "./wire-type";

export abstract class BlockComponent implements AfterViewInit {

  private static base_path = 'WORKFLOWS.BLOCKS.INPUT_OUTPUT.';

  name: string;
  id: string;
  type: string;
  valid = false;
  position: BlockPosition;
  numberOfInputs = 0;
  inputs: Array<WireType> = [];
  numberOfOutputs = 0;
  outputs: Array<WireType> = [];
  configurationParameters: Array<BlockParameter<any>> = [];
  @Output('onDelete') onDelete = new EventEmitter<any>();

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

  // noinspection JSMethodCanBeStatic - this doesn't work if static
  range(maxValue) {
    return Array.from(Array(maxValue).keys());
  }

  // noinspection JSMethodCanBeStatic - this doesn't work if static
  popUpInputOutPutTitle(type: WireType): string {
    switch (type) {
      case WireType.DATASET: return BlockComponent.base_path + WireType.DATASET.i18nValue;
      case WireType.REGRESSION_MODEL: return BlockComponent.base_path + WireType.REGRESSION_MODEL.i18nValue;
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
    this.onDelete.emit();
  }
}



