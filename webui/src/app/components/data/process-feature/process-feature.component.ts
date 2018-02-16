import {
  Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges,
  ViewChild
} from '@angular/core';
import {Column} from '../../../model/data-set';

@Component({
  selector: 'data-process-feature',
  templateUrl: './process-feature.component.html',
  styleUrls: ['./process-feature.component.scss']
})
export class ProcessFeatureComponent implements OnInit, OnChanges {

  @Input() visible: boolean;
  @Input() column: Column;

  @Output() resultEmitter = new EventEmitter<FeatureProcessResult>();

  @ViewChild('modalActivator') modalActivatorEl: ElementRef;

  constructor() {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.visible) {
      this.modalActivatorEl.nativeElement.click();
    }
  }

  onClickDone(): void {
    this.resultEmitter.emit({executed: true});
  }

  onClickCancel(): void {
    this.resultEmitter.emit({executed: false});
  }

}

export interface FeatureProcessResult {
  executed: boolean;
}
