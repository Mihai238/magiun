import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BarSettingsComponent} from './bar-settings.component';
import {DataService} from "../../../../services/data.service";
import {HttpClientModule} from "@angular/common/http";
import {logging} from "../../../../app.logging";
import {StubColumnSelectorComponent} from "../../../../../testing/stub-column-selector.component";
import {translate} from "../../../../app.translate";

describe('BarSettingsComponent', () => {
  let component: BarSettingsComponent;
  let fixture: ComponentFixture<BarSettingsComponent>;
  let dataService: DataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        BarSettingsComponent,
        StubColumnSelectorComponent
      ],
      providers: [
        DataService
      ],
      imports: [
        HttpClientModule,
        logging,
        translate
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BarSettingsComponent);
    component = fixture.componentInstance;

    dataService = fixture.debugElement.injector.get(DataService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
