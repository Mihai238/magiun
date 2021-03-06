import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HistogramSettingsComponent} from './histogram-settings.component';
import {FormsModule} from '@angular/forms';
import {DataService} from '../../../../services/data.service';
import {HttpClientModule} from '@angular/common/http';
import {logging} from '../../../../app.logging';
import {StubColumnSelectorComponent} from '../../../../../testing/stub-column-selector.component';
import {translate} from '../../../../app.translate';

describe('HistogramSettingsComponent', () => {
  let component: HistogramSettingsComponent;
  let fixture: ComponentFixture<HistogramSettingsComponent>;
  let dataService: DataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        HistogramSettingsComponent,
        StubColumnSelectorComponent
      ],
      imports: [
        FormsModule,
        HttpClientModule,
        logging,
        translate
      ],
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
