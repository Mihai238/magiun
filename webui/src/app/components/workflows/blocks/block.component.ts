import {OnInit} from '@angular/core';

export class BlockComponent implements OnInit {
  name: string;
  id: string;
  code: string;
  valid = false;
  popUp = false;

  ngOnInit(): void {
  }

  protected showPopUp() {
    this.popUp = true;
  }

  protected hidePopUp() {
    this.popUp = false;
  }
}


