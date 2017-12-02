import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScatterSettingsComponent } from './scatter-settings.component';
import {DataService} from '../../../../services/data.service';
import {HttpClientModule} from '@angular/common/http';
import {logging} from '../../../../app.logging';
import {StubColumnSelectorComponent} from '../../../../../testing/stub-column-selector.component';

describe('ScatterSettingsComponent', () => {
  let component: ScatterSettingsComponent;
  let fixture: ComponentFixture<ScatterSettingsComponent>;
  let dataService: DataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ScatterSettingsComponent,
        StubColumnSelectorComponent
      ],
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
