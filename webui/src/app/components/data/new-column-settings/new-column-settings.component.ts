import {
  Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges,
  ViewChild
} from '@angular/core';

@Component({
  selector: 'data-new-column-settings',
  templateUrl: './new-column-settings.component.html',
  styleUrls: ['./new-column-settings.component.css']
})
export class NewColumnSettingsComponent implements OnInit, OnChanges {

  @Input() visible: boolean;
  @Input() index: number;

  @Output() resultEmitter = new EventEmitter();

  @ViewChild('modalActivator') modalActivatorEl: ElementRef;

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.visible) {
      this.modalActivatorEl.nativeElement.click();
    }
  }

  onClickDone(): void {
    this.resultEmitter.emit({executted: true});
  }

  onClickCancel(): void {
    this.resultEmitter.emit({executted: false});
  }

}

export interface NewColumnResult {
  executed: boolean
}
