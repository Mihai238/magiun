///<reference path="../model/data-set.ts"/>
import {Injectable} from '@angular/core';
import {DataRow} from '../model/data-row';
import {Http} from '@angular/http';
import {environment} from '../../environments/environment';
import {NGXLogger} from 'ngx-logger';
import {Observable} from 'rxjs/Observable';

import 'rxjs/add/observable/throw';
import {DataSet} from '../model/data-set';

@Injectable()
export class DataService {

  constructor(private http: Http,
              private logger: NGXLogger) {
  }

  getDataSets(): Observable<DataSet[]> {
    return this.http.get(environment.baseUrl + '/datasets/')
      .map(resp => resp.json())
      .map(resp => {
        this.logger.debug('Got all data sets' + resp);

        return resp.map(e => ({
          id: e.id,
          name: e.name
        }));
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  getData(dataSet: DataSet): Observable<DataRow[]> {
    return this.http.get(environment.baseUrl + '/datasets/' +  dataSet.id + '/rows')
      .map(resp => {
        this.logger.debug('Got data for data set' +  resp);
        return resp.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

}
