import {Component, OnInit} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {DataService} from '../../services/data.service';
import {DataRow} from '../../model/data-row.model';
import {Column, DataSet} from '../../model/data-set.model';
import {environment} from '../../../environments/environment';
import {FeatureProcessResult} from './process-feature/edit-column.component';
import {NewColumnResult} from './new-column-settings/new-column-settings.component';
import {DataTableParams} from '../shared/table';

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.scss'],
  providers: [NGXLogger]
})
export class DataComponent implements OnInit {

  dataSets: DataSet[];
  rows: DataRow[];
  rowsCount = 0;

  selectedDataSet: DataSet;
  selectedColumn: Column;

  showNewColumnSettingsComponent: boolean;
  newColumnIndex: number;

  showEditColumnComponent: boolean;

  constructor(private logger: NGXLogger,
              private dataService: DataService) {
  }

  ngOnInit() {
    this.getDataSets();
    this.showNewColumnSettingsComponent = false;
    this.showEditColumnComponent = false;
  }

  getDataSets() {
    this.logger.info('Loading datasets');

    this.dataService.getDataSets()
      .subscribe(resp => {
        this.dataSets = resp;
        if (!environment.production) {
          this.onSelectDataSet(this.dataSets[0]);
        }
      });
  }

  onSelectDataSet(dataSet: DataSet) {
    this.logger.info('Data Set selected: ' + dataSet.name);

    this.rowsCount = dataSet.schema.totalCount || this.rowsCount;
    this.selectedDataSet = dataSet;
    this.rows = [];
    this.reloadRows({limit: 10, offset: 0});
  }

  reloadRows(params: DataTableParams) {
    this.dataService.getDataForTable(this.selectedDataSet, params).then(result => {
      this.rows = result.items;
      this.rowsCount = result.count || this.rowsCount;
    })
  }

  onColumnEditClicked(columnIndex: number) {
    this.selectedColumn = this.selectedDataSet.schema.columns[columnIndex];
    this.logger.info('DataComponent: column selected ' + this.selectedColumn.name);

    this.showEditColumnComponent = true;
  }

  onClickRemoveColumn() {
    this.logger.info('Removing column: ' + this.selectedColumn.name);

    const columns: Column[] = this.selectedDataSet.schema.columns;
    const index = columns.indexOf(this.selectedColumn, 0);
    columns.splice(index, 1);

    this.rows.forEach(row => row.values.splice(index, 1));
  }

  onEditColumnResult(featureProcessResult: FeatureProcessResult) {
    this.showEditColumnComponent = false;
  }

  onNewColumnResult(newColumnResult: NewColumnResult) {
    this.showNewColumnSettingsComponent = false;
  }
}
