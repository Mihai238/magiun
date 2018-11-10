///<reference path="../model/data-set.model.ts"/>
import {Injectable} from '@angular/core';
import {DataRow} from '../model/data-row.model';
import {environment} from '../../environments/environment';
import {NGXLogger} from 'ngx-logger';
import {Observable} from 'rxjs/Observable';

import 'rxjs/add/observable/throw';
import {Column, ColumnType, DataSet, Schema} from '../model/data-set.model';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {DataTableParams} from '../components/shared/table';
import {Recommendations} from "../model/recommendations";

@Injectable()
export class DataService {

  private readonly sizePerPage = 20;
  private readonly dataSetsPath = '/datasets/';

  constructor(private http: HttpClient,
              private logger: NGXLogger) {
  }

  getDataSet(id: String): Observable<DataSet> {
    return this.http.get(environment.baseUrl + this.dataSetsPath + id)
      .map((resp: DataSet) => {
        this.logger.info('Got data set' + resp);

        return new DataSet(resp.id, resp.name, this.mapSchema(resp.schema));
      })
      .catch((error: any) => {
        if (typeof error.json === 'function') {
          return Observable.throw(error.json().error || 'Server error');
        } else {
          return Observable.throw(error.message || 'Server error');
        }
      });
  }

  getDataSets(): Observable<DataSet[]> {
    return this.http.get(environment.baseUrl + this.dataSetsPath)
      .map((resp: DataSet[]) => {
        this.logger.debug('Got all data sets' + resp);

        return resp.map(e => ( new DataSet(e.id, e.name, this.mapSchema(e.schema))));
      })
      .catch((error: any) => {
        if (typeof error.json === 'function') {
          return Observable.throw(error.json().error || 'Server error');
        } else {
          return Observable.throw(error.message || 'Server error');
        }
      });
  }

  private mapSchema(schema: Schema): Schema {
    const columns: Column[] = schema.columns.map(column => {
      const type = ColumnType[column.type];
      if (type === undefined) {
        throw Error('ColumnType not defined: ' + column.type);
      }

      return new Column(column.index, column.name, ColumnType[column.type])

    });

    return new Schema(columns, schema.totalCount);
  }

  getData(dataSet: DataSet, page = 1): Observable<DataRow[]> {
    return this.http.get(environment.baseUrl + this.dataSetsPath + dataSet.id + '/rows?_limit=' + this.sizePerPage + '&_page=' + page)
      .map(resp => {
        this.logger.debug('Got data for data set' + resp);
        return resp;
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  // not really wise to fetch all the data if we deal with GBs of data
  // should be refactored in further versions
  getAllData(dataSet: DataSet, columns: string[]): Observable<DataRow[]> {
    let columnsString: string = "";
    columns.forEach(col => columnsString += col + ",");
    columnsString = columnsString.substring(0, columnsString.length - 1);

    this.logger.info('DataService: load all data for dataset with id "' + dataSet.id + '" and column(s) "' + columnsString + '"');

    return this.http.get(environment.baseUrl + this.dataSetsPath + dataSet.id + '/rows' + '?_columns=' + columnsString)
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  getDataForTable(dataSet: DataSet, params: DataTableParams): Promise<{ items: DataRow[] | null; count: number }> {
    let queryString = this.paramsToQueryString(params);
    this.logger.info('DataSerice: get data for table with queryString ' + queryString);

    return this.http.get(environment.baseUrl + this.dataSetsPath + dataSet.id + '/rows?' + queryString, {observe: 'response'}).toPromise()
      .then((resp: HttpResponse<DataRow[]>) => {
        const totalCount = resp.headers.get('X-Total-Count');

        return {
          items: resp.body,
          count: Number(totalCount)
        };
      });
  }

  private paramsToQueryString(params: DataTableParams): string {
    const result = [];

    if (params.offset != null) {
      const page = (params.offset / params.limit) + 1;
      result.push(['_page', page]);
    }
    if (params.limit != null) {
      result.push(['_limit', params.limit]);
    }

    return result.map(param => param.join('=')).join('&');
  }

  getRecommendations(dataSet: DataSet): Observable<Recommendations> {
    return this.http.get(environment.baseUrl + this.dataSetsPath + dataSet.id + '/recommendations')
      .map((resp: Recommendations) => {
        this.logger.info("Got recommendation for dataset " + dataSet.id);
        return resp;
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

}
