import {Component, EventEmitter, Input, OnDestroy, Output, QueryList} from '@angular/core';
import {DataTableColumn} from "./column.component";


@Component({
  moduleId: module.id + '',
  selector: '[dataTableRow]',
  templateUrl: './row.component.html',
  styleUrls: ['./row.component.css']
})
export class DataTableRowComponent implements OnDestroy {

  @Input() item: any;
  @Input() index: number;
  @Input() selectColumnVisible: boolean;
  @Input() columns: QueryList<DataTableColumn>;

  private _selected: boolean;

  @Output() selectedChange = new EventEmitter();

  constructor() {
  }

  get selected() {
    return this._selected;
  }

  set selected(selected) {
    this._selected = selected;
    this.selectedChange.emit(selected);
  }

  ngOnDestroy() {
    this.selected = false;
  }

  public _this = this; // FIXME is there no template keyword for this in angular 2?
}
