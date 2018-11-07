import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddColumnSettingsComponent } from './add-column-settings.component';
import {ExecutionService} from "../../../services/execution.service";
import {BlockRestService} from "../../../rest/block.rest.service";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {logging} from "../../../app.logging";

describe('AddColumnSettingsComponent', () => {
  let component: AddColumnSettingsComponent;
  let fixture: ComponentFixture<AddColumnSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AddColumnSettingsComponent
      ],
      providers: [
        ExecutionService,
        BlockRestService
      ],
      imports: [
        FormsModule,
        HttpClientModule,
        logging
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddColumnSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
