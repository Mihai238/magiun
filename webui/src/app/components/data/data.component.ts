import {Component, OnInit} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {DataService} from '../../services/data.service';
import {DataRow} from '../../model/data-row';
import {Observable} from 'rxjs/Observable';
import {DataSet} from '../../model/data-set';

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css'],
  providers: [NGXLogger]
})
export class DataComponent implements OnInit {

  rowObservable: Observable<DataRow>;
  dataSetObservable: Observable<DataSet>;

  selectedDataSet: DataSet;

  constructor(private logger: NGXLogger,
              private dataService: DataService) { }

  ngOnInit() {
    this.getDataSets();
    this.selectedDataSet = undefined;
  }

  getDataSets() {
    this.logger.info('Loading datasets');

    this.dataSetObservable = this.dataService.getDataSets();
  }

  loadDataSet(dataSetName: String) {
    this.logger.info('Loading data set: ' + dataSetName);

    this.rowObservable = this.dataService.getData(undefined);
  }

  onSelectDataSet(dataSet: DataSet) {
    this.logger.info('Data Set selected: ' + dataSet.name);

    this.selectedDataSet = dataSet;
  }
}
