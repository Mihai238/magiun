import {Component, OnInit} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {DataService} from '../../services/data.service';
import {DataRow} from '../../model/data-row';
import {DataSet} from '../../model/data-set';

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css'],
  providers: [NGXLogger]
})
export class DataComponent implements OnInit {

  rows: DataRow[];
  dataSets: DataSet[];

  selectedDataSet: DataSet;
  rowsLoaded: boolean;

  constructor(private logger: NGXLogger,
              private dataService: DataService) {
  }

  ngOnInit() {
    this.getDataSets();
    this.selectedDataSet = undefined;
    this.rowsLoaded = true;
  }

  getDataSets() {
    this.logger.info('Loading datasets');

    this.dataService.getDataSets()
      .subscribe(resp => this.dataSets = resp);
  }

  onSelectDataSet(dataSet: DataSet) {
    this.logger.info('Data Set selected: ' + dataSet.name);

    this.selectedDataSet = dataSet;
    this.loadDataSet(dataSet);
  }

  private loadDataSet(dataSet: DataSet) {
    this.logger.info('Loading data set: ' + dataSet.name);

    this.dataService.getData(dataSet)
      .subscribe(
        resp => {
          this.rows = resp;
        },
        err => {
          console.log(err);
          this.rowsLoaded = true;
        },
        () => {
          this.rowsLoaded = true;
        }
      );

  }
}
