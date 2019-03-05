import {AfterViewInit, EventEmitter, Output} from '@angular/core';
import {BlockPosition} from './block-position';
import {BlockService} from '../../../services/block.service';
import {BlockParameter} from './block-parameter';
import {DialogService} from 'ng2-bootstrap-modal';
import {ParametersModalComponent} from './parameters-modal/parameters-modal.component';
import {WireType} from './wire-type';
import {Tuple} from '../../../util/tuple';

export abstract class BlockComponent implements AfterViewInit {

  protected static base_path = 'WORKFLOWS.BLOCKS.';
  private static input_output_base_path = 'WORKFLOWS.BLOCKS.INPUT_OUTPUT.';

  abstract id: string;
  abstract name: string;
  abstract i18nValue: string;

  valid = false;
  position: BlockPosition;
  inputs: Array<WireType> = [];
  outputs: Array<WireType> = [];
  configurationParameters: Array<BlockParameter<any>> = [];
  setInputs: Array<Tuple<string, number>> = [];

  @Output('onDelete') onDelete = new EventEmitter<any>();

  protected constructor(private blockService: BlockService, private dialogService: DialogService) {}

  protected showParametersModal() {
    this.dialogService.addDialog(ParametersModalComponent, { parameters: this.configurationParameters })
      .subscribe(
        (result) => {
        this.configurationParameters = result;
        const unSetParametersCount = this.configurationParameters.filter(p => p.value === null || p.value === undefined).length;
        if (unSetParametersCount === 0) {
          this.valid = true;
          this.blockService.updateBlock(this);
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
      case WireType.DATASET: return BlockComponent.input_output_base_path + WireType.DATASET.i18nValue;
      case WireType.REGRESSION_MODEL: return BlockComponent.input_output_base_path + WireType.REGRESSION_MODEL.i18nValue;
      default: return 'ERROR';
    }
  }

  private startLine(index: number): void {
    this.blockService.startLine(this, this.id + '-output-' + index, this.outputs[index], index);
  }

  private endLine(index: number): void {
    this.blockService.endLine(this, this.id + '-input-' + index, this.inputs[index], index);
  }

  private deselect(index: number): void {
    this.blockService.deselectLineStartPoint(this.id + '-output-' + index);
  }

  private delete(): void {
    this.onDelete.emit();
  }
}



