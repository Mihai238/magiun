import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Column} from '../../../../../model/data-set';

@Component({
  selector: 'chart-column-selector',
  templateUrl: './column-selector.component.html',
  styleUrls: ['./column-selector.component.css']
})
export class ColumnSelectorComponent implements OnInit {

  @Input() columns: Column[];
  @Output() columnUpdated = new EventEmitter();

  selectedColumn: Column;

  constructor() { }

  ngOnInit() {
    this.selectedColumn = this.columns[0];
    this.columnUpdated.emit(this.selectedColumn);
  }

  onSelectColumn(column: Column) {
    this.selectedColumn = column;
    this.columnUpdated.emit(column);
  }

}
