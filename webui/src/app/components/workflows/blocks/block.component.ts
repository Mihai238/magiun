import {AfterViewInit} from '@angular/core';
import {BlockPosition} from './block-position';
import {BlockType} from './block-type';

export class BlockComponent implements AfterViewInit {

  private static base_path = 'WORKFLOWS.BLOCKS.INPUT_OUTPUT.';

  name: string;
  id: string;
  type: string;
  valid = false;
  popUp = false;
  position: BlockPosition;
  numberOfInputs = 0;
  inputs: Array<BlockType> = [];
  numberOfOutputs = 0;
  outputs: Array<BlockType> = [];

  protected hidePopUp() {
    this.popUp = false;
  }

  protected showSettingsPopUp() {
    console.log('settings')
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
}



