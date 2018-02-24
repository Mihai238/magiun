import {AfterViewChecked, OnInit} from '@angular/core';

export class BlockComponent implements OnInit, AfterViewChecked {
  name: string;
  id: string;
  code: string;
  valid = false;
  popUp = false;
  x: number;
  y: number;

  ngOnInit(): void {
  }

  protected hidePopUp() {
    this.popUp = false;
  }

  protected showSettingsPopUp() {
    console.log('settings')
  }

  setCoordinates(x: number, y: number) {
    this.x = x;
    this.y = y;
    return this;
  }

  ngAfterViewChecked() {
    const d = document.getElementById(this.id);
    d.style.left = this.x + 'px';
    d.style.top =  this.y + 'px';
  }
}


