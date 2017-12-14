import {Component, OnInit} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {DataService} from '../../services/data.service';
import {DataRow} from '../../model/data-row';
import {Column, ColumnType, DataSet} from '../../model/data-set';
import {environment} from '../../../environments/environment';
import {FeatureProcessResult} from './process-feature/process-feature.component';
import {NewColumnResult} from './new-column-settings/new-column-settings.component';

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css'],
  providers: [NGXLogger]
})
export class DataComponent implements OnInit {

  public ColumnType = ColumnType;

  rows: DataRow[];
  dataSets: DataSet[];

  selectedDataSet: DataSet;
  loadedPages = 0;
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

    this.selectedDataSet = dataSet;

    this.loadedPages = 0;
    this.rows = [];
    this.loadDataSet(dataSet, ++this.loadedPages);
  }

  onScrollDown() {
    this.loadDataSet(this.selectedDataSet, ++this.loadedPages);
    this.logger.info('Loading more rows');
  }

  private loadDataSet(dataSet: DataSet, pageToLoad: number) {
    this.logger.info('Loading data set: ' + dataSet.name);

    this.dataService.getData(dataSet, pageToLoad)
      .subscribe(
        resp => {
          this.rows.push(...resp);
        },
        err => {
          this.logger.error(err);
        },
        () => {
        }
      );
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
