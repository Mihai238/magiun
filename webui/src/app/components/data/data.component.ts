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
  loadedPages = 0;

  histogramValues;

  constructor(private logger: NGXLogger,
              private dataService: DataService) {
  }

  ngOnInit() {
    this.getDataSets();
    this.selectedDataSet = undefined;

    this.histogramValues = [1, 2, 3, 0, 9 , 12];
  }

  getDataSets() {
    this.logger.info('Loading datasets');

    this.dataService.getDataSets()
      .subscribe(resp => this.dataSets = resp);
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
}
