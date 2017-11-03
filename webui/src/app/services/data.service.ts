import {Injectable} from '@angular/core';
import {DataRow} from '../model/data-row';
import {Http} from '@angular/http';
import {environment} from '../../environments/environment';
import {NGXLogger} from 'ngx-logger';
import {Observable} from 'rxjs/Observable';

import 'rxjs/add/observable/throw';

@Injectable()
export class DataService {

  constructor(private http: Http,
              private logger: NGXLogger) {
  }

  getData(): Observable<DataRow> {
    return this.http.get(environment.baseUrl + '/datasets/1/rows')
      .map(resp => {
        this.logger.debug('Got response' +  resp);
        return resp.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

}
