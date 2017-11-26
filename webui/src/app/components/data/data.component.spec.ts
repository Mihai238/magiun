import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DataComponent} from './data.component';
import {InfiniteScrollModule} from 'ngx-infinite-scroll';
import {DataService} from '../../services/data.service';
import {HttpClientModule} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {logging} from '../../app.logging';
import 'rxjs/add/observable/of';
import {TranslateModule} from '@ngx-translate/core';
import {Component} from '@angular/core';

describe('DataComponent', () => {
  let component: DataComponent;
  let fixture: ComponentFixture<DataComponent>;
  let dataService: DataService;

  @Component({
    selector: 'app-chart',
    template: ''
  })
  class ChartStubComponent {}

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DataComponent, ChartStubComponent],
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
      {id: '1', name: 'people', schema: null},
      {id: '2', name: 'plants', schema: null}
    ]));

    fixture.detectChanges();
    expect(component.dataSets.length).toBe(2);
  });
});
