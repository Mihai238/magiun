import {Component, OnInit} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {DataService} from '../../services/data.service';
import {DataRow} from '../../model/data-row';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css'],
  providers: [NGXLogger]
})
export class DataComponent implements OnInit {

  rowObservable: Observable<DataRow>;

  constructor(private logger: NGXLogger,
              private dataService: DataService) { }

  ngOnInit() {
  }

  loadDataSet(dataSetName: String) {
    this.logger.info('Loading data set: ' + dataSetName);

    this.rowObservable = this.dataService.getData();
  }
}
