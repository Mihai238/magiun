import {Component, Input} from '@angular/core';
import {Column} from '../app/model/data-set.model';

@Component({
  selector: 'chart-column-selector',
  template: ''
})
export class StubColumnSelectorComponent {
  @Input() columns: Column[];
}
