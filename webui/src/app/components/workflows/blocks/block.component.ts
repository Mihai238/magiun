import {AfterViewInit, OnInit} from '@angular/core';

export class BlockComponent implements AfterViewInit {
  name: string;
  id: string;
  code: string;
  valid = false;
  popUp = false;
  x: number;
  y: number;

  protected hidePopUp() {
    this.popUp = false;
  }

  protected showSettingsPopUp() {
    console.log('settings')
  }

  ngAfterViewInit() {
    const d = document.getElementById(this.id);
    d.style.left = this.x + 'px';
    d.style.top =  this.y + 'px';
  }
}


