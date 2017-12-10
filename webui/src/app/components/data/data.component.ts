import {Component, OnInit} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {DataService} from '../../services/data.service';
import {DataRow} from '../../model/data-row';
import {Column, ColumnType, DataSet} from '../../model/data-set';
import {environment} from '../../../environments/environment';

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

  constructor(private logger: NGXLogger,
              private dataService: DataService) {
  }

  ngOnInit() {
    this.getDataSets();
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

  onClickColumnSettings(column: Column) {
    this.logger.info('Setting column: ' + column.name);
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
}
