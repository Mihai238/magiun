import {Injectable} from '@angular/core';
import {DataRow} from '../model/data-row';

@Injectable()
export class DataService {

  getData(): DataRow[] {
    return [new DataRow(0, ['Joe', 3])];
  }

}
