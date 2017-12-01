import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistogramSettingsComponent } from './histogram-settings.component';
import {FormsModule} from '@angular/forms';
import {DataService} from '../../../../services/data.service';
import {HttpClientModule} from '@angular/common/http';
import {logging} from '../../../../app.logging';

describe('HistogramSettingsComponent', () => {
  let component: HistogramSettingsComponent;
  let fixture: ComponentFixture<HistogramSettingsComponent>;
  let dataService: DataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistogramSettingsComponent ],
      imports: [
        FormsModule,
        HttpClientModule,
        logging],
      providers: [
        DataService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistogramSettingsComponent);
    component = fixture.componentInstance;


    dataService = fixture.debugElement.injector.get(DataService);
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
