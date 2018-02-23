import {AfterViewChecked, OnInit} from '@angular/core';
import * as $ from 'jquery';

export class BlockComponent implements OnInit, AfterViewChecked {
  name: string;
  id: string;
  code: string;
  valid = false;
  popUp = false;

  ngOnInit(): void {
  }

  ngAfterViewChecked(): void {
    // $('[data-toggle="tooltip"]').tooltip();
  }

  protected hidePopUp() {
    this.popUp = false;
  }

  protected showSettingsPopUp() {
    console.log('settings')
  }
}


