import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DataComponent} from './data.component';
import {InfiniteScrollModule} from 'ngx-infinite-scroll';
import {DataService} from '../../services/data.service';
import {HttpClientModule} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {HttpModule} from '@angular/http';
import {logging} from '../../app.logging';

describe('DataComponent', () => {
  let component: DataComponent;
  let fixture: ComponentFixture<DataComponent>;
  let dataService: DataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataComponent ],
      imports: [
        InfiniteScrollModule,
        HttpClientModule,
        HttpModule,
        logging
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
