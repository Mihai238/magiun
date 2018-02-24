import {OnInit} from '@angular/core';

export class BlockComponent implements OnInit {
  name: string;
  id: string;
  code: string;
  valid = false;
  popUp = false;

  ngOnInit(): void {
  }

  protected hidePopUp() {
    this.popUp = false;
  }

  protected showSettingsPopUp() {
    console.log('settings')
  }
}


