import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScatterSettingsComponent } from './scatter-settings.component';
import {DataService} from '../../../../services/data.service';
import {HttpClientModule} from '@angular/common/http';
import {logging} from '../../../../app.logging';

describe('ScatterSettingsComponent', () => {
  let component: ScatterSettingsComponent;
  let fixture: ComponentFixture<ScatterSettingsComponent>;
  let dataService: DataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScatterSettingsComponent ],
      imports: [
        HttpClientModule,
        logging
      ],
      providers: [
        DataService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScatterSettingsComponent);
    component = fixture.componentInstance;

    dataService = fixture.debugElement.injector.get(DataService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
