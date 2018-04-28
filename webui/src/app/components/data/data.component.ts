import {Component, OnInit} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {DataService} from '../../services/data.service';
import {DataRow} from '../../model/data-row.model';
import {Column, DataSet} from '../../model/data-set.model';
import {environment} from '../../../environments/environment';
import {FeatureProcessResult} from './process-feature/process-feature.component';
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

  showColumnSettings: boolean[];
  showNewColumnSettingsComponent: boolean;
  newColumnIndex: number;

  showProcessFeatureComponent: boolean;

  constructor(private logger: NGXLogger,
              private dataService: DataService) {
  }

  ngOnInit() {
    this.getDataSets();
    this.showNewColumnSettingsComponent = false;
    this.showProcessFeatureComponent = false;
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

    this.showColumnSettings = Array(dataSet.schema.columns.length);
    for (let i = 0; i < this.showColumnSettings.length; i++) {
      this.showColumnSettings[i] = false;
    }

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

  onSelectColumn(column: Column) {
    this.selectedColumn = column;
  }

  onClickRemoveColumn() {
    this.logger.info('Removing column: ' + this.selectedColumn.name);

    const columns: Column[] = this.selectedDataSet.schema.columns;
    const index = columns.indexOf(this.selectedColumn, 0);
    columns.splice(index, 1);

    this.rows.forEach(row => row.values.splice(index, 1));
  }

  onClickedColumnSettings(column: Column) {
    this.logger.info('onClickedColumnSettings: ' + column.name);
    this.showColumnSettings[column.index] = true;
  }

  onClickedOutside(column: Column) {
    this.closeDropdownSettings(column);
  }

  onClickedProcessFeature(column: Column) {
    this.logger.info('onClickedProcessFeature: ' + column.name);
    this.closeDropdownSettings(column);

    this.selectedColumn = column;
    this.showProcessFeatureComponent = true;
  }

  onClickAddColumn(column: Column, offset: number) {
    this.logger.info('onClickAddColumn ' + column.name + ' ,offset ' + offset);
    this.closeDropdownSettings(column);

    this.showNewColumnSettingsComponent = true;
    this.newColumnIndex = column.index + offset;
  }

  private closeDropdownSettings(column: Column) {
    this.showColumnSettings[column.index] = false;
  }

  onFeatureProcessResult(featureProcessResult: FeatureProcessResult) {
    this.showProcessFeatureComponent = false;
  }

  onNewColumnResult(newColumnResult: NewColumnResult) {
    this.showNewColumnSettingsComponent = false;
  }
}
