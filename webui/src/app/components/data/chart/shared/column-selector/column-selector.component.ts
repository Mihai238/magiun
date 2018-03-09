import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Column} from '../../../../../model/data-set.model';

@Component({
  selector: 'chart-column-selector',
  templateUrl: './column-selector.component.html',
  styleUrls: ['./column-selector.component.scss']
})
export class ColumnSelectorComponent implements OnInit, OnChanges {

  @Input() columns: Column[];
  @Output() columnUpdated = new EventEmitter();

  selectedColumn: Column;

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
    this.selectedColumn = this.columns[0];
    this.columnUpdated.emit(this.selectedColumn);
  }

  onSelectColumn(column: Column) {
    this.selectedColumn = column;
    this.columnUpdated.emit(column);
  }

}
