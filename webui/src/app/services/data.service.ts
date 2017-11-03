import {Injectable} from '@angular/core';
import {DataRow} from '../model/data-row';
import {Http} from '@angular/http';
import {environment} from '../../environments/environment';
import {NGXLogger} from 'ngx-logger';

@Injectable()
export class DataService {

  constructor(private http: Http,
              private logger: NGXLogger) {
  }

  getData(): DataRow[] {
    this.http.get(environment.baseUrl + '/data');

    return [new DataRow(0, ['Joe', 3])];
  }

}
