import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BarSettingsComponent } from './bar-settings.component';
import {DataService} from "../../../../services/data.service";
import {HttpClientModule} from "@angular/common/http";
import {logging} from "../../../../app.logging";

describe('BarSettingsComponent', () => {
  let component: BarSettingsComponent;
  let fixture: ComponentFixture<BarSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        BarSettingsComponent
      ],
      providers: [
        DataService
      ],
      imports: [
        HttpClientModule,
        logging
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BarSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
