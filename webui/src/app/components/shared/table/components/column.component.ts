import { Directive, Input, ContentChild, OnInit } from '@angular/core';
import { DataTableRowComponent } from './row.component';
import { CellCallback } from '../types/cell-callback.type';


@Directive({
  selector: 'data-table-column'
})
export class DataTableColumn implements OnInit {

    // init:
    @Input() header: string;
    @Input() sortable = false;
    @Input() resizable = false;
    @Input() index: string;
    @Input() styleClass: string;
    @Input() cellColors: CellCallback;

    // init and state:
    @Input() width: number | string;
    @Input() visible = true;

    @ContentChild('dataTableCell') cellTemplate;
    @ContentChild('dataTableHeader') headerTemplate;

    getCellColor(row: DataTableRowComponent, index: number) {
        if (this.cellColors !== undefined) {
            return (<CellCallback>this.cellColors)(row.item, row, this, index);
        }
    }

    private styleClassObject = {}; // for [ngClass]

    ngOnInit() {
        this._initCellClass();
    }

    private _initCellClass() {
        if (!this.styleClass && this.index) {
            if (/^[a-zA-Z0-9_]+$/.test(this.index)) {
                this.styleClass = 'column-' + this.index;
            } else {
                this.styleClass = 'column-' + this.index.replace(/[^a-zA-Z0-9_]/g, '');
            }
        }

        if (this.styleClass != null) {
            this.styleClassObject = {
                [this.styleClass]: true
            };
        }
    }
}
