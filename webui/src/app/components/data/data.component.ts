import {Component, OnInit} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {DataService} from '../../services/data.service';
import {DataRow} from '../../model/data-row.model';
import {Column, DataSet} from '../../model/data-set.model';
import {environment} from '../../../environments/environment';
import {EditColumnResult} from './process-feature/edit-column.component';
import {NewColumnResult} from './new-column-settings/add-column-settings.component';
import {DataTableParams} from '../shared/table';
import {Recommendations} from "../../model/recommendations";

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

  displayNameDataSet: string;
  selectedDataSet: DataSet;
  selectedColumn: Column;
  recommendations: Recommendations;

  showAddColumnSettingsComponent: boolean;
  newColumnIndex: number;

  showEditColumnComponent: boolean;

  constructor(private logger: NGXLogger,
              private dataService: DataService) {
  }

  ngOnInit() {
    this.getDataSets();
    this.showAddColumnSettingsComponent = false;
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
    if (!dataSet.name.startsWith("mem-")) {
      this.displayNameDataSet = dataSet.name;
    }

    this.selectedDataSet = dataSet;
    this.recommendations = null;
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

  onEditColumnResult(editColumnResult: EditColumnResult) {
    this.showEditColumnComponent = false;
    this.loadMemDataSet(editColumnResult.memDataSetId);
  }

  onAddColumnResult(newColumnResult: NewColumnResult) {
    this.showAddColumnSettingsComponent = false;
    this.loadMemDataSet(newColumnResult.memDataSetId)
  }

  private loadMemDataSet(dataSetId: string) {
    if (dataSetId) {
      this.dataService.getDataSet(dataSetId)
        .subscribe((ds: DataSet) => this.onSelectDataSet(ds));
    }
  }

  onAddColumnClicked() {
    this.showAddColumnSettingsComponent = true;
  }

  onRecommendClicked() {
    const ds = this.selectedDataSet;
    if (ds) {
      this.logger.info("Getting recommendations for dataset " + ds.id);
      this.dataService.getRecommendations(ds)
        .subscribe(resp => {
          if (ds.id === this.selectedDataSet.id) {
            this.recommendations = resp;
          }
        });
    }

  }
}
