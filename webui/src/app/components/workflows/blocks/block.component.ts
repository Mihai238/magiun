import {AfterViewInit} from '@angular/core';
import {BlockPosition} from './block-position';
import {BlockType} from './block-type';

export class BlockComponent implements AfterViewInit {
  name: string;
  id: string;
  code: string;
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
}



