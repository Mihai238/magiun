import { DataTableRowComponent } from '../components/row.component';
import { DataTableColumn } from '../components/column.component';

export type CellCallback = (item: any, row: DataTableRowComponent, column: DataTableColumn, index: number) => string;
