import {Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';

@Component({
  selector: 'data-new-column-settings',
  templateUrl: './new-column-settings.component.html',
  styleUrls: ['./new-column-settings.component.css']
})
export class NewColumnSettingsComponent implements OnInit, OnChanges {

  @Input() visible: boolean;
  @Input() index: number;

  @ViewChild('modalActivator') modalActivatorEl: ElementRef;

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.visible) {
      this.modalActivatorEl.nativeElement.click();
    }
  }

}
