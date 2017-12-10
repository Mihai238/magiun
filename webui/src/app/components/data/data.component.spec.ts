import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DataComponent} from './data.component';
import {InfiniteScrollModule} from 'ngx-infinite-scroll';
import {DataService} from '../../services/data.service';
import {HttpClientModule} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {logging} from '../../app.logging';
import 'rxjs/add/observable/of';
import {TranslateModule} from '@ngx-translate/core';
import {Component, Input} from '@angular/core';
import {DataSet} from '../../model/data-set';

describe('DataComponent', () => {
  let component: DataComponent;
  let fixture: ComponentFixture<DataComponent>;
  let dataService: DataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        DataComponent,
        ChartStubComponent,
        NewColumnSettingsStubComponent
      ],
      imports: [
        InfiniteScrollModule,
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

  it('should be created', () => {
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
  selector: 'data-new-column-settings',
  template: ''
})
class NewColumnSettingsStubComponent {
  @Input() visible: boolean;
  @Input() index: number;
}
