///<reference path="../model/data-set.model.ts"/>
import {Injectable} from '@angular/core';
import {DataRow} from '../model/data-row.model';
import {environment} from '../../environments/environment';
import {NGXLogger} from 'ngx-logger';
import {Observable} from 'rxjs/Observable';

import 'rxjs/add/observable/throw';
import {ColumnType, DataSet, Schema} from '../model/data-set.model';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class DataService {

  sizePerPage = 100;

  constructor(private http: HttpClient,
              private logger: NGXLogger) {
  }

  getDataSets(): Observable<DataSet[]> {
    return this.http.get(environment.baseUrl + '/datasets/')
      .map((resp: DataSet[]) => {
        this.logger.debug('Got all data sets' + resp);

        return resp.map(e => ({
          id: e.id,
          name: e.name,
          schema: this.mapSchema(e.schema)
        }));
      })
      .catch((error: any) => {
        if (typeof error.json === 'function') {
          return Observable.throw(error.json().error || 'Server error');
        } else {
          return Observable.throw(error.message || 'Server error');
        }
      });
  }

  private mapSchema(schema: any): Schema {
    const columns = schema.columns.map(column => {
      const type = ColumnType[column.type];
      if (type === undefined) {
        throw Error('ColumnType not defined: ' + column.type);
      }

      return {
        index: column.index,
        name: column.name,
        type: ColumnType[column.type]
      };
    });

    return {columns: columns};
  }

  getData(dataSet: DataSet, page = 1): Observable<DataRow[]> {
    return this.http.get(environment.baseUrl + '/datasets/' + dataSet.id + '/rows?_limit=' + this.sizePerPage + '&_page=' + page)
      .map(resp => {
        this.logger.debug('Got data for data set' + resp);
        return resp;
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  // not really wise to fetch all the data if we deal with GBs of data
  // should be refactored in further versions
  getAllData(dataSet: DataSet): Observable<DataRow[]> {
    return this.http.get(environment.baseUrl + '/datasets/' + dataSet.id + '/rows')
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

}
