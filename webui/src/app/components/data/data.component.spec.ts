import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DataComponent} from './data.component';
import {DataService} from '../../services/data.service';
import {HttpClientModule} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {logging} from '../../app.logging';
import 'rxjs/add/observable/of';
import {TranslateModule} from '@ngx-translate/core';
import {Component, Input} from '@angular/core';
import {Column, DataSet} from '../../model/data-set.model';
import {RowCallback} from '../shared/table';

describe('DataComponent', () => {
  let component: DataComponent;
  let fixture: ComponentFixture<DataComponent>;
  let dataService: DataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        DataComponent,
        ChartStubComponent,
        NewColumnSettingsStubComponent,
        ProcessFeatureStubComponent,
        DataTableStubComponent,
        TableColumnStubComponent
      ],
      imports: [
        HttpClientModule,
        logging,
        TranslateModule.forRoot()
      ],
      providers: [
        DataService
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataComponent);
    component = fixture.componentInstance;

    dataService = fixture.debugElement.injector.get(DataService);
  });

  xit('should be created', () => {
    spyOn(dataService, 'getDataSets')
      .and.returnValue(Observable.of([
      {
        id: '1',
        name: 'people',
        schema: {
          columns: []
        }
      },
      {
        id: '2',
        name: 'plants',
        schema: {
          columns: []
        }
      }
    ]));

    spyOn(dataService, 'getDataForTable')
      .and.returnValue(Promise.resolve({}));

    fixture.detectChanges();
    expect(component.dataSets.length).toBe(2);
  });
});

@Component({
  selector: 'app-chart',
  template: ''
})
class ChartStubComponent {
  @Input() dataSet: DataSet;
}

@Component({
  selector: 'data-add-column-settings',
  template: ''
})
class NewColumnSettingsStubComponent {
  @Input() visible: boolean;
  @Input() index: number;
}

@Component({
  selector: 'data-edit-column',
  template: ''
})
class ProcessFeatureStubComponent {
  @Input() visible: boolean;
  @Input() column: Column;
}

@Component({
  selector: 'data-table',
  template: ''
})
class DataTableStubComponent {
  @Input() headerTitle: string;
  @Input() items;
  @Input() itemCount: number;
  @Input() rowTooltip: RowCallback;
  @Input() pagination_limit = false;
  @Input() pagination_input = true;
  @Input() pagination_numbers = false;
}

@Component({
  selector: 'data-table-column',
  template: ''
})
class TableColumnStubComponent {
  @Input() property: string;
  @Input() header: string;
  @Input() visible = true;
}
